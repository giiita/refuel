package refuel

import akka.http.scaladsl.marshalling.ToResponseMarshaller
import akka.http.scaladsl.model.ContentTypes
import akka.http.scaladsl.model.headers.{`Cache-Control`, CacheDirectives}
import refuel.json.JsonTransform
import refuel.oauth.token.AccessToken

package object oauth extends JsonTransform {
  implicit def accessTokenMarshaller: ToResponseMarshaller[AccessToken] = {
    implicitly[ToResponseMarshaller[String]]
      .map(
        _.mapEntity(_.withContentType(ContentTypes.`application/json`)).mapHeaders(
          _ ++ Seq(
            `Cache-Control`.apply(CacheDirectives.`no-store`)
          )
        )
      )
      .compose[AccessToken](_.encodedStr)
  }
}
