package com.giitan

import com.giitan.injector.AutoInjector

object TopLevelAutoInjectable extends TopLevelAutoInjectable {
  depends[TopLevelAutoInjectable](this).acceptedGlobal
}

trait TopLevelAutoInjectable extends AutoInjector
