import stainless.lang.*
import stainless.collection.*
import stainless.annotation.*

import Formulas.*
import Resolution.*

object Utils {
  // Additional operations on lists

  // Re-implementation of `List[T].unique`
  def unique[T](l: List[T]): List[T] = {
    l match {
      case Nil() => Nil()
      case Cons(h, t) => 
        if (t.contains(h)) {
          unique(t)
        }
        else {
          Cons(h, unique(t))
        }
    }
  }.ensuring(res => 
    res.content == l.content &&
    ListOps.noDuplicate(res)
  )

  // Avoid the ugly `foldLeft[(List[U], S)]`
  def statefulLeftMap[T, U, S](l: List[T], init: S, f: (T, S) => (U, S)): (List[U], S) = {
    decreases(l.size)
    l match {
      case Nil() => (Nil(), init)
      case Cons(h, t) => {
        val (nH, nState) = f(h, init)
        val (nT, nnState) = statefulLeftMap(t, nState, f)
        (Cons(nH, nT), nnState)
      }
    }
  }

  @extern
  def mkString[T](l: List[T]): String = {
    l match {
      case Nil() => ""
      case Cons(h, Nil()) => h.toString()
      case Cons(h, t) => s"${h}, ${mkString(t)}"
    }
  }


  @extern
  def prettyPrint(proof: ResolutionProof): Unit = {
    def iter(proof: ResolutionProof, index: BigInt): Unit = {
      proof match {
        case Nil() => ()
        case Cons(h, t) => {
          val (clauses, justification) = h
          justification match {
            case Assumed =>                 println(f"${index}%3d Assumed             : ${toFormula(clauses)}")
            case Deduced((i, j), subst) =>  println(f"${index}%3d Deduced from ${i}%3d ${j}%3d: ${toFormula(clauses)} using ${subst}")
          }
          iter(t, index+1)
        }
      }
    }

    iter(proof, 0)
  }
}