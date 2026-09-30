-- Computation on natural numbers, Nat
-- One line comments start with --, multiple are within /- ... -/

#eval 100 + 3*7
#eval 100 / 3
#eval 100 % 3

def sum_of_squares (a b : Nat) : Nat :=
  a * a + b * b

#eval sum_of_squares 3 4

-- Functional programming: define lists and functions on them

inductive MyList (A: Type): Type where
  | nil : MyList A
  | cons (head : A) (tail : MyList A) : MyList A

open MyList

#check MyList.cons

-- putting a parameter in {...} makes Lean infer it when function is called
def len {A: Type} (lst: MyList A): Nat :=
  match lst with               -- match defines value by cases
  | .nil => 0                  -- base case
  | .cons hd tl => 1 + len tl  -- the recursive case

#eval len (cons (5 : Nat) (cons 2 (cons 100 nil)))

-- elements of `xs` followed by elements of `ys`
def append {A: Type} (xs ys : MyList A): MyList A :=
  match xs with
  | .nil => ys
  | .cons h t => cons h (append t ys)

#eval append (cons 1 (cons 2 nil)) (cons 50 (cons 60 (cons 70 nil)))

-- a theorem, named `appendMorph`. length of append is the sum of lengths
theorem appendMorph {A: Type} (xs ys : MyList A):  -- holds for arbitrary A,xs,ys
  len (append xs ys) = len xs + len ys := by -- theorem statement as an equality
  -- now comes the proof
  match xs with                  -- by induction, looks like recursive function def
  | .nil => simp [append, len]   -- base case
  | .cons h t =>                 -- inductive case
     let IH : (len (append t ys) = len t + len ys) := appendMorph t ys
     grind [append, len]         -- grind: a prover for equalities and linear arithmetic
     -- grind will use IH automatically but needs to be told to use function definitions

-- Higher order functions

def compose {A B C : Type} (f : A → B) (g : B → C) : A → C :=
  fun (a : A) =>
    g (f a)

-- Higher-order function map on lists

def map {A B : Type} (f: A → B) (lst: MyList A) : MyList B :=
  match lst with
  | .nil  => .nil
  | .cons hd tl => cons (f hd) (map f tl)

theorem mapComp {A B C : Type} (f : A → B) (g : B → C) (lst : MyList A) :
    (map g (map f lst)) = map (fun a => g (f a)) lst := by
  match lst with
  | .nil => simp [map]
  | .cons hd tl =>
    let IH: (map g (map f tl)) = map (fun a => g (f a)) tl  := mapComp f g tl
    grind [map]
