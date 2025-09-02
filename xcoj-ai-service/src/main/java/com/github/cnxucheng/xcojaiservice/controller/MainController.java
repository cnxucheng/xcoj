package com.github.cnxucheng.xcojaiservice.controller;

import com.github.cnxucheng.xcojModel.entity.User;
import com.github.cnxucheng.xcojModel.enums.UserRoleEnum;
import com.github.cnxucheng.xcojModel.vo.AIRequest;
import com.github.cnxucheng.xcojaiservice.agent.DeepseekAgent;
import com.github.cnxucheng.xcojfeignclient.service.UserFeignClient;
import jakarta.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.Objects;

@RestController
@RequestMapping("/ai")
public class MainController {

    @Resource
    private DeepseekAgent deepseekAgent;

    @Resource
    private UserFeignClient userFeignClient;

    @PostMapping("/rag")
    public Flux<String> rag(@RequestBody AIRequest aiRequest, HttpServletRequest request, HttpServletResponse response) {
        User user = userFeignClient.getLoginUser(request);
        if (Objects.equals(user.getUserRole(), UserRoleEnum.BAN.getValue()) ) {
            response.setStatus(401);
            return Flux.empty();
        }
        return deepseekAgent.doChatWithRag(
                aiRequest.getMessage()+ "\n我的用户ID为" + user.getUserId(),
                aiRequest.getChatId());
    }

    @PostMapping("/manus")
    public Flux<String> manus(@RequestBody AIRequest aiRequest, HttpServletRequest request, HttpServletResponse response) {
        User user = userFeignClient.getLoginUser(request);
        if (Objects.equals(user.getUserRole(), UserRoleEnum.BAN.getValue()) ) {
            response.setStatus(401);
            return Flux.empty();
        }
        return deepseekAgent.doChatByManus(
                aiRequest.getMessage() + "\n我的用户ID为" + user.getUserId(),
                aiRequest.getChatId());
    }
}
