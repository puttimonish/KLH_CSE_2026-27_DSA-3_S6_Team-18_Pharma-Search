# 💊 PharmaSearch

### Intelligent Medicine Search & Recommendation System using Data Structures and Algorithms

> DSA-3 Project | KL University Hyderabad | CSE
>
> Team 18 | Academic Year 2026–27

---

## 📌 Overview

PharmaSearch is a medicine search and recommendation system designed to provide fast and efficient access to medicine information.

The project applies multiple Data Structures and Algorithms (DSA) concepts to solve practical problems involved in medicine searching, matching, recommendation, allocation, and data processing.

The system combines a Java Spring Boot backend with a lightweight HTML, CSS and JavaScript frontend and uses a structured medicine dataset for processing and searching.

The project demonstrates how different algorithmic techniques can be selected and applied according to the computational requirements of different problems.

---

## 🎯 Objectives

The major objectives of PharmaSearch are:

- 🔎 Provide efficient medicine searching.
- ⚡ Implement multiple string-matching algorithms.
- 🧹 Clean and preprocess medicine data.
- 🧠 Apply Dynamic Programming for similarity and optimization problems.
- 🌐 Apply Network Flow concepts for medicine allocation.
- 💰 Provide budget-based medicine recommendations using approximation techniques.
- 🎲 Demonstrate randomized algorithms for search sampling.
- ⚙️ Apply parallel processing for large-scale data operations.
- 📊 Compare and organize different algorithms according to their use cases.
- 🖥️ Provide a simple web interface for interacting with the system.

---

# 🏗️ System Architecture

```text
                         ┌─────────────────────┐
                         │      User           │
                         └──────────┬──────────┘
                                    │
                                    ▼
                         ┌─────────────────────┐
                         │     Frontend        │
                         │ HTML / CSS / JS     │
                         └──────────┬──────────┘
                                    │
                                    ▼
                         ┌─────────────────────┐
                         │   Spring Boot       │
                         │     Backend         │
                         └──────────┬──────────┘
                                    │
                  ┌─────────────────┼─────────────────┐
                  │                 │                 │
                  ▼                 ▼                 ▼
            ┌──────────┐      ┌──────────┐      ┌──────────┐
            │ Search   │      │ Medicine │      │ Data     │
            │ Service  │      │Repository│      │ Cleaner  │
            └────┬─────┘      └────┬─────┘      └────┬─────┘
                 │                 │                 │
                 └────────────┬────┴─────────────────┘
                              ▼
                    ┌─────────────────────┐
                    │ DSA Algorithms      │
                    │                     │
                    │ String Matching     │
                    │ Dynamic Programming │
                    │ Network Flow        │
                    │ Approximation       │
                    │ Randomized          │
                    │ Parallel Processing │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │ Medicine Dataset    │
                    └─────────────────────┘
