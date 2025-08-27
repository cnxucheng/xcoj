package com.github.cnxucheng.judgeservice.controller;

import com.github.cnxucheng.judgeservice.service.JudgeService;
import com.github.cnxucheng.xcojModel.entity.Submission;
import com.github.cnxucheng.xcojfeignclient.service.JudgeFeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/inner")
public class JudgeInnerController implements JudgeFeignClient {

    @Resource
    private JudgeService judgeService;

    @PostMapping("/doJudge")
    @Override
    public void doJudge(@RequestBody Submission submission) {
        judgeService.doJudge(submission);
    }
}
