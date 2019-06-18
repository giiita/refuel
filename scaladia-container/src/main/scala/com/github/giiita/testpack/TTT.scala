package com.github.giiita.testpack

import com.github.giiita.injector.AutoInject

object TTT {

  trait MOMO {
    def test = "TEST"
  }

  object MOMO extends MOMO with AutoInject[MOMO] {
    println("MOMO INITED")

    override def test = "SUCCESS"
  }

}
