package interpretation

import formula.*
import stainless.lang.*
import stainless.collection.*

/**
 * A domain of discourse.
 *
 * The type parameter is the (Scala) type of the elements in the domain.
 * 
 * The exact elements of the domain must be defined via `elements`.
 */
trait Domain[D]:
  /**
   * The concrete set of elements in this domain
   */
  def elements: Set[D]
  /**
   * Check whether a predicate holds for *all* elements in this domain
   * 
   * May not terminate if the domain is infinite.
   */
  def forall(pred: D => Boolean): Boolean = 
    /* TODO: complete me */
    ???
  /** 
   * Check whether a predicate holds for *some* element in this domain
   * 
   * May not terminate if the domain is infinite.
   */
  def exists(pred: D => Boolean): Boolean = 
    /* TODO: complete me */
    ???

/**
 * An environment defining first-order functions and predicates over a chosen
 * domain type.
 *
 * It must additionally (totally) define variables, but can be updated.
 */
trait Environment[D]:
  /**
   * The interpretation of a function in this environment
   * 
   * $$f^e(a_1, ..., a_n)$$
   */
  def applyFunction(id: Identifier, args: List[D]): D
  /**
   * The interpretation of a predicate in this environment
   * 
   * $$P^e(a_1, ..., a_n)$$
   */
  def applyPredicate(id: Identifier, args: List[D]): Boolean
  /**
   * The value of a variable in this environment
   * 
   * $$x^e$$
   */
  def variableValue(id: Identifier): D
  /**
   * Update this environment with a new variable assignment
   * 
   * $$(e[x := a])$$
   */
  def withVariable(id: Identifier, value: D): Environment[D]

/**
 * An interpretation (domain, environment).
 *
 * Noted $$I = (D, e)$$
 *
 * The type parameter is the (Scala) type of the elements in the domain.
 */
case class Interpretation[D](domain: Domain[D], environment: Environment[D]):
  // aliases
  val (d, e) = (domain, environment)

  def withVariable(id: Identifier, value: D): Interpretation[D] =
    Interpretation(d, e.withVariable(id, value))

  def withVariable(mapping: (Identifier, D)): Interpretation[D] =
    val (id, value) = mapping
    withVariable(id, value)

/**
 * Evaluate a term in the given interpretation
 * 
 * If the interpretation is $$I = (D, e)$$, then this is
 * 
 * $$[[t]]_I$$
 */
def evalTerm[D](term: Term, interp: Interpretation[D]): D = 
    /* TODO: complete me */
    ???

/**
 * Evaluate a formula in the given interpretation
 *
 * If the interpretation is $$I = (D, e)$$, then this is
 *
 * $$[[formula]]_I$$
 */
def evalFormula[D](formula: Formula, interp: Interpretation[D]): Boolean = 
    /* TODO: complete me */
    ???


