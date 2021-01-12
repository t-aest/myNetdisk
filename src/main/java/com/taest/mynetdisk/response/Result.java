package com.taest.mynetdisk.response;

import lombok.*;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * @author twd
 * @description: TODO 类描述
 * @date 2021-01-12 13:55
 */
@Data
public class Result implements Serializable {

    /**
     * 业务上的成功或失败
     */
    private int code = 0;
    private String message;
    private Object data;

}
