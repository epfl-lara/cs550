namespace Dispenser

-- we model a slot as a count of items it contains from 0 to 10.
abbrev Slot := Fin 11
-- a row of the machine is a (sized) array of slots...
abbrev Row (w : Nat) := Vector Slot w
-- ...and the full grid is an array of rows.
abbrev Grid (w h : Nat) := Vector (Row w) h

-- convenience: update a slot in the grid to new value
def Grid.update {w h} (g : Grid w h) (row : Fin h) (col : Fin w) (new : Slot) :=
  g.set row ((g.get row).set col new)

-- we set the size for our dispenser grid:
-- it will be 4 items wide and 6 items tall.
def width := 4
def height := 6

structure State where
  -- the 4x6 `Grid` of items
  grid : Grid 4 6
  -- the amount of coins the machine contains
  coins : Fin 500
  -- whether or not a transaction has been started
  midTransaction : Bool

-- predicate : is this state the initial state?
def State.isInitial (s : State) : Prop :=
  (∀ l ∈ s.grid, ∀ i ∈ l, i == 10) -- there is 10 of every item
    /\ s.coins == 0                -- no coins have been inserted in total
    /\ !s.midTransaction           -- no transaction is in progress

@[simp]
def State.init : State := {
  grid        := Vector.replicate 6 (Vector.replicate 4 10)
  coins       := 0
  midTransaction := false
}

-- Exercise 0: the initial state is indeed initial
theorem init_isInitial : State.init.isInitial := by
  sorry

-- we use inductive datatypes to record the actions we can take.
inductive Action where
| FirstCoin : Action
| SecondCoin (rowNo : Fin 6) (slotNo : Fin 4) : Action

-- state transition relation, modelled as an inductive relation.
inductive Transition : State -> Action -> State -> Prop where
| First {s : State} :
    -- IF this is the first coin of this transaction
    !s.midTransaction ->
    -- IF there's still room for coins in the machine
    s.coins < 499 ->
    -- THEN we transition from `s` to `s` with one more coin,
    --   mid transaction.
    Transition (s) (.FirstCoin) ⟨s.grid, s.coins + 1, true⟩
| Second (row col) {s} :
    -- IF a transaction is in progress
    s.midTransaction ->
    -- IF there's still room for coins in the machine
    s.coins < 499 ->
    -- IF there's still items in the chosen slot
    (s.grid.get row).get col > 0 ->
    -- THEN we transition from `s` to `s` with one more coin,
    --   no longer mid transaction, and with one item removed
    --   from (`row`, `col`)
    Transition s (.SecondCoin row col)
      ⟨s.grid.update row col ((s.grid.get row).get col - 1),
      s.coins + 1,
      false⟩
notation:99 s₁ "~"a"~>" s₂ => Transition s₁ a s₂

inductive MultiTransition : State -> State -> Prop where
| refl {s} : MultiTransition s s
| step {s₁ s' s₂ a}:
    Transition s₁ a s' -> MultiTransition s' s₂ -> MultiTransition s₁ s₂
notation:99 s₁ "~>*" s₂ => MultiTransition s₁ s₂

def Reachable (s : State) : Prop := State.init ~>* s

-- Exercise 1: proofs!

def midTransactionProp (s : State) : Prop :=
  s.coins % 2 = if s.midTransaction then 1 else 0

theorem midTransactionInit : midTransactionProp .init := by
  sorry

theorem midTransactionStep : ∀ s₁ s₂,
    (s₁ ~>* s₂) -> midTransactionProp s₁ -> midTransactionProp s₂ := by
  sorry

theorem midTransactionCorr : ∀ s, Reachable s -> midTransactionProp s := by
  sorry

end Dispenser
