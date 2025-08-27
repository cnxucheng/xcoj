package com.github.cnxucheng.xcojModel.vo;

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
public class JudgeResponse implements Serializable {
    private Integer resultCode;

    private Long codeId;

    private String message;

    private Integer usedTime;

    private Integer usedMemory;

    private List<String> output;
}
