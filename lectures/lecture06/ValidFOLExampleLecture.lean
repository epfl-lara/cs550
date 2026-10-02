-- This example is used in Lecture 4 of CS-550
theorem runningExample {A: Type} -- domain
    -- signature:
    (a: A) (R : A → A → Prop) (P : A → Prop) (f : A → A → A)
    -- theorem hypotheses:
    (more:    ∀ x, ∃ y, R x y)
    (remains: ∀ x y, R x y → ∀ z, R x (f y z))
    (oneOf:   ∀ x, P x ∨ P (f x a)):
    -- theorem conclusion:
    ∀ x, ∃ y, R x y ∧ P y :=
  fun x =>
  let exy1: ∃ y, R x y := more x
  let ⟨y1, Rxy1⟩ := exy1
  let y2 := f y1 a
  match (oneOf y1) with
  | .inl (Py1 : P y1) =>
    (fun g1: R x y1 ∧ P y1 => ⟨y1,g1⟩) -- if we had g1, we would get the goal
      ⟨Rxy1, Py1⟩
  | .inr (Py2: P y2) =>
    suffices g2: R x y2 ∧ P y2 from ⟨y2,g2⟩ -- suffices: syntax for 'if we had'
    let Rxy2: R x (f y1 a) := remains x y1 Rxy1 (z:= a)
    ⟨Rxy2, Py2⟩
