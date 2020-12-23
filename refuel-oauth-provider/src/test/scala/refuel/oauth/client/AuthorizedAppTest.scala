package refuel.oauth.client

import akka.http.scaladsl.model.Uri
import org.scalatest.diagrams.Diagrams
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import refuel.oauth.client.AuthorizedAppTest.MyApp
import refuel.oauth.exception.AuthorizeChallengeException

import scala.util.Success

object AuthorizedAppTest {
  case class MyApp(clientId: String = "AAA", clientSecret: String = "BBB", callbackUri: Iterable[Uri] = None)
      extends AuthorizedApp
}

class AuthorizedAppTest extends AsyncWordSpec with Matchers with Diagrams {

  "redirect uri verification" should {
    "Unregisted redirection of authorized app" in {
      intercept[AuthorizeChallengeException] {
        MyApp().verifyRedirection(None).get
      }.getMessage shouldBe "Invalid redirection setting."

      MyApp().verifyRedirection(Some(Uri("test"))) shouldBe Success(Uri("test"))
    }

    "Registed a redirection of authorized app" in {
      val app = MyApp(callbackUri = Some(Uri("test")))
      app.verifyRedirection(None) shouldBe Success(Uri("test"))
      app.verifyRedirection(Some(Uri("test"))) shouldBe Success(Uri("test"))
      intercept[AuthorizeChallengeException] {
        app.verifyRedirection(Some(Uri("different"))).get
      }.getMessage shouldBe "Invalid redirection setting."
    }

    "Registed some redirection of authorized app" in {
      val app = MyApp(callbackUri = Seq(Uri("test_1"), Uri("test_2")))
      app.verifyRedirection(Some(Uri("test_1"))) shouldBe Success(Uri("test_1"))
      app.verifyRedirection(Some(Uri("test_2"))) shouldBe Success(Uri("test_2"))
      intercept[AuthorizeChallengeException] {
        app.verifyRedirection(Some(Uri("different"))).get
      }.getMessage shouldBe "Invalid redirection setting."
    }

    "Redirection handle with fragments" in {
      intercept[AuthorizeChallengeException] {
        MyApp(callbackUri = Some(Uri("test#bar"))).verifyRedirection(None).get
      }.getMessage shouldBe "Fragment components cannot be included in redirects."

      intercept[AuthorizeChallengeException] {
        MyApp(callbackUri = Seq(Uri("test"), Uri("yrdy"))).verifyRedirection(Some(Uri("test#bar"))).get
      }.getMessage shouldBe "Fragment components cannot be included in redirects."

      intercept[AuthorizeChallengeException] {
        MyApp(callbackUri = Seq(Uri("test#bar"), Uri("yrdy"))).verifyRedirection(None).get
      }.getMessage shouldBe "Fragment components cannot be included in redirects."
    }
  }
}
