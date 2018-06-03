package com.giitan.loader

import scala.reflect._
import runtime.universe._


case class ClassCrowd[T](instanceClass: Class[T], dependencyType: Type)