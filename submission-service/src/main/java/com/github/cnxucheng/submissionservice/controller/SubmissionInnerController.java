package com.github.cnxucheng.submissionservice.controller;

import com.github.cnxucheng.submissionservice.service.SubmissionService;
import com.github.cnxucheng.xcojModel.entity.Submission;
import com.github.cnxucheng.xcojModel.vo.JudgeResponse;
import com.github.cnxucheng.xcojfeignclient.service.SubmissionFeignClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/inner")
public class SubmissionInnerController implements SubmissionFeignClient {

    @Resource
    private SubmissionService submissionService;

    @PostMapping("/beRunning")
    @Override
    public void updateToRunning(@RequestParam Long submissionId) {
        submissionService.updateToRunning(submissionId);
    }

    @PostMapping("/update")
    @Override
    public void updateSubmissionJudgeInfo(@RequestBody JudgeResponse response) {
        submissionService.updateSubmissionJudgeInfo(response.getCodeId(), response);
    }

    @GetMapping("/")
    @Override
    public Submission getSubmissionById(@RequestParam Long submissionId) {
        return submissionService.getById(submissionId);
    }
}
