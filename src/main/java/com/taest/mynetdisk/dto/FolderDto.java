package com.taest.mynetdisk.dto;

import lombok.Data;

import java.util.List;

@Data
public class FolderDto {

    private String id;
    private String name;
    private String parentId;
    private String icon = "el-icon-folder";
    private List<FolderDto> children;
}
