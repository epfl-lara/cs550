# EPFL CS550 - Formal Verification

[Moodle](https://moodle.epfl.ch/course/view.php?id=13051), [Coursebook](https://edu.epfl.ch/coursebook/en/formal-verification-CS-550?cb_cycle=bama_cyclemaster&cb_section=in)

This  repository is the homepage of the course Formal Verification (autumn 2023) and hosts the material necesary for the labs.

### Staff:

- Professor: [Viktor Kunčak](https://people.epfl.ch/viktor.kuncak)
- Teaching Assistant: [Simon Guilloud](https://people.epfl.ch/simon.guilloud)
- Student Assistants: [Andrea Gilot](https://people.epfl.ch/andrea.gilot), [Valentin Aebi](https://people.epfl.ch/valentin.aebi/), [Noé De Santo](https://people.epfl.ch/noe.desanto)

### Grading

The grade is based on the written mid-term, as well as code, documentation, and explanation of projects during the semester. Specific percentages will be communicated in the first class and posted here.

The types of graded materials will include:

- 40% Late mid-term written exam: 23 November 15:15-18:00 (see [this folder with past exams](past-exams/))
- 20% total: four-five labs, to be done in groups, each group working independently on same projects
- 40% final project to be done in groups, you will choose a topic with our agreement
    - 10% Written presentation of a background paper 
    - 10% Results accomplished (how hard it was, how far you got)
    - 10% Presentation of results
    - 10% Final report

# Content

In this course we introduce formal verification as a principled approach for developing systems that do what they should do.

The course has two aspects:
- learning the practice of formal verification - how to use tools to construct verified software
- understanding the principles behind formal verification and the ways in which verification tools work

The course will follow a similar structure to the [2022 edition](https://gitlab.epfl.ch/lara/cs550/-/tree/2022?ref_type=heads). Project can be a case study in developing a verified piece of software, an implementation of verification tool functionality, or a theoretical result about verification, constraint solving or theorem proving. Students present their projects with a written report as well as by a live presentation of project results, answering our questions.

Note that slides can be found **underneath each lecture video** on switch tube linkes below. 

## Books

* [CalComp] **The Calculus of Computation - Decision Procedures with Applications to Verification**, 2007, [from Springer](https://doi.org/10.1007/978-3-540-74113-8), [from EPFL library](https://www.epfl.ch/campus/library/beast/?isbn=9783540741138), by Aaron Bradley and Zohar Manna.
* [HandMC] **Handbook of Model Checking**, 2018, from [from Springer](https://link.springer.com/book/10.1007/978-3-319-10575-8), [from EPFL Library](https://library.epfl.ch/en/beast?isbn=9783319105758), edited by Edmund M. Clarke, Thomas A. Henzinger, Helmut Veith, Roderick Bloem.
* [HandAR] **Handbook of Practical Logic and Automated Reasoning**, 2009, [from Cambridge University Press](https://doi.org/10.1017/CBO9780511576430) and [from EPFL Library](https://library.epfl.ch/en/beast?isbn=9786612058776), by John Harrison

In the reading list below, HandAR-Ch.2 means Chapter 2 in the Handbook of Practical Logic and Automated Reasoning Above, whereas HandMC-Ch.9 means Chapter 9 of the Handbook of Model Checking, etc.

## COURSE OUTLINE 


| Week | Day | Date       | Time  | Room   | Topic                           | Videos & Slides              |
| :--  | :-- | :--        | :--   | :--    | :--                             | :--                          |
| 1    | Thu | 21.09.2023 | 15:15 | [GRA330](https://plan.epfl.ch/?room==GR%20A3%2030) | Lecture 1                       | [Intro to FV](https://tube.switch.ch/videos/56b40f7e), [Intro to Stainless](https://tube.switch.ch/videos/c7d203e8), [Auxiliary Assertions](https://tube.switch.ch/videos/44e8a0dc), [Unfolding](https://tube.switch.ch/videos/ada8a42c), [Disasters, Successes, and Inductive Invariants](https://tube.switch.ch/videos/cca7c3f8) |
|      |     |           |   |   | Follow:                       | [Stainless ASPLOS'22 Tutorial](https://epfl-lara.github.io/asplos2022tutorial/)  |
|      |     |           | 17:15 | GRA330 | Lecture 2                       | [Dispenser Example](https://tube.switch.ch/videos/ded227dd), [Finite Systems Expressed with Formulas](https://tube.switch.ch/videos/088d2823) |
|      |     |            |   |   | Reading:                       | HandMC-Ch.10  |
|      | Fri | 22.09.2023 | 13:15 | [INR219](https://plan.epfl.ch/?room==INR%20219) | Lecture 3                       | [What is a Formal Proof?](https://tube.switch.ch/videos/4a211e7a) and [Propositional Resolution](https://tube.switch.ch/videos/280bbc4c) |
|      |     |            |   |   | Reading:                       | CalComp-Ch.1 ∨ HandAR-Ch.2 |
| 2    | Thu | 28.09.2023 | 15:15 | GRA330 | [Exercise 1](exercises/Exercises1/ex1.pdf) | Propositional logic
|      |     |            | 17:15 | GRA330 | [Lab 1](labs/lab1/) | Sublists in Stainless
|      | Fri | 29.09.2023 | 13:15 | [INR219](https://plan.epfl.ch/?room==INR%20219) | Lecture 4                       | [Propositional Resolution](https://tube.switch.ch/videos/280bbc4c) up to and including equisatisfiability |
| 3    | Thu | 5.10.2023 | 15:15 | GRA330 | Lecture 5 | [Propositional Resolution](https://tube.switch.ch/videos/280bbc4c): Tseytin's transformation and SAT solving. [Automating First-Order Logic using Resolution](https://tube.switch.ch/videos/60fb9217) | 
|      |     |           | 17:15 | GRA330 | [Lab 2](labs/lab2) | A communication protocol in Stainless 
|      | Fri | 6.10.2023 | 13:15 | [INR219](https://plan.epfl.ch/?room==INR%20219) | [Exercise 2](https://gitlab.epfl.ch/lara/cs550/-/tree/main/exercises/Exercises2?ref_type=heads)         | Traces, SAT, models |
| 4    | Thu | 12.10.2023 | 15:15 | GRA330 | Lecture 6 | Continue [Automating First-Order Logic using Resolution](https://tube.switch.ch/videos/60fb9217). [Term Models for First-Order Logic](https://mediaspace.epfl.ch/edit/0_jnuljf9n)
|      |     |            | 17:15 | GRA330 | [Lab 3](labs/lab3/) | Resolution proof checker (with Stainless) |
|      | Fri | 13.10.2023| 13:15 | INR219 | [Exercise 3](https://gitlab.epfl.ch/lara/cs550/-/tree/main/exercises/Exercises3) | Propositional logic. Transition systems. Models |
| 5    | Thu | 19.10.2023 | 15:15 | GRA330 | Lecture 7 | [Converting Imperative Programs to Formulas](https://tube.switch.ch/videos/79219264), [Hoare Logic, Strongest Postcondition, Weakest Precondition](https://tube.switch.ch/videos/3fc107a7) |  
|      |     |            | 17:15 | GRA330 | [Lab 3](labs/lab3/) | Continue resolution proof checker |
|      | Fri | 20.10.2023 | 13:15 | INR219 | Lecture 8 | [Presburger Arithmetic 1](https://tube.switch.ch/videos/535e9dea), [Presburger Airhtmetic 2](https://tube.switch.ch/videos/ceecf2f6) |
