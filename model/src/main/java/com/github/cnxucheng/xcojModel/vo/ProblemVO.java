package com.github.cnxucheng.xcojModel.vo;

import com.github.cnxucheng.xcojModel.entity.TestCase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 题目vo
 * @author : xucheng
 * @since : 2025-7-9
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProblemVO implements Serializable {
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
     * 测试数据
     */
    private List<TestCase> judgeCase;

    /**
     * 题目时间限制(MS)
     */
    private Integer timeLimit;

    /**
     * 空间限制(KB)
     */
    private Integer memoryLimit;

    /**
     * 通过数
     */
    private Integer acceptedNum;

    /**
     * 提交数
     */
    private Integer submitNum;

    /**
     * 是否删除
     */
    private Integer isHidden;
}
