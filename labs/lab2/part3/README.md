# Part 3 — Arithmetic and Programs

Consult the [Lean file](ArithmeticPrograms.lean) and complete all sorries. 

We start by reasoning about arithmetic within Lean (when we manipulate variables of type `Int`). Lean has a procedure `omega`. It does not behave as a decision procedures for full quantified linear arithmetic, but can be helpful for quantifier-free formulas with divisibility and inequalities. 

Subsequently, we define first-order logic formulas interpreted over arithmetic operations. This is a fairly general definition, which also serves as a concrete example of how to assign meaning to formulas in first-order logic.

We define several simple transformations on such formulas, including one-point rule. We hint at ways to specify restrictions on terms, such as terms evaluating to a constant or terms being linear, and define a few auxiliary transformations, such as renaming.

Finally, we revise formally the relationship between loop-free programs and formulas that represent them by relating initial and final variables. To keep things simple for proofs, we omit the sequential composition, keeping only basic statements and conditionals.
