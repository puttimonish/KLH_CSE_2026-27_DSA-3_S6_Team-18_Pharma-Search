# CO3 – Dynamic Programming

## Objective

CO3 focuses on applying dynamic-programming techniques to solve
optimization and combinatorial problems.

## Pharma Search Context

Pharma Search uses edit distance to identify medicine names that are
similar to a user's query even when spelling differences exist.

## Implementations

### Standard Edit Distance

File:

`backend/src/main/java/pharmasearch/algorithm/EditDistance.java`

The implementation uses a two-dimensional dynamic-programming table.

The state represents the minimum number of insertions, deletions, and
replacements required to transform one string into another.

Time Complexity:

- O(m × n)

Space Complexity:

- O(m × n)

### Space-Optimized Edit Distance

File:

`backend/src/main/java/pharmasearch/algorithm/EditDistanceOptimized.java`

The optimized implementation retains only the previous and current
dynamic-programming rows.

Time Complexity:

- O(m × n)

Space Complexity:

- O(min(m, n))

## Application

Edit distance supports approximate medicine-name matching.

## CO3 Evidence

The project demonstrates:

- DP state formulation
- Base-case initialization
- State transitions
- Minimum-cost optimization
- Space optimization