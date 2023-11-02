# Lab 5: Lattices and Automation in LISA

### The Lab
In this Lab, you will use LISA to show some properties of lattices, and implement a proof tactic for lattices corresponding to Whitman's algorithm.
As in the previous lab, mke sure to clone (or fetch) the LISA repository, then do
```
git switch Lab05
```
start sbt with
```
sbt
```
and finally within sbt's environment,
```
project fv-lab05
```

If you now hit `run`, you'll see 2 theorems printed in green and a bunch in yellow, corresponding to theorems in the file.

```
fv-lab04/src/main/scala/Lab04.scala
``` 
You first have to prove 5 theorems manually: joinAbsorption, meetUpperBound, meetCommutative, meetAbsorption, joinAssociative. Those theorems don't require quantifier reasoning: It is generally sufficient to find the good instantiation parameters for axioms, and then apply propositional reasoning.

This can however be somewhat tedious to figure out. It so happens that there exists an algorithm that can autmatically decide any equality and equality between two lattice terms, called Whitman's Algorithm. The algorithm works recursively. Here it is as a decision procedure (i.e. it does not produce a proof, as needed in LISA)

```scala
def solveLEQ(s, t):Boolean =
    (s, t) match
        case (a u b, _) => solveLEQ(a, t) && solveLEQ(b, t)
        case (_, c n d) => solveLEQ(s, c) && solveLEQ(s, d)
        case (a n b, c u d) => solveLEQ(a, t) || solveLEQ(b, t) || solveLEQ(s, c) || solveLEQ(s, d)
        case (a n b, x) => solveLEQ(a, x) || solveLEQ(b, x)  // when x is a literal, i.e. neither a meet nor a join
        case (x, c u d) => solveLEQ(x, c) || solveLEQ(x, d)
        case (x, y) => x == y // when x and y are literals
```
It is not difficult to see that this algrithm is sound, meaning it will never accepts an inequality that is not always true. Try to see which properties of lattices justify there different case.
This algorithm is also complete: It will accept every statement that is true in all lattice. However, showing this is trickier and requires defining the notion of free lattices.

Implemented like this, the algorithm is exponential. However observe that throughout execution, `solveLEQ` will only receive as arguments subterms of the two original arguments: In particular, it is necessary to evaluate it on only at most $\mathcal O(n^2)$ arguments. With memoization, we would obtain a quadratic algorithm, but we won't do that in today's Lab for simplicity.

Now, implement a proof-producing variant of Whitman's algorithm, using the skeleton provided in Lab04.scala. Cases 1 and 4 are already implemented to show you the syntax. In particular:
 - To abort a proof because no correct proof can be produced, use `fail`.
 - It is possible to test if a tactic succeeds by simply calling the tactic without the `have` keyword. Indeed, a tactic is a function that returns a `ProofTacticJudgement`, which can be either `Valid` if the tactic succeeded or `Invalid` otherwise. On can then test that with `isValid` as shown in the cases 1 and 4 given as examples.

When you've finished, upload your file Lab04.scala on [moodle](https://moodle.epfl.ch/mod/assign/view.php?id=1269436) (one submission per group).

### To Go Further

The algorithm can be generalised: In particular, there is a variant that can decide lattices inequality given any number of other inequalities assumed to be true: this would allow for example to show statements such as
$$
s_1\leq t_1, ... ,s_n \leq t_n \vdash s \leq t
$$
Moreover, it can take complementation into account, with properties such as 
$$
(x \cap \neg x) \leq y
$$
Both those extensions are described in https://infoscience.epfl.ch/record/305982?ln=en: Implementing such extension in LISA could be a suitable project, and so would be other proof-producing decision procedures!
