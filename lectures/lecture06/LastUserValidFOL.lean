
#check Classical.em -- excluded middle

-- There is always a last user of AI: if that person uses AI, then all do.
theorem lastToUse1 {A: Type} [Inhabited A]
        (usesAI: A → Prop):
        ∃ x: A, (usesAI x → ∀ y, usesAI y) := by
   match Classical.em (∀ z, usesAI z) with -- like by_cases
   | .inl (allUse: ∀ z, usesAI z) =>
      let impl: usesAI default → ∀ (y : A), usesAI y := by simp [allUse]
      exact ⟨default,impl⟩
   | .inr (notAllUse: ¬ ∀ z, usesAI z) =>
      simp at notAllUse
      let ⟨x1, notAll⟩ := notAllUse
      suffices g: usesAI x1 → ∀ y, usesAI y by exact ⟨x1, g⟩
      intro usesx1
      contradiction

-- A notational variant of the previous proof
theorem lastToUse2 {A: Type} [Inhabited A]
        (usesAI: A → Prop):
        ∃ x: A, usesAI x → ∀ y, usesAI y := by
   by_cases allUse: (∀ z, usesAI z) -- shorter but uses anonymous ·
   · let impl: usesAI default → ∀ y, usesAI y := (fun _ => allUse)
     exact ⟨default,impl⟩
   · simp at allUse -- simp makes it into ∃ x, ¬ usesAI x
     let ⟨x1, notx1uses⟩ := allUse
     suffices g: usesAI x1 → ∀ y, usesAI y by exact ⟨x1, g⟩
     intro usesx1
     exact (notx1uses usesx1).elim  -- obtain False, use it to prove the goal
