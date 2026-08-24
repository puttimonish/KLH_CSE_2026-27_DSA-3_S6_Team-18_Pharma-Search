# Pharma Search

## Data Structures and Algorithms - 3

**Course Code:** 25CS2103E  
**Project:** Pharma Search  
**Team:** 18  
**Branch:** monish

---

## 1. Project Overview

Pharma Search is an intelligent medicine search system designed to search and retrieve relevant medicine information from a structured medicine corpus.

The system demonstrates multiple string-processing and information-retrieval techniques required for the project:

- KMP Pattern Matching
- Fuzzy Search using Edit Distance
- Similarity Search using Cosine Similarity

The project currently represents the partial implementation prepared for Review-2.

---

## 2. Problem Statement

Pharmacies and hospitals maintain information about medicines, manufacturers, dosage, categories, uses, substitutes, and related keywords.

Searching this information using only exact matching may fail when:

- The user makes a spelling mistake.
- The search query contains only a keyword.
- The user describes symptoms rather than providing an exact medicine name.

Pharma Search addresses these situations using multiple search algorithms.

---

## 3. Objectives

The main objectives of the project are:

1. Create a structured medicine corpus.
2. Implement efficient pattern matching using KMP.
3. Implement fuzzy medicine-name searching using Edit Distance.
4. Implement similarity-based searching using Cosine Similarity.
5. Provide an integrated command-line search interface.
6. Store and demonstrate test results for the implemented algorithms.

---

## 4. Project Structure

```text
PharmaSearch/
│
├── .gitignore
│
├── data/
│   └── medicines.txt
│
├── docs/
│   └── .gitkeep
│
├── reports/
│   └── .gitkeep
│
├── results/
│   ├── .gitkeep
│   ├── kmp_test.txt
│   ├── fuzzy_test.txt
│   └── similarity_test.txt
│
└── src/
    ├── PharmaSearch.java
    ├── FuzzySearch.java
    └── SimilaritySearch.java