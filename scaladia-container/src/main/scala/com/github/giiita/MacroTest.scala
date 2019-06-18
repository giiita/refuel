package com.github.giiita

import com.github.giiita.injector.Injector
import com.github.giiita.testpack.TTT.MOMO

object MacroTest extends App with Injector {
  println(
    inject[MOMO].provide
  )














}
