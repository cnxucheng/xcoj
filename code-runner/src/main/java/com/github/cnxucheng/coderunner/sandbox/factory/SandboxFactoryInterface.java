package com.github.cnxucheng.coderunner.sandbox.factory;


import com.github.cnxucheng.coderunner.sandbox.SandBox;

public interface SandboxFactoryInterface {
    SandBox getCodeSandBoxByLang(String lang);
}
