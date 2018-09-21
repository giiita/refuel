package com.giitan.box

import scala.collection.JavaConverters._
import java.io.File
import java.net.{JarURLConnection, URL}

import com.giitan.loader.LoadableArchives._
import com.giitan.loader.RichClassCrowds.ClassCrowds
import com.giitan.loader.StringURIConvertor._
import org.slf4j.{Logger, LoggerFactory}

object ScaladiaClassLoader {
  private[giitan] val classLoader: ClassLoader = Thread.currentThread().getContextClassLoader

  implicit class IterateUrl(value: Iterator[URL]) {
    def or(next: Unit => Iterator[URL]): Iterator[URL] =
      try {
        if (value.isEmpty) next() else value
      } catch {
        case e: Throwable => throw new UnsupportedOperationException("The Runtime to be loaded must be a Class list or a jar archive.", e)
      }
  }

  def findClasses(rootPackageName: String = ""): ClassCrowds = {
    val thisPackage = this.getClass.getPackage.getName.dotToSlash
    val paths = "jar:" + classLoader.getResource(thisPackage).getPath.replace(thisPackage, "")

    classLoader.getResources(rootPackageName.dotToSlash).asScala.or(_ => Iterator(new URL(paths))).map({
      case null => ClassCrowds()
      case url => findClassesWithFile(url, rootPackageName)
    }) match {
      case x if x.isEmpty => ClassCrowds()
      case x => x.reduceLeft(_ +++ _)
    }
  }

  def findClassesWithFile(x: URL, rootPackageName: String): ClassCrowds = x match {
    case url if url.getProtocol == "file" => new File(x.getFile).classCrowdEntries(rootPackageName)
    case url if url.getProtocol == "jar" =>

      def findClassesWithJarFileInner(packageName: String): ClassCrowds =
        url.openConnection match {
          case jarURLConnection: JarURLConnection => jarURLConnection.getJarFile.classCrowdEntries(packageName)
          case _ => ClassCrowds()
        }

      findClassesWithJarFileInner(rootPackageName)
    case _ => ClassCrowds()
  }


  val logger: Logger = LoggerFactory.getLogger(this.getClass)

}

