# Lab 1: Introduction to stainless

This first lab is comprised of two parts. In the first, you will use stainless to verify a short scala file. In the second, you will implement some simple but useful functions on propositional formulas

## Installation

### Java Development Kit
In order to run stainless, as well as the programs you will verify, you will need **Java 17**.

You can check your Java version using 
```shell
$ java -version
openjdk version "17.0.9" 2023-10-17
OpenJDK Runtime Environment Temurin-17.0.9+9 (build 17.0.9+9)
OpenJDK 64-Bit Server VM Temurin-17.0.9+9 (build 17.0.9+9, mixed mode, sharing)
$ javac -version
javac 17.0.9
```
The exact version might vary, but the major version should be 17.

On some Linux distributions, a command exists to change your "active" JDK if multiple ones are installed on your machine. Examples include:
- **Debian-based:** `update-alternatives --config java`, and
- **[ArchLinux-based](https://wiki.archlinux.org/title/Java#Switching_between_JVM):** `archlinux-java set java-17-openjdk`.

### Scala
You will also need a way to compile Scala programs.
We will provide files meant to be used with [scala-cli](https://scala-cli.virtuslab.org/install/) while working with Stainless, but you will also need [sbt](https://www.scala-sbt.org/) when working with [LISA](https://github.com/epfl-lara/LISA) in future labs.

As such, we recommend following the standard [Scala install
instructions](https://docs.scala-lang.org/getting-started/install-scala.html) to
obtain `scala`, `scala-cli`, and `sbt`.

If you use Coursier, you will need to add its `bin` directory to your path. The
`bin` directory's path (typically `~/.local/share/coursier/bin` on Linux) will
be printed in the terminal near the end of the installation.

You can test your installation with
```shell
$ scala -version
Scala code runner version: 1.8.4
Scala version (default): 3.7.2
$ scala-cli -version
Scala CLI version: 1.8.0
Scala version (default): 3.7.0
$ sbt -version
sbt runner version: 1.11.6
```

### Stainless

Follow the instructions on the [Stainless installation
page](https://epfl-lara.github.io/stainless/installation.html) to install
Stainless. Installing an additional solver such as z3 or cvc5 is highly
recommended (instructions on same page).

You should be able to run Stainless to get the following output
```shell
$ stainless --version
[  Info  ] Stainless verification tool (https://github.com/epfl-lara/stainless)
[  Info  ]   Version: 0.9.9.1-1-gb59c746
[  Info  ]   Built at: 2025-09-11 11:55:08.469+0200
[  Info  ]   Stainless Scala version: 3.7.2
[  Info  ] Inox solver (https://github.com/epfl-lara/inox)
[  Info  ] Version: 1.1.5-212-gb2b836a
[  Info  ] Bundled Scala compiler: 3.7.2
```

## Getting the source

To start working on this lab, you can either clone this entire repository, or download the present directory alone from Gitlab (there should be a button for this on the top right of the web interface).

## Lab, part 0: Tutorial

A basic tutorial on Stainless can be found [here](https://epfl-lara.github.io/stainless/tutorial.html). Follow the tutorial to fill in the file `src/Tutorial.scala` in this directory. You can check your work by running

```shell
$ stainless src/Tutorial.scala
```

Implement the functions and lemmas `max`, `max_lemma`, `size`, `isize`,
`isSorted`, `content`, and `sInsert` as in the tutorial.

Finally, add an additional post-condition to your `sInsert` function:

```scala
  (!content(l).contains(x) || res == l) &&
  (content(l).contains(x) || size(res) == size(l) + 1)
```

and verify that Stainless can prove it, adding additional assertions and lemmas
if necessary.

### On Induction

To have a bit more intuition for how to do induction proofs, consider the following arithmetic example that verifies in Stainless:

```scala
def sumTo(n: BigInt): BigInt =
  require(0 <= n)
  if n == 0 then BigInt(0)
  else n + sumTo(n-1)    

def sumToIsCorrect(n: BigInt): Unit = {
  require(0 <= n)
  if n == 0 then ()
  else sumToIsCorrect(n-1)
} ensuring { _ => sumTo(n) == n*(n+1)/2 }
```

Whereas we could have modified `sumTo` to state the post-condition `res ==
n*(n+1)/2`, here we decided to leave `sumTo` as is. To ensure that Stainless
proves property by induction, we repeat the recursive structure of `sumTo`
inside the body of `sumToIsCorrect`. The result is the same induction schema as
if we added a post-condition to `sumTo`. Simple cases of such induction can be
done automatically by Stainless (or enforced using the `@induct` annotation),
but writing explicitly induction schemas as we did here is more general. 

These induction proofs work due to [function unfolding performed by
Stainless](https://mediaspace.epfl.ch/media/01-04%2C+Unfolding+recursive+functions+in+Stainless/0_4byxmv9i/30542).
Several other examples of inductive proofs can be found in the Stainless list
library in the file
[ListSpecs.scala](https://github.com/epfl-lara/stainless/blob/main/frontends/library/stainless/collection/ListSpecs.scala). One of the functions `appendIndex` is reproduced here as an example:

```scala
import stainless.collection.*
object TestAppendIndex:
  /**
   * Proves the validity of indices when appending two lists.
   */
  def appendIndex[T](l1: List[T], l2: List[T], i: BigInt): Boolean = {
    require(0 <= i && i < l1.size + l2.size)
    l1 match
      case Nil() => true
      case Cons(x,xs) =>
        (i == 0) || appendIndex(xs, l2, i - 1)
  }.ensuring: _ =>
    (l1 ++ l2).apply(i) == (if i < l1.size then l1(i) else l2(i - l1.size))

```

### Additional Resources

For additional help and practice, some older videos can be found on the
[repository](https://github.com/epfl-lara/stainless/#further-documentation-and-learning-materials).
In particular, there are illustrative examples in the
[bolts/tutorials](https://github.com/epfl-lara/bolts/tree/master/tutorials)
directory including those from the [FMCAD 2021
tutorial](https://github.com/epfl-lara/fmcad2021tutorial). The beginning of
[ASPLOS 2022 tutorial](https://epfl-lara.github.io/asplos2022tutorial/) may be
of interest as well. Some of the [verified examples in Stainless source
directory](https://github.com/epfl-lara/stainless/blob/main/frontends/benchmarks/verification/valid/)
are interesting, such as
[BalancedParentheses.scala](https://github.com/epfl-lara/stainless/blob/main/frontends/benchmarks/verification/valid/BalancedParentheses.scala),
[associative
list](https://github.com/epfl-lara/stainless/blob/main/frontends/benchmarks/verification/valid/AssociativeList.scala).

## Lab, part 1: sublists

### The `sublist` relation

The file [`Sublist.scala`](src/Sublist.scala) defines a relation `sublist` on
lists, also noted $`\sqsubseteq`$, which holds when all the elements of the
first list appear in the second *in the same order*. Some examples and non-examples:
```math
\begin{align*}
    \langle0,2\rangle &\sqsubseteq \langle0,1,2\rangle \\
    \langle0,0,2\rangle &\not\sqsubseteq \langle0, 2\rangle\\
    \langle1,0\rangle &\not\sqsubseteq \langle0,0,1\rangle \\
    \langle10,5,25\rangle &\sqsubseteq \langle70,10,11,8,5,25,22\rangle
\end{align*}
```
![sublist example](SublistExample.png)

The file includes a main function which checks the examples above
```shell
$ scala-cli run Sublist.scala
<0,2> ⊑ <0,1,2> = true
<0,0,2> ⊑ <0,2> = false
<1,0> ⊑ <0,0,1> = false
<10,5,25> ⊑ <70,10,11,8,5,25,22> = true
<25,11,53,38> ⊑ <15,25,11,8,53,22,38> = true
```

### Goal of the lab

The `List` data-structure as well as the `sublist` relation are already
implemented; your job is now to prove some properties on the latter, such as
reflexivity, transitivity, and anti-symmetry. These properties are stated as
lemmas in the form of functions which "do nothing": they return `Unit` and have
no effects. You have to fill these functions with a proof of their specification.

As an example, the first property to prove is reflexivity. It is stated as follows 
```scala
def reflexivity[T](l: List[T]): Unit = {
  /* TODO: Prove me */
}.ensuring(_ =>
  sublist(l, l)
)
```
which should be understood mathematically as
```math
\forall l,\  l \sqsubseteq l
```
Another example: transitivity
```scala
def transitivity[T](l1: List[T], l2: List[T], l3: List[T]): Unit = {
  require(sublist(l1, l2) && sublist(l2, l3))
  /* TODO: Prove me */
}.ensuring(_ =>
  sublist(l1, l3)
)
```
which should be interpreted as
```math
\forall l_1\, l_2\, l_3,\ l_1 \sqsubseteq l_2 \land l_2 \sqsubseteq l_3 \implies l_1 \sqsubseteq l_3
```

The file contains eleven properties on `sublist` that you have to prove.

To check your proofs, use
```shell
$ stainless src/Sublist.scala
``` 


The provided configuration file ([stainless.conf](stainless.conf)) will automatically set the SMT solver's timeout to 2 seconds.
You can override this while experimenting with your proofs by either changing the configuration file or using the command line, by adding e.g. `--timeout=5` to set the timeout to 5 seconds.
You can also add `--watch` for stainless to automatically run on file save:
```shell
$ stainless --timeout=5 --watch src/Sublist.scala
```

You are not allowed to change the definition of `sublist` or the statement (parameters/return types, function name, requirements, conclusion, etc.) for any of the properties.
<!-- The only exception to this rule is the `@induct` annotation, which you are allowed to add to any parameter if you so choose. -->

Some advice:
- Try to understand how you would prove these properties with paper and pencil, and use examples to gain intuition;
- Induction is the main proof method in many cases; see the above example and instructions on induction;
- Prove lemmas in order: earlier lemmas (and their structure) will help you with subsequent lemmas;
- Even though it is not necessary, you can define new lemmas if it helps (but you then have to prove them correct as well 😊);
- Regarding the four lemmas about concatenation: two very similar lemmas can have vastly different proofs (in both size and difficulty – can you tell why?).

## Lab, part 2: Boolean Algebras
In the second part, you have to implement 9 functions on propositional formulas.
In the [`BooleanAlgebra.scala`](src/BooleanAlgebra.scala) file, you will find
first the definition of `Formula` as an Algebraic Data Type (ADT): a
Boolean `Formula` is either
- A variable `Var(id)`, identified by a unique integer,
- A conjunction `And(left, right)`, where `left` and `right` are formulas,
- A disjunction `Or(left, right)`, where `left` and `right` are formulas,
- A implication `Implies(left, right)`, where `left` and `right` are formulas,
- A negation `Not(formula)`, where `formula` is a formula,
- The constant`True`, or
- The constant `False`.

You have to implement the functions marked by `???` in the file. To help you understand what the function should do, you can find examples of input-output pairs in the `test/Tests.scala` file.
- `eval` evaluates a formula under a given assignment of Boolean values to variables.
- `substitute` replaces occurrences of variables in a formula by corresponding formulas.
- `nnf` transforms a formula into its [Negation Normal Form](https://en.wikipedia.org/wiki/Negation_normal_form).
- `variables` returns the set of variables appearing in a given formula.
- `validity` checks if a formula is valid, i.e., if it is true under all
  possible assignments of Boolean values to variables.

Second, you then find the definition of formulas represented as [And-Inverter
Graphs](https://en.wikipedia.org/wiki/And-inverter_graph) (AIGs). It so happens
that this format, although more restricted, is complete, i.e. it can represent
any formula. You have to implement the following functions:
- `AIG_eval`, `AIG_variables` and `AIG_validity` are similar to their counterparts for formulas, but for AIGs.
- `formulaToAIG` converts a formula in the usual representation to an AIG
  formula. Note that there may be multiple equivalent ways to do this: it is
  only required that the input and output formulas are equivalent, i.e. that
  under any assignment of Boolean values to variables, the two formulas evaluate
  to the same value.

Again, don't forget to look at the tests in `test/Tests.scala` to see examples of how your implementation should behave.

You can run the tests with
```shell
$ scala-cli test .
```
When all tests pass successfully, you are done!

## Submission

You need to pick your groups (min 2, max 3) for the labs and projects for the
semester on
[Moodle](https://moodle.epfl.ch/mod/choicegroup/view.php?id=1342473). The lab
submission must be made as a group by exactly one of the members.

Once you've picked a group and completed all proofs, you can submit your
[Tutorial.scala](src/Tutorial.scala), [Sublist.scala](src/Sublist.scala), and
[BooleanAlgebra.scala](src/BooleanAlgebra.scala) files on
[Moodle](https://moodle.epfl.ch/mod/assign/view.php?id=1092878).

## Troubleshooting
- If, when running `scala-cli test .` you obtain an error of the form `Error: bloop.rifle.FailedToStartServerExitCodeException: Server failed with exit code 1`, try to run the command:
```shell
$ eval "$(cs java --env --jvm temurin:17)"
```
