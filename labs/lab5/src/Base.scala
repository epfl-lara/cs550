
import lisa.maths.SetTheory.Base.Pair.{*, given}
import lisa.maths.SetTheory.Functions.Function.{*, given}

object Base extends lisa.Main:

  private val R = variable[Ind]
  private val f = variable[Ind]
  private val x = variable[Ind]
  private val y = variable[Ind]

  // define simple infix notations for relations and binary functions

  val infixRelation = DEF(λ(R, λ(x, λ(y, pair(x)(y) ∈ R)))).printAs(args =>
      val r = args(0)
      val x = args(1)
      val y = args(2)
      s"$x ${r} $y"
    )

  object IRel:
    def apply(R: Expr[Ind], x: Expr[Ind], y: Expr[Ind]): Expr[Prop] = infixRelation(R)(x)(y)

    def unapply(e: Expr[Prop]): Option[(Expr[Ind], Expr[Ind], Expr[Ind])] = e match
      case App(App(App(`infixRelation`, r), x), y) => Some((r, x, y))
      case _ => None

  
  val infixBinaryFunction = DEF(λ(f, λ(x, λ(y, app(f)(pair(x)(y)))))).printAs(args =>
      val f = args(0)
      val x = args(1)
      val y = args(2)
      s"($x ${f} $y)"
    )

  object IBinFun:
    def apply(f: Expr[Ind], x: Expr[Ind], y: Expr[Ind]): Expr[Ind] = infixBinaryFunction(f)(x)(y)

    def unapply(e: Expr[Ind]): Option[(Expr[Ind], Expr[Ind], Expr[Ind])] = e match
      case App(App(App(`infixBinaryFunction`, f), x), y) => Some((f, x, y))
      case _ => None

  functionBetween.printAs(args =>
      val f = args(0)
      val x = args(1)
      val y = args(2)
      s"$f :: $x → $y"
  )

end Base

