# Lab 4: First Order Logic proofs in LISA

### Prelude: Proof Assistants

Proof assistants, also called Interactive Theorem Provers (ITP), are systems that allow for the development of fully formal mathematical proofs.

Usually, the system is built on a small, fixed syntax for mathematical statements, as well as a set of inference rules based on a given logic, such as first order logic. Everything else is then built on top of those small foundations. The main role of the proof assistant is to provide tools and assistance to the user in producing this kind of low level proof: they typically build levels of abstraction to hide the low level formalism and expose higher level concepts. It also provides _tactics_, algorithms that will partially or fully solve mathematical statements of some kind while producing a low level proof.

This approach is quite different from what you've seen in Stainless. While it is possible to state some mathematical statements and properties in Stainless, its aim is moreso in formalizing program semantics rather than mathematical statements. In particular, Stainless doesn't produce explicit proofs: programs and correctness conditions are transformed into SMT formulas (Satisfiability Modulo Theories, i.e. propositional statements over theories such as linear arithmetic, arrays, strings and more). These formulas are then given to a specialized decision procedure, a solver, providing an final statement of satisfiability. Neither the transformation of the program into SMT formulas, nor the decision procedure are witnessed by formal proofs (that is, they are algorithmic procedures, and not formally verified ones). 

Note that a minority of proof assistants designed for mathematics adopt a similar procedure: their set of deduction rules is then an algorithm that decide if a given formula (the conclusion) is entailed by a set of other formulas (the premises).

A system that can produce formal proofs has several advantages, since it ensures a high level of confidence in the correctness of the statements it can accept. However, it requires very advanced decision procedures, that can be difficult to implement, slow at runtime, and sometimes prone to bugs.

### The Lab

In this Lab, you will use LISA, a proof assistant developed in the LARA lab at EPFL. The first step for you is to read [the first chapter of the user manual](https://github.com/epfl-lara/lisa/blob/main/refman/lisa.pdf), though you do not need to install LISA manually. To verify the theorems in the file [Lab04.scala](./Lab04.scala), you can run

```console
$ scala-cli run .
```

You will see 3 theorems printed in green and 6 in yellow. Those correspond to the 9 theorems you can find in the file. 

The goal for this Lab is to complete the proofs of the last 6 theorems so that they are all accepted. All of the tools you need to complete the proofs are in the first chapter of LISA's user manual, and in the 3 proofs given as example. The last two theorems, `richGrandfather` and `greenDragonsAreHappy`, are more challenging, so keep them for last.

You may use any tactic described in the user manual except for the `Tableau` and `Goeland` tactics, though the tactics shown in the comment at the begining of the `Lab04.scala` file should suffice. Make sure to read the examples attentively to understand the syntax.

When you're finished, upload the file `Lab04.scala` on moodle (one submission per group). The deadline for this lab is Friday 01 November, 03:59am.
