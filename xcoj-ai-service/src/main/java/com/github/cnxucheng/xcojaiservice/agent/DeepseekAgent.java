package com.github.cnxucheng.xcojaiservice.agent;

import com.github.cnxucheng.xcojaiservice.manus.XCAIManus;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import com.github.cnxucheng.xcojaiservice.adviser.XCAILoggerAdvisor;

/**
 * 基于deepseek大模型的Agent
 * @since : 1.0.0
 * @author : xucheng
 */
@Component
public class DeepseekAgent {

    @Resource
    private ChatModel deepSeekChatModel;

    private ChatClient client;

    @Resource
    private VectorStore xcaiVectorStore;

    @Resource
    private ToolCallback[] allTool;

    /**
     * 初始化Agent
     */
    @PostConstruct
    public void init() {
        String DEFAULT_SYSTEM_PROMPT = "你是XCAI系统的一个善于和用户沟通的客服，能够从上下文中获取到关键信息，" +
                "并且让用户很流畅的阅读，不会在回复中具体体现：`根据上下文`、`根据系统信息`等机械式的回答。" +
                "当上下文中出现的与用户提问无关的内容时，请忽略！";
        ChatMemory chatMemory = MessageWindowChatMemory.builder().build();

        this.client = ChatClient.builder(deepSeekChatModel)
                .defaultSystem(DEFAULT_SYSTEM_PROMPT)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build(),
                        new XCAILoggerAdvisor(),
                        new QuestionAnswerAdvisor(xcaiVectorStore)
                )
                .build();
    }

    /**
     * 知识库同步对话
     */
    public Flux<String> doChatWithRag(String message, String chatId) {
        return client.prompt()
                .user(message)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId))
                .advisors( new QuestionAnswerAdvisor(xcaiVectorStore) )
                .stream()
                .content();
    }

    public Flux<String> doChatByManus(String message, String chatId) {
        XCAIManus xcaiManus = new XCAIManus(allTool, deepSeekChatModel);
        String thinkResult = xcaiManus.run(message);
        return client.prompt()
                .user("system {" + thinkResult + "\n请不要把题目测试数据给用户!!! , 尽可能的完整的给用户解释提问！" +
                        "user{} 中包含的内容为用户的提问，尽可能的完整的给用户解释提问！而且不要凭空捏造}" +
                        "\n-----------------\n" +
                       "user { " + message + " }")
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId))
                .stream()
                .content();
    }
}