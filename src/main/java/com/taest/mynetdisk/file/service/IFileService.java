package com.taest.mynetdisk.file.service;

import com.taest.mynetdisk.dto.FileDto;
import com.taest.mynetdisk.file.entity.MyFile;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taest.mynetdisk.response.Result;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <p>
 * 文件 服务类
 * </p>
 *
 * @author jobob
 * @since 2021-01-11
 */
public interface IFileService {

    List<MyFile> list();

    List<MyFile> queryByParentId(String parentId);

    Result uploadFile(FileDto fileDto);

    Result checkFile(FileDto fileDto);

    void save(FileDto fileDto);

    MyFile selectByKey(String key);

    void insert(MyFile file);

    void update(MyFile file);

    void delete(String id);

    Result deleteFile(String id);

    Result mkdir(String parentId, String dirName);
}
