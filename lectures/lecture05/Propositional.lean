-- This is a comment line. Ignored by Lean.

--  For arbitrary propositions p, q, r, this tautology holds.
-- Curly brace parameters will be inferred automatically.
theorem taut1 {p q r : Prop}: ((p → q) ∧ ((¬p) → r)) ↔ ((p ∧ q) ∨ (¬ p) ∧ r) := by grind
-- grind proves propositional tautologies automatically

-- The theorem is easy to prove if we do case analysis on p, then simplify.
-- use \ . to type the dots for case analysis. We use simplifier in both cases,
-- telling to use the assumption saying what the value of p is.
theorem taut2 {p q r : Prop}: ((p → q) ∧ ((¬p) → r)) ↔ ((p ∧ q) ∨ (¬ p) ∧ r) := by
  by_cases h : p
  · -- here we assume p holds
    simp [h]
  · -- here we assume ¬ p holds
    simp [h]

-- When we have complex formulas, it can be useful to do propositional reasoning step by step.
-- For that it can be helpful to realize that implication is just a function.
-- An assumption is an argument to a function. Using a theorem with a given argument
-- means applying the function to that argument.
-- This theorem says that, if we assume p → q, p → (q → r), and r, then we can prove r.
theorem example1 {p q r: Prop}
    (pq_holds:  (p → q))
    (pqr_holds: p → (q → r))
    (p_holds: p)              : r := by
  have  q_holds:  q     := pq_holds p_holds -- modus ponens is just application
  have  qr_holds: q → r := pqr_holds p_holds
  have  r_holds:  r     := qr_holds q_holds
  exact r_holds

-- What we introduced by `have` is just an abbreviation, so we can write proof directly as one by expression
theorem example2 {p q r: Prop}
    (pq_holds:  (p → q))
    (pqr_holds: p → (q → r))
    (p_holds: p)              : r := by
  exact (pqr_holds p_holds ) (pq_holds p_holds)

-- In fact, we can omit both by and exact, what Lean really needs is just that proof term:
theorem example3 {p q r: Prop}
    (pq_holds:  (p → q))
    (pqr_holds: p → (q → r))
    (p_holds: p)              : r := (pqr_holds p_holds ) (pq_holds p_holds)

-- if the theorem is stated as an implication, `intro` command reduces it to previous case
theorem example4 {p q r: Prop}: (p → q) → ((p → (q → r)) → (p → r)) := by
  intro pq_holds      -- p → q
  intro pqr_holds     -- (p → (q → r)
  intro p_holds       -- p
  have  q_holds:  q     := pq_holds p_holds
  have  qr_holds: q → r := pqr_holds p_holds
  have  r_holds:  r     := qr_holds q_holds
  exact r_holds

-- a proof for an implication is a function, so the proof for above is:
theorem example5 {p q r: Prop}: (p → q) → ((p → (q → r)) → (p → r)) :=
  fun pq_holds: p → q =>
    fun pqr_holds : (p → (q → r)) =>
      fun p_holds : p =>
        (pqr_holds p_holds ) (pq_holds p_holds)

theorem K (p q: Prop): p → (q → p) :=
  fun pp: p => fun _: q => pp

theorem I (p: Prop): p → p :=
  fun pp: p => pp

-- A proof for a conjunction is just an angle bracket pair of proofs:
theorem example6 {p q: Prop} (p_holds: p) (q_holds: q): p ∧ q :=
  ⟨p_holds, q_holds⟩

#check And

/-
structure And (a b : Prop) : Prop where
  left : a
  right : b
-/

-- Conjunction is a structure. Proof for conjunction proves each conjunct.
-- We can make and pattern match on such proofs using angle brackets
-- (as for all structure-s)
theorem example7 {p q: Prop}: p ∧ q → q ∧ p := by
  intro pq_holds
  let ⟨p_holds, q_holds⟩ := pq_holds  -- deconstruct pair, get proofs for p and for q
  exact ⟨q_holds, p_holds⟩

/-
inductive Or (a b : Prop) : Prop where
  | inl (h : a) : Or a b
  | inr (h : b) : Or a b
-/

-- disjunction is a tagged union. `left` says we will prove left disjunct
theorem hamlet {b: Prop}: (b ∨ ¬ b) := by
  by_cases bTruth: b
  · left
    assumption
  · right
    assumption

theorem hamlet_in_old_English {b: Prop}: (b ∨ ¬ b) := by
  by_cases bTruth: b
  · exact Or.inl bTruth
  · exact Or.inr bTruth

-- proof by cases on truth value is an if
theorem hamlet_in_logic {b: Prop}: (b ∨ ¬ b) :=
  let : Decidable b := Classical.propDecidable b
  if bTruth: b then Or.inl bTruth else Or.inr bTruth

-- to analyze a hypothesis that is a disjunction, we can use rcases
theorem or_commutes {a b: Prop}: (a ∨ b) → (b ∨ a) := by
  intros aOrb
  rcases aOrb with aHolds | bHolds -- names of assumptions in different cases
  · right
    assumption
  · left
    assumption

/- False and Negation:
  False → p   for any p, proof is False.elim
  ¬ p is just (p → False)
-/

theorem contraExample {p q : Prop} : p → ¬ p → q :=
  fun pp: p =>
    fun np: ¬ p =>
      (np pp).elim      -- can also give type: `(show False from (np pp))` instead of just `(np pp)`

-- intuitionistically valid direction follows from definitions of negation
theorem intuitionisticElim {p : Prop} : p → ¬ ¬ p :=
  fun pp: p =>
    fun np: p → False => np pp

-- classical direction we just invoke from the library
theorem classicalElim {p : Prop} : ¬ ¬ p → p :=
  Classical.byContradiction

-- classical propositional reasoning can all be done using proof by cases
theorem contraPos {p q : Prop}: (¬ p → ¬ q) → (q → p) := by
  intros nPnQ
  intros q_holds
  by_cases pTruth : p
  · assumption
  · have nQ: ¬ q := nPnQ pTruth
    contradiction

-- if you can't remember name of a propositional tautology
-- you can prove it by grind and use it where you need it
theorem deMorgan {x y : Prop} : (¬ (x ∧ y) = (¬ x ∨ ¬ y)) := by grind
