package pharmasearch;

import pharmasearch.model.Medicine;
import pharmasearch.repository.MedicineRepository;
import pharmasearch.service.SearchService;

import java.util.List;

public class SearchServiceTest {

    public static void main(String[] args) {

        System.out.println("==============================================");
        System.out.println("       PHARMASEARCH SEARCH ENGINE TEST");
        System.out.println("==============================================");

        long start = System.currentTimeMillis();

        MedicineRepository repository = new MedicineRepository();

        long loadTime = System.currentTimeMillis() - start;

        System.out.println();
        System.out.println("Medicines loaded : " + repository.size());
        System.out.println("Loading time     : " + loadTime + " ms");

        SearchService searchService = new SearchService(repository);

        testSearch(searchService, "paracetamol");
        testSearch(searchService, "augmentin");
        testSearch(searchService, "azithral");
        testSearch(searchService, "amoxicillin");
        testSearch(searchService, "paracetmol");

        System.out.println();
        System.out.println("==============================================");
        System.out.println("              SEARCH TEST COMPLETE");
        System.out.println("==============================================");
    }

    private static void testSearch(
            SearchService searchService,
            String query) {

        System.out.println();
        System.out.println("----------------------------------------------");
        System.out.println("Search query: " + query);
        System.out.println("----------------------------------------------");

        long start = System.currentTimeMillis();

        List<Medicine> results = searchService.search(query);

        long time = System.currentTimeMillis() - start;

        System.out.println("Results found : " + results.size());
        System.out.println("Search time   : " + time + " ms");

        int count = Math.min(10, results.size());

        for (int i = 0; i < count; i++) {

            Medicine medicine = results.get(i);

            System.out.println(
                    (i + 1) + ". " +
                    medicine.getName()
            );
        }
    }
}