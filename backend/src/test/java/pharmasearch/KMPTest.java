package pharmasearch;

import pharmasearch.algorithm.KMPAlgorithm;

public class KMPTest {

    public static void main(String[] args) {

        System.out.println("==============================================");
        System.out.println("          PHARMASEARCH KMP TEST");
        System.out.println("==============================================");

        String medicine = "Augmentin 625 Duo Tablet";

        String query1 = "augmentin";
        String query2 = "625 duo";
        String query3 = "paracetamol";

        System.out.println();
        System.out.println("Medicine : " + medicine);

        System.out.println(
                "Search '" + query1 + "' : " +
                KMPAlgorithm.contains(medicine, query1)
        );

        System.out.println(
                "Search '" + query2 + "' : " +
                KMPAlgorithm.contains(medicine, query2)
        );

        System.out.println(
                "Search '" + query3 + "' : " +
                KMPAlgorithm.contains(medicine, query3)
        );

        System.out.println();

        int position = KMPAlgorithm.search(medicine, "625");

        System.out.println(
                "Position of '625' : " + position
        );

        System.out.println("==============================================");
    }
}