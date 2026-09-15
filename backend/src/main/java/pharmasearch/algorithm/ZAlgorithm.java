package pharmasearch.algorithm;

/**
 * Z-Algorithm for linear-time pattern matching.
 *
 * The Z array stores, for every position i, the length
 * of the longest substring starting at i that matches
 * the prefix of the string.
 *
 * Time Complexity:
 *   O(n)
 *
 * Space Complexity:
 *   O(n)
 */
public final class ZAlgorithm {

    private ZAlgorithm() {
        // Utility class
    }

    /**
     * Builds the Z-array for the supplied string.
     *
     * @param text input string
     * @return Z-array
     */
    public static int[] buildZArray(String text) {

        if (text == null || text.isEmpty()) {
            return new int[0];
        }

        int n = text.length();
        int[] z = new int[n];

        int left = 0;
        int right = 0;

        for (int i = 1; i < n; i++) {

            if (i <= right) {
                z[i] =
                        Math.min(
                                right - i + 1,
                                z[i - left]
                        );
            }

            while (
                    i + z[i] < n
                            && text.charAt(z[i])
                            == text.charAt(i + z[i])
            ) {
                z[i]++;
            }

            if (i + z[i] - 1 > right) {
                left = i;
                right = i + z[i] - 1;
            }
        }

        return z;
    }

    /**
     * Checks whether a pattern occurs in the text.
     *
     * @param text input text
     * @param pattern search pattern
     * @return true when pattern occurs
     */
    public static boolean contains(
            String text,
            String pattern) {

        if (text == null || pattern == null) {
            return false;
        }

        if (pattern.isEmpty()) {
            return true;
        }

        if (pattern.length() > text.length()) {
            return false;
        }

        String combined =
                pattern + "$" + text;

        int[] z = buildZArray(combined);

        int patternLength =
                pattern.length();

        for (int value : z) {

            if (value == patternLength) {
                return true;
            }
        }

        return false;
    }
}