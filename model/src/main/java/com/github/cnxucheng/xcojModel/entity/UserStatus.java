package com.github.cnxucheng.xcojModel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户通过题目表
 * @author : xucheng
 * @since : 2025-7-8
 */
@TableName(value ="user_status")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserStatus {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 题目id
     */
    private Long problemId;

    /**
     * 是否通过
     */
    private Integer isAc;

    /**
     * 创建时间
     */
    private Date createTime;
}