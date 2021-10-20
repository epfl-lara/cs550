# EPFL CS550 - Formal Verification

[Moodle](https://moodle.epfl.ch/course/view.php?id=13051), [Coursebook](https://edu.epfl.ch/coursebook/en/formal-verification-CS-550?cb_cycle=bama_cyclemaster&cb_section=in)

This  repository will be the homepage of the course Formal Verification (fall 2021) and host all the material necesary for the labs.

 **Construct 100% correct software!** The course is all project based. 

### Staff:

- Professor: [Viktor Kunčak](https://people.epfl.ch/viktor.kuncak)
- Teaching Assistant: [Simon Guilloud](https://people.epfl.ch/simon.guilloud)

### Early Task

The projects will consist in 4 small labs and one larger project. Students should form groups of 2 to 3 by October 1st and send their group to [Simon Guilloud](https://people.epfl.ch/simon.guilloud)

### Grading

- 40% Four labs to be done in groups, each group working independently on same projects
- 60% Final project to be done in groups : you will choose a topic with our agreement
    - 15% Presentation of a background paper 
    - 15% Results accomplished (how hard it was, how far you got)
    - 15% Presentation of results 
    - 15% Final report

# Content

In this course we introduce formal verification as a principled approach for developing systems that do what they should do.

The course has two aspects:
- learning the practice of formal verification - how to use tools to construct verified software
- understanding the principles behind formal verification and the ways in which verification tools work

The course will follow a similar structure to the [2020 edition](https://lara.epfl.ch/w/fv20/top). The grading will be based only on project work. Project can be a case study in developing a verified piece of software, an implementation of verification tool functionality, or a theoretical result about verification, constraint solving or theorem proving. Students present their projects with a written report as well as by a live presentation of the background material and project results, answering our questions.

Note that slides can be found **underneath each lecture video** on switch tube linkes below. 


| Week | Day | Date       | Time  | Room   | Topic                           | Videos & Slides              |
| :--  | :-- | :--        | :--   | :--    | :--                             | :--                          |
| 1    | Thu | 23.09.2021 | 15:15 | GRA330 | Lecture 1                       | [Intro to FV](https://tube.switch.ch/videos/56b40f7e) ([Live1](https://tube.switch.ch/videos/oTm0zFBy0d)), [Intro to Stainless](https://tube.switch.ch/videos/c7d203e8) ([Live2](https://tube.switch.ch/videos/gFl27xlOFU)), [Auxiliary Assertions](https://tube.switch.ch/videos/44e8a0dc), [Unfolding](https://tube.switch.ch/videos/ada8a42c), [Disasters, Successes, and Inductive Invariants](https://tube.switch.ch/videos/cca7c3f8) ([Live3](https://tube.switch.ch/videos/YBYi1cdSUs)) |
|      |     |            | 17:15 | GRA330 | [Lab01 (Stainless)](https://gitlab.epfl.ch/lara/cs550/-/tree/main/labs/lab01) | [Tutorial 1](https://tube.switch.ch/videos/03edee61) (installation), [Tutorial 2](https://tube.switch.ch/videos/c22ea3e8), [Tutorial 3](https://tube.switch.ch/videos/7f57f7a9), [Tutorial 4](https://tube.switch.ch/videos/2a9fd35c) |
|      | Fri | 24.09.2021 | 13:15 | CE1103 | Labs                  |                               |
| 2    | Thu | 30.09.2021 | 15:15 | GRA330 | Lecture 2                       | [Dispenser Example](https://tube.switch.ch/videos/ded227dd) ([Live1](https://tube.switch.ch/videos/oK3WSD3M5Y)), [Finite Systems Expressed with Formulas](https://tube.switch.ch/videos/088d2823) ([Live2](https://tube.switch.ch/videos/tA79fK2yKh)) |
|      |     |            | 17:15 | GRA330 | [Lab02 (More Stainless)](https://gitlab.epfl.ch/lara/cs550/-/tree/main/labs/lab02) |
|      | Fri | 01.10.2021 | 13:15 | CE1103 | Lecture 3                       | [What is a Formal Proof?](https://tube.switch.ch/videos/4a211e7a) and [Propositional Resolution](https://tube.switch.ch/videos/280bbc4c) - without resolution: ([Live1](https://tube.switch.ch/videos/hFl8GLRF7L), [Live2](https://tube.switch.ch/videos/L5v3jWtYQd)) |
| 3    | Thu | 07.10.2021 | 15:15 | GRA330 | Lecture 4                       | [Propositional Resolution](https://tube.switch.ch/videos/280bbc4c) ([Live1](https://tube.switch.ch/videos/X2kSoTE4Y4), [Live2](https://tube.switch.ch/videos/y1b1zdEleN)) |
|      |     |            | 17:15 | GRA330 | [Lab03 (Propositional Logic (with Stainless))](https://gitlab.epfl.ch/lara/cs550/-/tree/main/labs/lab03) |   |
|      | Fri | 08.10.2021 | 13:15 | CE1103 | Lecture 5                       | [Presburger Arithmetic 1](https://tube.switch.ch/videos/535e9dea), [Presburger Arithmetic 2](https://tube.switch.ch/videos/ceecf2f6) ([Live1 until technical glitch](https://tube.switch.ch/videos/xk6qhRv3hV), [Live2](https://tube.switch.ch/videos/LMdUDApcOU)) | 
| 4    | Thu | 14.10.2021 | 15:15 | GRA330 | Lecture 6                       | [Automating First-Order Logic Proofs Using Resolution](https://tube.switch.ch/videos/60fb9217), (Alternative live recordings with overamplified audio: [Live 1](https://tube.switch.ch/videos/xiA6VEfZBW), [Live 2](https://tube.switch.ch/videos/HtT3u8yU4i)) |
|      |     |            |       |        |                                 | [Provers Proved New Math Results](https://www.mcs.anl.gov/research/projects/AR/new_results/) ([also in NYT](https://archive.nytimes.com/www.nytimes.com/library/cyber/week/1210math.html)), [SPASS Prover on The Web](https://webspass.spass-prover.org/) |
|      |     |             | 17:15 | GRA330 | Continue Lab03 |      
|      | Fri | 15.10.2021  | 13:15 | CE1103 | Continue Lab03 |
|      |     |             | 14:15 | BC 410 | Talk on Flix                 | Attend the talk by Magnus Madsen if interested |
|      | Thu | 21.10.2021  | 15:15 | GRA330 | Lecture 7                    | [Converting Imperative Programs to Formulas](https://tube.switch.ch/videos/79219264), [Hoare Logic, Strongest Postcondition, Weakest Precondition](https://tube.switch.ch/videos/3fc107a7) |
|      |     |             | 17:15 | GRA330 | Lab04                        |    |
|      |     |             |       | CE1103 | Lab04, watch video           | [J Strother Moore: Machines Reasoning about Machines](http://slideshot.epfl.ch/play/suri_moore), [alternative video](https://www.newton.ac.uk/seminar/21742/) |
