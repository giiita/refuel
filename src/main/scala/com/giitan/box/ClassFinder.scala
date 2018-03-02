package com.giitan.box

import scala.collection.JavaConverters._
import scala.collection.mutable.ListBuffer
import java.io.File
import java.net.{JarURLConnection, URL}
import java.util.jar.{JarEntry, JarFile}

import com.giitan.box.ClassFinder.RichClassCrowd
import com.giitan.injector.AutoInjector

import scala.reflect.runtime.universe._

object ClassFinder {
  private val classLoader: ClassLoader = Thread.currentThread().getContextClassLoader
  private val autoInjectorClassSymbol = classOf[AutoInjector]

  case class RichClassCrowd(value: List[Class[_]] = List.empty) {

    def isAutoInjectorSubType(v: List[Symbol]): Boolean = {
      v.exists(r => {
        println(r)
        r.typeSignature.typeSymbol.asClass == autoInjectorClassSymbol ||
          isAutoInjectorSubType(r.typeSignature.baseClasses)
      })
    }

    private[this] def fire[T: TypeTag](clazz: Class[T]): Unit = {

      val mirror = runtimeMirror(classLoader)
      if (clazz.getName.trim.endsWith("$")) {

        try {
          mirror.reflectModule(mirror.staticModule(clazz.getName)).instance
        } catch {
          case _: Throwable =>
        }
      }
    }

    def initialize(): Unit = {
      value.foreach(r => fire(r))
    }

    def +++(next: RichClassCrowd): RichClassCrowd = RichClassCrowd(value ++: next.value)
  }
}

case class ClassFinder(classLoader: ClassLoader = Thread.currentThread().getContextClassLoader) {

  private def pathToClassName(path: String): String = path.substring(0, path.length - ".class".length)

  private def isClassFile(entry: JarEntry): Boolean = isClassFile(entry.getName)
  private def isClassFile(file: File): Boolean = file.isFile && isClassFile(file.getName)
  private def isClassFile(filePath: String): Boolean = true // filePath.endsWith(".class")

  private def resourceNameToClassName(resourceName: String): String =
    pathToClassName(resourceNameToPackageName(resourceName))
  private def resourceNameToPackageName(resourceName: String): String =
    resourceName.replace('/', '.')
  private def packageNameToResourceName(packageName: String): String =
    packageName.replace('.', '/')

  private val finderFunction: PartialFunction[URL, String => RichClassCrowd] =
    findClassesWithFile.orElse(findClassesWithJarFile).orElse(findClassesWithNone)

  def findClasses(rootPackageName: String): RichClassCrowd = {
    val resourceName = packageNameToResourceName(rootPackageName)

    val resources = classLoader.getResources(resourceName).asScala

    resources.map({
      case null => println(null); RichClassCrowd()
      case url => finderFunction(url)(rootPackageName)
    }).reduce(_ +++ _)
  }

  def findClassesWithFile: PartialFunction[URL, String => RichClassCrowd] = {
    case url if url.getProtocol == "file" =>
      val classes = new ListBuffer[Class[_]]

      def findClassesWithFileInner(packageName: String, dir: File): RichClassCrowd = {
        dir.list.foreach { path =>
          new File(dir, path) match {
            case file if isClassFile(file) =>
              classes += classLoader.loadClass(packageName + "." + pathToClassName(file.getName))
            case directory if directory.isDirectory =>
              findClassesWithFileInner(packageName + "." + directory.getName, directory)
            case _ =>
          }
        }

        RichClassCrowd(classes.toList)
      }

      findClassesWithFileInner(_: String, new File(url.getFile))
  }

  def findClassesWithJarFile: PartialFunction[URL, String => RichClassCrowd] = {
    case url if url.getProtocol == "jar" =>
      def manageJar[T](jarFile: JarFile)(body: JarFile => T): T = try {
        body(jarFile)
      } finally {
        jarFile.close()
      }

      def findClassesWithJarFileInner(packageName: String): RichClassCrowd =
        url.openConnection match {
          case jarURLConnection: JarURLConnection =>
            manageJar(jarURLConnection.getJarFile) { jarFile =>
              RichClassCrowd(
                jarFile.entries.asScala.toList.collect {
                  case jarEntry if resourceNameToPackageName(jarEntry.getName).startsWith(packageName) &&
                    isClassFile(jarEntry) =>
                    classLoader.loadClass(resourceNameToClassName(jarEntry.getName))
                }
              )
            }
        }

      findClassesWithJarFileInner
  }

  def findClassesWithNone: PartialFunction[URL, String => RichClassCrowd] = {
    case _ => _ => RichClassCrowd()
  }
}

