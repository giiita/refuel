package com.giitan.inject

import com.giitan.injector.AutoInjector

object TopLevelAutoInjectable extends TopLevelAutoInjectable {
  depends[TopLevelAutoInjectable](this)
}

trait TopLevelAutoInjectable extends AutoInjector
