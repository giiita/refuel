package com.phylage.scaladia.container

import java.net.{URI, URL}

import com.phylage.scaladia.injector.AutoInject
import com.phylage.scaladia.injector.scope.IndexedSymbol
import com.phylage.scaladia.runtime.InjectionReflector

import scala.reflect.runtime.universe


object RuntimeReflector extends InjectionReflector {

  def mirror: universe.Mirror = universe.runtimeMirror(getClass.getClassLoader)

  /**
   * Create injection applyment.
   *
   * @param symbols module symbols
   * @tparam T injection type
   * @return
   */
  override def reflectClass[T: universe.WeakTypeTag](c: Container)(symbols: Set[universe.ClassSymbol]): Set[IndexedSymbol[T]] = {
    symbols.map { x =>
      mirror.reflectClass(x)
        .reflectConstructor(x.primaryConstructor.asMethod)
        .apply()
        .asInstanceOf[AutoInject[T]] match {
        case ai => ai.flush(c)
      }
    }
  }

  /**
   * Create injection applyment.
   *
   * @param symbols module symbols
   * @tparam T injection type
   * @return
   */
  override def reflectModule[T: universe.WeakTypeTag](c: Container)(symbols: Set[universe.ModuleSymbol]): Set[IndexedSymbol[T]] = {
    symbols.map { x =>
      mirror.reflectModule(x)
        .instance
        .asInstanceOf[AutoInject[T]] match {
        case ai => ai.flush(c)
      }
    }
  }

  /**
   * Reflect to a runtime class.
   *
   * @param t Type symbol.
   * @return
   */
  override def reflectClass(t: universe.Type): universe.RuntimeClass = {
    mirror.runtimeClass(t)
  }

  final def classpathUrls: List[URL] = {

    val FILE_SCHEME = "file:%s"
    val JAR_SCHEME = "jar:file:%s!/"
    val IGNORE_PATHS = Seq(
      " ",
      "scala-reflect.jar",
      "scala-library.jar",
      "sbt-launch.jar"
    )

    import collection.JavaConverters._

    System.getProperty("java.class.path")
      .split(":")
      .++(this.getClass.getClassLoader.getResources("").asScala.map(_.getPath))
      .distinct
      .withFilter(x => IGNORE_PATHS.forall(!x.contains(_)))
      .map {
        case x if x.endsWith(".jar") => JAR_SCHEME.format(x)
        case x                       => FILE_SCHEME.format(x)
      }.map(new URI(_).toURL).toList
  }
}
