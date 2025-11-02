package org.example;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;

public class Test1 {

    public static void main(String[] args) {

        // Récupération de la clé API depuis les variables d'environnement
        String cle = System.getenv("GEMINI_KEY");

        // Création du modèle Gemini
        ChatModel modele = GoogleAiGeminiChatModel.builder()
                .apiKey(cle)
                .modelName("gemini-2.5-flash")
                .temperature(0.7)
                .build();

        // Pose une question et affiche la réponse
        String reponse = modele.chat("Comment s'appelle le chat de Pierre");
        System.out.println("Réponse du modèle : " + reponse);
    }
}
