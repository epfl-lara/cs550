# Lab 2: Working with Lean

## Part 1 — Syntax and semantics of quantified Boolean formulas

The first part of Lab 2 is available in
[`part1/QBFSyntaxSemantics.lean`](part1/QBFSyntaxSemantics.lean).
Quantified Boolean formulas (QBF) extend propositional logic with quantifiers
over Boolean variables. You will prove correctness and size bounds for formula
transformations, and handle variable binding in substitution.

Optional reading: the
[Lecture 6 example on propositional syntax and semantics](../../lectures/lecture06/PropositionalSyntaxSemantics.lean).

All parts of Lab 2 are **group work**, using the same group registered for Lab 1.
If your group changes, email the TAs.

## Setup and checking your file

Use **Lean 4.34.1** with the Lean 4 extension in your editor.
**Mathlib is not required for this part.**
Open `part1/QBFSyntaxSemantics.lean` and check the Lean messages.

You can also check the file from a terminal. From the repository root, run:

```shell
cd labs/lab2
lean --version
lean part1/QBFSyntaxSemantics.lean
```

The starting file should have **no errors** and seven expected
`declaration uses sorry` warnings. After completing the exercises, the file
should check with no errors and no `sorry` warnings.

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
completing `blowUpConst`. You may add helper lemmas.

Start by understanding `Formula`, `size`, `updatedV`, and `eval`.
For recursive proofs, use the structure of the formula and consider which
parameters must vary in the induction hypothesis. The completed proofs in the
file and the lecture examples illustrate how to combine case analysis with
`grind`.

In the substitution section, read the supplied counterexample carefully.
The `freeFor` hypothesis prevents variable capture; it is part of the theorem.
You may need a helper lemma about evaluation when updating a variable that does
not occur free in a formula.

## Submission

Submission instructions will be released on
[Moodle](https://moodle.epfl.ch/course/view.php?id=13051) soon.
We also plan to provide an automatic grader to help you check your work.
