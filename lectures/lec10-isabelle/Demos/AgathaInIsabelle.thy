theory AgathaInIsabelle
imports Main
begin

theorem agatha:
assumes a1 : "EX  x. (lives x & killed x  a)"
and     a2 : "lives a & lives b & lives c & (ALL x. (lives x --> (x=a | x=b | x=c)))"
and     a3 : "ALL x. ALL y. killed x y --> (hates x y & ~richer x y)"
and     a4 : "ALL x. (hates a x --> ~hates c x)"
and     a5 : "ALL x. (hates a x = (x ~= b))"
and     a6 : "ALL x. ((~richer x a) = hates b x)"
and     a7 : "ALL x. (hates a x --> hates b x)"
and     a8 : "~(EX x. ALL y. hates x y)"
and     a9 : "a ~= b"
shows   "killed a a"
(* use sledgehammer to find suggestion *)

thm "agatha"

end