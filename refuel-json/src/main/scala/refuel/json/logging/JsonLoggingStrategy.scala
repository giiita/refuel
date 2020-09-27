package refuel.json.logging

import com.typesafe.scalalogging.LazyLogging
import refuel.json.JsonVal

trait JsonLoggingStrategy extends LazyLogging {
  def jsonReadLogging(v: => JsonVal)(implicit logEnabled: JsonConvertLogEnabled): JsonVal = {
    val res = v
    if (logEnabled.enabled) {
      logger.info(s"Json reading: $res")
    }
    res
  }

  def jsonWriteLogging(v: => JsonVal)(implicit logEnabled: JsonConvertLogEnabled): JsonVal = {
    val res = v
    if (logEnabled.enabled) {
      logger.info(s"Json writing: $res")
    }
    res
  }
}
