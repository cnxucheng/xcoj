package com.github.cnxucheng.xcojModel.dto.problem;

import com.github.cnxucheng.xcojModel.entity.TestCase;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 修改题目dto
 * @author : xucheng
 * @since : 2025-7-9
 */
@Data
public class ProblemUpdateDTO implements Serializable {

    /**
     * 题目id
     */
    private Long problemId;

    /**
     * 题目标题
     */
    private String title;

    /**
     * 题目内容
     */
    private String content;

    /**
     * 题解
     */
    private String solution;

    /**
     * 标签
     */
    private List<String> tags;

    /**
     * 题目时间限制(MS)
     */
    private Integer timeLimit;

    /**
     * 空间限制(KB)
     */
    private Integer memoryLimit;

    /**
     * 判题数据
     */
    private List<TestCase> judgeCase;

    /**
     * 是否隐藏
     */
    private Integer isHidden;
}
