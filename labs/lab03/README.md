# Week 3: Propositional Logic (with Stainless)

In today's lab you will prove some lemmas about propositional logic.
The provided scala file defines the syntax of boolean formulas as an algebraice data type **Formula**. The function **evaluate(env: Environment, f: Formula)** computes the truth value of **f** as explained in the course. 

The function **instantiation(f:Formula, id:Identifier, value:Boolean)** replaces instances of the Variable **id** by a Literal **value** as expected.

An **Environment** is a ListMap, which is a map implemented as a list of pairs. You can find the implementation, as part of the Stainless library, at https://github.com/epfl-lara/stainless/blob/master/frontends/library/stainless/collection/ListMap.scala.

## Lemmas
We ask you to prove the three lemmas 
- **instantiationIdentityLemma**
- **instantiateStillDefinedLemma**
- **caseAnalysisSoundness**

The difficulty of those lemmas vary and you may need to prove a few auxilliary lemmas to help you. You may also need to use the lemma **addIrrelevantElementLemma** about environment.

Don't modify **require** or **ensuring** closes and make sure stainless accepts your solution. You may want to run stainless with option **--vc-cache=false** before submitting for safety.



## Submission
Once you've completed all proofs, you can submite your Lab03.scala file [on Moodle](https://moodle.epfl.ch/mod/assign/view.php?id=1099233) by Saturday, 16 October 2021, 23:59. Only one member of the group needs to submit.

## Project Session
Project session can be followed in classroom or in [zoom](https://epfl.zoom.us/j/69030789600).
