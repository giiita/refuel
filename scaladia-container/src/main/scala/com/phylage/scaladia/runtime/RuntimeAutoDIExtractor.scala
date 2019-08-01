package com.phylage.scaladia.runtime

import java.io.File
import java.net.{JarURLConnection, URL}

import com.phylage.scaladia.Config
import com.phylage.scaladia.container.RuntimeReflector
import com.phylage.scaladia.injector.AutoInjectable

import scala.collection.JavaConverters._
import scala.reflect.runtime.universe
import scala.collection.parallel.immutable.ParVector


object RuntimeAutoDIExtractor {

  lazy val unloadablePackages: Seq[String] = Config.blackList.map(_.value)

  private[this] val autoDITag = universe.weakTypeOf[AutoInjectable[_]]

  /**
    * Load module symbols with automatic injection enabled.
    *
    * @return
    */
  def run(): Vector[universe.Symbol] = {
    ParVector.apply(archiveProtocolResolver(RuntimeReflector.classloader.getURLs.toList): _*).flatMap(findPackageWithFile).seq.distinct.flatMap { x =>
      try {
        RuntimeReflector.mirror.staticModule(x) match {
          case r if r.typeSignature <:< autoDITag => Some(r)
          case _ => None
        }
      } catch {
        case _: Throwable => None
        case _: Error => None
      }
    }
  }.toVector

  /**
    * Parse url protocol from classpath url and load class symbol.
    *
    * @param x classpath url
    * @return
    */
  private def findPackageWithFile(x: URL): Seq[String] = {
    x match {
      case url if url.getPath.contains(".jdk") => Nil
      case url if url.getProtocol.isFile =>
        filePackageExtraction(Vector(new File(url.getPath))).map { x =>
          x.diff(url.getPath).slashToDot
        }
      case url if url.getProtocol.isJar =>
        jarPackageExtraction(url)
      case _ => Nil
    }
  }.map(_.split("\\$\\.class").head.replaceAll("\\$", ".")).collect {
    case r if !unloadablePackages.exists(z => r.startsWith(z)) => r
  }

  /**
    * Get static module symbol from Jar archives in classpath.
    *
    * @param url target urls
    * @return
    */
  private def jarPackageExtraction(url: URL): Seq[String] = {
    url.openConnection match {
      case jarURLConnection: JarURLConnection =>
        jarURLConnection.getJarFile.entries().asScala.toSeq.collect {
          case entry if entry.getName.isModuleSymbol => entry.getName.slashToDot
        }.distinct
      case _ => Nil
    }
  }


  /**
    * Resolve url protocol.
    *
    * @param v urls
    * @return
    */
  private def archiveProtocolResolver(v: List[URL]): List[URL] = {
    v.map {
      case url if url.getPath.endsWith(".jar") => new URL(s"jar:file:${url.getPath}!/")
      case url => url
    }
  }

  /**
    * Get static module symbol from File in classpath.
    *
    * @param files input files
    * @return
    */
  private[this] final def filePackageExtraction(files: Seq[File]): Seq[String] = {
    files.flatMap { file =>
      val collected = file.list().toVector match {
        case null => Vector.empty
        case list => list.map { part =>
          new File(file, part) match {
            case x if x.isDirectory => Some(x) -> None
            case x if x.isFile && x.getName.isModuleSymbol => None -> Some(x)
            case _ => None -> None
          }
        }
      }

      collected.flatMap(_._2.map(_.getPath)) ++ filePackageExtraction(collected.flatMap(_._1))
    }
  }

}
