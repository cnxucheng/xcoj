package com.github.cnxucheng.common.common;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 给前端返回的分页数据
 * @author : xucheng
 * @since : 2025-7-9
 */
@Data
public class MyPage<T> implements Serializable {
    /**
     * 数据列表
     */
    List<T> data;

    /**
     * 当前页
     */
    long current;

    /**
     * 页大小
     */
    int pageSize;

    /**
     * 数据库中的数量
     */
    long total;

    /**
     * 一共有几页
     */
    long totalPages;
}
