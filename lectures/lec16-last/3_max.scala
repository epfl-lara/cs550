import stainless.collection._
import stainless.lang._
import stainless.annotation._

object Max:

  def solution(lst: List[Int]): Int =
    lst match
      case Nil() => Integer.MIN_VALUE
      case Cons(hd, Nil()) => hd
      case Cons(hd, tl) => if (hd > solution(tl)) hd else solution(tl)

  def max1(l: List[Int]): Int = l match
    case Nil() => Integer.MIN_VALUE
    case Cons(h, t) => if (max1(t) > h) max1(t) else h
  
  def max2(l: List[Int]): Int = l match
    case Nil() => Integer.MIN_VALUE
    case Cons(x, Nil()) => x
    case Cons(x, y) =>
      val temp = max2(y)
      if (x > temp) x else temp

  def max3(lst: List[Int]): Int = lst match
    case Nil() => Int.MinValue
    case Cons(h, t) => t.foldLeft(h)((a, b) => if (a >= b) a else b)  

  def max4(l: List[Int]): Int = l match
    case Nil()      => Integer.MIN_VALUE
    case Cons(h, t) => if (h > max4(t)) h else max4(t)

  def max5(lst: List[Int]): Int = lst match
    case Nil() => Int.MinValue
    case Cons(a, Nil()) => a
    case Cons(a, Cons(b, t)) => if (a > b) max5(a :: t) else max5(b :: t)

  def max6(lst: List[Int]): Int =
    def fold(f: (Int, Int) => Int, l: List[Int], a: Int): Int = l match
      case Nil() => a
      case Cons(hd, tl) => f(hd, fold(f, tl, a))
    if(lst.isEmpty) Integer.MIN_VALUE
    else fold({ case (x, y) => { if (x > y) x else y } }, lst, 0)
  
  def max7(lst: List[Int]): Int = lst match
    case Nil() => Integer.MIN_VALUE
    case Cons(h, t) =>
      def helper(seen: Int, rest: List[Int]): Int =
        rest match
          case Nil() => seen
          case Cons(h_0, t_0) =>
            val seen_0 = if (h_0 > seen) h_0 else seen
            val rest_0 = t_0
            helper(seen_0, rest_0)
      helper(h, t)

  def max8(lst: List[Int]): Int =
    if (lst.isEmpty) Integer.MIN_VALUE
    else lst.foldRight(lst.head)({ case (x, y) => { if (x > y) x else y }})