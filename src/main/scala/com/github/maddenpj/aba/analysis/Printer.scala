package com.github.maddenpj.aba

import Response._


object Printer {
  private def extraPadding = 2

  def row[T](r: Map[T, Int])(implicit cw: Int = maxWidth(r)) = {
    val fmt = "%" + cw + "d"
    r.values.map(x => fmt.format(x)).mkString("")
  }

  def rowWithHeaders[T](r: Map[T, Int])(implicit cw: Int = maxWidth(r)) = {
    val fmt = "%" + cw + "s"
    Seq(
      r.keys.map(x => fmt.format(x)).mkString(""),
      row(r)
    )
  }


  def namedRow[T](r: Map[T, Int], name: String)(implicit colWidth: Int = maxWidth(r)): String =
    name + row(r)

  def namedRowWithHeaders[T](r: Map[T, Int], name: String)(implicit colWidth: Int = maxWidth(r)): Seq[String] = {
    // val n = ("%" + colWidth + "s").format(name)
      // case Seq(h, t) => Seq(" " * n.length + h, n + t)
    rowWithHeaders(r) match {
      case Seq(h, t) => Seq(" "*name.length + h, name + t)
      case _ => Seq()
    }
  }

    // (implicit ord: Ordering[U] = scala.math.Ordering.IntOrdering)
    // (implicit ordering: Ordering[(T, Map[U, Int])])
  def table[T, U](r: Map[T, Map[U, Int]])
    (orderFn: ((T, Map[U, Int])) => Int)
    : Seq[String] = {
      val rs = r.toSeq.sortBy(orderFn)
      val nameMax = maxWidth(r.keys.toSeq)

      val (rowName, firstRow) = rs.head
      def nameFormat(n: String)  = ("%" + nameMax + "s").format(n)

      val headerSeq = rs.map(_._2.keySet).reduce(_ union _).toSeq
      def fullMap(r: Map[U, Int]) = headerSeq.map(x => x -> r.getOrElse(x, 0)).toMap

      implicit val colWidth = maxWidth(rs.flatMap(_._2.keys.toSeq))
      val headers = namedRowWithHeaders(fullMap(firstRow), nameFormat(rowName.toString))
      val rest = rs.tail.map { case (n, r) =>
        namedRow(fullMap(r), nameFormat(n.toString))
      }
      headers ++ rest
    }

  def stringOrdering: Tuple2[Any, _] => Int = x => -x._1.toString.hashCode

  def byResponse[T](res: Response, reverse: Int = -1): Tuple2[T, Map[Response, Int]] => Int =
    x => reverse * x._2.getOrElse(res, 0)

  def printTable[T, U](r: Map[T, Map[U, Int]], orderFn: ((T, Map[U, Int])) => Int = stringOrdering) =
    println(table(r)(orderFn).mkString("\n"))

  def pad(x: String, c: String = " ")(implicit p: Int = extraPadding) =
    (c * extraPadding) + x + (c * extraPadding)

  def maxWidth[T](r: Map[T, Int]): Int = maxWidth(r.keys.toSeq)
  def maxWidth[T](r: Seq[T]): Int      = extraPadding + r.toSeq.map(_.toString.length).max
}
