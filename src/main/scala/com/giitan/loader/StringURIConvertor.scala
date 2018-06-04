package com.giitan.loader

object StringURIConvertor {
  implicit class StringUri(uri: String) {
    def ignoreClass: String = uri.replaceAll(".class$", "")

    def isClassFile: Boolean = uri.endsWith(".class")

    def resourceNameToClassName: String =
      uri.slashToDot.ignoreClass

    def slashToDot: String =
      uri.replace('/', '.')

    def dotToSlash: String =
      uri.replace('.', '/')

    def asParentPackagePrefix: String = if (uri.isEmpty) "" else s"$uri."
  }
}
