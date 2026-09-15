package pharmasearch.algorithm;

/**
 * Space-optimized Dynamic Programming implementation
 * of Levenshtein Edit Distance.
 *
 * Time Complexity:
 * O(m * n)
 *
 * Space Complexity:
 * O(min(m, n))
 *
 * CO3:
 * Demonstrates dynamic-programming optimization by
 * reducing the standard 2D DP table to one dimension.
 */
public final class EditDistanceOptimized {

    private EditDistanceOptimized() {
        // Utility class
    }

    public static int calculate(String a, String b) {

        if (a == null || b == null) {
            return -1;
        }

        a = a.toLowerCase();
        b = b.toLowerCase();

        // Keep the second string as the shorter dimension.
        if (a.length() < b.length()) {
            String temp = a;
            a = b;
            b = temp;
        }

        int m = a.length();
        int n = b.length();

        int[] previous = new int[n + 1];
        int[] current = new int[n + 1];

        for (int j = 0; j <= n; j++) {
            previous[j] = j;
        }

        for (int i = 1; i <= m; i++) {

            current[0] = i;

            for (int j = 1; j <= n; j++) {

                if (a.charAt(i - 1) == b.charAt(j - 1)) {

                    current[j] =
                            previous[j - 1];

                } else {

                    int insert =
                            current[j - 1];

                    int delete =
                            previous[j];

                    int replace =
                            previous[j - 1];

                    current[j] =
                            1 + Math.min(
                                    insert,
                                    Math.min(
                                            delete,
                                            replace
                                    )
                            );
                }
            }

            int[] temp = previous;
            previous = current;
            current = temp;
        }

        return previous[n];
    }

    public static boolean isSimilar(
            String a,
            String b,
            int threshold) {

        int distance =
                calculate(a, b);

        return distance >= 0
                && distance <= threshold;
    }
}