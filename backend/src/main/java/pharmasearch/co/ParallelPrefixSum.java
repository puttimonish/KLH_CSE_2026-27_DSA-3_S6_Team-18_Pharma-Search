package pharmasearch.co;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * CO6 - Parallel Algorithm
 *
 * Computes prefix sums using a parallel divide-and-conquer
 * strategy with Java's Fork/Join framework.
 *
 * For input:
 *   [1, 2, 3, 4]
 *
 * Output:
 *   [1, 3, 6, 10]
 *
 * Work: O(n)
 * Span: O(log n)
 */
public final class ParallelPrefixSum {

    private static final int SEQUENTIAL_THRESHOLD = 1_000;

    private ParallelPrefixSum() {
        // Utility class
    }

    /**
     * Computes an inclusive prefix sum in parallel.
     *
     * @param input input array
     * @return prefix-sum array
     */
    public static int[] compute(int[] input) {

        if (input == null) {
            return new int[0];
        }

        if (input.length == 0) {
            return new int[0];
        }

        int[] result =
                input.clone();

        ForkJoinPool.commonPool().invoke(
                new PrefixSumTask(
                        result,
                        0,
                        result.length
                )
        );

        return result;
    }

    /**
     * Divide-and-conquer task.
     */
    private static final class PrefixSumTask
            extends RecursiveAction {

        private final int[] values;
        private final int start;
        private final int end;

        private PrefixSumTask(
                int[] values,
                int start,
                int end) {

            this.values = values;
            this.start = start;
            this.end = end;
        }

        @Override
        protected void compute() {

            int length =
                    end - start;

            if (length <= SEQUENTIAL_THRESHOLD) {

                sequentialPrefixSum(
                        values,
                        start,
                        end
                );

                return;
            }

            int middle =
                    start + length / 2;

            PrefixSumTask left =
                    new PrefixSumTask(
                            values,
                            start,
                            middle
                    );

            PrefixSumTask right =
                    new PrefixSumTask(
                            values,
                            middle,
                            end
                    );

            invokeAll(left, right);

            int leftTotal =
                    values[middle - 1];

            addOffset(
                    values,
                    middle,
                    end,
                    leftTotal
            );
        }
    }

    /**
     * Sequential prefix sum for small segments.
     */
    private static void sequentialPrefixSum(
            int[] values,
            int start,
            int end) {

        for (int i = start + 1;
                i < end;
                i++) {

            values[i] +=
                    values[i - 1];
        }
    }

    /**
     * Adds the completed left-segment total
     * to every value in the right segment.
     */
    private static void addOffset(
            int[] values,
            int start,
            int end,
            int offset) {

        for (int i = start;
                i < end;
                i++) {

            values[i] += offset;
        }
    }
}