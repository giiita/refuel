package com.github.giiita.injector

object Hoge {
  trait TagFactory {
    var tag: scala.collection.mutable.Seq[String] = scala.collection.mutable.Seq.empty

    def add(value: String): scala.collection.mutable.Seq[String] = {
      tag.:+(value)
    }
  }

  type @@[+T, +U]     = T with Seq[U]

  case class Basket(blueberry: TagFactory @@ String)
  implicit case object TagFactory extends TagFactory
  case class HOGEHOGEFactory() extends TagFactory
}
