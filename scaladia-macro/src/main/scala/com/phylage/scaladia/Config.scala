package com.phylage.scaladia

import com.typesafe.config.ConfigFactory

import scala.jdk.CollectionConverters._
import scala.util.Try
object Config {
  lazy val blackList: List[String] = Try {
    ConfigFactory.load().getStringList("di.unscaning.package").asScala.toList
  }.getOrElse(Nil)
}
