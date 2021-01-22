package com.taest.mynetdisk.file.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
    @Transactional
    public Result uploadFile(FileDto fileDto) {
        LOG.info("上传文件开始");

        String key = fileDto.getFileKey();
        String type =  fileDto.getFileType();
        String path =  fileDto.getPath();
        try {
            MultipartFile shard = fileDto.getFile();
            //如果文件夹不存在则创建
            File file = new File(FILE_PATH);
            if (!file.exists()){
                file.mkdir();
            }
            String loaclpath = new StringBuffer(path)
                    .append(".")
                    .append(fileDto.getShardIndex())
                    .toString();
            String fullPath = FILE_PATH+loaclpath;
            File dest = new File(fullPath);
            shard.transferTo(dest);
            LOG.info(dest.getAbsolutePath());
            LOG.info("保存文件记录开始");
            this.save(fileDto);
            if (fileDto.getShardIndex().equals(fileDto.getShardTotal())){
                this.merge(fileDto);
            }
            return success();
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
    public void save(FileDto fileDto) {
        MyFile file = CopyUtil.copy(fileDto, MyFile.class);
//        Wrapper wrapper = new Wrapper();
//        wrapper.
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
}
