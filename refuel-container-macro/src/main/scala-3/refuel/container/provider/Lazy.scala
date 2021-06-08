package refuel.container.provider

trait Lazy[X] {

  /**
   * Provide dependency and make it persistent.
   *
   * @return
   */
  def _provide: X
}

object Lazy {
  def apply[T](t: => T): Lazy[T] = new Lazy[T] {
    def _provide: T = t
  }
  import scala.language.implicitConversions
  given aliasLazyT[T]: scala.Conversion[Lazy[T], T] = refuel.container.provider._implicitProviding[T]
}
