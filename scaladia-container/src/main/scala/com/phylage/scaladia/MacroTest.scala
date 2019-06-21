package com.phylage.scaladia

import com.phylage.scaladia.injector.Injector
import com.phylage.scaladia.injector.Injector.@@
import com.phylage.scaladia.testpack.TTT.{MOMO, MUNE}
import com.phylage.scaladia.testpack.deeper.MMMMMM

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

  val xx = inject[MOMO @@ MMMMMM]
  println("星星星星星星星星星星星星星星星星星星星星星星星星星星星")
  println(
    xx.test
  )
  println(
    xx.test
  )
}
