package com.github.cnxucheng.coderunner.sandbox.factory;

import com.github.cnxucheng.coderunner.sandbox.SandBox;
import com.github.cnxucheng.coderunner.sandbox.nativeSandBox.NativeCppSandBox;
import com.github.cnxucheng.coderunner.sandbox.nativeSandBox.NativeJavaSandBox;
import com.github.cnxucheng.coderunner.sandbox.nativeSandBox.NativePythonSandBox;

public class NativeSandBoxFactory implements SandboxFactoryInterface {

    public SandBox getCodeSandBoxByLang(String lang) {
        if (lang.equalsIgnoreCase("java")) {
            return new NativeJavaSandBox();
        }
        if (lang.equalsIgnoreCase("cpp")) {
            return new NativeCppSandBox();
        }
        if (lang.equalsIgnoreCase("python")) {
            return new NativePythonSandBox();
        }
        return null;
    }
}
