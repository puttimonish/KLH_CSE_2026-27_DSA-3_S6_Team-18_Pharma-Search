# DSA-3 Course Outcome Mapping

## Pharma Search – An Intelligent Medicine Search and Recommendation System

This document maps the implemented algorithms and project components of
Pharma Search to the Course Outcomes (COs) of Design and Analysis of
Algorithms-3 (DSA-3).

---

## CO1 – Algorithmic Strategy Selection

**Course Outcome:**

Evaluate problem-class signatures and select an appropriate advanced
algorithmic strategy.

### Project Implementation

**File:**
`backend/src/main/java/pharmasearch/co/AlgorithmSelector.java`

The project includes an algorithm-selection component that evaluates
the characteristics of a search problem and selects an appropriate
strategy.

The available strategies include:

- Exact pattern matching
- Hash-based pattern matching
- Fuzzy matching
- Similarity matching
- Combined search

### Relevance to Pharma Search

Medicine search can involve different requirements such as exact
matching, large-text searching, spelling variations, and similarity
matching. The selection component demonstrates how the characteristics
of the problem influence the choice of algorithmic strategy.

---

## CO2 – String Algorithms

**Course Outcome:**

Apply linear-time string algorithms such as KMP, Z-function, and
Rabin-Karp with rolling hash for large-scale pattern matching.

### Project Implementations

**KMP:**
`backend/src/main/java/pharmasearch/algorithm/KMPAlgorithm.java`

**Rabin-Karp:**
`backend/src/main/java/pharmasearch/algorithm/RabinKarp.java`

**Z-Algorithm:**
`backend/src/main/java/pharmasearch/algorithm/ZAlgorithm.java`

### Relevance to Pharma Search

String matching is a core operation in medicine search.

KMP is used by the search service for efficient pattern matching.
Rabin-Karp provides rolling-hash based pattern detection.
The Z-Algorithm provides an additional linear-time pattern matching
implementation for algorithmic evaluation.

These algorithms demonstrate different approaches to searching for
medicine names and related text patterns.

---

## CO3 – Dynamic Programming

**Course Outcome:**

Apply advanced dynamic-programming patterns to solve
combinatorial and optimization problems.

### Project Implementations

**Standard Edit Distance:**
`backend/src/main/java/pharmasearch/algorithm/EditDistance.java`

**Space-Optimized Edit Distance:**
`backend/src/main/java/pharmasearch/algorithm/EditDistanceOptimized.java`

### Relevance to Pharma Search

The Pharma Search system uses Levenshtein Edit Distance to measure
spelling differences between the user's query and medicine names.

The standard implementation uses a two-dimensional dynamic-programming
table.

The optimized implementation reduces memory usage by retaining only
the required previous and current DP rows.

### Complexity

Standard implementation:

- Time: O(m × n)
- Space: O(m × n)

Space-optimized implementation:

- Time: O(m × n)
- Space: O(min(m, n))

This demonstrates both the basic dynamic-programming formulation and
space optimization.

---

## CO4 – Network Flow

**Course Outcome:**

Apply network-flow algorithms and max-flow/min-cut concepts to
capacity-constrained problems.

### Project Implementation

**File:**
`backend/src/main/java/pharmasearch/co/MedicineAllocationMaxFlow.java`

The implementation uses the Edmonds-Karp approach to calculate maximum
flow in a capacity-constrained network.

### Pharma Search Application

The model represents a medicine allocation scenario:

```text
Source
  |
  v
Medicine Demand
  |
  v
Pharmacy Capacity
  |
  v
Sink