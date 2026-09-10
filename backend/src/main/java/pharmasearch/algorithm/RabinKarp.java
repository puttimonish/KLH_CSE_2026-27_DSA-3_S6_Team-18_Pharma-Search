package pharmasearch.algorithm;

/**
 * Rabin-Karp pattern matching using a rolling hash.
 *
 * Hash matches are verified using an exact comparison
 * so hash collisions cannot create false matches.
 */
public final class RabinKarp {

    private static final long BASE = 256L;
    private static final long MOD = 1_000_000_007L;

    private RabinKarp() {
        // Utility class
    }

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

        int patternLength = pattern.length();

        long highPower = 1L;

        for (int i = 1; i < patternLength; i++) {
            highPower =
                    (highPower * BASE) % MOD;
        }

        long patternHash = 0L;
        long windowHash = 0L;

        for (int i = 0; i < patternLength; i++) {

            patternHash =
                    (patternHash * BASE
                            + pattern.charAt(i))
                            % MOD;

            windowHash =
                    (windowHash * BASE
                            + text.charAt(i))
                            % MOD;
        }

        for (
                int start = 0;
                start <= text.length() - patternLength;
                start++) {

            if (
                    patternHash == windowHash
                            && text.regionMatches(
                            start,
                            pattern,
                            0,
                            patternLength)
            ) {
                return true;
            }

            if (
                    start
                            < text.length() - patternLength) {

                long outgoing =
                        (text.charAt(start)
                                * highPower)
                                % MOD;

                windowHash =
                        (windowHash
                                - outgoing
                                + MOD)
                                % MOD;

                windowHash =
                        (windowHash * BASE
                                + text.charAt(
                                start + patternLength))
                                % MOD;
            }
        }

        return false;
    }
}