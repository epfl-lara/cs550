//> using jar "stainless-library_2.13-0.9.8.jar"
//> using scala "3.2.0"

import stainless.lang.*
import stainless.annotation.*
import stainless.collection.*


trait Message

/**
  * Network that can randomly loose packet.
  * 
  * When a message is sent over the network, one can query the hasSent
  * method to know whether it has been transmitted.
  * In order to be non-deterministic, hasSent relies on a seed. In the rest
  * of this lab, we will assume that the seed is the time.
  * 
  * We also  define two special types of networks:
  *  - noLossNetwork: networks that do not loose packets
  *  - badButPredictableNetwork: networks that transmits a packet every n
  *    iteration and drop it otherwise
  */
trait Network {
  def hasSent(message: Message, seed: BigInt): Boolean

  /**
    * Simulate iter iterations of a communication between a sender and a receiver.
    * At each iteration, the sender sends a message according to the protocol.
    * At the end of the process returns the state of the sender and the receiver
    * (i.e. the messages the sender still needs to transmit and the ones the receiver
    *  received)
    */
  final def messageExchange(sender: Endpoint, receiver: Endpoint, iter: BigInt): (Endpoint, Endpoint) =
    decreases(iter)
    require(iter >= 0)

    //we first check if we are still iterating and if we still have messages to transmit
    if sender.msgQueued && iter > 0 then
      val m = sender.nextMsg

      //we send the message and query the network to know whether it was transmitted
      // - if it's the case then we focus on the next message
      // - otherwise we reiterate the process
      if hasSent(m, iter) then
        messageExchange(sender.updated, receiver.receive(m), iter - 1)
      else
        messageExchange(sender, receiver, iter - 1)
    else (sender, receiver)

}

object Network{

  case object noLossNetwork extends Network {
    override def hasSent(message: Message, seed: BigInt): Boolean = true
  }

  case object fullLossNetwork extends Network {
    override def hasSent(message: Message, seed: BigInt): Boolean = false
  }

  case class badButPredictableNetwork(n: BigInt) extends Network {
    require(n > 0)
    override def hasSent(message: Message, seed: BigInt): Boolean = seed % n == 0
  }
}


/**
  * Communicating party.
  * 
  * @param toSend list of messages left to transmit.
  * @param received list of messages received up to this point.
  */
case class Endpoint(toSend: List[Message], received: List[Message]) {

  /**
    * Returns the next message to transmit.
    */
  def nextMsg: Message = 
    require(msgQueued)
    toSend.head

  /**
    * If the last message has been transmitted and there are still messages to transmit,
    * call this method to go to the next message.
    */
  def updated: Endpoint = 
    require(msgQueued)
    Endpoint(toSend.tail, received)

  /**
    * Returns whether there are still messages left to transmit to the receiver.
    */
  def msgQueued: Boolean = !toSend.isEmpty
  
  /**
    * Adds a new message to the list of received messages.
    */
  def receive(m: Message): Endpoint = Endpoint(toSend, received :+ m)
}


@ghost
object NetworkProperties {
  import Network.*
  import Helpers.*


  /**
    * Returns whether the receiver has received all the messages the sender wanted to transmit.
    */
  def receivedAllMsgCorrectly(sender: Endpoint, receiverBeforeExchange: Endpoint, receiverAfterExchange: Endpoint): Boolean =
    (receiverBeforeExchange.received ++ sender.toSend) == receiverAfterExchange.received


  /**
    * Correctness of the protocol: that is, if the sender has no more message to send then it means the receiver
    * received all of them.
    * 
    * HINTS: You may want to use 
    *  - the fact that Stainless automatically proves that l :+ e == l ++ Cons(e, Nil())
    *  - theorems of the standard library:
    *    https://github.com/epfl-lara/stainless/blob/64a09dbc58d0a41e49e7dffbbd44b234c4d2da59/frontends/library/stainless/collection/ListSpecs.scala
    */
  def messageExchangeCorrectness(network: Network, sender: Endpoint, receiver: Endpoint, iter: BigInt): Unit = {
    decreases(iter)
    require(iter >= 0)
    require(!network.messageExchange(sender, receiver, iter)._1.msgQueued)

    /* TODO: Prove me */

  }.ensuring(receivedAllMsgCorrectly(sender, receiver, network.messageExchange(sender, receiver, iter)._2))

  /**
    * For any network, if the receiver has received all the messages transmitted by the sender, then the number
    * of iteration of the protocol is at least the number of messages.
    * 

    */
  def messageExchangeLowerBound(network: Network, sender: Endpoint, receiver: Endpoint, iter: BigInt): Unit = {
    decreases(iter)
    require(iter >= 0)
    require(receivedAllMsgCorrectly(sender, receiver, network.messageExchange(sender, receiver, iter)._2))

    /* TODO: Prove me */

  }.ensuring(iter >= sender.toSend.size)


  /**
    * If the network does not loose any packet, then the number of iteration of the protocol is at most
    * the number of messages.
    */
  def messageExchangeWithNoLoss(sender: Endpoint, receiver: Endpoint, iter: BigInt): Unit = {
    decreases(iter)
    require(iter >= sender.toSend.size)

    /* TODO: Prove me */

  }.ensuring(
    receivedAllMsgCorrectly(sender, receiver, noLossNetwork.messageExchange(sender, receiver, iter)._2)
  )

  /**
  * Any network is less efficient then a network that does not loose any packets.
  */
  def noLossNetworkIsOptimal(sender: Endpoint, receiver: Endpoint, iter: BigInt, network: Network): Unit = {
    require(iter >= 0)
    require(receivedAllMsgCorrectly(sender, receiver, network.messageExchange(sender, receiver, iter)._2))

    /* TODO: Prove me */

  }.ensuring(receivedAllMsgCorrectly(sender, receiver, noLossNetwork.messageExchange(sender, receiver, iter)._2)) 

  /**
  * If a network does not transmit any packet, then no matter how many iterations of the protocol one runs, the 
  * result will always be the same.
  */
  def messageExchangeWithFullLosses(sender: Endpoint, receiver: Endpoint, iter: BigInt): Unit = {
    decreases(iter)
    require(iter >= 0)

    /* TODO: Prove me */

  }.ensuring(
    fullLossNetwork.messageExchange(sender, receiver, iter)
      ==
    (sender, receiver)
  )

  /**
    * If a network does not transmit any packet, then the receiver has received all messages if only if 
    * the sender had none to send.
    */
  def receivedAllMsgCorrectlyFullLosses(sender: Endpoint, receiver: Endpoint, iter: BigInt): Unit = {
    decreases(iter)
    require(iter >= 0)
    require(receivedAllMsgCorrectly(sender, receiver, fullLossNetwork.messageExchange(sender, receiver, iter)._2))

    /* TODO: Prove me */

  }.ensuring(!sender.msgQueued)



  /**
   * When dealing with a [[badButPredictableNetwork]], if the number of iterations of the protocol is
   * not a multiple of n, then it can be reduced without changing the result.
   * 
   * HINT: You may want to use [[modMinusOne]] from the Helpers object at the end of the file.
   */
  def messageExchangeBadNetwork(sender: Endpoint, receiver: Endpoint, iter: BigInt, n: BigInt): Unit = {
    decreases(iter)
    require(iter >= 0)
    require(n > 0)

    /* TODO: Prove me */

  }.ensuring(
    badButPredictableNetwork(n).messageExchange(sender, receiver, iter)
     ==
    badButPredictableNetwork(n).messageExchange(sender, receiver, iter - (iter % n))
  )

}

object Helpers {
  def modMinusOne(a: BigInt, n: BigInt): Unit = {
    require(a >= 0)
    require(n > 0)
    require(a % n != 0)
    assert(a / n == (a - 1) / n)
  }.ensuring((a - 1) % n == (a % n) - 1)
}