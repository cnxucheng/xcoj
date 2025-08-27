package com.github.cnxucheng.coderunner.sandbox.factory;

import com.github.cnxucheng.coderunner.sandbox.SandBox;
import com.github.cnxucheng.coderunner.sandbox.dockerSandbox.DockerCppSandBox;
import com.github.cnxucheng.coderunner.sandbox.dockerSandbox.DockerJavaSandBox;
import com.github.cnxucheng.coderunner.sandbox.dockerSandbox.DockerPythonSandBox;

public class DockerSandboxFactory implements SandboxFactoryInterface {

    public SandBox getCodeSandBoxByLang(String lang) {
        if (lang.equalsIgnoreCase("java")) {
            return new DockerJavaSandBox();
        }
        if (lang.equalsIgnoreCase("cpp")) {
            return new DockerCppSandBox();
        }
        if (lang.equalsIgnoreCase("python")) {
            return new DockerPythonSandBox();
        }
        return null;
    }

}
