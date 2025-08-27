package com.github.cnxucheng.coderunner.model;

import lombok.Getter;

@Getter
public enum ResultMessageEnum {
    OK("ok"),
    CE("Compilation Error"),
    TLE("Time Limit Exceeded"),
    MLE("Memory Limit Exceeded"),
    RE("Runtime Error"),
    SE("System Error");

    private final String message;

    ResultMessageEnum(String message) {
        this.message = message;
    }

}
