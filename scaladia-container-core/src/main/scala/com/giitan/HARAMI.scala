package com.giitan

import com.giitan.injector.AutoInject

trait HARAMI

trait MOMO {
  def test = "TEST"
}

object MOMO_A extends MOMO with AutoInject[MOMO] {
  println("MOMO_A INITED")

  override def test = "A"
}

trait MUNE {
  def test = "MUNE"
}

object MUNE_A extends MUNE with AutoInject[MUNE] {
  println("MUNE_A INITED")

  override def test = "AA"
}