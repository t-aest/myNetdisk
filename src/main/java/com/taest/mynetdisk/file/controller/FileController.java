package com.taest.mynetdisk.file.controller;


import com.taest.mynetdisk.dto.FileDto;
import com.taest.mynetdisk.file.entity.MyFile;
import com.taest.mynetdisk.file.service.IFileService;
import com.taest.mynetdisk.response.BaseController;
import com.taest.mynetdisk.response.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 文件 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2021-01-11
 */
@RestController
@Api(tags = "文件管理")
@RequestMapping("file")
public class FileController extends BaseController {

    @Autowired
    private IFileService fileService;

    @ApiOperation(value = "文件列表",notes = "文件列表查询")
    @GetMapping("/files")
    public Result list(){
        List<MyFile> list = fileService.list();
        return success(list);
    }

    @ApiOperation(value = "文件上传",notes = "文件上传")
    @PostMapping("/upload")
    public Object upload(
            @RequestParam("relativePath") String path,
            @RequestParam("filename") String name,
            @RequestParam("parentId") String parentId,
            @RequestParam("fileType") String fileType,
            @RequestParam("totalSize") Integer fileSize,
            @RequestParam("chunkNumber") Integer shardIndex,
            @RequestParam("chunkSize") Integer shardSize,
            @RequestParam("totalChunks") Integer shardTotal,
            @RequestParam("identifier") String key,
            @RequestParam("uploadFile") MultipartFile uploadFile){
        FileDto fileDto = new FileDto();
        fileDto.setPath(path);
        fileDto.setName(name);
        fileDto.setParentId(parentId);
        fileDto.setFileType(fileType);
        fileDto.setFileSize(fileSize);
        fileDto.setShardIndex(shardIndex);
        fileDto.setShardSize(shardSize);
        fileDto.setShardTotal(shardTotal);
        fileDto.setFileKey(key);
        fileDto.setFile(uploadFile);
        return fileService.uploadFile(fileDto);
    }



}
