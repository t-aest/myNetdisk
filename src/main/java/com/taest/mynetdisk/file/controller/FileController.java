package com.taest.mynetdisk.file.controller;


import com.taest.mynetdisk.dto.FileDto;
import com.taest.mynetdisk.file.entity.MyFile;
import com.taest.mynetdisk.file.service.IFileService;
import com.taest.mynetdisk.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/admin")
public class FileController{

    @Autowired
    private IFileService fileService;

    @GetMapping("/files")
    public Result list(){
        Result result = new Result();
        List<MyFile> list = fileService.list();
        result.setData(list);
        return result;
    }

    @PostMapping("/file")
    public Result upload(FileDto fileDto){
        return fileService.uploadFile(fileDto);
    }



}
