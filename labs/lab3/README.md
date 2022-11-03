# Lab 3: Sequent Calculus Proofs in LISA

In this lab, you will use the LISA proof assistant to formally prove 4 theorems of first order logic and set theory
Start by cloning this specific branch of LISA: 

[https://github.com/epfl-lara/lisa/tree/Lab03]

and open the file lisa/lisa-examples/src/main/scala/Lab03.

The first two theorems are theorems of pure first order logic. You will need to correctly use the rules for quantifier introductions, thinking about the free variables.
Then you will prove that the subset relation is a partial order. Reflexivity is given to you as an example, and you need to prove transitivity and antisymmetry.
The set of rules you may need is indicated atop of the file. You may also look at [LISA's Manual](https://github.com/epfl-lara/lisa/blob/main/Reference%20Manual/lisa.pdf) for more details.

To test your proof, do the following. Open a terminal, and in the root `lisa` folder, type `sbt`. Once sbt has started, type `project lisa-examples` and then `run` to run your code. SBT will ask you which main class you want to run, be sure to pick Lab03.

Warning: If you're on Windows, the terminal by default typically does not support well printing Unicode characters. If you have no linux system, you can try using WSL (windows subsystem for linux) or otherwise run chcp65001 in you terminal before starting sbt and picking a font in your terminal that can show the mathematics unicode symbols.




## Submission
Once you've finished, you can submit your Lab03.scala file [on Moodle](https://moodle.epfl.ch/mod/assign/view.php?id=1099233) by Sunday, 13 November 2022, 23:59. You are given 10 days so that you can ask questions if needed during the next exercise session, but keep in mind that Lab04 will start next week. Only one member of the group needs to submit. 
