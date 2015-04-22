package com.github.maddenpj.aba


object Response extends Enumeration {
  type Response = Value
  val Stopped, Escalate, Continue, OnTask = Value
}
import Response._

trait Event[A, B] {
  def a: A
  def b: B
  def response: Response
}

case class ABEvent(
  a: Antecedent,
  b: Behavior,
  response: Response
) extends Event[Antecedent, Behavior]

case class ACEvent(
  a: Antecedent,
  b: Consequence,
  response: Response
) extends Event[Antecedent, Consequence]

case class BCEvent(
  a: Behavior,
  b: Consequence,
  response: Response
) extends Event[Behavior, Consequence]


object Event {
  def fromTextFile(path: String): List[Option[(Int, Int, String)]] = {
    val tuples = scala.io.Source.fromFile(path).getLines.map { l =>
      l.trim.split("\\s+") match {
        case Array(c1, c2, res, _*) => Some((c1.toInt, c2.toInt, res))
        case _ => None
      }
    }

    tuples.toList
  }
}
