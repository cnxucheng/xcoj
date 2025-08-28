package com.github.cnxucheng.judgeservice.service;

import cn.hutool.json.JSONUtil;
import com.github.cnxucheng.common.common.ErrorCode;
import com.github.cnxucheng.common.exception.BusinessException;
import com.github.cnxucheng.judgeservice.sandbox.Sandbox;
import com.github.cnxucheng.xcojModel.dto.judge.JudgeRequest;
import com.github.cnxucheng.xcojModel.entity.*;
import com.github.cnxucheng.xcojModel.enums.UserProblemStatusEnum;
import com.github.cnxucheng.xcojModel.vo.JudgeResponse;
import com.github.cnxucheng.xcojfeignclient.service.ProblemFeignClient;
import com.github.cnxucheng.xcojfeignclient.service.SubmissionFeignClient;
import com.github.cnxucheng.xcojfeignclient.service.UserFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class JudgeServiceImpl implements JudgeService {

    @Resource
    private Sandbox systemSandbox;

    @Resource
    private ProblemFeignClient problemFeignClient;

    @Resource
    private SubmissionFeignClient submissionFeignClientService;

    @Resource
    private UserFeignClient userFeignClientService;

    @Override
    public void doJudge(Submission submission) {
        submissionFeignClientService.updateToRunning(submission.getSubmissionId());
        log.info("update ok!");
        JudgeRequest request = getRequest(submission);
        log.info("get request: {}", JSONUtil.toJsonStr(request));
        JudgeResponse response = systemSandbox.judge(request);
        log.info("Judge response: {}", JSONUtil.toJsonStr(response));
        if (response == null) {
            throw new BusinessException(ErrorCode.API_ERROR, "判题API调用失败");
        }

        if (response.getResultCode() == 0) {
            if (response.getUsedTime() >= request.getTimeLimit()) {
                response.setResultCode(-1);
                response.setMessage("Time Limit Exceeded");
                response.setUsedTime(getRequest(submission).getTimeLimit() + 1);
            } else {
                submission.setUsedTime(response.getUsedTime());
                submission.setUsedMemory(response.getUsedMemory());
                List<String> output = response.getOutput();
                Problem problem = problemFeignClient.getById(submission.getProblemId());
                List<TestCase> ans = JSONUtil.toList(problem.getJudgeCase(), TestCase.class);
                if (output.size() != ans.size()) {
                    response.setResultCode(-1);
                    response.setMessage("Wrong Answer");
                }
                for (int i = 0; i < output.size(); i++) {
                    if (!output.get(i).trim().equals(ans.get(i).getOutput().trim())) {
                        response.setResultCode(-1);
                        response.setMessage("Wrong Answer");
                    }
                }
            }
        }
        UserProblemStatusEnum userProblemStatusEnum = userFeignClientService.getUserProblemStatus(
                submission.getProblemId(), submission.getUserId()
        );
        if (userProblemStatusEnum == UserProblemStatusEnum.NO_SUBMIT) {
            UserStatus userStatus = UserStatus.builder()
                    .userId(submission.getUserId())
                    .problemId(submission.getProblemId())
                    .isAc(response.getResultCode() == 0 ? 1 : 0).build();
            userFeignClientService.save(userStatus);
        }
        if (userProblemStatusEnum == UserProblemStatusEnum.NOT_AC && response.getResultCode() == 0) {
            userFeignClientService.updateStatus(
                    submission.getUserId(),
                    submission.getProblemId(),
                    1
            );
        }
        if (userProblemStatusEnum != UserProblemStatusEnum.AC) {
            problemFeignClient.updateStatistics(submission.getProblemId(), response.getResultCode() == 0 ? 1 : 0);
            userFeignClientService.updateStatistics(submission.getUserId(), response.getResultCode() == 0 ? 1 : 0);
        }
        if (response.getResultCode() == 0) {
            response.setMessage("Accepted");
        }
        submissionFeignClientService.updateSubmissionJudgeInfo(response);
    }

    private JudgeRequest getRequest(Submission submission) {
        JudgeRequest request = new JudgeRequest();
        Problem problem = problemFeignClient.getById(submission.getProblemId());
        log.info("problem: {}", JSONUtil.toJsonStr(problem));
        request.setCodeId(submission.getSubmissionId());
        request.setCode(submission.getCode());
        request.setLang(submission.getLang());
        request.setTimeLimit(problem.getTimeLimit());
        request.setMemoryLimit(problem.getMemoryLimit());
        System.out.println(request);
        List<String> inputData = new ArrayList<>();
        for (TestCase testCase : JSONUtil.toList(problem.getJudgeCase(), TestCase.class)) {
            inputData.add(testCase.getInput());
        }
        System.out.println(request);
        request.setInput(inputData);
        return request;
    }

}
