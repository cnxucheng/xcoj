package com.github.cnxucheng.common.common;

import lombok.Data;

@Data
public class PageRequest {
    private Integer pageSize;

    private Long current;
}
