package com.github.giiita.injector.testpack

import com.github.giiita.injector.AutoInject

object TTT {

  object MOMO extends MOMO with AutoInject[String] {
    println("INITED")

    override def test = "SUCCESS"
  }

  trait MOMO {
    def test = "TEST"
  }
}
