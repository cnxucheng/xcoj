package com.github.cnxucheng.xcojModel.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SubmissionVO implements Serializable {
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
     * 时间使用
     */
    private Integer usedTime;

    /**
     * 空间使用
     */
    private Integer usedMemory;

    /**
     * 创建时间
     */
    private Date createTime;
}
