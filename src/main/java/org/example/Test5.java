package org.example;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.googleai.GoogleAiEmbeddingModel;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Test 5 : RAG avec un PDF et conversation multi-questions
 */
public class Test5 {

    interface Assistant {
        String chat(String userMessage);
    }

    public static void main(String[] args) {

        // 1) Nettoyage des logs
        String llmKey = System.getenv("GEMINI_KEY");
        Logger.getLogger("ai.djl").setLevel(Level.SEVERE);
        Logger.getLogger("ai.djl.huggingface.tokenizers").setLevel(Level.SEVERE);
        Logger root = Logger.getLogger("");
        root.setLevel(Level.SEVERE);
        for (var h : root.getHandlers()) h.setLevel(Level.SEVERE);

        // 2) Configuration du modèle LLM (Gemini)
        ChatModel model = GoogleAiGeminiChatModel.builder()
                .apiKey(llmKey)
                .modelName("gemini-2.5-flash")
                .temperature(0.5)
                .build();

        // 3) Charger le document PDF (support de cours ML)
        String nomDocument = "ml.pdf";
        Document document = FileSystemDocumentLoader.loadDocument(nomDocument);

        // 4) Créer une base vectorielle en mémoire
        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

        // 5) Calculer et enregistrer les embeddings du PDF
        EmbeddingModel embeddingModel = GoogleAiEmbeddingModel.builder()
                .apiKey(llmKey)
                .modelName("gemini-embedding-001")
                .build();
        EmbeddingStoreIngestor.ingest(document, embeddingStore);

        // 6) Construire l’assistant conversationnel avec mémoire + RAG
        Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(model)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
                .build();

        // 7) Lancer la conversation interactive (boucle Scanner)
        conversationAvec(assistant);
    }

    /**
     * Méthode utilitaire : permet de poser plusieurs questions à l’assistant.
     */
    private static void conversationAvec(Assistant assistant) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("Posez votre question (ou tapez '/' pour arrêter) : ");
                String question = scanner.nextLine();
                if (question == null || question.isBlank()) continue;
                if ("/".equalsIgnoreCase(question.trim())) {
                    System.out.println("Fin de la conversation");
                    break;
                }


                String reponse = assistant.chat(question);
                System.out.println("Assistant : " + reponse);

            }
        }
    }
}
