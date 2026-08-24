import java.util.*;

public class SimilaritySearch {

    // =========================================================
    // COSINE SIMILARITY
    // =========================================================

    public static double cosineSimilarity(String text1, String text2) {

        Map<String, Integer> vector1 = buildFrequencyVector(text1);
        Map<String, Integer> vector2 = buildFrequencyVector(text2);

        Set<String> allWords = new HashSet<>();

        allWords.addAll(vector1.keySet());
        allWords.addAll(vector2.keySet());

        double dotProduct = 0;
        double magnitude1 = 0;
        double magnitude2 = 0;

        for (String word : allWords) {

            int value1 = vector1.getOrDefault(word, 0);
            int value2 = vector2.getOrDefault(word, 0);

            dotProduct += value1 * value2;
            magnitude1 += value1 * value1;
            magnitude2 += value2 * value2;
        }

        if (magnitude1 == 0 || magnitude2 == 0) {
            return 0.0;
        }

        return dotProduct /
                (Math.sqrt(magnitude1) * Math.sqrt(magnitude2));
    }


    // =========================================================
    // BUILD WORD FREQUENCY VECTOR
    // =========================================================

    private static Map<String, Integer> buildFrequencyVector(
            String text) {

        Map<String, Integer> frequency =
                new HashMap<>();

        String cleanedText =
                text.toLowerCase()
                    .replaceAll("[^a-z0-9 ]", " ");

        String[] words =
                cleanedText.split("\\s+");

        for (String word : words) {

            if (!word.isEmpty()) {

                frequency.put(
                    word,
                    frequency.getOrDefault(word, 0) + 1
                );
            }
        }

        return frequency;
    }


    // =========================================================
    // EXTRACT MEDICINE NAME
    // =========================================================

    private static String getMedicineName(String record) {

        String[] lines = record.split("\\R");

        for (String line : lines) {

            if (line.toLowerCase().startsWith("medicine:")) {

                return line.substring(
                    "medicine:".length()
                ).trim();
            }
        }

        return "";
    }


    // =========================================================
    // SIMILARITY SEARCH
    // =========================================================

    public static void search(
            List<String> records,
            String query) {

        class Result {

            String medicine;
            String record;
            double similarity;

            Result(
                    String medicine,
                    String record,
                    double similarity) {

                this.medicine = medicine;
                this.record = record;
                this.similarity = similarity;
            }
        }

        List<Result> results =
                new ArrayList<>();


        // Calculate similarity between query
        // and every medicine record
        for (String record : records) {

            String medicineName =
                    getMedicineName(record);

            double similarity =
                    cosineSimilarity(query, record);

            results.add(
                new Result(
                    medicineName,
                    record,
                    similarity
                )
            );
        }


        // Highest similarity first
        results.sort(
            (a, b) ->
                Double.compare(
                    b.similarity,
                    a.similarity
                )
        );


        System.out.println();
        System.out.println(
            "=============================================="
        );

        System.out.println(
            "           SIMILARITY SEARCH RESULT"
        );

        System.out.println(
            "=============================================="
        );

        System.out.println(
            "Search Query: " + query
        );

        System.out.println();


        int shown = 0;

        for (Result result : results) {

            // Only show meaningful similarity results
            if (result.similarity > 0) {

                shown++;

                System.out.println(
                    "----------------------------------------------"
                );

                System.out.println(
                    "RANK " + shown
                );

                System.out.println(
                    "Medicine: " + result.medicine
                );

                System.out.printf(
                    "Cosine Similarity: %.4f%n",
                    result.similarity
                );

                System.out.println(
                    "----------------------------------------------"
                );

                System.out.println(
                    result.record
                );

                System.out.println();

                // Show top 3 results
                if (shown == 3) {
                    break;
                }
            }
        }


        if (shown == 0) {

            System.out.println(
                "No similar medicine records found."
            );
        }


        System.out.println(
            "Top similar records: " + shown
        );

        System.out.println(
            "=============================================="
        );
    }


    // =========================================================
    // MAIN METHOD
    // =========================================================

    public static void main(String[] args) {

        Scanner scanner =
                new Scanner(System.in);


        System.out.println(
            "=============================================="
        );

        System.out.println(
            "          PHARMA SIMILARITY SEARCH"
        );

        System.out.println(
            "=============================================="
        );

        System.out.print(
            "Enter symptoms or medicine-related keywords: "
        );

        String query =
                scanner.nextLine().trim();


        if (query.isEmpty()) {

            System.out.println(
                "Search query cannot be empty."
            );

            scanner.close();
            return;
        }


        List<String> records =
                PharmaSearch.readMedicineRecords(
                    "data/medicines.txt"
                );


        if (records.isEmpty()) {

            System.out.println(
                "No medicine records available."
            );

            scanner.close();
            return;
        }


        System.out.println(
            "Medicine records loaded: "
            + records.size()
        );


        search(records, query);

        scanner.close();
    }
}