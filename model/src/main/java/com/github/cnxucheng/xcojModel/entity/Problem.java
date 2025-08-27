package com.github.cnxucheng.xcojModel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 题目表
 * @author : xucheng
 * @since : 2025-7-8
 */
@TableName(value ="problem")
@Data
public class Problem {
    /**
     * 题目id
     */
    @TableId(type = IdType.AUTO)
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
    private String tags;

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
    private String judgeCase;

    /**
     * 通过数
     */
    private Integer acceptedNum;

    /**
     * 提交数
     */
    private Integer submitNum;

    /**
     * 创建人id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否隐藏
     */
    private Integer isHidden;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;
}