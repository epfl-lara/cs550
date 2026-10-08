# Lab 2: Working with Lean

## Part 1 — Syntax and semantics of quantified Boolean formulas

The first part of Lab 2 is available in
[`part1/QBFSyntaxSemantics.lean`](part1/QBFSyntaxSemantics.lean).
You will prove properties of simplification, negation normal form, and
substitution for quantified Boolean formulas (QBF).

Lab 2 is individual work. Submission instructions will be announced separately.

## Before you start

Review the Lean material already released for the course:

- [Lecture 5: Introduction to Lean](../../lectures/lecture05/README.md), including
  [programs and proofs](../../lectures/lecture05/Programming.lean) and the
  [GCD example](../../lectures/lecture05/GCD.lean).
- [Lecture 6: Lean proofs, syntax, and semantics](../../lectures/lecture06/README.md).
- [Propositional logic syntax and semantics](../../lectures/lecture06/PropositionalSyntaxSemantics.lean),
  which provides examples of recursive definitions and correctness proofs.

Read the lecture files separately: they define their own formula types and
should not be pasted into the lab file.

## Setup and checking your file

Use VS Code with the Lean 4 extension, following the
[Lean installation instructions](https://lean-lang.org/install/).
The editor alternatives described in Lecture 5 are also available.

This lab was checked with **Lean 4.31.0**. The included `lean-toolchain` file
selects that version for a local installation managed by Elan.
The file uses Lean's standard library; Mathlib is not required.

1. Update or download the course repository.
2. Open the `labs/lab2` folder in your editor.
3. Open `part1/QBFSyntaxSemantics.lean` and check the Lean messages.

You can also check the file from a terminal. From the repository root, run:

```shell
cd labs/lab2
lean --version
lean part1/QBFSyntaxSemantics.lean
```

The starting file should have **no errors** and seven warnings of the form
`declaration uses sorry`. These warnings mark the exercises. A successful
command while placeholders remain does not mean the lab is complete.
After completing the exercises, the file should check with no errors and no
`sorry` warnings.

For a quick start, you can also paste the file into
[Lean's online editor](https://live.lean-lang.org/), as in Lecture 5.
Save your work locally.

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
