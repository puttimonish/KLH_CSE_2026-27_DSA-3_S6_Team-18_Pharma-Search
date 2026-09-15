package pharmasearch.co;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * CO6 - Randomized Algorithm
 *
 * Randomly samples medicine records from a large corpus.
 * This can be used for efficient evaluation/testing of
 * search behaviour without processing the entire dataset.
 *
 * The algorithm uses a fixed seed when supplied so that
 * experiments can be reproduced.
 */
public final class RandomizedSearchSampler {

    private RandomizedSearchSampler() {
        // Utility class
    }

    /**
     * Returns a random sample of medicine records.
     *
     * @param records available records
     * @param sampleSize number of records to sample
     * @param seed random seed
     * @return randomly selected records
     */
    public static <T> List<T> sample(
            List<T> records,
            int sampleSize,
            long seed) {

        if (records == null
                || records.isEmpty()
                || sampleSize <= 0) {
            return new ArrayList<>();
        }

        int size =
                Math.min(sampleSize, records.size());

        List<T> shuffled =
                new ArrayList<>(records);

        Collections.shuffle(
                shuffled,
                new Random(seed)
        );

        return new ArrayList<>(
                shuffled.subList(0, size)
        );
    }

    /**
     * Returns a random sample using a
     * non-deterministic random generator.
     *
     * @param records available records
     * @param sampleSize number of records to sample
     * @return randomly selected records
     */
    public static <T> List<T> sample(
            List<T> records,
            int sampleSize) {

        if (records == null
                || records.isEmpty()
                || sampleSize <= 0) {
            return new ArrayList<>();
        }

        int size =
                Math.min(sampleSize, records.size());

        List<T> shuffled =
                new ArrayList<>(records);

        Collections.shuffle(shuffled);

        return new ArrayList<>(
                shuffled.subList(0, size)
        );
    }
}