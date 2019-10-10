package com.phylage.scaladia.injector

/**
 * Inject automatically by specifying priority.
 *
 * This was been deprecated.
 * If you want to use this feature, please declare your own trait with custom priority.
 *
 * {{{
 *   trait MyAutoInject[T] extends AutoInject[T] {
 *     override val injectionPriority: Int = AutoInject.DEFAULT_INJECTION_PRIORITY + 1
 *   }
 * }}}
 *
 * @tparam T Type to register
 */
@deprecated("Define and use traits with custom priorities as needed")
class AutoInjectCustomPriority[T](override val injectionPriority: Int) extends AutoInject[T] {
  me: T =>

}
