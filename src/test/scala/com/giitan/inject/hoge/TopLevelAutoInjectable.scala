package com.giitan.inject.hoge

import com.giitan.injector.AutoInjector

object TopLevelAutoInjectable extends TopLevelAutoInjectable {
  depends[TopLevelAutoInjectable](this).acceptedGlobal
}

trait TopLevelAutoInjectable extends AutoInjector
