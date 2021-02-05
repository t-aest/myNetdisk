package com.taest.mynetdisk.bussiness.user.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户
 * </p>
 *
 * @author jobob
 * @since 2021-02-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MyUser implements Serializable {

    private static final long serialVersionUID = 1L;

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
