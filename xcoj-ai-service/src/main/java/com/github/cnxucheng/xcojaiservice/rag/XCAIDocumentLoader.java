package com.github.cnxucheng.xcojaiservice.rag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 加载知识库
 * @since : 1.0.0
 * @author : xucheng
 */
@Component
public class XCAIDocumentLoader {

    private static final Logger logger = LoggerFactory.getLogger(SimpleLoggerAdvisor.class);

    private final ResourcePatternResolver resourcePatternResolver;

    public XCAIDocumentLoader(ResourcePatternResolver resourcePatternResolver) {
        this.resourcePatternResolver = resourcePatternResolver;
    }

    public List<Document> loadMarkdowns() {
        List<Document> documents = new ArrayList<>();
        try {
            Resource[] resources = resourcePatternResolver.getResources("classpath:doc/*.md");
            for (Resource resource : resources) {
                String filename = resource.getFilename();
                if (filename != null) {
                    MarkdownDocumentReaderConfig config = MarkdownDocumentReaderConfig.builder()
                            .withHorizontalRuleCreateDocument(true)
                            .withIncludeCodeBlock(false)
                            .withIncludeBlockquote(false)
                            .withAdditionalMetadata("filename", filename)
                            .build();
                    MarkdownDocumentReader reader = new MarkdownDocumentReader(resource, config);
                    documents.addAll(reader.read());
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return documents;
    }
}
