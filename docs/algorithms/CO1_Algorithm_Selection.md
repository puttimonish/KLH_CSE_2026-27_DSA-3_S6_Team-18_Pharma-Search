# CO1 – Algorithm Selection

## Objective

CO1 focuses on evaluating the characteristics of a problem and selecting
an appropriate algorithmic strategy.

## Pharma Search Context

Pharma Search handles medicine queries that may involve:

- Exact pattern matching
- Large text searching
- Spelling variations
- Text similarity
- Combined search requirements

Different problem characteristics can require different algorithmic
approaches.

## Implementation

File:

`backend/src/main/java/pharmasearch/co/AlgorithmSelector.java`

The `AlgorithmSelector` class provides a strategy-selection component
for these search requirements.

### Available Strategies

| Strategy | Suitable Problem |
|---|---|
| Exact Pattern Matching | Exact text/pattern searching |
| Hash-Based Pattern Matching | Large text pattern detection |
| Fuzzy Matching | Spelling variations |
| Similarity Matching | Comparing related text |
| Combined Search | Multiple search requirements |

## Algorithm Selection

The selector evaluates the characteristics of the search requirement
and returns an appropriate strategy.

This demonstrates the CO1 principle of selecting an algorithm based on
the problem characteristics rather than applying the same algorithm to
every problem.

## Connection to the Main Search System

The actual Pharma Search system combines multiple techniques:

```text
User Query
    ↓
Pattern Matching
    ↓
Fuzzy Matching
    ↓
Similarity Analysis
    ↓
Result Ranking