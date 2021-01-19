package com.taest.mynetdisk.file.entity;

import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 文件
 * </p>
 *
 * @author jobob
 * @since 2021-01-11
 */
@Data
public class MyFile {

    private static final long serialVersionUID = 1L;

    private String id;

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
    private Date createdAt;

    /**
     * 修改时间
     */
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
    @TableField(value = "`key`")
    private String key;



}
