package com.phylage.scaladia.injector

import java.lang.annotation.{Retention, RetentionPolicy}

@Retention(RetentionPolicy.CLASS)
final class AutoDI extends scala.annotation.StaticAnnotation {

}
