package pharmasearch;

import pharmasearch.controller.MedicineController;
import pharmasearch.model.Medicine;

import java.util.List;

public class ControllerTest {

    public static void main(String[] args) {

        System.out.println("==============================================");
        System.out.println("       PHARMASEARCH CONTROLLER TEST");
        System.out.println("==============================================");

        long start = System.currentTimeMillis();

        MedicineController controller = new MedicineController();

        long loadTime = System.currentTimeMillis() - start;

        System.out.println();
        System.out.println("Medicines loaded : " + controller.getMedicineCount());
        System.out.println("Loading time     : " + loadTime + " ms");

        System.out.println();
        System.out.println("----------------------------------------------");
        System.out.println("Search query: paracetamol");
        System.out.println("----------------------------------------------");

        List<Medicine> results = controller.search("paracetamol");

        System.out.println("Results found : " + results.size());

        for (int i = 0; i < Math.min(10, results.size()); i++) {
            System.out.println((i + 1) + ". " + results.get(i).getName());
        }

        System.out.println();
        System.out.println("==============================================");
        System.out.println("        CONTROLLER TEST COMPLETE");
        System.out.println("==============================================");
    }
}