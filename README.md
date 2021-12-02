# EPFL CS550 - Formal Verification

[Moodle](https://moodle.epfl.ch/course/view.php?id=13051), [Coursebook](https://edu.epfl.ch/coursebook/en/formal-verification-CS-550?cb_cycle=bama_cyclemaster&cb_section=in)

This  repository is the homepage of the course Formal Verification (fall 2021) and hosts the material necesary for the labs.

 **Construct 100% correct software!** The course is all project based. 

### Staff:

- Professor: [Viktor Kunčak](https://people.epfl.ch/viktor.kuncak)
- Teaching Assistant: [Simon Guilloud](https://people.epfl.ch/simon.guilloud)

### Early Task

The projects will consist in 4 small labs and one larger project. Students should form groups of 2 to 3 by October 1st and send their group to [Simon Guilloud](https://people.epfl.ch/simon.guilloud)

### Grading

- 40% Four labs to be done in groups, each group working independently on same projects
- 60% Final project to be done in groups : you will choose a topic with our agreement
    - 15% Written presentation of a background paper 
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
|      |     |            | 17:15 | GRA330 | [Lab01 (Stainless)](https://gitlab.epfl.ch/lara/cs550/-/tree/main/labs/lab01) | [Tutorial 1](https://tube.switch.ch/videos/03edee61) (installation), [Tutorial 2](https://tube.switch.ch/videos/c22ea3e8), [Tutorial 3](https://tube.switch.ch/videos/7f57f7a9), [Tutorial 4](https://tube.switch.ch/videos/2a9fd35c), [FMCAD 2021 Tutorial](https://github.com/epfl-lara/fmcad2021tutorial/) |
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
|      | Thu | 21.10.2021  | 15:15 | GRA330 | Lectures 7,8                    | [Converting Imperative Programs to Formulas](https://tube.switch.ch/videos/79219264), [Hoare Logic, Strongest Postcondition, Weakest Precondition](https://tube.switch.ch/videos/3fc107a7), [Live Recording 1](https://tube.switch.ch/videos/FZx8YneDwJ), [Live Recording 2](https://tube.switch.ch/videos/3qWOMPmLz3), [Live Recording 3](https://tube.switch.ch/videos/jnBtMCUhu6/) |
|      |     |             | 17:15 | GRA330 | [Lab04](labs/lab04/)           |    |
|      |     |             |       | CE1103 | Lab04, watch videos          | [J Strother Moore: Machines Reasoning about Machines](http://slideshot.epfl.ch/play/suri_moore), [alternative video](https://www.newton.ac.uk/seminar/21742/) |
|      |     |             |       |        |                              | [Talk of Tony Hoare at EPFL in June 2011](http://slideshot.epfl.ch/play/suri_hoare), [Wikipedia Page of Tony Hoare](https://en.wikipedia.org/wiki/Tony_Hoare) |
|      | Thu | 28.10.2021  | 15:15 | GRA330 | Lecture 9                    | [Monotonicity and Semantics of Local Variables](https://tube.switch.ch/videos/pJFK2gi0YM), [Relational Semantics of Loops](https://tube.switch.ch/videos/jAePaQR8jc)
|      |     |             | 17:15 | GRA330 | [Lab04](labs/lab04/)         |       |
|      | Fri | 29.10.2021  | 13:15 | CE1103 | Lecture 10                   | [Loop Semantics Example](https://tube.switch.ch/videos/M2YCTkGZ4F), [Approximating Loops. Recursion 1](https://tube.switch.ch/videos/xCQoLRTGKq), [Recursion 2](https://tube.switch.ch/videos/NjerTXfE9z) |
|      |     |             |       |        |                              | [General Suggestions on Projects](https://gitlab.epfl.ch/kuncak/student-projects/) |
|      | Thu | 04.11.2021  | 15:15 | GRA330 | Lecture 11                    | [Rustan Leino: Directions to and for verified software](https://slideshot.epfl.ch/play/kYmvaDF6Z6jI), [Introduction to SMT Solvers](https://tube.switch.ch/videos/CDDwI5RZD0), [SMT by Example](https://sat-smt.codes/)
|      |     |             | 17:15 | GRA330 | Project and paper discussions  | [Work on background paper report](labs/Background%20Papers) |
|      | Fri | 05.11.2021  | 13:15 | CE1103 |                                | Discuss projects and background papers |
|      | Thu | 11.11.2021  | 15:15 | GRA330 | Lecture 12                     | [Abstract Interpretation Idea](https://tube.switch.ch/videos/cOvvbWjTpU), [Lattices for Abstract Interpretation](https://tube.switch.ch/videos/Zj3TNfknHG) |
|      |     |             |       |        |                                | Talk to watch offline: [Gentle Introduction to AI](https://www.youtube.com/watch?v=k6xH47tIS_I) |
|      |     |             | 17:15 | GRA330 | Project and paper discussions  | [Work on background paper report](labs/Background%20Papers)
|      | Fri | 12.11.2021  | 13:15 | CE1103 |                                | [Work on background paper report](labs/Background%20Papers) |
|      | Thu | 18.11.2021  | 15:15 | GRA330 | Lecture 13                     | [From Lattice Products to Tarski's Fixpoint Theorem](https://tube.switch.ch/videos/dQ5vvteGhz), [Omega Continuity, Galois Connection, and AI Recipe](https://tube.switch.ch/videos/DkObtnApKb) |
|      |     |             | 17:15 | GRA330 | Individual Projects            |   |
|      | Fri | 19.11.2021  | 13:15 | CE1103 | Presentation by Jérôme Boillot | [Abstract Interpretation in Stainless](https://tube.switch.ch/videos/LVjHvczUpy) | 
|      |     |             | 14:15 | CE1103 | Individual Projects            |  |
|      | Thu | 25.11.2021  | 15:15 | GRA330 | Lecture 14a  | [Semantics and Verification of Concurrency](https://tube.switch.ch/videos/BtlLSpFINW) |
|      |     |             | 17:15 | GRA330 | Individual Projects            |     |
|      | Fri | 26.11.2021  | 13:15 | CE1103 | Lecture 14b | [Total Functions: Why and How](https://tube.switch.ch/videos/n9OsbDPfwG) | 
|      | Thu | 02.12.2021  | 15:15 | GRA330 | Lecture 15a | [Sequent Calculus](https://tube.switch.ch/videos/OE4GR0K9nK) by Simon Guilloud |   |
|      |     |             | 16:00 | [Zoom](https://mit.zoom.us/j/96451987305)  | Thesis Defense of [Clément Pit-Claudel](http://pit-claudel.fr/clement/)  | [Abstract](https://www.csail.mit.edu/event/thesis-defense-relational-compilation-end-end-verification), [Background](https://people.csail.mit.edu/cpitcla/thesis/relational-compilation.html)  |
|      |     |             | 17:15 | GRA330 | Individual Projects            |   |

# Coming soon

| Week | Day | Date       | Time  | Room   | Topic                           | Videos & Slides              |
| :--  | :-- | :--        | :--   | :--    | :--                             | :--                          |
|      | Fri | 03.12.2021  | 13:15 | CE1103 | Individual Projects |     | 
|      | Thu | 09.12.2021  | 15:15 | GRA330 | Lecture 16: Reasoning About State. Monadic Semantics |   |
|      |     |             | 17:15 | GRA330 | Individual Projects            |   |
|      | Fri | 11.12.2021  | 13:15 | CE1103 | Individual Projects |     | 
|      | Thu | 16.12.2021  | 15:15 | GRA330 | Project Presentations    |     |
|      |     |             | 17:15 | GRA330 | Project Presentations    |     |
|      | Fri | 17.12.2021  | 13:15 | CE1103 | Project Presentations    |     | 
|      | Thu | 23.12.2021  | 15:15 | GRA330 | Project Presentations    |     |
|      |     |             | 17:15 | GRA330 | Project Presentations    |     |

# Deadlines

  * ~~29.10 Start reading [projects suggestions](https://gitlab.epfl.ch/kuncak/student-projects/) and discuss directions with colleagues~~
  * ~~04.11, 05.11 Discuss projects with teaching staff and choose a project as well as a paper to read for its background.~~
  * ~~06.11 Deadline to send us the title and PDF of the paper you plan to read and report on~~
  * ~~13.11 Submit a written short 5-page report about the chosen paper (template will be provided)~~
  * Last two weeks of semester: project presentations (15min) in class or pre-recorded with 5 min Q&A live or over zoom
  * End of semester or the latest on 10 January 2021: written report about your project with links to any repository code
