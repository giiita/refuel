package com.phylage.scaladia.http.io

import com.phylage.scaladia.container.RuntimeInjectionPool
import com.phylage.scaladia.lang.RuntimeTZ

object Main extends App {
  val from = System.currentTimeMillis()
  RuntimeInjectionPool.collect[RuntimeTZ].foreach(println)
  println(s"TIME : ${System.currentTimeMillis() - from}")
}