package com.github.giiita.injector

class AutoInjectCustomPriority[T](private[giiita] override val injectionPriority: Int) extends AutoInject[T] with Injector { me: T =>

}
