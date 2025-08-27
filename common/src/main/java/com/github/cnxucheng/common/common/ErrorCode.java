package com.github.cnxucheng.common.common;

import lombok.Getter;

/**
 * 自定义错误码
 * @author : xucheng
 * @since : 2025-7-9
 */
@Getter
public enum ErrorCode {

    SUCCESS(0, "ok"),
    PARAMS_ERROR(40000, "请求参数错误"),
    NOT_LOGIN_ERROR(40001, "未登录"),
    NO_AUTH_ERROR(4002, "无权限"),
    NOT_FOUND_ERROR(4003, "数据不存在"),
    FORBIDDEN_ERROR(40004, "禁止访问"),
    SYSTEM_ERROR(50000, "系统内部异常"),
    OPERATION_ERROR(50001, "操作失败"),
    API_ERROR(50002, "API调用失败");

    /**
     * 状态码
     */
    private final int code;

    /**
     * 信息
     */
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}