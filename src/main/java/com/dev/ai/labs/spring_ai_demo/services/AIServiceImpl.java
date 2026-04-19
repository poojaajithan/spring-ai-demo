package com.dev.ai.labs.spring_ai_demo.services;

import com.dev.ai.labs.spring_ai_demo.model.Answer;
import com.dev.ai.labs.spring_ai_demo.model.GetCapitalRequest;
import com.dev.ai.labs.spring_ai_demo.model.Question;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JsonParseException;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class AIServiceImpl implements AIService {

    private final ChatModel chatModel;

    @Autowired
    private ObjectMapper objectMapper;

    final SimpleVectorStore vectorStore;

    public AIServiceImpl(ChatModel chatModel, SimpleVectorStore vectorStore) {
        this.chatModel = chatModel;
        this.vectorStore = vectorStore;
    }

    @Value("classpath:templates/rag-prompt-template.st")
    private Resource ragPromptTemplate;

    @Override
    public String getAnswer(String question) {
        PromptTemplate promptTemplate = new PromptTemplate(question);
        Prompt prompt = promptTemplate.create();
        ChatResponse response = chatModel.call(prompt);
        return response.getResult().getOutput().getText(); 
    }

    @Value("classpath:templates/get-capital-prompt.st")
    private Resource getCapitalPrompt;

    @Override
    public Answer getCapital(GetCapitalRequest request) {
        PromptTemplate promptTemplate = new PromptTemplate(getCapitalPrompt);
        Prompt prompt = promptTemplate.create(Map.of("stateorCountry", request.stateorCountry()));
        ChatResponse response = chatModel.call(prompt);
        String capital = response.getResult().getOutput().getText().trim();
        try{
            JsonNode jsonNode = objectMapper.readTree(capital);
            capital = jsonNode.get("answer").asText();
        }
        catch(JsonParseException e){
            System.out.println("Failed to parse JSON response: " + capital);
        }
        return new Answer(capital);
    }

    @Value("classpath:templates/get-capital-with-info-prompt.st")
    private Resource getCapitalWithInfoPrompt;

    @Override
    public Answer getCapitalWithInfo(GetCapitalRequest request) {
        PromptTemplate promptTemplate = new PromptTemplate(getCapitalWithInfoPrompt);
        Prompt prompt = promptTemplate.create(Map.of("stateorCountry", request.stateorCountry()));
        ChatResponse response = chatModel.call(prompt);
        String info = response.getResult().getOutput().getText().trim();
        return new Answer(info);
    }

    public Answer getAnswerWithRAG(Question question) {
        List<Document> retrievedDocs = vectorStore.similaritySearch(
            SearchRequest.builder()
                .query(question.question())
                .topK(5)
                .build()
        );

        List<String> retrievedTexts = retrievedDocs.stream()
            .map(Document::getText)
            .toList();

        PromptTemplate promptTemplate = new PromptTemplate(ragPromptTemplate);
        Prompt prompt = promptTemplate.create(Map.of(
            "input", question.question(),
            "documents", String.join("\n", retrievedTexts)
        ));
        ChatResponse response = chatModel.call(prompt);
        String answer = response.getResult().getOutput().getText().trim();
        return new Answer(answer);
    }
}