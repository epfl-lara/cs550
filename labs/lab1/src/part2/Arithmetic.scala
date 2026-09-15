
import GodelNumbering.*
import stainless.lang.*

object Arithmetic {

  object NatArith {

    def powMul(a: Nat, p: Nat, q: Nat): Unit = {
      ()
    }.ensuring(_ =>
        pow(pow(a, p), q) == pow(a, p * q)
      )

  }

  object ZArith {

    /* TODO: Change me */
    case class ZZ() {

      def +(other: ZZ): ZZ = {
        /* TODO: Define me */
        ???
      }

      def *(other: ZZ): ZZ = {
        /* TODO: Define me */
        ???
      }

    }

    def natFrom(n: BigInt): Nat = {
      require(n >= 0)
      decreases(n)

      if n == 0 then 
        Zero 
      else 
        Succ(natFrom(n - 1))
    }

    def zzFrom(n: BigInt): ZZ = {
      /* TODO: Define me */
      ???
    }

    def addComm(x: ZZ, y: ZZ): Unit = {
      /* TODO: Prove me */
      ()
    }.ensuring(_ =>
        x + y == y + x
      )

    def addAssoc(x: ZZ, y: ZZ, z: ZZ): Unit = {
      /* TODO: Prove me */
      ()
    }.ensuring(_ =>
        (x + y) + z == x + (y + z)
      )

    def mulComm(x: ZZ, y: ZZ): Unit = {
      /* TODO: Prove me */
      ()
    }.ensuring( _ =>
        x * y == y * x
      )

    def mulAssoc(x: ZZ, y: ZZ, z: ZZ): Unit = {
      /* TODO: Prove me */
      ()
    }.ensuring(_ =>
        (x * y) * z == x * (y * z)
      )

  }

}
