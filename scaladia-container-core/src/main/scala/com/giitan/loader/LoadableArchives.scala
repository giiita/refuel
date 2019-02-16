package com.giitan.loader

import java.io.File
import java.util.jar.{JarEntry, JarFile}

import com.giitan.box.ScaladiaClassLoader.classLoader
import com.giitan.injector.AutoInject
import com.giitan.loader.RichClassCrowds.ClassCrowds
import StringURIConvertor._

import scala.collection.JavaConverters._
import scala.reflect._
import runtime.universe._
import scala.collection.mutable.ListBuffer

object LoadableArchives {

  def asAutoInjectSymbol(className: String): Type = {
    val x = runtimeMirror(classLoader)
      .staticClass(className)
      .info

    x.baseType(typeOf[AutoInject[_]].typeSymbol)
  }

  implicit class LoadableFile(file: File) {
    def isClassFile: Boolean = file.isFile && file.getName.isClassFile

    def classCrowdEntries(packageName: String): ClassCrowds = {
      file.list match {
        case null => ClassCrowds()
        case list =>
          ClassCrowds(
            list.to[Seq].map { path =>
              new File(file, path) match {
                case x if x.isClassFile =>
                  try {
                    val className = packageName.asParentPackagePrefix + x.getName.ignoreClass
                    val classType = classLoader.loadClass(className)
                    if (classOf[AutoInject[_]].isAssignableFrom(classType) && !classType.isInterface)
                      ClassCrowds(
                        ListBuffer(
                          ClassCrowd(
                            classType,
                            asAutoInjectSymbol(className)
                          )
                        )
                      )
                    else ClassCrowds()
                  } catch {
                    case _: Throwable => ClassCrowds()
                  }
                case directory if directory.isDirectory =>
                  directory.classCrowdEntries(packageName.asParentPackagePrefix + directory.getName)
                case _ => ClassCrowds()
              }
            }.flatMap(_.value)
          )
      }
    }
  }

  implicit class LoadableJarEntry(jarEntry: JarEntry) {
    def isClassFile: Boolean = jarEntry.getName.isClassFile
  }

  implicit class LoadableJarFile(jarFile: JarFile) {
    def classCrowdEntries(packageName: String): ClassCrowds = {
      ClassCrowds(
        jarFile.entries.asScala.toList.collect {
          case entry if entry.getName.slashToDot.startsWith(packageName) && entry.isClassFile =>
            try {
              val className = entry.getName.resourceNameToClassName
              val classType = classLoader.loadClass(entry.getName.resourceNameToClassName)
              if (classOf[AutoInject[_]].isAssignableFrom(classType) && !classType.isInterface) {
                ClassCrowds(
                  Seq(
                    ClassCrowd(
                      classType,
                      asAutoInjectSymbol(className)
                    )
                  )
                )
              } else ClassCrowds()
            } catch {
              case _: Throwable => ClassCrowds()
            }
        }.flatMap(_.value)
      )
    }
  }
}
