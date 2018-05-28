package com.giitan

import com.giitan.injectable.StoredDependency

package object injector {

  import scala.language.implicitConversions

  implicit def aliasCall[X](variable: StoredDependency[X]): X = variable.provide
}
