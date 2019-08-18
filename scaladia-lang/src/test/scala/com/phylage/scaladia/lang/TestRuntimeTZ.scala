package com.phylage.scaladia.lang

import java.time.{ZoneId, ZoneOffset}
import java.util.TimeZone

import com.phylage.scaladia.injector.AutoInject

object TestRuntimeTZ extends RuntimeTZ with AutoInject[RuntimeTZ] {
  override val TIME_ZONE: TimeZone = TimeZone.getTimeZone("Asia/Tokyo")
  override val ZONE_ID: ZoneId = ZoneId.of("Asia/Tokyo")
  override val ZONE_OFFSET: ZoneOffset = ZoneOffset.ofHours(9)
  override val DEFAULT_FORMAT: String = RuntimeTZ.DEFAULT_FORMAT
}
