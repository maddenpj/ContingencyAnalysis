package com.github.maddenpj.aba

package object aba {
  type CodeMap = Map[Int, Coding]

  case class CodeSet(
    a: Map[Int, Antecedent],
    b: Map[Int, Behavior],
    c: Map[Int, Consequence]
  )

  object Testing {
    val aCoding = Coding.fromFile[Antecedent]("sample/antecedent.json")
    val bCoding = Coding.fromFile[Behavior]("sample/behavior.json")
    val cCoding = Coding.fromFile[Consequence]("sample/consequence.json")

    val coding = CodeSet(aCoding, bCoding, cCoding)
    val ab = Event.abFromTextFile(aCoding, bCoding, "sample/AB.txt")
    val ac = Event.acFromTextFile(aCoding, cCoding, "sample/AC.txt")
    val bc = Event.bcFromTextFile(bCoding, cCoding, "sample/BC.txt")
  }
}
