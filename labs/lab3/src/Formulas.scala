import stainless.lang.*
import stainless.collection.*
import stainless.annotation.*

import scala.annotation.targetName

import Utils.*

object Formulas {
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

    @extern
    override def toString(): String = this match {
      case Named(str) => str
      case Synthetic(id) => s"$$${id}"
    }
  }
  case class Named(str: String) extends Identifier
  case class Synthetic(i: BigInt) extends Identifier {
    require(i >= 0)
  }

  // Terms
  sealed abstract class Term {

    def freeVariables: List[Identifier] = {
      val fv = this match {
        case Var(v)                   => List(v)
        case Function(name, children) => children.flatMap(_.freeVariables)
      }
      unique(fv)
    }.ensuring(ListOps.noDuplicate(_))

    /*
    * Performs simultaneous substitution of Vars by Terms.
    */
    def substitute(subst: Map[Identifier, Term]): Term = {
      this match {
        case Var(v) => subst.getOrElse(v, this)
        case Function(name, children) => Function(name, children.map(_.substitute(subst)))
      }
    }

    @extern
    override def toString(): String = this match {
      case Var(id) => id.toString()
      case Function(id, children) => s"${id}(${mkString(children.map(_.toString()))})"
    }
  }
  case class Var(name: Identifier) extends Term
  case class Function(name: Identifier, children: List[Term]) extends Term

  // Formulas
  sealed abstract class Formula {

    def freeVariables: List[Identifier] = {
      val fv = this match {
        case Predicate(name, children)    => children.flatMap(_.freeVariables)
        case And(left, right)             => left.freeVariables ++ right.freeVariables
        case Or(left, right)              => left.freeVariables ++ right.freeVariables
        case Implies(left, right)         => left.freeVariables ++ right.freeVariables
        case Neg(inner)                   => inner.freeVariables
        case Forall(Var(id), inner)       => inner.freeVariables - id
        case Exists(Var(id), inner)       => inner.freeVariables - id
      }
      unique(fv)
    }.ensuring(ListOps.noDuplicate(_))


    // We don't need substitution in Formulas, which conveniently avoid having to implement capture avoiding substitution.

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

    def isLiteral: Boolean = this match {
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

    @extern
    override def toString(): String = this match {
      case Predicate(id, children) => s"${id}(${mkString(children.map(_.toString()))})"
      case And(l, r) => s"(${l} ∧ ${r})"
      case Or(l, r) => s"(${l} ∨ ${r})"
      case Implies(l, r) => s"(${l} ⇒ ${r})"
      case Neg(in) => s"¬${in}"
      case Forall(Var(id), in) => s"(∀${id}. ${in})"
      case Exists(Var(id), in) => s"(∃${id}. ${in})"
    }
  }
  case class Predicate(name: Identifier, children: List[Term]) extends Formula
  case class And(l: Formula, r: Formula) extends Formula
  case class Or(l: Formula, r: Formula) extends Formula
  case class Implies(left: Formula, right: Formula) extends Formula
  case class Neg(inner: Formula) extends Formula
  case class Forall(variable: Var, inner: Formula) extends Formula
  case class Exists(variable: Var, inner: Formula) extends Formula

  // Some syntactic sugar to build formulas

  def id(name: String): Identifier = Named(name)
  def id(i: BigInt): Identifier = {
    require(i >= 0)
    Synthetic(i)
  }

  def nvar(name: String): Var = Var(Named(name))
  def svar(i: BigInt): Var = {
    require(i >= 0)
    Var(Synthetic(i))
  }

  def const(name: String) = Function(id(name), Nil())
  def const(i: BigInt) = {
    require(i >= 0)
    Function(id(i), Nil())
  }

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

  def forall(id: String, f: Formula): Formula = {
    Forall(nvar(id), f)
  }

  def forall(id: BigInt, f: Formula): Formula = {
    require(id >= 0)
    Forall(svar(id), f)
  }

  def forall(l: List[Identifier], f: Formula): Formula = {
    l.foldRight(f)( (id: Identifier, f: Formula) => Forall(Var(id), f))
  }

  def universallyQuantified(f: Formula): Formula = {
    forall(f.freeVariables, f)
  }

  /**
   * A "box" for literals.
   * One may wonder "why are we boxing formulas into this case class?".
   * If we did not do that, ensuring that a `List[List[Formula]]` (which is how we represent formulas in CNF)
   * is indeed in CNF would mean stating the following:
   * cnf.forall(clause => clause.forall(literal => literal.isLiteral))
   * which is both verbose and impractical, one would need to use many lemmas on lists to ensure e.g.
   * cnf(0)(1) // "The second literal of the first clause is indeed a literal"
   *
   * It turns out to be much simpler to box formulas satisfying this condition into this class,
   * which then ensures implicitly that the formulas are literals.
   */
  case class Literal(private val f: Formula) {
    require(f.isLiteral)

    def get: Formula = {
      f
    }.ensuring(_.isLiteral)

    def substitute(subst: Map[Identifier, Term]): Literal = {
      def rec(f: Formula, subst: Map[Identifier, Term]): Formula = {
        require(f.isLiteral)
        f match {
          case Predicate(name, children) => Predicate(name, children.map(_.substitute(subst)))
          case Neg(inner) => Neg(rec(inner, subst))
        }
      }.ensuring(_.isLiteral)

      Literal(rec(this.get, subst))
    }

    def negation: Literal = {
      Literal(
        this.get match {
          case p@Predicate(_, _)  => Neg(p)
          case Neg(in)            => in
        }
      )
    }

    @extern
    override def toString(): String = {
      f.toString
    }
  }

  type Clause = List[Literal]

  /** 
   * You can safely ignore the [[@targetName]] annotations. 
   * See https://docs.scala-lang.org/scala3/reference/other-new-features/targetName.html if you're curious.
   */

  @targetName("clauseToFormula")
  def toFormula(clause: Clause): Formula = {
    require(!clause.isEmpty)
    or(clause.map(_.get))
  }

  @targetName("cnfToFormula")
  def toFormula(cnf: List[Clause]): Formula = {
    require(!cnf.isEmpty)
    require(cnf.forall(!_.isEmpty))
    def iter(cnf: List[Clause]): Formula = {
      require(!cnf.isEmpty)
      require(cnf.forall(!_.isEmpty))

      val Cons(h, t) = cnf
      val hf = toFormula(h)
      if (t.isEmpty) {
        hf
      }
      else {
        And(hf, iter(t))
      }
    }
    universallyQuantified(iter(cnf))
  }
}