package com.phylage.scaladia.internal

import scala.reflect.macros.blackbox

trait PropertyDebugModeEnabler {
  val xxx = System.getProperties
  private[this] val debugEnabled =
    Option(System.getProperty("scaladia.macro.debug")).
      filterNot(_.isEmpty).map(_.toLowerCase).exists { v =>
      "true".equals(v) || v.substring(0, 1) == "y"
    }

  protected def debuglog(c: blackbox.Context)(message: String): Unit = {
    if (debugEnabled) {
      c.info(c.enclosingPosition, message, force = true)
    } else ()
  }
}
