package com.github.maddenpj.aba

import argonaut._, Argonaut._

import Response._

// Map[Set[Int] -> Coding]
// Set of codes -> New Coding. Should have list of old codings -> new coding

// trait CodeGroup {
//   type Type <: Coding
//   def code: Type
//   def mapping: Set[Type]
// }


case class CodeGroup[A](code: A, mapping: Set[A]) {
  // type Type = A
  def apply(a: A) = if(mapping(a)) code else a
}

object CodeGroup {
  import Coding._

  implicit def codec[A <: Coding : HasCodec] = {
    implicit val aCodec = implicitly[HasCodec[A]].codec
    CodecJson(
      (g: CodeGroup[A]) => ("code" := g.code) ->: ("mappings" := g.mapping) ->: jEmptyObject,
      c => for { code <- (c --\ "code").as[A] ; map <- (c --\ "mappings").as[List[A]] } yield CodeGroup(code, map.toSet)
    )
  }

  def fromJson[A <: Coding : HasCodec](json: String): Option[CodeGroup[A]] =
    Parse.decodeOption[CodeGroup[A]](json)

}
