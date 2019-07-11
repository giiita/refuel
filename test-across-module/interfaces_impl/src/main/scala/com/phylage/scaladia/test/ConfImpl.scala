package com.phylage.scaladia.test

import com.phylage.scaladia.injector.AutoInject

object ConfImpl extends Conf with AutoInject[Conf] {
  val value: String = "ConfImpl"
}
