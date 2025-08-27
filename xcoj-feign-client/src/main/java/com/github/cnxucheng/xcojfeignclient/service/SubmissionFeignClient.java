package com.github.cnxucheng.xcojfeignclient.service;

import com.github.cnxucheng.xcojModel.entity.Submission;
import com.github.cnxucheng.xcojModel.vo.JudgeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "xcoj-backend-submission-service", path = "/api/submission/inner")
public interface SubmissionFeignClient {

    @PostMapping("/beRunning")
    void updateToRunning(@RequestParam("submissionId") Long submissionId);

    @PostMapping("/update")
    void updateSubmissionJudgeInfo(@RequestBody JudgeResponse response);

    @GetMapping("/")
    Submission getSubmissionById(@RequestParam("submissionId") Long submissionId);
}
