package com.github.cnxucheng.xcojaiservice.controller;

import com.github.cnxucheng.xcojModel.entity.User;
import com.github.cnxucheng.xcojModel.enums.UserRoleEnum;
import com.github.cnxucheng.xcojModel.vo.AIRequest;
import com.github.cnxucheng.xcojaiservice.agent.DeepseekAgent;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import static com.github.cnxucheng.common.constant.UserLoginState.USER_LOGIN_STATE;

@RestController
@RequestMapping("/ai")
public class MainController {

    @Resource
    private DeepseekAgent deepseekAgent;

    @PostMapping("/rag")
    public Flux<String> rag(@RequestBody AIRequest aiRequest) {
        return deepseekAgent.doChatWithRag(aiRequest.getMessage(), aiRequest.getChatId());
    }

    @PostMapping("/manus")
    public Flux<String> manus(@RequestBody AIRequest aiRequest) {
        return deepseekAgent.doChatByManus(aiRequest.getMessage(), aiRequest.getChatId());
    }
}
