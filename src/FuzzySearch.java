import java.util.*;

public class FuzzySearch {

    // Wagner-Fischer Edit Distance
    // Measures insertions, deletions and substitutions
    public static int editDistance(String a, String b) {

        a = a.toLowerCase();
        b = b.toLowerCase();

        int m = a.length();
        int n = b.length();

        int[][] dp = new int[m + 1][n + 1];

        // Base cases
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
        }

        // Dynamic Programming
        for (int i = 1; i <= m; i++) {

            for (int j = 1; j <= n; j++) {

                int cost;

                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    cost = 0;
                } else {
                    cost = 1;
                }

                dp[i][j] = Math.min(
                    Math.min(
                        dp[i - 1][j] + 1,
                        dp[i][j - 1] + 1
                    ),
                    dp[i - 1][j - 1] + cost
                );
            }
        }

        return dp[m][n];
    }


    // Extract medicine name from a complete record
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


    // Perform fuzzy search over medicine names
    public static void search(
            List<String> records,
            String query) {

        class Result {

            String medicine;
            String record;
            int distance;

            Result(
                    String medicine,
                    String record,
                    int distance) {

                this.medicine = medicine;
                this.record = record;
                this.distance = distance;
            }
        }

        List<Result> results = new ArrayList<>();

        for (String record : records) {

            String medicineName =
                    getMedicineName(record);

            if (!medicineName.isEmpty()) {

                int distance =
                        editDistance(query, medicineName);

                results.add(
                    new Result(
                        medicineName,
                        record,
                        distance
                    )
                );
            }
        }

        // Sort by smallest edit distance
        results.sort(
            Comparator.comparingInt(
                result -> result.distance
            )
        );

        System.out.println();
        System.out.println(
            "=============================================="
        );
        System.out.println(
            "             FUZZY SEARCH RESULT"
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

            /*
             * Display close matches.
             * The threshold adapts to query length.
             */
            int threshold =
                    Math.max(2, query.length() / 3);

            if (result.distance <= threshold) {

                shown++;

                System.out.println(
                    "----------------------------------------------"
                );

                System.out.println(
                    "MATCH " + shown
                );

                System.out.println(
                    "Medicine: " + result.medicine
                );

                System.out.println(
                    "Edit Distance: " + result.distance
                );

                System.out.println(
                    "----------------------------------------------"
                );

                System.out.println(result.record);

                System.out.println();

                if (shown == 3) {
                    break;
                }
            }
        }

        if (shown == 0) {

            System.out.println(
                "No close medicine match found."
            );
        }

        System.out.println(
            "Total fuzzy matches: " + shown
        );

        System.out.println(
            "=============================================="
        );
    }


    // Test program
    public static void main(String[] args) {

        Scanner scanner =
                new Scanner(System.in);

        System.out.println(
            "=============================================="
        );

        System.out.println(
            "          PHARMA FUZZY SEARCH"
        );

        System.out.println(
            "=============================================="
        );

        System.out.print(
            "Enter medicine name: "
        );

        String query =
                scanner.nextLine().trim();

        /*
         * Temporary test records.
         * PharmaSearch.java will later supply
         * the actual medicines.txt records.
         */
        List<String> records =
                PharmaSearch.readMedicineRecords(
                    "data/medicines.txt"
                );

        search(records, query);

        scanner.close();
    }
}