package com.github.cnxucheng.coderunner.sandbox.dockerSandbox;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class DockerCppSandBox implements DockerSandBox {
    @Override
    public String getFileName() {
        return "Main.cpp";
    }

    @Override
    public Boolean compile(String codeParentPath) {
        try {
            ProcessBuilder builder = new ProcessBuilder("g++", "Main.cpp", "-o", "Main", "-O2", "-std=c++17");
            builder.directory(new File(codeParentPath));
            Process process = builder.start();
            int exitCode = process.waitFor();
            return exitCode == 0;
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }

    @Override
    public List<String> getExecuteCommand() {
        return Collections.singletonList("/app/Main");
    }
}
