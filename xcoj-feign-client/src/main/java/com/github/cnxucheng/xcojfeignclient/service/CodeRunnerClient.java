package com.github.cnxucheng.xcojfeignclient.service;

import com.github.cnxucheng.xcojModel.dto.judge.JudgeRequest;
import com.github.cnxucheng.xcojModel.vo.JudgeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "xcoj-backend-code-runner-service", path = "/api/runner/inner")
public interface CodeRunnerClient {

    @PostMapping("/run")
    JudgeResponse runCode(@RequestBody JudgeRequest request);
}
