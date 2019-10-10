package com.phylage.scaladia.json

object Hoge extends App with JTransform {
  val x = s"""{"value":"3}""".jsonTree
  val a = x
  println("complete")
}
