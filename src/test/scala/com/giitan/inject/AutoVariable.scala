package com.giitan.inject

import com.giitan.injector.{AutoInject, AutoInjector, Injector}

object AutoVariable extends AutoVariable {
  depends[AutoVariable](this)
}

trait AutoVariable extends AutoInjector

object AutoVariable2 extends AutoVariable2

trait AutoVariable2 extends AutoInject[AutoVariable2]

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


trait A
object B extends A
object C extends A

trait X
object Y extends X
object Z extends X

trait Named {
  def name: Int
}

object Named1 extends Named {
  def name = 1
}

object Named2 extends Named {
  def name = 2
}