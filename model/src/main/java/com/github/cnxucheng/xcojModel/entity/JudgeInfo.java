package com.github.cnxucheng.xcojModel.entity;

import com.github.cnxucheng.xcojModel.enums.ResultMessageEnum;
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
