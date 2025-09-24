# Lab 2: From Arithmetic to a Communication Protocol in Stainless

In this second lab, you will first use Stainless to prove some properties about
algebraically defined natural numbers and integers. As we will see, Stainless
also allows us to prove properties on traits and classes, which are at the core
of every Scala program.

In the second part of the lab, you will elaborate on these techniques to verify
a simple communication protocol. 

By now you should already have some familiarity with Stainless. You may
nonetheless find the resources in the handout of [Lab
1](../lab1/README.md#Tutorial) or on the [Stainless
repository](https://github.com/epfl-lara/stainless) useful.


### Getting the source

To start working on this lab, you can either clone this entire repository, or
download the present directory alone from GitLab (top-right, `Code > Download
this directory`, for example).

> **Note**:
> This lab has two independent parts, separated as sections below.

The provided configuration file ([stainless.conf](stainless.conf)) will
automatically set the SMT solver's timeout to 2 seconds. You can also pass other
options by default such as `--compact` (only displaying VCs Stainless was not
able to prove) or `--watch` by adding respectively `compact=true` and
`watch=true` as new lines in the configuration file.

## Part 1: Reasoning about arithmetic

This part of the lab uses and refers to the file
[`Arithmetic.scala`](src/Arithmetic.scala).

Consider the file [`GodelNumbering.scala`](src/GodelNumbering.scala), which can
also be found in the Stainless repository at
`stainless/frontends/benchmarks/verification/valid/GodelNumbering.scala`. A copy
is bundled with the lab.

The file defines natural numbers with the type `Nat`, with two constructors,
`Zero` and `Succ(n: Nat)`. Follow the file to understand the definition of
addition (`+`), multiplication (`*`), and exponentiation (`pow`) on `Nat` as
given in the file.

To run the files, use

```shell
$ stainless src/Arithmetic.scala src/GodelNumbering.scala --timeout=2 --watch --compact
```

The use of the options `--watch --compact` is highly recommended due to the
large number of VCs generated. This hides passing VCs from the output table.

### 1.1: Reasoning with `pow`

Prove the lemma `powMul` in the file [`Arithmetic.scala`](src/Arithmetic.scala). 

### 1.2: Reasoning about integers

Following the definition of `Nat` in the given files, define a new case class
`ZZ` of unbounded integers containing a sign `sgn: Int` and an absolute value
`abs: Nat`. 

The class must have an invariant that the sign must be either `1` (for
positive integers), `-1` (for negative integers), or `0` (for zero).

Following this, define addition and multiplication on `ZZ`, proving that they
are each commutative and associative, filling in the respective lemmas given. 

## Part 2: Reasoning about Protocols

This part of the lab uses and refers to the file
[`SimpleProtocol.scala`](src/SimpleProtocol.scala).

### The protocol

The communication protocol we will prove properties about involves two
`Endpoint`s that both hold a sending and a receiving buffer. They contain
respectively the messages that still need to be sent, and the ones already
received. The messages go through a `Network` that can be queried to know
whether they have been transmitted. The protocol is then defined as follows:

1. The sender sends a message over the network.
2. It then queries the network to know whether the message has been transmitted.
3. If this is the case, drop the last message sent from the buffer of the sender
   and add it to the buffer of the receiver. Otherwise, skip to step 4.
4. Repeat step 1 with the updated sender and receiver.

In practice this means a message will be sent over and over until it has been
transmitted. Since we want to reason about finite programs, we will run the
protocol for a finite number of iterations. The method simulating the protocol
is `Network.messageExchange`.

### Goal

The protocol, in addition to the classes representing the `Network` and the
`Endpoint`s of the communication, are already implemented. The goal of the lab
is proving some properties of the protocol such as its correctness, optimality
conditions, etc.

As a reminder, these properties are stated in the form of "lemmas", functions
which "do nothing": they return `Unit` and have no effects. You have to fill
these functions with a proof of their statement. The file contains seven
properties on `messageExchange` that you have to prove. Some proofs may be
shorter than others but all of them can be written in less than 7 lines of code.

You are not allowed to change the definitions given in the file or the statement
(parameters/return types, function name, requirements, conclusion, etc.) of the
properties. Even though this is not required, you may define new intermediate
lemmas, but you need to prove them correct as well.

To check your proofs, use
```shell
$ stainless --solvers=smt-z3 SimpleProtocol.scala
``` 
using `--watch` while you are working for quicker turnaround.

## Submission

Once you've completed all proofs, you can submit the two files
[`Arithmetic.scala`](src/Arithmetic.scala) and
[`SimpleProtocol.scala`](src/SimpleProtocol.scala) on
[Moodle](https://moodle.epfl.ch/mod/assign/view.php?id=1169500&forceview=1).
Only one member of each group should submit a solution. 
