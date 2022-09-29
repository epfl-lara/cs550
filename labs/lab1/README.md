# Lab 1: Introduction to Stainless

**DEADLINE: 2022-10-12 (Wednesday, 12 October) at 23:59**

In this first lab, you will use Stainless to verify a short scala file.

## Installation

Start by installing stainless version 0.9.6 from its [repository](https://github.com/epfl-lara/stainless/releases/tag/v0.9.6) (download the file named `stainless-dotty-standalone-0.9.6-<your_os>.zip`). The release is an archive containing among other things a binary called **stainless**, that you should make available in your path. Detailed instructions can be found in [this video](https://tube.switch.ch/videos/03edee61). Make sure that you are using **Java 8** (more recent versions will not work). On some Linux distributions, you can use:
- `update-alternatives –config java` (Debian-based);
- [`archlinux-java set java-8-openjdk/jre`](https://wiki.archlinux.org/title/Java#Switching_between_JVM) (Arch-based)

to switch between multiple installed java versions. 

You can check your java version using
```
java -version
```

## Tutorial

Video tutorials for stainless can be found on the tool's [repository](https://github.com/epfl-lara/stainless/#further-documentation-and-learning-materials).

## Lab

The file SubList.scala (in this directory) defines a relation on lists ($`\sqsubseteq`$), which holds when the first list can be embedded in the second list. A few examples:
```math
\newcommand{\slist}[0]{\sqsubseteq}
\newcommand{\seq}[1]{\langle#1\rangle}
\begin{align*}
    \seq{0,2} &\slist \seq{0,1,2} \\
    \seq{1,0} &\not\slist \seq{0,0,1} \\
    \seq{10,5,25} &\slist \seq{70,10,11,8,5,25,22}
\end{align*}
```

The goal is to prove basic properties on this relation, such as reflexivity, transitivity, and antisymmetry. More precisely, you should make Stainless accept the eleven lemmas defined in the code. To verify your proofs, use
```shell
stainless SubList.scala
``` 

The provided configuration file ([stainless.conf](stainless.conf)) will automatically set the timeout to 2 seconds.
You can override this by either changing the configuration file or using the command line, by adding e.g. `--timeout=5` to set the timeout to 5 seconds.
You can also add `--watch` for stainless to automatically run on file save.
```shell
stainless --timeout=5 --watch SubList.scala
```

Some advice:
- Try to understand how you would prove these properties with paper and pencil, and use examples of lists to gain intuition;
- Induction is one of the main methods needed in many cases; see the videos on how to write inductive proofs;
- Prove lemmas in order: earlier lemmas (and their structure) will help you with subsequent lemmas;
- Even though it is not necessary, you can define new lemmas if it helps (but then you have to prove them as well 😊);
- Regarding the four lemmas about concatenation: two very similar lemmas can have vastly different proofs (in both size and difficulty – can you tell why ?).

## Submission

Once you've completed all proofs, you can submit your [SubList.scala](SubList.scala) file on [Moodle](). One submission per group.

