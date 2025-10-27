# Lab 5: Lattices and Automation in Lisa

In this lab, you will use Lisa to show some properties of lattices, and
implement a proof tactic for lattices corresponding to Whitman's algorithm.

To check the theorems in the file [Lattices.scala](./src/Lattices.scala), run 
```
scala-cli .
```

### Reasoning about lattices

First, prove five theorems about lattices manually: `joinAbsorption`,
`meetUpperBound`, `meetCommutative`, `meetAbsorption`, `joinAssociative`.

The required properties about the lattice operations are given in the file as
theorems proved from the definition.

While working on your proof, you can write `showCurrentProof()` anywhere in your
current proof to print the steps till that point to the terminal. Running
`scala-cli` in watch-mode provides quick feedback:

```console
scala-cli . --watch
```

For these theorems, it is generally sufficient to find the right instantiation
parameters for axioms, and then apply propositional reasoning. More complicated
quantifier reasoning is not necessary.

### Implementing a decision procedure

The right instantiations and applications of the lattice axioms can however be
somewhat tedious to figure out. It so happens that there exists an algorithm
that can automatically decide any equality and inequality between two lattice
terms, called Whitman's Algorithm. The algorithm works recursively. Here it is
as a decision procedure (i.e. it does not produce a proof, as needed in Lisa):

```scala
def solveLEQ(s, t):Boolean =
    (s, t) match
        case (a u b, _) => 
            solveLEQ(a, t) && solveLEQ(b, t)
        case (_, c n d) => 
            solveLEQ(s, c) && solveLEQ(s, d)
        case (a n b, c u d) => 
            solveLEQ(a, t) || solveLEQ(b, t) || solveLEQ(s, c) || solveLEQ(s, d)
        case (a n b, x) => 
            // when x is a literal, i.e. neither a meet, nor a join
            solveLEQ(a, x) || solveLEQ(b, x)  
        case (x, c u d) => 
            solveLEQ(x, c) || solveLEQ(x, d)
        case (x, y) =>
            // when both x and y are literals 
            x == y
```

It is not difficult to see that this algorithm is sound, i.e., that it will
never accept an inequality that is not always true (in every lattice). Try to
identify which properties of lattices justify each case above. 

Notably, this algorithm is also complete, i.e., it will accept every statement
that is true in all lattices. However, showing this is trickier and requires
defining the notion of free lattices (not required here).

Implemented like this, the algorithm is exponential. However observe that
throughout execution, `solveLEQ` will only receive as arguments pairs of
subterms of the two original arguments: in particular, it is necessary to
evaluate it on only at most $\mathcal O(n^2)$ arguments, with $n$ being the
maximum of the sizes of the two terms. With memoization, we would obtain a
quadratic algorithm, but we won't do that in today's lab for simplicity.

Now, implement a *proof-producing variant* of Whitman's algorithm, using the
skeleton provided in [Lattices.scala](./src/Lattices.scala). Case 1 is already
implemented as an illustration. 

In particular, note that is is possible to test if a tactic succeeds by simply
calling the tactic without the `have` keyword. Indeed, a tactic is a function
that returns a `ProofTacticJudgement`, which can be either `Valid` if the tactic
succeeded or `Invalid` otherwise, similar to an option type, or in particular
the proof result type you have seen in the resolution lab. As you can see, the
case provided uses this API to produce its own proof.

### Submission

When you've finished, upload your file [Lattices.scala](./src/Lattices.scala) on
[Moodle](https://moodle.epfl.ch/mod/assign/view.php?id=1269436) (one submission
per group).

### To Go Further

The algorithm can be generalised: In particular, there is a variant that can
decide lattice inequalities given any number of other inequalities assumed to be
true. This would allow, for example, showing statements such as

$$ s_1\leq t_1, ... ,s_n \leq t_n \vdash s \leq t $$

Moreover, it can take complementation into account, with properties such as 

$$ (x \sqcap \neg x) \leq y $$

Both of these extensions are described in [this linked
publication](https://infoscience.epfl.ch/record/305982?ln=en). Implementing such
an extension in Lisa could be a suitable project, and so would be other
proof-producing decision procedures!
