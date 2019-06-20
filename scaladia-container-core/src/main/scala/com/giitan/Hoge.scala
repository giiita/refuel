package com.giitan

import com.giitan.injector.Injector

object Hoge extends App with Injector {
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
}