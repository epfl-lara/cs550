import stainless.collection._
import stainless.annotation._
import stainless.lang._

object FastExp:

  def solution1(exp: BigInt, base: BigInt): BigInt =
    if(exp <= 0) BigInt(1)
    else if (exp % 2 == 0) solution1(exp/2, base * base)
    else base * solution1(exp - 1, base)    

  def solution2(exp: BigInt, base: BigInt): BigInt =
    if (exp <= 0) BigInt(1)
    else if (exp % 2 == 1) base * solution2(exp-1, base)
    else solution2(exp/2, base) * solution2(exp/2, base)

  def solution3(exp: BigInt, base: BigInt): BigInt = 
    if (exp <= 0) BigInt(1)
    else if (exp % 2 == 0) solution3(exp/2, base) * solution3(exp/2, base)
    else base * solution3((exp-1)/2, base * base)

  def solution4(exp: BigInt, base: BigInt): BigInt = 
    if (exp <= 0) 1 else base * solution4(exp - 1, base)
  
  def fastExpA(exp: BigInt, base: BigInt): BigInt =
    if (exp <= 0)  1
    else if (exp == 1) base
    else if (exp % 2 == 1) base * fastExpA(exp - 1, base)
    else fastExpA(exp/2, base*base)

  def fastExpB(exp: BigInt, base: BigInt): BigInt = 
    if (exp <= 0)  1 
    else fastExpB(exp / 2, base) * fastExpB(exp /  2, base) * (if (exp % 2 == 1) base else 1)

  def fastExpC(exp: BigInt, base: BigInt): BigInt =
    if (exp <= 0)  1
    else if(base == 0 )  0 
    else if (exp ==1)  base
    else if (exp%2==0)  fastExpC(exp/2, base * base)
    else base * fastExpC(exp - 1, base)

  def fastExpD(exp: BigInt, base: BigInt): BigInt =
    exp match
      case x if (x<=0) => 1
      case x if (x==1) => base
      case _ =>
        if (exp%2 == 1)
          base * fastExpD(exp/2, base*base)
        else
          fastExpD(exp/2, base*base)

  def fastExpF(exp: BigInt, base: BigInt): BigInt =
    if(exp <= 0) 1
    else if(exp == 1) base
    else if(exp % 2 == 0) fastExpF(exp/2, base) * fastExpF(exp/2, base)
    else fastExpF(exp - 1, base) * base

  def fastExpG(exp: BigInt, base: BigInt): BigInt =
    if (exp <= 0) 1
    else if (exp % 2 == 0)
      val res = fastExpG(exp / 2, base)
      res * res
    else
      val res = fastExpG((exp -1)  / 2, base)
      base * res * res

  def fastExpJ(exp: BigInt, base: BigInt): BigInt =
    def square(n: BigInt): BigInt = n*n
    if (exp <= 0) 1
    else if (exp%2 == 0) square(fastExpJ(exp/2, base))
    else square(fastExpJ((exp-1)/2, base))*base

  def fastExpK(exp: BigInt, base: BigInt): BigInt = 
    if (exp <= 0) 1
    else if (exp == 1) base
    else
      val temp = fastExpK(exp / 2, base)
      if (exp % 2 == 0) temp * temp
      else base * temp * temp

      