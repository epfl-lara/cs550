//> using jar "stainless-library_2.13-0.9.6.jar"

import stainless.lang.*
import stainless.collection.*
import stainless.annotation.*

object Resolution {

  // Additional operations on lists

  // Re-implementation of `List[T].unique`
  def unique[T](l: List[T]): List[T] = {
    l match {
      case Nil() => Nil()
      case Cons(h, t) => 
        if (t.contains(h)) {
          unique(t)
        }
        else {
          Cons(h, unique(t))
        }
    }
  }.ensuring(res => 
    res.content == l.content &&
    ListOps.noDuplicate(res)
  )

  // Avoid the ugly `foldLeft[(List[U], S)]` 
  // If you wish to write code along the lines of
  // var s = ...; ls.map( x => { <uses variable s> } )
  // this function might be useful to avoid mutation
  def statefulLeftMap[T, U, S](l: List[T], init: S, f: (T, S) => (U, S)): (List[U], S) = {
    decreases(l.size)
    l match {
      case Nil() => (Nil(), init)
      case Cons(h, t) => {
        val (nH, nState) = f(h, init)
        val (nT, nnState) = statefulLeftMap(t, nState, f)
        (Cons(nH, nT), nnState)
      }
    }
  }

  // Additional operation on tuples

  def leftMap[S, T, U](p: (S, T), f: S => U): (U, T) = {
    ( f(p._1), p._2 )
  }

  /*
   * There are two kinds of variables:
   * - Named are identified by (free-form) strings, e.g. "lives", "x", "R",...
   * - Synthetic are identified with a number
   * When creating identifiers "by-hand", you should use "Named" (which should also be more natural)
   * Synthetic are reserved for identifiers created by the different transformations of the formula
   */
  sealed trait Identifier {
    def isSynthetic = this match {
      case Named(_) => false
      case Synthetic(_) => true
    }
  }
  case class Named(str: String) extends Identifier
  case class Synthetic(i: BigInt) extends Identifier {
    require(i >= 0)
  }

  /*
   * A generator of fresh names
   * Any call to `get` should be followed by a call to `next`
   */
  case class FreshNames(i: BigInt) {
    require(i >= 0)

    // Return a fresh identifier
    def get: Identifier = Synthetic(i)
    // Update (functionally) this generator
    def next = FreshNames(i + 1)
  }

  // Term syntax
  sealed abstract class Term
  case class Var(name: Identifier) extends Term
  case class Function(name: Identifier, children: List[Term]) extends Term

  // Formula syntax
  sealed abstract class Formula {
    def containsNoExistential: Boolean = this match {
      case Predicate(_, _) => true
      case And(l, r) => l.containsNoExistential && r.containsNoExistential
      case Or(l, r) => l.containsNoExistential && r.containsNoExistential
      case Implies(l, r) => l.containsNoExistential && r.containsNoExistential
      case Neg(in) => in.containsNoExistential
      case Forall(_, in) => in.containsNoExistential
      case Exists(_, in) => false
    }

    def containsNoUniversal: Boolean = this match {
      case Predicate(_, _) => true
      case And(l, r) => l.containsNoUniversal && r.containsNoUniversal
      case Or(l, r) => l.containsNoUniversal && r.containsNoUniversal
      case Implies(l, r) => l.containsNoUniversal && r.containsNoUniversal
      case Neg(in) => in.containsNoUniversal
      case Forall(_, in) => false
      case Exists(_, in) => in.containsNoUniversal
    }

    def isAtom: Boolean = this match {
      case Predicate(_, _) => true
      case Neg(Predicate(_, _)) => true
      case _ => false
    }

    def isNNF: Boolean = this match {
      case Predicate(_, _) => true 
      case And(l, r) => l.isNNF && r.isNNF
      case Or(l, r) => l.isNNF && r.isNNF
      case Implies(_, _) => false
      case Neg(Predicate(_, _)) => true 
      case Neg(_) => false
      case Forall(_, in) => in.isNNF
      case Exists(_, in) => in.isNNF
    }
  }
  case class Predicate(name: Identifier, children: List[Term]) extends Formula
  case class And(l: Formula, r: Formula) extends Formula
  case class Or(l: Formula, r: Formula) extends Formula
  case class Implies(left: Formula, right: Formula) extends Formula
  case class Neg(inner: Formula) extends Formula
  case class Forall(variable: Var, inner: Formula) extends Formula
  case class Exists(variable: Var, inner: Formula) extends Formula

  // A "box" for atomic formulas
  case class Atom(private val f: Formula) {
    require(f.isAtom)

    def get: Formula = {
      f
    }.ensuring(_.isAtom)
  }


  /*
   * Computes the free variables of a Term, respectively a Formula.
   * As the name suggests, free variables are variables: function/predicate names are not included.
   */
  def freeVariables(t: Term): List[Identifier] = {
    val fv = t match {
      case Var(v)                   => List(v)
      case Function(name, children) => children.flatMap(freeVariables(_))
    }
    unique(fv)
  }.ensuring(ListOps.noDuplicate(_))

  def freeVariables(f: Formula): List[Identifier] = {
    val fv = f match {
      case Predicate(name, children)    => children.flatMap(freeVariables(_))
      case And(left, right)             => freeVariables(left) ++ freeVariables(right)
      case Or(left, right)              => freeVariables(left) ++ freeVariables(right)
      case Implies(left, right)         => freeVariables(left) ++ freeVariables(right)
      case Neg(inner)                   => freeVariables(inner)
      case Forall(Var(id), inner)       => freeVariables(inner) - id
      case Exists(Var(id), inner)       => freeVariables(inner) - id
    }
    unique(fv)
  }.ensuring(ListOps.noDuplicate(_))

  /*
   * Performs simultaneous substitution of Vars by Terms.
   */
  def substitute(t: Term, subst: Map[Identifier, Term]): Term = {
    t match {
      case Var(v) => subst.getOrElse(v, t)
      case Function(name, children) =>
        Function(name, children.map(substitute(_, subst)))
    }
  }
  // We don't need substitution in Formulas, which conveniently avoid having to implement capture avoiding substitution.

  /*
   * Make sure that all bound variables are uniquely named, and with names different from free variables.
   * This will simplify a lot future transformations. The specific renaming can be arbitrary.
   * Return both the new formula and a generator of fresh names for subsequent transformations.
   */
  def makeVariableNamesUnique(f: Formula): (Formula, FreshNames) = {
    def mVNUForm(subst: Map[Identifier, Term])(f: Formula, freshId0: FreshNames): (Formula, FreshNames) = {
      decreases(f)
      f match {
        case Predicate(name, children) => 
          (Predicate(name, children.map(t => substitute(t, subst))), freshId0)
        case And(left, right) =>
          val (nLeft, freshId1) = mVNUForm(subst)(left, freshId0)
          val (nRight, freshId2) = mVNUForm(subst)(right, freshId1)
          (And(nLeft, nRight), freshId2)
        case Or(left, right)  =>
          val (nLeft, freshId1) = mVNUForm(subst)(left, freshId0)
          val (nRight, freshId2) = mVNUForm(subst)(right, freshId1)
          (Or(nLeft, nRight), freshId2)
        case Implies(left, right) =>
          val (nLeft, freshId1) = mVNUForm(subst)(left, freshId0)
          val (nRight, freshId2) = mVNUForm(subst)(right, freshId1)
          (Implies(nLeft, nRight), freshId2)
        case Neg(inner) => leftMap(mVNUForm(subst)(inner, freshId0), Neg(_))
        case Forall(Var(id), inner) =>
          val x = Var(freshId0.get)
          val p = mVNUForm(subst + ((id, x)))(inner, freshId0.next)
          (Forall(x, p._1), p._2)
        case Exists(Var(id), inner) =>
          val x = Var(freshId0.get)
          val p = mVNUForm(subst + ((id, x)))(inner, freshId0.next)
          (Exists(x, p._1), p._2)
      }
    }

    // Generate fresh names for free variables
    val (alreadyTaken, freshId) = statefulLeftMap(
      freeVariables(f), 
      FreshNames(0), 
      (v: Identifier, id: FreshNames) => ((v, Var(id.get): Term), id.next)
    )
    mVNUForm(ListOps.toMap(alreadyTaken))(f, freshId)
  }

  /*
   * Put the formula in Negation Normal Form.
   */
  def negationNormalForm(f: Formula): Formula = {
    (??? : Formula)
  }.ensuring(res =>
    res.isNNF
  )

  /*
   * Put the formula in negation normal form and then eliminates existential quantifiers using Skolemization
   */
  def skolemizationNegation(f0: Formula): Formula = {
    (??? : Formula)
  }.ensuring(res =>
    res.isNNF && res.containsNoExistential
  )

  /*
   * Return the matrix of f in negation normal, skolemized form and make sure variables are uniquely named. Since there are no existential
   * quantifiers and all variable names are unique, the matrix is equivalent to the whole formula.
   */
  def prenexSkolemizationNegation(f: Formula): Formula = {
    (??? : Formula)
  }.ensuring(res =>
    res.isNNF && res.containsNoUniversal && res.containsNoExistential
  )

  type Clause = List[Atom]

  /*
   * This may exponentially blowup the size in the formula in general.
   * If we only preserve satisfiability, we can avoid it by introducing fresh variables, but that is not asked here.
   */
  def conjunctionPrenexSkolemizationNegation(f: Formula): List[Clause] = {
    (??? : List[Clause])
  }


  /*
   * A clause in a proof is either assumed, i.e. it is part of the initial formula, or it is deduced from previous clauses.
   * A proof is written in a specific order, and a justification should not refer to a previous step.
   */
  sealed abstract class Justification
  case object Assumed extends Justification
  case class Deduced(premises: (BigInt, BigInt), subst: Map[Identifier, Term])
      extends Justification

  type ResolutionProof = List[(Clause, Justification)]

  /*
   * Verify if a given proof is correct. The function should verify that every clause is correctly justified (unless assumed).
   */
  def checkResolutionProof(proof: ResolutionProof): Boolean = {
    (??? : Boolean)
  }

  // Smart constructors
  def and(l: List[Formula]): Formula = {
    require(!l.isEmpty)
    val Cons(h, t) = l
    t.foldLeft(h)(And(_: Formula, _: Formula))
  }

  def or(l: List[Formula]): Formula = {
    require(!l.isEmpty)
    val Cons(h, t) = l
    t.foldLeft(h)(Or(_: Formula, _: Formula))
  }

  def solveMansionMystery: Unit = {
    // The three suspects:
    val a = Function(Named("Agatha"), Nil())
    val b = Function(Named("Butler"), Nil()) 
    val c = Function(Named("Charles"), Nil())

    // Variables
    val x = Var(Named("x"))
    val y = Var(Named("y"))

    // Predicates
    def lives(t: Term) = Predicate(Named("lives"), List(t))
    def killed(t: Term, s: Term) = Predicate(Named("killed"), List(t, s))
    def hates(t: Term, s: Term) = Predicate(Named("hates"), List(t, s))
    def richer(t: Term, s: Term) = Predicate(Named("richer"), List(t, s))
    def eq(t: Term, s: Term) = Predicate(Named("eq"), List(t, s))

    val mansionMystery: Formula = and(List(
        Exists(x, And(lives(x), killed(x, a))),
        and(List(
          lives(a),
          lives(b),
          lives(c),
          Forall(x, Implies(lives(x), or(List(eq(x, a), eq(x, b), eq(x, c)))))
        )),
        Forall(
          x,
          Forall(
            y,
            Implies(killed(x, y), And(hates(x, y), Neg(richer(x, y))))
          )
        ),
        Forall(x, Implies(hates(a, x), Neg(hates(c, x)))),
        Forall(x, Implies(hates(a, x), Neg(eq(x, b)))),
        Forall(x, Implies(Neg(eq(x, b)), hates(a, x))),
        Forall(x, Implies(hates(b, x), Neg(richer(x, a)))),
        Forall(x, Implies(Neg(richer(x, a)), hates(b, x))),
        Forall(x, Implies(hates(a, x), hates(b, x))),
        Neg(Exists(x, Forall(y, hates(x, y)))),
        Neg(eq(a, b))
      )
    )
  }

  def assumptions(proof: ResolutionProof): List[Clause] = {
    proof
      .filter(_._2 match {
        case Assumed        => true
        case Deduced(_, _)  => false
      })
      .map(_._1)
  }

  /*
   * To show that a formula phi is valid, we show that it's negation is unsatisfiable, i.e. !phi -> false.
   * Hence if a Proof contains an empty clause, then the negation of the conjunction of all assumed clauses has to be valid
   */
  def extractTheorem(proof: ResolutionProof): Formula = {
    require(!assumptions(proof).isEmpty && assumptions(proof).forall(!_.isEmpty))  // Has "reasonable" assumptions
    require(proof.last._1 == Nil()) // Concludes unsat

    def toFormulas(clauses: List[Clause]): List[Formula] = {
      require(clauses.forall(!_.isEmpty))

      clauses match {
        case Nil() => Nil()
        case Cons(c, cs) => Cons(or(c.map(_.get)), toFormulas(cs))
      }
    }

    val assumpts = toFormulas(assumptions(proof))
    assert(!assumpts.isEmpty)

    Neg(and(assumpts))
  }

}
