package com.giitan.inject

import com.giitan.injector.{AutoInjector, Injector}

object TopLevelAutoInjectable extends TopLevelAutoInjectable {
  depends[TopLevelAutoInjectable](this)
}

trait TopLevelAutoInjectable extends AutoInjector

trait I extends AutoInjector {
  def name: String
}
object J extends I {
  def name: String = "J"
  depends[I](this)
}

object Caller extends Caller

trait Caller extends Injector {
  val i: I = inject[I]
  def result: String = i.name
}