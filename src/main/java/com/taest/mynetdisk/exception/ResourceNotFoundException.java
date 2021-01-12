package com.taest.mynetdisk.exception;


import com.taest.mynetdisk.response.ResultStatus;

import java.util.Map;

/**
 * @author twd
 * @description: TODO 类描述
 * @date 2021-01-12 15:06
 */
public class ResourceNotFoundException extends BaseException{
    public ResourceNotFoundException(ResultStatus error, Map<String, Object> data) {
        super(ResultStatus.RESOURCE_NOT_FOUND, data);
    }
}
