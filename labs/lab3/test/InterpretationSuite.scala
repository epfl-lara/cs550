package interpretation

import stainless.lang.{forall => foralls, *}
import stainless.collection.*
import stainless.annotation.*

import munit.{FunSuite, Tag}

import formula.*
import field.*

class InterpretationSuite extends FunSuite {

  val booleanDomain = new Domain[Boolean] {
    def elements: Set[Boolean] = Set(true, false)
  }

  class BooleanEnvironment(variables: Map[Identifier, Boolean]) extends Environment[Boolean] {
    def applyFunction(id: Identifier, args: List[Boolean]): Boolean = 
      id match
        case Named("0") => false
        case Named("1") => true
        case Named("*") => args(0) && args(1)
        case Named("+") => args(0) || args(1)
        case _ => false

    def applyPredicate(id: Identifier, args: List[Boolean]): Boolean = 
      id match
        case Named("=") => args(0) == args(1)
        case _ => false

    def variableValue(id: Identifier): Boolean =
      variables.getOrElse(id, false)

    def withVariable(id: Identifier, value: Boolean): Environment[Boolean] =
      new BooleanEnvironment(variables + (id -> value))
  }

  val emptyBooleanEnvironment = new BooleanEnvironment(Map.empty)

  val booleanAlgebra = Interpretation(booleanDomain, emptyBooleanEnvironment)

  extension (t: Term) {
    infix def + (s: Term): Term = Function(Named("+"), List(t, s))
    infix def * (s: Term): Term = Function(Named("*"), List(t, s))
  }

  def equal(l: Term, r: Term): Formula = Predicate(Named("="), List(l, r))

  def zero = Function(Named("0"), List())
  def one = Function(Named("1"), List())

  val x = nvar("x")
  val y = nvar("y")
  val z = nvar("z")

  test("Boolean Algebra: Simple term evaluation (zero)") {
    assertEquals(evalTerm(zero, booleanAlgebra), false)
  }

  test("Boolean Algebra: Simple shadowing") {
    val f = exists(x, equal(x, zero))
    assertEquals(evalFormula(f, booleanAlgebra.withVariable(x.name, true)), true)
  }

  test("Boolean Algebra:  Multiple shadowing") {
    val f = forall(x, exists(x, equal(x, zero)))
    assertEquals(evalFormula(f, booleanAlgebra.withVariable(x.name, true)), true)
  }

  val simpleTautologies = List(
    equal(x, y),
    equal(y, x + y * x),
    forall(x, equal(x, zero) \/ equal(x, one)),
    exists(x, equal(x, zero)),
    forall(x, forall(x, equal(x, x))),
  )

  val simpleFallacies = List(
    forall(x, equal(x + one, x)),
    forall(x, forall(y, equal(x, y))),
  )

  test("Boolean Algebra: Simple tautologies") {
    simpleTautologies.map { f =>
      assert(evalFormula(f, booleanAlgebra), s"$f should be valid under two-element boolean algebra") 
    }
  }

  test("Boolean Algebra: Simple fallacies") {
    simpleFallacies.map { f =>
      assert(!evalFormula(f, booleanAlgebra), s"$f should be invalid under two-element boolean algebra") 
    }
  }

  // tests for fields

  val testFields = List(2, 3, 5, 7, 11).map(BigInt.apply).map(n => n -> FiniteField.interpretation(n))

  val fieldTautologies = List(
    forall(x, equal(x + zero, x)),
    forall(x, equal(x * one, x)),
    forall(y, exists(x, equal(x + y, zero))),

    forall(x, forall(y, equal(x + y, y + x))),
    forall(x, forall(y, forall(z, equal((x + y) + z, x + (y + z))))),

    forall(x, forall(y, equal(x * y, y * x))),
    forall(x, forall(y, forall(z, equal((x * y) * z, x * (y * z))))),
  )

  val fieldFallacies = List(
    forall(x, equal(x + one, x)),
    forall(x, forall(y, equal(x, y))),
    forall(x, forall(y, equal(x * y, zero))),
  )

  testFields.map: (n, interp) =>
    fieldTautologies.map: f =>
      test(s"Field of size $n: $f is a tautology") {
        assert(evalFormula(f, interp), s"$f should be valid under field of size $n") 
      }

    fieldFallacies.map: f =>
      test(s"Field of size $n: $f is a fallacy") {
        assert(!evalFormula(f, interp), s"$f should be invalid under field of size $n") 
      }

  test("Only size 2 prime field has characteristic 2") {
    testFields.map: (n, interp) =>
      val char2 = forall(x, equal(x + x, zero))
      if n == 2 then
        assert(evalFormula(char2, interp), s"$char2 should be valid under field of size $n") 
      else
        assert(!evalFormula(char2, interp), s"$char2 should be invalid under field of size $n")
  }

}