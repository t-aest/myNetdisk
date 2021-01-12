package com.taest.mynetdisk.exception;


import com.taest.mynetdisk.response.ResultStatus;

import java.util.Map;

/**
 * @author twd
 * @description: TODO 类描述
 * @date 2021-01-12 17:13
 */
public class FileException extends BaseException{
    public FileException(ResultStatus error, Map<String, Object> data) {
        super(error, data);
    }
}
