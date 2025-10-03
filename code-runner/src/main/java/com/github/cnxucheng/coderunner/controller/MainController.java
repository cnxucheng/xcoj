package com.github.cnxucheng.coderunner.controller;

import com.github.cnxucheng.coderunner.sandbox.SandboxFactory;
import com.github.cnxucheng.coderunner.sandbox.SandBox;
import com.github.cnxucheng.xcojModel.dto.judge.JudgeRequest;
import com.github.cnxucheng.xcojModel.entity.User;
import com.github.cnxucheng.xcojModel.vo.JudgeResponse;
import com.github.cnxucheng.xcojfeignclient.service.CodeRunnerClient;
import com.github.cnxucheng.xcojfeignclient.service.UserFeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/")
public class MainController {

    @Resource
    private UserFeignClient userFeignClient;

    @PostMapping("/inner/run")
    public JudgeResponse runCodeInner(@RequestBody JudgeRequest runCodeDTO) {
        if (runCodeDTO == null) {
            throw new RuntimeException("请求参数为空");
        }
        if (runCodeDTO.getCodeId() == null || runCodeDTO.getCode() == null ||
                runCodeDTO.getTimeLimit() == null || runCodeDTO.getMemoryLimit() == null ||
                runCodeDTO.getLang() == null || runCodeDTO.getInput() == null) {
            throw new RuntimeException("请求参数不合法");
        }
        if (runCodeDTO.getTimeLimit() <= 0 || runCodeDTO.getTimeLimit() > 10000) {
            throw new RuntimeException("时间限制不合法");
        }
        if (runCodeDTO.getMemoryLimit() <= 0 || runCodeDTO.getMemoryLimit() > 1024 * 1024) {
            throw new RuntimeException("空间限制不合法");
        }

        SandBox sandBox = SandboxFactory.getCodeSandBoxByLang(runCodeDTO.getLang());
        if (sandBox == null) {
            throw new RuntimeException("编程语言不合法");
        }

        return sandBox.executeCode(runCodeDTO);
    }

    @PostMapping("/run")
    public JudgeResponse runCode(@RequestBody JudgeRequest runCodeDTO, HttpServletRequest request) {
        String token = request.getHeader("token");
        if (token == null) {
            return JudgeResponse.builder()
                    .codeId(runCodeDTO.getCodeId())
                    .resultCode(-1)
                    .message("用户未登录")
                    .build();
        }
        User user = userFeignClient.getLoginUser(request.getHeader("token"));
        System.out.println(user);
        if (user == null || user.getUserId() == null) {
            return JudgeResponse.builder()
                    .codeId(runCodeDTO.getCodeId())
                    .resultCode(-1)
                    .message("用户信息错误")
                    .build();
        }
        return runCodeInner(runCodeDTO);
    }
}
