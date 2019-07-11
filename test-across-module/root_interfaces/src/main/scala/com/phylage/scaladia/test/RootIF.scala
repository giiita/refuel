package com.phylage.scaladia.test

import com.phylage.scaladia.injector.{AutoInject, Injector}

object RootIF extends Injector {

  trait Runner {
    def run: String = inject[Conf].value
  }
  object Runner extends Runner with AutoInject[Runner]
}
trait Conf {
  val value: String
}
