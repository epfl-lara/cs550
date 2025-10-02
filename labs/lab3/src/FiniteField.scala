package interpretation.field

import stainless.lang.*
import stainless.collection.*
import stainless.annotation.*
import interpretation.*
import formula.*

import FiniteField.isPrime

/**
 * A finite field of prime order n.
 * 
 * Mathematically, represents the field Z/nZ. 
 */
class FiniteField(n: BigInt) extends Domain[BigInt]:
  require(n > 1 && isPrime(n))
  @extern // due to until
  def elements: Set[BigInt] = Set((BigInt(0) until n).toSet)

/**
 * An environment for a finite field of prime order n.
 * 
 * Defines addition and multiplication modulo n, as well as the constants
 * 0 and 1.
 */
case class FiniteFieldEnvironment(n: BigInt, variables: Map[Identifier, BigInt]) extends Environment[BigInt]:
  require(n > 1)
  require(isPrime(n))

  /**
   * positive mod wrt n
   */
  def mod(x: BigInt): BigInt = 
    if n <= 0 then 0
    else if x >= 0 then x % n
    else (n - ((-x) % n)) % n

  // field operations we will map everything to
  def zero: BigInt = 0
  def one: BigInt = 1
  def add(x: BigInt, y: BigInt): BigInt = mod(x + y)
  def mul(x: BigInt, y: BigInt): BigInt = mod(x * y)

  def default: BigInt = zero

  def applyFunction(id: Identifier, args: List[BigInt]): BigInt = 
    id match
      case Named("0") => zero
      case Named("1") => one
      case Named("+") if args.length >= 2 => add(args(0), args(1))
      case Named("*") if args.length >= 2 => mul(args(0), args(1))
      case _ => default

  def applyPredicate(id: Identifier, args: List[BigInt]): Boolean = 
    id match
      case Named("=") if args.length >= 2 => args(0) == args(1)
      case _ => false

  def variableValue(id: Identifier): BigInt =
    variables.getOrElse(id, default)

  def withVariable(id: Identifier, value: BigInt): Environment[BigInt] =
    FiniteFieldEnvironment(n, variables + (id -> mod(value)))

object FiniteField:
  @extern
  def isPrime(n: BigInt): Boolean =
    require(n > 0)
    if n == 1 then false
    else if n == 2 then true
    else !(BigInt(2) `to` (n / 2)).exists(n % _ == 0)

  /**
   * Create a default interpretation over a finite field of prime order `size`. 
   */
  def interpretation(size: BigInt): Interpretation[BigInt] = 
    require(size > 1 && isPrime(size))
    val domain = new FiniteField(size)
    val environment = FiniteFieldEnvironment(size, Map.empty)
    Interpretation(domain, environment)

  /**
   * Create an interpretation over a finite field of prime order `size`,
   * initialized with a variable mapping.
   */
  def interpretation(size: BigInt, variables: Map[Identifier, BigInt]): Interpretation[BigInt] = 
    require(size > 1 && isPrime(size))
    variables.toList.foldLeft(interpretation(size))(_.withVariable(_))

