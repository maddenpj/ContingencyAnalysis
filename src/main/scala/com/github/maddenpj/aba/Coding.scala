package com.github.maddenpj.aba
import Response._

import argonaut._, Argonaut._


sealed trait Coding {
  def code: Int
  def value: String
  // def createEvent[B <: Coding](other: B, res: Response): Event[this.type, B]
}

trait HasCodec[T <: Coding] {
  def codec: CodecJson[T]
}

case class Antecedent (code: Int, value: String) extends Coding {
  // def createEvent(other: Coding, res: Response) = other match {
  //   case b: Behavior => ABEvent(this, b, res)
  //   case c: Consequence => ACEvent(this, c, res)
  // }
}

case class Behavior (code: Int, value: String) extends Coding {
  // def createEvent[B <: Coding](other: B, res: Response) = other match {
  //   case c: Consequence => ACEvent(this, other, res)
  // }
}

case class Consequence (code: Int, value: String) extends Coding {
}


object Coding {
  // import Coding._
  implicit object antecedentCodec extends HasCodec[Antecedent] {
    def codec = casecodec2(Antecedent.apply, Antecedent.unapply)("code", "value")
  }
  implicit object behaviorCodec extends HasCodec[Behavior] {
    def codec = casecodec2(Behavior.apply, Behavior.unapply)("code", "value")
  }
  implicit object consequenceCodec extends HasCodec[Consequence] {
    def codec = casecodec2(Consequence.apply, Consequence.unapply)("code", "value")
  }


  // Why can I never get subtypes...
  // def fromFile(tpe: String, path: String) = {
  // def fromFile[T <: Coding](tpe: String, path: String): Option[List[T]] = {
  // def fromFile[_ <: Coding](tpe: String, path: String): Option[List[_]] = {
  def betterFromFile[T <: Coding : HasCodec](path: String): Map[Int, T] = {
    val json = scala.io.Source.fromFile(path).getLines.toList

    val p = json.flatMap { line =>
      Parse.decodeOption[T](line)(implicitly[HasCodec[T]].codec)
    }
    p.groupBy(_.code).mapValues(_.head)
    // json.decodeOption[List[T]].map(_.groupBy(_.code).mapValues(_.head))
  }


  // def fromFile(tpe: String, path: String): Option[Map[Int, Coding]] = {
  //   val json = scala.io.Source.fromFile(path).mkString
  //   val list = tpe.toLowerCase match {
  //     case "antecedent" => json.decodeOption[List[Antecedent]]
  //     case "behavior" => json.decodeOption[List[Behavior]]
  //     case "consequence" => json.decodeOption[List[Consequence]]
  //     case _ => None
  //   }

  //   list.map { l =>
  //     val grouped = l.groupBy(_.code)
  //     require(grouped.values.forall(_.length == 1))

  //     grouped.mapValues(_.head)
  //   }
  // }
}

// class CodeSet {
//   def mapping: Set[Coding]
// }

// implicit def codec = casecodec2(this.apply, this.unapply)("code", "value")
// import scala.reflect._
// implicit def jsonCodec[T <: Coding : ClassTag] = {
//   def unap[T](t: T): Option[(Int, String)] = Some((t.code, t.value))
//   val ap: (Int, String) => T = implicitly[ClassTag[T]].runtimeClass.newInstance _
//   casecodec2[Int, String, T](ap, unap _)("code", "value")
// }

// trait CodingFactory {
//   type T <: Coding
//   def apply(x: Int, s: String): T
//   def unapply(t: T): Some((Int, String))
// }
// trait CodingParse[A] {
//   this: CodingFactory =>
//   implicit def codec: CodecJson[A] = casecodec2(this.apply, this.unapply)("code", "value")
// }
// object Antecedent extends CodingParse[Antecedent] {

// jsonString.decodeOption[List[Antecedent]]
