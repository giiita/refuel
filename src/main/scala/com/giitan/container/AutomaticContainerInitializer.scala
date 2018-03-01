package com.giitan.container

import com.giitan.box.Container
import com.giitan.exception.InjectableDefinitionException

private[giitan] object AutomaticContainerInitializer {
  implicitly[Container].automaticDependencies.foreach(r =>
    try {
      r.instance
    } catch {
      case _ : Throwable => throw new InjectableDefinitionException(s"$r can not be accessed.")
    }
  )
}
