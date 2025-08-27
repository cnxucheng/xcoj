package com.github.cnxucheng.xcojModel.enums;

import lombok.Getter;

/**
 * 用户权限
 * @author : xucheng
 * @since : 2025-7-9
 */
@Getter
public enum UserRoleEnum {
    BAN("ban", 1),  // 被封号 即 未登录
    USER("user", 2),
    ADMIN("admin",3),
    ROOT("root", 4);

    private final String value;
    private final Integer weight;

    UserRoleEnum(String value, Integer weight) {
        this.value = value;
        this.weight = weight;
    }

    public static UserRoleEnum getEnum(String value) {
        for (UserRoleEnum e : UserRoleEnum.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }

    public static Integer getWeight(String value) {
        for (UserRoleEnum e : UserRoleEnum.values()) {
            if (e.getValue().equals(value)) {
                return e.getWeight();
            }
        }
        return 0;
    }
}
