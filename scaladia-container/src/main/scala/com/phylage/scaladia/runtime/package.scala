package com.phylage.scaladia

package object runtime {
  private[runtime] implicit class StringUri(uri: String) {
    def isJar: Boolean = uri == "jar"

    def isFile: Boolean = uri == "file"

    def isModuleSymbol: Boolean = uri.endsWith("$.class")

    def slashToDot: String =
      uri.replace('/', '.')
  }
}
