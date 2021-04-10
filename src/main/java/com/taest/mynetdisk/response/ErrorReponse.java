package com.taest.mynetdisk.response;

import com.taest.mynetdisk.exception.BaseException;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.ObjectUtils;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * @author twd
 * @description: err rep
 * @date 2021-01-12 15:12
 */
@Getter
@ToString
public class ErrorReponse {
    private int code;
    private int status;
    private String message;
    private String path;
    private Instant timestamp;
    private HashMap<String, Object> data = new HashMap<String, Object>();

    public ErrorReponse() {
    }

    public ErrorReponse(BaseException ex, String path) {
        this(ex.getError().getCode(), ex.getError().getStatus().value(), ex.getError().getMessage(), path, ex.getData());
    }

    public ErrorReponse(int code, int status, String message, String path, Map<String, Object> data) {
        this.code = code;
        this.status = status;
        this.message = message;
        this.path = path;
        this.timestamp = Instant.now();
        if (!ObjectUtils.isEmpty(data)) {
            this.data.putAll(data);
        }
    }

}
