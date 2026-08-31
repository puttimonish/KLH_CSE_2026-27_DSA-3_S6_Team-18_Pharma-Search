package pharmasearch;

import pharmasearch.algorithm.CosineSimilarity;

public class CosineSimilarityTest {

    public static void main(String[] args) {

        System.out.println("==============================================");
        System.out.println("       PHARMASEARCH COSINE SIMILARITY TEST");
        System.out.println("==============================================");

        test(
            "paracetamol tablet pain relief",
            "paracetamol tablet pain relief"
        );

        test(
            "paracetamol tablet",
            "paracetamol tablets"
        );

        test(
            "amoxicillin antibiotic",
            "amoxicillin antibiotic medicine"
        );

        test(
            "paracetamol",
            "aspirin"
        );

        System.out.println("----------------------------------------------");

        System.out.println(
            "Similar check: "
            + CosineSimilarity.isSimilar(
                "paracetamol tablet",
                "paracetamol tablets",
                0.5
            )
        );

        System.out.println("==============================================");
    }

    private static void test(String text1, String text2) {

        double score =
            CosineSimilarity.calculate(text1, text2);

        System.out.printf(
            "'%s' <-> '%s' : %.4f%n",
            text1,
            text2,
            score
        );
    }
}