import stainless.lang.Map
import stainless.collection.*
import stainless.annotation.*

import Formulas.*
import Resolution.*
import Utils.*

object Mansion {
  val a = const("a")
  val b = const("b")
  val c = const("c")

  val x = nvar("x")
  val y = nvar("y")
  val z = nvar("z")

  // x lives in the mansion
  def lives(x: Term) = Literal(Predicate(id("lives"), List(x)))
  // x killed y
  def killed(x: Term, y: Term) = Literal(Predicate(id("killed"), List(x, y)))
  // x hates y
  def hates(x: Term, y: Term) = Literal(Predicate(id("hates"), List(x, y)))
  // x is richer than y
  def richer(x: Term, y: Term) = Literal(Predicate(id("richer"), List(x, y)))
  // x and y are the same person
  def eqv(x: Term, y: Term) = Literal(Predicate(id("="), List(x, y)))

  def livesp(a: Term) = Predicate(id("lives"), List(a))
  def killedp(a: Term, b: Term) = Predicate(id("killed"), List(a, b))
  def hatesp(a: Term, b: Term) = Predicate(id("hates"), List(a, b))
  def richerp(a: Term, b: Term) = Predicate(id("richer"), List(a, b))
  def eqvp(a: Term, b: Term) = Predicate(id("="), List(a, b))

  def hated(t: Term) = Function(id("hated"), List(t))
  def leibnizProp(predicate: Term => Predicate): Formula = Forall(nvar("x"), Forall(nvar("y"), Implies(eqvp(nvar("x"), nvar("y")), Implies(predicate(nvar("x")), predicate(nvar("y"))))))

  val mansionMystery: Formula = and(List(
    Exists(nvar("x"), and( List(livesp(x), killedp(x, a)) )),
    and(List(livesp(a), livesp(b), livesp(c), Forall(nvar("x"), Implies(livesp(x), or(List(eqvp(x, a), eqvp(x, b), eqvp(x, c))))))),
    Forall(nvar("x"), Forall(nvar("y"), Implies(killedp(x, y), and(List( hatesp(x,y), Neg(richerp(x,y)) )) ))),
    Forall(nvar("x"), Implies(hatesp(a,x), Neg(hatesp(c,x)))),
    Forall(nvar("x"), Implies(hatesp(a,x), Neg(eqvp(x,b)))),
    Forall(nvar("x"), Implies(Neg(eqvp(x,b)), hatesp(a,x))),
    Forall(nvar("x"), Implies(hatesp(b,x), Neg(richerp(x,a)))),
    Forall(nvar("x"), Implies(Neg(richerp(x,a)), hatesp(b,x))),
    Forall(nvar("x"), Implies(hatesp(a, x), hatesp(b,x))),
    Neg(Exists(nvar("x"), Forall(nvar("y"), hatesp(x,y)))),
    Neg(eqvp(a,b))
  ))

  val additionalAssumptions: Formula = and(List(
    // 16) = commutativity
    forall("x", forall("y", Implies(eqvp(nvar("x"), nvar("y")), eqvp(nvar("y"), nvar("x"))))), 
    // 17) Leibniz's property for `killed(_, x)`                                               
    forall("z", leibnizProp(killedp(_, nvar("z")))),   
    // 18) Leibniz's property for `hates(_, x)`                                                                            
    forall("z", leibnizProp(hatesp(_, nvar("z")))),   
    // 19) Leibniz's property for `hates(x, _)`                                                                                  
    forall("z", leibnizProp(hatesp(nvar("z"), _))),                                                                                     
  ))

  val baseFormula = conjunctionPrenexSkolemizationNegation(And(mansionMystery, additionalAssumptions))
  val assumptions: ResolutionProof = baseFormula.map(c => (c, Assumed))

  /* The assumptions are:
   *   0 Assumed             : lives($0())
   *   1 Assumed             : killed($0(), a())
   *   2 Assumed             : lives(a())
   *   3 Assumed             : lives(b())
   *   4 Assumed             : lives(c())
   *   5 Assumed             : (((¬lives($1) ∨ =($1, a())) ∨ =($1, b())) ∨ =($1, c()))
   *   6 Assumed             : (¬killed($2, $3) ∨ hates($2, $3))
   *   7 Assumed             : (¬killed($2, $3) ∨ ¬richer($2, $3))
   *   8 Assumed             : (¬hates(a(), $4) ∨ ¬hates(c(), $4))
   *   9 Assumed             : (¬hates(a(), $5) ∨ ¬=($5, b()))
   *  10 Assumed             : (=($6, b()) ∨ hates(a(), $6))
   *  11 Assumed             : (¬hates(b(), $7) ∨ ¬richer($7, a()))
   *  12 Assumed             : (richer($8, a()) ∨ hates(b(), $8))
   *  13 Assumed             : (¬hates(a(), $9) ∨ hates(b(), $9))
   *  14 Assumed             : ¬hates($10, $11($10))
   *  15 Assumed             : ¬=(a(), b())
   *  16 Assumed             : (¬=($12, $13) ∨ =($13, $12))
   *  17 Assumed             : ((¬=($15, $16) ∨ ¬killed($15, $14)) ∨ killed($16, $14))
   *  18 Assumed             : ((¬=($18, $19) ∨ ¬hates($18, $17)) ∨ hates($19, $17))
   *  19 Assumed             : ((¬=($21, $22) ∨ ¬hates($20, $21)) ∨ hates($20, $22))
   */

  // Skolem functions
  // These were found by analyzing the assumptions once transformed
  val killer = const(0)
  def notHatedBy(t: Term) = Function(id(11), List(t))

  def buildFirstPart(missing: ResolutionProof) = {
    assumptions ++
      List(
      // 20) The killer is one of the characters
      ( List(eqv(killer, a), eqv(killer, b), eqv(killer, c)), Deduced((0, 5), Map(id(1) -> killer)) ),
    ) ++
    missing
  }

  def buildFullProof(missing1: ResolutionProof, missing2: BigInt => ResolutionProof) = {
    val firstPart = buildFirstPart(missing1)
    val offset = firstPart.length

    val prelude = firstPart ++ List(
        // -11) If someone is the killer, then it killed Agatha                                           
        ( List(eqv(killer, svar(16)).negation, killed(svar(16), a)), Deduced((1, 17), Map(id(14) -> a, id(15) -> killer)) ),           
        // -10) Charles isn't the killer
        ( List(eqv(killer, c).negation), Deduced((offset-1, offset), Map(id(16) -> c)) ),                                                             
        // -9) The killer is Agatha or Butler
        ( List(eqv(killer, a), eqv(killer, b)), Deduced((20, offset+1), Map()) ),                                                                 
        // -8) Agatha hates X ==> Butler hates X
        ( List(hates(a, notHatedBy(b)).negation), Deduced((13, 14), Map(id(9) -> notHatedBy(b), id(10) -> b)) ),        
        // -7) The person Butler doesn't hate is Butler                       
        ( List(eqv(notHatedBy(b), b)), Deduced((10, offset+3), Map(id(6) -> notHatedBy(b))) ),  
        // -6) Butler is the person Butler doesn't hate                                                     
        ( List(eqv(b, notHatedBy(b))), Deduced((16, offset+4), Map(id(12) -> notHatedBy(b), id(13) -> b)) ),  
        // -5) 31's intermediary step                                     
        ( List(hates(b, b).negation, hates(b, notHatedBy(b))), Deduced((19, offset+5), Map(id(20) -> b, id(21) -> b, id(22) -> notHatedBy(b))) ), 
        // -4) Butler doesn't hate himself
        ( List(hates(b, b).negation), Deduced((14, offset+6), Map(id(10) -> b)) ),  
        // -3) Butler is richer than Agatha                                                             
        ( List(richer(b, a)), Deduced((12, offset+7), Map(id(8) -> b)) ),     
        // -2) Butler didn't kill Agatha                                                              
        ( List(killed(b, a).negation), Deduced((7, offset+8), Map(id(2) -> b, id(3) -> a)) ),    
        // -1) Butler isn't the killer                                           
        ( List(eqv(killer, b).negation), Deduced((offset, offset+9), Map(id(16) -> b)) )
      )
    val newOffset = prelude.length

    prelude ++ missing2(newOffset)
  }


  lazy val firstPartProof = buildFirstPart(MansionFragments.charlesInnocent)
  lazy val fullProof = buildFullProof(MansionFragments.charlesInnocent, MansionFragments.agathaKilledAgatha)

  @extern
  @main
  def main(part: Int): Unit = {
    val (goalClause, proof) = part match {
      case 1 => (killed(c, a).negation, buildFirstPart(MansionFragments.charlesInnocent))
      case 2 => (killed(a, a), buildFullProof(MansionFragments.charlesInnocent, MansionFragments.agathaKilledAgatha))
      case _ => throw new Exception(s"Argument should be 0 or 1; was ${part}")
    }

    prettyPrint(proof)
    val checked = checkResolutionProof(proof)

    if (!checked.valid) {
      println("Proof is not valid.")
      println(checked)
    }
    else if (conclusion(proof) != List(goalClause)) {
      println("Proof valid but incomplete.")
      println(s"The conclusion should be ${toFormula(List(goalClause))}.")
    }
    else {
      println("Proof successful!")
    }
  }
}