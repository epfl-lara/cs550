-- Syntax and semantics of QBF

inductive Formula (Var : Type) : Type where
| var (s : Var)
| const (b : Bool)
| al (v: Var) (g : Formula Var)
| ex (v: Var) (g : Formula Var)
| and (left : Formula Var) (right : Formula Var)
| or  (left : Formula Var) (right : Formula Var)
| not (g : Formula Var)


-- Count one node for each variable, constant, connective, and quantifier.
@[grind]
def size {Var : Type} (f : Formula Var) : Nat :=
  match f with
  | .var _ | .const _ => 1
  | .al _ g | .ex _ g | .not g => 1 + size g
  | .and l r | .or l r => 1 + size l + size r

abbrev Env Var := Var → Bool

@[grind]
def updatedV {Var : Type} [DecidableEq Var]
    (e : Env Var) (x : Var) (b : Bool) : Env Var :=
  fun (v : Var) => if v = x then b else e v

@[grind]
def eval {Var : Type} [DecidableEq Var] (f : Formula Var) (e : Env Var) : Bool :=
  match f with
  | .var s => e s
  | .const b => b
  | .al v g => eval g (updatedV e v false) && eval g (updatedV e v true)
  | .ex v g => eval g (updatedV e v false) || eval g (updatedV e v true)
  | .and l r => eval l e && eval r e
  | .or  l r => eval l e || eval r e
  | .not g   => ! eval g e

-- Simplification
def simpf {Var : Type} (f : Formula Var) : Formula Var :=
  match f with
  | .var _ | .const _ => f
  | .al _ g =>
    match simpf g with
    | .const b => .const b
    | _ => f
  | .ex _ g =>
    match simpf g with
    | .const b => .const b
    | _ => f
  | .not g =>
    match simpf g with
    | .const b => .const (!b)
    | _ => f
  | .and l r =>
    let l' := simpf l
    let r' := simpf r
    match l', r' with
    | .const false, _ | _, .const false => .const false
    | .const true, g | g, .const true => g
    | _, _ => f
  | .or l r =>
    let l' := simpf l
    let r' := simpf r
    match l', r' with
    | .const true, _ | _, .const true => .const true
    | .const false, g | g, .const false => g
    | _, _ => f

theorem simp_eval {Var : Type} [DecidableEq Var] (f : Formula Var) (e : Env Var) :
    eval (simpf f) e = eval f e := sorry

theorem simp_size {Var : Type} (f : Formula Var) :
    size (simpf f) ≤ size f := sorry


-- Negation normal form

@[grind]
def negate {Var : Type} (polarity : Bool) (f : Formula Var) : Formula Var :=
  match polarity with
  | true => f
  | false => Formula.not f

theorem negEval {Var : Type} [DecidableEq Var] (polarity : Bool) (f : Formula Var) (e : Env Var) :
    eval (negate polarity f) e = ((eval f e) = polarity) := by
  match polarity with
  | true => grind
  | false => grind

def nnf_with {Var : Type} (polarity : Bool) (f : Formula Var) : Formula Var :=
  match f with
  | .var _ => negate polarity f
  | .al v g => if polarity then Formula.al v (nnf_with polarity g)
                else Formula.ex v (nnf_with polarity g)
  | .ex v g => if polarity then Formula.ex v (nnf_with polarity g)
                else Formula.al v (nnf_with polarity g)
  | .const b => if polarity then Formula.const b else Formula.const (!b)
  | .and l r => if polarity then
                  Formula.and (nnf_with polarity l) (nnf_with polarity r)
                else
                  Formula.or (nnf_with polarity l) (nnf_with polarity r)
  | .or l r => if polarity then
                  Formula.or (nnf_with polarity l) (nnf_with polarity r)
               else
                  Formula.and (nnf_with polarity l) (nnf_with polarity r)
  | .not g => nnf_with (!polarity) g

theorem nnfw_eval {Var : Type} [DecidableEq Var] (polarity : Bool) (f : Formula Var) (e : Env Var) :
    eval (nnf_with polarity f) e = ((eval f e) = polarity) := sorry

def nnf {Var : Type} (f : Formula Var) : Formula Var := nnf_with true f

theorem nnf_eval {Var : Type} [DecidableEq Var] (f : Formula Var) (e : Env Var) :
    eval (nnf f) e = eval f e := sorry


-- Choose a uniform bound on the size growth of nnf.
def blowUpConst : Nat := sorry

theorem nnf_size {Var : Type} (f : Formula Var) :
    size (nnf f) ≤ blowUpConst * size f := sorry

-- Substitution

@[grind]
def subst {Var : Type} [DecidableEq Var]
  (f : Formula Var) (x : Var) (t : Formula Var) : Formula Var :=
  match f with
  | .var s => if s = x then t else f
  | .const _ => f
  | .al v g => if v = x then f else Formula.al v (subst g x t)
  | .ex v g => if v = x then f else Formula.ex v (subst g x t)
  | .and l r => Formula.and (subst l x t) (subst r x t)
  | .or  l r => Formula.or (subst l x t) (subst r x t)
  | .not g   => Formula.not (subst g x t)

-- Without a free-variable condition, substitution can capture a variable:
-- Taking formula (∀ y, x) and replacing blindly x with y gives non-equivalent ∀ y, y.
theorem subst_lemma_unconditional_false :
    ¬ (∀ (f : Formula String) (x : String) (t : Formula String) (e : Env String),
      eval (subst f x t) e = eval f (updatedV e x (eval t e))) := by
  intro h
  let f := Formula.al "y" (.var "x")
  let env : Env String := (fun _ => true)
  have counterexample := h f "x" (.var "y") env
  grind

-- A variable occurs free when it is not bound by a quantifier.
def free {Var : Type} (x : Var) (f : Formula Var) : Prop :=
  match f with
  | .var s => s = x
  | .const _ => False
  | .al v g | .ex v g => v ≠ x ∧ free x g
  | .and l r | .or l r => free x l ∨ free x r
  | .not g => free x g

-- A sufficient condition: no quantifier encountered by substitution may capture
-- a free variable of t.
def freeFor {Var : Type} (t : Formula Var) (x : Var) (f : Formula Var) : Prop :=
  match f with
  | .var _ | .const _ => True
  | .al v g | .ex v g => (v = x) ∨ (¬ free v t ∧ freeFor t x g)
  | .and l r | .or l r => freeFor t x l ∧ freeFor t x r
  | .not g => freeFor t x g

theorem updatedV_same {Var : Type} [DecidableEq Var]
    (e : Env Var) (x : Var) (a b : Bool) :
    updatedV (updatedV e x a) x b = updatedV e x b := by
  funext v
  grind

theorem updatedV_comm {Var : Type} [DecidableEq Var]
    (e : Env Var) (x y : Var) (a b : Bool) (h : x ≠ y) :
    updatedV (updatedV e x a) y b = updatedV (updatedV e y b) x a := by
  funext v
  grind

theorem subst_lemma {Var : Type} [DecidableEq Var]
    (f : Formula Var) (x : Var) (t : Formula Var) (e : Env Var)
    (h : freeFor t x f) :
  eval (subst f x t) e = eval f (updatedV e x (eval t e)) := sorry
