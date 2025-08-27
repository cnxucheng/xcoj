package com.github.cnxucheng.common.common;

import lombok.Data;

import java.io.Serializable;

/**
 * web返回的结果
 * @param <T> data的数据类型
 * @author : xucheng
 * @since : 2025-7-9
 */
@Data
public class Result<T> implements Serializable {

    /**
     * 错误码
     */
    private int code;

    /**
     * 信息
     */
    private String message;

    /**
     * 数据
     */
    private T data;

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> Result<T> success(String message) {
        return new Result<>(ErrorCode.SUCCESS.getCode(), message, null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(ErrorCode.SUCCESS.getCode(), "success", data);
    }

    public static <T> Result<T> error(ErrorCode errorCode) {
        return new Result<>(errorCode.getCode(), errorCode.getMessage(), null);
    }

    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message, null);
    }

    public static <T> Result<T> error(ErrorCode errorCode, String message) {
        return new Result<>(errorCode.getCode(), message, null);
    }
}
