package com.phylage.scaladia

package object runtime {
  private[runtime] implicit class StringUri(uri: String) {
    def isJar: Boolean = uri == "jar"

    def isFile: Boolean = uri == "file"

    def isModuleSymbol: Boolean = uri.endsWith("$.class")

    def isClassSymbol: Boolean = uri.endsWith(".class")

    def slashToDot: String =
      uri.replace('/', '.')
  }

  implicit class RichPackagePathEntries(v: Set[PackagePathEntries]) {
    def join: PackagePathEntries = new PackagePathEntries(
      v.flatMap(_.moduleSymbolPath),
      v.flatMap(_.classSymbolPath)
    )
  }
}
