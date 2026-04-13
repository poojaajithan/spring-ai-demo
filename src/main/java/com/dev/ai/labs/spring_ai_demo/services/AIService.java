package com.dev.ai.labs.spring_ai_demo.services;

import com.dev.ai.labs.spring_ai_demo.model.Answer;
import com.dev.ai.labs.spring_ai_demo.model.GetCapitalRequest;

public interface AIService {
    String getAnswer(String question);

    Answer getCapital(GetCapitalRequest request);

    Answer getCapitalWithInfo(GetCapitalRequest request);
}