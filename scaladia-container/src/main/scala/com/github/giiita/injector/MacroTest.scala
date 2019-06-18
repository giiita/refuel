package com.github.giiita.injector

import com.github.giiita.injector.testpack.TTT.MOMO

object MacroTest extends App with Injector {
  println(
    inject[MOMO].provide
  )


}
