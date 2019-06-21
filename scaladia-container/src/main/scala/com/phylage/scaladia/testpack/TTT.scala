package com.phylage.scaladia.testpack

import com.phylage.scaladia.injector.Injector.@@
import com.phylage.scaladia.injector.{AutoInject, AutoInjectCustomPriority, Injector, RecoveredInject}
import com.phylage.scaladia.provider.Tag
import com.phylage.scaladia.testpack.deeper.MMMMMM

object TTT extends Injector {

  trait HARAMI

  trait MOMO {
    def test = "TEST"
  }

  object MOMO_A extends MOMO with Tag[MMMMMM] with AutoInject[MOMO @@ MMMMMM] { me: MOMO @@ MMMMMM =>
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
