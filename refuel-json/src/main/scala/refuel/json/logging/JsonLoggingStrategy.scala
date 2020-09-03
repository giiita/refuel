package refuel.json.logging

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import refuel.json.JsonVal

trait JsonLoggingStrategy extends LazyLogging {
  private[this] val logEnabled: Boolean = {
    val conf = ConfigFactory.load()
    if (conf.hasPath("json.logging.enabled")) conf.getBoolean("json.logging.enabled") else false
  }

  def jsonReadLogging(v: String): Unit = {
    if (logEnabled) {
      logger.info(s"Json reading: $v")
    }
  }

  def jsonWriteLogging(v: => JsonVal): JsonVal = {
    val res = v
    if (logEnabled) {
      logger.info(s"Json writing: $res")
    }
    res
  }
}
