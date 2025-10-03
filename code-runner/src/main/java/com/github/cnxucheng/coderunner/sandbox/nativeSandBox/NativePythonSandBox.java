package com.github.cnxucheng.coderunner.sandbox.nativeSandBox;

import java.util.Arrays;
import java.util.List;

public class NativePythonSandBox implements NativeSandBox {

    @Override
    public String getFileName() {
        return "Main.py";
    }

    @Override
    public List<String> getExecuteCommand() {
        return Arrays.asList("py", "Main.py");
    }
}
