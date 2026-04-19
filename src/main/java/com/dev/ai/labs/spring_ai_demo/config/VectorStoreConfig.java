package com.dev.ai.labs.spring_ai_demo.config;

import java.io.File;
import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableConfigurationProperties(VectorStoreProperties.class)
public class VectorStoreConfig {

    @Bean
    public SimpleVectorStore simpleVectorStore(EmbeddingModel embeddingModel, VectorStoreProperties properties) {
        log.info("Initializing SimpleVectorStore with path: {}", properties.getVectorStorePath());
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(embeddingModel).build();
        File vectorStoreFile = new File(properties.getVectorStorePath());
        
        if(vectorStoreFile.exists()) {
            log.info("Loading existing vector store from: {}", properties.getVectorStorePath());
            simpleVectorStore.load(vectorStoreFile);
        } 
        else {
            log.info("No existing vector store found at: {}. Starting with an empty store.", properties.getVectorStorePath());
            
            properties.getDocumentsToLoad().forEach(document -> {
                TikaDocumentReader reader = new TikaDocumentReader(document);
                List<Document> docs = reader.get();
                TextSplitter textSplitter = TokenTextSplitter.builder().withChunkSize(512).build();
                List<Document> splitDocs = textSplitter.apply(docs);        
                simpleVectorStore.add(splitDocs);
            });

            simpleVectorStore.save(vectorStoreFile);
            log.info("Vector store initialized and saved to: {}", properties.getVectorStorePath());
        }
        
        return simpleVectorStore;
    }
}