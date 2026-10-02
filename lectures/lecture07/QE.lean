
#eval - (2^65 : Int)

inductive Term (V : Type) : Type where
| var (v : V)
| const (c : Int)
| ctimes (c : Int) (t : Term V)
| add (left right : Term V)

inductive Formula (V : Type) : Type where
| equal (left right : Term V)
| less (left right : Term V)
| and (left right : Formula V)
| or (left right : Formula V)
| not (g : Formula V)
| al (v : V) (g : Formula V)
| ex (v : V) (g : Formula V)

abbrev Env V := V → Int

def updated {V : Type} [DecidableEq V] (e : Env V) (v : V) (k : Int) : Env V :=
  fun (x : V) => if x = v then k else e v

/-- meaning of the term `t` in the environment `e` -/
def term_eval {V : Type} (t : Term V) (e : Env V) : Int :=
match t with
| .var v => e v
| .const c => c
| .ctimes c t0 => c * term_eval t0 e
| .add t1 t2 => term_eval t1 e + term_eval t2 e

/-- meaning of the formula `f` in the environment `e` -/
def eval {V : Type} [DecidableEq V] (f : Formula V) (e : Env V) : Prop :=
match f with
| .equal l r => term_eval l e = term_eval r e
| .less l r => term_eval l e < term_eval r e
| .and f1 f2 => eval f1 e ∧ eval f2 e
| .or f1 f2 => eval f1 e ∨ eval f2 e
| .not f0 => ¬ eval f0 e
| .al v f0 => ∀ k : Int, eval f0 (updated e v k)
| .ex v f0 => ∃ k : Int, eval f0 (updated e v k)
