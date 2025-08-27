package com.github.cnxucheng.judgeservice.service;

import com.github.cnxucheng.xcojModel.entity.Submission;

public interface JudgeService {

    void doJudge(Submission submission);
}
