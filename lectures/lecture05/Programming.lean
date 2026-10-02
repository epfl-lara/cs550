-- Computation on natural numbers, Nat
-- One line comments start with --, multiple are within /- ... -/

#eval (100 : Nat) + 3*7
#eval 100 / 3
#eval 100 % 3
#eval 2^65
#eval 0 - 50

def sum_of_squares (a b : Nat) : Nat :=
  a * a + b * b

#eval sum_of_squares 3 4

-- Functional programming: define lists and functions on them

inductive MyList (A: Type): Type where
  | nil : MyList A
  | cons (head : A) (tail : MyList A) : MyList A

open MyList

-- putting a parameter in {...} makes Lean infer it when function is called
@[grind]
def len {A: Type} (lst: MyList A): Nat :=
  match lst with               -- match defines value by cases
  | .nil => (0 : Nat)          -- base case
  | .cons _ tl => 1 + len tl  -- the recursive case

#eval len (cons (5 : Nat) (cons 2 (cons 100 (cons 7 nil))))

-- elements of `xs` followed by elements of `ys`
def append {A: Type} (xs ys : MyList A): MyList A :=
  match xs with
  | .nil => ys
  | .cons h t => cons h (append t ys)

-- Scala: f(f(x, y),z)       Lean:  f (f x y) z

#eval len (append (cons 1 (cons 2 nil))
                  (cons 50 (cons 60 (cons 70 nil))))

-- a theorem, named `appendMorph`. length of append is the sum of lengths
theorem appendMorph {A: Type} (xs ys : MyList A):  -- holds for arbitrary A,xs,ys
  len (append xs ys) = len xs + len ys := by -- theorem statement as an equality
  -- now comes the proof
  match xs with                  -- by induction, looks like recursive function def
  | .nil => grind [append, len]   -- base case
  | .cons h t =>                 -- inductive case
     let IH : (len (append t ys) = len t + len ys) := appendMorph t ys
     grind [append, len]         -- grind: a prover for equalities and linear arithmetic
     -- grind will use IH automatically but needs to be told to use function definitions

-- Higher order functions

def compose {A B C : Type} (f : A → B) (g : B → C) : A → C :=
  fun (a : A) =>
    let b := f a
    g b

-- Higher-order function map on lists

@[grind]
def map {A B : Type} (f: A → B) (lst: MyList A) : MyList B :=
  match lst with
  | .nil  => .nil
  | .cons hd tl => cons (f hd) (map f tl)

theorem mapKeepsLength {A B : Type} (as : MyList A) (f : A → B) :
    len as = len (map f as) := by
  match as with
  | .nil => grind
  | .cons h t =>
    let IH := mapKeepsLength t f
    grind

theorem mapComp {A B C : Type} (f : A → B) (g : B → C) (lst : MyList A) :
    (map g (map f lst)) = map (fun a => g (f a)) lst := by
  match lst with
  | .nil => grind
  | .cons hd tl =>
    let IH: (map g (map f tl)) = map (fun a => g (f a)) tl  := mapComp f g tl
    grind

theorem ultimateTheorem: (0 : Nat) = 1 := sorry
#print axioms ultimateTheorem
