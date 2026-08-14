import java.io.*;
import java.util.*;

public class PharmaSearch {

    // =========================================================
    // KMP STRING MATCHING ALGORITHM
    // =========================================================

    public static boolean kmpSearch(String text, String pattern) {

        text = text.toLowerCase();
        pattern = pattern.toLowerCase();

        if (pattern.length() == 0) {
            return true;
        }

        int[] lps = computeLPS(pattern);

        int i = 0;
        int j = 0;

        while (i < text.length()) {

            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;

                // Complete pattern found
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


    // =========================================================
    // COMPUTE LPS ARRAY FOR KMP
    // =========================================================

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


    // =========================================================
    // READ MEDICINE RECORDS FROM TXT FILE
    // =========================================================

    public static List<String> readMedicineRecords(String filePath) {

        List<String> records = new ArrayList<>();

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(filePath))) {

            StringBuilder record = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {

                // Blank line means one medicine record is complete
                if (line.trim().isEmpty()) {

                    if (record.length() > 0) {
                        records.add(record.toString().trim());
                        record.setLength(0);
                    }

                } else {

                    record.append(line).append("\n");
                }
            }

            // Add the final record if the file doesn't end
            // with a blank line
            if (record.length() > 0) {
                records.add(record.toString().trim());
            }

        } catch (FileNotFoundException e) {

            System.out.println("ERROR: Medicine corpus file not found.");
            System.out.println("Expected location: " + filePath);

        } catch (IOException e) {

            System.out.println("ERROR: Unable to read the medicine corpus.");
        }

        return records;
    }


    // =========================================================
    // SEARCH MEDICINE CORPUS USING KMP
    // =========================================================

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

            // KMP searches the complete medicine record
            if (kmpSearch(record, searchText)) {

                matchCount++;
                found = true;

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


    // =========================================================
    // MAIN METHOD
    // =========================================================

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // Project corpus stored in the data folder
        String filePath = "data/medicines.txt";

        System.out.println("==============================================");
        System.out.println("           PHARMA SEARCH SYSTEM");
        System.out.println("==============================================");
        System.out.println("Data Source: " + filePath);
        System.out.println("String Matching: KMP Algorithm");
        System.out.println();

        List<String> medicineRecords =
                readMedicineRecords(filePath);

        if (medicineRecords.isEmpty()) {

            System.out.println("No medicine records available.");
            scanner.close();
            return;
        }

        System.out.println("Medicine records loaded: "
                + medicineRecords.size());

        System.out.println();
        System.out.print(
                "Enter medicine name or keyword to search: ");

        String searchText = scanner.nextLine().trim();

        if (searchText.isEmpty()) {

            System.out.println("Search query cannot be empty.");
            scanner.close();
            return;
        }

        searchMedicine(medicineRecords, searchText);

        scanner.close();
    }
}
