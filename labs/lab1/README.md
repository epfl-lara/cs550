# Lab 1: Working with Stainless

This first lab will be comprised of multiple parts, designed to help you get
familiar and proficient with the Stainless verification tool for Scala.

---

## Setup

### Java
In order to run stainless, as well as the programs you will verify, you will
need **Java 17**.

You can check your Java version using 
```shell
$ java -version
openjdk version "17.0.9" 2023-10-17
OpenJDK Runtime Environment Temurin-17.0.9+9 (build 17.0.9+9)
OpenJDK 64-Bit Server VM Temurin-17.0.9+9 (build 17.0.9+9, mixed mode, sharing)
$ javac -version
javac 17.0.9
```
The exact version might vary, but the major version should be 17.

If you have multiple JDK versions installed, some tools exist to change your
"active" version. For example:
- **On Debian-based systems:** `update-alternatives --config java`, and
- **On [Arch-based](https://wiki.archlinux.org/title/Java#Switching_between_JVM)
  systems:** `archlinux-java set java-17-openjdk`.

### Scala
You will also need a way to compile Scala programs.

We recommend following the standard [Scala install
instructions](https://docs.scala-lang.org/getting-started/install-scala.html) to
obtain the `scala` compiler, as well as the command line utilities `scala-cli`,
and `sbt`. Once you're done, test your installation with:

You can test your installation with
```shell
$ scala -version
Scala code runner version: 1.16.0
Scala version (default): 3.7.2
$ scala-cli -version
Scala CLI version: 1.17.0
Scala version (default): 3.9.0
$ sbt -version
sbt runner version: 2.0.8
```

#### Troubleshooting

If the commands don't work after having restarted your terminal, check that the
`coursier` path (which is printed in an interactive step of the scala install
script) is registered to your system's `$PATH`. If it's not, register it in your
`.bashrc`/`.zshrc` file and try again!

### Stainless

Follow the instructions on the [Stainless installation
page](https://epfl-lara.github.io/stainless/installation.html) to install
Stainless. Installing an additional solver such as z3 or cvc5 is highly
recommended (instructions on same page).

You should be able to run Stainless to get the following output
```shell
$ stainless --version
[  Info  ] Stainless verification tool (https://github.com/epfl-lara/stainless)
[  Info  ]   Version: 0.10.2-8-gbc5358c
[  Info  ]   Built at: 2026-09-10 14:40:35.463+0200
[  Info  ]   Stainless Scala version: 3.10.1-RC1-bin-20260903-e1f9361-NIGHTLY
[  Info  ] Inox solver (https://github.com/epfl-lara/inox)
[  Info  ] Version: 1.1.5-244-g37db954
[  Info  ] Bundled Scala compiler: 3.10.1-RC1-bin-20260903-e1f9361-NIGHTLY
```

---

## Part 0 - Follow the Stainless tutorial!

You can find an introduction to the syntax and behavior of Stainless at [this
webpage](https://epfl-lara.github.io/stainless/tutorial.html). You can find a
minimal scaffold in `src/Tutorial.scala`: follow the tutorial to fill it out and
discover how to prove program properties in Stainless!

## Part I - Introduction to Stainless

For the first part of this lab, you'll test your Stainless installation by
proving some properties of data structure, and you'll train your logical mind by
implementing a few functions on formulas of propositional logic. You can find
the scaffold in `src/part1`, and the handout in the [relevant `.md`
file](./Part1.md).

## Part II - Maths and Programs in Stainlessland

For the second part of this lab, you are going to go deeper down the Stainless
rabbit hole, following two main paths:

- the mathematical side, where you will implement mathematical procedures on
  signed integers;
- the program-correctness side, where you will be tasked with showing the
  correctness of the implementation of a simple communication protocol.

Through your journey, you will discover that these two "sides" are actually more
similar than you'd think...

You can find the scaffold in `src/part2` and the handout in the [relevant `.md`
file](./Part2.md).
