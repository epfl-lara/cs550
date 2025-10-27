
import lisa.maths.SetTheory.Base.Predef.{*, given}
import lisa.maths.SetTheory.Functions.Predef.{*, given}
import lisa.maths.SetTheory.Base.Pair.{pair, given}
import lisa.maths.SetTheory.Functions.BasicTheorems.{appTyping}
import lisa.automation.Substitution.Apply as Substitution

import Base.{IBinFun, IRel, infixBinaryFunction}

object Lattices extends lisa.Main:

  private val x = variable[Ind]
  private val y = variable[Ind]
  private val z = variable[Ind]

  private val L = variable[Ind] // lattice base set
  
  // We introduce the signature of lattices

  // leq order
  // <= ⊆ L × L
  object <= extends Variable[Ind]("<="):
    val leq = this // just an alias, as `this` cannot be used in patterns

    /**
      * Constructs an expression representing a <= b
      */
    def construct(a: Expr[Ind], b: Expr[Ind]): Expr[Prop] = IRel(leq, a, b)
    
    /**
      * allow pattern matching on expressions of the form a <= b
      * as `case a <= b => ...`
      */
    def unapply(e: Expr[Prop]): Option[(Expr[Ind], Expr[Ind])] = 
      e match
        case IRel(`leq`, a, b) => Some((a, b))
        case _ => None

  // join symbol: Union for Sets, Disjunction for boolean algebras
  // u ∈ L × L → L
  object u extends Variable[Ind]("u"):
    val join = this

    def construct(a: Expr[Ind], b: Expr[Ind]): Expr[Ind] = IBinFun(join, a, b)

    def unapply(e: Expr[Ind]): Option[(Expr[Ind], Expr[Ind])] = 
      e match
        case IBinFun(`join`, a, b) => Some((a, b))
        case _ => None
  
  // meet symbol: Intersection for Sets, Conjunction for boolean algebras
  // n ∈ L × L → L
  object n extends Variable[Ind]("n"):
    val meet = this

    def construct(a: Expr[Ind], b: Expr[Ind]): Expr[Ind] = IBinFun(meet, a, b)

    def unapply(e: Expr[Ind]): Option[(Expr[Ind], Expr[Ind])] = 
      e match
        case IBinFun(`meet`, a, b) => Some((a, b))
        case _ => None
  
  // Enables infix notation
  extension (left: Expr[Ind]) {
    infix def <=(right : Expr[Ind]): Expr[Prop]  = Lattices.<=.construct(left, right)
    infix def  u(right : Expr[Ind]): Expr[Ind]   = Lattices.u.construct(left, right)
    infix def  n(right : Expr[Ind]): Expr[Ind]   = Lattices.n.construct(left, right)
  }

  // the definition of a lattice with the signature as above
  // the properties are listed here in full for reference
  object lattice:
    // wrap the curried definition for convenience
    protected val lattice = DEF( λ(L, λ(<=, λ(u, λ(n, 
      <= ⊆ (L × L) /\
      (u :: (L × L -> L)) /\
      (n :: (L × L -> L)) /\
      // partial order
        ∀(x, (x ∈ L) ==> x <= x) /\ // reflexivity
        ∀(x, ∀(y, ((x ∈ L) /\ (y ∈ L) /\ (x <= y) /\ (y <= x)) ==> (x === y))) /\ // antisymmetry
        ∀(x, ∀(y, ∀(z, ((x ∈ L) /\ (y ∈ L) /\ (z ∈ L) /\ (x <= y) /\ (y <= z)) ==> (x <= z)))) /\ // transitivity
      // least upper bound
        ∀(x, ∀(y, ∀(z, ((x ∈ L) /\ (y ∈ L) /\ (z ∈ L)) ==> (((x <= z) /\ (y <= z)) <=> ((x u y) <= z))))) /\
      // greatest lower bound
        ∀(x, ∀(y, ∀(z, ((x ∈ L) /\ (y ∈ L) /\ (z ∈ L)) ==> (((z <= x) /\ (z <= y)) <=> (z <= (x n y))))))
    ))))).printAs(args => s"lattice(${args.mkString(", ")})")

    inline def apply(L: Expr[Ind], leq: Expr[Ind], u: Expr[Ind], n: Expr[Ind]): Expr[Prop] = lattice(L)(leq)(u)(n)

    inline def definition = lattice.definition

  // we can instantiate and extract each of the properties for easier reuse

  val reflexivity = Theorem(
    (lattice(L, <=, u, n), x ∈ L) |- x <= x
  ):
    assume(lattice(L, <=, u, n))

    have(∀(x, (x ∈ L) ==> x <= x)) by Tautology.from(lattice.definition)
    have(thesis) by Restate.from(lastStep of (x))

  val antisymmetry = Theorem(
    (lattice(L, <=, u, n), x ∈ L, y ∈ L, x <= y, y <= x) |- x === y
  ):
    assume(lattice(L, <=, u, n))

    have(∀(x, ∀(y, ((x ∈ L) /\ (y ∈ L) /\ (x <= y) /\ (y <= x)) ==> (x === y)))) by Tautology.from(lattice.definition)
    have(thesis) by Restate.from(lastStep of (x, y))

  val transitivity = Theorem(
    (lattice(L, <=, u, n), x ∈ L, y ∈ L, z ∈ L, x <= y, y <= z) |- x <= z
  ):
    assume(lattice(L, <=, u, n))

    have(∀(x, ∀(y, ∀(z, ((x ∈ L) /\ (y ∈ L) /\ (z ∈ L) /\ (x <= y) /\ (y <= z)) ==> (x <= z)))) ) by Tautology.from(lattice.definition)
    have(thesis) by Restate.from(lastStep of (x, y, z))

  val lub = Theorem(
    (lattice(L, <=, u, n), x ∈ L, y ∈ L, z ∈ L) |- ((x <= z) /\ (y <= z)) <=> ((x u y) <= z)
  ):
    assume(lattice(L, <=, u, n))

    have(∀(x, ∀(y, ∀(z, ((x ∈ L) /\ (y ∈ L) /\ (z ∈ L)) ==> (((x <= z) /\ (y <= z)) <=> ((x u y) <= z))))) ) by Tautology.from(lattice.definition)
    have(thesis) by Restate.from(lastStep of (x, y, z))

  val glb = Theorem(
    (lattice(L, <=, u, n), x ∈ L, y ∈ L, z ∈ L) |- ((z <= x) /\ (z <= y)) <=> (z <= (x n y))
  ):
    assume(lattice(L, <=, u, n))

    have(∀(x, ∀(y, ∀(z, ((x ∈ L) /\ (y ∈ L) /\ (z ∈ L)) ==> (((z <= x) /\ (z <= y)) <=> (z <= (x n y)))))) ) by Tautology.from(lattice.definition)
    have(thesis) by Restate.from(lastStep of (x, y, z))

  val meetRange = Theorem(
    (lattice(L, <=, u, n), x ∈ L, y ∈ L) |- (x n y) ∈ L
  ):
    // follows from the fact that n :: (L × L → L)
    assume(lattice(L, <=, u, n), x ∈ L, y ∈ L)

    have(app(n)((x, y)) ∈ L) by Tautology.from(
      lattice.definition, 
      appTyping of (f := n, x := (x, y), A := L × L, B := L), 
      CartesianProduct.membershipSufficientCondition of (A := L, B := L)
    )

    thenHave(thesis) by Substitution(infixBinaryFunction.definition of (f := n))

  val joinRange = Theorem(
    (lattice(L, <=, u, n), x ∈ L, y ∈ L) |- (x u y) ∈ L
  ):
    // follows from the fact that u :: (L × L → L)
    assume(lattice(L, <=, u, n), x ∈ L, y ∈ L)

    have(app(u)((x, y)) ∈ L) by Tautology.from(
      lattice.definition, 
      infixBinaryFunction.definition of (f := u), 
      appTyping of (f := u, x := (x, y), A := L × L, B := L), 
      CartesianProduct.membershipSufficientCondition of (A := L, B := L)
    )

    thenHave(thesis) by Substitution(infixBinaryFunction.definition of (f := u))

  // Now we'll need to reason with equality. We can do so with the Substitution
  // tactic, which substitutes equals for equals. The argument of Substitution
  // can be either an equality (s === t). In this case, the result should
  // contain (s === t) in assumptions. Or, it can be a previously proven step
  // showing a formula of the form (s === t). In this case, assumptions of this
  // precedently proven fact need to be in the assumptions of the conclusion.
  
  // Note, however, that Restate and Tautology know by themselves that t === t,
  // for any t.
  
  val joinLowerBound = Theorem((lattice(L, <=, u, n), x ∈ L, y ∈ L) |- (x <= (x u y)) /\ (y <= (x u y))):
    have (thesis) by Tautology.from(lub of (z := (x u y)), reflexivity of (x := (x u y)), joinRange)
  
  val joinCommutative = Theorem((lattice(L, <=, u, n), x ∈ L, y ∈ L) |- (x u y) === (y u x)):
    assume(lattice(L, <=, u, n), x ∈ L, y ∈ L)

    // to apply the lattice theorems, we need to know that the elements we use are actually in L
    val typing = have((x u y) ∈ L /\ (y u x) ∈ L) by Tautology.from(joinRange, joinRange of (x := y, y := x))

    val s1 = have ((x u y) <= (y u x)) by Tautology.from(lub of (z := (y u x)), joinLowerBound of (x := y, y := x), typing)
    have (thesis) by Tautology.from(s1, s1 of (x := y, y := x), antisymmetry of (x := x u y, y := y u x), typing)
  
  val joinAbsorption = Theorem((lattice(L, <=, u, n), x ∈ L, y ∈ L, x <= y) |- (x u y) === y):
    // TODO: prove me!
    sorry
  
  val meetUpperBound = Theorem((lattice(L, <=, u, n), x ∈ L, y ∈ L) |- ((x n y) <= x) /\ ((x n y) <= y)):
    // TODO: prove me!
    sorry
  
  val meetCommutative = Theorem((lattice(L, <=, u, n), x ∈ L, y ∈ L) |- (x n y) === (y n x)):
    // TODO: prove me!
    sorry
  
  val meetAbsorption = Theorem((lattice(L, <=, u, n), x ∈ L, y ∈ L, x <= y) |- (x n y) === x):
    // TODO: prove me!
    sorry
  
  val joinAssociative = Theorem((lattice(L, <=, u, n), x ∈ L, y ∈ L, z ∈ L) |- (x u (y u z)) === ((x u y) u z)):
    assume(lattice(L, <=, u, n), x ∈ L, y ∈ L, z ∈ L)

    // use `typing` to apply lattice properties on any of these expressions
    val typing = have((x u y) ∈ L /\ (y u z) ∈ L /\ (x u (y u z)) ∈ L /\ ((x u y) u z) ∈ L) by Tautology.from(
      joinRange of (x := x, y := y),
      joinRange of (x := y, y := z),
      joinRange of (x := x, y := (y u z)),
      joinRange of (x := (x u y), y := z)
    )

    // TODO: prove me!
    sorry
  
  // Tedious, isn't it
  // Can we automate this?
  // Yes, we can!
  
  import lisa.utils.prooflib.ProofTacticLib.ProofTactic
  import lisa.utils.prooflib.Library

  object Whitman extends ProofTactic:
    import scala.collection.immutable.{Set => S}
    
    /**
     * Collect all lattice subexpressions of the given term.
     */
    private def collectSubexpressions(e: Expr[Ind]): S[Expr[Ind]] = e match
      case u(a, b) => collectSubexpressions(a) ++ collectSubexpressions(b) + e
      case n(a, b) => collectSubexpressions(a) ++ collectSubexpressions(b) + e
      case _       => S(e)

    /**
     * Solve the given inequality goal using Whitman's algorithm, and eliminate
     * unnecessary typing assumptions.
     */
    def apply(using lib: library.type, proof: lib.Proof)(goal: Sequent): proof.ProofTacticJudgement =
      if goal.right.size != 1 then
        proof.InvalidProofTactic("Whitman can only be applied to solve lattice tautologies of the form ... |- s <= t or ... |- s === t")
      else
        // extract the statement to prove
        val statement: Expr[Prop] = goal.right.head
        TacticSubproof:
          assume(lattice(L, <=, u, n))

          // attempt to solve the statement, while accumulating typing assumptions
          val sol = solve(statement)

          if !sol.isValid then
            return proof.InvalidProofTactic(s"The goal statement $statement is not a lattice tautology.")
          else
            // if the inner solve succeeded, the conclusion should be derivable
            // from the typing properties and the result of solve 
            val (a, b) = 
              statement match
                case left <= right => (left, right)
                case left `equality` right => (left, right)
                case _ => return proof.InvalidProofTactic(s"Unexpected goal statement form after solve succeeded: $statement.")

            val subexprs = collectSubexpressions(a) ++ collectSubexpressions(b)
            val typingAssumptions = subexprs.map: 
              case u(left, right) => joinRange of (x := left, y := right)
              case n(left, right) => meetRange of (x := left, y := right)
              case expr           => have(expr ∈ L |- expr ∈ L) by Restate
            
            have(goal) by Tautology.from((typingAssumptions + have(sol)).toSeq*)

    /**
     * Given a lattice and elements a, b, attempt to prove 
     *
     * lattice(L, <=, u, n), <typing info> |- a <= b
     *
     * where <typing info> says that a, b, and any and *all* subexpressions are
     * in L. This is an invariant of the function that must be preserved
     * recursively. 
     *
     * e.g. given x, (x u y), it should prove
     *
     * lattice(L, <=, u, n), x ∈ L, y ∈ L, (x u y) ∈ L |- x <= (x u y)
     */
    private def solve(using lib: library.type, proof: lib.Proof)(goal: Expr[Prop]): proof.ProofTacticJudgement =
      TacticSubproof {
        goal match
          case left <= right => {
            // We have different cases to consider
            (left, right) match {
              // 1. left is a join. In that case, lub gives us the decomposition
              case (a `u` b, _) =>
                // solve a <= right and b <= right recursively
                val s1 = solve(a <= right)
                val s2 = solve(b <= right)
                // check if the recursive calls succedded
                if s1.isValid && s2.isValid then
                  // add one typing assumption: a u b ∈ L
                  // then conclude using lub
                  val h1 = have(s1)
                  val h2 = have(s2)

                  // need to keep the assumptions accumulated recursively!
                  // `++<< s` adds the assumptions of the proof step `s` to the current statement
                  val statement = 
                    val newStatement = ((a u b) ∈ L |- left <= right) 
                    newStatement ++<< h1.bot ++<< h2.bot

                  have (statement) by Tautology.from(lub of (x := a, y := b, z := right), h1, h2)
                else
                  return proof.InvalidProofTactic("The inequality is not true in general")

              // 2. right is a meet. In that case, glb gives us the decomposition
              case (_, a `n` b) =>
                // TODO: implement me!
                ???
              // 3. left is a meet, right is a join. In that case, we try all pairs.
              case (a `n` b, c `u` d) =>
                // TODO: implement me!
                ???
              // 4. left is a meet, right is a variable or unknown Expr[Ind].
              case (a `n` b, _) =>
                // TODO: implement me!
                ???


              // 5. left is a variable or unknown Expr[Ind], right is a join.
              case (_, c `u` d) =>
                // TODO: implement me!
                ???

              // 6. left and right are variables or unknown Expr[Ind]s.
              case _ =>
                // TODO: implement me!
                ???
            }
          }

          case left `equality` right =>
            // TODO: implement me!
            ???
          case _ => 
            // TODO: implement me!
            ???
      }
  end Whitman
  
//   // uncomment when the tactic is implemented
  
  val test1 = Theorem(lattice(L, <=, u, n) /\ x ∈ L |- x <= x):
    sorry // have(thesis) by Whitman.apply

  val test2 = Theorem(lattice(L, <=, u, n) /\ x ∈ L /\ y ∈ L |- x <= (x u y)):
    sorry // have(thesis) by Whitman.apply
  
  val test3 = Theorem(lattice(L, <=, u, n) /\ x ∈ L /\ y ∈ L |- (y n x) <= x):
    sorry // have(thesis) by Whitman.apply
  
  val test4 = Theorem(lattice(L, <=, u, n) /\ x ∈ L /\ y ∈ L /\ z ∈ L |- (x n y) <= (y u z)):
    sorry // have(thesis) by Whitman.apply

  val idempotence = Theorem(lattice(L, <=, u, n) /\ x ∈ L |- (x u x) === x):
    sorry // have(thesis) by Whitman.apply

  val meetAssociative = Theorem(lattice(L, <=, u, n) /\ x ∈ L /\ y ∈ L /\ z ∈ L |- (x n (y n z)) === ((x n y) n z)):
    sorry // have(thesis) by Whitman.apply

  val semiDistributivity = Theorem(lattice(L, <=, u, n) /\ x ∈ L /\ y ∈ L /\ z ∈ L |- (x u (y n z)) <= ((x u y) n (x u z))):
    sorry // have(thesis) by Whitman.apply

end Lattices
