package com.github.cnxucheng.xcojaiservice.aiTool;

import com.github.cnxucheng.xcojModel.entity.Submission;
import com.github.cnxucheng.xcojfeignclient.service.SubmissionFeignClient;
import jakarta.annotation.Resource;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
public class SubmissionTool {

    @Resource
    private SubmissionFeignClient submissionFeignClient;

    @Tool(description = "get submission by submission Id")
    public Submission getSubmissionById(@ToolParam(description = "submissionId") String submissionId) {
        return submissionFeignClient.getSubmissionById(Long.valueOf(submissionId));
    }
}
