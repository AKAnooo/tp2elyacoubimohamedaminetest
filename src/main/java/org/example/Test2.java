package org.example;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;

import java.util.Map;

public class Test2 {
    public static void main(String[] args) {
        String cle = System.getenv("GEMINI_KEY");

        ChatModel modele = GoogleAiGeminiChatModel.builder()
                .apiKey(cle)
                .modelName("gemini-2.5-flash")
                .temperature(0.7)
                .build();

        PromptTemplate template = PromptTemplate.from("Traduis le texte suivant en espagnol : {{texte}}");
        Prompt prompt = template.apply(Map.of("texte", "Je m'appelle Amine, ravi de vous rencontrer"));

        ChatRequest req = ChatRequest.builder()
                .messages(prompt.toUserMessage())
                .build();

        ChatResponse resp = modele.chat(req);
        System.out.println(resp.aiMessage().text());
    }
}
