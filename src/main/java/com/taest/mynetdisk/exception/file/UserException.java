package com.taest.mynetdisk.exception.file;

import com.taest.mynetdisk.exception.BaseException;
import com.taest.mynetdisk.response.ResultStatus;

import java.util.Map;

public class UserException extends BaseException {
    public UserException(ResultStatus error, Map<String, Object> data) {
        super(error, data);
    }

}
