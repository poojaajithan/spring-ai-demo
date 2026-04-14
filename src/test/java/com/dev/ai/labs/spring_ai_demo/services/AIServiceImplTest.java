package com.dev.ai.labs.spring_ai_demo.services;

import com.dev.ai.labs.spring_ai_demo.BaseTestClass;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AIServiceImplTest extends BaseTestClass {

    @Autowired
    private AIServiceImpl aiService;

    @Test
    void testGetAnswer() {
        String question = "Give me 5 sentences that must end with word apple.";
        String answer = aiService.getAnswer(question);
        System.out.println("Question: " + question);
        System.out.println("Answer: " + answer);
    }
}
