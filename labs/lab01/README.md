# Week 1: Course Introduction and Stainless

In this first lab, you will use Stainless to verify a short scala file.

## Installation

Start by installing stainless version 0.9.0 from [this repository](https://github.com/epfl-lara/stainless/releases). The release is an archive containing among other things a binary called **stainless**, that you should make available in your path. Detailed instructions can be found in [This video](https://tube.switch.ch/videos/03edee61). Make sure that you are using Java 8. On some Linux distributions, you can use **sudo update-alternatives –config java** to change versions. 

#### Note for Windows users

As Stainless currently does not work out of the box on Windows, we advise you to run it with either the Windows Subsystem for Linux or a virtual machine running Linux. It may also work with something like Cygwin but this has not been tested yet.

## Tutorial

You can follow this tutorial in 4 videos to discover stainless:

- [Part 1/4](https://tube.switch.ch/videos/03edee61) (the installation video linked above)
- [Part 2/4](https://tube.switch.ch/videos/c22ea3e8)
- [Part 3/4](https://tube.switch.ch/videos/7f57f7a9)
- [Part 4/4](https://tube.switch.ch/videos/2a9fd35c)

## Laboratory
The file SubList.scala (in this directory) defines a relation on lists, which holds when the first list can be embedded in the second list. The goal is to prove basic properties on this relation, such as reflexivity, transitivity, and antisymmetry. More precisely, you should make Stainless accept the seven lemmas defined in the code. To do so, use **stainless –timeout=10 SubList.scala** (you can also add **-watch** if you want to actively edit your file)

 Some advice:

- Try to understand how you would prove these properties with paper and pencil, and use examples of lists to gain intuition
- Induction is one of the main methods needed in many cases, see the videos on how to write inductive proofs
- Even though simplest cases may work automatically, in general you may need to prove additional lemmas
- Prove lemmas in order; earlier lemmas (and their structure) will help you with subsequent lemmas

## Submission
Once you've completed all proofs, you can submite your solution SubList.scala file on Moodle.
