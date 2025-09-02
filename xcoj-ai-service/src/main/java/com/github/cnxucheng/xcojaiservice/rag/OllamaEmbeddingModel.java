package com.github.cnxucheng.xcojaiservice.rag;

import lombok.NonNull;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Component
public class OllamaEmbeddingModel implements EmbeddingModel {

    private final WebClient webClient;

    public OllamaEmbeddingModel() {
        String apiUrl = "http://localhost:11434";
        this.webClient = WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Override
    @NonNull
    public EmbeddingResponse call(@NonNull EmbeddingRequest request) {
        Assert.notNull(request, "EmbeddingRequest must not be null");
        List<String> inputs = request.getInstructions();
        Assert.notEmpty(inputs, "Input list must not be empty");

        List<Embedding> embeddings = Flux.fromIterable(inputs)
                .flatMap(this::getEmbeddingAsync)
                .collectList()
                .block();

        Assert.notNull(embeddings, "Ollama embedding response is null");
        return new EmbeddingResponse(embeddings);
    }

    private Mono<Embedding> getEmbeddingAsync(String input) {
        String modelName = "nomic-embed-text";
        return webClient.post()
                .uri("/api/embeddings")
                .bodyValue(new OllamaEmbeddingRequest(modelName, input))
                .retrieve()
                .bodyToMono(OllamaEmbeddingResponse.class)
                .map(resp -> new Embedding(resp.embedding, 0))
                .onErrorResume(e -> {
                    System.err.println("Embedding request failed for input: " + input + ", error: " + e.getMessage());
                    return Mono.just(new Embedding(new float[0], 0));
                });
    }

    @Override
    public float @NonNull [] embed(@NonNull Document document) {
        Assert.notNull(document, "Document must not be null");
        String text = Objects.requireNonNull(document.getText());
        if (text.trim().isEmpty()) {
            return new float[0];
        }
        EmbeddingResponse response = call(new EmbeddingRequest(List.of(text), null));
        return response.getResult().getOutput();
    }

    record OllamaEmbeddingRequest(String model, String prompt) { }

    record OllamaEmbeddingResponse(float[] embedding) { }
}