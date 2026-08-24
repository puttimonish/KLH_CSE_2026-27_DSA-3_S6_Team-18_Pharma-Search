import java.io.*;
import java.util.*;

public class PharmaSearch {

    // =====================================================
    // KMP STRING MATCHING ALGORITHM
    // =====================================================

    public static boolean kmpSearch(String text, String pattern) {

        text = text.toLowerCase();
        pattern = pattern.toLowerCase();

        if (pattern.isEmpty()) {
            return true;
        }

        int[] lps = computeLPS(pattern);

        int i = 0;
        int j = 0;

        while (i < text.length()) {

            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;

                if (j == pattern.length()) {
                    return true;
                }

            } else {

                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }

        return false;
    }


    // =====================================================
    // COMPUTE LPS ARRAY
    // =====================================================

    private static int[] computeLPS(String pattern) {

        int[] lps = new int[pattern.length()];

        int length = 0;
        int i = 1;

        while (i < pattern.length()) {

            if (pattern.charAt(i) == pattern.charAt(length)) {

                length++;
                lps[i] = length;
                i++;

            } else {

                if (length != 0) {
                    length = lps[length - 1];

                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }

        return lps;
    }


    // =====================================================
    // READ MEDICINE CORPUS
    // =====================================================

    public static List<String> readMedicineRecords(String filePath) {

        List<String> records = new ArrayList<>();

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(filePath))) {

            StringBuilder record = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) {

                    if (record.length() > 0) {
                        records.add(record.toString().trim());
                        record.setLength(0);
                    }

                } else {

                    record.append(line).append("\n");
                }
            }

            // Add the last record
            if (record.length() > 0) {
                records.add(record.toString().trim());
            }

        } catch (IOException e) {

            System.out.println("ERROR: Unable to read medicine corpus.");
            System.out.println("File: " + filePath);
        }

        return records;
    }


    // =====================================================
    // SEARCH MEDICINE CORPUS USING KMP
    // =====================================================

    public static void searchMedicine(
            List<String> records,
            String searchText) {

        boolean found = false;
        int matchCount = 0;

        System.out.println();
        System.out.println("==============================================");
        System.out.println("             PHARMA SEARCH RESULT");
        System.out.println("==============================================");
        System.out.println("Search Query: " + searchText);
        System.out.println();

        for (String record : records) {

            if (kmpSearch(record, searchText)) {

                found = true;
                matchCount++;

                System.out.println("----------------------------------------------");
                System.out.println("MATCH " + matchCount);
                System.out.println("----------------------------------------------");
                System.out.println(record);
                System.out.println();
            }
        }

        if (!found) {

            System.out.println("----------------------------------------------");
            System.out.println("No matching medicine record found.");
            System.out.println("----------------------------------------------");
        }

        System.out.println();
        System.out.println("Total matching records: " + matchCount);
        System.out.println("==============================================");
    }


    // =====================================================
    // MAIN METHOD
    // =====================================================

   public static void main(String[] args) {

    Scanner scanner = new Scanner(System.in);

    String filePath = "data/medicines.txt";

    System.out.println("==============================================");
    System.out.println("             PHARMA SEARCH SYSTEM");
    System.out.println("==============================================");
    System.out.println("Data Source: " + filePath);
    System.out.println("Algorithms: KMP | Fuzzy Search | Similarity");
    System.out.println();

    List<String> medicineRecords =
            readMedicineRecords(filePath);

    if (medicineRecords.isEmpty()) {

        System.out.println("No medicine records available.");
        scanner.close();
        return;
    }

    System.out.println(
            "Medicine records loaded: "
                    + medicineRecords.size());

    while (true) {

        System.out.println();
        System.out.println("==============================================");
        System.out.println("              SEARCH MENU");
        System.out.println("==============================================");
        System.out.println("1. KMP Pattern Matching");
        System.out.println("2. Fuzzy Search");
        System.out.println("3. Similarity Search");
        System.out.println("4. Exit");
        System.out.println("==============================================");

        System.out.print("Enter your choice: ");

        String choice = scanner.nextLine().trim();

        switch (choice) {

            case "1":

                System.out.print(
                        "Enter medicine name or keyword: ");

                String kmpQuery =
                        scanner.nextLine().trim();

                if (!kmpQuery.isEmpty()) {

                    searchMedicine(
                            medicineRecords,
                            kmpQuery);

                } else {

                    System.out.println(
                            "Search query cannot be empty.");
                }

                break;


            case "2":

                System.out.print(
                        "Enter medicine name: ");

                String fuzzyQuery =
                        scanner.nextLine().trim();

                if (!fuzzyQuery.isEmpty()) {

                    FuzzySearch.search(
                            medicineRecords,
                            fuzzyQuery);

                } else {

                    System.out.println(
                            "Search query cannot be empty.");
                }

                break;


            case "3":

                System.out.print(
                        "Enter symptoms or keywords: ");

                String similarityQuery =
                        scanner.nextLine().trim();

                if (!similarityQuery.isEmpty()) {

                    SimilaritySearch.search(
                            medicineRecords,
                            similarityQuery);

                } else {

                    System.out.println(
                            "Search query cannot be empty.");
                }

                break;


            case "4":

                System.out.println();
                System.out.println(
                        "Thank you for using Pharma Search.");
                System.out.println(
                        "Exiting system...");

                scanner.close();
                return;


            default:

                System.out.println(
                        "Invalid choice. Please select 1-4.");
        }
    }
}
}