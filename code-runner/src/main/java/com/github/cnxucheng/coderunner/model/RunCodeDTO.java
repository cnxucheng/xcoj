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
public class RunCodeDTO implements Serializable {

    private Long codeId;

    private String code;

    private String lang;

    private Integer timeLimit;

    private Integer memoryLimit;

    private List<String> input;
}
