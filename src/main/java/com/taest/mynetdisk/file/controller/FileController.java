package com.taest.mynetdisk.file.controller;


import com.taest.mynetdisk.dto.FileDto;
import com.taest.mynetdisk.file.entity.MyFile;
import com.taest.mynetdisk.file.service.IFileService;
import com.taest.mynetdisk.response.BaseController;
import com.taest.mynetdisk.response.Result;
import com.taest.mynetdisk.util.UuidUtil;
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
    @GetMapping("/files/{parentId}")
    public Result list(@PathVariable("parentId") String parentId){
        List<MyFile> list = fileService.queryByParentId(parentId);
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
            @RequestParam(value = "folderId",required = false,defaultValue = "") String folderId,
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
        fileDto.setFolderId(folderId);
        fileDto.setFile(uploadFile);
        return fileService.uploadFile(fileDto);
    }

//    @ApiOperation(value = "分片检查",notes = "文件上传之前的分片检查")
//    @GetMapping("/upload")
//    public Object check(
//            @RequestParam("relativePath") String path,
//            @RequestParam("filename") String name,
//            @RequestParam("parentId") String parentId,
//            @RequestParam("fileType") String fileType,
//            @RequestParam("totalSize") Integer fileSize,
//            @RequestParam("chunkNumber") Integer shardIndex,
//            @RequestParam("chunkSize") Integer shardSize,
//            @RequestParam("totalChunks") Integer shardTotal,
//            @RequestParam("identifier") String key){
//        FileDto fileDto = new FileDto();
//        fileDto.setPath(path);
//        fileDto.setName(name);
//        fileDto.setParentId(parentId);
//        fileDto.setFileType(fileType);
//        fileDto.setFileSize(fileSize);
//        fileDto.setShardIndex(shardIndex);
//        fileDto.setShardSize(shardSize);
//        fileDto.setShardTotal(shardTotal);
//        fileDto.setFileKey(key);
//        return fileService.checkFile(fileDto);
//    }
    @ApiOperation(value = "文件删除",notes = "删除选择的文件")
    @DeleteMapping("/delFile/{id}")
    public Result delFile(@PathVariable("id") String id){
        return fileService.deleteFile(id);
    }

    @ApiOperation(value = "创建文件夹",notes = "创建文件夹")
    @PostMapping("/mkdir")
    public Result mkdir(@RequestParam("parentId") String parentId,
                        @RequestParam("filename") String dirName){
        return fileService.mkdir(parentId,dirName);
    }
}
