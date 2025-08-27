package com.github.cnxucheng.xcojModel.dto.user;

import lombok.Data;

@Data
public class UserQueryDTO {

    private Long userId;

    private String username;

    /**
     * 当前页号
     */
    private long current = 1;

    /**
     * 页面大小
     */
    private long pageSize = 10;
}
