package com.github.cnxucheng.xcojModel.dto.problem;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 题目查询dto
 * @author : xucheng
 * @since : 2025-7-9
 */
@Data
public class ProblemQueryDTO implements Serializable {
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
     * 标签
     */
    private List<String> tags;

    /**
     * 当前页号
     */
    private long current = 1;

    /**
     * 页面大小
     */
    private long pageSize = 10;
}
