package com.github.cnxucheng.xcojModel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 提交记录表
 * @author : xucheng
 * @since : 2025-7-8
 */
@TableName(value ="submission")
@Data
public class Submission implements Serializable {
    /**
     * 提交id
     */
    @TableId(type = IdType.AUTO)
    private Long submissionId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 题目id
     */
    private Long problemId;

    /**
     * 编程语言
     */
    private String lang;

    /**
     * 提交代码
     */
    private String code;

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

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;
}