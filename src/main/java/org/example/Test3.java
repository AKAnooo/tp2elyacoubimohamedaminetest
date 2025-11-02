package org.example;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.googleai.GoogleAiEmbeddingModel;
import dev.langchain4j.model.googleai.GoogleAiEmbeddingModel.TaskType;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.store.embedding.CosineSimilarity;

import java.time.Duration;

public class Test3 {

    public static void main(String[] args) {
        String cle = System.getenv("GEMINI_KEY");

        EmbeddingModel modele = GoogleAiEmbeddingModel.builder()
                .apiKey(cle)
                .modelName("gemini-embedding-001")
                .taskType(TaskType.SEMANTIC_SIMILARITY)
                .outputDimensionality(256)
                .timeout(Duration.ofSeconds(20)) //
                .build();

        String s1 = "Le voleur s'est fait attrapé.";
        String s2 = "La police a fait son travail.";

        Response<Embedding> r1 = modele.embed(s1);
        Response<Embedding> r2 = modele.embed(s2);

        Embedding e1 = r1.content();
        Embedding e2 = r2.content();

        double similarite = CosineSimilarity.between(e1, e2);
        System.out.println("Similarité ≈ " + similarite);
    }
}
