package com.taest.mynetdisk.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class FileDto {

    /**
     * uid
     */
    private String uid;

    /**
     * 路径
     */
    private String path;

    /**
     * 父id
     */
    private String parentId;

    /**
     * 文件名
     */
    private String name;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 大小|字节B
     */
    private Integer fileSize;

    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createdAt;

    /**
     * 修改时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updatedAt;

    /**
     * 已上传分片
     */
    private Integer shardIndex;

    /**
     * 分片大小|B
     */
    private Integer shardSize;

    /**
     * 分片总数
     */
    private Integer shardTotal;

    /**
     * 文件标识|MD5
     */
    private String fileKey;

    /**
     * base64
     */
    private String shard;

    /**
     * file
     */
    private MultipartFile file;



}