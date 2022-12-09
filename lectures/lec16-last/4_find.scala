import stainless.collection._
import stainless.annotation._
import stainless.lang._

object Find:

  def exists1(n: Int, lst: List[Int], acc: Boolean): Boolean = 
    lst.foldRight(acc) { (hd, acc) => n == hd || acc }

  def exists2(n: Int, lst: List[Int], acc: Boolean): Boolean =
    lst match
      case Nil() => acc
      case Cons(hd, tl) => n == hd || exists2(n, tl, acc)

  def exists3(n: Int, lst: List[Int], acc: Boolean): Boolean = 
    lst.foldLeft(acc) { (acc, hd) => hd == n || acc }

  def exists4(n: Int, lst: List[Int], acc: Boolean): Boolean =
    lst match
      case Nil() => acc
      case Cons(hd, tl) => exists4(n, tl, n == hd || acc)

  @traceInduct("")
  def check12(n: Int, lst: List[Int], acc: Boolean): Unit = {
  } ensuring(exists1(n, lst, acc) == exists2(n, lst, acc))

  @traceInduct("")
  def check14(n: Int, lst: List[Int], acc: Boolean): Unit = {
  } ensuring(exists1(n, lst, acc) == exists4(n, lst, acc))

  @traceInduct("")
  def check34(n: Int, lst: List[Int], acc: Boolean): Unit = {
  } ensuring(exists3(n, lst, acc) == exists4(n, lst, acc))

  @traceInduct("")
  def check24(n: Int, lst: List[Int], acc: Boolean): Unit = {
  } ensuring(exists2(n, lst, acc) == exists4(n, lst, acc))
