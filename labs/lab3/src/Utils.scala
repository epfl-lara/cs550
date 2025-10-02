package utils

import stainless.lang.*
import stainless.collection.*
import stainless.annotation.*

import formula.*

extension [T](l: List[T]) {
  def reduce(op: (T, T) => T): T =
    require(!l.isEmpty)
    l match
      case Cons(h, t) => t.foldLeft(h)(op)

  def reduceOption(op: (T, T) => T): Option[T] =
    if l.isEmpty then None()
    else Some(l.reduce(op))

  def zipWithIndex: List[(T, BigInt)] = {
    def rec(l: List[T], index: BigInt, original: List[T]): List[(T, BigInt)] = {
      require(0 <= index && index + l.length == original.length)
      l match
        case Nil()      => Nil()
        case Cons(h, t) => Cons((h, index), rec(t, index + 1, original))
    }.ensuring( res =>
      res.length == l.length &&
      res.forall( p => p._2 < original.length && p._2 >= 0)
    )

    rec(l, 0, l)
  }.ensuring(res => res.length == l.length && res.forall(p => p._2 < l.length && p._2 >= 0))

  @extern
  def mkString(prefix: String, sep: String, suffix: String): String =
    val inner = l.mkString(sep)
    s"$prefix$inner$suffix"

  @extern
  def mkString(sep: String): String =
    l.map(_.toString).reduceOption(_ + sep + _).getOrElse("")
}

def unique[T](l: List[T]): List[T] = {
  l match
    case Nil() => Nil()
    case Cons(h, t) =>
      if (t.contains(h)) then unique(t)
      else Cons(h, unique(t))
}.ensuring(res =>
  res.content == l.content &&
    ListOps.noDuplicate(res)
)
