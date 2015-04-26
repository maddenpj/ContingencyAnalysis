package com.github.maddenpj.aba


object EventAnalysis {

  def single[A <: Coding, B <: Coding, T <: Coding]
    (xs: List[Event[A, B]])
    (f: Event[A, B] => T): Map[T, Int] = {
      xs.groupBy(f(_)).mapValues(_.length)
    }

  def double[A <: Coding, B <: Coding, T <: Coding, U <: Coding]
    (xs: List[Event[A, B]])
    (f1: Event[A, B] => T, f2: Event[A, B] => U): Map[T, Map[U, Int]] = {
      xs.groupBy(f1(_)).mapValues(x => single(x)(f2))
    }

}
