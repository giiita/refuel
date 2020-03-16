package refuel.test

import refuel.injector.AutoInject

object ConfImpl extends Conf with AutoInject {
  val value: String = "ConfImpl"
}
