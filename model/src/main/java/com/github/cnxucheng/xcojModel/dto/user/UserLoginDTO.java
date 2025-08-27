package com.github.cnxucheng.xcojModel.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录dto
 * @author : xucheng
 * @since : 2025-7-9
 */
@Data
public class UserLoginDTO implements Serializable {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;
}
