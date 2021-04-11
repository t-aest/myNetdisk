package com.taest.mynetdisk.response;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

/**
 * @className: 返回类
 * @description: TODO 类描述
 * @author: twd
 * @date: 2021/1/12
 **/
@Getter
@ToString
public enum ResultStatus {

    SUCCESS(0, HttpStatus.OK, "成功"),
    RESOURCE_NOT_FOUND(1001, HttpStatus.NOT_FOUND, "未找到该资源"),
    REQUEST_VALIDATION_FAILED(1002, HttpStatus.BAD_REQUEST, "请求数据格式验证失败"),
    UPLOAD_FAILED(1003, HttpStatus.INTERNAL_SERVER_ERROR, "上传失败"),
    DOWNLOAD_FAILED(1010, HttpStatus.INTERNAL_SERVER_ERROR, "下载失败"),
    UPLOAD_SUCCESS(1004, HttpStatus.CREATED, "上传成功"),
    UPLOAD_FILE_EXISTS(1005, HttpStatus.CREATED, "文件已存在"),
    UPLOAD_FILE_CHECK_ERROR(1006, HttpStatus.INTERNAL_SERVER_ERROR, "文件验证失败"),
    DELETE_FILE_ERROR(1007, HttpStatus.INTERNAL_SERVER_ERROR, "文件删除失败"),
    FILE_NOT_EXIST(1008, HttpStatus.INTERNAL_SERVER_ERROR, "文件不存在"),
    USER_NOT_EXIST(1013, HttpStatus.INTERNAL_SERVER_ERROR, "用户不存在"),
    FOLDER_CREATE_ERROR(1009, HttpStatus.INTERNAL_SERVER_ERROR, "文件夹创建失败"),
    EXPIRED_VERIFICATION_CODE(1018, HttpStatus.INTERNAL_SERVER_ERROR, "验证码已过期"),
    ERROR_VERIFICATION_CODE(1014, HttpStatus.INTERNAL_SERVER_ERROR, "验证码错误"),
    GET_USERINFO_ERROR(1015, HttpStatus.INTERNAL_SERVER_ERROR, "获取登录用户信息失败"),
    LOGIN_ERROR(1016, HttpStatus.INTERNAL_SERVER_ERROR, "登录失败"),
    USERNAME_OR_PASSWORD_ERROR(1017, HttpStatus.INTERNAL_SERVER_ERROR, "账户名或者密码错误"),
    FILE_SECOND_PASS(1011, HttpStatus.OK, "文件秒传"),
    FILE_BREAKPOINT_UPLOAD(1012, HttpStatus.OK, "断点续传"),
    MOVE_OR_COPY_ERROR(1020, HttpStatus.OK, "移动复制失败"),
    RENAME_ERROR(1019, HttpStatus.INTERNAL_SERVER_ERROR, "重命名失败");

    private Integer code;
    private String message;
    private HttpStatus status;

    ResultStatus(Integer code, HttpStatus status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

}
