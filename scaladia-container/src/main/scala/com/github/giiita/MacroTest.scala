package com.github.giiita

import com.github.giiita.injector.Injector
import com.github.giiita.testpack.TTT.{MOMO, MUNE}
import com.github.giiita.testpack.deeper.MMMMMM

object MacroTest extends App with Injector {
  println(
    inject[MOMO].test
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



  narrow[MOMO](new MOMO {
    override def test = "NARROWS"
  }).accept[App].indexing()

  println(
    inject[MOMO @@ MMMMMM].test
  )
}
