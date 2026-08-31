package pharmasearch.repository;

import pharmasearch.model.Medicine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class MedicineRepository {

    private final List<Medicine> medicines = new ArrayList<>();

    private static final String DATA_FILE =
            "data/processed/medicines_clean.csv";

    public MedicineRepository() {
        loadMedicines();
    }

    private void loadMedicines() {

        try (BufferedReader br = new BufferedReader(new FileReader(DATA_FILE))) {

            String line;

            // Skip CSV header
            br.readLine();

            while ((line = br.readLine()) != null) {

                String[] fields = parseCSVLine(line);

                if (fields.length < 8) {
                    continue;
                }

                try {
                    int id = Integer.parseInt(fields[0]);
                    String name = fields[1];
                    double price = Double.parseDouble(fields[2]);
                    String manufacturer = fields[3];
                    String type = fields[4];
                    String packSize = fields[5];
                    String composition1 = fields[6];
                    String composition2 = fields[7];

                    Medicine medicine = new Medicine(
                            id,
                            name,
                            price,
                            manufacturer,
                            type,
                            packSize,
                            composition1,
                            composition2
                    );

                    medicines.add(medicine);

                } catch (NumberFormatException e) {
                    // Skip invalid numeric records
                }
            }

            System.out.println("Medicines loaded: " + medicines.size());

        } catch (Exception e) {
            System.err.println("Error loading medicine dataset:");
            System.err.println(e.getMessage());
        }
    }

    private String[] parseCSVLine(String line) {

        List<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean insideQuotes = false;

        for (int i = 0; i < line.length(); i++) {

            char c = line.charAt(i);

            if (c == '"') {
                insideQuotes = !insideQuotes;
            } 
            else if (c == ',' && !insideQuotes) {
                fields.add(current.toString().trim());
                current.setLength(0);
            } 
            else {
                current.append(c);
            }
        }

        fields.add(current.toString().trim());

        return fields.toArray(new String[0]);
    }

    public List<Medicine> getAllMedicines() {
        return medicines;
    }

    public int size() {
        return medicines.size();
    }
}