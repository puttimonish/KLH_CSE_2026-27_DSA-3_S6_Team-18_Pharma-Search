# CO5 – Approximation Algorithms

## Objective

CO5 focuses on NP-completeness, NP-hard optimization problems, and approximation algorithms.

## Pharma Search Context

A medicine-recommendation scenario can be represented as a budget-constrained selection problem.

The objective is to select useful medicine options while respecting a fixed budget.

## Implementation

File:

`backend/src/main/java/pharmasearch/co/BudgetRecommendationApproximation.java`

Each candidate contains:

- Medicine name
- Price
- Usefulness score

Candidates are ranked using:

```text
Usefulness / Cost