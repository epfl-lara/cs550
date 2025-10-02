package formula

import stainless.lang.*
import stainless.collection.*
import stainless.annotation.*

import scala.annotation.targetName

import utils.*

/**
  * Identifiers correspond to variables, function symbols, and relation symbols
  * in formulas.
  *
  * There are two kinds of variables:
  * - `Named` identifiers are given by (free-form) strings, e.g. "lives", "x",
  *   "R",...
  * - `Synthetic` identifiers are given by a number 
  *
  * When creating identifiers "by-hand", you should use `Named` (which should
  * also be more natural) `Synthetic` identifiers are reserved for those created
  * internally by different transformations of the formula
  */
sealed trait Identifier:
  def isSynthetic = 
    this match
      case Named(_) => false
      case Synthetic(_) => true

  @extern
  override def toString(): String = 
    this match
      case Named(str) => str
      case Synthetic(id) => s"$$${id}"

case class Named(str: String) extends Identifier
case class Synthetic(i: BigInt) extends Identifier:
  require(i >= 0)


////////////////////////////////////////////////////////
// Terms and Formulas
////////////////////////////////////////////////////////

sealed trait Term:
  def freeVariables: List[Var] = {
      val fv = 
        this match
          case v @ Var(_)               => List(v)
          case Function(name, children) => children.flatMap(_.freeVariables)
      unique(fv)
    }.ensuring(ListOps.noDuplicate(_))

  /**
    * Performs simultaneous substitution of Vars by Terms.
    */
  def substitute(subst: Map[Identifier, Term]): Term =
    this match
      case Var(v) => subst.getOrElse(v, this)
      case Function(name, children) => Function(name, children.map(_.substitute(subst)))

  @extern
  override def toString(): String = 
    this match
      case Var(id) => id.toString()
      case Function(id, children) => 
        val arguments = children.map(_.toString).mkString("(", ", ", ")")
        s"$id$arguments"

case class Var(name: Identifier) extends Term
case class Function(name: Identifier, children: List[Term]) extends Term

sealed trait Formula: 
  @extern
  override def toString(): String = 
    this match
      case Predicate(id, children) => s"$id(${children.mkString(", ")})"
      case And(l, r) => s"($l ∧ $r)"
      case Or(l, r) => s"($l ∨ $r)"
      case Implies(l, r) => s"($l ⇒ $r)"
      case Neg(in) => s"¬$in"
      case Forall(Var(id), in) => s"(∀$id. $in)"
      case Exists(Var(id), in) => s"(∃$id. $in)"

  def freeVariables: List[Var] =
    this match
      case Predicate(_, children) => children.flatMap(_.freeVariables)
      case And(l, r) => l.freeVariables ++ r.freeVariables
      case Or(l, r) => l.freeVariables ++ r.freeVariables
      case Implies(l, r) => l.freeVariables ++ r.freeVariables
      case Neg(in) => in.freeVariables
      case Forall(v, in) => in.freeVariables - v
      case Exists(v, in) => in.freeVariables - v

case class Predicate(name: Identifier, children: List[Term]) extends Formula
case class And(l: Formula, r: Formula) extends Formula
case class Or(l: Formula, r: Formula) extends Formula
case class Implies(left: Formula, right: Formula) extends Formula
case class Neg(inner: Formula) extends Formula
case class Forall(variable: Var, inner: Formula) extends Formula
case class Exists(variable: Var, inner: Formula) extends Formula

// syntactic sugar to build formulas

def id(name: String): Identifier = Named(name)
def id(i: BigInt): Identifier =
  require(i >= 0)
  Synthetic(i)

def nvar(name: String): Var = Var(Named(name))
def svar(i: BigInt): Var =
  require(i >= 0)
  Var(Synthetic(i))

def const(name: String) = Function(id(name), Nil())
def const(i: BigInt) =
  require(i >= 0)
  Function(id(i), Nil())

def and(l: List[Formula]): Formula =
  require(!l.isEmpty)
  val Cons(h, t) = l
  t.foldLeft(h)(And(_: Formula, _: Formula))

def or(l: List[Formula]): Formula =
  require(!l.isEmpty)
  val Cons(h, t) = l
  t.foldLeft(h)(Or(_: Formula, _: Formula))

def forall(v: Var, f: Formula): Formula = Forall(v, f)
def exists(v: Var, f: Formula): Formula = Exists(v, f)

extension (f: Formula) {
  def unary_! : Formula = Neg(f)
  infix def `/\\` (g: Formula): Formula = And(f, g)
  infix def `\\/` (g: Formula): Formula = Or(f, g)
  infix def ==> (g: Formula): Formula = Implies(f, g)
  /**
    * Quantify over all free variables in the formula.
    */
  def universallyClosed: Formula =
    f.freeVariables.foldRight(f)(forall)
}

val zeroPred = Predicate(id("0"), Nil())

val True: Formula =
  zeroPred \/ !zeroPred

val False: Formula =
  zeroPred /\ !zeroPred

// properties of formulas we can check and use to specify functions
extension (f: Formula) {
    def containsNoExistential: Boolean = 
      f match
        case Predicate(_, _) => true
        case And(l, r) => l.containsNoExistential && r.containsNoExistential
        case Or(l, r) => l.containsNoExistential && r.containsNoExistential
        case Implies(l, r) => l.containsNoExistential && r.containsNoExistential
        case Neg(in) => in.containsNoExistential
        case Forall(_, in) => in.containsNoExistential
        case Exists(_, in) => false

    def containsNoUniversal: Boolean = 
      f match
        case Predicate(_, _) => true
        case And(l, r) => l.containsNoUniversal && r.containsNoUniversal
        case Or(l, r) => l.containsNoUniversal && r.containsNoUniversal
        case Implies(l, r) => l.containsNoUniversal && r.containsNoUniversal
        case Neg(in) => in.containsNoUniversal
        case Forall(_, in) => false
        case Exists(_, in) => in.containsNoUniversal

    def isLiteral: Boolean = 
      f match
        case Predicate(_, _) => true
        case Neg(Predicate(_, _)) => true
        case _ => false

    def isNNF: Boolean = 
      f match 
        case Predicate(_, _) => true
        case And(l, r) => l.isNNF && r.isNNF
        case Or(l, r) => l.isNNF && r.isNNF
        case Implies(_, _) => false
        case Neg(Predicate(_, _)) => true
        case Neg(_) => false
        case Forall(_, in) => in.isNNF
        case Exists(_, in) => in.isNNF
}

////////////////////////////////////////////////////////
// Literals and CNF representation
////////////////////////////////////////////////////////

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
case class Literal(private val f: Formula):
  require(f.isLiteral)

  def get: Formula = f.ensuring(_.isLiteral)

  def substitute(subst: Map[Identifier, Term]): Literal =
    def rec(f: Formula, subst: Map[Identifier, Term]): Formula = {
      require(f.isLiteral)      
      f match 
        case Predicate(name, children) => 
          Predicate(name, children.map(_.substitute(subst)))
        case Neg(inner) => 
          Neg(rec(inner, subst))
    }.ensuring(_.isLiteral)

    Literal(rec(this.get, subst))

  def negation: Literal =
    get match 
      case Neg(in) => Literal(in)
      case p => Literal(Neg(p))

  def unary_! : Literal = negation

  @extern
  override def toString(): String = get.toString

/**
 * A clause is a disjunction of literals.
 * We represent it as a list of literals.
 * The empty clause represents false.
 */
type Clause = List[Literal]

extension (c: Clause)
  def toFormula: Formula =
    if c.isEmpty then
      False
    else
      or(c.map(_.get))

