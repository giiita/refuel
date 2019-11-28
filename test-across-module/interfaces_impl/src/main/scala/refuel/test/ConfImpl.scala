package refuel.test

import refuel.injector.AutoInject

object ConfImpl extends Conf with AutoInject[Conf] {
  val value: String = "ConfImpl"
}
