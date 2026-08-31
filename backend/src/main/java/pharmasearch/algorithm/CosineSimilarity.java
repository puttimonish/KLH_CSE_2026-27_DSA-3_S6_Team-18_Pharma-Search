package pharmasearch.algorithm;

import java.util.HashMap;
import java.util.Map;

public class CosineSimilarity {

    public static double calculate(String text1, String text2) {

        if (text1 == null || text2 == null) {
            return 0.0;
        }

        text1 = text1.toLowerCase().trim();
        text2 = text2.toLowerCase().trim();

        if (text1.isEmpty() || text2.isEmpty()) {
            return 0.0;
        }

        Map<String, Integer> vector1 = createFrequencyVector(text1);
        Map<String, Integer> vector2 = createFrequencyVector(text2);

        double dotProduct = 0.0;
        double magnitude1 = 0.0;
        double magnitude2 = 0.0;

        for (String word : vector1.keySet()) {

            if (vector2.containsKey(word)) {
                dotProduct +=
                        vector1.get(word) * vector2.get(word);
            }
        }

        for (int value : vector1.values()) {
            magnitude1 += value * value;
        }

        for (int value : vector2.values()) {
            magnitude2 += value * value;
        }

        magnitude1 = Math.sqrt(magnitude1);
        magnitude2 = Math.sqrt(magnitude2);

        if (magnitude1 == 0 || magnitude2 == 0) {
            return 0.0;
        }

        return dotProduct / (magnitude1 * magnitude2);
    }

    private static Map<String, Integer> createFrequencyVector(String text) {

        Map<String, Integer> vector = new HashMap<>();

        String[] words = text.split("\\s+");

        for (String word : words) {

            word = word.replaceAll("[^a-zA-Z0-9]", "");

            if (!word.isEmpty()) {
                vector.put(
                        word,
                        vector.getOrDefault(word, 0) + 1
                );
            }
        }

        return vector;
    }

    public static boolean isSimilar(
            String text1,
            String text2,
            double threshold) {

        return calculate(text1, text2) >= threshold;
    }
}