# Lab 3: Semantics and Resolution for First Order Logic

## Lab Overview

### Prelude
The format of this lab is a bit different from the previous ones: you won't have
to prove properties of a program using Stainless. Instead, you will implement
first-order logic semantics and a resolution proof checker in Scala. We still
require that your implementation is accepted by Stainless; this will ensure that
some basic properties hold, and that all your functions terminate.

The implementation is split across multiple files, and you will need to modify
and submit the following files:

- [src/Interpretation.scala](src/Interpretation.scala): first-order logic semantics
- [src/Resolution.scala](src/Resolution.scala): formula transformations and
  resolution proof checking
- [src/MansionFragments.scala](src/MansionFragments.scala): proof fragments for
  the Dreadsbury Mansion mystery

For this lab, we provide you with some tests that your code must pass. Use

```bash
$ scala-cli test .
```

to run them. We only provide you with some basic tests; as such, we strongly
encourage you to add new tests to assist debugging.

We recommend that you frequently check that your implementation is accepted by Stainless.
You can use

```bash
$ stainless src/*.scala --functions=<name of the functions you are working on>
# E.g.
$ stainless src/*.scala --functions=negationNormalForm,skolemizationNegation
```

to only check a set of chosen functions. The options `--watch`,
`--timeout=<seconds>` and `--compact` are useful as always.

### Formulas and Terms

Before you begin working, take a look at [Formula.scala](src/Formula.scala) to
familiarize yourself with the representation of terms and formulas. Some basic
operations and syntactic sugar to build formulas are provided.

You can ignore everything after (and including) `Literal` for now.

## Semantics of First-Order Logic

This section of the lab focuses on implementing the semantics of first-order
logic. This corresponds to the operation $[\![\phi]\!]_{(D, e)}$ seen in class.

You will be mainly working in
[src/Interpretation.scala](src/Interpretation.scala).

First, familiarize yourself with the definitions of the traits `Domain` and
`Environment`. An `Interpretation` is a pair of a `Domain` and an `Environment`.
Match these to the similarly named concepts seen in class.

Each domain, alongside defining the set of elements it represents, has an
associated Scala type from which its elements are drawn. This type is mainly
used to ensure that the elements of the domain are manipulated in a type-safe
manner. 

The environment, given a domain type, interprets functions, relations, and
variables.

You must implement the following functions:

- `Domain.forall`, checking if a predicate holds for all elements of the domain
- `Domain.exists`, checking if a predicate holds for at least one element of the
  domain
- `evalTerm`, corresponding to $[\![t]\!]_{(D, e)}$ for a term $t$
- `evalFormula`, corresponding to $[\![\phi]\!]_{(D, e)}$ for a formula $\phi$

Your functions must terminate if the domain (`Domain.elements`) is finite.

> **Note**:
> Take care to account for shadowing of variables during evaluation.

An example of a non-trivial domain is given in
[FiniteField.scala](src/FiniteField.scala), defining the prime fields $(Z/pZ, +,
*, 0, 1)$ for each prime $p$.

The tests ([InterpretationSuite.scala](test/InterpretationSuite.scala)) check
whether your implementation correctly evaluates some formulas on finite fields
as well as a Boolean algebra domain.

## Resolution Proof Checking

### Part one: Transforming formulas

For this part, you will be working in [Resolution.scala](src/Resolution.scala).

Before performing resolution proofs, formulas must be transformed into a prenex
Skolem conjunctive normal form.

The transformation is done using 5 successive satisfiability preserving
transformations. The transformations have been described in class, so we detail
them here. Please refer to the lectures for complete descriptions.

- `makeVariableNamesUnique` is already implemented. It renames all variables so
  that each one is defined only once. You can see its behavior on some examples
  in [TransformationSuite.scala](test/TransformationSuite.scala).
- `negationNormalForm` pushes the negation operators as far down the tree as
  possible.
- `skolemizationNegation` replaces each existential quantifier with a Skolem
  function. Note that having the formula in negation normal form and without
  repeated names greatly simplifies its implementation. As such, its first step
  will be to apply the previous transformations.
- `prenexSkolemizationNegation` pulls all quantifiers to the top of the formula.
  Once again, this is greatly simplified by the absence of existential
  quantifiers. This also means that all the quantifiers at the top-level will be
  universal quantifiers and can be left implicit; as such, this function should
  only return the matrix of the formula.
- `conjunctionPrenexSkolemizationNegation` puts the formula in conjunctive
  normal form (CNF). Note that from this point on, formulas are represented
  using `List[Clause]`, so you should have a look at the remainder of
  [Formula.scala](src/Formula.scala) to find the details of the `Literal` and
  `Clause` structures.

### Part two: Proof checking

For this part as well, you will be working in
[Resolution.scala](src/Resolution.scala).

Once a formula is in conjunctive normal form (i.e. a `List[Clause]`), we can use
it to write resolution proofs. As you have seen in class, a resolution proof is
a list of clauses, each one being either part of the original formula
(`Assumed`) or deduced from two previous clauses and a specific instantiation
(`Deduced`). Implement the function `checkResolutionProofStep` which, given a
resolution proof step, verifies that it is valid. This is extended to an entire
proof by `checkResolutionProof`.

The system you have implemented is not quite automated yet, but isn't so far
from it either. A practical implementation would also include a unification
algorithm that would automatically compute the adequate instantiations at each
step. Then, a simple proof search procedure could try to unify all pairs of
clauses, exploring the whole proof space.

If the initial formula was unsatisfiable, this procedure would end up producing
the empty clause at some point. Hence, this theorem-proving technique is
**refutationally complete**.

### Part three: The Dreadsbury Mansion Mystery

It is now time to use our proof checker! For this part, you will be working in
[MansionFragments.scala](src/MansionFragments.scala). You will need to refer to
[Mansion.scala](src/Mansion.scala) as well.

We will use it to get to the bottom of the following mystery:

> Someone who lives in Dreadbury Mansion killed Aunt Agatha. Agatha, the butler,
> and Charles live in Dreadbury Mansion, and are the only people who live
> therein. A killer always hates his victim, and is never richer than his
> victim. Charles hates no one that Aunt Agatha hates. Agatha hates everyone
> except the butler. The butler hates everyone not richer than Aunt Agatha. The
> butler hates everyone Aunt Agatha hates. No one hates everyone. Agatha is not
> the butler.

Stated more formally:

```math
\begin{align}
& \exists x. lives(x) \land killed(x,a) \\
& lives(a) \land lives(b) \land lives(c) \land \forall x. lives(x) \rightarrow (x=a \lor x=b \lor x=c) \\
& \forall x. \forall y. killed(x,y) \rightarrow (hates(x,y) \land \neg richer(x,y)) \\
& \forall x. hates(a,x) \rightarrow \neg hates(c,x) \\
& \forall x. hates(a,x) \leftrightarrow x \not= b \\
& \forall x. \neg richer(x,a) \leftrightarrow hates(b,x) \\
& \forall x. hates(a,x) \rightarrow hates(b,x) \\
& \neg \exists x. \forall y. hates(x,y) \\
& a \not= b
\end{align}
```

> **Note**: The code and text beyond **will** spoil the mystery. Solve it on
> your own first if you wish to!

We have written these using our representation in
[Mansion.scala](src/Mansion.scala). We have also added some additional
assumptions, such as commutativity of equality and Leibniz's property for all
predicates.

The transformations you implemented are then applied to all of these, resulting
in the following assumed clauses (`Mansion.assumptions`):

<details>
<summary>Clauses</summary>

```scala
 0 Assumed             : lives($0())
 1 Assumed             : killed($0(), a())
 2 Assumed             : lives(a())
 3 Assumed             : lives(b())
 4 Assumed             : lives(c())
 5 Assumed             : (((¬lives($1) ∨ =($1, a())) ∨ =($1, b())) ∨ =($1, c()))
 6 Assumed             : (¬killed($2, $3) ∨ hates($2, $3))
 7 Assumed             : (¬killed($2, $3) ∨ ¬richer($2, $3))
 8 Assumed             : (¬hates(a(), $4) ∨ ¬hates(c(), $4))
 9 Assumed             : (¬hates(a(), $5) ∨ ¬=($5, b()))
10 Assumed             : (=($6, b()) ∨ hates(a(), $6))
11 Assumed             : (¬hates(b(), $7) ∨ ¬richer($7, a()))
12 Assumed             : (richer($8, a()) ∨ hates(b(), $8))
13 Assumed             : (¬hates(a(), $9) ∨ hates(b(), $9))
14 Assumed             : ¬hates($10, $11($10))
15 Assumed             : ¬=(a(), b())
16 Assumed             : (¬=($12, $13) ∨ =($13, $12))
17 Assumed             : ((¬=($15, $16) ∨ ¬killed($15, $14)) ∨ killed($16, $14))
18 Assumed             : ((¬=($18, $19) ∨ ¬hates($18, $17)) ∨ hates($19, $17))
19 Assumed             : ((¬=($21, $22) ∨ ¬hates($20, $21)) ∨ hates($20, $22))
```
</details>

It introduces two Skolem functions `$0()` which represents the killer, and
`$11(x)` which given `x`, returns someone `x` doesn't hate. We defined
`Mansion.killer` and `Mansion.notHatedBy` to allow you to use these more easily.

As an example, one can then show that the killer is among Agatha, Butler and
Charles by unifying assumptions `0` and `5` using the assignment `$1 |-> $0()`.
This is done in `buildFirstPart`.

Your first task is to prove that Charles is innocent! In the file
[MansionFragments.scala](src/MansionFragments.scala), you will find a proof
fragment `charlesInnocent` which you should complete (<5 steps). An example
proof step is given as reference. Your proof should make use of the assumption
clauses and your own.

To print and check your proof, run
```shell
$ scala-cli run --watch . -- 1
```

The `--watch` flag makes it so that your file will be re-compiled and run every
time you make a change (and save). This will allow you to solve this
interactively: run to see the initial assumptions, add a proof step, save (which
triggers a re-run), see if your step is accepted by the checker, change it,
save, add a new step, and so on. Rinse and repeat until you've proved Charles'
innocence!

Finally, you need to prove that Agatha is the culprit. This requires many steps;
we have most of the reasoning for you (`Mansion.prelude`), you should just add
the few (`< 5`) final steps, using the same workflow as for the first
proof fragment. Complete the proof in `MansionFragments.agathaKilledAgatha`.

To print and check your proof as before, run:
```shell
$ scala-cli run --watch . -- 2
```

## Grading and Submission

Your implementation must pass the provided tests. You can check them with

```bash
$ scala-cli test .
```

You can choose to run only some tests with `--test-only <regex>` where `<regex>`
matches the test names you want to run. See `scala-cli test --help` for more.

Once you're done, you can submit files [Resolution.scala](src/Resolution.scala),
[Interpretation.scala](src/Interpretation.scala), and
[MansionFragments.scala](src/MansionFragments.scala) on
[Moodle](https://moodle.epfl.ch/mod/assign/view.php?id=1099233).

You are not allowed to change the signature of any function. If you add helper
functions, they should be in [Resolution.scala](src/Resolution.scala).

Only one member of each group should submit a solution.