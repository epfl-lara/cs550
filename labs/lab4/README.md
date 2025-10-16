# Lab 4: First Order Logic proofs in LISA

In this lab, you will learn about interactive theorem proving using [the Lisa
proof assistant](https://github.com/epfl-lara/lisa) based on first-order logic.
You will familiarize yourself with the syntax and basic proof steps of Lisa, and
use it to prove some first-order logic theorems.

There is only one file to edit for this lab, [Lab04.scala](./Lab04.scala). To
run it, you can use `scala-cli`, as in previous labs:

```console
$ scala-cli .
```

### Prelude: Proof Assistants

Proof assistants, also called Interactive Theorem Provers (ITP), are systems
that allow for the development of completely formal computer-checked
mathematical proofs.

Usually, the system is built on a small, fixed syntax for mathematical
statements, as well as a set of inference rules based on a given logic, such as
first order logic. Everything else is then built on top of those small
foundations. The main role of the proof assistant is to provide tools and
assistance to the user in producing this kind of low level proof: they typically
build levels of abstraction to hide the low level formalism and expose higher
level concepts. It also provides _tactics_, algorithms that will partially or
fully solve mathematical statements of some kind while producing a low level
proof.

This approach is quite different from what you've seen in Stainless. While it is
possible to state some mathematical statements and properties in Stainless, its
aim is more so in formalizing program semantics rather than mathematical
statements. In particular, Stainless doesn't produce explicit proofs: programs
and correctness conditions are transformed into SMT formulas (Satisfiability
Modulo Theories, i.e. propositional statements over theories such as linear
arithmetic, arrays, etc.). These formulas are then given to a specialized
decision procedure, a solver, providing a final statement of satisfiability.
Neither the transformation of the program into SMT formulas, nor the decision
procedure are witnessed by formal proofs (that is, they are algorithmic
procedures, and not formally verified ones). 

## Working with Lisa

Lisa is an ITP written and embedded in Scala. Formulas, theorems, and proofs in
Lisa are simply Scala objects, similar to the constructions you have seen in the
previous labs. These proofs are checked by running the Scala file, which
constructs low-level proofs and invokes the internal proof checker.

A set of example declarations and proofs are given in the file
[Lab04.scala](./Lab04.scala). Alongside the first chapter of the [Lisa user
manual](https://github.com/epfl-lara/lisa/blob/main/refman/lisa.pdf), use these
to familiarize yourself in particular with the construction of terms, formulas,
and sequents.

### Basic types and constructions

All objects in Lisa have one of two base types: `Ind` (for individuals)
representing terms, and `Prop` (for propositions) representing formulas. As
expected, terms can be variables or constants, and can be combined using
function symbols. Formulas can be atomic (predicates applied to terms) or
combinations constructed using first-order logic connectives and quantifiers.

```scala
  val x = variable[Ind] // a term variable named "x"
  val y = variable[Ind]
  val f = function[Ind >>: Ind] //  a function "f" taking a term and returning a term
  val g = function[Ind >>: Ind >>: Ind] // a function "g" taking two terms and returning a term
  val P = predicate[Ind >>: Prop] // a predicate "P" taking a term and returning a formula

  val fx = f(x) // the term "f(x)"
  val gxfx = g(x)(fx) // the term "g(x, f(x))"; note that functions are curried!
  val Pfx = P(f(x)) // the formula "P(x)"
  
  val PxPy = P(x) /\ P(y) // conjunction
  val PxPyOr = P(x) \/ P(y) // disjunction
  val notPx = !P(x) // negation

  val eqxy = x === y // equality

  val existsPx = ∃(x, P(x)) 
  val forallpx = ∀(x, P(x)) // written with unicode
  assert(forallpx == forall(x, P(x))) // ASCII alternative is fine too
```

### Sequents and Theorems

You have seen the "vdash" symbol $\vdash$ in class before. We read $\phi, \psi
\vdash \xi$ as $\xi$ follows from $\phi$ and $\psi$. A sequent is such a pair of
sets of formulas, the premises on the left of the $\vdash$ and the conclusions
on the right. In Lisa, sequents are the central objects of proofs.

"vdash" is written in Lisa as `|-`.

> **Note**: In classical first-order logic, $\phi, \psi \vdash \xi, \chi$ is
> equivalent to $\vdash (\phi \land \psi) \Rightarrow (\xi \lor \chi)$. We will
> typically have at most one formula on the right for now. Also note that if the
> right side is empty, it is the same as $\ldots \vdash \bot$. In Lisa, you can
> write an empty sequent as `() |- ()`.


A theorem is a sequent, alongside a proof of this sequent from axioms. We will
not use any axioms in this lab. They behave like theorems, but have no proofs.

```scala
  val simpleTheorem = Theorem(P(x) /\ P(y) |- P(x)) {
    // proof goes here

    // a proof is a list of steps

    val hypStep = have(P(x) |- P(x)) by Hypothesis
    thenHave(thesis) by Weakening
    // thenHave means from the last step;
    // thesis = the goal of the current proof;
    // we could also have written by expanding:
    have(P(x) /\ P(y) |- P(x)) by Weakening(hypStep)

    // we had a duplicate step, but all that matters is 
    // that the last step proves the thesis!
  }
```

Each step is written as `have(sequent) by proofStep(premises...)`. Some steps
like `Hypothesis` take no premises. A full list of the basic proof steps can be
found in the Lisa manual. The steps you will need for this lab are described
below. For each of the steps, you can find concrete instances in the examples.

Note that when we write $P(x) \vdash P(x)$, $x$ is a free variable, which means
that this statement is true for any value of $x$ (similar to what we did with
implicit free variables in resolution proofs).

<details>

<summary>Basic proof steps</summary>

* Restate: prove a "trivially" true sequent or transformation. Can have zero or
  one premises.

  $$\frac{\phi \vdash \psi}{\phi' \vdash \psi'} \text{Restate}_1 \quad\quad \frac{}{\phi \vdash \psi} \text{Restate}_0$$

  On the left, $\phi' \vdash \psi'$ must be a provable from $\phi \vdash \psi$
  by applying simple propositional equivalences (except distributivity) (e.g. $P
  \land Q \vdash P$ can be transformed to $Q \land P \vdash P$). On the right,
  $\phi \vdash \psi$ must be trivially true (e.g. $P \vdash P$ or $P \land Q
  \vdash P$), or stated like the first case, it must be trivially transformable
  to true.

* Weakening: add more premises or conclusions (+ Restate) to a
  sequent. Has one premise.

  $$\frac{\phi \vdash \psi}{\phi, \xi \vdash \psi} \text{Weakening}_L \quad\quad \frac{\phi \vdash \psi}{\phi \vdash \psi, \chi} \text{Weakening}_R$$

  Or simultaneously on both sides. $\xi$ and $\chi$ can be any formulas.

* Tautology: prove a propositional tautology. Has 0 or more premises.

  Employs a DPLL-like procedure to prove propositional tautologies. More
  powerful but more expensive than restate.

  With 0 premises, prove *any* propositional tautology. `have(...) by Tautology`

  With premises: prove a propositional consequence of the premises `have(...) by Tautology.from(p1, p2, p3, ...)`

  This is a derived step that reduces to low-level proofs.

* LeftForall: introduce a universal quantifier on the left.

  $$\frac{\phi(t), \Gamma \vdash \Delta}{\forall x.\phi(x), \Gamma \vdash \Delta} \text{LeftForall}$$

* RightForall: introduce a universal quantifier on the right.

  $$\frac{\Gamma \vdash \Delta, \phi(y)}{\Gamma \vdash \Delta, \forall x.\phi(x)} \text{RightForall}$$

  Here, $y$ must not appear in $\Gamma, \Delta$ or $\forall x.\phi(x)$.

* LeftExists: introduce an existential quantifier on the left.

  $$\frac{\phi(y), \Gamma \vdash \Delta}{\exists x.\phi(x), \Gamma \vdash \Delta} \text{LeftExists}$$

  Here, $y$ must not appear in $\Gamma, \Delta$ or $\exists x.\phi(x)$.

* RightExists: introduce an existential quantifier on the right.

  $$\frac{\Gamma \vdash \Delta, \phi(t)}{\Gamma \vdash \Delta, \exists x.\phi(x)} \text{RightExists}$$

* InstantiateForall: instantiate a universally quantified formula on the right.

  $$\frac{\Gamma \vdash \Delta, \forall x.\phi(x)}{\Gamma \vdash \Delta, \phi(t)} \text{InstantiateForall}$$
</details>

### Checking proofs

Follow the examples provided in the attached file to familiarize yourself with
these ideas, and experiment with writing and proving some first-order properties
that you have seen in lectures or exercises!

You can construct a context to write proofs in as:

```scala
  object MyProofLibrary extends lisa.Main {
    // your declarations and proofs go here
  }
```

This provides you with the basic imports and definitions to construct formulas
and write proofs. One such object is provided to you. Note that `scala-cli` does
not like it if you have multiple main classes, so for now you can experiment
within the same context. You can check your proofs by simply running the file

```console
$ scala-cli .
```

Correct proofs show up in green, incorrect proofs show up with error messages in
red. If you want to leave a proof incomplete, you can use `sorry` to end the
proof. This shows up in yellow when run.

```scala
  val incompleteTheorem = Theorem(P(x) |- P(y)) {
    sorry
    // same as
    have(thesis) by Sorry
  }
```

## Lab: Proving first-order theorems in Lisa

First running the file, you will see three theorems printed in green and the
rest in yellow. These correspond to the theorems you can find in the file.

The goal for this lab is to complete the proofs of the theorems so that they are
all accepted. 

The last two theorems, `richGrandfather` and `greenDragonsAreHappy`, are more
challenging, so keep them for last.

You may use any tactic described in the user manual except for the `Tableau` and
`Goeland` tactics, though the tactics shown in the comment at the beginning of
file and described above should suffice.

When you're finished, upload the file [`Lab04.scala`](Lab04.scala) on moodle
(one submission per group). The deadline for this lab is Friday 31st October,
03:59am.

