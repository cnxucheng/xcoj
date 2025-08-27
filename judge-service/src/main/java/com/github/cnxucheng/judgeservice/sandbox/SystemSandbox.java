package com.github.cnxucheng.judgeservice.sandbox;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.cnxucheng.common.common.ErrorCode;
import com.github.cnxucheng.common.exception.BusinessException;
import com.github.cnxucheng.xcojModel.dto.judge.JudgeRequest;
import com.github.cnxucheng.xcojModel.vo.JudgeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SystemSandbox implements Sandbox {

    @Override
    public JudgeResponse judge(JudgeRequest request) {
        String url = "http://127.0.0.1:8600/run";
        System.out.println(url);
        String requestString = JSONUtil.toJsonStr(request);
        log.info(requestString);
        String responseStr = HttpUtil.createPost(url)
                .header("auth", "xcoj-system-auth-secret")
                .body(requestString)
                .execute()
                .body();
        if (StringUtils.isBlank(responseStr)) {
            throw new BusinessException(ErrorCode.API_ERROR, "executeCode remoteSandbox error, message = " + responseStr);
        }
        return JSONUtil.toBean(responseStr, JudgeResponse.class);
    }
}
