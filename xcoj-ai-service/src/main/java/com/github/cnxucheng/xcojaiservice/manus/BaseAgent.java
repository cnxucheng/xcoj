package com.github.cnxucheng.xcojaiservice.manus;

import cn.hutool.core.util.StrUtil;
import com.github.cnxucheng.xcojaiservice.manus.model.AgentState;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Data
public abstract class BaseAgent {

    private String name;

    private String systemPrompt;
    private String nextStepPrompt;

    private AgentState state = AgentState.IDLE;

    private int maxSteps = 10;
    private int currentStep = 0;

    private ChatClient chatClient;

    private List<Message> messageList = new ArrayList<>();

    private static final Logger logger = LoggerFactory.getLogger(SimpleLoggerAdvisor.class);

    public String run(String userPrompt) {
        if (this.state != AgentState.IDLE) {
            throw new RuntimeException("非法状态: " + this.state);
        }
        if (StrUtil.isBlank(userPrompt)) {
            throw new RuntimeException("用户提示为空");
        }
        state = AgentState.RUNNING;
        messageList.add(new UserMessage(userPrompt));
        List<String> results = new ArrayList<>();
        try {
            for (int stepNumber = 1; stepNumber <= maxSteps && state != AgentState.FINISHED; stepNumber ++ ) {
                currentStep = stepNumber;
                logger.info("Executing step {}/{}", stepNumber, maxSteps);
                // 单步执行
                String stepResult = step();
                String result = "Step " + stepNumber + ": " + stepResult;
                results.add(result);
            }
            // 检查是否超出步骤限制
            if (currentStep >= maxSteps) {
                state = AgentState.FINISHED;
                results.add("超过最大思考步数: " + maxSteps);
            }
            return String.join("\n", results);
        } catch (Exception e) {
            state = AgentState.ERROR;
            logger.error("智能体执行错误: ", e);
            return "执行错误: " + e.getMessage();
        } finally {
            this.cleanup();
        }
    }

    public abstract String step();

    protected void cleanup() {
    }

    public String getName() {
        return name;
    }

    public String getSystemPrompt() {
        return systemPrompt;
    }

    public String getNextStepPrompt() {
        return nextStepPrompt;
    }

    public AgentState getState() {
        return state;
    }

    public int getMaxSteps() {
        return maxSteps;
    }

    public ChatClient getChatClient() {
        return chatClient;
    }

    public int getCurrentStep() {
        return currentStep;
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSystemPrompt(String systemPrompt) {
        this.systemPrompt = systemPrompt;
    }

    public void setNextStepPrompt(String nextStepPrompt) {
        this.nextStepPrompt = nextStepPrompt;
    }

    public void setState(AgentState state) {
        this.state = state;
    }

    public void setCurrentStep(int currentStep) {
        this.currentStep = currentStep;
    }

    public void setMaxSteps(int maxSteps) {
        this.maxSteps = maxSteps;
    }

    public void setChatClient(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }
}
