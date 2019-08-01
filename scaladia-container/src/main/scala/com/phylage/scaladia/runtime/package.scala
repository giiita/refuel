package com.phylage.scaladia

package object runtime {
  implicit class StringUri(uri: String) {
    def isClassFile: Boolean = uri.endsWith(".class")

    def onlyPackage: String = uri.replaceAll("/[^/]+$", "")

    def slashToDot: String =
      uri.replace('/', '.')

    def dotToSlash: String =
      uri.replace('.', '/')

    def asParentPackagePrefix: String = if (uri.isEmpty) "" else s"$uri."
  }
}
