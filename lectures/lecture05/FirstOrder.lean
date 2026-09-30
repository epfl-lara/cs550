
-- Instead of writing a binary predicate P(x,y) we write (P x y)
-- Here, (P : A → A → Prop) where Prop denotes truth values (propositions).
-- Propositions can be true or false and we use them as in propositional logic.

theorem pxpx1 {A: Type} (P: A → Prop) (x: A):
        (P x) →  (P x) := by
  intro px
  exact px

-- proof of implication remains a function,
-- it's just that its type P x has free variables
theorem pxpx2 {A: Type} (P: A → Prop) (x: A):
        (P x) →  (P x) :=
  fun (px: P x) => px

-- The proof of  (∀ x:A, F x) is a function
--  fun (x:A) => ... that returns a proof of F x.
theorem apxpx1 {A: Type} (F: A → Prop):
        ∀ x, ((F x) →  (F x)) := by
  intro x
  intro (fx : F x)
  exact fx

theorem apxpx2 {A: Type} (F: A → Prop):
        ∀ x, ((F x) →  (F x)) :=
  fun (x: A) =>
    fun (px: F x) => px

theorem distAll {A: Type} (F G : A → Prop):
       (∀ x, F x ∧ G x) → ((∀ x, G x) ∧ (∀ x, F x)) := by
  intro (h: (∀ x, F x ∧ G x))
  have g: ∀ x, G x := by
    intro x
    let hx : F x ∧ G x := h x
    let ⟨_, (gx: G x)⟩ := hx
    exact gx
  have f: ∀ x, F x := by
    intro x
    let hx : F x ∧ G x := h x
    let ⟨(fx : F x),_⟩ := hx
    exact fx
  exact ⟨g, f⟩

/- Proof for an existential (∃ x:A, F x) has
     a specific x1:A and the proof of F x1 -/

theorem exP {A : Type} (x1: A) (F : A → Prop) (h: F x1):
    ∃ x, F x :=
  ⟨x1, h⟩

theorem distEx {A: Type} (F G : A → Prop):
       (∃ x, (F x ∨ G x)) → ((∃ x, G x) ∨ (∃ y, F y)) := by
  intro (h: (∃ x, F x ∨ G x))
  let ⟨x1, (fgx1: F x1 ∨ G x1)⟩ := h
  match fgx1 with
  | .inl (fx1 : F x1) =>
    let efx: ∃ y, F y := ⟨x1, fx1⟩
    exact (Or.inr efx)
  | .inr (gx1 : G x1) =>
    let egx: ∃ x, G x := ⟨x1, gx1⟩
    exact (Or.inl egx)
