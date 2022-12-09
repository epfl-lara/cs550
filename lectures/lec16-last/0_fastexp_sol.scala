import stainless.collection._
import stainless.annotation._
import stainless.lang._

object FastExpSol:

  def solution1(exp: BigInt, base: BigInt): BigInt = ({
    if(exp <= 0) BigInt(1)
    else if (exp % 2 == 0) {
      mulBaseLemma(exp/2, base, base)
      mulExpLemma(exp/2, base, exp/2)
      solution1(exp/2, base * base)
    } else
      base * solution1(exp - 1, base)    
  }).ensuring(res => res == solution4(exp, base))

  def solution2(exp: BigInt, base: BigInt): BigInt = {
    if (exp <= 0) BigInt(1)
    else if (exp % 2 == 1) base * solution2(exp-1, base)
    else {
      mulBaseLemma(exp/2, base, base)
      solution2(exp/2, base) * solution2(exp/2, base)
    }
  }

  def solution3(exp: BigInt, base: BigInt): BigInt = ({
    if (exp <= 0) BigInt(1)
    else if (exp % 2 == 0) {
      mulExpLemma(exp/2, base, exp/2)
      solution3(exp/2, base) * solution3(exp/2, base)
   }
   else {
      mulBaseLemma((exp-1)/2, base, base)
      mulExpLemma((exp-1)/2, base, (exp-1)/2)
      base * solution3 ((exp-1)/2, base * base)
   }
  }).ensuring(res => res == solution4(exp, base))

  def solution4(exp: BigInt, base: BigInt): BigInt = {
    if (exp <= 0) 1 else base * solution4(exp - 1, base)
  }

  def mulExpLemma(exp1: BigInt, base: BigInt, exp2: BigInt): Unit = {
    require(0 <= exp1 && 0 <= exp2)
    decreases(exp1)
    if (exp1 <= 0)
      ()
    else 
      mulExpLemma(exp1 - 1,  base, exp2)
  } ensuring(_ =>
    solution4(exp1, base) * solution4(exp2, base) == solution4(exp1 + exp2, base))

  def mulBaseLemma(exp: BigInt, base1: BigInt, base2: BigInt): Unit = {
    require(0 <= exp)
    decreases(exp)
    if (exp <= 0)
      ()
    else 
      mulBaseLemma(exp - 1,  base1, base2)
  } ensuring(_ =>
    solution4(exp, base1 * base2) == solution4(exp, base1) * solution4(exp, base2))