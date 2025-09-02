package com.github.cnxucheng.xcojaiservice.manus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;

public abstract class ReActAgent extends BaseAgent {

    public abstract boolean think();

    public abstract String act();

    private static final Logger logger = LoggerFactory.getLogger(SimpleLoggerAdvisor.class);

    @Override
    public String step() {
        try {
            boolean shouldAct = think();
            if (!shouldAct) {
                return "思考完成: 无需下一步";
            }
            return act();
        } catch (Exception e) {
            logger.info("步骤执行失败: {}", e.getMessage());
            return "步骤执行失败: " + e.getMessage();
        }
    }

}
