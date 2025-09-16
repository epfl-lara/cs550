import stainless.lang.*

// Follow the tutorial here to fill in this file:
//
// https://epfl-lara.github.io/stainless/tutorial.html
//
// Additionally, extend the `sInsert` post-condition with the following property:
//
//   (!content(l).contains(x) || res == l) &&
//   (content(l).contains(x) || size(res) == size(l) + 1)

def max(a: BigInt, b: BigInt): BigInt = ???

def max_lemma(x: BigInt, y: BigInt, z: BigInt): Unit = ???

sealed trait List

def size(l: List): BigInt = ???

def isize(l: List): Int = ???

def content(l: List): Set[BigInt] = ???

def sInsert(x: BigInt, l: List): List = ???
