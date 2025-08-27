package com.github.cnxucheng.coderunner.sandbox.dockerSandbox;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class DockerJavaSandBox implements DockerSandBox {

    @Override
    public String getFileName() {
        return "Main.java";
    }

    @Override
    public Boolean compile(String codePath) {
        try {
            ProcessBuilder builder = new ProcessBuilder("javac", "Main.java");
            builder.directory(new File(codePath));
            Process process = builder.start();
            int exitCode = process.waitFor();
            return exitCode == 0;
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }

    @Override
    public List<String> getExecuteCommand() {
        return Arrays.asList("java", "-cp", "/app", "Main");
    }

}
