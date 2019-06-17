package com.github.giiita.injector

import com.github.giiita.injector.Hoge.TagFactory

object MacroTest extends App {
  println(
    injector.exe[TagFactory]("HUGA")
  )




}
