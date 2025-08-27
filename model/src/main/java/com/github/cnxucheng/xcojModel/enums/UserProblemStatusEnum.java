package com.github.cnxucheng.xcojModel.enums;

import lombok.Getter;

@Getter
public enum UserProblemStatusEnum {
    AC(1, "已通过"),
    NOT_AC(0, "未通过"),
    NO_SUBMIT(-1, "未提交");

    private final int code;
    private final String desc;

    UserProblemStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
