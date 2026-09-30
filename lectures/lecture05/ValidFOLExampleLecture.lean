-- This example is used in Lecture 4 of CS-550
theorem runningExample {A: Type} -- domain
    -- signature:
    (a: A) (R : A → A → Prop) (P : A → Prop) (f : A → A → A)
    -- theorem hypotheses:
    (more:    ∀ x, ∃ y, R x y)
    (remains: ∀ x y, R x y → ∀ z, R x (f y z))
    (oneOf:   ∀ x, P x ∨ P (f x a)):
    -- theorem conclusion:
    ∀ x, ∃ y, R x y ∧ P y := by
  intros x
  let exy1: ∃ y, R x y := more x
  let ⟨y1, Rxy1⟩ := exy1
  let y2 := f y1 a
  match (oneOf y1) with
  | .inl (Py1 : P y1) =>
    suffices g1: R x y1 ∧ P y1 by exact ⟨y1,g1⟩
    exact ⟨Rxy1, Py1⟩
  | .inr (Py2: P y2) =>
    suffices g2: R x y2 ∧ P y2 by exact ⟨y2,g2⟩
    let Rxy2: R x (f y1 a) := remains x y1 Rxy1 (z:= a)
    exact ⟨Rxy2, Py2⟩
