
object Lab04 {

  // Term syntax
  sealed abstract class Term
  case class Var(name:String) extends Term
  case class Function(name:String, children:List[Term]) extends Term

  //Formula syntax
  sealed abstract class Formula
  case class Predicate(name:String, children:List[Term]) extends Formula

  case class And(children:List[Formula]) extends Formula
  case class Or(children:List[Formula]) extends Formula
  case class Implies(left:Formula, right:Formula) extends Formula
  case class Neg(inner:Formula) extends Formula

  case class Forall(variable:String, inner:Formula) extends Formula
  case class Exists(variable:String, inner:Formula) extends Formula


  /*
  Computes the free variables of a Term, respectively a Formula.
   */
  def freeVariables(t:Term) : Set[Var] = t match {
    case v: Var => Set(v)
    case Function(name, children) => children.flatMap(freeVariables).toSet
  }
  def freeVariables(f:Formula):Set[Var] = f match {
    case Predicate(name, children) => children.flatMap(freeVariables).toSet
    case And(children) => children.flatMap(freeVariables).toSet
    case Or(children) => children.flatMap(freeVariables).toSet
    case Implies(left, right) => freeVariables(left)++freeVariables(right)
    case Neg(inner) => freeVariables(inner)
    case Forall(variable, inner) => freeVariables(inner)-Var(variable)
    case Exists(variable, inner) => freeVariables(inner)-Var(variable)
  }

  /*
  Performs simultaneous substitution of Vars by Terms
   */
  def substitute(t:Term, subst:Map[Var, Term]):Term = t match {
    case v:Var => subst.getOrElse(v, v)
    case Function(name, children) => Function(name, children.map(substitute(_, subst)))
  }
  //We don't need substitution in Formulas, which conveniently avoid having to implement capture avoiding substitution.

  /*
  Make sure that all bound variables are uniquely named, and with names different from free variables.
  This will simplify a lot future transformations. The specific renaming can be arbitrary
   */
  def makeVariableNamesUnique(f:Formula):Formula = {
    var i:Int = 0
    def mVNUForm(f:Formula, subst:Map[Var, Var]) :Formula= {
      f match {
        case Predicate(name, children) => Predicate(name, children.map(t => substitute(t, subst)))
        case And(children) => And(children.map(s => mVNUForm(s, subst)))
        case Or(children) => Or(children.map(s => mVNUForm(s, subst)))
        case Implies(left, right) => Implies(mVNUForm(left, subst), mVNUForm(right, subst))
        case Neg(inner) => Neg(mVNUForm(inner, subst))
        case Forall(variable, inner) =>
          val nvar = "x"+i
          val np = (Var(variable), Var(nvar))
          val t = mVNUForm(inner, subst+np )
          i+=1
          Forall(nvar, t)
        case Exists(variable, inner) =>
          val nvar = "x"+i
          val t = mVNUForm(inner, subst + ((Var(variable), Var(nvar))) )
          i+=1
          Exists(nvar, t)
      }
    }
    val alreadyTaken = freeVariables(f).zipWithIndex.map(p => (p._1, Var("x"+p._2)))
    i = alreadyTaken.size
    mVNUForm(f, alreadyTaken.toMap)
  }

  /*
  Put the formula in negation normal form.
   */
  def negationNormalForm(f:Formula):Formula = {
    ???
  }

  /*
  Put the formula in negation normal form and then eliminates existential quantifiers using Skolemization
   */
  def skolemizationNegation(f: Formula):Formula = {
    ???
  }

  /*
  Return the matrix of f in negation normal, skolemized form and make sure variables are uniquely named. Since there are no existential
  quantifiers and all variable names are unique, the matrix is equivalent to the whole formula.
   */
  def prenexSkolemizationNegation(f:Formula):Formula = {
    ???
  }

  type Clause = List[Formula]

  /*
  This may exponentially blowup the size in the formula in general.
  If we only preserve satisfiability, we can avoid it by introducing fresh predicates, but that is not asked here.
   */
  def conjunctionPrenexSkolemizationNegation(f:Formula):List[Clause] = {
    ???
  }
  /*
  A clause in a proof is either assumed, i.e. it is part of the initial formula, or it is deduced from previous clauses.
  A proof is written in a specific order, and a justification should not refer to a previous step.
   */
  sealed abstract class Justification
  case object Assumed extends Justification
  case class Deduced(premices: (Int, Int), subst: Map[Var, Term]) extends Justification

  type ResolutionProof = List[(Clause, Justification)]

  /*
  Verify if a given proof is correct. The function should verify that every clause is correctly justified (unless assumed).

   */
  def checkResolutionProof(proof:ResolutionProof):Boolean = {
    ???
  }

  val a = Function("a", Nil)
  val b = Function("b", Nil)
  val c = Function("c", Nil)
  val x = Var("x")
  val y = Var("y")
  def lives(a:Term) = Predicate("lives", List(a))
  def killed(a:Term, b:Term) = Predicate("killed", List(a, b))
  def hates(a:Term, b:Term) = Predicate("hates", List(a, b))
  def richer(a:Term, b:Term) = Predicate("richer", List(a, b))
  def eq(a:Term, b:Term) = Predicate("=", List(a, b))

  val mansionMystery: Formula = And(List(
    Exists("x", And( List(Predicate("lives", List(x)), Predicate("killed", List(x, a))) )),
    And(List(lives(a), lives(b), lives(c), Forall("x", Implies(lives(x), Or(List(eq(x, a), eq(x, b), eq(x, c))))))),
    Forall("x", Forall("y", Implies(killed(x, y), And(List( hates(x,y), Neg(richer(x,y)) )) ))),
    Forall("x", Implies(hates(a,x), Neg(hates(c,x)))),
    Forall("x", Implies(hates(a,x), Neg(eq(x,b)))),
    Forall("x", Implies(Neg(eq(x,b)), hates(a,x))),
    Forall("x", Implies(hates(a,x), Neg(eq(x,b)))),
    Forall("x", Implies(hates(b,x), Neg(richer(x,a)))),
    Forall("x", Implies(Neg(richer(x,a)), hates(b,x))),
    Forall("x", Implies(Neg(hates(a, x)), hates(b,x))),
    Neg(Exists("x", Forall("y", hates(x,y)))),
    Neg(eq(a,b))
  ))

  /*
  To show that a formula phi is valid, we show that it's negation is unsatisfiable, i.e. !phi -> false.
  Hence if a Proof contains an empty clause, then the negation of the conjunction of all assumed clauses has to be valid
   */
  def extractTheorem(proof: ResolutionProof):Formula = {
    if (proof.contains(Nil)) Neg(And(proof.filter(_._2 match {
      case Assumed => true
      case Deduced(premices, subst) =>false
    }).map(_._1).map(Or)))
    else throw new Exception("The proof did not succeed")
  }



}
