package com.taest.mynetdisk.bussiness.user.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户
 * </p>
 *
 * @author twd
 * @since 2021-02-04
 */
@Data
public class MyUser implements Serializable {

    private static final long serialVersionUID = 1L;


    private String id;

    /**
     * 登陆名
     */
    private String loginName;

    /**
     * 昵称
     */
    private String name;

    /**
     * 密码
     */
    private String password;


}
