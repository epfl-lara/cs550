theory ProgramsAreRelationsLab
  imports Main

begin
section \<open> Start by writing your solution to problem 2.10 of
http://concrete-semantics.org/concrete-semantics.pdf\<close>





section \<open> See e.g. src/HOL/Relations.thy for properties of relations

type_synonym 'a rel = "('a * 'a) set"

Basic definitions of sets are in src/HOL/Set.thy
\<close>

section \<open>Relation composition and identity\<close>

thm "Id_def"

lemma "Id_on P = {(x,x)|x. x \<in> P}" by auto

lemma "r O (s O t) = (r O s) O t" by auto

lemma "r O Id = r" by auto

lemma "Id O r = r" by auto

lemma \<open>(x,x) \<in> r^*\<close> by auto

section \<open>Programs as relations. Example with two integers\<close>

datatype state = S (x_in: int) (y_in: int)

type_synonym command = "state rel"

definition incX :: command where
  "incX = {(S x y,S (x+1) y)|x y. True}"

definition incXby :: "int \<Rightarrow> command" where
  "incXby k = {(S x y,S (x+k) y)|x y. True}"

lemma "incX O incX = incXby 2"
  by (auto simp add: incX_def incXby_def)

definition incYby :: "int \<Rightarrow> command" where
  "incYby k = {(S x y,S x (y + k))|x y. True}"

lemma xySwap: "incXby dx  O  incYby dy  =  incYby dy  O  incXby dx "
  sorry

lemma doubleInc: "incXby dx1  O  incXby dx2  =  incXby (dx1 + dx2)"
  sorry

type_synonym condition = "state set"

definition AssumeC :: "condition \<Rightarrow> command" where
  "AssumeC cond = Id_on cond"

definition HavocX :: command where
  "HavocX = {(S x y, S x' y)| x x' y. True}"

definition HavocY :: command where
  "HavocY = {(S x y, S x y')| x y y'. True}"

definition IfC :: "condition \<Rightarrow> command \<Rightarrow> command \<Rightarrow> command" where
  "IfC cond s1 s2 = (AssumeC cond O s1) \<union> (AssumeC (- cond) O s2)"

lemma ifC_alt: 
  "IfC cond s1 s2 = {(s,s')|s s'. (s \<in> cond \<longrightarrow> (s,s')\<in>s1) \<and>
                                  (s \<notin> cond \<longrightarrow> (s,s')\<in>s2)}"
  sorry

definition WhileC :: "condition \<Rightarrow> command \<Rightarrow> command" where
  "WhileC b c = (AssumeC b O c)^* O AssumeC (- b)"

definition assignX :: "(state \<Rightarrow> int) \<Rightarrow> command" where
  "assignX f = {(s,s')|s s'. s' = S (f s) (y_in s)}"

lemma "assignX (\<lambda> s. x_in s + k) = incXby k" sorry

section \<open>Hoare Triple\<close>

definition triple :: "'a set \<Rightarrow> 'a rel \<Rightarrow> 'a set \<Rightarrow> bool" ("\<Turnstile> ({(1_)}/ (_)/ {(1_)})" 50)
  where
  "triple P r Q = (\<forall> x x'. (x \<in> P \<and> (x,x') \<in> r \<longrightarrow> x' \<in> Q))"

lemma "\<Turnstile> {{s. x_in s > 0}} incX {{s. x_in s > 0}}" sorry

lemma "k > 0 \<Longrightarrow>
      \<Turnstile> {{s. x_in s \<ge> 0 \<and> y_in s \<ge> 0}} 
           (incX O (incYby k))
         {{s. x_in s > 0 \<and> y_in s > 0}}" 
  sorry

lemma "\<lbrakk> \<Turnstile> {P} s1 {Q} ; \<Turnstile> {Q} s2 {R} \<rbrakk> \<Longrightarrow> 
             \<Turnstile> {P} (s1 O s2) {R}" 
  sorry

lemma "\<lbrakk> \<Turnstile> {P} c {Q} ; Q \<subseteq> Q' \<rbrakk> \<Longrightarrow> \<Turnstile> {P} c {Q'}" 
  sorry

lemma "\<lbrakk>P' \<subseteq> P ; \<Turnstile> {P} c {Q}\<rbrakk> \<Longrightarrow> \<Turnstile> {P'} c {Q}" 
  sorry

section \<open>Strongest postcondition (relation image)\<close>

definition sp :: \<open>'a set \<Rightarrow> 'a rel \<Rightarrow> 'a set\<close> where
  \<open>sp P r = {x'. \<exists> x \<in> P. (x,x') \<in> r}\<close>

lemma spPointWise: "sp P r = \<Union> {sp {s} r|s. s \<in> P}" sorry

lemma "Range {(x,y)|x y. P x y} = {y. \<exists> x. P x y}" sorry

lemma sp_via_range2: "Range ((Id_on P) O r) = sp P r"
proof -
  have "Range ((Id_on P) O r)
                 = Range ({(x,x)|x. x \<in> P} O r)" 
    by auto
  also have "... = Range ({(x,x)|x. x \<in> P} O {(x,y)|x y. (x,y) \<in> r})"
    by auto
  also have "... = Range ({(x,y)|x y. x \<in> P \<and> (x,y) \<in> r})" by auto 
  also have "... = {y. \<exists> x. x \<in> P \<and> (x,y) \<in> r}" by auto 
  also have "... = sp P r" sorry
  finally show ?thesis .
qed

thm sym

lemma sp_via_range: "sp P r = Range ((Id_on P) O r)"
  sorry

lemma "sp (sp P r1) r2 = sp P (r1 O r2)" 
  sorry

section \<open>Weakest precondition\<close>

definition wp :: "'a rel \<Rightarrow> 'a set \<Rightarrow> 'a set" where
  "wp r Q = {x. \<forall> x'. ((x,x') \<in> r \<longrightarrow> x' \<in> Q)}"

section \<open>Properties Connecting triple, wp, and sp\<close>

lemma tripleSp: "(\<Turnstile> {P} r {Q}) = (sp P r \<subseteq> Q)" sorry
lemma tripleWp: "(\<Turnstile> {P} r {Q}) = (P \<subseteq> wp r Q)" sorry

lemma sp_post: "\<Turnstile> {P} r {sp P r}" sorry
lemma sp_Strongest: "\<Turnstile> {P} r {Q} \<Longrightarrow> sp P r \<subseteq> Q" sorry

lemma wp_pre: "\<Turnstile> {wp r Q} r {Q}" sorry

lemma wp_Weakest: "\<Turnstile> {P} r {Q} \<Longrightarrow> P \<subseteq> wp r Q" sorry

lemma wp_sp_dual: "- wp r Q = sp (- Q) (r^-1)" sorry

lemma wpPointWise: "wp r Q = {s. sp {s} r \<subseteq> Q}" sorry

end