package com.github.giiita

import com.github.giiita.container.Container
import com.github.giiita.provider.Lazy

package object injector extends Injector {
  implicit val container: Lazy[Container] = inject[Container]
}
