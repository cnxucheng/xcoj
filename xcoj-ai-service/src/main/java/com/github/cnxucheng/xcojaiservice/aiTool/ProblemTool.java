package com.github.cnxucheng.xcojaiservice.aiTool;

import com.github.cnxucheng.xcojModel.entity.Problem;
import com.github.cnxucheng.xcojfeignclient.service.ProblemFeignClient;
import jakarta.annotation.Resource;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
public class ProblemTool {

    @Resource
    private ProblemFeignClient problemFeignClient;

    @Tool(description = "get problem by id")
    public Problem getProblemById(@ToolParam(description = "problemId") String id) {
        return problemFeignClient.getById(Long.valueOf(id));
    }
}
