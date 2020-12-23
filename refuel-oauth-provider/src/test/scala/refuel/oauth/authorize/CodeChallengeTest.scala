package refuel.oauth.authorize

import org.scalatest.diagrams.Diagrams
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import refuel.injector.Injector
import refuel.oauth.exception.{InvalidGrantException, RequirementFailedException}

import scala.util.Success

class CodeChallengeTest extends AsyncWordSpec with Matchers with Diagrams with Injector {
  "code challenge pure construnction" should {
    "success creation - plain" in {
      val codeChallenge = CodeChallenge.pure("xxx", "plain")
      assert(codeChallenge.isSuccess)
      codeChallenge.map(_.value) shouldBe Success("xxx")
      codeChallenge.map(_.method) shouldBe Success(CodeChallenge.Plain)
    }
    "success creation - S256" in {
      val codeChallenge = CodeChallenge.pure("xxx", "S256")
      assert(codeChallenge.isSuccess)
      codeChallenge.map(_.value) shouldBe Success("xxx")
      codeChallenge.map(_.method) shouldBe Success(CodeChallenge.S256)
    }
    "failure creation - s256" in {
      val codeChallenge = CodeChallenge.pure("xxx", "s256")
      assert(codeChallenge.isFailure)
      val e = codeChallenge.failed.get
      e.getMessage shouldBe "Unsupported challenge code method."
      assert(e.isInstanceOf[InvalidGrantException])
    }
  }

  "code challenge map construnction" should {
    "success creation - plain" in {
      val codeChallenge = CodeChallenge.fromRequest(
        Map(
          "code_challenge"        -> "xxx",
          "code_challenge_method" -> "plain"
        )
      )
      assert(codeChallenge.isSuccess)
      codeChallenge.map(_.map(_.value)) shouldBe Success(Some("xxx"))
      codeChallenge.map(_.map(_.method)) shouldBe Success(Some(CodeChallenge.Plain))
    }
    "success creation - S256" in {
      val codeChallenge = CodeChallenge.fromRequest(
        Map(
          "code_challenge"        -> "xxx",
          "code_challenge_method" -> "S256"
        )
      )
      assert(codeChallenge.isSuccess)
      codeChallenge.map(_.map(_.value)) shouldBe Success(Some("xxx"))
      codeChallenge.map(_.map(_.method)) shouldBe Success(Some(CodeChallenge.S256))
    }
    "success creation - None" in {
      val codeChallenge = CodeChallenge.fromRequest(Map())
      assert(codeChallenge.isSuccess)
      codeChallenge shouldBe Success(None)
    }
    "failure creation - s256" in {
      val codeChallenge = CodeChallenge.fromRequest(
        Map(
          "code_challenge"        -> "xxx",
          "code_challenge_method" -> "s256"
        )
      )
      assert(codeChallenge.isFailure)
      val e = codeChallenge.failed.get
      e.getMessage shouldBe "Unsupported challenge code method."
      assert(e.isInstanceOf[InvalidGrantException])
    }
    "failure creation - only code" in {
      val codeChallenge = CodeChallenge.fromRequest(
        Map(
          "code_challenge" -> "xxx"
        )
      )
      assert(codeChallenge.isFailure)
      val e = codeChallenge.failed.get
      e.getMessage shouldBe "Both code_challenge and code_challenge_method must be specified, or none of them"
      assert(e.isInstanceOf[RequirementFailedException])
    }
    "failure creation - only method" in {
      val codeChallenge = CodeChallenge.fromRequest(
        Map(
          "code_challenge_method" -> "S256"
        )
      )
      assert(codeChallenge.isFailure)
      val e = codeChallenge.failed.get
      e.getMessage shouldBe "Both code_challenge and code_challenge_method must be specified, or none of them"
      assert(e.isInstanceOf[RequirementFailedException])
    }
  }

  "challenge code verification - plain" should {
    "same verifier" in {
      CodeChallenge.pure("xxx", "plain").map { x => x.verify("xxx") shouldBe true }.getOrElse(fail())
    }
    "diff verifier" in {
      CodeChallenge.pure("xxx", "plain").map { x => x.verify("yyy") shouldBe false }.getOrElse(fail())
    }
  }

  "challenge code verification - S256" should {
    "same verifier" in {
      CodeChallenge
        .pure("zS6wg3ybTJYsItL_i1RBt7RYBYh_BR05vxM7WDuvaGA", "S256")
        .map { x => x.verify("xxx") shouldBe true }
        .getOrElse(fail())
    }
    "diff verifier" in {
      CodeChallenge.pure("xxx", "S256").map { x => x.verify("yyy") shouldBe false }.getOrElse(fail())
    }
  }
}
