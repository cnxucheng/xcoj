package com.github.cnxucheng.xcojaiservice.adviser;

import lombok.NonNull;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import org.springframework.ai.chat.prompt.Prompt;
import reactor.core.publisher.Flux;

/**
 * 重读提示词Adviser
 * @since : 1.0.0
 * @author : xucheng
 */
public class Re2Adviser implements CallAdvisor, StreamAdvisor {

    @Override
    @NonNull
    public ChatClientResponse adviseCall(@NonNull ChatClientRequest chatClientRequest,
                                         CallAdvisorChain callAdvisorChain) {
        return callAdvisorChain.nextCall(this.re2Request(chatClientRequest));
    }

    @Override
    @NonNull
    public Flux<ChatClientResponse> adviseStream(@NonNull ChatClientRequest chatClientRequest,
                                                 StreamAdvisorChain streamAdvisorChain) {
        return streamAdvisorChain.nextStream(this.re2Request(chatClientRequest));
    }

    private ChatClientRequest re2Request(ChatClientRequest chatClientRequest) {
        String userPrompt = chatClientRequest.prompt().getUserMessage().getText();
        String userNewMessage = userPrompt + "\nread this question again: " + userPrompt;
        chatClientRequest.prompt().augmentUserMessage(userNewMessage);
        Prompt newPrompt = chatClientRequest.prompt().augmentUserMessage(userNewMessage);
        return new ChatClientRequest(newPrompt, chatClientRequest.context());
    }

    @Override
    @NonNull
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
