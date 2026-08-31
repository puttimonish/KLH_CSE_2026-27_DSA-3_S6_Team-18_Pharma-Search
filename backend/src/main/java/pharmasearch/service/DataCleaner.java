package pharmasearch.service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class DataCleaner {

    private static final String INPUT_FILE =
            "data/raw/indian_medicine_data.csv";

    private static final String OUTPUT_FILE =
            "data/processed/medicines_clean.csv";

    private static final String REPORT_FILE =
            "results/data_cleaning_report.txt";

    public static void main(String[] args) {

        int totalRecords = 0;
        int validRecords = 0;
        int discontinuedRecords = 0;
        int duplicateRecords = 0;
        int invalidRecords = 0;

        Set<String> uniqueMedicines = new HashSet<>();

        Path outputPath = Paths.get(OUTPUT_FILE);
        Path reportPath = Paths.get(REPORT_FILE);

        try {

            Files.createDirectories(outputPath.getParent());
            Files.createDirectories(reportPath.getParent());

            try (
                BufferedReader reader = Files.newBufferedReader(
                        Paths.get(INPUT_FILE),
                        StandardCharsets.UTF_8);

                BufferedWriter writer = Files.newBufferedWriter(
                        outputPath,
                        StandardCharsets.UTF_8);

                BufferedWriter report = Files.newBufferedWriter(
                        reportPath,
                        StandardCharsets.UTF_8)
            ) {

                // Header
                reader.readLine();

                writer.write(
                    "id,name,price,manufacturer,type,pack_size,"
                    + "composition1,composition2"
                );
                writer.newLine();

                String line;

                while ((line = reader.readLine()) != null) {

                    totalRecords++;

                    List<String> fields = parseCSVLine(line);

                    if (fields.size() < 9) {
                        invalidRecords++;
                        continue;
                    }

                    String id = clean(fields.get(0));
                    String name = clean(fields.get(1));
                    String price = clean(fields.get(2));
                    String discontinued = clean(fields.get(3));
                    String manufacturer = clean(fields.get(4));
                    String type = clean(fields.get(5));
                    String packSize = clean(fields.get(6));
                    String composition1 = clean(fields.get(7));
                    String composition2 = clean(fields.get(8));

                    // Ignore discontinued medicines
                    if ("TRUE".equalsIgnoreCase(discontinued)) {
                        discontinuedRecords++;
                        continue;
                    }

                    // Medicine name is mandatory
                    if (name.isEmpty()) {
                        invalidRecords++;
                        continue;
                    }

                    // Remove duplicate medicine records
                    String uniqueKey =
                            name.toLowerCase() + "|" +
                            composition1.toLowerCase() + "|" +
                            composition2.toLowerCase();

                    if (!uniqueMedicines.add(uniqueKey)) {
                        duplicateRecords++;
                        continue;
                    }

                    // Normalize price
                    price = normalizePrice(price);

                    writer.write(
                            csv(id) + "," +
                            csv(name) + "," +
                            csv(price) + "," +
                            csv(manufacturer) + "," +
                            csv(type) + "," +
                            csv(packSize) + "," +
                            csv(composition1) + "," +
                            csv(composition2)
                    );

                    writer.newLine();

                    validRecords++;
                }

                report.write("==============================================");
                report.newLine();
                report.write("       PHARMASEARCH DATA CLEANING REPORT");
                report.newLine();
                report.write("==============================================");
                report.newLine();
                report.newLine();

                report.write("Input File: " + INPUT_FILE);
                report.newLine();

                report.write("Output File: " + OUTPUT_FILE);
                report.newLine();

                report.newLine();

                report.write("Total records processed: " + totalRecords);
                report.newLine();

                report.write("Valid active records: " + validRecords);
                report.newLine();

                report.write(
                        "Discontinued records removed: "
                        + discontinuedRecords);
                report.newLine();

                report.write(
                        "Duplicate records removed: "
                        + duplicateRecords);
                report.newLine();

                report.write(
                        "Invalid records removed: "
                        + invalidRecords);
                report.newLine();

                report.newLine();

                report.write("Cleaning operations:");
                report.newLine();
                report.write("1. Removed discontinued medicines");
                report.newLine();
                report.write("2. Removed records without medicine names");
                report.newLine();
                report.write("3. Removed duplicate medicine records");
                report.newLine();
                report.write("4. Trimmed unnecessary whitespace");
                report.newLine();
                report.write("5. Normalized medicine text fields");
                report.newLine();
                report.write("6. Normalized price values");
                report.newLine();
                report.write("7. Generated a clean CSV dataset");
                report.newLine();

                report.newLine();

                report.write("==============================================");
                report.newLine();
                report.write("Cleaning completed successfully.");
                report.newLine();
                report.write("==============================================");

            }

            System.out.println();
            System.out.println("==============================================");
            System.out.println("       PHARMASEARCH DATA CLEANER");
            System.out.println("==============================================");
            System.out.println();
            System.out.println("Total records       : " + totalRecords);
            System.out.println("Valid active records: " + validRecords);
            System.out.println("Discontinued removed: " + discontinuedRecords);
            System.out.println("Duplicates removed  : " + duplicateRecords);
            System.out.println("Invalid removed     : " + invalidRecords);
            System.out.println();
            System.out.println("Clean dataset:");
            System.out.println(OUTPUT_FILE);
            System.out.println();
            System.out.println("Cleaning report:");
            System.out.println(REPORT_FILE);
            System.out.println();
            System.out.println("==============================================");

        } catch (Exception e) {

            System.err.println();
            System.err.println("DATA CLEANING FAILED");
            System.err.println("----------------------------------------------");
            e.printStackTrace();
        }
    }

    private static String clean(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("\uFEFF", "")
                .replace("\u00A0", " ")
                .trim()
                .replaceAll("\\s+", " ");
    }

    private static String normalizePrice(String value) {

        if (value == null || value.isEmpty()) {
            return "";
        }

        try {

            double number = Double.parseDouble(value);

            return String.format(Locale.US, "%.2f", number);

        } catch (NumberFormatException e) {

            return "";
        }
    }

    private static String csv(String value) {

        if (value == null) {
            return "\"\"";
        }

        value = value.replace("\"", "\"\"");

        return "\"" + value + "\"";
    }

    private static List<String> parseCSVLine(String line) {

        List<String> fields = new ArrayList<>();

        StringBuilder current = new StringBuilder();

        boolean insideQuotes = false;

        for (int i = 0; i < line.length(); i++) {

            char c = line.charAt(i);

            if (c == '"') {

                if (insideQuotes &&
                    i + 1 < line.length() &&
                    line.charAt(i + 1) == '"') {

                    current.append('"');
                    i++;

                } else {

                    insideQuotes = !insideQuotes;
                }

            } else if (c == ',' && !insideQuotes) {

                fields.add(current.toString());
                current.setLength(0);

            } else {

                current.append(c);
            }
        }

        fields.add(current.toString());

        return fields;
    }
}