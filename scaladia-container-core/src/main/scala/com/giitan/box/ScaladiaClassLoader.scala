package com.giitan.box

import java.io.File
import java.net.{JarURLConnection, URL, URLClassLoader}

import com.giitan.box.ScaladiaAutoDIConfigSet._
import com.giitan.loader.LoadableArchives._
import com.giitan.loader.RichClassCrowds.ClassCrowds
import com.giitan.loader.StringURIConvertor._
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._
import scala.util.Try

object ScaladiaClassLoader {
  private val logger = LoggerFactory.getLogger(getClass)
  private[giitan] val classLoader: ClassLoader = Thread.currentThread().getContextClassLoader

  implicit class IterateUrl(value: Iterator[URL]) {
    def or(next: Unit => Iterator[URL]): Iterator[URL] =
      try {
        if (value.isEmpty) next() else value
      } catch {
        case e: Throwable => throw new UnsupportedOperationException("The Runtime to be loaded must be a Class list or a jar archive.", e)
      }
  }

  private[giitan] def findClasses(rootPackageName: String = ""): ClassCrowds = {
    Try {
      accessibleProtocolResolve(classLoader.asInstanceOf[URLClassLoader].getURLs.toList)
    }
      .orElse {
        Try {
          accessibleProtocolResolve(classLoader.getParent.asInstanceOf[URLClassLoader].getURLs.toList)
        }
      }
      .getOrElse(Nil)
      .scanAccepted(classLoader.getResources(rootPackageName.dotToSlash).asScala.toList)
      .distinct
      .map(findClassesWithFile(_, rootPackageName)) match {
      case x if x.isEmpty => ClassCrowds()
      case x              => x.reduceLeft(_ +++ _)
    }
  }

  private def findClassesWithFile(x: URL, rootPackageName: String): ClassCrowds = {
    logger.debug(s"Injectable set loading from ${x.getProtocol}:${x.getPath}")
    x match {
      case url if url.getProtocol == "file" => new File(x.getFile).classCrowdEntries(rootPackageName)
      case url if url.getProtocol == "jar"  =>

        def findClassesWithJarFileInner(packageName: String): ClassCrowds =
          url.openConnection match {
            case jarURLConnection: JarURLConnection => jarURLConnection.getJarFile.classCrowdEntries(packageName)
            case _                                  => ClassCrowds()
          }

        findClassesWithJarFileInner(rootPackageName)
      case _                                => ClassCrowds()
    }
  }

  /**
    * Resolve url protocol.
    * @param v urls
    * @return
    */
  private def accessibleProtocolResolve(v: List[URL]): List[URL] = {
    v.map {
      case url if url.getPath.endsWith(".jar") => new URL(s"jar:file:${url.getPath}!/")
      case url                                 => url
    }
  }
}

