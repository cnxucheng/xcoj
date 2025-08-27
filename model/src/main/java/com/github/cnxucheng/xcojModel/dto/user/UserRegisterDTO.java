package com.github.cnxucheng.xcojModel.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册dto
 * @author : xucheng
 * @since : 2025-7-9
 */
@Data
public class UserRegisterDTO implements Serializable {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 确认密码
     */
    private String checkPassword;
}
