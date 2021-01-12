package com.taest.mynetdisk.exception;

import com.taest.mynetdisk.response.ResultStatus;
import lombok.Getter;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author twd
 * @description: TODO 类描述
 * @date 2021-01-12 15:03
 */
@Getter
public abstract class BaseException extends RuntimeException {
    private final ResultStatus error;
    private final HashMap<String, Object> data = new HashMap<>();

    public BaseException(ResultStatus error, Map<String, Object> data) {
        super(error.getMessage());
        this.error = error;
        if (!ObjectUtils.isEmpty(data)) {
            this.data.putAll(data);
        }
    }
    protected BaseException(ResultStatus error, Map<String, Object> data, Throwable cause) {
        super(error.getMessage(), cause);
        this.error = error;
        if (!ObjectUtils.isEmpty(data)) {
            this.data.putAll(data);
        }
    }

}