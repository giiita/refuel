package com.giitan.container

import com.giitan.box.Container

private[giitan] object AutomaticContainerInitializer {
  implicitly[Container].automaticDependencies.initialize()
}
