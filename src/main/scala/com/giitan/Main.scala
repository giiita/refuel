package com.giitan

import com.giitan.injector.Injector

object Main extends Injector {
  def main(args: Array[String]) = {
    println(inject[TopLevelAutoInjectable].toString)
  }
}
