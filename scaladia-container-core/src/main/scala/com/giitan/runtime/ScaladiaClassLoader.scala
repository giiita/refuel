package com.giitan.runtime

import java.io.File
import java.net.{JarURLConnection, URL, URLClassLoader}

import com.giitan.runtime.ScaladiaAutoDIConfigSet._
import com.giitan.loader.LoadableArchives._
import com.giitan.loader.RichClassCrowds.ClassCrowds
import org.slf4j.LoggerFactory

import scala.annotation.tailrec

object ScaladiaClassLoader {
  private val URL_CLASSLOADER_DEPTH_LIMIT = 5
  private val logger = LoggerFactory.getLogger(getClass)
  private[giitan] val classLoader: ClassLoader = getClass.getClassLoader

  private[giitan] def findClasses(rootPackageName: String = ""): ClassCrowds = {
    val ucl = getUrlClassloader(classLoader)
    accessibleProtocolResolve(ucl.fold[List[URL]](Nil)(_.getURLs.toList))
      .scanAccepted(classLoader)
      .distinct
      .par
      .map(findClassesWithFile(_, rootPackageName)) match {
      case x if x.isEmpty => ClassCrowds()
      case x              => x.reduceLeft(_ +++ _)
    }
  }

  private def findClassesWithFile(x: URL, rootPackageName: String): ClassCrowds = {
    logger.debug(s"Injectable set loading from ${x.getProtocol}:${x.getPath}")
    x match {
      case url if url.getPath.contains(".jdk") => ClassCrowds()
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
    *
    * @param v urls
    * @return
    */
  private def accessibleProtocolResolve(v: List[URL]): List[URL] = {
    v.map {
      case url if url.getPath.endsWith(".jar") => new URL(s"jar:file:${url.getPath}!/")
      case url                                 => url
    }
  }

  @tailrec
  private final def getUrlClassloader(currentClassLoader: ClassLoader, depth: Int = 0): Option[URLClassLoader] = {
    currentClassLoader match {
      case x: URLClassLoader                        => Some(x)
      case x if depth < URL_CLASSLOADER_DEPTH_LIMIT => getUrlClassloader(x.getParent, depth + 1)
      case _                                        => None
    }
  }
}

