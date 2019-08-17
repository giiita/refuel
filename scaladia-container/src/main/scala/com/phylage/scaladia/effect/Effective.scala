package com.phylage.scaladia.effect

import java.lang.annotation.{Retention, RetentionPolicy}

@Retention(RetentionPolicy.CLASS)
final class Effective(target: Effect) extends scala.annotation.StaticAnnotation {

}