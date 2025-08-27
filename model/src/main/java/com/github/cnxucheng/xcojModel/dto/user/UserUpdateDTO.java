package com.github.cnxucheng.xcojModel.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 修改用户dto
 * @author : xucheng
 * @since : 2025-7-9
 */
@Data
public class UserUpdateDTO implements Serializable {
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户权限
     */
    private String userRole;
}
