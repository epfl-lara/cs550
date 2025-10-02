package resolution

import stainless.lang.*
import stainless.collection.*
import stainless.annotation.*
import stainless.lang.Map.ToMapOps

import utils.*
import formula.*

case class FreshNames(counter: BigInt):
  require(counter >= 0)
  def get: Identifier =
    Synthetic(counter)
  def next: FreshNames =
    FreshNames(counter + 1)

/**
 * Generate synthetic identifiers for quantified variables to ensure they are
 * unique.
 */
def makeVariableNamesUnique(f: Formula): Formula =

  def rename(substitution: Map[Identifier, Term])
            (freshNames: FreshNames)
            (f: Formula): (Formula, FreshNames) =
    f match
      case Predicate(name, children) =>
        (Predicate(name, children.map(_.substitute(substitution))), freshNames)
      case Neg(inner) =>
        val (renamedInner, nextFreshNames) = rename(substitution)(freshNames)(inner)
        (Neg(renamedInner), nextFreshNames)
      case And(left, right) =>
        val (renamedLeft, freshNamesAfterLeft) = rename(substitution)(freshNames)(left)
        val (renamedRight, freshNamesAfterRight) = rename(substitution)(freshNamesAfterLeft)(right)
        (And(renamedLeft, renamedRight), freshNamesAfterRight)
      case Or(left, right) =>
        val (renamedLeft, freshNamesAfterLeft) = rename(substitution)(freshNames)(left)
        val (renamedRight, freshNamesAfterRight) = rename(substitution)(freshNamesAfterLeft)(right)
        (Or(renamedLeft, renamedRight), freshNamesAfterRight)
      case Implies(left, right) =>
        val (renamedLeft, freshNamesAfterLeft) = rename(substitution)(freshNames)(left)
        val (renamedRight, freshNamesAfterRight) = rename(substitution)(freshNamesAfterLeft)(right)
        (Implies(renamedLeft, renamedRight), freshNamesAfterRight)
      case Forall(variable, inner) =>
        val newVar = Var(freshNames.get)
        val newSubst = substitution + (variable.name -> newVar)
        val (renamedInner, nextFreshNames) = rename(newSubst)(freshNames.next)(inner)
        (Forall(newVar, renamedInner), nextFreshNames)
      case Exists(variable, inner) =>
        val newVar = Var(freshNames.get)
        val newSubst = substitution + (variable.name -> newVar)
        val (renamedInner, nextFreshNames) = rename(newSubst)(freshNames.next)(inner)
        (Exists(newVar, renamedInner), nextFreshNames)

  val (subst, freshNames) = f.freeVariables.foldLeft((Map.empty[Identifier, Term], FreshNames(0))) {
    case ((substs, fn), Var(id)) =>
      (substs + (id -> Var(fn.get)), fn.next)
  }

  rename(subst)(freshNames)(f)._1

////////////////////////////////////////////////////////
// PART 1: Transforming Formulas
////////////////////////////////////////////////////////

/**
 * Transform the formula into negation normal form (NNF).
 */
def negationNormalForm(f: Formula): Formula = {
  /* TODO: Implement me */
  (??? : Formula)
}.ensuring(_.isNNF)

/**
 * Perform the following steps:
 * - Make variable names unique (using [[makeVariableNamesUnique]]);
 * - Transform the formula into negation normal form (using
 *   [[negationNormalForm]]);
 * - Eliminate existential quantifiers using Skolemization.
 *
 * You should reuse the names of the existentially quantified variables when
 * replacing them with Skolem functions.
 */
def skolemizationNegation(f: Formula): Formula = {
  /* TODO: Implement me */
  (??? : Formula)
}.ensuring(res =>
  res.isNNF && res.containsNoExistential
)

/**
 * Perform the following steps:
 * - Transform the formula into negation normal, skolemized form (using
 *   [[skolemizationNegation]]);
 * - Return the matrix of the formula.
 */
def prenexSkolemizationNegation(f: Formula): Formula = {
  /* TODO: Implement me */
  (??? : Formula)
}.ensuring(res =>
  res.isNNF && res.containsNoUniversal && res.containsNoExistential
)

/**
 * Perform the following steps:
 * - Transform the formula into prenex, negation normal, skolemized form (using
 *   [[prenexSkolemizationNegation]]);
 * - Transform the formula into conjunctive normal form (CNF).
 *
 * Note that the formula might grow exponentially in size. If we only want to
 * preserve satisfiability, we could avoid it by introducing fresh variables.
 * This function should **NOT** do that.
 */
def conjunctionPrenexSkolemizationNegation(f: Formula): List[Clause] = {
  /* TODO: Implement me */
  (??? : List[Clause])
}

////////////////////////////////////////////////////////
// PART 2: Proof Checking
////////////////////////////////////////////////////////

/**
 * A justification for a proof step. A clause in a proof can either be
 * [[Assumed]], i.e. it's a hypothesis, or it can be [[Deduced]] from
 * previous clauses using resolution.
 */
sealed trait Justification
case object Assumed extends Justification
case class Deduced(left: BigInt, right: BigInt, subst: Map[Identifier, Term]) extends Justification

/**
 * The result of checking a proof step is either Valid or an error message. The
 * error message in the invalid case is up to you and only intended for
 * debugging for now.
 */
sealed trait ProofCheckResult:
  def isValid: Boolean =
    this match
      case Valid => true
      case Invalid(_) => false

case object Valid extends ProofCheckResult
case class Invalid(reason: String) extends ProofCheckResult

/**
 * Helper function to combine multiple proof checking results.
 */
def mergeResults(r1: ProofCheckResult, r2: ProofCheckResult): ProofCheckResult =
  (r1, r2) match
    case (Valid, Valid) => Valid
    case (Invalid(reason), _) => Invalid(reason)
    case (_, Invalid(reason)) => Invalid(reason)

/**
 * A resolution proof is a list of steps, each comprising a clause and a
 * justification used to derive it.
 */
type ResolutionProof = List[(Clause, Justification)]

@extern
def prettyPrint(proof: ResolutionProof): String =
  proof
    .zipWithIndex
    .map:
      case ((clause, justification), index) =>
        justification match
          case Assumed              => f"${index}%3d Assumed             : ${clause.toFormula}"
          case Deduced(i, j, subst) => f"${index}%3d Deduced from ${i}%3d ${j}%3d: ${clause.toFormula} using ${subst}"
    .mkString("\n")

/**
 * Check whether a single step in a resolution proof is correct.
 * 
 * See [[checkResolutionProof]].
 * 
 * @param proof the resolution proof (at least up to this step)
 * @param conclusion the clause that is being justified
 * @param step the justification for this step
 * @param index the index of this step in the proof (0-based)
 */
def checkResolutionStep(proof: ResolutionProof)
                       (conclusion: Clause, step: Justification, index: BigInt): ProofCheckResult =
  require(index >= 0 && index < proof.length)
  /* TODO: Implement me */
  ???

/**
 * Check whether a resolution proof is correct.
 *
 * It is quite easy to miss some corner cases. We thus recommend that you:
 * - Have a look at the provided methods on Literal, as you will most likely
 *   need them;
 * - "Keep It Simple, Stupid!": efficiency is not taken into account, so no need
 *   for fancy efficient checks;
 * - On the other hand, checking that the conclusion of a resolution step is
 *   valid might be a bit more involved than it seems;
 * - As a consequence of the previous point: add more tests;
 * - You should return [[Valid]] when the proof is valid, and [[Invalid]]
 *   otherwise. In the latter case, you are free to set any string as the
 *   reason. Having precise failure reasons will help you a lot in the third
 *   part of this lab.
 *
 * Note: in order to use string interpolation in stainless, you need to wrap it
 * in an extern function, e.g.
 * ```
 *   @extern def mkErrorMessage = s"This is an error at step ${k}"
 *   Invalid(mkErrorMessage)
 * ```
 */
def checkResolutionProof(proof: ResolutionProof): ProofCheckResult =
  proof
    .zipWithIndex
    .map:
      case ((conclusion, step), index) => 
        if index >= 0 && index < proof.length then
          checkResolutionStep(proof)(conclusion, step, index)
        else 
          // unreachable case
          @extern def invalidIndex = s"Index $index out of bounds for proof of length ${proof.length}"
          Invalid(invalidIndex)
    .reduceOption(mergeResults)
    .getOrElse(Valid) // Empty proofs are valid

extension (proof: ResolutionProof) {
  def conclusion: Clause =
    require(!proof.isEmpty)
    proof.last._1

  def assumptions: List[Clause] =
    proof.filter(_._2 == Assumed).map(_._1)
}

/*
  * To show that a formula φ is valid, we show that its negation is
  * unsatisfiable, i.e. !φ -> false. Hence, if a proof contains an empty clause
  * (and is thus unsat), then the _negation_ of the conjunction of all assumed
  * clauses used to derive it must be valid.
  */
def extractTheorem(proof: ResolutionProof): Formula = {
  // proof is not empty;
  // an empty proof "proves" nothing
  require(!proof.isEmpty)
  // proof has "reasonable" assumptions
  require(!proof.assumptions.isEmpty && proof.assumptions.forall(!_.isEmpty))
  // and concludes the empty clause (==> unsat)
  require(proof.conclusion == List())

  val assumpts = proof.assumptions.map(_.toFormula)
  assert(!assumpts.isEmpty)

  Neg(and(assumpts))
}
