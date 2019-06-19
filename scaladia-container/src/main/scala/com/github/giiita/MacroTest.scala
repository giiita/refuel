package com.github.giiita

import com.github.giiita.injector.Injector
import com.github.giiita.testpack.TTT.{MOMO, MUNE}

object MacroTest extends App with Injector {
  println(
    inject[MOMO].provide
  )

  println(
    inject[MUNE].test
  )

  println(
    inject[MOMO].test
  )

  println(
    inject[MUNE].test
  )
}
