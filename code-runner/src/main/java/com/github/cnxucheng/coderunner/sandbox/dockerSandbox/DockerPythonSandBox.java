package com.github.cnxucheng.coderunner.sandbox.dockerSandbox;

import java.util.Arrays;
import java.util.List;

public class DockerPythonSandBox implements DockerSandBox {

    @Override
    public String getFileName() {
        return "Main.py";
    }

    @Override
    public List<String> getExecuteCommand() {
        return Arrays.asList("python3", "/app/Main.py");
    }
}
