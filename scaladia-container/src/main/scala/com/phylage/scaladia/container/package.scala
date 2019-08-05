package com.phylage.scaladia

import com.phylage.scaladia.Types.@@
import com.phylage.scaladia.container.indexer.{BroadSenseIndexer, Indexer}
import com.phylage.scaladia.injector.scope.{InjectableScope, OpenScope}
import com.phylage.scaladia.provider.{Accessor, Tag}
import com.phylage.scaladia.runtime.{ConcreteDeferredAttachementOnce, InjectionReflector}

import scala.collection.concurrent.TrieMap
import scala.collection.mutable
import scala.reflect.runtime.universe._

package object container {
  type ContainerPool = TrieMap[ContainerIndexedKey, mutable.LinkedHashSet[InjectableScope[_]]]

  implicit val injectionReflector: InjectionReflector = RuntimeReflector

  case class StandardContainer(shade: Boolean = false, buffer: ContainerPool = TrieMap.empty, lights: Vector[Container] = Vector.empty) extends Container with Tag[Types.Localized] {

    /**
      * Cache in the injection container.
      *
      * @param value injection object
      * @tparam T injection type
      * @return
      */
    def cache[T](value: InjectableScope[T]): InjectableScope[T] = synchronized {

      val key = implicitly[ConcreteDeferredAttachementOnce].get(value.tag.tpe)
      buffer.get(key) match {
        case None    => buffer.+=(key -> mutable.LinkedHashSet.apply(value))
        case Some(x) => buffer.update(key, x + value)
      }
      value
    }

    /**
      * May return an injectable object.
      *
      * @param requestFrom object that called inject
      * @tparam T return object type
      * @return
      */
    def find[T: WeakTypeTag](requestFrom: Accessor[_]): Option[T] = {
      buffer.get(implicitly[ConcreteDeferredAttachementOnce].get(implicitly[WeakTypeTag[T]].tpe)) match {
        case None    => None
        case Some(x) => x.filter(_.accepted(requestFrom)).toSeq.sortBy(_.priority)(Ordering.Int.reverse).headOption.map(_.value.asInstanceOf[T])
      }
    }

    /**
      * Generate an indexer.
      *
      * @param x        Injectable object.
      * @param priority priority
      * @tparam T injection type
      * @return
      */
    def createIndexer[T: WeakTypeTag](x: T, priority: Int, lights: Vector[Container]): Indexer[T] = {
      new BroadSenseIndexer(OpenScope[T](x, priority, weakTypeTag[T]), lights :+ this)
    }

    override def shading: @@[Container, Types.Localized] = {
      copy(
        shade = true,
        buffer = buffer.snapshot(),
        lights = this.lights.:+(this)
      )
    }
  }

  implicit object ConcreteDeferredAttachementOnce extends ConcreteDeferredAttachementOnce {
    private[this] val runtimeBuffer: TrieMap[Type, ContainerIndexedKey] = TrieMap.empty

    /**
      * Temporarily hold concrete deferred type names for high multi-threading environments.
      * This is because it may be synchronized inside scala-reflect during container search.
      *
      * @param t Type instance
      * @return
      */
    def get(t: Type): ContainerIndexedKey = {
      runtimeBuffer.getOrElse(t, {

        val key = ContainerIndexedKey(t.toString)
        runtimeBuffer.+=(t -> key)
        key
      })
    }
  }
}
