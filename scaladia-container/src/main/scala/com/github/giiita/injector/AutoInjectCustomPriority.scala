package com.github.giiita.injector

class AutoInjectCustomPriority[T](override val injectionPriority: Int) extends AutoInject[T] with Injector { me: T =>

}
