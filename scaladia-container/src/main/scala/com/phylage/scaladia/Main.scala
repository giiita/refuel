package com.phylage.scaladia

import com.phylage.scaladia.injector.{AutoInject, Injector}

object Main extends App with Injector {
  trait A
  object A extends A with AutoInject[A]

  inject[A]._provide
}
