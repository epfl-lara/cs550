import stainless.lang.*
import stainless.collection.*
import stainless.annotation.*

import scala.collection.mutable.{Set => MSet}
import scala.collection.immutable.{Set => SSet}

import munit.{FunSuite, Tag}

import Resolution.*
import Formulas.*
import Mansion.*

class Tests extends FunSuite {

  // Variables
  val x = Var(Named("x"))
  val y = Var(Named("y"))
  val z = Var(Named("z"))
  val u = Var(Named("u"))
  val v = Var(Named("v"))
  val w = Var(Named("w"))
  val x1 = Var(Named("x1"))
  val y1 = Var(Named("y1"))
  val x2 = Var(Named("x2"))
  val y2 = Var(Named("y2"))
  val z2 = Var(Named("z2"))
  val x3 = Var(Named("x3"))
  val x4 = Var(Named("x4"))
  val y4 = Var(Named("y4"))

  val s0 = Var(Synthetic(0))
  val s1 = Var(Synthetic(1))
  val s2 = Var(Synthetic(2))
  val s3 = Var(Synthetic(3))
  val s4 = Var(Synthetic(4))
  val s5 = Var(Synthetic(5))
  val s6 = Var(Synthetic(6))
  val s7 = Var(Synthetic(7))
  val s8 = Var(Synthetic(8))
  val s9 = Var(Synthetic(9))

  // Predicates
  def p(v: Var)(ts: Term*) = Predicate(v.name, ts.foldRight(List())(Cons(_, _)))
  def p1(a: Term) = Predicate(Named("P1"), List(a))
  def p2(a: Term, b: Term) = Predicate(Named("P2"), List(a, b))
  def p6(a: Term, b: Term, c: Term, d: Term, e: Term, f: Term) = Predicate(Named("P6"), List(a, b, c, d, e, f))
  def q1(a: Term) = Predicate(Named("Q1"), List(a))
  def r1(a: Term) = Predicate(Named("R1"), List(a))
  
  // Functions
  def f(v: Var)(ts: Term*) = Function(v.name, ts.foldRight(List())(Cons(_, _)))
  def f1(a: Term) = Function(Named("f1"), List(a))
  def f2(a: Term, b: Term) = Function(Named("f2"), List(a, b))
  def g1(a: Term) = Function(Named("g1"), List(a))
  def g2(a: Term, b: Term) = Function(Named("g2"), List(a, b))
  def h1(a: Term) = Function(Named("h1"), List(a))
  def h2(a: Term, b: Term) = Function(Named("h2"), List(a, b))

  // Constants
  val c1 = Function(Named("c1"), Nil())
  val c2 = Function(Named("c2"), Nil())
  val c3 = Function(Named("c3"), Nil())

  val runningExample: Formula = {
    // "Automating First-Order Logic Proofs Using Resolution", 
    // slide "Running example of FOL formula"
    // P <=> p1
    // R <=> p2
    // f <=> f2
    val prem1 = Forall(x, Exists(y, p2(x, y)))
    val prem2 = Forall(x, Forall(y, Implies(p2(x, y), Forall(z, p2(x, f2(y, z))))))
    val prem3 = Forall(x, Or(p1(x), p1(f2(x, c1))))
    val concl = Forall(x, Exists(y, And(p2(x, y), p1(y))))
    Neg(Implies(and(List(prem1, prem2, prem3)), concl))
  }

  def runningExampleClauses(skolem1: Var, skolem2: Var): List[Clause] = {
    // "Automating First-Order Logic Proofs Using Resolution", 
    // slide "Clauses for the example"
    // P <=> p1
    // R <=> p2
    // f <=> f2
    // s_1 <=> skolem1
    // s_2 <=> skolem2
    // + unique names (e.g. x <=> x1,x2,...)
    val cl1 = List( Literal(p2(x1, f(skolem1)(x1))) )
    val cl2 = List( Literal(Neg(p2(x2, y2))), Literal(p2(x2, f2(y2, z2))) )
    val cl3 = List( Literal(p1(x3)), Literal(p1(f2(x3, c1))) )
    val cl4 = List( Literal(Neg(p2(f(skolem2)(), y4))), Literal(Neg(p1(y4))) )
    List(cl1, cl2, cl3, cl4)
  }

  // Unique names

  test("Unique names - name re-use") {
    val f = And(Exists(x, p1(x)), Exists(x, p1(x)))
    val uf = makeVariableNamesUnique(f)
    assertEquals(
      uf,
      And(Exists(s0, p1(s0)), Exists(s1, p1(s1)))
    )
  }

  test("Unique names - free variable") {
    val f = And(Exists(x, p1(y)), Exists(x, p1(x)))
    val uf = makeVariableNamesUnique(f)
    assertEquals(
      uf,
      And(Exists(s1, p1(s0)), Exists(s2, p1(s2)))
    )
  }

  test("Unique names - nested identical bindings") {
    val f = Exists(x, And(p1(x), Forall(x, p1(x))))
    val uf = makeVariableNamesUnique(f)
    assertEquals(
      uf,
      Exists(s0, And(p1(s0), Forall(s1, p1(s1))))
    )
  }

  test("Unique names - all variables become synthetic") {
    val f = And(p1(x), Exists(y, p1(y)))
    val uf = makeVariableNamesUnique(f)
    val fv = uf.freeVariables
    assert(
      fv.forall(_.isSynthetic)
    )

    uf match {
      case And(Predicate(_, Cons(Var(nx), Nil())), Exists(Var(ny1), Predicate(_, Cons(Var(ny2), Nil())))) => 
        assert(ny1 == ny2)
        assert(nx != ny1)
        assert(nx.isSynthetic && ny1.isSynthetic && ny2.isSynthetic)
      case _ => fail(s"Unexpected formula shape: ${uf}")
    }
  }

  test("Unique names - running example") {
    assertEquals(
      makeVariableNamesUnique(runningExample),
      Neg(Implies(
        and(List(
          Forall(s0, Exists(s1, p2(s0, s1))),
          Forall(s2, Forall(s3, Implies(p2(s2, s3), Forall(s4, p2(s2, f2(s3, s4)))))),
          Forall(s5, Or(p1(s5), p1(f2(s5, c1))))
        )),
        Forall(s6, Exists(s7, And(p2(s6, s7), p1(s7))))
      ))
    )
  }

  // Negation normal form

  test("NNF - basic") {
    val f = Neg(Implies(p1(x), p1(y)))
    val nnf = negationNormalForm(f)
    assertEquals(
      nnf,
      And(p1(x), Neg(p1(y))): Formula
    )
  }

  test("NNF - running example") {
    assertEquals(
      makeVariableNamesUnique(negationNormalForm(runningExample)),
      And(and(List(
          Forall(s0, Exists(s1, p2(s0, s1))),
          Forall(s2, Forall(s3, Or(Neg(p2(s2, s3)), Forall(s4, p2(s2, f2(s3, s4)))))),
          Forall(s5, Or(p1(s5), p1(f2(s5, c1)))),
        )),
        Exists(s6, Forall(s7, Or(Neg(p2(s6, s7)), Neg(p1(s7)))))
      )
    )
  }

  // Skolemization

  test("Skolemization - basic") {
    val formula = Forall(x, Exists(y, And(p1(x), p1(y))))
    val skolemized = skolemizationNegation(formula)
    assertEquals(
      skolemized,
      Forall(s0, And(p1(s0), p1(f(s1)(s0))))
    )
  }

  test("Skolemization - running example") {
    val skolemized = skolemizationNegation(runningExample)
    assertEquals(
      skolemized,
      And(and(List(
          Forall(s0, p2(s0, f(s1)(s0))),
          Forall(s2, Forall(s3, Or(Neg(p2(s2, s3)), Forall(s4, p2(s2, f2(s3, s4)))))),
          Forall(s5, Or(p1(s5), p1(f2(s5, c1)))),
        )),
        Forall(s7, Or(Neg(p2(f(s6)(), s7)), Neg(p1(s7))))
      )
    )
  }

  // Prenex

  test("Prenex - basic") {
    val f = Forall(x, p1(x))
    val matrix = prenexSkolemizationNegation(f)
    assertEquals(
      matrix,
      p1(s0)
    )
  }

  test("Prenex - running example") {
    val matrix = prenexSkolemizationNegation(runningExample)
    assertEquals(
      matrix,
      And(and(List(
          p2(s0, f(s1)(s0)),
          Or(Neg(p2(s2, s3)), p2(s2, f2(s3, s4))),
          Or(p1(s5), p1(f2(s5, c1))),
        )),
        Or(Neg(p2(f(s6)(), s7)), Neg(p1(s7)))
      )
    )
  }

  // CNF

  test("CNF - basic") {
    val f = universallyQuantified( And(Or(p1(x), q1(y)), Or(p1(u), q1(v))) )
    val cnf = conjunctionPrenexSkolemizationNegation(f)
    assertEquals(
      toFormula(cnf),
      universallyQuantified( And(Or(p1(s0), q1(s1)), Or(p1(s2), q1(s3))) )
    )
  }

  test("CNF - running example") {
    assertEquals(
      conjunctionPrenexSkolemizationNegation(runningExample),
      List(
        List( Literal(p2(s0, f(s1)(s0))) ),
        List( Literal(Neg(p2(s2, s3))), Literal(p2(s2, f2(s3, s4))) ),
        List( Literal(p1(s5)), Literal(p1(f2(s5, c1))) ),
        List( Literal(Neg(p2(f(s6)(), s7))), Literal(Neg(p1(s7))) )
      )
    )
  }

  // Proof checking

  test("Proof check - empty proof") {
    assert(checkResolutionProof(Nil()).valid)
  }

  test("Proof check - valid proof") {
    // "Automating First-Order Logic Proofs Using Resolution", 
    // slide "Applying resolution"
    val proof: ResolutionProof = runningExampleClauses(s0, s1).map((_, Assumed)) ++ List(
      /*4*/ ( List( Literal(p2(x1, f2(f(s0)(x1), z2))) ),                 Deduced((0, 1), Map(x2.name -> x1, y2.name -> f(s0)(x1))) ), 
      /*5*/ ( List( Literal(Neg(p1(f(s0)(f(s1)())))) ),                   Deduced((0, 3), Map(x1.name -> f(s1)(), y4.name -> f(s0)(f(s1)()))) ),
      /*6*/ ( List( Literal(p1(f2(f(s0)(f(s1)()), c1))) ),                Deduced((2, 5), Map(x3.name -> f(s0)(f(s1)()))) ),
      /*7*/ ( List( Literal(Neg(p2(f(s1)(), f2(f(s0)(f(s1)()), c1)))) ),  Deduced((3, 6), Map(y4.name -> f2(f(s0)(f(s1)()), c1))) ),
      /*8*/ ( Nil(),                                                      Deduced((4, 7), Map(x1.name -> f(s1)(), z2.name -> c1)) ),
    )
    assert(checkResolutionProof(proof).valid)
  }

  test("Proof check - reordered conclusion") {
    val assumptions: ResolutionProof = List(
      ( List( Literal(p1(c1)), Literal(p1(c2)) ),     Assumed ),
      ( List( Literal(Neg(p1(c1))), Literal(p1(c3))), Assumed ),
    )

    val proof1 = assumptions ++ List(
      ( List( Literal(p1(c2)), Literal(p1(c3))), Deduced((0, 1), Map()) )
    )
    val proof2 = assumptions ++ List(
      ( List( Literal(p1(c3)), Literal(p1(c2))), Deduced((0, 1), Map()) )
    )

    assert(checkResolutionProof(proof1).valid)
    assert(checkResolutionProof(proof2).valid)
  }

  test("Proof check - contraction") {
    val assumptions: ResolutionProof = List(
      ( List( Literal(p1(x1)), Literal(q1(x1)) ),       Assumed ),
      ( List( Literal(Neg(p1(x1))), Literal(q1(x1)) ),  Assumed ),
    )
    val standard = assumptions ++ List(
      ( List(Literal(q1(x1)), Literal(q1(x1))), Deduced((0, 1), Map()) )
    )
    val contracted = assumptions ++ List(
      ( List(Literal(q1(x1))), Deduced((0, 1), Map()) )
    )

    assert(checkResolutionProof(standard).valid)
    assert(checkResolutionProof(contracted).valid)
  }

  // Theorem extraction

  test("Extract theorem - triviality") {
    val f = List(Literal( p1(x) ))
    val nf = List(Literal( Neg(p1(x)) ))
    val proof: ResolutionProof = List(
      (f,     Assumed                     ), // 0
      (nf,    Assumed                     ), // 1
      (Nil(), Deduced((0, 1), Map.empty)  ), // 2
    )
    assert(checkResolutionProof(proof).valid)
    assertEquals(
      extractTheorem(proof),
      Neg(And(p1(x), Neg(p1(x))))
    )
  }

  // The Dreadbury Mansion Mystery

  test("Mansion mystery - Charles' innocence") {
    val proof = Mansion.firstPartProof
    assertEquals(checkResolutionProof(proof), Valid)
    assertEquals(
      conclusion(proof), 
      List(Literal(Neg(Predicate(id("killed"), List(const("c"), const("a"))))))
    )
  }

  test("Mansion mystery - Conclusion") {
    val proof = Mansion.fullProof
    assertEquals(checkResolutionProof(proof), Valid)
    assertEquals(
      conclusion(proof), 
      List(Literal(Predicate(id("killed"), List(const("a"), const("a")))))
    )
  }
}
