package com.github.cnxucheng.xcojModel.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 题目简单vo, 不包含样例、题目内容等数据
 * @author : xucheng
 * @since : 2025-7-9
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProblemSampleVO implements Serializable {
    /**
     * 题目id
     */
    private Long problemId;

    /**
     * 题目标题
     */
    private String title;

    /**
     * 标签
     */
    private List<String> tags;

    /**
     * 通过数
     */
    private Integer acceptedNum;

    /**
     * 提交数
     */
    private Integer submitNum;
}
