package com.github.cnxucheng.xcojaiservice.aiTool;

import jakarta.annotation.Resource;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 工具注册
 * @since : 1.0.1
 * @author : xucheng
 */
@Configuration
public class ToolRegister {

    @Bean
    public ToolCallback[] allAiTools(ProblemTool problemTool, SubmissionTool submissionTool, TerminateTool terminateTool) {
        return ToolCallbacks.from(
              problemTool,
              submissionTool,
              terminateTool
        );
    }
}
