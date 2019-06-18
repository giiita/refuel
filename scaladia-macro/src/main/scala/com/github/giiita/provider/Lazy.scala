package com.github.giiita.provider

trait Lazy[X] {

  /**
    * Provide dependency and make it persistent.
    *
    * @return
    */
  def provide: X
}
