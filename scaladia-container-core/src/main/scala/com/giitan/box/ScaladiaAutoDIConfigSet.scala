package com.giitan.box

import java.net.URL

import com.typesafe.config.ConfigFactory

import scala.collection.JavaConverters._
import scala.util.Try

private[giitan] object ScaladiaAutoDIConfigSet {
  private lazy val SCALADIA_CONFIG_SET_PATH = "scaladia"
  private lazy val LOAD_MULTI_CLASSPATHSET = "fullscan"
  private lazy val SCANING_ARCHIVE_WHITELIST = "whitelist"
  private lazy val isMultiClassPathMode: Boolean = Try {
    configSet.getBoolean(s"$SCALADIA_CONFIG_SET_PATH.$LOAD_MULTI_CLASSPATHSET")
  }.getOrElse(false)
  private lazy val scaningArchiveWhitelist: Seq[String] = {
    Try {
      configSet.getStringList(s"$SCALADIA_CONFIG_SET_PATH.$SCANING_ARCHIVE_WHITELIST").asScala
    }.getOrElse(Nil)
  }
  private val configSet = ConfigFactory.load()

  implicit class RichURLList(optionalUrls: List[URL]) {
    /**
      * Add URL to load depending on scan condition.
      *
      * @param withDefault Default url set.
      * @return
      */
    def scanAccepted(withDefault: List[URL]): List[URL] = {
      withDefault ++ {
        (isMultiClassPathMode, scaningArchiveWhitelist) match {
          case (true, Nil) => optionalUrls
          case (_, whitelist@_) if whitelist.nonEmpty => optionalUrls.filter(url => whitelist.exists(url.getPath.contains))
          case _ => Nil
        }
      }
    }
  }

}
