# CO2 – String Algorithms

## Objective

CO2 focuses on applying efficient string algorithms for large-scale
pattern-matching problems.

## Pharma Search Context

String matching is a core operation in Pharma Search because users
search medicine names and related medicine information using text
queries.

## Implementations

### KMP Algorithm

File:

`backend/src/main/java/pharmasearch/algorithm/KMPAlgorithm.java`

The implementation builds the Longest Prefix Suffix (LPS) array and
uses it to avoid unnecessary comparisons during pattern matching.

Time Complexity:

- Preprocessing: O(m)
- Searching: O(n)
- Overall: O(n + m)

### Rabin-Karp Algorithm

File:

`backend/src/main/java/pharmasearch/algorithm/RabinKarp.java`

The implementation uses a rolling hash to compare a search pattern with
text windows. Hash matches are verified using exact comparison to
prevent false matches caused by hash collisions.

### Z-Algorithm

File:

`backend/src/main/java/pharmasearch/algorithm/ZAlgorithm.java`

The implementation builds the Z-array and uses it for linear-time
pattern matching.

Time Complexity:

- O(n)

## Integration

The existing SearchService uses KMP during medicine searching.
Rabin-Karp provides an additional hash-based pattern detection
strategy.

The Z-Algorithm is included as an additional CO2 implementation.

## CO2 Evidence

```text
Medicine Query
     |
     +---- KMP
     |
     +---- Rabin-Karp
     |
     +---- Z-Algorithm
     |
     v
Pattern Matching