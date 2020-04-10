package refuel.container

import java.net.{URI, URL}

import refuel.domination.InjectionPriority
import refuel.exception.InjectDefinitionException
import refuel.injector.scope.IndexedSymbol
import refuel.injector.{InjectionPool, Injector}
import refuel.internal.ClassTypeAcceptContext
import refuel.runtime.InjectionReflector

import scala.annotation.tailrec
import scala.reflect.runtime.universe


object RuntimeReflector extends InjectionReflector with Injector {

  def mirror: universe.Mirror = universe.runtimeMirror(Thread.currentThread().getContextClassLoader)


  def embody[T](ms: universe.ModuleSymbol): T = mirror.reflectModule(ms).instance.asInstanceOf[T]

  /**
   * Create injection applyment.
   *
   * @tparam T injection type
   * @return
   */
  override def reflectClass[T](clazz: Class[_], ip: InjectionPool)(c: Container)(x: universe.ClassSymbol)(implicit wtt: universe.WeakTypeTag[T]): InjectionPriority => IndexedSymbol[T] = { p =>
    val pcInject = x.primaryConstructor.asMethod.paramLists.flatten.map { prm =>
      val tpe: universe.WeakTypeTag[_] = universe.WeakTypeTag(wtt.mirror, new reflect.api.TypeCreator {
        def apply[U <: reflect.api.Universe with Singleton](m: reflect.api.Mirror[U]) = {
          assert(m eq mirror, s"TypeTag[$prm] defined in $mirror cannot be migrated to $m.")
          prm.typeSignature.asInstanceOf[U#Type]
        }
      })

      c.find(clazz)(tpe, ClassTypeAcceptContext) getOrElse {
        ip.collect(clazz)(tpe)(c).fold(
          throw new InjectDefinitionException(s"Cannot found ${tpe.tpe.typeSymbol} implementations.")
        ) {
          case (p, fs) if fs.size == 1 => fs.head(p).value
          case (p, fs) =>
            val x = fs.map(_.apply(p).tag)
            throw new InjectDefinitionException(
              s"""Invalid dependency definition of $tpe. There must be one automatic injection of inject[T] per priority. But found [${x.mkString(", ")}]"""
            )
        }
      }
    }

    mirror.reflectClass(x)
      .reflectConstructor(x.primaryConstructor.asMethod)
      .apply(pcInject: _*)
      .asInstanceOf[T] match {
      case x => c.createIndexer(x, p).indexing()
    }
  }

  /**
   * Create injection applyment.
   *
   * @tparam T injection type
   * @return
   */
  override def reflectModule[T](c: Container)(x: universe.ModuleSymbol)(implicit wtt: universe.WeakTypeTag[T]): InjectionPriority => IndexedSymbol[T] = { p =>
    mirror.reflectModule(x)
      .instance
      .asInstanceOf[T] match {
      case x => c.createIndexer(x, p)(wtt).indexing()
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

  @tailrec
  private[this] def getClassLoaderUrls(cl: ClassLoader): Seq[URL] = {
    cl match {
      case null => Nil
      case x: java.net.URLClassLoader => x.getURLs.toSeq
      case x => getClassLoaderUrls(x.getParent)
    }
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
      .++(getClassLoaderUrls(this.getClass.getClassLoader).map(_.getPath))
      .distinct
      .withFilter(x => IGNORE_PATHS.forall(!x.contains(_)))
      .map {
        case x if x.endsWith(".jar") => JAR_SCHEME.format(x)
        case x => FILE_SCHEME.format(x)
      }.map(new URI(_).toURL).toList
  }
}
