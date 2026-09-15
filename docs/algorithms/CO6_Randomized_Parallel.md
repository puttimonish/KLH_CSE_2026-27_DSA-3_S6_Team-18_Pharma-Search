
### CO6 — `CO6_Randomized_Parallel.md`

```markdown
# CO6 – Randomized and Parallel Algorithms

## Objective

CO6 focuses on randomized algorithms and parallel algorithmic primitives.

## Randomized Algorithm

### Randomized Medicine Sampling

File:

`backend/src/main/java/pharmasearch/co/RandomizedSearchSampler.java`

The implementation randomly selects a subset of medicine records from a larger collection.

A supplied random seed can be used to reproduce an experiment.

## Parallel Algorithm

### Parallel Prefix Sum

File:

`backend/src/main/java/pharmasearch/co/ParallelPrefixSum.java`

The implementation uses Java's Fork/Join framework and a divide-and-conquer strategy.

Example:

```text
Input:
[1, 2, 3, 4]

Output:
[1, 3, 6, 10]