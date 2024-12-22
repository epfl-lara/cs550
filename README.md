# EPFL CS550 - Formal Verification

[Moodle](https://moodle.epfl.ch/course/view.php?id=13051), [Coursebook](https://edu.epfl.ch/coursebook/en/formal-verification-CS-550?cb_cycle=bama_cyclemaster&cb_section=in)

This  repository is the homepage of the course Formal Verification and hosts the material necesary for the labs.

### Staff:

- Professor: [Viktor Kunčak](https://people.epfl.ch/viktor.kuncak)
- Teaching Assistant: [Simon Guilloud](https://people.epfl.ch/simon.guilloud)
- Student Assistant: [Dario Halilovic](https://people.epfl.ch/dario.halilovic)

### Grading

The grade is based on the written mid-term, as well as code, documentation, and explanation of projects during the semester. Specific percentages will be communicated in the first class and posted here.

The types of graded materials will include:

- 40% Late mid-term written exam in November (see [this folder with past exams](past-exams/))
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

The course will follow a similar structure to the [2023 edition](https://gitlab.epfl.ch/lara/cs550/-/tree/2023?ref_type=heads). Project can be a case study in developing a verified piece of software, an implementation of verification tool functionality, or a theoretical result about verification, constraint solving or theorem proving. Students present their projects with a written report as well as by a live presentation of project results, answering our questions.

Note that slides can be found **underneath each lecture video** on switch tube linkes below. 

## Books

* [CalComp] **The Calculus of Computation - Decision Procedures with Applications to Verification**, 2007, [from Springer](https://doi.org/10.1007/978-3-540-74113-8), [from EPFL library](https://www.epfl.ch/campus/library/beast/?isbn=9783540741138), by Aaron Bradley and Zohar Manna.
* [HandMC] **Handbook of Model Checking**, 2018, from [from Springer](https://link.springer.com/book/10.1007/978-3-319-10575-8), [from EPFL Library](https://library.epfl.ch/en/beast?isbn=9783319105758), edited by Edmund M. Clarke, Thomas A. Henzinger, Helmut Veith, Roderick Bloem.
* [HandAR] **Handbook of Practical Logic and Automated Reasoning**, 2009, [from Cambridge University Press](https://doi.org/10.1017/CBO9780511576430) and [from EPFL Library](https://library.epfl.ch/en/beast?isbn=9786612058776), by John Harrison

In the reading list below, HandAR-Ch.2 means Chapter 2 in the Handbook of Practical Logic and Automated Reasoning Above, whereas HandMC-Ch.9 means Chapter 9 of the Handbook of Model Checking, etc.

## NOTE

To see the material, please visit https://mediaspace.epfl.ch , log in with your EPFL credentials and 
[select this channel](https://mediaspace.epfl.ch/channel/CS-550+Formal+Verification/30542). Slides and listings are attached underneath the videos.

## COURSE OUTLINE 


| Week | Day | Date       | Time  | Room   | Topic                           | Videos & Slides              |
| :--  | :-- | :--        | :--   | :--    | :--                             | :--                          |
| 1    | Thu | 12.09.2024 | 15:15 | [GRA330](https://plan.epfl.ch/?room==GR%20A3%2030) | [Lecture 1](https://mediaspace.epfl.ch/playlist/dedicated/30542/0_vw42tr2d/0_3z52dv8y) | [Intro to FV](https://mediaspace.epfl.ch/media/01-01%2C+What+is+Formal+VerificationF/0_3z52dv8y/30542), [Intro to Stainless](https://mediaspace.epfl.ch/media/01-02%2C+First+Steps+with+Stainless/0_tghlsgep/30542), [Auxiliary Assertions](https://mediaspace.epfl.ch/media/01-03%2C+Auxiliary+Assertions+in+Stainless/0_54yx91xi/30542), [Unfolding](https://mediaspace.epfl.ch/media/01-04%2C+Unfolding+recursive+functions+in+Stainless/0_4byxmv9i/30542), [Disasters, Successes, and Inductive Invariants](https://mediaspace.epfl.ch/media/01-05%2C+Disasters%2C+Successes%2C+and+Inductive+Invariants/0_fei98b8f) |
|      |     |           | 17:15 | GRA330 | [Lecture 2](https://mediaspace.epfl.ch/playlist/dedicated/30542/0_b3ga55fo/0_omextd9i)                       | [Dispenser Example](https://mediaspace.epfl.ch/media/02-01%2C+Dispenser+Example+of+Finite+System/0_omextd9i), [Finite Systems Expressed with Formulas](https://mediaspace.epfl.ch/media/02-02%2C+Finite+Systems+Expressed+with+Formulas/0_8a6q0uve) |
|      |     |            |   |   | Reading:                       | HandMC-Ch.10  |
|      |     |            |   |   | Follow:                        | [Stainless Tutorial Videos](https://mediaspace.epfl.ch/playlist/dedicated/30542/0_t2ld6vzn/0_azxgetu9) and [materials](https://epfl-lara.github.io/asplos2022tutorial/)  |
|      | Fri | 13.09.2024 | 13:15 | [INR219](https://plan.epfl.ch/?room==INR%20219) | [Lecture 3](https://mediaspace.epfl.ch/playlist/dedicated/30542/0_thr9uebs/0_tv48ew7w)                       | [What is a Formal Proof?](https://mediaspace.epfl.ch/playlist/dedicated/30542/0_thr9uebs/0_tv48ew7w) and [Propositional Resolution](https://mediaspace.epfl.ch/playlist/dedicated/30542/0_thr9uebs/0_lovmc46b) |
| 2    | Thu | 19.09.2024 | 15:15 | GRA330 | [Lecture 4](https://mediaspace.epfl.ch/playlist/dedicated/30542/0_1c4580tg/0_lovmc46b) [PDF](lectures/prop-resolution.pdf) | continue [Propositional Resolution](https://mediaspace.epfl.ch/playlist/dedicated/30542/0_thr9uebs/0_lovmc46b) |
|      |     |            | 17:15 | GRA330 | [Lab 1](labs/lab1/README.md) | |
|      | Fri | 20.09.2024 | 13:15 | INR219 | [Exercises 1](exercises/Exercises1) | Propositional Logic |
|      |     |            |   |   | Reading:                       | CalComp-Ch.1 ∨ HandAR-Ch.2 |
| 3    | Thu | 26.09.2024 | 15:15 | GRA330 | [Lecture 5](https://mediaspace.epfl.ch/playlist/dedicated/30542/0_x3485b9h/0_hmbkv363) [PDF](lectures/fol.pdf) | SAT solving from [Propositional Resolution](https://mediaspace.epfl.ch/playlist/dedicated/30542/0_thr9uebs/0_lovmc46b). Start [Automating First-Order Logic Proofs Using Resolution ](https://mediaspace.epfl.ch/media/06-01%2C%20Automating%20First-Order%20Logic%20Proofs%20Using%20Resolution/0_hmbkv363) |
|      |     |            | 17:15 | GRA330 | [Lab 2](labs/lab2/README.md) | A communication protocol in Stainless |
|      |     |            |       |        | Reading:   | HandAR-Ch.3 |
|      | Fri | 27.09.2024 | 13:15 | INR219 | [Exercises 2](exercises/Exercises2) | Traces, SAT, models |
| 4    | Thu | 03.10.2024 | 15:15 | GRA330 | Lecture 6 [PDF](lectures/TermModels.pdf) | Continue [Automating First-Order Logic using Resolution](https://mediaspace.epfl.ch/media/06-01%2C%20Automating%20First-Order%20Logic%20Proofs%20Using%20Resolution/0_hmbkv363). [Term Models for First-Order Logic](https://mediaspace.epfl.ch/media/06-03+Term+Models+for+First-Order+Logic/0_jnuljf9n/30542)
|      |     |            | 17:15 | GRA330 | [Lab 3](labs/lab3/README.md) | FOL Resolution |
|      |     |            |       |        |                                 | [Provers Proved New Math Results](https://www.mcs.anl.gov/research/projects/AR/new_results/) ([also in NYT](https://archive.nytimes.com/www.nytimes.com/library/cyber/week/1210math.html)), [SPASS Prover on The Web](https://webspass.spass-prover.org/) |
|      |     |            |       |        | Reading:   | HandAR-Ch.3 |
|      | Fri | 04.10.2024 | 13:15 | INR219 | [Exercises 3](exercises/Exercises3) |            |
| 5    | Thu | 10.10.2024 | 15:15 | GRA330 | [Lecture 7](https://mediaspace.epfl.ch/playlist/dedicated/30542/0_i70unjji/): [vcgen.pdf](lectures/lec07-vcgen.pdf) [Hoare.pdf](lectures/lec07-hoare.pdf) | [Converting Imperative Programs to Formulas](https://mediaspace.epfl.ch/media/07-01%2C+Converting+Imperative+Programs+to+Formulas/0_a525ptjc), [Hoare Logic, Strongest Postcondition, Weakest Precondition](https://mediaspace.epfl.ch/media/08-01%2C+Hoare+Logic%2C+Strongest+Postcondition%2C+Weakest+Precondition/0_8ebxh5n4) | 
|      | Fri | 11.10.2024 | 13:15 | INR219 | [Exercises 4](exercises/Exercises4) | Transition Systems, Hoare Logic, Relations |
| 6    | Thu | 17.10.2024 | **15:05** | GRA330 | Lecture | Guest Lecture of Gilles Barthe |
| 6    | Thu | 17.10.2024 | **16:15** | GRA330 | [Lab 4](labs/lab4/) | Using Lisa Proof Framework |
|      |     |            | 17:15 | GRA330 | [Lecture 8](https://mediaspace.epfl.ch/playlist/dedicated/30542/0_0uec4oov/0_z0hn64mj) [PDF](lectures/lec08-qe.pdf) | Quantifier Elimination |
|      | Fri | 18.10.2024 | 13:15 | INR219 | [Exercises 5](exercises/Exercises5/) | Quantifier elimination exercises |
| -    | Thu | 24.10.2024 | 15:15 |        | Holidays | |
|      |     |            | 17:15 |        | Holidays | |
|      | Fri | 25.10.2024 | 13:15 |        | Holidays | |
| 7    | Thu | 31.10.2024 | 15:15 | GRA330 | Lecture 9: [1](https://mediaspace.epfl.ch/media/12-01%2C+Abstract+Interpretation+Idea/0_5ijgftx3/30542), [2](https://mediaspace.epfl.ch/media/12-02-Live2%2C+Lattices+for+Abstract+Interpretation/0_1v7ouncc/30542), [3](https://mediaspace.epfl.ch/media/Omega+Continuity%2C+Galois+Connection%2C+and+AI+Recipe/0_0rxm5t2s/30542) [PDF](lectures/lec09-ai-fixpoint.pdf) | Abstract Interpration. Lattices. Tarski's fixpoint theorem |
|      |     |            | 17:15 | GRA330 | Labs | |
|      |     |            |       |        | | [Deadline to pick your project topic and Background Paper](project/README.md) |
|      | Fri | 01.11.2024 | 13:15 | INR219 | [Exercises 6](exercises/Exercises6) | |
| 8    | Thu | 07.11.2024 | 15:15 | GRA330 | Lecture 10 [PDF](lectures/lec10-lattices-predicates.pdf) | Fixed Points. Predicates |
|      |     |            | 17:15 | GRA330 | Labs | |
|      |     |            |       |        | | [Deadline to write your Background Paper Review](project/README.md) |
|      | Fri | 08.11.2024 | 13:15 | INR219 | [Exercises 7](exercises/Exercises7/) |  |
| 9    | Thu | 14.11.2024 | 15:15 | GRA330 | Lecture 11: Recursion: [1](https://mediaspace.epfl.ch/media/09-01%2C+Monotonicity+and+Semantics+of+Local+Variables/0_uqnv7pkj/30542), [2](https://mediaspace.epfl.ch/media/09-02%2C+Relational+Semantics+of+Loops/0_iy1fbb8t/30542), [3](https://mediaspace.epfl.ch/media/10-03%2C+Recursion+2/0_w0rgyean/30542) [PDF](lectures/lec11-recursion.pdf) | Finish Predicates. [Recursion](lectures/lec11-recursion.pdf) |
|      |     |            | 17:15 | GRA330 | | |
|      | Fri | 15.11.2024 | 13:15 | INR219 | [Exercises 8](exercises/Exercises8/) | |
| 10   | Thu | 21.11.2024 | 15:15 | GRA330 | Lecture 12 | [Widening](lectures/lec12-widening.pdf), [Loops](lectures/lec12-loop.pdf) |
|      |     |            | 17:15 | GRA330 | | |
|      | Fri | 22.11.2024 | 13:15 | INR219 | Q&A session | |
| 11   | Thu | 28.11.2024 | 15:15 | **GRA330**, **AAC114** | **Midterm**, until 18:00 | [Seat Plan](seatplan.pdf). [Solutions](past-exams/solutions2024.pdf) |
|      | Fri | 29.11.2024 | 13:15 | INR219 | Labs | Work on final course project |
| 12   | Thu | 05.12.2024 | 15:15 | GRA330 | Lecture 13 | [Concurrency](lectures/lec13-concurrency.pdf), [Termination](lectures/lec12-termination.pdf), finish [SMT Solvers](lectures/lec12-smt.pdf) |
|      |     |            | 17:15 | GRA330 | Labs | |
|      | Fri | 06.12.2024 | 13:15 | INR219 | Guest Lecture, Labs | The first part: lecture by Samuel Chassot on [Verified Hash Table](https://link.springer.com/chapter/10.1007/978-3-031-63498-7_18) and related case studies in Stainless. Second part: labs |
| 13   | Thu | 12.12.2024 | 15:15 | GRA330 | Labs | Work on the project. [Advice for Presentations](labs/presentation.md) |
|      |     |            | 17:15 | GRA330 | Labs | Work on the project |
|      | Fri | 13.12.2024 | 13:15 | INR219 | Presentations | Sébastien KOBLER, Jacopo MORETTI |
|      |     |            |       |        |               | Léo LARIGAUDERIE, Daniele PUSCEDDU, Christoph WECH |
|      |     |            |       |        |               | Victor GARVALOV, Tsung-Han LIU, Alexander MÜLLER |
| 14   | Thu | 19.12.2024 | 15:15 | GRA330 | Presentations | Inés ARAUJO CAÑAS, Roman PACCAUD, Sergi SOLER ARRUFAT |
|      |     |            |       |        |               | Jonathan ARNOULT,	Dobrin BASHEV, Fabrice EGGER |
|      |     |            |       |        |               | Alessio DEMATTIA, Heikel JEBALI, Kevin SOLMSSEN |
|      |     |            |       |        |               | Kacper KORBAN, Zhekai JIANG |
|      |     |            | 17:15 | GRA330 | Presentations | Bodong JIA, Matthew MEYER, Amirkhon ALIMOV |
|      |     |            |       |        |               | Myriam HIMMI, Mamoun IMGHI |
|      |     |            |       |        |               | Marwan AZUZ, Guillaume BOYÉ, Alban TRINCHERINI |
|      |     |            |       |        |               | Zhili PAN, Nicola STOCCO, Samuel TEPOORTEN |
|      | Fri | 20.12.2024 | 13:15 | INR219 | Presentations | Mohamad ATWI, Salya DIALLO, Taufiq MOHAMMED |
|      |     |            |       |        |               | Sidonie BOUTHORS, Mai-Linh CORDONNIER |
|      |     |            |       |        |               | Wuyue SUN, Boran XU, Yue YU |
|      |     |            |       |        |               | Giorgio AJMONE, Pedro PALACIOS ALMENDROS, Adrian SCHEMEL |
|      | Sun | 05.01.2025 | 23:59 |        |               | Project report submission deadline on [Moodle](https://moodle.epfl.ch/mod/assign/view.php?id=1125375) |

