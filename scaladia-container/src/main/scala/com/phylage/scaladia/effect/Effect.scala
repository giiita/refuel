package com.phylage.scaladia.effect

import com.phylage.scaladia.injector.AutoInject

/**
  * Effect for dependency handling in runtime classpath.
  * When effective injection is performed,
  * only the activated ones of all effects are candidates for injection.
  *
  * {{{
  *   object Effects {
  *     def getKind = sys.props.getOrElse("env", "local")
  *
  *     object LOCAL extends Effect {
  *       def activate: Boolean = getKind == "local"
  *     }
  *     object DEV extends Effect {
  *       def activate: Boolean = getKind == "dev"
  *     }
  *     object STG extends Effect {
  *       def activate: Boolean = getKind == "stg"
  *     }
  *     object PRD extends Effect {
  *       def activate: Boolean = getKind == "prd"
  *     }
  *   }
  *
  *   @Effective(LOCAL)
  *   object LocalRuntimeSetting extends Setting with AutoInject[Setting]
  *
  *   @Effective(DEV)
  *   object DevRuntimeSetting extends Setting with AutoInject[Setting]
  *
  *   @Effective(STG)
  *   object StgRuntimeSetting extends Setting with AutoInject[Setting]
  *
  *   @Effective(PRD)
  *   object PrdRuntimeSetting extends Setting with AutoInject[Setting]
  * }}}
  */
trait Effect extends AutoInject[Effect] {
  def activate: Boolean
}
