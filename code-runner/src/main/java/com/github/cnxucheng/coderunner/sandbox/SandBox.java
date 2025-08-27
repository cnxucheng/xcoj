package com.github.cnxucheng.coderunner.sandbox;

import cn.hutool.core.io.FileUtil;
import com.github.cnxucheng.coderunner.model.RunCodeDTO;
import com.github.cnxucheng.coderunner.model.RunCodeVO;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

public interface SandBox {
    String GLOBAL_CODE_DIR_NAME = "tmpCode";

    RunCodeVO executeCode(RunCodeDTO dto);

    default String saveCodeToFile(String code, String fileName) {
        String userDir = System.getProperty("user.dir");
        String globalCodePathName = userDir + File.separator + GLOBAL_CODE_DIR_NAME;
        if (!FileUtil.exist(globalCodePathName)) {
            FileUtil.mkdir(globalCodePathName);
        }
        String userCodeParentPath = globalCodePathName + File.separator + UUID.randomUUID();
        String userCodePath = userCodeParentPath + File.separator + fileName;

        FileUtil.writeString(code, userCodePath, StandardCharsets.UTF_8);
        return userCodeParentPath;
    }

    default Boolean compile(String codePath) {
        return true;
    }

    default void saveDataToFile(List<String> input, String codeParentPath) {
        String testCasePath = codeParentPath + File.separator + "data";
        for (int i = 0; i < input.size(); i++) {
            FileUtil.writeString(input.get(i), testCasePath + File.separator + i + ".in", StandardCharsets.UTF_8);
        }
    }
}


