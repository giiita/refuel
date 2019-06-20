package com.github.giiita.testpack

import com.github.giiita.injector.{AutoInject, AutoInjectCustomPriority, RecoveredInject}

object TTT {

  trait HARAMI

  trait MOMO {
    def test = "TEST"
  }

  object MOMO_A extends MOMO with AutoInject[MOMO] {
    println("MOMO_A INITED")

    override def test = "A"
  }

  object MOMO_B extends MOMO with RecoveredInject[MOMO] {
    println("MOMO_B INITED")

    override def test = "B"
  }

  object MOMO_C extends AutoInjectCustomPriority[MOMO](Int.MaxValue - 1) with MOMO {
    println("MOMO_C INITED")

    override def test = "C"
  }

  trait MUNE {
    def test = "MUNE"
  }

  object MUNE_A extends MUNE with AutoInject[MUNE] {
    println("MUNE_A INITED")

    override def test = "AA"
  }

  object MUNE_B extends MUNE with RecoveredInject[MUNE] {
    println("MUNE_B INITED")

    override def test = "BB"
  }

  object MUNE_C extends AutoInjectCustomPriority[MUNE](Int.MaxValue - 1) with MUNE {
    println("MUNE_C INITED")

    override def test = "CC"
  }
}
