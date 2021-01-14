package com.taest.mynetdisk.dto;

import lombok.Data;

import java.util.List;

/**
 * @author twd
 * @description: TODO 类描述
 * @date 2021-01-14 16:09
 */
@Data
public class PageDto<T> {

    /**
     * 当前页码
     */
    protected int page;

    /**
     * 每页条数
     */
    protected int size;

    /**
     * 总条数
     */
    protected long total;

    protected List<T> list;
}
