# Lab 3: Resolution for First Order Logic

## Lab

### Prelude
The format of this lab is a bit different from the previous ones:
you won't have to prove properties of a program using stainless.
Instead, you will implement a resolution proof checker in scala.
We still require that your implementation is accepted by stainless; this will ensure that some basic properties hold, and that all your functions terminate.

The implementation is split across multiple files, but you will only need to change [Resolution.scala](Resolution.scala).
We will explain the content of the other files as they become relevant.

For this lab, we provide you with some tests for your code. Use
```bash
> scala-cli test .
```
to run them. We only provide you with some basic tests; as such, we strongly encourage you to add new tests.

We recommend that you frequently check that your implementation is accepted by stainless.
You can use
```bash
# Remember to add --solvers=smt-z3 if on MacOS
> stainless src/*.scala --functions=<name of the functions you are working on>
# E.g.
> stainless src/*.scala --functions=negationNormalForm,skolemizationNegation
```
to only check a handful of functions. 
The `--watch`, `--timeout=<seconds>` and `--compact` flags might also prove useful.

### Part one: Transforming formulas
The first thing to do is to put formulas in a form suitable for clausal resolution.

Have a look at [Formulas.scala](Formulas.scala) to see how identifiers, terms and formulas are described. You can ignore everything after (and including) `Literal` for now.
It also defines some operations and predicates on them (e.g. `substitute`, `freeVariables`, `containsNoExistential`) which will be useful.

The transformation is done using 5 successive equivalence preserving transformations.
You must have seen these in class, so we won't describe them in details; 
please refer to the lectures for complete descriptions.
- `makeVariableNamesUnique` is already implemented. It renames all variables so that each one is defined only once. You can see its behavior on some examples in [Test.scala](Test.scala).
- `negationNormalForm` pushes the negation operators as far down the tree as possible.
- `skolemizationNegation` replaces all existential quantifiers with a skolem function. Note that having the formula in negation normal form and without name repetitions greatly simplifies its implementation. As such, its first step will be to apply the previous transformations.
- `prenexSkolemizationNegation` pulls all quantifiers to the top of the formula. Once again, this is greatly simplified by the absence of existential quantifiers. This also means that all the quantifiers at the top will be universal quantifiers and can be left implicit; as such, this function should only return the matrix of the formula.
- `conjunctionPrenexSkolemizationNegation` puts the formula in conjunctive normal form (CNF). Note that from that point on, formulas are represented using `List[List[Literal]]`, so you should have a look at the remainder of [Formulas.scala](Formulas.scala).

### Part two: Proof checking
Once a formula is in conjunctive normal form (i.e. a `List[List[Literal]]`), one can use it to do proofs using resolution.
As you've seen in class, a resolution proof is a list of clauses, each one being either part of the original formula (`Assumed`) or deduced from two previous clauses and a specific substitution (`Deduced`).
Implement the function `checkResolutionProof` which, given a resolution proof, verifies that it is valid.

The system you've just implemented is not quite automated yet, but isn't so far from it either.
A practical implementation would also include a unification algorithm that would automatically compute the adequate substitution at each step. Then, a simple proof search procedure could try to unify all pairs of clauses, exploring the whole proof space.

If the initial formula was unsat, this procedure would end up producing the empty clause at some point. Hence this theorem-proving technique is **refutationally complete**.

### Part three: The Dreadsbury Mansion Mystery
It is now time to use our proof checker!

We will use it to get to the bottom of the following mystery:
> Someone who lives in Dreadbury Mansion killed Aunt Agatha. Agatha, the butler, and Charles live in Dreadbury Mansion, and are the only people who live therein. A killer always hates his victim, and is never richer than his victim. Charles hates no one that Aunt Agatha hates. Agatha hates everyone except the butler. The butler hates everyone not richer than Aunt Agatha. The butler hates everyone Aunt Agatha hates. No one hates everyone. Agatha is not the butler.

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

We implemented this in [Mansion.scala](Mansion.scala).
We also added some additional assumptions, such as commutativity of equality and Leibniz's property for all predicates.

The transformation you implemented are then applied to all of these, resulting in the following assumed clauses:
<details>
<summary>Clauses</summary>

```
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

It introduces two skolem functions `$0()` which represents the killer, and `$11(x)` which given `x`, returns someone `x` doesn't hate. We defined `killer` and `notHatedBy` to allow you to use these more easily.

As an example, one can then show that the killer is among Agatha, Butler and Charles by unifying assumptions 0 and 5 using the assignment `$1 |-> $0()`.

Your first task is to prove that Charles is innocent.
To do so, run
```shell
> scala-cli run --watch . -- 1
```
You should then complete `charlesInnocent` with some steps (`< 5`) to prove his innocence.
The `--watch` flag makes it so that your file will be re-compiled and run every time you do a change (and save). This will allow you to solve this interactively: run to see the initial assumptions, add a proof step, save (which triggers a re-run), see if your step is accepted by the checker, change it, save, add a new step, ...; rinse and repeat until you proved Charles' innocence.

Finally, you should prove that Agatha is the culprit.
This require many steps; we did most of the reasoning for you, you should just add the few (`< 5`) final steps, using the same workflow as for the for the first proof fragment. Use this command:
```shell
> scala-cli run --watch . -- 2
```

## Submission
Once you have implemented, you can submit your [Resolution.scala](Resolution.scala) file on [Moodle](https://moodle.epfl.ch/mod/assign/view.php?id=1099233).

You are not allowed to change the signature of any function. If you add helper functions, they should be in [Resolution.scala](Resolution.scala).

Only one member of each group should submit a solution.