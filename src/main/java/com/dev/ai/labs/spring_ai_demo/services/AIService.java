package com.dev.ai.labs.spring_ai_demo.services;

import com.dev.ai.labs.spring_ai_demo.model.Answer;
import com.dev.ai.labs.spring_ai_demo.model.GetCapitalRequest;
import com.dev.ai.labs.spring_ai_demo.model.Question;

public interface AIService {
    String getAnswer(String question);

    Answer getCapital(GetCapitalRequest request);

    Answer getCapitalWithInfo(GetCapitalRequest request);

    Answer getAnswerWithRAG(Question question);
}