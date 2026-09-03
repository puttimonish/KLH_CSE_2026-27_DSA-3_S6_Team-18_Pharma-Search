package pharmasearch.service;

import pharmasearch.algorithm.CosineSimilarity;
import pharmasearch.algorithm.EditDistance;
import pharmasearch.algorithm.KMPAlgorithm;
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
     * Ranking uses:
     * 1. KMP Pattern Matching
     * 2. Edit Distance
     * 3. Cosine Similarity
     *
     * A two-stage approach is used for better performance
     * on the large medicine dataset.
     */
    public List<Medicine> search(String query) {

        List<Medicine> medicines = repository.getAllMedicines();

        List<SearchResult> results = new ArrayList<>();

        if (query == null) {
            return new ArrayList<>();
        }

        String searchQuery = normalize(query);

        if (searchQuery.isEmpty()) {
            return new ArrayList<>();
        }

        /*
         * -------------------------------------------------
         * STAGE 1
         * -------------------------------------------------
         *
         * Use KMP to quickly identify medicines where
         * the query occurs as a pattern.
         *
         * This avoids performing expensive fuzzy calculations
         * on every field of every medicine.
         */
        for (Medicine medicine : medicines) {

            String name = normalize(medicine.getName());
            String composition1 = normalize(medicine.getComposition1());
            String composition2 = normalize(medicine.getComposition2());
            String manufacturer = normalize(medicine.getManufacturer());
            String packSize = normalize(medicine.getPackSize());
            String type = normalize(medicine.getType());

            /*
             * Medicine name is the most important field.
             */
            boolean nameKmp = KMPAlgorithm.contains(
                    name,
                    searchQuery
            );

            /*
             * Composition is also highly important.
             */
            boolean composition1Kmp = KMPAlgorithm.contains(
                    composition1,
                    searchQuery
            );

            boolean composition2Kmp = KMPAlgorithm.contains(
                    composition2,
                    searchQuery
            );

            /*
             * Other fields.
             */
            boolean manufacturerKmp = KMPAlgorithm.contains(
                    manufacturer,
                    searchQuery
            );

            boolean packKmp = KMPAlgorithm.contains(
                    packSize,
                    searchQuery
            );

            boolean typeKmp = KMPAlgorithm.contains(
                    type,
                    searchQuery
            );

            /*
             * -------------------------------------------------
             * FAST RELEVANCE CHECK
             * -------------------------------------------------
             *
             * If there is no KMP match anywhere, we only
             * perform fuzzy matching on the medicine name.
             *
             * This keeps typo-tolerant search available without
             * calculating expensive metrics for every field.
             */
            boolean anyKmpMatch =
                    nameKmp
                    || composition1Kmp
                    || composition2Kmp
                    || manufacturerKmp
                    || packKmp
                    || typeKmp;

            double finalScore = 0.0;

            /*
             * -------------------------------------------------
             * NAME SCORE
             * -------------------------------------------------
             */
            if (nameKmp) {

                FieldScore nameScore =
                        calculateFieldScore(
                                searchQuery,
                                name
                        );

                finalScore += nameScore.score * 5.0;
            }

            /*
             * -------------------------------------------------
             * COMPOSITION SCORE
             * -------------------------------------------------
             */
            if (composition1Kmp) {

                FieldScore compositionScore =
                        calculateFieldScore(
                                searchQuery,
                                composition1
                        );

                finalScore += compositionScore.score * 4.0;
            }

            if (composition2Kmp) {

                FieldScore compositionScore =
                        calculateFieldScore(
                                searchQuery,
                                composition2
                        );

                finalScore += compositionScore.score * 4.0;
            }

            /*
             * -------------------------------------------------
             * MANUFACTURER SCORE
             * -------------------------------------------------
             */
            if (manufacturerKmp) {

                FieldScore manufacturerScore =
                        calculateFieldScore(
                                searchQuery,
                                manufacturer
                        );

                finalScore += manufacturerScore.score * 2.0;
            }

            /*
             * -------------------------------------------------
             * PACK SIZE SCORE
             * -------------------------------------------------
             */
            if (packKmp) {

                FieldScore packScore =
                        calculateFieldScore(
                                searchQuery,
                                packSize
                        );

                finalScore += packScore.score;
            }

            /*
             * -------------------------------------------------
             * TYPE SCORE
             * -------------------------------------------------
             */
            if (typeKmp) {

                FieldScore typeScore =
                        calculateFieldScore(
                                searchQuery,
                                type
                        );

                finalScore += typeScore.score;
            }

            /*
             * -------------------------------------------------
             * FUZZY NAME SEARCH
             * -------------------------------------------------
             *
             * If KMP did not find a match, check the medicine
             * name using Edit Distance + Cosine Similarity.
             *
             * This allows searches such as:
             *
             * paracetmol
             * paracitamol
             * amoxcillin
             *
             * while avoiding expensive calculations on all
             * other fields.
             */
            if (!anyKmpMatch && !name.isEmpty()) {

                double fuzzyScore =
                        calculateFuzzyNameScore(
                                searchQuery,
                                name
                        );

                if (fuzzyScore >= 0.35) {
                    finalScore = fuzzyScore * 5.0;
                }
            }

            /*
             * -------------------------------------------------
             * EXACT MATCH BOOSTS
             * -------------------------------------------------
             */

            if (name.equals(searchQuery)) {
                finalScore += 10.0;
            }

            if (composition1.equals(searchQuery)
                    || composition2.equals(searchQuery)) {

                finalScore += 8.0;
            }

            if (manufacturer.equals(searchQuery)) {
                finalScore += 5.0;
            }

            if (packSize.equals(searchQuery)) {
                finalScore += 2.0;
            }

            if (type.equals(searchQuery)) {
                finalScore += 2.0;
            }

            /*
             * -------------------------------------------------
             * ADD RELEVANT RESULT
             * -------------------------------------------------
             */
            if (finalScore > 0) {

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
         * SORT BY RELEVANCE
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
                Math.min(20, results.size());

        for (int i = 0; i < limit; i++) {

            output.add(
                    results.get(i).getMedicine()
            );
        }

        return output;
    }

    /**
     * Calculates fuzzy similarity between the query and
     * individual words in the medicine name.
     *
     * This allows searches such as:
     * paracetmol -> paracetamol
     * amoxcillin -> amoxicillin
     */
    private double calculateFuzzyNameScore(
            String query,
            String medicineName) {

        if (query == null || medicineName == null) {
            return 0.0;
        }

        query = normalize(query);
        medicineName = normalize(medicineName);

        if (query.isEmpty() || medicineName.isEmpty()) {
            return 0.0;
        }

        String[] words = medicineName.split("\\s+");

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

            double editSimilarity =
                    1.0
                    - ((double) distance / maxLength);

            double cosineSimilarity =
                    CosineSimilarity.calculate(
                            query,
                            word
                    );

            double score =
                    (editSimilarity * 0.8)
                    + (cosineSimilarity * 0.2);

            if (score > bestScore) {
                bestScore = score;
            }
        }

        return bestScore;
    }

    /**
     * Calculates the search score for one field.
     *
     * Combines:
     * KMP
     * Edit Distance
     * Cosine Similarity
     */
    private FieldScore calculateFieldScore(
            String query,
            String field) {

        if (field == null || field.isEmpty()) {
            return new FieldScore(0.0);
        }

        /*
         * -------------------------------------------------
         * 1. KMP PATTERN MATCHING
         * -------------------------------------------------
         */
        boolean kmpMatch =
                KMPAlgorithm.contains(
                        field,
                        query
                );

        /*
         * -------------------------------------------------
         * 2. EDIT DISTANCE
         * -------------------------------------------------
         */
        int editDistance =
                EditDistance.calculate(
                        query,
                        field
                );

        /*
         * -------------------------------------------------
         * 3. COSINE SIMILARITY
         * -------------------------------------------------
         */
        double cosineScore =
                CosineSimilarity.calculate(
                        query,
                        field
                );

        /*
         * -------------------------------------------------
         * COMBINED SCORE
         * -------------------------------------------------
         */
        double score =
                cosineScore;

        /*
         * KMP substring match receives a strong boost.
         */
        if (kmpMatch) {
            score += 1.0;
        }

        /*
         * Smaller edit distance gives a higher score.
         */
        score +=
                1.0 /
                (1.0 + editDistance);

        return new FieldScore(score);
    }

    /**
     * Normalizes search text.
     *
     * Converts text to lowercase,
     * removes unnecessary whitespace,
     * and handles null values safely.
     */
    private String normalize(String value) {

        if (value == null) {
            return "";
        }

        return value
                .toLowerCase(Locale.ROOT)
                .trim()
                .replaceAll("\\s+", " ");
    }

    /**
     * Stores the score of an individual field.
     */
    private static class FieldScore {

        private final double score;

        public FieldScore(double score) {
            this.score = score;
        }
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

            this.medicine = medicine;
            this.score = score;
        }

        public Medicine getMedicine() {
            return medicine;
        }

        public double getScore() {
            return score;
        }
    }
}