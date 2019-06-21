package com.github.giiita.injector

/**
  * Inject automatically by specifying priority
  * @tparam T Type to register
  */
class AutoInjectCustomPriority[T](private[giiita] override val injectionPriority: Int) extends AutoInject[T] with Injector { me: T =>

}
