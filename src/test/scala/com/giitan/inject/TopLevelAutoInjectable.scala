package com.giitan.inject

import com.giitan.injector.AutoInjector

object TopLevelAutoInjectable extends TopLevelAutoInjectable

trait TopLevelAutoInjectable extends AutoInjector {me =>
  depends[TopLevelAutoInjectable](me).acceptedGlobal
}
