package com.taest.mynetdisk.handler;


import com.taest.mynetdisk.exception.BaseException;
import com.taest.mynetdisk.exception.ResourceNotFoundException;
import com.taest.mynetdisk.response.ErrorReponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;

/**
 * @author twd
 * @description: TODO 类描述
 * @date 2021-01-12 15:10
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    // 也可以将 BaseException 换为 RuntimeException
    // 因为 RuntimeException 是 BaseException 的父类
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<?> handleAppException(BaseException ex, HttpServletRequest request) {
        ErrorReponse representation = new ErrorReponse(ex, request.getRequestURI());
        return new ResponseEntity<>(representation, new HttpHeaders(),ex.getError().getStatus());
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<ErrorReponse> handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        ErrorReponse errorReponse = new ErrorReponse(ex, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorReponse);
    }
}