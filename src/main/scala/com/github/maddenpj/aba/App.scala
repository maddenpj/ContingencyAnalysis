package com.github.maddenpj.aba

////////////////////////////////////////////////////////////////////////////////
// ABC Analysis
// Codings, input obersavations to a code ie: "Student threw fit" => 2
//
// Observations are recorded times of antecdents behaviors consequences as
// well as student responses. Don't have original forms.
// Original data was lists of A,B,C,R datapoints
// Cartesian products were created for every AB, AC, BC pair
//
// Important - BA's are analyszing 3 term contingency of:
//  Antecedents - stimulus that happenes before a behavior
//  Behaviors - the response to the stimulus, usually challenging behavior
//  Consequence - Renforcement or punishment to behavior
//  Response - After the consequece, did the student continue doing behavior
//             or did he stop, did he react to consequence?
//
// Events - Domain model
// All ABC observations are taken down and broken into pairs + Response for
//  frequency analysis aka:
//    Antecedent/Behavior -> Response
//    Behavior/Consequence -> Response
//    Antecedent/Consequence -> Response
//
//  Frequency analysis will show which Consequences:
//    Reinforced (Increased frequency of behavior) or..
//    Punished (Decreased frequency of behavior) or..
//    Had no effect
//
//  Usually it's counter intuitive. ie: Teacher yells to stop beh. but really
//    student just wanted attention so he was reinforced and continues beh.
//
//  We also were trying to determine the *Function* of the behavior
//  The underlying root cause. Ie: Wants attention
//
//  Another trick we used was to group the codings from the specific ones
//  to more general categories, ie: condensing the codes
//    "Verbal attention from teacher"
//    "Physical attention from teacher"
//    "Verbal attention from peer"
//    ....
//  into a general: Attention category, for easier spot-checking analysis.
//
//  Wwe used this code grouping techinque many times to sort of zoom in and
//  out of the data trying to find the root cause
//
////////////////////////////////////////////////////////////////////////////////

object App {
  def test() = {
    val aCoding = Coding.fromFile[Antecedent]("sample/antecedent.json")
    val bCoding = Coding.fromFile[Behavior]("sample/behavior.json")

    Event.abFromTextFile(aCoding, bCoding, "sample/AB.txt")
  }
  def main(args: Array[String]) = {}
}
