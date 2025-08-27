package com.github.cnxucheng.coderunner.sandbox.nativeSandBox;

import cn.hutool.core.io.FileUtil;
import com.github.cnxucheng.coderunner.model.JudgeInfo;
import com.github.cnxucheng.coderunner.model.ResultMessageEnum;
import com.github.cnxucheng.coderunner.model.RunCodeDTO;
import com.github.cnxucheng.coderunner.model.RunCodeVO;
import com.github.cnxucheng.coderunner.sandbox.SandBox;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public interface NativeSandBox extends SandBox {

    default RunCodeVO executeCode(RunCodeDTO dto) {
        Long codeId = dto.getCodeId();
        String code = dto.getCode();
        Integer timeLimit = dto.getTimeLimit();
        Integer memoryLimit = dto.getMemoryLimit();
        List<String> input = dto.getInput();

        RunCodeVO runCodeVO = new RunCodeVO();
        runCodeVO.setCodeId(codeId);

        String codeParentPath = saveCodeToFile(code, getFileName());
        saveDataToFile(input, codeParentPath);
        if (!compile(codeParentPath)) {
            runCodeVO.setResultCode(-1);
            runCodeVO.setMessage(ResultMessageEnum.CE.getMessage());
            return runCodeVO;
        }

        JudgeInfo judgeInfo = runCode(codeParentPath, timeLimit, memoryLimit, input.size());
        runCodeVO.setMessage(judgeInfo.getResult().getMessage());
        runCodeVO.setUsedTime(judgeInfo.getUsedTime());
        runCodeVO.setUsedMemory(judgeInfo.getUsedMemory());
        if (judgeInfo.getResult() == ResultMessageEnum.OK) {
            runCodeVO.setResultCode(0);
            runCodeVO.setOutput(getOutputFile(codeParentPath, input.size()));
        } else {
            runCodeVO.setResultCode(-1);
        }

        FileUtil.del(codeParentPath);

        return runCodeVO;
    }

    String getFileName();

    /**
     * 获取输出文件
     */
    default List<String> getOutputFile(String codeParentPath, Integer judgeSize) {
        List<String> output = new ArrayList<>();
        for (int i = 0; i < judgeSize; i++) {
            String filePath = codeParentPath + File.separator + "data" + File.separator + i + ".out";
            try {
                output.add(FileUtil.readUtf8String(filePath));
            } catch (Exception e) {
                output.add("Error reading output file: " + filePath);
            }
        }
        return output;
    }

    default JudgeInfo runCode(String codeParentPath, Integer timeLimit, Integer memoryLimit, int judgeCaseSize) {
        JudgeInfo judgeInfo = JudgeInfo.builder()
                .result(ResultMessageEnum.OK)
                .usedTime(0)
                .usedMemory(0)
                .build();
        for (int i = 0; i < judgeCaseSize; i++) {
            String inputPath = codeParentPath + File.separator + "data" + File.separator + i + ".in";
            String outputPath = codeParentPath + File.separator + "data" + File.separator + i + ".out";
            JudgeInfo res = executeProcess(
                getExecuteCommand(),
                codeParentPath,
                inputPath,
                outputPath,
                timeLimit,
                memoryLimit
            );
            judgeInfo.setUsedTime(Math.max(judgeInfo.getUsedTime(), res.getUsedTime()));
            judgeInfo.setUsedMemory(Math.max(judgeInfo.getUsedMemory(), res.getUsedMemory()));
            if (res.getResult() != ResultMessageEnum.OK) {
                judgeInfo.setResult(res.getResult());
                return judgeInfo;
            }
        }
        return judgeInfo;
    }

    /**
     * 获取执行命令
     */
    List<String> getExecuteCommand();

    /**
     * 执行进程并返回结果
     */
    default JudgeInfo executeProcess(List<String> command, String codeParentPath, 
                                   String inputFile, String outputFile,
                                   Integer timeLimit, Integer memoryLimit) {
        JudgeInfo judgeInfo = JudgeInfo.builder()
                .result(ResultMessageEnum.OK)
                .usedTime(0)
                .usedMemory(0)
                .build();
        long startTime, endTime;

        try {
            ProcessBuilder builder = new ProcessBuilder(command);
            builder.directory(new File(codeParentPath));
            builder.redirectInput(new File(inputFile));
            builder.redirectOutput(new File(outputFile));
            builder.redirectError(new File("err.err"));

            Process process = builder.start();
            startTime = System.currentTimeMillis();

            boolean finished = process.waitFor(timeLimit, TimeUnit.MILLISECONDS);
            endTime = System.currentTimeMillis();

            judgeInfo.setUsedTime((int) (endTime - startTime));

            if (!finished) {
                process.destroyForcibly();
                judgeInfo.setResult(ResultMessageEnum.TLE);
                return judgeInfo;
            }

            int exitCode = process.exitValue();
            if (exitCode != 0) {
                judgeInfo.setResult(ResultMessageEnum.RE);
                return judgeInfo;
            }

            // 确保输出文件存在
            if (!FileUtil.exist(outputFile)) {
                FileUtil.writeString("", outputFile, StandardCharsets.UTF_8);
            }

            judgeInfo.setUsedMemory(memoryLimit);
            return judgeInfo;

        } catch (Exception e) {
            e.printStackTrace();
            judgeInfo.setResult(ResultMessageEnum.SE);
            return judgeInfo;
        }
    }
}
