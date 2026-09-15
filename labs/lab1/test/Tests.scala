import munit.{FunSuite, Tag}

import BooleanAlgebra.*

class Tests extends FunSuite {


  test("eval") {
    def env = (id: Int) => id%2 == 0

    assertEquals(eval(True, env), true)
    assertEquals(eval(False, env), false)
    assertEquals(eval(x(0), env), true)
    assertEquals(eval(x(1), env), false)
    assertEquals(eval(x(0) && x(1), env), false)
    assertEquals(eval(x(0) || x(1), env), true)
    assertEquals(eval(x(0) ==> x(1), env), false)
    assertEquals(eval(!x(1), env), true)
    assertEquals(eval((!x(0) && x(1)) || (x(2) && x(3) || x(4)) ==> (x(0) && !x(1)), env), true)
  }

  test("substitute") {
    def env(id: Int): Formula = 
      if id == 0 then Var(10)
      else if id == 1 then Var(11)
      else if id % 2 == 0 then env(id/2) && !env(id/2 - 1)
      else !env((id-1)/2) || env((id-1)/2 - 1)

    assertEquals(substitute(x(0), id => x(id+1)), x(1))
    assertEquals(substitute(x(0) && x(1), id => x(id+1)), x(1) && x(2))
    assertEquals(substitute(x(0) || x(1), id => x(id+1)), x(1) || x(2))
    assertEquals(substitute(!x(0), id => x(id+1)), !x(1))
    assertEquals(substitute(x(0) ==> x(1), id => x(id+1)), x(1) ==> x(2))
    assertEquals(substitute((!x(0) && x(1)) || (x(2) && x(3) || x(4)) ==> (x(0) && !x(1)), id => x(id+1)), 
      (!x(1) && x(2)) || (x(3) && x(4) || x(5)) ==> (x(1) && !x(2)))
    assertEquals(substitute((!x(0) && x(1)) || (x(2) && x(3) || x(4)) ==> (x(0) && !x(1)), env), 
      ((!x(10) && x(11)) || ((((x(11) && !x(10)) && (!x(11) || x(10))) || ((x(11) && !x(10)) && !x(11))) ==> (x(10) && !x(11)))))
    
  }

  test("nnf") {
    assertEquals(nnf(!True), False)
    assertEquals(nnf(!False), True)
    assertEquals(nnf(x(0)), x(0))
    assertEquals(nnf(!(x(0) && x(1))), !x(0) || !x(1))
    assertEquals(nnf(!(x(0) || x(1))), !x(0) && !x(1))
    assertEquals(nnf(!(x(0) ==> x(1))), x(0) && !x(1))
    assertEquals(nnf((x(0) ==> x(1))), !x(0) || x(1))
    assertEquals(nnf(!(!x(0))), x(0))
    assertEquals(nnf((!x(0) && x(1)) || ((x(2) && x(3) || x(4)) ==> (x(0) && !x(1)))), 
      (!x(0) && x(1)) || (((!x(2) || !x(3)) && !x(4)) || (x(0) && !x(1))))
  }

  test("variables") {
    assertEquals(variables(True), Set())
    assertEquals(variables(False), Set())
    assertEquals(variables(x(0)), Set(0))
    assertEquals(variables(x(0) && x(1)), Set(0, 1))
    assertEquals(variables(x(0) || x(1)), Set(0, 1))
    assertEquals(variables(x(0) ==> x(1)), Set(0, 1))
    assertEquals(variables(!(x(0) && x(1))), Set(0, 1))
    assertEquals(variables(!x(0)), Set(0))
    assertEquals(variables((!x(0) && x(1)) || ((x(2) && x(3) || x(4)) ==> (x(0) && !x(1)))), Set(0, 1, 2, 3, 4))
  }

  test("validity") {
    assertEquals(validity(True), true)
    assertEquals(validity(False), false)
    assertEquals(validity(x(0)), false)
    assertEquals(validity(x(0) && !x(0)), false)
    assertEquals(validity(x(0) || !x(0)), true)
    assertEquals(validity(False || (x(0) ==> x(0))), true)
    assertEquals(validity(x(0) ==> !x(0)), false)
    assertEquals(validity(x(1) ==> (x(0) ==> x(1))), true)
    assertEquals(validity((x(0) ==> x(1)) ==> (x(1) ==> !x(0))), false)
    assertEquals(validity((!x(0) && x(1)) || ((x(2) && x(3) || x(4)) ==> (x(0) && !x(1)))), false)
    assertEquals(validity(((x(0) ==> x(1)) && (x(1) ==> x(2))) ==> (x(0) ==> x(2))), true)
  }

  test("AIG_eval") {
    def env = (id: Int) => id%2 == 0
    assertEquals(AIG_eval(!y(0), env), false)
    assertEquals(AIG_eval(y(0) ↑ y(1), env), true)
    assertEquals(AIG_eval(y(0) ↑ y(1), env), true)
    assertEquals(AIG_eval(y(0) && y(0), env), true)
    assertEquals(AIG_eval(y(1) && y(1), env), false)
    assertEquals(AIG_eval(y(0) ↑ y(0), env), false)
    assertEquals(AIG_eval(y(1) ↑ y(1), env), true)
    assertEquals(AIG_eval((y(0) ↑ y(1)) ↑ ((!y(2) && y(3) && !y(4)) ↑ !y(5)), env), false)
  }

  test("AIG_variables") {
    assertEquals(AIG_variables(y(0)), Set(0))
    assertEquals(AIG_variables(y(0) ↑ y(1)), Set(0, 1))
    assertEquals(AIG_variables(y(0) && y(0)), Set(0))
    assertEquals(AIG_variables(!y(1) ↑ y(1)), Set(1))
    assertEquals(AIG_variables((y(0) ↑ y(1)) ↑ ((!y(2) && y(0) && !y(4)) ↑ !y(2))), Set(0, 1, 2, 4))
  }

  test("AIG_validity") {
    //correct test cases with &&, !, ↑
    assertEquals(AIG_validity(y(0) ↑ !y(0)), true)
    assertEquals(AIG_validity(y(0) ↑ y(1)), false)
    assertEquals(AIG_validity(y(0) ↑ y(0)), false)
    assertEquals(AIG_validity((y(1) ↑ (y(0) && !y(1)))), true)
    assertEquals(AIG_validity(((y(0) ↑ !y(1)) ↑ (y(1) && y(0)))), false)
    assertEquals(AIG_validity((!y(0) ↑ y(1)) ↑ (((y(2) ↑ y(3)) ↑ !y(4)) && (y(0) ↑ !y(1)))), false)
    assertEquals(AIG_validity((((y(0) ↑ !y(1)) && (y(1) ↑ !y(2))) ↑ (y(0) && !y(2)))), true)
  }

  test("formulaToAIG") {
    inline def assertCorrectAIG(f: Formula): Unit =
      val aigf = formulaToAIG(f)
      (variables(f) ++ AIG_variables(aigf)).subsets().foreach(subset => 
          assertEquals(eval(f, subset.contains),AIG_eval(aigf, subset.contains))
      )
    assertCorrectAIG(True)
    assertCorrectAIG(False)
    assertCorrectAIG(x(0))
    assertCorrectAIG(x(0) && x(1))
    assertCorrectAIG(x(0) || x(1))
    assertCorrectAIG(x(0) ==> x(1))
    assertCorrectAIG(!(x(0) && x(1)))
    assertCorrectAIG(!(x(0) || x(1)))
    assertCorrectAIG(!(x(0) ==> x(1)))
    assertCorrectAIG(!x(1))
    assertCorrectAIG((!x(0) && x(1)) || (x(2) && x(3) || x(4)) ==> (x(0) && !x(1)))
    assertCorrectAIG((x(0) ==> x(1)) && (x(1) ==> x(2)) && (x(0) ==> x(2)))
    assertCorrectAIG((x(0) ==> x(1)) || (x(1) ==> x(2)) || (x(0) ==> x(2)))
    assertCorrectAIG((x(0) ==> x(1)) && !(x(1) ==> x(2) || x(3) || x(4) || x(5)) && (x(0) ==> x(2)))
    assertCorrectAIG((x(0) ==> x(1)) || (x(1) ==> x(2) && x(3) && x(4) && !x(5)) || (x(0) ==> x(2)))
    

  }


}