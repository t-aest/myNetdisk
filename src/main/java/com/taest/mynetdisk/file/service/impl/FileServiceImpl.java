package com.taest.mynetdisk.file.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.taest.mynetdisk.dto.FileDto;
import com.taest.mynetdisk.exception.file.FileException;
import com.taest.mynetdisk.file.entity.MyFile;
import com.taest.mynetdisk.file.mapper.FileMapper;
import com.taest.mynetdisk.file.service.IFileService;
import com.taest.mynetdisk.response.BaseController;
import com.taest.mynetdisk.response.Result;
import com.taest.mynetdisk.response.ResultStatus;
import com.taest.mynetdisk.util.CopyUtil;
import com.taest.mynetdisk.util.UuidUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    @Value("${file.path}")
    private String FILE_PATH;

    @Autowired
    private FileMapper fileMapper;

    @Override
    public List<MyFile> list() {
        return fileMapper.selectList(null);
    }

    @Override
    public List<MyFile> queryByParentId(String parentId) {
        if (String.valueOf(0).equals(parentId)){
            return fileMapper.selectList(null);
        }else {
            QueryWrapper<MyFile> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("parent_id", parentId);
            return fileMapper.selectList(queryWrapper);
        }
    }

    @Override
    @Transactional
    public Result uploadFile(FileDto fileDto) {
        LOG.info("上传文件开始");
        String key = fileDto.getFileKey();
        String type =  fileDto.getFileType();
        String path =  fileDto.getPath();
        try {
            MultipartFile shard = fileDto.getFile();
            //如果文件夹不存在则创建

            String loaclpath = new StringBuffer(path)
                    .append(".")
                    .append(fileDto.getShardIndex())
                    .toString();
            String fullPath = FILE_PATH+loaclpath;
            File file = new File(fullPath).getParentFile();
            if (!file.exists()){
                file.mkdir();
            }
            File dest = new File(fullPath);
            shard.transferTo(dest);
            LOG.info(dest.getAbsolutePath());
            LOG.info("保存文件记录开始");
            this.save(fileDto);
            if (fileDto.getShardIndex().equals(fileDto.getShardTotal())){
                this.merge(fileDto);
//                this.deleteTmpRecord(fileDto);
            }
            MyFile myFile = this.selectByKey(fileDto.getFileKey());
            return success(myFile);
        }catch (Exception e){
            throw new FileException(ResultStatus.UPLOAD_FAILED,null);
        }
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
        String type =  fileDto.getFileType();
        String path =  fileDto.getPath();
        QueryWrapper<MyFile> wrapper = new QueryWrapper<>();
        wrapper.eq("file_key", fileDto.getFileKey());
        wrapper.eq("name", fileDto.getName());
        wrapper.eq("path", fileDto.getPath());
        wrapper.eq("shard_index", fileDto.getShardIndex());
        MyFile myFile = fileMapper.selectOne(wrapper);
        if (StringUtils.checkValNull(myFile)) {
            return success();
        } else {
            return failure(ResultStatus.UPLOAD_FILE_CHECK_ERROR);
        }
    }

    @Override
    @Transactional
    public void save(FileDto fileDto) {
        MyFile file = CopyUtil.copy(fileDto, MyFile.class);
        MyFile myFile = selectByKey(fileDto.getFileKey());
        if (myFile == null) {
            this.insert(file);
        } else {
            myFile.setShardIndex(fileDto.getShardIndex());
            this.update(myFile);
        }
    }

    @Override
    public MyFile selectByKey(String key) {
        QueryWrapper<MyFile> wrapper = new QueryWrapper<>();
        wrapper.eq("file_key",key);
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
        QueryWrapper<MyFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        MyFile myFile = fileMapper.selectOne(queryWrapper);
        if (StringUtils.checkValNull(myFile)){
            return failure(ResultStatus.FILE_NOT_EXIST);
        }
        String path = myFile.getPath();
        String dirPath = FILE_PATH + path;
        File delFile = new File(dirPath);
        boolean flag = delFile.delete();
        if (path.indexOf("/")>0){
            File delParentFile = new File(dirPath).getParentFile();
            delParentFile.delete();
        }
        if (flag){
            delete(id);
            return success(myFile);
        }else {
            return failure(ResultStatus.DELETE_FILE_ERROR);
        }
    }

    @Override
    public Result mkdir(String parentId, String dirName) {
        FileDto fileDto = new FileDto();
        String fileKey = UuidUtil.getUuid();
        if (String.valueOf(0).equals(parentId)){
            fileDto.setPath("");
            fileDto.setParentId(parentId);
            fileDto.setFileType("folder");
        }
        fileDto.setName(dirName);
        fileDto.setFileKey(fileKey);
        save(fileDto);
        String dir_name = FILE_PATH + File.separator + dirName;
        File driFile = new File(dir_name);
        boolean mkdir = driFile.mkdir();
        if (mkdir){
            MyFile myFile = selectByKey(fileKey);
            return success(myFile);
        }else {
            return failure(ResultStatus.REQUEST_VALIDATION_FAILED);
        }
    }

    public static void main(String[] args) {
        String a = "sdfasd";
        System.out.println(a.indexOf("/"));
    }
    @Transactional
    public void deleteTmpRecord(FileDto fileDto) {
        QueryWrapper<MyFile> wrapper = new QueryWrapper<>();
        wrapper.notIn("shard_index",fileDto.getShardTotal()).eq("file_key",fileDto.getFileKey());
        List<MyFile> fileList = fileMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(fileList)&&fileList.size()>1){
            List<String> ids = fileList.stream().map(MyFile::getId).collect(Collectors.toList());
            fileMapper.deleteBatchIds(ids);
        }
    }


}
