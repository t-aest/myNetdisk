package com.taest.mynetdisk.bussiness.file.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taest.mynetdisk.bussiness.file.entity.MyFile;
import com.taest.mynetdisk.bussiness.file.mapper.FileMapper;
import com.taest.mynetdisk.bussiness.file.service.IFileService;
import com.taest.mynetdisk.dto.CommandResult;
import com.taest.mynetdisk.dto.FileDto;
import com.taest.mynetdisk.dto.FolderDto;
import com.taest.mynetdisk.exception.file.FileException;
import com.taest.mynetdisk.response.BaseController;
import com.taest.mynetdisk.response.Result;
import com.taest.mynetdisk.response.ResultStatus;
import com.taest.mynetdisk.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * <p>
 * 文件 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2021-01-11
 */
@Service
public class FileServiceImpl extends BaseController implements IFileService {

    private static final Logger LOG = LoggerFactory.getLogger(FileServiceImpl.class);

    private static Map<String, MyFile> parentPathMap = new HashMap<>();

    @Value("${file.path}")
    private String FILE_PATH;

    @Autowired
    private FileMapper fileMapper;

    @Override
    public List<FolderDto> listFolder() {
        QueryWrapper<MyFile> wrapper = new QueryWrapper<>();
        wrapper.eq("file_type", "folder");
        wrapper.eq("parent_id", String.valueOf(0));
        List<MyFile> myFileList = fileMapper.selectList(wrapper);
        List<FolderDto> folderDtos = new ArrayList<>();
        if (!MyCollectionUtils.isEmpty(myFileList)){
            folderDtos = myFileList.stream().map(myFile -> {
                FolderDto folderDto = new FolderDto();
                folderDto.setId(myFile.getId());
                folderDto.setParentId(myFile.getParentId());
                folderDto.setName(myFile.getName());
                findChild(folderDto);
                return folderDto;
            }).collect(Collectors.toList());
        }
        return folderDtos;
    }

    public void findChild(FolderDto folderDto){
        List<FolderDto> folderDtos = new ArrayList<>();
        QueryWrapper<MyFile> wrapper = new QueryWrapper<>();
        wrapper.eq("file_type", "folder");
        wrapper.eq("parent_id", folderDto.getId());
        List<MyFile> myFileList = fileMapper.selectList(wrapper);
        if (!MyCollectionUtils.isEmpty(myFileList)){
            folderDtos = myFileList.stream().map(myFile -> {
                FolderDto dto = new FolderDto();
                dto.setId(myFile.getId());
                dto.setParentId(myFile.getParentId());
                dto.setName(myFile.getName());
                findChild(dto);
                return dto;
            }).collect(Collectors.toList());
            folderDto.setChildren(folderDtos);
        }
    }

    @Override
    public List<MyFile> queryByParentId(String parentId) {
        QueryWrapper<MyFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", parentId);
        List<MyFile> myFileList = fileMapper.selectList(queryWrapper);
        myFileList = myFileList
                .stream()
                .filter(myFile -> myFile.getShardIndex() == null || myFile.getShardIndex().equals(myFile.getShardTotal()))
                .collect(Collectors.toList());
        return myFileList;
    }

    @Override
    @Transactional
    public Result uploadFile(FileDto fileDto) {
        LOG.info("上传文件开始");
        String key = fileDto.getFileKey();
        String type = fileDto.getFileType();
        String path = fileDto.getPath();
        String folderId = fileDto.getFolderId();
        MyFile resultFile = null;
        try {
            MultipartFile shard = fileDto.getFile();
            //如果文件夹不存在则创建
            String loaclpath = new StringBuffer(path)
                    .append(".")
                    .append(fileDto.getShardIndex())
                    .toString();
            String fullPath = FILE_PATH + loaclpath;
            resultFile = getMyFile(fileDto, folderId, resultFile, fullPath);
            File dest = new File(fullPath);
            shard.transferTo(dest);
            LOG.info(dest.getAbsolutePath());
            LOG.info("保存文件记录开始");
            this.save(fileDto);
            if (fileDto.getShardIndex().equals(fileDto.getShardTotal())) {
                this.merge(fileDto);
            }
            if (MyObjectUtils.isEmpty(resultFile)) {
                resultFile = this.selectByKey(fileDto.getFileKey());
            }
            return success(resultFile);
        } catch (Exception e) {
            throw new FileException(ResultStatus.UPLOAD_FAILED, null);
        }
    }

    private MyFile getMyFile(FileDto fileDto, String folderId, MyFile resultFile, String fullPath) {
        if (MyStringUtils.isNotEmpty(folderId)) {
            if (parentPathMap.containsKey(folderId)) {
                resultFile = parentPathMap.get(folderId);
                fileDto.setParentId(resultFile.getId());
            } else {
                File file = new File(fullPath).getParentFile();
                if (!file.exists()) {
                    Result result = mkdir(fileDto.getParentId(), file.getName());
                    if (result.getCode() == 0) {
                        ObjectMapper mapper = new ObjectMapper();
                        MyFile dirFile = mapper.convertValue(result.getData(), MyFile.class);
                        parentPathMap.put(folderId, dirFile);
                        resultFile = dirFile;
                        fileDto.setParentId(resultFile.getId());
                    }
                }
            }
        }
        return resultFile;
    }


    public void merge(FileDto fileDto) throws Exception {
        LOG.info("合并分片开始");
        String path = fileDto.getPath();
        Integer shardTotal = fileDto.getShardTotal();
        File newFile = new File(FILE_PATH + path);
        FileOutputStream outputStream = new FileOutputStream(newFile, true);//文件追加写入
        FileInputStream fileInputStream = null;//分片文件
        byte[] byt = new byte[10 * 1024 * 1024];
        int len;

        try {
            for (int i = 0; i < shardTotal; i++) {
                // 读取第i个分片
                fileInputStream = new FileInputStream(new File(FILE_PATH + path + "." + (i + 1))); //  course\6sfSqfOwzmik4A4icMYuUe.mp4.1
                while ((len = fileInputStream.read(byt)) != -1) {
                    outputStream.write(byt, 0, len);
                }
            }
        } catch (IOException e) {
            LOG.error("分片合并异常", e);
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                outputStream.close();
                LOG.info("IO流关闭");
            } catch (Exception e) {
                LOG.error("IO流关闭", e);
            }
        }
        LOG.info("合并分片结束");

        System.gc();
        Thread.sleep(100);

        // 删除分片
        LOG.info("删除分片开始");
        for (int i = 0; i < shardTotal; i++) {
            String filePath = FILE_PATH + path + "." + (i + 1);
            File file = new File(filePath);
            boolean result = file.delete();
            LOG.info("删除{}，{}", filePath, result ? "成功" : "失败");
        }
        LOG.info("删除分片结束");
    }

    @Override
    public Result checkFile(FileDto fileDto) {
        LOG.info("上传检查开始");
        String key = fileDto.getFileKey();
        String type = fileDto.getFileType();
        String path = fileDto.getPath();
        QueryWrapper<MyFile> wrapper = new QueryWrapper<>();
        wrapper.eq("file_key", fileDto.getFileKey());
        wrapper.eq("name", fileDto.getName());
        wrapper.eq("path", fileDto.getPath());
        MyFile myFile = fileMapper.selectOne(wrapper);
        if (MyObjectUtils.isEmpty(myFile)) {
            return success();
        } else if (myFile.getShardIndex().equals(myFile.getShardTotal())) {
            return success(ResultStatus.FILE_SECOND_PASS);
        } else {
            return success(ResultStatus.FILE_BREAKPOINT_UPLOAD);
        }
    }

    @Override
    @Transactional
    public void save(FileDto fileDto) {
        MyFile file = CopyUtil.copy(fileDto, MyFile.class);
        MyFile myFile = selectByKey(fileDto.getFileKey());
        if (myFile == null) {
            this.insert(file);
        } else if (myFile.getPath().equals(fileDto.getPath())) {
            myFile.setShardIndex(fileDto.getShardIndex());
            this.update(myFile);
        } else {
            this.insert(file);
        }
    }

    @Override
    public MyFile selectByKey(String key) {
        QueryWrapper<MyFile> wrapper = new QueryWrapper<>();
        wrapper.eq("file_key", key);
        List<MyFile> fileList = fileMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(fileList)) {
            return null;
        } else {
            return fileList.get(0);
        }
    }

    @Override
    public void insert(MyFile file) {
        Date now = new Date();
        file.setCreatedAt(now);
        file.setUpdatedAt(now);
        file.setId(UuidUtil.getShortUuid());
        fileMapper.insert(file);
    }

    @Override
    public void update(MyFile file) {
        file.setUpdatedAt(new Date());
        fileMapper.updateById(file);
    }

    @Override
    public void delete(String id) {
        fileMapper.deleteById(id);
    }

    @Override
    public Result deleteFile(String id) {
        MyFile myFile = selectFileById(id);
        if (StringUtils.checkValNull(myFile)) {
            return failure(ResultStatus.FILE_NOT_EXIST);
        }
        String path = myFile.getPath();
        String dirPath = FILE_PATH + path;
        File delFile = new File(dirPath);
        boolean flag = delFile.delete();
        if (path.indexOf("/") > 0) {
            File delParentFile = new File(dirPath).getParentFile();
            delParentFile.delete();
        }
        if (flag) {
            delete(id);
            return success(myFile);
        } else {
            return failure(ResultStatus.DELETE_FILE_ERROR);
        }
    }

    private MyFile selectFileById(String id) {
        QueryWrapper<MyFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        return fileMapper.selectOne(queryWrapper);
    }

    @Override
    public Result mkdir(String parentId, String dirName) {
        FileDto fileDto = new FileDto();
        String fileKey = UuidUtil.getUuid();
        if (String.valueOf(0).equals(parentId)) {
            fileDto.setPath(File.separator + dirName);
        }else {
            QueryWrapper<MyFile> wrapper = new QueryWrapper<>();
            wrapper.eq("id",parentId);
            MyFile parentFile = fileMapper.selectOne(wrapper);
            fileDto.setPath(parentFile.getPath());
        }
        fileDto.setParentId(parentId);
        fileDto.setName(dirName);
        fileDto.setFileKey(fileKey);
        fileDto.setFileType("folder");
        save(fileDto);
        String dir_name = FILE_PATH + File.separator + dirName;
        File driFile = new File(dir_name);
        boolean mkdir = driFile.mkdir();
        if (mkdir) {
            MyFile myFile = selectByKey(fileKey);
            return success(myFile);
        } else {
            return failure(ResultStatus.REQUEST_VALIDATION_FAILED);
        }
    }

    @Override
    @Transactional
    public Result rename(String fileId, String filename) {
        MyFile selectFileById = selectFileById(fileId);
        if (MyObjectUtils.isEmpty(selectFileById)) {
            return failure(ResultStatus.FILE_NOT_EXIST);
        }
        String old_db_path = selectFileById.getPath();
        int i = old_db_path.lastIndexOf("/");
        old_db_path = old_db_path.substring(0, i);
        String old_path = FILE_PATH + selectFileById.getPath();
        File rename_file = new File(old_path);
        String new_path = rename_file.getParent() + File.separator + filename;
        String cmd = "mv " + old_path + " " + new_path;
        CommandResult commandResult = CommandUtils.exec(cmd);
        selectFileById.setName(filename);
        selectFileById.setPath(old_db_path + File.separator + filename);
        int updateById = fileMapper.updateById(selectFileById);
        if (updateById != 0 && commandResult.isSuccess()) {
            return success(selectFileById);
        } else {
            return failure(ResultStatus.RENAME_ERROR);
        }
    }

    @Override
    public Result moveOrCopy(String fileId, String targetFileId, Integer operFlag) {
        MyFile file = selectFileById(fileId);
        MyFile targetFile = selectFileById(targetFileId);
        CommandResult commandResult = null;
        if (MyObjectUtils.isEmpty(file) ||MyObjectUtils.isEmpty(targetFile)) {
            return failure(ResultStatus.FILE_NOT_EXIST);
        }
        try {
            String modifyPath = FILE_PATH + targetFile.getPath();
            if ("folder".equals(file.getFileType())){
                QueryWrapper<MyFile> wrapper = new QueryWrapper<>();
                wrapper.eq("parent_id", fileId);
                List<MyFile> myFiles = fileMapper.selectList(wrapper);
                if (MyCollectionUtils.isEmpty(myFiles)){
                    return failure(ResultStatus.FILE_NOT_EXIST);
                }
                for (MyFile myFile : myFiles) {
                    commandResult = this.moveOrCopyResult(myFile,targetFile,operFlag,modifyPath);
                }
            }else {
                commandResult = this.moveOrCopyResult(file,targetFile,operFlag,modifyPath);
            }
            if (commandResult.isSuccess()) {
                return success();
            } else {
                return failure(ResultStatus.MOVE_OR_COPY_ERROR);
            }
        }catch (Exception e){
            throw new FileException(ResultStatus.MOVE_OR_COPY_ERROR,null);
        }
    }
    public CommandResult moveOrCopyResult(MyFile file,MyFile targetFile,Integer operFlag,String modifyPath){
        String oldPath = FILE_PATH + file.getPath();
        CommandResult commandResult = null;
        if (operFlag==0){
            //移动到
            file.setPath(targetFile.getPath() + File.separator + file.getName());
            file.setParentId(targetFile.getId());
            this.update(file);
            String moveCmd = "mv " + oldPath + " " + modifyPath;
            commandResult = CommandUtils.exec(moveCmd);
        }
        if (operFlag==1){
            //复制到
            file.setPath(targetFile.getPath() + File.separator + file.getName());
            file.setParentId(targetFile.getId());
            this.insert(file);
            String copyCmd = "cp " + oldPath + " " + modifyPath;
            commandResult = CommandUtils.exec(copyCmd);
        }
        return commandResult;
    }

    @Override
    public Object download(String id, HttpServletResponse response) {
        //TODO 文件夹、多文件打包下载
        MyFile myFile = selectFileById(id);
        String downloadPath = FILE_PATH + myFile.getPath();
        File downloadFile = new File(downloadPath);
        if (!downloadFile.exists()) {
            return failure(ResultStatus.FILE_NOT_EXIST);
        }
        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/octet-stream");
        if (downloadFile.isDirectory()){
            // 对文件名进行编码处理中文问题
            // inline在浏览器中直接显示，不提示用户下载
            // attachment弹出对话框，提示用户进行下载保存本地
            // 默认为inline方式
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(downloadFile.getName().getBytes(StandardCharsets.UTF_8)));
            // 设置成这样可以不用保存在本地，再输出， 通过response流输出,直接输出到客户端浏览器中。
            try {
                ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
                List<String> files = new ArrayList<>();
                File[] listFiles = downloadFile.listFiles();
                for (File listFile : listFiles) {
                    files.add(listFile.getPath());
                }
                if (MyCollectionUtils.isEmpty(files)){
                    throw new FileException(ResultStatus.DOWNLOAD_FAILED,null);
                }
                zipFiles(files,zos,"");
                zos.closeEntry();
                zos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else {
            response.setContentLength((int) downloadFile.length());
            response.setHeader("Content-Disposition", "attachment;filename=" + downloadFile.getName());
            try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(downloadFile));) {
                byte[] buff = new byte[1024];
                OutputStream os = response.getOutputStream();
                int i = 0;
                while ((i = bis.read(buff)) != -1) {
                    os.write(buff, 0, i);
                    os.flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        return null;
    }

    /**
     * 压缩文件
     * @param files  压缩的文件的路径
     * @param zos
     */
    private void zipFiles(List<String> files, ZipOutputStream zos,String baseDir) {
        byte[] buffer = new byte[4096];
        try {
            for (String file : files) {
                File zip_file = new File(file);
                if (!zip_file.exists()){
                    throw new FileException(ResultStatus.DOWNLOAD_FAILED,null);
                }
                if (zip_file.isFile()){
                    String baseFileDir = baseDir + "";
                    //创建输入流读取文件
                    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(zip_file));
                    //将文件写入zip内，即将文件进行打包
                    ZipEntry zipEntry = new ZipEntry(baseFileDir + zip_file.getName());
                    zos.putNextEntry(zipEntry);
                    int size = 0;
                    while ((size = bis.read(buffer))>0){
                        zos.write(buffer, 0, size);
                    }
                    bis.close();
                }else {
                    String baseFileDir = baseDir + zip_file.getName();
                    File[] listFiles = zip_file.listFiles();
                    List<String> pathFiles = new ArrayList<>();
                    for (File listFile : listFiles) {
                        pathFiles.add(listFile.getPath());
                    }
                    if (MyCollectionUtils.isEmpty(files)){
                        throw new FileException(ResultStatus.DOWNLOAD_FAILED,null);
                    }
                    zipFiles(pathFiles,zos,baseFileDir + File.separator);
                }
            }
        }catch (Exception e){
            if (e instanceof FileException){
                throw new FileException(ResultStatus.DOWNLOAD_FAILED,null);
            }
            e.printStackTrace();
        }finally {
//            if (null!=zos){
//                try {
//                    zos.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
        }
    }

    public static void main(String[] args) {
        File file = new File("/home/taest/tmpdir");
        File[] files = file.listFiles();
        for (File file1 : files) {
            System.out.println("file1 = " + file1.getPath());
        }
        System.out.println("file = " + file.listFiles());
    }

    @Transactional
    public void deleteTmpRecord(FileDto fileDto) {
        QueryWrapper<MyFile> wrapper = new QueryWrapper<>();
        wrapper.notIn("shard_index", fileDto.getShardTotal()).eq("file_key", fileDto.getFileKey());
        List<MyFile> fileList = fileMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(fileList) && fileList.size() > 1) {
            List<String> ids = fileList.stream().map(MyFile::getId).collect(Collectors.toList());
            fileMapper.deleteBatchIds(ids);
        }
    }


}
