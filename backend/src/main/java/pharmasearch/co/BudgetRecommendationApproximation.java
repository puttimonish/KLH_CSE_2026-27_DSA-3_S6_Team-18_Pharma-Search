package pharmasearch.co;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * CO5 - Approximation Algorithm
 *
 * Solves a budget-constrained medicine selection problem
 * using a greedy approximation strategy.
 *
 * Each medicine has:
 * - a price
 * - a usefulness score
 *
 * The algorithm selects medicines by usefulness-per-cost
 * while respecting the available budget.
 *
 * This is an approximation strategy for a knapsack-style
 * optimization problem. It is not used to claim that the
 * normal Pharma Search operation is NP-hard.
 */
public final class BudgetRecommendationApproximation {

    /**
     * Represents one medicine candidate.
     */
    public record MedicineOption(
            String name,
            double price,
            double usefulness) {

        public double usefulnessPerCost() {

            if (price <= 0) {
                return 0.0;
            }

            return usefulness / price;
        }
    }

    private BudgetRecommendationApproximation() {
        // Utility class
    }

    /**
     * Selects medicines within the supplied budget.
     *
     * Greedy approximation:
     * candidates are ordered by usefulness per unit cost.
     *
     * @param medicines available medicine candidates
     * @param budget maximum spending limit
     * @return selected medicines
     */
    public static List<MedicineOption> selectWithinBudget(
            List<MedicineOption> medicines,
            double budget) {

        List<MedicineOption> selected =
                new ArrayList<>();

        if (medicines == null || budget <= 0) {
            return selected;
        }

        List<MedicineOption> candidates =
                new ArrayList<>();

        for (MedicineOption medicine : medicines) {

            if (medicine != null
                    && medicine.price() > 0
                    && medicine.usefulness() >= 0
                    && medicine.price() <= budget) {

                candidates.add(medicine);
            }
        }

        candidates.sort(
                Comparator.comparingDouble(
                        MedicineOption::usefulnessPerCost
                ).reversed()
        );

        double remainingBudget = budget;

        for (MedicineOption medicine : candidates) {

            if (medicine.price() <= remainingBudget) {

                selected.add(medicine);

                remainingBudget -=
                        medicine.price();
            }
        }

        return selected;
    }

    /**
     * Calculates the total price of a recommendation.
     */
    public static double totalCost(
            List<MedicineOption> medicines) {

        if (medicines == null) {
            return 0.0;
        }

        double total = 0.0;

        for (MedicineOption medicine : medicines) {

            if (medicine != null) {
                total += medicine.price();
            }
        }

        return total;
    }

    /**
     * Calculates the total usefulness of a recommendation.
     */
    public static double totalUsefulness(
            List<MedicineOption> medicines) {

        if (medicines == null) {
            return 0.0;
        }

        double total = 0.0;

        for (MedicineOption medicine : medicines) {

            if (medicine != null) {
                total += medicine.usefulness();
            }
        }

        return total;
    }
}