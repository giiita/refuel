package com.phylage.scaladia

import scala.reflect.runtime.universe

object Main extends App {
  val from = System.currentTimeMillis()
  val mirror = universe.runtimeMirror(getClass.getClassLoader)
  mirror.reflectModule(mirror.staticModule("com.phylage.scaladia.X$")).instance.asInstanceOf[X].test
  println(s"TIME : ${System.currentTimeMillis() - from}")
}

trait X {
  def test = println("HOGE")
}
object X extends X