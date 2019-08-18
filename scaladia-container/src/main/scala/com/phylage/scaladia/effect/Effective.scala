package com.phylage.scaladia.effect

import java.lang.annotation.{Retention, RetentionPolicy}

@Retention(RetentionPolicy.RUNTIME)
final class Effective(target: Effect) extends scala.annotation.StaticAnnotation {

}