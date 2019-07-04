package com.phylage.scaladia

import com.typesafe.config.ConfigFactory

import collection.JavaConverters._
import scala.util.Try
object Config {
  val blackList: List[String] = Try {
    ConfigFactory.load().getStringList("di.unscaning.package").asScala.toList
  }.getOrElse(Nil)
}