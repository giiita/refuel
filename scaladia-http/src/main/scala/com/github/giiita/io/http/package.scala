package com.github.giiita.io

import com.github.giiita.io.http.HttpMethod.{DELETE, GET, POST, PUT}
import dispatch.Req

package object http {

  class MethodType[T <: HttpMethod.Method](val method: Req => Req)

  implicit case object GET extends MethodType[GET](_.GET)
  implicit case object PUT extends MethodType[PUT](_.PUT)
  implicit case object POST extends MethodType[POST](_.POST)
  implicit case object DELETE extends MethodType[DELETE](_.DELETE)
}
