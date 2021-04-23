package refuel.provider

import refuel.container.Container

trait Lazy[X] {

  /**
    * Provide dependency and make it persistent.
    *
    * @return
    */
  def _provide(implicit ctn: Container): X
}

object Lazy {
  def apply[T](t: => T): Lazy[T] = new Lazy[T]() {
    def _provide(implicit ctn: Container): T = t
  }
}
