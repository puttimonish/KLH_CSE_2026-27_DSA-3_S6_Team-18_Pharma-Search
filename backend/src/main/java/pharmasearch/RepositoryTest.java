package pharmasearch;

import pharmasearch.repository.MedicineRepository;

public class RepositoryTest {

    public static void main(String[] args) {

        System.out.println("==============================================");
        System.out.println("       PHARMASEARCH DATA LOAD TEST");
        System.out.println("==============================================");

        long start = System.currentTimeMillis();

        MedicineRepository repository = new MedicineRepository();

        long end = System.currentTimeMillis();

        System.out.println();
        System.out.println("Total medicines loaded : " + repository.size());
        System.out.println("Loading time            : " + (end - start) + " ms");

        if (repository.size() == 242381) {
            System.out.println("STATUS                  : SUCCESS");
        } else {
            System.out.println("STATUS                  : CHECK DATA");
        }

        System.out.println("==============================================");
    }
}