package com.github.cnxucheng.coderunner.sandbox.dockerSandbox;

import cn.hutool.core.io.FileUtil;
import com.github.cnxucheng.coderunner.sandbox.SandBox;
import com.github.cnxucheng.xcojModel.dto.judge.JudgeRequest;
import com.github.cnxucheng.xcojModel.enums.ResultMessageEnum;
import com.github.cnxucheng.xcojModel.vo.JudgeResponse;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.command.StatsCmd;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public interface DockerSandBox extends SandBox {
    String getFileName();
    List<String> getExecuteCommand();

    @Override
    default JudgeResponse executeCode(JudgeRequest dto) {
        Long codeId = dto.getCodeId();
        String code = dto.getCode();

        JudgeResponse JudgeResponse = new JudgeResponse();
        JudgeResponse.setCodeId(codeId);
        JudgeResponse.setResultCode(0);

        String codeParentPath = saveCodeToFile(code, getFileName());
        saveDataToFile(dto.getInput(), codeParentPath);

        // 编译代码
        if (!compile(codeParentPath)) {
            JudgeResponse.setResultCode(-1);
            JudgeResponse.setMessage(ResultMessageEnum.CE.getMessage());
            FileUtil.del(codeParentPath);
            return JudgeResponse;
        }

        // 执行代码
        Integer timeLimit = dto.getTimeLimit();
        Integer memoryLimit = dto.getMemoryLimit();
        List<String> input = dto.getInput();
        JudgeResponse tmpVO = runByDocker(codeParentPath, timeLimit, memoryLimit, input);
        if (!tmpVO.getMessage().equals(ResultMessageEnum.OK.getMessage())) {
            JudgeResponse.setResultCode(-1);
        }
        JudgeResponse.setOutput(tmpVO.getOutput());
        JudgeResponse.setMessage(tmpVO.getMessage());
        JudgeResponse.setUsedTime(tmpVO.getUsedTime());
        JudgeResponse.setUsedMemory(tmpVO.getUsedMemory());
        FileUtil.del(codeParentPath);
        return JudgeResponse;
    }

    default JudgeResponse runByDocker(String codeParentPath, Integer timeLimit, Integer memoryLimit, List<String> input) {
        DockerClient dockerClient = DockerClientBuilder.getInstance().build();
        String image = "xcoj-judge:1.0";

        CreateContainerResponse createContainerResponse = createContainer(dockerClient, image, codeParentPath, memoryLimit);

        String containerId = createContainerResponse.getId();
        dockerClient.startContainerCmd(containerId).exec();

        JudgeResponse JudgeResponse = new JudgeResponse();
        JudgeResponse.setMessage(ResultMessageEnum.OK.getMessage());
        JudgeResponse.setUsedTime(0);
        JudgeResponse.setUsedMemory(0);

        // 准备输出结果列表
        List<String> outputList = new ArrayList<>();

        for (int i = 0; i < input.size(); i ++ ) {
            StopWatch stopWatch = new StopWatch();
            String joinedCmd = String.format("cat /app/data/%d.in | %s", i, String.join(" ", getExecuteCommand()));
            String[] cmdArray = {"sh", "-c", joinedCmd};

            // 创建执行命令
            ExecCreateCmdResponse execCreateCmdResponse = dockerClient.execCreateCmd(containerId)
                    .withCmd(cmdArray)
                    .withAttachStderr(true)
                    .withAttachStdin(true)
                    .withAttachStdout(true)
                    .exec();

            // 准备收集输出和错误
            final StringBuilder outputBuilder = new StringBuilder();
            final StringBuilder errorBuilder = new StringBuilder();
            final boolean[] timeout = {true};
            final long[] maxMemory = {0L};
            String execId = execCreateCmdResponse.getId();

            // 创建回调处理器
            ExecStartResultCallback execStartResultCallback = new ExecStartResultCallback() {
                @Override
                public void onComplete() {
                    timeout[0] = false;
                    super.onComplete();
                }

                @Override
                public void onNext(Frame frame) {
                    if (StreamType.STDERR.equals(frame.getStreamType())) {
                        errorBuilder.append(new String(frame.getPayload()));
                    } else {
                        outputBuilder.append(new String(frame.getPayload()));
                    }
                    super.onNext(frame);
                }
            };

            // 启动内存监控
            StatsCmd statsCmd = dockerClient.statsCmd(containerId);
            ResultCallback<Statistics> statsCallback = new ResultCallback.Adapter<Statistics>() {
                @Override
                public void onNext(Statistics statistics) {
                    maxMemory[0] = Math.max(statistics.getMemoryStats().getUsage(), maxMemory[0]);
                }
            };
            statsCmd.exec(statsCallback);

            try {
                stopWatch.start();

                // 执行命令并发送输入
                dockerClient.execStartCmd(execId)
                        .exec(execStartResultCallback)
                        .awaitCompletion(timeLimit, TimeUnit.MILLISECONDS);

                stopWatch.stop();
                statsCmd.close();

                // 处理执行结果
                long time = stopWatch.getLastTaskTimeMillis();
                JudgeResponse.setUsedTime((int) Math.max(JudgeResponse.getUsedTime(), time));
                JudgeResponse.setUsedMemory((int) Math.max(JudgeResponse.getUsedMemory(), maxMemory[0] / 1024));

                // 如果有错误输出
                if (errorBuilder.length() > 0) {
                    JudgeResponse.setResultCode(-1);
                    JudgeResponse.setMessage(ResultMessageEnum.RE.getMessage());
                    outputList.add(errorBuilder.toString());
                } else {
                    outputList.add(outputBuilder.toString());
                }

            } catch (InterruptedException e) {
                JudgeResponse.setResultCode(-1);
                JudgeResponse.setMessage(ResultMessageEnum.RE.getMessage());
                outputList.add(e.getMessage());
            } finally {
                statsCmd.close();
            }

            if (timeout[0]) {
                JudgeResponse.setResultCode(-1);
                JudgeResponse.setMessage(ResultMessageEnum.TLE.getMessage());
                return JudgeResponse;
            }
        }

        // 清理容器
        try {
            dockerClient.removeContainerCmd(containerId).withForce(true).exec();
        } catch (Exception e) {
            System.err.println("Failed to remove container: " + e.getMessage());
        }

        JudgeResponse.setOutput(outputList);
        return JudgeResponse;
    }


    default CreateContainerResponse createContainer(DockerClient dockerClient, String image,
                                                    String hostPath, int memoryLimit) {
        HostConfig hostConfig = new HostConfig()
                .withMemory(memoryLimit * 1024L)
                .withMemorySwap(0L)
                .withMemorySwappiness(0L)
                .withCpuCount(1L)
                .withBinds(new Bind(hostPath, new Volume("/app")));

        return dockerClient.createContainerCmd(image)
                .withHostConfig(hostConfig)
                .withNetworkDisabled(true)
                .withReadonlyRootfs(true)
                .withTty(true)
                .exec();
    }
}