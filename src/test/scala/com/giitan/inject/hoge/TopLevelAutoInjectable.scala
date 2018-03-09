package com.giitan.inject.hoge

import com.giitan.injector.AutoInjector

object TopLevelAutoInjectable extends TopLevelAutoInjectable {
  println("★★★★★★★★★★★★★登録")
  depends[TopLevelAutoInjectable](this).acceptedGlobal
}

trait TopLevelAutoInjectable extends AutoInjector
