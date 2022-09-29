import stainless.collection._
import stainless.lang._
import stainless.annotation._
 
object SubList {
 
  def subList[T](l1: List[T], l2: List[T]): Boolean = (l1, l2) match {
    case (Nil(), _)                 => true
    case (_, Nil())                 => false
    case (Cons(x, xs), Cons(y, ys)) => (x == y && subList(xs, ys)) || subList(l1, ys)
  }
 
  def subListRefl[T](l: List[T]): Unit = {
 
  }.ensuring(_ =>
    subList(l, l)
  )
 
  def subListTail[T](l1: List[T], l2: List[T]): Unit = {
    require(!l1.isEmpty && subList(l1, l2))

  }.ensuring(_ =>
    subList(l1.tail, l2)
  )
 
  def subListTails[T](l1: List[T], l2: List[T]): Unit = {
    require(!l1.isEmpty && !l2.isEmpty && l1.head == l2.head && subList(l1, l2))
 
  }.ensuring(_ =>
    subList(l1.tail, l2.tail)
  )
 
  def subListTrans[T](l1: List[T], l2: List[T], l3: List[T]): Unit = {
    require(subList(l1, l2) && subList(l2, l3))
 
  }.ensuring(_ =>
    subList(l1, l3)
  )
 
  def subListLength[T](l1: List[T], l2: List[T]): Unit = {
    require(subList(l1, l2))
 
  }.ensuring(_ =>
    l1.length <= l2.length
  )
 
  def subListEqual[T](l1: List[T], l2: List[T]): Unit = {
    require(subList(l1, l2) && l1.length >= l2.length)
 
  }.ensuring(_ =>
    l1 == l2
  )
 
  def subListAntisym[T](l1: List[T], l2: List[T]): Unit = {
    require(subList(l1, l2) && subList(l2, l1))
 
  }.ensuring(_ =>
    l1 == l2
  )

  def subListAppend[T](l1: List[T], l2: List[T]): Unit ={

  }.ensuring(_ => 
    subList(l1, l1 ++ l2)  
  )

  def subListPrepend[T](l1: List[T], l2: List[T]): Unit = {

  }.ensuring(_ => 
    subList(l2, l1 ++ l2)  
  )

  def subListConcatLeft[T](l: List[T], l1: List[T], l2: List[T]): Unit = {
    require(subList(l, l2))

  }.ensuring(_ =>
    subList(l1 ++ l, l1 ++ l2)  
  )

  def subListConcatRight[T](l: List[T], l1: List[T], l2: List[T]): Unit = {
    require(subList(l, l1))

  }.ensuring(_ =>
    subList(l ++ l2, l1 ++ l2)  
  )
 
  @extern
  def main(args: Array[String]): Unit = {
    println(subList(List(0, 2), List(0, 1, 2))) // true
    println(subList(List(1, 0), List(0, 0, 1))) // false
    println(subList(List(10, 5, 25), List(70, 10, 11, 8, 5, 25, 22))) // true
  }
 
}
