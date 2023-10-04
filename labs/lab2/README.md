# Lab 2: Simple communication protocol in Stainless

In this second lab, you will use stainless to verify a simple communication protocol. As we will see, Stainless also allows us to prove properties on traits and classes, which are at the core of every Scala program.

By now you should already have some familiarities with Stainless. You can nonetheless find useful ressources in the handout of [Lab 1](../../lab2/README.md#Tutorial) or on [Stainless repository](https://github.com/epfl-lara/stainless).


### Getting the source

To start working on this lab, you can either clone this entire repository, or download the present directory alone from Gitlab (there should be a button for this on the top right of the web interface).

### The protocol

The communication protocol we will prove properties about involves two `Endpoint`s that both hold a sending and a receiving buffer. They contain respetively the messages that still need to be sent, and the ones already received. The messages go through a `Network` that can be queried to know whether they have been transmitted. The protocol is then defined as follow:

1. The sender sends a message over the network.
2. It then queries the network to know whether the message has been transmitted.
3. If this is the case, drop the last message sent from the buffer of the sender and add it to the buffer of the receiver. Otherwise skip to step 4.
4. Repeat step 1 with the updated sender and receiver.

In practice this means a message will be sent over and over until it has been transmitted. Since we want to reason about finite programs, we will run the protocol for a finite number of iterations. The method simulating the protocol is `Network.messageExchange`.

### Goal of the lab

The protocol, in addition to the classes representing the `Network` and the `Endpoint`s of the communication, are already implemented. The goal of the lab is proving some properties of the protocol such as its correctness, optimality conditions etc...

As a reminder, these properties are stated in the form of functions which "do nothing": they return `Unit` and have no effects.
You have to fill these functions with a proof of their statement. The file contains seven properties on `messageExchange` that you have to prove. Some proofs may be shorter than others but all of them can be written in less than 7 lines of code.

You are not allowed to change the definitions given in the file or the statement (parameters/return types, function name, requirements, conclusion, ...) of any of the properties. Even though this is not required, you may define new lemmas, but you need to prove them correct as well.


To check your proofs, use
```shell
# On Linux and WSL
> stainless SimpleProtocol.scala
# On Mac
> stainless --solvers=smt-z3 SimpleProtocol.scala
``` 


The provided configuration file ([stainless.conf](stainless.conf)) will automatically set the SMT solver's timeout to 2 seconds. You can also pass other options by default such as `--compact` (only displaying VCs Stainless was not able to prove) or `--watch` by adding respectively  `compact=true` and `watch=true` as new lines in the configuration file.





### Submission

Once you've completed all proofs, you can submit your [SimpleProtocol.scala](SimpleProtocol.scala) file on [Moodle](https://moodle.epfl.ch/mod/assign/view.php?id=1169500). Only one member of each group should submit a solution. 
