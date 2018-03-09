package com.giitan.inject.hoge

import com.giitan.annotation.AutoLoad
import com.giitan.injector.AutoInjector

@AutoLoad
object TopLevelAutoInjectable extends TopLevelAutoInjectable

trait TopLevelAutoInjectable extends AutoInjector {me =>
  depends[TopLevelAutoInjectable](me).acceptedGlobal
}
