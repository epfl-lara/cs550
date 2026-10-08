# Part 1 — Syntax and semantics of quantified Boolean formulas



You can find the first task for this lab in the file
[`part1/QBFSyntaxSemantics.lean`](part1/QBFSyntaxSemantics.lean).

As you've seen in the [course][cours], Quantified Boolean formulas (QBF) are
formulas in an extension of propositional logic that allows quantifiers over
Boolean variables, i.e. formulas like: 
$$
  \forall x. \exists y. \exists x. (x \lor z) \land y
$$

[cours]: ../../lectures/lecture06/PropositionalSyntaxSemantics.lean

For this lab, you will need to reason about formula transformations and variable
bindings, and prove correctness and size bounds for your work.

## Exercises

Complete the seven `sorry` placeholders, working through the file in order:

| Declaration | Task |
| :-- | :-- |
| `simp_eval` | Prove that simplification preserves the value of a formula. |
| `simp_size` | Prove that simplification does not increase its size. |
| `nnfw_eval` | Prove correctness of negation normal form for either polarity. |
| `nnf_eval` | Deduce that `nnf` preserves the value of a formula. |
| `blowUpConst` | Choose a fixed natural-number bound on the size growth of `nnf`. |
| `nnf_size` | Prove the size bound using your chosen constant. |
| `subst_lemma` | Prove correctness of substitution under the `freeFor` condition. |

Keep the provided definitions and theorem statements unchanged, apart from
completing `blowUpConst`. You may add helper lemmas, but you'll need to prove
those as well ;)

Start by understanding `Formula`, `size`, `updatedV`, and `eval`.
For recursive proofs, use the structure of the formula and consider which
parameters must vary in the induction hypothesis. The completed proofs in the
file and the lecture examples illustrate how to combine case analysis with
`grind`.

In the substitution section, read the supplied counterexample carefully.
The `freeFor` hypothesis prevents variable capture; it is part of the theorem.
You may need a helper lemma about evaluation when updating a variable that does
not occur free in a formula.

