# Lab 2: Working with Lean

The second lab will help you discover and learn the ins and outs of the Lean
interactive theorem prover. This lab will, again, be comprised of multiple
parts, to be released progressively over the course of the coming weeks. 

---

## Setup

Installing Lean is a piece of cake: simply use the Lean extension for the editor
of your choosing ([VSCode/VSCodium][vsc], [Neovim][nvim], and others)! It will
read the Lean version for this project from the [`lean-toolchain`][ltc] file we
provide, and install it. This lab will require **Lean 4.34.1**, as well as some
optional dependencies that will be setup for each part.

[vsc]: https://github.com/leanprover/vscode-lean4
[nvim]: https://github.com/Julian/lean.nvim
[ltc]: ./lean-toolchain

## To work!

Your task will be to complete the following parts. This list is inexhaustive for
now, as more parts will be released soon, but they will be either disjoint or
linearly dependent, so you can start working as soon as you wish to.

- [Part 1](./part1/): Working with QBF formulas, and proving your work.
- And more!

### Resources

- Last week's lecture slots introduced Lean and interactive theorem proving! You
  can find the related material [here][l5] and [here][l6].
- You can find a wealth of [material about learning Lean][refs] online, but
  other than the [language reference][langref], they might be too in-depth. The
  reference entry on [tactics][tac] might be interesting when in doubt about
  what a tactic can (and can't!) do.
- If you want to play around on simple definitions, you can create (but not
  submit!) your own `.lean` file, and your editor should check it as normal. You
  can use `#check` and `#eval` directives to inspect your definitions.
- As always, if you have any questions, feel free to ask us during the exercise
  slots, or on Edstem!

[refs]: https://lean-lang.org/learn
[langref]: https://lean-lang.org/doc/reference/latest
[tac]: https://lean-lang.org/doc/reference/latest/Tactic-Proofs/Tactic-Reference
[l5]: ../../lectures/lecture05/README.md
[l6]: ../../lectures/lecture06/README.md

## Checking progress

The Lean theorem prover enjoys great editor support for the most common setups.
If you are using the suggested extensions, simply open the files you are working
on and you should see yellow markings for `sorry`-ed definitions, and blue
checkmarks or other completion indicators for theorems you've proven.

If you prefer to use the command line, you can run the following commands from
the root of the course repository:

```shell
cd labs/lab2
lean --version
lean path/to/file.lean
```

The starting file should have **no errors**, but expected warnings of 
`decalaration uses sorry`. After completing the exercises, the files should
check without errors and without `sorry` warnings.

## Submission

As previously announced, this lab is to be completed **alone**. Instructions for
the submission will be made available on [Moodle][mood] in due time, along with an
automatic grading setup to help you check your work.

[mood]: https://moodle.epfl.ch/course/view.php?id=13051