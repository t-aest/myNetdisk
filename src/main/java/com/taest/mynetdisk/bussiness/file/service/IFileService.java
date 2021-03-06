package com.taest.mynetdisk.bussiness.file.service;

import com.taest.mynetdisk.bussiness.file.entity.MyFile;
import com.taest.mynetdisk.dto.FileDto;
import com.taest.mynetdisk.dto.FolderDto;
import com.taest.mynetdisk.response.Result;

import javax.servlet.http.HttpServletResponse;
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

    List<FolderDto> listFolder();

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

    Result rename(String fileId, String filename);

    Result moveOrCopy(String fileId, String targetFileId,Integer operFlag);

    Object download(String id, HttpServletResponse response);
}
