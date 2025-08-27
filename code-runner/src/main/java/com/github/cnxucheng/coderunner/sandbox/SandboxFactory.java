package com.github.cnxucheng.coderunner.sandbox;

import com.github.cnxucheng.coderunner.sandbox.factory.DockerSandboxFactory;
import com.github.cnxucheng.coderunner.sandbox.factory.NativeSandBoxFactory;
import com.github.cnxucheng.coderunner.sandbox.factory.SandboxFactoryInterface;
import org.springframework.beans.factory.annotation.Value;

import java.util.Objects;

public class SandboxFactory {

    @Value("${xcojSandbox.boxName}")
    static String boxType;

    public static SandBox getCodeSandBoxByLang(String lang) {
        // 默认原生判题
        SandboxFactoryInterface sandboxFactory = new NativeSandBoxFactory();
        if (Objects.equals(boxType, "docker")) {
            sandboxFactory = new DockerSandboxFactory();
        }
        return sandboxFactory.getCodeSandBoxByLang(lang);
    }
}
