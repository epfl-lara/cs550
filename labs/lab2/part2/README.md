# Part 2 — Specifying and verifying a system!

Recall the [Dispenser][cours] example from your first lecture, and have
a look at the [file for this part][file].

[cours]: ../../../lectures/lecture02.pdf
[file]: ./Dispenser.lean

You can see how the code first specifies what the system _looks like_, as a
record with multiple fields, before describing how it _behaves_ using what we
call "inductive predicates". These are just ways of building a property by
specifying its possible cases!

Let's take a simple property: `is_even n` states that the positive integer `n`
is even. This property can be stated as the following inductive:

```lean
-- a number is even if
inductive is_even : Prop where
-- case 1: it is 0
| Zero : is_even 0
-- case 2: for any m, n, 
--   IF n is even
--   THEN IF m is equal to n + 2
--   THEN m is even
| NonZero : ∀ n m, is_even n -> m = n + 2 -> is_even m
```

We use a very similar technique to specify the dispenser's behavior, both for
its single steps, as well as the multistep relation, `MultiTransition`. Keep 
in mind that you can do induction on these relations as well! Try it out in a
spare file to see what the result of that is.

Your goal for this lab is to complete the `sorry`-ed statements. Be mindful that
this assignment will test your ability to specify a system, as well as proving
theorems: you'll be required to fill out the definitions of properties you'll
write proofs about! Be mindful about that while doing working on your proofs.

## Exercises

Complete the four `sorry` placeholders, working through the file in order. 
You may add helper lemmas, but you'll need to prove those as well ;)

