package com.phylage.scaladia.injector

/**
  * Inject automatically by specifying priority
  * @tparam T Type to register
  */
class AutoInjectCustomPriority[T](private[scaladia] override val injectionPriority: Int) extends AutoInject[T] with Injector { me: T =>

}
