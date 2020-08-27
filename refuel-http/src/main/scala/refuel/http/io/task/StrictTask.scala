package refuel.http.io.task

import akka.actor.ActorSystem
import akka.http.scaladsl.model.HttpResponse
import akka.util.ByteString
import refuel.http.io.setting.HttpSetting

import scala.concurrent.Future
import scala.concurrent.duration._

private[refuel] class StrictTask(response: HttpResponse)(
    implicit setting: HttpSetting,
    timeout: FiniteDuration = 30.seconds
) extends CombineTask[String] {

  /**
    * Execute future functions.
    *
    * @return
    */
  override def run(implicit as: ActorSystem): Future[String] = {
    response.entity
      .toStrict(timeout)
      .flatMap { x =>
        setting.responseBuilder(x).dataBytes.runFold(ByteString.empty)(_ ++ _).map(_.utf8String)(as.dispatcher)
      }(as.dispatcher)
  }
}
