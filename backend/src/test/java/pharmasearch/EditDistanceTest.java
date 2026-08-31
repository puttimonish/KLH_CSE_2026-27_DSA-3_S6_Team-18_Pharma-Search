package pharmasearch;

import pharmasearch.algorithm.EditDistance;

public class EditDistanceTest {

    public static void main(String[] args) {

        System.out.println("==============================================");
        System.out.println("       PHARMASEARCH EDIT DISTANCE TEST");
        System.out.println("==============================================");

        test("paracetamol", "paracetamol");
        test("paracetmol", "paracetamol");
        test("amoxycillin", "amoxicillin");
        test("augmentin", "augmentin");
        test("aspirin", "paracetamol");

        System.out.println("----------------------------------------------");

        System.out.println(
            "Similar check ('paracetmol', 'paracetamol'): "
            + EditDistance.isSimilar("paracetmol", "paracetamol", 2)
        );

        System.out.println(
            "Similar check ('aspirin', 'paracetamol'): "
            + EditDistance.isSimilar("aspirin", "paracetamol", 2)
        );

        System.out.println("==============================================");
    }

    private static void test(String a, String b) {

        int distance = EditDistance.calculate(a, b);

        System.out.println(
            "'" + a + "' -> '" + b + "' : " + distance
        );
    }
}