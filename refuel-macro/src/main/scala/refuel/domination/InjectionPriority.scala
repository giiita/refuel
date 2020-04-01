package refuel.domination

/**
 * Arbitrarily extensible priority object.
 * If extending, it must be a case object.
 *
 * @param value priority
 */
sealed abstract class InjectionPriority(val value: Int)

/**
 *
 * Sample for defining any priority.
 *
 * ```
 * case object Secondary extends InjectionPriority(Primary >>)
 * case object SemiFinally extends InjectionPriority(Finally <<)
 * ```
 *
 * Define priority objects below Finally or Default assuming that they will be overwritten.
 *
 */
object InjectionPriority {
  implicit val Order: Ordering[InjectionPriority] = Ordering.by(_.value)

  /**
   * It will be the highest priority automatic injection.
   * No manual override of dependencies.
   */
  case object Primary extends InjectionPriority(Int.MinValue)

  /**
   * Used in normal declarations.
   * Even if [[refuel.domination.Inject]] annotation is not added, it will be ranked as Default.
   * With custom injection priority you can override this.
   */
  case object Overwrite extends InjectionPriority(Int.MinValue >> 1)

  /**
   * Used in normal declarations.
   * Even if [[refuel.domination.Inject]] annotation is not added, it will be ranked as Default.
   * With custom injection priority you can override this.
   */
  case object Default extends InjectionPriority(0)

  /**
   * It is injected as a substitute when there is no injection candidate.
   * If there are other similar definitions, they will definitely be overwritten.
   */
  case object Finally extends InjectionPriority(Int.MaxValue)

}