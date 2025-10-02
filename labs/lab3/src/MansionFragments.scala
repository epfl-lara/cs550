package resolution.mansion

import stainless.lang.*
import stainless.collection.*

import formula.*
import resolution.*
import Mansion.*

object MansionFragments {


  // Note that a `ResolutionProof` is just a list of steps `(Clause,
  // Justification)`.

  // You can use the (Scala) variable `killer` to refer to the killer E.g. of a
  // proof step using it: The killer is one of the characters 

  // a possible step is
  private val exampleStep =  
    ( 
      List(eqv(killer,a), eqv(killer, b), eqv(killer, c)), 
      Deduced(0, 5, Map(id(1) -> killer))
    )

  /**
   * Starting from just [[Mansion.assumptions]], prove that Charles is innocent.
   */
  def charlesInnocent: ResolutionProof = {
    List(
      /* TODO: your proof here */
    )
  }

  /**
   * Starting from the proof from [[charlesInnocent]] and [[Mansion.prelude]],
   * extend it to prove that Agatha killed herself.
   *
   * You may assume that your proof begins at index `k` in the full proof.
   * 
   * i.e., k - 1 is the last step of the prelude, and so on.
   */
  def agathaKilledAgatha(k: BigInt): ResolutionProof = {
    List(
        /* TODO: Complete me */
    )
  }
}
