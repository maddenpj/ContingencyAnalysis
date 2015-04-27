package com.github.maddenpj.aba.analysis


object EventAnalysis {

  def single[A, B, T]
  (xs: List[Event[A, B]])
  (f: Event[A, B] => T)
  : Map[T, Int] = {
    xs.groupBy(f(_)).mapValues(_.length)
  }

  def double[A, B, T, U]
  (xs: List[Event[A, B]])
  (f1: Event[A, B] => T, f2: Event[A, B] => U)
  : Map[T, Map[U, Int]] = {
    xs.groupBy(f1(_)).mapValues(x => single(x)(f2))
  }

}
