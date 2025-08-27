package com.github.cnxucheng.judgeservice.sandbox;

import com.github.cnxucheng.xcojModel.dto.judge.JudgeRequest;
import com.github.cnxucheng.xcojModel.vo.JudgeResponse;

public interface Sandbox {

    JudgeResponse judge(JudgeRequest request);
}
