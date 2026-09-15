# CO4 – Network Flow

## Objective

CO4 focuses on applying network-flow algorithms to capacity-constrained problems.

## Pharma Search Context

Medicine availability can be represented as an allocation problem in which medicine demand must be supplied through available pharmacy capacity.

## Implementation

File:

`backend/src/main/java/pharmasearch/co/MedicineAllocationMaxFlow.java`

The implementation uses the Edmonds-Karp algorithm, a BFS-based implementation of Ford-Fulkerson.

## Network Model

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