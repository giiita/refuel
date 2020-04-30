package refuel.lang

import java.time.{ZoneId, ZoneOffset}
import java.util.TimeZone

import refuel.injector.AutoInject

object TestRuntimeTZ extends RuntimeTZ with AutoInject {
  override val TIME_ZONE: TimeZone     = TimeZone.getTimeZone("Asia/Tokyo")
  override val ZONE_ID: ZoneId         = ZoneId.of("Asia/Tokyo")
  override val ZONE_OFFSET: ZoneOffset = ZoneOffset.ofHours(9)
  override val DEFAULT_FORMAT: String  = RuntimeTZ.DEFAULT_FORMAT
}
