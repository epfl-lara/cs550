-- Greatest Common Divisor function

partial def gcd1 (a b : Nat) : Nat :=
  if a = 0 then b
  else
    let r := b % a
    gcd1 r a

#check gcd1 48 60
#check gcd1 60 48

-- We cannot prove this, because Lean does not allow reasoning about partial functions:
-- theorem gcdSym1 (x: Nat) (y: Nat) : gcd1 x y = gcd1 y x

-- Indeed, we could even define
partial def f (x: Nat) : Nat :=
  f x + 1

-- don't try this at home:
-- #eval (f 0)

/- Indeed, if we assumed equality
  f x = 1 + f x
 we would get
  0 = 1
-/

-- So to use function definitions, we prove termination!

-- useful fact about modulo
#check Nat.mod_lt
#check Nat.pos_of_ne_zero

def gcd2 (a b : Nat) : Nat :=
  if isZero: a = 0 then b
  else
    let r := b % a
    gcd2 r a
termination_by a
decreasing_by
  have aPos: 0 < a := Nat.pos_of_ne_zero isZero
  exact Nat.mod_lt b aPos
-- ignore the linter bug: https://github.com/leanprover/lean4/issues/2920

-- thanks to termination, we get a definitional equation:
#print gcd2.eq_def

-- fact from the library
theorem modLess {a b : Nat} (h : a < b) : a % b = a := Nat.mod_eq_of_lt h

-- when a<b small, then it's the remainder
theorem gcdSym2_swap (a: Nat) (b: Nat) (h: a < b) : gcd2 a b = gcd2 b a := by
  grind [gcd2.eq_def, modLess h]
-- grind is one of automated provers, good for prop equalities and linear equations.

-- Theorem: the order of args does not matter. Showcases proof by cases.
theorem gcdSym2 (a: Nat) (b: Nat) : gcd2 a b = gcd2 b a := by
  have tri: a = b  ∨  a < b  ∨  a > b := by grind [Nat.lt_trichotomy a b] -- changed order a bit
  rcases tri with _ | _ | _
  -- we have three cases type the funny dot using \ and .
  · have h: a = b := by assumption -- to make case readable
    grind
  · have h: a < b := by assumption
    grind [gcdSym2_swap a b h]
  · have h: a > b := by assumption
    grind [gcdSym2_swap b a h]

-- A lemma, we take it from the library
theorem divisionLemma {k m n : Nat} (h : k ∣ n) : k ∣ m % n ↔ k ∣ m := Nat.dvd_mod_iff h

/- Theorem: gcd divides each argument: showcases proof by stong induction.
   To type divisibility ∣ , write backslash and vertical bar together (not just |).
   The proof restates many statements unnecessarily in the proof,
   so that it is a bit easier to read. A compact proof is 5 lines but harder to follow.
   We avoid propositional reasoning and some equality reasoning using `grind` and `simp`.

   Forward reasoning: use what we know to derive new facts, stated using `have`.
   Backward reasoning: if the goal can be simplified, use `suffices` with a short proof that it suffices.
-/
theorem gcdDivides (a: Nat): ∀ (b : Nat), gcd2 a b ∣ a ∧ gcd2 a b ∣ b := by
  induction a using Nat.strongRecOn with
  | ind a ih =>
    have IH: ∀ (m : Nat), m < a → ∀ (b : Nat), gcd2 m b ∣ m ∧ gcd2 m b ∣ b := ih --restate for readiability
    intro b
    by_cases isZero : a = 0 -- proof of this case can be made in one line, this just for explanation:
    · have h : a = 0 := by assumption
      subst a
      suffices gcd2 0 b ∣ 0 ∧ gcd2 0 b ∣ b from by assumption
      have dvdzero: ∀ (x : Nat), x ∣ 0 := by simp -- the name of this fact is hidden but simp knows it
      have first:    gcd2 0 b ∣ 0 := dvdzero (gcd2 0 b) -- for illustration only; could have just done simp
      have second1: gcd2 0 b = b := by simp [gcd2] -- gcd2 was not marked for auto-simplification, so must be given
      have second2: b ∣ b := by simp
      have second:  gcd2 0 b ∣ b := by grind -- grind picks up assumptions, like second1 and second2
      exact ⟨first, second⟩ -- proof for a conjunction is a pair of proofs for each conjunct
      -- let's move to the more interesting case now:
    · have h : a ≠ 0 := by assumption
      let r := b % a
      have s: gcd2 a b = gcd2 r a := by grind [gcd2.eq_def]
      suffices gcd2 r a ∣ a  ∧  gcd2 r a ∣ b from by grind
      have remainderLess : r < a := Nat.mod_lt b (Nat.pos_of_ne_zero h) -- combining lemmas in an expression tree
      -- passing some of the arguments by name to IH instead of by position. We get two individual assumptions
      have ⟨_, _⟩ := IH (m := r) remainderLess (b := a)
      -- restate what we got from IH:
      have goal1:        gcd2 r a ∣ a  := by assumption
      have divRemainder: gcd2 r a ∣ r  := by assumption
      have goal2:        gcd2 r a ∣ b  := by grind [divisionLemma]
      exact ⟨goal1, goal2⟩
