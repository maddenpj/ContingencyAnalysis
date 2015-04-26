package com.github.maddenpj.aba


object Response extends Enumeration {
  type Response = Value
  val Stopped, Escalate, Continue = Value
  val OnTask = Value("On task")
}
import Response._

sealed trait Event[A, B] {
  def fst: A
  def snd: B
  def response: Response
}

case class ABEvent(
  fst: Antecedent,
  snd: Behavior,
  response: Response
) extends Event[Antecedent, Behavior]

case class ACEvent(
  fst: Antecedent,
  snd: Consequence,
  response: Response
) extends Event[Antecedent, Consequence]

case class BCEvent(
  fst: Behavior,
  snd: Consequence,
  response: Response
) extends Event[Behavior, Consequence]


object Event {
  // def apply(a: Antecedent, b: Behavior, res: Response)    = ABEvent(a, b, res)
  // def apply(a: Antecedent, b: Consequence, res: Response) = ACEvent(a, b, res)
  // def apply(a: Behavior, b: Consequence, res: Response)   = BCEvent(a, b, res)
  // def  = ev match {

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
  def abFromTextFile(aCoding: Map[Int, Antecedent], bCoding: Map[Int, Behavior], path: String): List[ABEvent] =
    fromTextFile(path)(x => ABEvent(aCoding(x._1), bCoding(x._2), x._3))

  def acFromTextFile(aCoding: Map[Int, Antecedent], cCoding: Map[Int, Consequence], path: String): List[ACEvent] =
    fromTextFile(path)(x => ACEvent(aCoding(x._1), cCoding(x._2), x._3))

  def bcFromTextFile(bCoding: Map[Int, Behavior], cCoding: Map[Int, Consequence], path: String): List[BCEvent] =
    fromTextFile(path)(x => BCEvent(bCoding(x._1), cCoding(x._2), x._3))

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
