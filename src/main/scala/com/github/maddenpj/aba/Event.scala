package com.github.maddenpj.aba


object Response extends Enumeration {
  type Response = Value
  val Stopped, Escalate, Continue = Value
  val OnTask = Value("On task")
}
import Response._

sealed trait Event[A, B] {
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
  // def apply(a: Antecedent, b: Behavior, res: Response)    = ABEvent(a, b, res)
  // def apply(a: Antecedent, b: Consequence, res: Response) = ACEvent(a, b, res)
  // def apply(a: Behavior, b: Consequence, res: Response)   = BCEvent(a, b, res)
  // def  = ev match {
  //   case 

  def fromTextFile[E](path: String)(fn: ((Int, Int, Response)) => E): List[E] = {
    val tuples = scala.io.Source.fromFile(path).getLines.flatMap { l =>
      l.trim.split("\\s+") match {
        case Array(c1, c2, "Stopped", _*) => Some((c1.toInt, c2.toInt, Stopped))
        case Array(c1, c2, "Continue", _*) => Some((c1.toInt, c2.toInt, Continue))
        case Array(c1, c2, "Escalate", _*) => Some((c1.toInt, c2.toInt, Escalate))
        case Array(c1, c2, "On", "task", _*) => Some((c1.toInt, c2.toInt, OnTask))
        case _ => None
      }
    }
    tuples.map(fn).toList
  }

  // def betterFromFile[A <: Event, B <: Event](aCoding: CodeMap, bCoding: CodeMap, path: String) = {
  // def betterFromFile[A <: Coding, B <: Coding](
  //   aCoding: Map[Int, A],
  //   bCoding: Map[Int, B],
  //   path: String
  // ) = {
  //   Event.fromTextFile(path).flatten.map { case (a, b, res) =>
  //     apply(aCoding(a), bCoding(b), res)
  //   }
  // }

}
