package com.github.giiita.provider

trait Lazy[X] {

  /**
    * Provide dependency and make it persistent.
    *
    * @return
    */
  val provide: X
}
