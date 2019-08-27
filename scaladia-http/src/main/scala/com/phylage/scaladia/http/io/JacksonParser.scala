package com.phylage.scaladia.http.io

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.phylage.scaladia.http.exception.DeserializingException
import com.phylage.scaladia.injector.Injector

import scala.reflect.ClassTag

object JacksonParser {
  val jackson: ObjectMapper = new ObjectMapper().registerModule(DefaultScalaModule)
}

trait JacksonParser extends Injector {
  /**
    * Serialize to json string.
    *
    * @param v Serialized value.
    * @tparam T Serialized value type.
    * @return
    */
  def serialize[T](v: T): String = JacksonParser.jackson.writeValueAsString(v)

  /**
    * Serialize from json string
    *
    * @param v JsonString
    * @tparam T Response type.
    * @return
    */
  def deserialize[T: ClassTag](v: String): T = try {
    JacksonParser.jackson.readValue(v, implicitly[ClassTag[T]].runtimeClass.asInstanceOf[Class[T]])
  } catch {
    case e: Throwable => throw new DeserializingException(s"Cannot deseialize to ${implicitly[ClassTag[T]]}", e)
  }
}
