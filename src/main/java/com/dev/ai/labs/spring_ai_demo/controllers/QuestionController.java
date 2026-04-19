package com.dev.ai.labs.spring_ai_demo.controllers;

import com.dev.ai.labs.spring_ai_demo.model.Answer;
import com.dev.ai.labs.spring_ai_demo.model.GetCapitalRequest;
import com.dev.ai.labs.spring_ai_demo.model.Question;
import com.dev.ai.labs.spring_ai_demo.services.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class QuestionController {

    @Autowired
    private AIService aiService;

    @PostMapping("/ask")
    public Answer askQuestion(@RequestBody Question question) {
        return new Answer(aiService.getAnswer(question.question()));
    }

    @PostMapping("/capital")
    public Answer getCapital(@RequestBody GetCapitalRequest request) {
        return aiService.getCapital(request);
    }

    @PostMapping("/capitalWithInfo")
    public Answer getCapitalWithInfo(@RequestBody GetCapitalRequest request) {
        return aiService.getCapitalWithInfo(request);
    }

    @PostMapping("/rag")
    public Answer getAnswerWithRAG(@RequestBody Question question) {
        return aiService.getAnswerWithRAG(question);
    }
}