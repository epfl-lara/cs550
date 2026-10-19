
namespace UsingArithmetic

/- To illustrate linear integer arithmetic in Lean, prove the following theorem.
  You are allowed to use omega. Currently, this tactic does not solve arbitrary
  Presburger arithmetic formulas, so we suggest that you handle quantifiers and propositional
  structure yourself, as in the usual proofs in Lean, then try omega dnd grind when
  you feel that you need to use basic properties of integers.

  If you need to get a remainder, you can use the % operator.
  To type divisibility, you write `\|` or `\mid`.
  -/
theorem multipleOfThreeBetween :
    ∀ x y r : Int, ∃ z : Int,
      (5 ≤ r ∧ x + r ≤ y) → (x < z ∧ z < y ∧ 3 ∣ z) := by
  sorry

end UsingArithmetic

namespace ArithmeticFormula

/- Syntax and semantics of arithmetic formulas (not necessarily linear). -/

/-- integer-valued arithmetic term -/
inductive Term (Var : Type) where
/-- variable-/
| var   (v : Var)
/-- integer constant -/
| const (c : Int)
/-- expression `t1` + `t2`, addition of two terms -/
| plus  (t1 : Term Var) (t2 : Term Var)
/-- expression `t1` * `t2`, multiplication of two terms -/
| times (t1 : Term Var) (t2 : Term Var)
/-- expression `t1` / `t2`, division, rounded towards minus infinity -/
| div   (t1 : Term Var) (t2 : Term Var)
/-- expression `t1` % `t2`, remainder, always between 0 and the absolute value of t2 -/
| rem   (t1 : Term Var) (t2 : Term Var)

/-- arithmetic formula (possibly with quantifiers and division) -/
inductive Formula (Var : Type) where
/-- formula `t1` = `t2`, equality of integer-valued terms -/
| eq   (t1 t2 : Term Var)
/-- formula `t1` < `t2`, strictly less on integer-valued terms -/
| less (t1 t2 : Term Var)
/-- formula `t1` ≤ `t2`, less than or equal on integer-valued terms -/
| leq  (t1 t2 : Term Var)
/-- divisibility, same as `t1` % `t2` = 0 -/
| divisible (t1 t2 : Term Var)
/-- formula denoting ∀ `(v : Int)`, `g` -/
| al (v: Var) (g : Formula Var)
/-- formula denoting ∃ `(v : Int)`, `g` -/
| ex (v: Var) (g : Formula Var)
/-- conjunction, `left` ∧ `right` -/
| and (left right : Formula Var)
/-- disjunction, `left` ∨ `right` -/
| or  (left right : Formula Var)
/-- implication, `left` → `right` -/
| implies (left right : Formula Var)
/-- negation, ¬ `g` -/
| not (g : Formula Var)

/-- Translation of the statement of `multipleOfThreeBetween` into `Formula` with variables of type `String` -/
def exampleAbove: Formula String :=
  .al "x" (.al "y" (.al "r" (.ex "z"
    (.implies
      (.and
        (.leq (.const 5) (.var "r"))
        (.leq (.plus (.var "x") (.var "r")) (.var "y")))
      (.and
        (.less (.var "x") (.var "z"))
        (.and
          (.less (.var "z") (.var "y"))
          (.divisible (.var "z") (.const 3))))))))

abbrev Env (Var : Type) := Var → Int

def term_meaning {Var : Type} (t : Term Var) (e : Env Var) : Int :=
  match t with
  | .var v => e v
  | .const c => c
  | .plus t1 t2 => term_meaning t1 e + term_meaning t2 e
  | .times t1 t2 => term_meaning t1 e * term_meaning t2 e
  | .div t1 t2 => term_meaning t1 e / term_meaning t2 e
  | .rem t1 t2 => term_meaning t1 e % term_meaning t2 e

def updated_env {Var : Type} [DecidableEq Var]
  (e : Env Var) (v : Var) (x : Int) : Env Var :=
    fun w => if w = v then x else e w

def formula_meaning {Var : Type} [DecidableEq Var]
  (f : Formula Var) (e : Env Var) : Prop :=
  match f with
  | .eq t1 t2 => term_meaning t1 e = term_meaning t2 e
  | .less t1 t2 => term_meaning t1 e < term_meaning t2 e
  | .leq t1 t2 => term_meaning t1 e ≤ term_meaning t2 e
  | .divisible t1 t2 => term_meaning t1 e % term_meaning t2 e = 0
  | .al v g => ∀ n : Int, formula_meaning g (updated_env e v n)
  | .ex v g => ∃ n : Int, formula_meaning g (updated_env e v n)
  | .and left right => formula_meaning left e ∧ formula_meaning right e
  | .or left right => formula_meaning left e ∨ formula_meaning right e
  | .implies left right => formula_meaning left e → formula_meaning right e
  | .not g => ¬ formula_meaning g e


end ArithmeticFormula

namespace FormulaTransformations
open ArithmeticFormula

/-- removing implication from a formula by replacing it with negation and disjunction -/
def remove_implies {Var : Type} (f : Formula Var) : Formula Var :=
  match f with
  | .eq _ _ | .less _ _ | .leq _ _ | .divisible _ _ => f
  | .al v g => .al v (remove_implies g)
  | .ex v g => .ex v (remove_implies g)
  | .and left right => .and (remove_implies left) (remove_implies right)
  | .or left right => .or (remove_implies left) (remove_implies right)
  | .implies left right => .or (.not (remove_implies left)) (remove_implies right)
  | .not g => .not (remove_implies g)

/-- removing implication yields an equivalent formula -/
theorem remove_implies_sound {Var : Type} [DecidableEq Var] (f : Formula Var):
    formula_meaning (remove_implies f) = formula_meaning f := by
  sorry

/-- in term `t` replace variable `v` by `vT` -/
def subst_term {Var : Type} [DecidableEq Var]
  (t : Term Var) (v : Var) (vT : Term Var) : Term Var :=
  match t with
  | .var s => if s = v then vT else t
  | .const c => .const c
  | .plus t1 t2 => .plus (subst_term t1 v vT) (subst_term t2 v vT)
  | .times t1 t2 => .times (subst_term t1 v vT) (subst_term t2 v vT)
  | .div t1 t2 => .div (subst_term t1 v vT) (subst_term t2 v vT)
  | .rem t1 t2 => .rem (subst_term t1 v vT) (subst_term t2 v vT)

/-- substituting a term vs updating an evaluation with its value -/
theorem subst_term_lemma {Var : Type} [DecidableEq Var]
  (t : Term Var) (v : Var) (vT : Term Var) (e : Env Var) :
  term_meaning (subst_term t v vT) e =
  term_meaning t (updated_env e v (term_meaning vT e)) := by
  sorry

/-- formula `f` is quantifier-free -/
def quantifier_free {Var : Type} (f : Formula Var) : Prop :=
  match f with
  | .eq _ _ | .less _ _ | .leq _ _ | .divisible _ _ => True
  | .al _ _ | .ex _ _ => False
  | .and left right | .or left right | .implies left right =>
     quantifier_free left ∧ quantifier_free right
  | .not g => quantifier_free g

/-- Substitute in a formula, stopping where `v` is bound.
For quantified formulas this can capture variables of `vT`; soundness below
is restricted to quantifier-free formulas. -/
def subst_formula {Var : Type} [DecidableEq Var]
  (f : Formula Var) (v : Var) (vT : Term Var) : Formula Var :=
  match f with
  | .eq t1 t2 => .eq (subst_term t1 v vT) (subst_term t2 v vT)
  | .less t1 t2 => .less (subst_term t1 v vT) (subst_term t2 v vT)
  | .leq t1 t2 => .leq (subst_term t1 v vT) (subst_term t2 v vT)
  | .divisible t1 t2 => .divisible (subst_term t1 v vT) (subst_term t2 v vT)
  | .al w g => if w = v then f else .al w (subst_formula g v vT)
  | .ex w g => if w = v then f else .ex w (subst_formula g v vT)
  | .and left right => .and (subst_formula left v vT) (subst_formula right v vT)
  | .or left right => .or (subst_formula left v vT) (subst_formula right v vT)
  | .implies left right => .implies (subst_formula left v vT) (subst_formula right v vT)
  | .not g => .not (subst_formula g v vT)

theorem subst_formula_lemma {Var : Type} [DecidableEq Var]
  (f : Formula Var) (v : Var) (vT : Term Var) (e : Env Var)
  (hf : quantifier_free f) :
  formula_meaning (subst_formula f v vT) e =
  formula_meaning f (updated_env e v (term_meaning vT e)) := by
  sorry

/-- Whether a variable occurs in an arithmetic term. -/
def occurs_in_term {Var : Type} (x : Var) (t : Term Var) : Prop :=
  match t with
  | .var v => v = x
  | .const _ => False
  | .plus t1 t2 | .times t1 t2 | .div t1 t2 | .rem t1 t2 =>
    occurs_in_term x t1 ∨ occurs_in_term x t2

theorem term_meaning_updated_env {Var : Type} [DecidableEq Var]
  (t : Term Var) (x : Var) (e : Env Var) (n : Int)
  (hx : ¬ occurs_in_term x t) :
  term_meaning t (updated_env e x n) = term_meaning t e := by
  sorry

/-- Substitution rule soundness: (∃ x, x = t ∧ f) <-> f [x:=t],
provided x does not occur in t. -/
theorem onePoint {Var : Type} [DecidableEq Var]
  (x : Var) (t : Term Var) (f : Formula Var) (qff: quantifier_free f)
  (hx : ¬ occurs_in_term x t) :
  formula_meaning (.ex x (.and (.eq (.var x) t) f)) =
  formula_meaning (subst_formula f x t) := by
  sorry

def is_constant {Var : Type} (t : Term Var) : Prop :=
  match t with
  | .var _ => False
  | .const _ => True
  | .plus t1 t2 | .times t1 t2 | .div t1 t2 | .rem t1 t2 =>
    is_constant t1 ∧ is_constant t2

def is_affine_term {Var : Type} (t : Term Var) : Prop :=
  match t with
  | .var _ | .const _ => True
  | .plus t1 t2 => is_affine_term t1 ∧ is_affine_term t2
  | .times t1 t2 => (is_constant t1 ∧ is_affine_term t2) ∨ (is_affine_term t1 ∧ is_constant t2)
  | .div _ _ | .rem _ _ => is_constant t

theorem const_is_affine {Var : Type} (t : Term Var) (tc : is_constant t) : is_affine_term t := by
  sorry

/-- Rename term variables, possibly changing their type. -/
def renameTerm {A B : Type} (r : A → B) : Term A → Term B
  | .var v => .var (r v)
  | .const c => .const c
  | .plus a b => .plus (renameTerm r a) (renameTerm r b)
  | .times a b => .times (renameTerm r a) (renameTerm r b)
  | .div a b => .div (renameTerm r a) (renameTerm r b)
  | .rem a b => .rem (renameTerm r a) (renameTerm r b)

/-- Rename all formula variables, including quantifier binders.
Injectivity of the renaming ensures that distinct variables remain distinct. -/
def renameFormula {A B : Type} (r : A → B) : Formula A → Formula B
  | .eq a b => .eq (renameTerm r a) (renameTerm r b)
  | .less a b => .less (renameTerm r a) (renameTerm r b)
  | .leq a b => .leq (renameTerm r a) (renameTerm r b)
  | .divisible a b => .divisible (renameTerm r a) (renameTerm r b)
  | .al v g => .al (r v) (renameFormula r g)
  | .ex v g => .ex (r v) (renameFormula r g)
  | .and a b => .and (renameFormula r a) (renameFormula r b)
  | .or a b => .or (renameFormula r a) (renameFormula r b)
  | .implies a b => .implies (renameFormula r a) (renameFormula r b)
  | .not g => .not (renameFormula r g)

theorem renameTerm_meaning {A B : Type} (r : A → B) (t : Term A) (e : Env B) :
    term_meaning (renameTerm r t) e = term_meaning t (fun v => e (r v)) := by
  sorry

theorem renameFormula_meaning {A B : Type} [DecidableEq A] [DecidableEq B]
    (r : A → B) (hr : Function.Injective r) (f : Formula A) (e : Env B) :
    formula_meaning (renameFormula r f) e =
      formula_meaning f (fun v => e (r v)) := by
  sorry

/-- A finite conjunction, with true as the empty conjunction. -/
def allFormulas {V : Type} (fs : List (Formula V)) : Formula V :=
  fs.foldr .and (.eq (.const 0) (.const 0))

theorem allFormulas_meaning {V : Type} [DecidableEq V]
    (fs : List (Formula V)) (e : Env V) :
    formula_meaning (allFormulas fs) e ↔ ∀ f ∈ fs, formula_meaning f e := by
  sorry

end FormulaTransformations

namespace ArithmeticPrograms
open ArithmeticFormula
open FormulaTransformations

inductive Program (Var : Type) where
| pAssign (v : Var) (t : Term Var)
| pIf  (f : Formula Var) (thenBranch : Program Var) (elseBranch : Program Var)
| pHavoc  (v : Var)
| pChoice (p1 p2 : Program Var)

/-- Relation describing a program relates initial environment to a final environment -/
abbrev ProgramRelation Var := Env Var → Env Var → Prop

/-- Relation associated with a given loop-free program -/
def rho {Var : Type} [DecidableEq Var] (p : Program Var) : ProgramRelation Var :=
  match p with
  | .pAssign v t => fun (e e' : Env Var) => e' = updated_env e v (term_meaning t e)
  | .pIf b p1 p2 => fun (e e' : Env Var) =>
      (formula_meaning b e ∧ rho p1 e e') ∨
      (¬ formula_meaning b e ∧ rho p2 e e')
  | .pHavoc v => fun (e e' : Env Var) => ∃ x : Int, e' = updated_env e v x
  | .pChoice p1 p2 => fun (e e' : Env Var) => rho p1 e e' ∨ rho p2 e e'

inductive MaybePrimed (Var : Type) where
-- initial state variable
| unprimed (v : Var)
-- final state variable
| primed (v : Var)
deriving DecidableEq

def env_sum {Var : Type} (e e' : Env Var) : Env (MaybePrimed Var) :=
  fun w => match w with
  | .unprimed v => e v
  | .primed v => e' v

/-- Relate initial and final values, including preservation of untouched variables. -/
def R {n : Nat} (p : Program (Fin n)) : Formula (MaybePrimed (Fin n)) :=
  sorry

theorem R_correct {n : Nat} (p : Program (Fin n)) (e e' : Env (Fin n)) :
    formula_meaning (R p) (env_sum e e') = rho p e e' := by
  sorry

end ArithmeticPrograms
