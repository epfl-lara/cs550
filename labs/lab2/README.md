# Lab 2: Resolution for First Order Logic


## Part 1 (Introduction)
To get started, consider the following situation:

Someone who lives in Dreadbury Mansion killed Aunt Agatha. Agatha, the butler, and Charles live in Dreadbury Mansion, and are the only people who live therein. A killer always hates his victim, and is never richer than his victim. Charles hates no one that Aunt Agatha hates. Agatha hates everyone except the butler. The butler hates everyone not richer than Aunt Agatha. The butler hates everyone Aunt Agatha hates. No one hates everyone. Agatha is not the butler. 

This problem can be encoded in First Order Logic. Try to do it before checking the solution!

<details> 
  <summary>Solution </summary>

```math
\exists x. lives(x) \land killed(x,a)
```
```math
lives(a) \land lives(b) \land lives(c) \land \forall x. lives(x) \rightarrow (x=a \lor x=b \lor x=c)
```
```math
\forall x. \forall y. killed(x,y) \rightarrow (hates(x,y) \land \neg richer(x,y))
```
```math
\forall x. hates(a,x) \rightarrow \neg hates(c,x)
```
```math
\forall x. hates(a,x) \leftrightarrow x \not\approx b
```
```math
\forall x. \neg richer(x,a) \leftrightarrow hates(b,x)
```
```math
\forall x. hates(a,x) \rightarrow hates(b,x)
```
```math
\neg \exists x. \forall y. hates(x,y)
```
```math
a \not\approx b
```
</details>

You can now use your favorite theorem prover to have it automatically deduce who killed who. We recommend using Isabelle: https://isabelle.in.tum.de/
```
theory Scratch
imports Main
begin
theorem agatha:
assumes a1 : "EX  x.(lives x & killed x a)"
and     a2 : "lives a & lives b & lives c & (ALL x. (lives x --> (x=a | x=b | x=c)))"
and     a3 : "ALL x. ALL y. (killed x y --> (hates x y & ~richer x y))"
and     a4 : "ALL x. (hates a x --> ~hates c x)"
and     a5 : "ALL x. (hates a x = (x ~= b))"
and     a6 : "ALL x. ((~richer x a) = hates b x)"
and     a7 : "ALL x. (hates a x --> hates b x)"
and     a8 : "~(EX x. ALL y. hates x y)"
and     a9 : "a ~= b"
shows   "killed a a"
sledgehammer [e]
end
```
the line `sledgehammer [e]` will invoke the external automated theorem prover [E](https://wwwlehre.dhbw-stuttgart.de/~sschulz/E/E.html). Click on the "sledgehammer" tab at the bottom of the window and replace the line by the suggested proof method.

Play a bit with the example. Try to modify the input formulas or to encode a different problem of your liking. You can try to use different provers too. If you only use `sledgehammer` without argument, then Isabelle will call 7 different provers.

## Part 2 (Lab)
In the second part, you will implement (part of) a resolution procedure for first order logic as you have seen in the course. Moreover, your code will have to be Stainless-compatible. The file [Resolution.scala](Resolution.scala) gives you the template to do so.

### Setting
The beginning of the file contains a selection of lemmas and utility functions about collections that will be needed for the lab. You need not care too much about them for now. 
Then, you can find the datastructures of terms and formulas that we assume, as well as a some functions testing various properties of the formulas and some utility functions (substitution, free variables and a function to make sure all bound and free variables have different names).

### Transformations
To be subject to clausal resolution, a formula must have a very specific form. This is what you will implement now. Your code should in the end be accepted by stainless, so keep your implementation functional.
- Start with `negationNormalForm`. This should be equivalence-preserving. The resulting formula should not contain implication symbols, and negation should only appear directly surronding predicates, as ensured by the `isNNF` predicate.
- Then implement `skolemizationNegation`, which has to call `negationNormalForm` first . You will need to provide fresh constant names. This transformation is only satisfiability-preserving, and the resulting formula should be in nnf and should not contain any existential quantifiers (predicate `containsNoExistential`).
- Proceed now to `prenexSkolemizationNegation`, which has to call the previous function first. This step can be tricky in general, but if we make sure that all bound variables have different names (and different from free variables) this step becomes actually simple. Moreover, since all variables are universally quantified, we don't need to keep the prefix of the formula (i.e. the quantifiers) explicitly and we only care about keeping the matrix (i.e. the quantifier-free part of the formula).
To this point, if you have implemented the functionalities directly, Stainless will be able to prove the ensuring clauses without much direct help.
- Finally, implement `conjunctionPrenexSkolemizationNegation`. This function first call the previous function, at which point it consists only in alternations of conjunctions and disjunctions up to literals. In the end, the function should output a list of clauses as seen in course. Your formula is allowed to be (singly) exponential in the size of the input formula. This is more tricky to implement in a way that will satisfy Stainless, and you may need to be more explicit with the proof of the ensuring clause. We suggest you to implement helper functions


### Proofs
Once the formula is in conjunctive normal form (i.e. a List of Lists of literals), we can do resolution with it. As you've seen in class, a resolution proof is a list of clause, each being either part of the original formula (`Assumed`) or deduced from previous clauses and a specific substitution (`Deduced`).
Implement the function `checkResolutionProof`, which given a resolution proof verify that it is correct.

The system you've just implemented is not quite automated, but it's not so far from automation. 
A practical implementation would also include a unification algorithm that would automatically compute the adequate substitution at each step. Then, if we implement a proof search procedure that simply explore the whole proof space (i.e. all deducible clauses) carefully so that we don't miss any one clause, then if the initial clauses are unsat we will, necessarily, produce the empty clause at some point. Hence the system would be **refutationally complete**.


Once you've finished your implementation, you can test it with the Mansion example (or other formulas you designed in the first part). Produce a proof that finishes with a clause `killed(a, a)` to impress the local detectives! You may need to add reasonnable assumptions about equality such as commutativity ($`x=y \rightarrow y=x`$) or Leibniz's property ($`x=y \rightarrow P(x) \rightarrow P(y)`$ for any predicate (or formula) $P$)

Also make sure your implementation is accepted by Stainless.

### Stainless tips and tricks & Implementation details
First a recommendation: try to do everything in a functional fashion.
Even though are allowed to use imperative features (but **do not** use the `--full-imperative` flag!), Stainless can reject imperative code which might seem reasonable (in particular when higher-order-functions and mutability are both involved).

Some useful stainless flags:
- `--timeout=s`: sets the verification timeout to `s` seconds;
- `--watch`: do not close stainless after verification, and verify automatically on file save;
- `--functions=f1,f2,...`: only verify functions "f1", "f2",...

#### **Representing identifiers**
In some transformations, fresh names are required to identify newly introduced variables or functions. 
To keep things (relatively) simple, we use two kinds of identifiers to name symbols:
- `Named` can be used freely to name things using free-form strings;
- `Synthetic` are numeric identifiers, reserved by the transformations. This separation makes it easy to generate fresh names (look at the implementation of the `FreshNames` class).

You will need to generate fresh names when implementing `skolemizationNegation`.
To do so, use the `FreshNames` object returned by `makeVariableNamesUnique`.
Otherwise, you might end up accidentally using again a name generated by `makeVariableNamesUnique`.

#### **Wrapping (quasi-)atomic formulas**
At the end of the `conjunctionPrenexSkolemizationNegation` function, one might want to ensure that the conjuncts of the result are (quasi-)atomic (i.e. possibly negated predicates).

One might be tempted to represent this with a `List[List[Formula]]` which satisfies
```scala
_.forall(_.forall(_.isAtom))
```
however, it turns out to be quite painful to prove that such a property is preserved when doing some operations, e.g. appending an atom to the last clause.
Instead, one can take advantage of the type system. We wrap our atoms inside a case class:
```scala
case class Atom(f: Formula) { require(f.isAtom) }
```
and use a `List[List[Atom]]` instead. Then, when appending to the last list, one has to append an `Atom`, which requires its formula to be atomic; thus, the wanted property holds implicitly.

## Compiling

### Scala-cli
If you want to test your program, you can add a a main file along the lines of 
```scala
//> using file "Resolution.scala"
import Resolution.*
import stainless.collection.List

@main
def main = {
  val x = Var(Named("x"))
  val y = Var(Named("y"))
  def p(t: Term) = Predicate(Named("P"), List(t))
  val f = Implies(p(x), p(y))
  println(prenexSkolemizationNegation(f))
}
```
And then run it using [scala-cli](https://scala-cli.virtuslab.org/install)
```shell
scala-cli run Main.scala
```

### SBT
Alternatively, you can use [sbt](https://www.scala-sbt.org/download.html).
First create a project
```shell
sbt new scala/scala3.g8
```
Then copy the files from this lab so that the directory structure is as follows:
```
<project-name> 
  |- project
  |   |- ...
  |
  |- lib                                      (new)
  |   |- stainless-library_2.13-0.9.6.scala   (moved)
  |
  |- src
  |   |- main
  |   |   |- scala
  |   |       |- Main.scala                   (example above)
  |   |       |- Resolution.scala             (moved)
  |   |
  |   |- test
  |       |- ...
  |- .gitignore
  |- build.sbt
  |- README.md
```

Finally you can run the main with
```shell
sbt run
```

> **_Note_**: when compiling the program, many warnings will appear about match exhaustivity. This is normal: we proved in Stainless that all matches were exhaustive, but the scala compiler is not aware of that.

## Submission
Once you've finished, you can submit your [Resolution.scala](Resolution.scala) file [on Moodle](https://moodle.epfl.ch/mod/assign/view.php?id=1169500) by Thursday, 20 October 2022, 23:59. Only one member of the group needs to submit.
