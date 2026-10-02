abbrev Variable := String

inductive Formula: Type where
| Fvar (s : Variable)
| Fconst (b : Bool)
| Fand (left : Formula) (right : Formula)
| For  (left : Formula) (right : Formula)
| Fnot (left : Formula)

open Formula

def ex1 : Formula :=
   For (Fand (Fvar "p") (Fvar "q"))
       (Fnot (Fvar "p"))

abbrev Valuation := Variable → Bool

-- Bool is not a Prop, it is always a constant true or false
-- and its values true,false are never types of proof

def eval (f : Formula) (e : Valuation) : Bool :=
  match f with
  | Fvar s => e s
  | Fconst b => b
  | Fand l r => eval l e && eval r e
  | For  l r => eval l e || eval r e
  | Fnot g   => ! eval g e

-- Negation normal form

def negate (polarity : Bool) (f : Formula) : Formula :=
  match polarity with
  | true => f
  | false => Fnot f

theorem negEval (polarity : Bool) (f : Formula) (e : Valuation) :
    eval (negate polarity f) e = ((eval f e) = polarity) := by
  match polarity with -- I wish grind did this split automatically
  | true => grind [negate, eval]
  | false => grind [negate, eval]

def nnf_with (polarity : Bool) (f : Formula) : Formula :=
  match f with
  | Fvar _ => negate polarity f
  | Fconst b => if polarity then Fconst b else Fconst (!b)
  | Fand l r => if polarity then
                  Fand (nnf_with polarity l) (nnf_with polarity r)
                else
                  For (nnf_with polarity l) (nnf_with polarity r)
  | For l r => if polarity then
                  For (nnf_with polarity l) (nnf_with polarity r)
               else
                  Fand (nnf_with polarity l) (nnf_with polarity r)
  | Fnot g => nnf_with (!polarity) g

theorem nnfw_eval (polarity : Bool) (f : Formula) (e : Valuation) :
    eval (nnf_with polarity f) e = ((eval f e) = polarity) := by
  match f with
  | Fvar s => grind [nnf_with, negEval]
  | Fconst b => grind [nnf_with, eval]
  | Fand l r =>
    let IH_l := nnfw_eval polarity l e
    let IH_l := nnfw_eval polarity r e
    grind [nnf_with, eval]
  | For l r =>
    let IH_l := nnfw_eval polarity l e
    let IH_l := nnfw_eval polarity r e
    grind [nnf_with, eval]
  | Fnot g =>
    let IH_g := nnfw_eval (!polarity) g e
    grind [nnf_with, eval]

def nnf (f : Formula) : Formula := nnf_with true f

theorem nnf_eval (f : Formula) (e : Valuation) :
    eval (nnf f) e = eval f e := by
  grind [nnfw_eval, nnf]

-- Substitution

def subst (f : Formula) (x : Variable) (t : Formula) : Formula :=
  match f with
  | Fvar s => if s = x then t else f
  | Fconst _ => f
  | Fand l r => Fand (subst l x t) (subst r x t)
  | For  l r => For (subst l x t) (subst r x t)
  | Fnot g   => Fnot (subst g x t)

def updatedV (e : Valuation) (x : Variable) (b : Bool) : Valuation :=
  fun (v : Variable) => if v = x then b else e v

theorem subst_lemma (f : Formula) (x : Variable) (t : Formula) (e : Valuation) :
    eval (subst f x t) e =
    eval f (updatedV e x (eval t e)) := by
  match f with
  | Fvar s => grind [subst, updatedV, eval]
  | Fconst _ => grind [subst, updatedV, eval]
  | Fand l r =>
    let IH_l := subst_lemma l x t e
    let IH_r := subst_lemma r x t e
    grind [subst, updatedV, eval]
  | For  l r =>
    let IH_l := subst_lemma l x t e
    let IH_r := subst_lemma r x t e
    grind [subst, updatedV, eval]
  | Fnot g =>
    let IH_g := subst_lemma g x t e
    grind [subst, updatedV, eval]
