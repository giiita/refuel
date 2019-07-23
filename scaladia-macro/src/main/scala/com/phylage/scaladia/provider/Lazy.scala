package com.phylage.scaladia.provider

trait Lazy[X] {

  /**
    * Provide dependency and make it persistent.
    *
    * @return
    */
  def _provide: X
}