package com.github.cnxucheng.coderunner.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RunCodeVO implements Serializable {
    private Integer resultCode;

    private Long codeId;

    private String message;

    private long usedTime;

    private long usedMemory;

    private List<String> output;
}
