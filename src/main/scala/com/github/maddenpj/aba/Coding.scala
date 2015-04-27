package com.github.maddenpj.aba
import Response._

import argonaut._, Argonaut._


sealed trait Coding {
  def code: Int
  def value: String

  def typeName: String
  override def toString = f"$value  - $typeName[$code%2d]"
  // override def toString = f"$typeName[$code%2d]"
}

trait HasCodec[T <: Coding] {
  def codec: CodecJson[T]
}

case class Antecedent (code: Int, value: String) extends Coding {
  def typeName = "A"
}

case class Behavior (code: Int, value: String) extends Coding {
  def typeName = "B"
}

case class Consequence (code: Int, value: String) extends Coding {
  def typeName = "C"
}


object Coding {
  implicit object antecedentCodec extends HasCodec[Antecedent] {
    def codec = casecodec2(Antecedent.apply, Antecedent.unapply)("code", "value")
  }
  implicit object behaviorCodec extends HasCodec[Behavior] {
    def codec = casecodec2(Behavior.apply, Behavior.unapply)("code", "value")
  }
  implicit object consequenceCodec extends HasCodec[Consequence] {
    def codec = casecodec2(Consequence.apply, Consequence.unapply)("code", "value")
  }

  def fromFile[T <: Coding : HasCodec](path: String): Map[Int, T] = {
    val json = scala.io.Source.fromFile(path).getLines.toList

    val p = json.flatMap { line =>
      Parse.decodeOption[T](line)(implicitly[HasCodec[T]].codec)
    }
    p.groupBy(_.code).mapValues(_.head)
  }
}
