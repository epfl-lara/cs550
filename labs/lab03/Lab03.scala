import stainless.collection._
import stainless.lang._
import stainless.annotation._

object Lab03 {


  type Identifier = String
  type Environment = ListMap[Identifier, Boolean]
  /* Implementation of ListMap can be found at https://github.com/epfl-lara/stainless/blob/master/frontends/library/stainless/collection/ListMap.scala */

  sealed abstract class Formula {}

  case class Variable(id: Identifier) extends Formula
  case class Literal(b: Boolean) extends Formula
  case class And(left: Formula, right: Formula) extends Formula
  case class Or(left: Formula, right: Formula) extends Formula
  case class Iff(left: Formula, right: Formula) extends Formula
  case class Not(inner: Formula) extends Formula


  /* Evaluate a formula in a given environment */
  def evaluate(env: Environment, f: Formula): Option[Boolean] = {
    f match {
      case Variable(id) => env.get(id)
      case Literal(b) => Some(b)
      case And(left, right) => (evaluate(env, left), evaluate(env, right)) match {
        case (Some(a), Some(b)) => Some(a && b)
        case _ => None()
      }
      case Or(left, right) => (evaluate(env, left), evaluate(env, right)) match {
        case (Some(a), Some(b)) => Some(a || b)
        case _ => None()
      }
      case Iff(left, right) => (evaluate(env, left), evaluate(env, right)) match {
        case (Some(a), Some(b)) => Some(a == b)
        case _ => None()
      }
      case Not(inner) => evaluate(env, inner).map(!_)
    }
  }

  /* Instantiate a single variable in a formula */
  def instantiation(f:Formula, id:Identifier, value:Boolean):Formula = f match {
    case Variable(id2) => if (id2 == id) Literal(value) else f
    case Literal(b) => f
    case And(left, right) => And(instantiation(left, id, value), instantiation(right, id, value))
    case Or(left, right) => Or(instantiation(left, id, value), instantiation(right, id, value))
    case Iff(left, right) => Iff(instantiation(left, id, value), instantiation(right, id, value))
    case Not(inner) => Not(instantiation(inner, id, value))
  }

  /* --------------- Lemmas -------------- */
  /* You may need to define and prove auxilliary lemmas */


  /* Evaluation coincides with instantiation: evaluate(env, F) == evaluate(env, F[x:=env(x)] */
  def instantiationIdentityLemma(env:Environment, f:Formula, id:Identifier) :Unit = {
    require(env.contains(id) && evaluate(env, f).nonEmpty)

  }.ensuring(evaluate(env, instantiation(f, id, env(id))) == evaluate(env, f))

  /* Instantiating a variable can't make an evaluation ill-defined if it was well-defined */
  def instantiateStillDefinedLemma(env:Environment, f:Formula, id:Identifier, value:Boolean):Unit = {
    require(evaluate(env, f).isDefined)

  }.ensuring(evaluate(env, instantiation(f, id, value)).isDefined)

  /* The case analysis proof step ( F, G ==> F[x:=1]\/G[y:=0] ) is sound */
  def caseAnalysisSoundness(env:Environment, f:Formula, g:Formula, id:Identifier):Unit = {
    require(env.contains(id) && evaluate(env, f)==Some(true) && evaluate(env, g)==Some(true))

  }.ensuring(evaluate(env, Or(instantiation(f, id, true), instantiation(g, id, false)))==Some(true))



  /* ------------- Collections Lemmas ----------------- */

  /* You may use this Lemma about Environment (A=Identifier, B=Boolean) */
  def addIrrelevantElementLemma[A, B](lm: ListMap[A, B], a1: A, a2:A, b:B):Unit = {
    require(lm.contains(a1) && a1!=a2 )
    assert(lm.get(a1).nonEmpty)
    ListMapLemmas.filterStillContains(lm.toList, a2, a1)
    ListMapLemmas.addApplyDifferent(lm, a2, b, a1)
  }.ensuring((lm+(a2, b)).contains(a1) && lm(a1) == (lm+(a2, b))(a1))


}
