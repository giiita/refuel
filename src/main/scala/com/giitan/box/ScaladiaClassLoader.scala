package com.giitan.box

import scala.collection.JavaConverters._
import java.io.File
import java.net.{JarURLConnection, URL}
import java.util.jar.{JarEntry, JarFile}

import com.giitan.box.ScaladiaClassLoader.RichClassCrowd
import com.giitan.injector.AutoInjector

import scala.collection.mutable.ListBuffer
import scala.reflect._
import scala.reflect.runtime.universe._

object ScaladiaClassLoader {
  private val classLoader: ClassLoader = Thread.currentThread().getContextClassLoader

  object RichClassCrowd {
    def apply(value: List[Class[_]]): RichClassCrowd = {
      RichClassCrowd(new ListBuffer() ++= value)
    }
  }

  case class RichClassCrowd(value: ListBuffer[Class[_]] = ListBuffer.empty) {

    private[this] def fire[T: TypeTag](clazz: Class[T]): Unit = {

      drop(clazz)
      val mirror = runtimeMirror(classLoader)
      if (clazz.getName.trim.endsWith("$")) {
        try {
          val x = mirror.reflectModule(mirror.staticModule(clazz.getName)).instance
          x.toString
        } catch {
          case _: Throwable =>
        }
      }
    }

    def initialize[T: ClassTag](tag: TypeTag[T]): Unit = {
      val target = classTag[T].runtimeClass
      value.find(r => target.isAssignableFrom(r)) match {
        case Some(x) => fire(x)
        case _ => initialize()
      }
    }

    def initialize(): Unit = {
      def loop(looped: ListBuffer[Class[_]]): Unit = {
        looped.result() match {
          case Nil =>
          case _ =>
            fire(looped.head)
            loop(value)
        }
      }
      loop(value)
    }

    def +++(next: RichClassCrowd): RichClassCrowd = RichClassCrowd(value ++= next.value)
    def drop[T](dropClass: Class[T]): Unit = value -= dropClass
  }
}

case class ScaladiaClassLoader(classLoader: ClassLoader = Thread.currentThread().getContextClassLoader) {

  private[this] def pathToClassName(path: String): String = path.substring(0, path.length - ".class".length)

  private[this] def isClassFile(entry: JarEntry): Boolean = isClassFile(entry.getName)
  private[this] def isClassFile(file: File): Boolean = file.isFile && isClassFile(file.getName)
  private[this] def isClassFile(filePath: String): Boolean = filePath.endsWith(".class")

  private[this] def resolvePackage(packageName: String): String = if (packageName.isEmpty) "" else s"$packageName."

  private def resourceNameToClassName(resourceName: String): String =
    pathToClassName(resourceNameToPackageName(resourceName))
  private def resourceNameToPackageName(resourceName: String): String =
    resourceName.replace('/', '.')
  private def packageNameToResourceName(packageName: String): String =
    packageName.replace('.', '/')

  implicit class IterateUrl(value: Iterator[URL]) {
    def or(next: Unit => Iterator[URL]): Iterator[URL] =
      try {
        if (value.isEmpty) next() else value
      } catch {
        case e: Throwable => throw new UnsupportedOperationException("The Runtime to be loaded must be a Class list or a jar archive.", e)
      }
  }

  def findClasses(rootPackageName: String = ""): RichClassCrowd = {
    val resourceName = packageNameToResourceName(rootPackageName)

    val resources = classLoader.getResources(resourceName).asScala

    val thisPackage = packageNameToResourceName(this.getClass.getPackage.getName)
    val paths = "jar:" + classLoader.getResource(thisPackage).getPath.replace(thisPackage, "")

    resources.or(_ => Iterator(new URL(paths))).map({
      case null => RichClassCrowd()
      case url  => findClassesWithFile(url, rootPackageName)
    }) match {
      case x if x.isEmpty => RichClassCrowd()
      case x => x.reduceLeft(_ +++ _)
    }
  }

  def findClassesWithFile(x: URL, rootPackageName: String): RichClassCrowd = x match {
    case url if url.getProtocol == "file" =>
      def findClassesWithFileInner(packageName: String, dir: File): List[Class[_]] = {
        dir.list.flatMap(path => {
          new File(dir, path) match {
            case file if isClassFile(file) =>
              try {
                val classType = classLoader.loadClass(resolvePackage(packageName) + pathToClassName(file.getName))
                if (classOf[AutoInjector].isAssignableFrom(classType) && !classType.isInterface) Some(classType) else None
              } catch {
                case _: Throwable => None// class load failed.
              }
            case directory if directory.isDirectory =>
              findClassesWithFileInner(resolvePackage(packageName) + directory.getName, directory)
            case _ => None
          }
        }).toList
      }

      RichClassCrowd(findClassesWithFileInner(rootPackageName, new File(x.getFile)))
    case url if url.getProtocol == "jar" =>
      def manageJar[T](jarFile: JarFile)(body: JarFile => T): T = try {
        body(jarFile)
      } finally {
        jarFile.close()
      }

      def findClassesWithJarFileInner(packageName: String): RichClassCrowd =
        url.openConnection match {
          case jarURLConnection: JarURLConnection =>
            manageJar(jarURLConnection.getJarFile)(jarFile =>
              RichClassCrowd(
                jarFile.entries.asScala.map(entry => {
                  if (resourceNameToPackageName(entry.getName).startsWith(packageName) && isClassFile(entry)) {
                    try {
                      val classType = classLoader.loadClass(resourceNameToClassName(entry.getName))
                      if (classOf[AutoInjector].isAssignableFrom(classType) && !classType.isInterface) Some(classType) else None
                    } catch {
                      case _: Throwable => None// class load failed.
                    }
                  } else None
                }).flatten.toList
              )
            )
          case _ => RichClassCrowd()
        }

      findClassesWithJarFileInner(rootPackageName)
    case _ => RichClassCrowd()
  }
}

