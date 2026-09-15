package pharmasearch.co;

/**
 * CO1 - Algorithm Selection
 *
 * Represents the algorithmic strategy selection used
 * for different characteristics of medicine-search queries.
 */
public final class AlgorithmSelector {

    public enum Strategy {
        EXACT_PATTERN_MATCHING,
        HASH_BASED_PATTERN_MATCHING,
        FUZZY_MATCHING,
        SIMILARITY_MATCHING,
        COMBINED_SEARCH
    }

    private AlgorithmSelector() {
        // Utility class
    }

    /**
     * Selects an appropriate strategy based on the
     * characteristics of the search requirement.
     *
     * @param exactMatchRequired whether exact matching is required
     * @param largeTextSearch whether the search involves large text data
     * @param spellingVariation whether spelling variation is expected
     * @param semanticSimilarity whether similarity between text is required
     * @return selected strategy
     */
    public static Strategy select(
            boolean exactMatchRequired,
            boolean largeTextSearch,
            boolean spellingVariation,
            boolean semanticSimilarity) {

        if (spellingVariation) {
            return Strategy.FUZZY_MATCHING;
        }

        if (semanticSimilarity) {
            return Strategy.SIMILARITY_MATCHING;
        }

        if (largeTextSearch) {
            return Strategy.HASH_BASED_PATTERN_MATCHING;
        }

        if (exactMatchRequired) {
            return Strategy.EXACT_PATTERN_MATCHING;
        }

        return Strategy.COMBINED_SEARCH;
    }
}