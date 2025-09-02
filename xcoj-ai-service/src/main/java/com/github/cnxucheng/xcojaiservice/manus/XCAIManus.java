package com.github.cnxucheng.xcojaiservice.manus;

import com.github.cnxucheng.xcojaiservice.adviser.XCAILoggerAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.stereotype.Component;

@Component
public class XCAIManus extends ToolCallAgent {

    public XCAIManus(ToolCallback[] allTools, ChatModel deepSeekChatModel) {
        super(allTools);
        this.setName("XCAI-Manus");
        String SYSTEM_PROMPT = """
                You are XCAI-Manus, an all-capable AI assistant, aimed at solving any task presented by the user.
                You have various tools at your disposal that you can call upon to efficiently complete complex requests.
                If you think this user problem doesn't need user function/tool, you can use the terminate function/tool to end
                """;
        this.setSystemPrompt(SYSTEM_PROMPT);
        String NEXT_STEP_PROMPT = """
                Proactively select the most appropriate tool or tool combination based on user requirements.
                For complex tasks, decompose the problem and invoke different tools in a stepwise manner.
                After each tool execution, explicitly report the results and suggest subsequent actions.
                Use the terminate function/tool to end the interaction at any time if needed.
                """;
        this.setNextStepPrompt(NEXT_STEP_PROMPT);
        this.setMaxSteps(10);
        ChatClient chatClient = ChatClient.builder(deepSeekChatModel)
                .defaultAdvisors(new XCAILoggerAdvisor())
                .build();
        this.setChatClient(chatClient);
    }
}