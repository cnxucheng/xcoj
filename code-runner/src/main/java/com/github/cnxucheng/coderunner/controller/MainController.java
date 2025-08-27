package com.github.cnxucheng.coderunner.controller;

import com.github.cnxucheng.coderunner.model.RunCodeDTO;
import com.github.cnxucheng.coderunner.model.RunCodeVO;
import com.github.cnxucheng.coderunner.sandbox.SandboxFactory;
import com.github.cnxucheng.coderunner.sandbox.SandBox;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/")
public class MainController {

    @PostMapping("/run")
    public RunCodeVO runCode(@RequestBody RunCodeDTO runCodeDTO, HttpServletRequest request, HttpServletResponse response) {
        String authHeader = request.getHeader("auth");
        if (!"xcoj-system-auth-secret".equals(authHeader)) {
            response.setStatus(403);
            return null;
        }
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
}
