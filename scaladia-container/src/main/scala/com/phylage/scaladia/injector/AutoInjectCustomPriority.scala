package com.phylage.scaladia.injector

/**
  * Inject automatically by specifying priority
  * @tparam T Type to register
  */
@deprecated("Define and use traits with custom priorities as needed")
class AutoInjectCustomPriority[T](override val injectionPriority: Int) extends AutoInject[T] { me: T =>

}
