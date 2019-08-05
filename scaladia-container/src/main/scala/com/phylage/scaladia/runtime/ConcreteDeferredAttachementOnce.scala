package com.phylage.scaladia.runtime

import com.phylage.scaladia.container.ContainerIndexedKey

import scala.reflect.runtime.universe._

trait ConcreteDeferredAttachementOnce {
  /**
    * Temporarily hold concrete deferred type names for high multi-threading environments.
    * This is because it may be synchronized inside scala-reflect during container search.
    *
    * @param t Type instance
    * @return
    */
  def get(t: Type): ContainerIndexedKey
}
