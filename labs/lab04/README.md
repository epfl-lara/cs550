# Week 4: Resolution for First Order Logic


## Part 1
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

You can now use your favourite theorem prover to have it automatically deduce who killed who. You can find a list of adequate theorem provers and the corresponding inputs here: https://lara.epfl.ch/w/sav08/tools_demo

Play a bit with those tools (we recommand Isabelle). Try to modify the input formulas or to encode a different problem of your liking.

## Part 2
In the second part, you will implement (part of) a resolution procedure for first order logic as you have seen in the course. The file [Lab04.scala](Lab04.scala) gives you the template to do so.

### Setting
Observe that we are defining Formulas and Terms as simple algebraic data types. For your convenience, we provide you a few pre-implemented functions. Unsurprisingly, `freeVariables` compute the set of free variables occuring in a `Formula` or a `Term`, and `substitute` performs simultaneous substitution of terms for variable inside terms. Fortunately for us, we do not need to implement substitution inside formulas, so we need not deal with capture-avoiding substitutions.

More interestingly, you are also provided with a function that makes sure that all variable names are unique. This will make it easier to implement later steps.

### Transformations

To be subject to clausal resolution, a formula must have a very specific form. This is what you will implement now.
- Start with `negationNormalForm`. This should be equivalence-preserving. The resulting formula should not contain implication symbols, and negation should only appear directly surronding predicates.
- Then implement `skolemizationNegation`, which has to call `negationNormalForm` first . You will need to provide fresh constant names. This transformation is only satisfiability-preserving, and the resulting formula should not contain any existential quantifiers.
- Proceed now to `prenexSkolemizationNegation`, which has to call the previous function first. This step can be tricky in general, but if we make sure that all bound variables have different names (and different from free variables) this step becomes actually simple. Moreover, since all variables are universally quantified, we don't need to keep the prefix of the formula (i.e. the quantifiers) explicitly and we only care about keeping the matrix (i.e. the quantifier-free part of the formula).
- Finally, implement `conjunctionPrenexSkolemizationNegation`. This function first call the previous function, at which point it consists only in alternations of conjunctions and disjunctions up to literals. In the end, the function should output a list of clauses as seen in course. Your formula is allowed to be (singly) exponential in the size of the input formula.


### Proofs
Once the formula is in conjunctive normal form (i.e. a List of Lists of literals), we can do resolution with it. As you've seen in class, a resolution proof is a list of clause, each being either part of the original formula (`Assumed`) or deduced from previous clauses and a specific substitution (`Deduced`).
Implement the function `checkResolutionProof`, which given a resolution proof verify that it is correct.

The system you've just implemented is not quite automated, but it's not so far from automation. 
A practical implementation would also include a unification algorithm that would authomatically compute the adequate substitution at each step. Then, if we implement a proof search procedure that simply explore the whole proof space (i.e. all deducible clauses) carefully so that we don't miss any one clause, then if the initial clauses are unsat we will, necessarily, produce the empty clause at some point. Hence the system would be **refutationally complete**.


Once you've finished your implementation, you can test it with the Mansion example (or other formulas you designed in the first part). Produce a proof that finishes with a clause `killed(a/b/c, a)` to impress the local detectives! You may need to add reasonnable assumptions about equality such as commutativity (x=y -> y=x) or Leibniz's property (x=y -> P(x) -> P(y) for any predicate (or formula) P)



## Submission
Once you've finished, you can submite your Lab04.scala file [on Moodle](https://moodle.epfl.ch/mod/assign/view.php?id=1100580) by Saturday, 30 October 2021, 23:59. Only one member of the group needs to submit.

## Project Session
Project session can be followed in classroom or in [zoom](https://epfl.zoom.us/j/69030789600).
