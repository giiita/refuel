package com.phylage.scaladia.runtime

import java.io.File
import java.net.{JarURLConnection, URL}

import com.phylage.scaladia.container.RuntimeReflector
import com.phylage.scaladia.injector.AutoInjectable

import scala.annotation.tailrec
import scala.collection.JavaConverters._
import scala.collection.parallel.immutable.ParVector
import scala.reflect.runtime.universe


object RuntimeAutoDIExtractor {

  private[this] val autoDITag = universe.weakTypeOf[AutoInjectable[_]]

  /**
    * Load module symbols with automatic injection enabled.
    *
    * @return
    */
  def run(): RuntimeAutoInjectableSymbols = {
    val entries = ParVector.apply(archiveProtocolResolver(RuntimeReflector.classloader.getURLs.toList): _*)
      .map(findPackageWithFile)
      .seq
      .toSet[PackagePathEntries]
      .join

    RuntimeAutoInjectableSymbols(
      entries.moduleSymbolPath.flatMap { x =>
        try {
          RuntimeReflector.mirror.staticModule(x) match {
            case r if r.typeSignature <:< autoDITag => Some(r)
            case _                                  => None
          }
        } catch {
          case _: Throwable => None
        }
      },
      entries.classSymbolPath.flatMap { x =>
        try {
          RuntimeReflector.mirror.staticClass(x) match {
            case r if r.toType <:< autoDITag => Some(r)
            case _                               => None
          }
        } catch {
          case _: Throwable => None
        }
      }
    )
  }

  /**
    * Parse url protocol from classpath url and load class symbol.
    *
    * @param x classpath url
    * @return
    */
  private def findPackageWithFile(x: URL): PackagePathEntries = {
    x match {
      case url if url.getPath.contains(".jdk") => PackagePathEntries.empty
      case url if url.getProtocol.isFile       =>
        filePackageExtraction(Set(new File(url.getPath))).rounding(url.getPath).doFinalize
      case url if url.getProtocol.isJar        =>
        jarPackageExtraction(url).doFinalize
      case _                                   => PackagePathEntries.empty
    }
  }

  /**
    * Get static module symbol from Jar archives in classpath.
    *
    * @param url target urls
    * @return
    */
  private def jarPackageExtraction(url: URL): PackagePathEntries = {
    url.openConnection match {
      case jarURLConnection: JarURLConnection =>
        jarURLConnection.getJarFile.entries().asScala.toSeq.collect {
          case entry if entry.getName.isModuleSymbol => Some(entry.getName.slashToDot) -> None
          case entry if entry.getName.isClassSymbol  => None -> Some(entry.getName.slashToDot)
        } match {
          case rs => new PackagePathEntries(
            rs.flatMap(_._1).toSet,
            rs.flatMap(_._2).toSet
          )
        }
      case _                                  => PackagePathEntries.empty
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
      case url                                 => url
    }
  }

  /**
    * Get static module symbol from File in classpath.
    *
    * @param files input files
    * @return
    */
  @tailrec
  private[this] final def filePackageExtraction(files: Set[File], rs: PackagePathEntries = PackagePathEntries.empty): PackagePathEntries = {
    files.map { file =>
      val collected = file.list() match {
        case null => Vector.empty
        case list => list.toVector.map { part =>
          new File(file, part) match {
            case x if x.isDirectory                        => (Some(x), None, None)
            case x if x.isFile && x.getName.isModuleSymbol => (None, Some(x), None)
            case x if x.isFile && x.getName.isClassSymbol  => (None, None, Some(x))
            case _                                         => (None, None, None)
          }
        }
      }

      new PackagePathEntries(
        collected.flatMap(_._2.map(_.getPath)).toSet,
        collected.flatMap(_._3.map(_.getPath)).toSet
      ) -> collected.flatMap(_._1).toSet
    } match {
      case r if r.flatMap(_._2).isEmpty => r.map(_._1).join.union(rs)
      case r                            => filePackageExtraction(r.flatMap(_._2), r.map(_._1).join.union(rs))
    }
  }

}
