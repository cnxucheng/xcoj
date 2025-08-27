package com.github.cnxucheng.coderunner.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JudgeInfo {
    private ResultMessageEnum result;

    private long usedTime;

    private long usedMemory;
}
