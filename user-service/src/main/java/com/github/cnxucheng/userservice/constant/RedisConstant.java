package com.github.cnxucheng.userservice.constant;

public interface RedisConstant {
    String SIGN_IN_FRONT = "user_sign_in";

    static String getUserSignKey(int year, long userId) {
        return String.format("%s:%s:%s", SIGN_IN_FRONT, year, userId);
    }
}
