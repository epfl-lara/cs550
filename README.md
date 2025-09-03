# EPFL CS550 - Formal Verification

[Moodle](https://moodle.epfl.ch/course/view.php?id=13051), [Coursebook](https://edu.epfl.ch/coursebook/en/formal-verification-CS-550?cb_cycle=bama_cyclemaster&cb_section=in)

This repository is the homepage of the course Formal Verification and hosts the material necessary for the labs.

### Staff

- Professor: [Viktor Kunčak](https://people.epfl.ch/viktor.kuncak)
- Teaching Assistant: [Sankalp Gambhir](https://people.epfl.ch/sankalp.gambhir)
- Student Assistant: [Jacopo Philipp Moretti](https://people.epfl.ch/jacopo.moretti)

### Grading

The grade is based on the labs, written midterm exam, as well as code, documentation, and explanation of projects during the semester. Specific percentages will be communicated in the first class and posted here.

The types of graded materials will include:

- **40%** written midterm exam (see [this folder with past exams](past-exams/))
- **20%** total: five labs, to be done in groups, each group working independently on same projects
- **40%** final project to be done in groups, you will choose a topic with our agreement
    - 10% Written presentation of a background paper 
    - 10% Results accomplished (how hard it was, how far you got)
    - 10% Presentation of results
    - 10% Final report

### Midterm Exam

The midterm exam will tentatively be held on Thursday 27/11/2025, during the 15h-19h slot for the course. The exact time and rooms will be communicated later.

### Course Project

The course project can be a case study in developing a verified piece of software, an implementation of verification tool functionality, or a theoretical result about verification, constraint solving or theorem proving. The projects must be presented with a written report as well as a live presentation of project results, answering our questions.

# Content

In this course, we introduce formal verification as a principled approach for developing systems that do what they are expected to.

The course has two aspects:
- learning the practice of formal verification - how to use tools to construct verified software
- understanding the principles behind formal verification and the ways in which verification tools work

The course will follow a similar structure to the [2024 edition](https://gitlab.epfl.ch/lara/cs550/-/tree/2024?ref_type=tags). 

Note that slides can be found **underneath each lecture video as attachments** on the Mediaspace links below. 

### Books

* [CalComp] **The Calculus of Computation - Decision Procedures with Applications to Verification**, 2007, [from Springer](https://doi.org/10.1007/978-3-540-74113-8), [from EPFL library](https://www.epfl.ch/campus/library/beast/?isbn=9783540741138), by Aaron Bradley and Zohar Manna.
* [HandMC] **Handbook of Model Checking**, 2018, from [from Springer](https://link.springer.com/book/10.1007/978-3-319-10575-8), [from EPFL Library](https://library.epfl.ch/en/beast?isbn=9783319105758), edited by Edmund M. Clarke, Thomas A. Henzinger, Helmut Veith, Roderick Bloem.
* [HandAR] **Handbook of Practical Logic and Automated Reasoning**, 2009, [from Cambridge University Press](https://doi.org/10.1017/CBO9780511576430) and [from EPFL Library](https://library.epfl.ch/en/beast?isbn=9786612058776), by John Harrison

In the reading list below, HandAR-Ch.2 means Chapter 2 in the Handbook of Practical Logic and Automated Reasoning Above, whereas HandMC-Ch.9 means Chapter 9 of the Handbook of Model Checking, etc.

### NOTE

To see the material, please visit https://mediaspace.epfl.ch , log in with your EPFL credentials and 
[select this channel](https://mediaspace.epfl.ch/channel/CS-550+Formal+Verification/30542). Slides and listings are attached underneath the videos.

### COURSE OUTLINE 


| Week | Day | Date       | Time  | Room   | Topic                           | Videos & Slides              |
| :--  | :-- | :--        | :--   | :--    | :--                             | :--                          |
| 1    | Thu | 11.09.2025 | 15:15 | [GRA330](https://plan.epfl.ch/?room==GR%20A3%2030) | [Lecture 1](https://mediaspace.epfl.ch/playlist/dedicated/30542/0_vw42tr2d/0_3z52dv8y) | [Intro to FV](https://mediaspace.epfl.ch/media/01-01%2C+What+is+Formal+VerificationF/0_3z52dv8y/30542), [Intro to Stainless](https://mediaspace.epfl.ch/media/01-02%2C+First+Steps+with+Stainless/0_tghlsgep/30542), [Auxiliary Assertions](https://mediaspace.epfl.ch/media/01-03%2C+Auxiliary+Assertions+in+Stainless/0_54yx91xi/30542), [Unfolding](https://mediaspace.epfl.ch/media/01-04%2C+Unfolding+recursive+functions+in+Stainless/0_4byxmv9i/30542), [Disasters, Successes, and Inductive Invariants](https://mediaspace.epfl.ch/media/01-05%2C+Disasters%2C+Successes%2C+and+Inductive+Invariants/0_fei98b8f) |
|      |     |           | 17:15 | GRA330 | [Lecture 2](https://mediaspace.epfl.ch/playlist/dedicated/30542/0_b3ga55fo/0_omextd9i)                       | [Dispenser Example](https://mediaspace.epfl.ch/media/02-01%2C+Dispenser+Example+of+Finite+System/0_omextd9i), [Finite Systems Expressed with Formulas](https://mediaspace.epfl.ch/media/02-02%2C+Finite+Systems+Expressed+with+Formulas/0_8a6q0uve) |
|      |     |            |   |   | Reading:                       | HandMC-Ch.10  |
|      |     |            |   |   | Follow:                        | [Stainless Tutorial Videos](https://mediaspace.epfl.ch/playlist/dedicated/30542/0_t2ld6vzn/0_azxgetu9) and [materials](https://epfl-lara.github.io/asplos2022tutorial/)  |
|      | Fri | 12.09.2025 | 13:15 | [INR219](https://plan.epfl.ch/?room==INR%20219) | [Lecture 3](https://mediaspace.epfl.ch/playlist/dedicated/30542/0_thr9uebs/0_tv48ew7w)                       | [What is a Formal Proof?](https://mediaspace.epfl.ch/playlist/dedicated/30542/0_thr9uebs/0_tv48ew7w) and [Propositional Resolution](https://mediaspace.epfl.ch/playlist/dedicated/30542/0_thr9uebs/0_lovmc46b) |
| 2    | Thu | 18.09.2025 | 15:15 | GRA330 | [Lecture 4](https://mediaspace.epfl.ch/playlist/dedicated/30542/0_1c4580tg/0_lovmc46b) [PDF](lectures/prop-resolution.pdf) | continue [Propositional Resolution](https://mediaspace.epfl.ch/playlist/dedicated/30542/0_thr9uebs/0_lovmc46b) |
|      |     |            | 17:15 | GRA330 | [Lab 1](labs/lab1/README.md) | |
|      | Fri | 19.09.2025 | 13:15 | INR219 | [Exercises 1](exercises/Exercises1) | Propositional Logic |
|      |     |            |   |   | Reading:                       | CalComp-Ch.1 ∨ HandAR-Ch.2 |

