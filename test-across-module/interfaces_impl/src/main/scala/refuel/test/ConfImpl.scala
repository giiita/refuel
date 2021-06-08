package refuel.test

import refuel.inject.AutoInject

object ConfImpl extends Conf with AutoInject {
  val value: String = "ConfImpl"
}
