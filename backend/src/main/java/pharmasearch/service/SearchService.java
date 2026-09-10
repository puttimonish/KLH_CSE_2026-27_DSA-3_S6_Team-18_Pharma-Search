package pharmasearch.service;

import pharmasearch.algorithm.CosineSimilarity;
import pharmasearch.algorithm.EditDistance;
import pharmasearch.algorithm.KMPAlgorithm;
import pharmasearch.algorithm.RabinKarp;
import pharmasearch.model.Medicine;
import pharmasearch.repository.MedicineRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class SearchService {

    private final MedicineRepository repository;

    public SearchService(MedicineRepository repository) {
        this.repository = repository;
    }

    /**
     * Returns the total number of medicines loaded.
     */
    public int getMedicineCount() {
        return repository.size();
    }

    /**
     * Intelligent medicine search.
     *
     * Searches across:
     * 1. Medicine name
     * 2. Composition 1
     * 3. Composition 2
     * 4. Manufacturer
     * 5. Pack size
     * 6. Medicine type
     *
     * Final ranking:
     *
     * KMP Score      = 40%
     * Fuzzy Score    = 30%
     * Cosine Score   = 30%
     *
     * Rabin-Karp is used as an additional exact-pattern
     * detection method.
     */
    public List<Medicine> search(String query) {

        List<Medicine> medicines =
                repository.getAllMedicines();

        List<SearchResult> results =
                new ArrayList<>();

        if (query == null) {
            return new ArrayList<>();
        }

        String searchQuery =
                normalize(query);

        if (searchQuery.isEmpty()) {
            return new ArrayList<>();
        }

        /*
         * -------------------------------------------------
         * SEARCH THROUGH ALL MEDICINES
         * -------------------------------------------------
         */
        for (Medicine medicine : medicines) {

            String name =
                    normalize(medicine.getName());

            String composition1 =
                    normalize(medicine.getComposition1());

            String composition2 =
                    normalize(medicine.getComposition2());

            String manufacturer =
                    normalize(medicine.getManufacturer());

            String packSize =
                    normalize(medicine.getPackSize());

            String type =
                    normalize(medicine.getType());

            /*
             * -------------------------------------------------
             * 1. KMP MATCHING
             * -------------------------------------------------
             */

            boolean nameKmp =
                    KMPAlgorithm.contains(
                            name,
                            searchQuery
                    );

            boolean composition1Kmp =
                    KMPAlgorithm.contains(
                            composition1,
                            searchQuery
                    );

            boolean composition2Kmp =
                    KMPAlgorithm.contains(
                            composition2,
                            searchQuery
                    );

            boolean manufacturerKmp =
                    KMPAlgorithm.contains(
                            manufacturer,
                            searchQuery
                    );

            boolean packKmp =
                    KMPAlgorithm.contains(
                            packSize,
                            searchQuery
                    );

            boolean typeKmp =
                    KMPAlgorithm.contains(
                            type,
                            searchQuery
                    );

            /*
             * KMP score:
             *
             * 1.0 = exact pattern found
             * 0.0 = no pattern found
             */
            double kmpScore =
                    (
                            nameKmp
                            || composition1Kmp
                            || composition2Kmp
                            || manufacturerKmp
                            || packKmp
                            || typeKmp
                    )
                    ? 1.0
                    : 0.0;

            /*
             * -------------------------------------------------
             * 2. RABIN-KARP MATCHING
             * -------------------------------------------------
             *
             * Rabin-Karp is used as an additional exact
             * pattern detector.
             *
             * It does NOT add another percentage to the
             * final ranking formula.
             */
            boolean nameRabinKarp =
                    RabinKarp.contains(
                            name,
                            searchQuery
                    );

            boolean composition1RabinKarp =
                    RabinKarp.contains(
                            composition1,
                            searchQuery
                    );

            boolean composition2RabinKarp =
                    RabinKarp.contains(
                            composition2,
                            searchQuery
                    );

            boolean manufacturerRabinKarp =
                    RabinKarp.contains(
                            manufacturer,
                            searchQuery
                    );

            boolean packRabinKarp =
                    RabinKarp.contains(
                            packSize,
                            searchQuery
                    );

            boolean typeRabinKarp =
                    RabinKarp.contains(
                            type,
                            searchQuery
                    );

            boolean rabinKarpMatch =
                    nameRabinKarp
                    || composition1RabinKarp
                    || composition2RabinKarp
                    || manufacturerRabinKarp
                    || packRabinKarp
                    || typeRabinKarp;

            /*
             * -------------------------------------------------
             * 3. FUZZY SCORE
             * -------------------------------------------------
             *
             * Fuzzy matching compares the query against
             * individual words in the medicine name.
             *
             * The score is normalized between 0 and 1.
             */
            double fuzzyScore =
                    calculateFuzzyNameScore(
                            searchQuery,
                            name
                    );

            /*
             * -------------------------------------------------
             * 4. COSINE SCORE
             * -------------------------------------------------
             *
             * Calculate cosine similarity against all
             * searchable fields and keep the strongest
             * similarity.
             *
             * This produces a normalized 0-1 score.
             */
            double cosineScore =
                    calculateCosineScore(
                            searchQuery,
                            name,
                            composition1,
                            composition2,
                            manufacturer,
                            packSize,
                            type
                    );

            /*
             * -------------------------------------------------
             * 5. FINAL 40 / 30 / 30 SCORE
             * -------------------------------------------------
             *
             * KMP       = 40%
             * Fuzzy     = 30%
             * Cosine    = 30%
             */
            double finalScore =
                    (kmpScore * 0.40)
                    + (fuzzyScore * 0.30)
                    + (cosineScore * 0.30);

            /*
             * -------------------------------------------------
             * RESULT FILTER
             * -------------------------------------------------
             *
             * Exact KMP/Rabin-Karp matches are included.
             *
             * Fuzzy-only matches are included when their
             * similarity is strong enough.
             */
            boolean relevant =
                    kmpScore > 0.0
                    || rabinKarpMatch
                    || fuzzyScore >= 0.35
                    || cosineScore >= 0.35;

            if (relevant && finalScore > 0.0) {

                results.add(
                        new SearchResult(
                                medicine,
                                finalScore
                        )
                );
            }
        }

        /*
         * -------------------------------------------------
         * SORT BY FINAL RELEVANCE SCORE
         * -------------------------------------------------
         */
        results.sort(
                Comparator.comparingDouble(
                        SearchResult::getScore
                ).reversed()
        );

        /*
         * -------------------------------------------------
         * RETURN TOP 20
         * -------------------------------------------------
         */
        List<Medicine> output =
                new ArrayList<>();

        int limit =
                Math.min(
                        20,
                        results.size()
                );

        for (int i = 0; i < limit; i++) {

            output.add(
                    results.get(i).getMedicine()
            );
        }

        return output;
    }

    /**
     * Calculates fuzzy similarity between the query
     * and individual words in the medicine name.
     *
     * Uses normalized Edit Distance.
     *
     * Examples:
     *
     * paracetmol -> paracetamol
     * amoxcillin -> amoxicillin
     */
    private double calculateFuzzyNameScore(
            String query,
            String medicineName) {

        if (
                query == null
                || medicineName == null
        ) {
            return 0.0;
        }

        query =
                normalize(query);

        medicineName =
                normalize(medicineName);

        if (
                query.isEmpty()
                || medicineName.isEmpty()
        ) {
            return 0.0;
        }

        String[] words =
                medicineName.split("\\s+");

        double bestScore = 0.0;

        for (String word : words) {

            if (word.isEmpty()) {
                continue;
            }

            int distance =
                    EditDistance.calculate(
                            query,
                            word
                    );

            int maxLength =
                    Math.max(
                            query.length(),
                            word.length()
                    );

            if (maxLength == 0) {
                continue;
            }

            /*
             * Normalize edit distance into a
             * similarity score between 0 and 1.
             */
            double editSimilarity =
                    1.0
                    - (
                        (double) distance
                        / maxLength
                    );

            /*
             * Keep score inside 0-1.
             */
            editSimilarity =
                    Math.max(
                            0.0,
                            Math.min(
                                    1.0,
                                    editSimilarity
                            )
                    );

            if (
                    editSimilarity
                    > bestScore
            ) {
                bestScore =
                        editSimilarity;
            }
        }

        return bestScore;
    }

    /**
     * Calculates the strongest Cosine Similarity
     * across all searchable medicine fields.
     *
     * Cosine similarity is normalized between 0 and 1.
     */
    private double calculateCosineScore(
            String query,
            String name,
            String composition1,
            String composition2,
            String manufacturer,
            String packSize,
            String type) {

        double bestScore = 0.0;

        bestScore =
                Math.max(
                        bestScore,
                        calculateCosine(
                                query,
                                name
                        )
                );

        bestScore =
                Math.max(
                        bestScore,
                        calculateCosine(
                                query,
                                composition1
                        )
                );

        bestScore =
                Math.max(
                        bestScore,
                        calculateCosine(
                                query,
                                composition2
                        )
                );

        bestScore =
                Math.max(
                        bestScore,
                        calculateCosine(
                                query,
                                manufacturer
                        )
                );

        bestScore =
                Math.max(
                        bestScore,
                        calculateCosine(
                                query,
                                packSize
                        )
                );

        bestScore =
                Math.max(
                        bestScore,
                        calculateCosine(
                                query,
                                type
                        )
                );

        return bestScore;
    }

    /**
     * Safely calculates cosine similarity for one field.
     */
    private double calculateCosine(
            String query,
            String field) {

        if (
                field == null
                || field.isEmpty()
        ) {
            return 0.0;
        }

        double score =
                CosineSimilarity.calculate(
                        query,
                        field
                );

        return Math.max(
                0.0,
                Math.min(
                        1.0,
                        score
                )
        );
    }

    /**
     * Normalizes search text.
     *
     * Converts text to lowercase,
     * removes unnecessary whitespace,
     * and handles null values safely.
     */
    private String normalize(
            String value) {

        if (value == null) {
            return "";
        }

        return value
                .toLowerCase(Locale.ROOT)
                .trim()
                .replaceAll(
                        "\\s+",
                        " "
                );
    }

    /**
     * Stores a medicine and its final ranking score.
     */
    private static class SearchResult {

        private final Medicine medicine;
        private final double score;

        public SearchResult(
                Medicine medicine,
                double score) {

            this.medicine =
                    medicine;

            this.score =
                    score;
        }

        public Medicine getMedicine() {
            return medicine;
        }

        public double getScore() {
            return score;
        }
    }
}