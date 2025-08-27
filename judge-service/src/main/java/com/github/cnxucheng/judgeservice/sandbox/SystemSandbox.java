package com.github.cnxucheng.judgeservice.sandbox;

import com.github.cnxucheng.xcojModel.dto.judge.JudgeRequest;
import com.github.cnxucheng.xcojModel.vo.JudgeResponse;
import com.github.cnxucheng.xcojfeignclient.service.CodeRunnerClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class SystemSandbox implements Sandbox {

    @Resource
    private CodeRunnerClient codeRunnerClient;

    @Override
    public JudgeResponse judge(JudgeRequest request) {
        return codeRunnerClient.runCode(request);
    }
}
