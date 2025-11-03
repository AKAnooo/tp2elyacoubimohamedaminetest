package org.example;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.service.AiServices;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Test 6 : Outil météo + mémoire de conversation
 */
public class Test6 {

    // Assistant conversationnel (comme Test4)
    interface Assistant {
        String chat(String userMessage);
    }

    public static void main(String[] args) {

        // -- Réduction des logs verbeux (comme d'hab)(c'est la manière que j'ai trouvé grace à chatgpt pour enlever une erreur non fatale.)
        Logger.getLogger("ai.djl").setLevel(Level.SEVERE);
        Logger root = Logger.getLogger("");
        root.setLevel(Level.SEVERE);
        for (var h : root.getHandlers()) h.setLevel(Level.SEVERE);

        // -- Clé API
        String apiKey = System.getenv("GEMINI_KEY");


        // -- Modèle LLM (température basse = plus factuel)
        ChatModel model = GoogleAiGeminiChatModel.builder()
                .apiKey(apiKey)
                .modelName("gemini-2.5-flash")
                .temperature(0.3)
                .build();

        // -- Mémoire de conversation
        var memory = MessageWindowChatMemory.withMaxMessages(10);

        // -- Assistant relié au modèle, à la mémoire ET à l’outil météo
        Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(model)
                .chatMemory(memory)
                .tools(new MeteoTool())
                .build();

        // -- Boucle interactive (pour povoir poser plusieurs questions)
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {

                System.out.print("Pose ta question (ou '/' pour quitter) : ");
                String q = scanner.nextLine();
                if (q == null || q.isBlank()) continue;
                if ("/".equalsIgnoreCase(q.trim())) break;

                String reply = assistant.chat(q);
                System.out.println("Assistant : " + reply);
            }
        }
    }
}
