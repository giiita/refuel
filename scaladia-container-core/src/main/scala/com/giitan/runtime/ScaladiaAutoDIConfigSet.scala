package com.giitan.runtime

import java.net.URL

import com.giitan.loader.StringURIConvertor._
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._
import scala.util.Try

private[giitan] object ScaladiaAutoDIConfigSet {

  private lazy val SCALADIA_CONFIG_SET_PATH = "scaladia"
  private lazy val LOAD_MULTI_CLASSPATHSET = "fullscan"
  private lazy val SCANING_ARCHIVE_WHITELIST = "whitelist"
  private lazy val SCANING_ARCHIVE_BLACKLIST = "blacklist"

  private lazy val isMultiClassPathMode: Boolean = Try {
    configSet.getBoolean(s"$SCALADIA_CONFIG_SET_PATH.$LOAD_MULTI_CLASSPATHSET")
  }.getOrElse(true)

  private lazy val scanningArchiveWhitelist: Seq[String] = {
    Try {
      configSet.getStringList(s"$SCALADIA_CONFIG_SET_PATH.$SCANING_ARCHIVE_WHITELIST").asScala.toSeq
    }.getOrElse(Nil)
  }

  private lazy val scanningArchiveBlacklist: Seq[String] = {
    Try {
      configSet.getStringList(s"$SCALADIA_CONFIG_SET_PATH.$SCANING_ARCHIVE_BLACKLIST").asScala.toSeq
    }.getOrElse(
      Seq(".jdk")
    )
  }

  private val logger = LoggerFactory.getLogger(this.getClass)
  private val configSet = ConfigFactory.load()

  implicit class RichURLList(optionalUrls: List[URL]) {
    /**
      * Add URL to load depending on scan condition.
      *
      * @param classLoader Target classloader.
      * @return
      */
    def scanAccepted(classLoader: ClassLoader): List[URL] = {

      logger.debug(s"Multi classpath loadable : [ $isMultiClassPathMode ] with [ ${scanningArchiveWhitelist.mkString(", ")} ]")
      logger.debug(optionalUrls.map(_.getPath).+:("-- Additional url candidation --").mkString("\n  "))

      {
        classLoader.getResources("").asScala.toList ++ {
          (isMultiClassPathMode, scanningArchiveWhitelist) match {
            case (true, Nil)       => optionalUrls
            case (true, whitelist) => optionalUrls.filter(url => whitelist.exists(url.getPath.contains))
            case _                 => Try {
              val thisPackage = this.getClass.getPackage.getName.dotToSlash
              val paths = "jar:" + classLoader.getResource(thisPackage).getPath.replace(thisPackage, "")
              Seq(new URL(paths))
            } match {
              case scala.util.Success(value) => value
              case _                         => Nil
            }
          }
        }
      } match {
        case x =>
          x.map(_.getPath).foreach(logger.debug)
          x
      }
    }
  }

}
