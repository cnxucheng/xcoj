package com.github.cnxucheng.xcojModel.dto.submision;

import lombok.Data;

import java.io.Serializable;

@Data
public class SubmissionQueryDTO implements Serializable {
    /**
     * 提交id
     */
    private Long submissionId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 题目id
     */
    private Long problemId;

    /**
     * 编程语言
     */
    private String lang;

    /**
     * 判题结果
     */
    private String judgeResult;

    /**
     * 页码
     */
    private Long current;

    /**
     * 大小
     */
    private Integer size;
}
