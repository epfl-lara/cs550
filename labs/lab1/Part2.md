# Lab 1, part 2: From Arithmetic to a Communication Protocol in Stainless

In this second part of the lab, you'll gain more practice with Stainless. We
will see that Stainless allows us to state and prove properties on traits and
classes, allowing us to write expressive and idiomatic Scala programs. These
techniques will be applied to two different tasks, one for each section (and
`.scala` file).

In the first section, you'll learn how to state and enforce properties on
datatypes, in order to implement and show the correctness of arithmetic
operations on integers.

In the second section, you'll explore a more "systems"-y approach to
verification: you'll be provided with a model of a simple communication
protocol, and your task will be verifying some properties on its execution.

## Part 2.1: Reasoning about arithmetic

Before you write any code, have a look at the contents of
[`GodelNumbering.scala`](src/GodelNumbering.scala), which is a file from the
[stainless repository](https://github.com/epfl-lara/stainless/blob/main/frontends/benchmarks/verification/valid/GodelNumbering.scala)
which we have copied and bundled for your convenience.

The file defines natural numbers with the type `Nat`, with two constructors,
`Zero` and `Succ(n: Nat)`. Follow the file to understand the definition of
addition (`+`), multiplication (`*`), and exponentiation (`pow`) on `Nat` as
given in the file.

In addition, take note of the lemmas that are proven on these operations, and
_how_ these proofs are carried out. The rest of this section will require
showing similar properties, and the techniques may prove useful.

Having taken note of the contents of the first file, turn your attention to
[`Arithmetic.scala`](src/Arithmetic.scala) for the next exercises.

To run and verify your work, you can use the following command:
```shell
$ stainless src/Arithmetic.scala src/GodelNumbering.scala --timeout=2 --watch --compact
```

Note the use of the `--watch` option to speed up iteration, as well as the 
`--compact` option which hides all passing conditions from the output table.

### 2.1.1: Reasoning with `pow`

Prove the lemma `powMul` in the file [`Arithmetic.scala`](src/Arithmetic.scala).

### 2.1.2: Reasoning about integers

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
`Endpoint`s that both hold a sending and a receiving buffer. These contain
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
$ stainless SimpleProtocol.scala
``` 
using `--watch` while you are working for quicker turnaround.

## Submission

Once you've completed all proofs, you can submit the two files
[`Arithmetic.scala`](src/Arithmetic.scala) and
[`SimpleProtocol.scala`](src/SimpleProtocol.scala) on
[Moodle](https://moodle.epfl.ch/mod/assign/view.php?id=1169500&forceview=1).
Only one member of each group should submit a solution.
