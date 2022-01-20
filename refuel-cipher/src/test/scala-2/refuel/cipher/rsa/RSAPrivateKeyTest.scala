package refuel.cipher.rsa

import org.scalatest.diagrams.Diagrams
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import refuel.inject.Injector

class RSAPrivateKeyTest extends AsyncWordSpec with Matchers with Diagrams with Injector {
  "fromBase64Encoded" should {
    "pkcs8" in {
      val encoded = Seq(
        "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQC3spFiW01QJsfm",
        "+5tqb4d2JNTKW388/vrAwqYLPwXQ9IMII1FhpBO1Q+fVfwn6Ku13exyD68jwu/J0",
        "TicZaFzU0E57wBDtOzqYBLG9t87VVlL//wvDK0L1IRPJm45JYdUaLQ3iad5Wnzi9",
        "c6oGIPbYYHw+QegCydKetMAQRKH72FibIXcf+BFjmVwa4GxdnWgQLhLL3RnTVQot",
        "0NphaLpbKx55V8UXcbyco4Qe5oDeU0Mry0RO7sCXfjUrIv8qu/VUUEmbOhdMuHr5",
        "O2fqmdGwa3aSydlhbOjDdX6VnPQ78gQpudgaN/aMYnoX5nSQKNiJgZ2bKcxAbKmj",
        "y8/u62ojAgMBAAECggEAPGo8Km8AxGWcxTjm6mB32sFOQqdI95Scf50E4xn9HbXt",
        "ClYjQ8ukt673pl27uE06AFu7CyAW3CXUHbA+Z7rNHSjNRyHp7zvVj32rv0ueeMeF",
        "ULBe+/dUdC5Gxb2lVDkazxsWP+q1tyNyk8xglNdul8dT1V3zokciQBQOPstzwtHb",
        "SgYU0pDs98T90dCqh9yd29QWdjry+FzwVkXWxECoppsEeIY1zQ214nIeIz0tOL72",
        "udgMll/6OiHLCaw8d5tJiBP2wApbewykPEDjTncf+pCOyp1h3FxjrYcqsCcPBThY",
        "VSck89pj/Vh9VT+OdVSDMTgO7bIjQwy3lBJn2LULwQKBgQDtQ5pYTT0W0wjy/tFM",
        "GvH/MJwQKFgH1WRM418SEiiY36IW6xukssjGre4TUnVlWDv7gNCJFjyUdJhjEE61",
        "GZ+h/9xoGguebO2/q8te5ezx2czHkoIl3/aRJV98b4FTXjhN2cWCMIsUvpYe7/Q0",
        "DwjiyLAuFU5pui6f2KLi68WmUwKBgQDGNBgJCGTyPhDg8L/Oa636e1oEm0i4Wfcx",
        "OJpXqmcNqA4EWq4SbzZ0L0XcGrhplND/TX9AW/bi7J5y7X2t9JvA9qcoFBzH6Wk0",
        "fHtGt0FDHHmQM5xtVq2F5FE/wHdglWzG/mmDptaq70Mqv3bgK5L42C1vK2NRvWBU",
        "/YTnRjgS8QKBgQDOFgNRob1nPuUuFBH99gWvMCzOiAx2uW7GrlRc4PLsXotvNsYr",
        "F7P3hwodNGmOHsbzHR+Fg5AZLvFSUs7a1SfoUdLDhaQD9v/HldQvWK2oIROTYfm0",
        "JkYIKxs2fbAqltN3I6o//CRCX40L0EcglNKWEdWeIZmbkkF/TG+nhT2HywKBgQCW",
        "YuRxxhrB6vHnmwQN3YoOahWtNR2CM1Dp2ebR6YtzwzWPfDZj6lpI4a2CCgwuoCnL",
        "7MNG1ACJwbufvC8GFHkxenTuZgRIcwPOPT1UNKuh/jMGhQKFu6TB2LEFTEXH1NjK",
        "TDNHHwn2fMBb6RaLlMRZZ71hgmxDkwtKsWlsCc12IQKBgQDBbJtkTaXcEBx/Vg1A",
        "+l9BhRpuJh7zXvbNki0mkTqq9i7ygUaWL7Ca6FfUq4Hs3jBmHkE+p7bIuNvx1MXZ",
        "WSIPfET0M04xCR9G9wQpGM07BIeg5H3JMKj6VYIphELiF4ngCgCLDFtGVhdTK+QA",
        "Gi44ik2bp71X+kpcKiHT8X3nYA==",
      ).mkString
      val result = RSAPrivateKey.fromBase64Encoded(encoded)
      result.map { key =>
        key.serialize shouldBe encoded
      } getOrElse fail()
    }
  }

  "fromFile" should {
    "pkcs8" in {
      val encoded = Seq(
        "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQC3spFiW01QJsfm",
        "+5tqb4d2JNTKW388/vrAwqYLPwXQ9IMII1FhpBO1Q+fVfwn6Ku13exyD68jwu/J0",
        "TicZaFzU0E57wBDtOzqYBLG9t87VVlL//wvDK0L1IRPJm45JYdUaLQ3iad5Wnzi9",
        "c6oGIPbYYHw+QegCydKetMAQRKH72FibIXcf+BFjmVwa4GxdnWgQLhLL3RnTVQot",
        "0NphaLpbKx55V8UXcbyco4Qe5oDeU0Mry0RO7sCXfjUrIv8qu/VUUEmbOhdMuHr5",
        "O2fqmdGwa3aSydlhbOjDdX6VnPQ78gQpudgaN/aMYnoX5nSQKNiJgZ2bKcxAbKmj",
        "y8/u62ojAgMBAAECggEAPGo8Km8AxGWcxTjm6mB32sFOQqdI95Scf50E4xn9HbXt",
        "ClYjQ8ukt673pl27uE06AFu7CyAW3CXUHbA+Z7rNHSjNRyHp7zvVj32rv0ueeMeF",
        "ULBe+/dUdC5Gxb2lVDkazxsWP+q1tyNyk8xglNdul8dT1V3zokciQBQOPstzwtHb",
        "SgYU0pDs98T90dCqh9yd29QWdjry+FzwVkXWxECoppsEeIY1zQ214nIeIz0tOL72",
        "udgMll/6OiHLCaw8d5tJiBP2wApbewykPEDjTncf+pCOyp1h3FxjrYcqsCcPBThY",
        "VSck89pj/Vh9VT+OdVSDMTgO7bIjQwy3lBJn2LULwQKBgQDtQ5pYTT0W0wjy/tFM",
        "GvH/MJwQKFgH1WRM418SEiiY36IW6xukssjGre4TUnVlWDv7gNCJFjyUdJhjEE61",
        "GZ+h/9xoGguebO2/q8te5ezx2czHkoIl3/aRJV98b4FTXjhN2cWCMIsUvpYe7/Q0",
        "DwjiyLAuFU5pui6f2KLi68WmUwKBgQDGNBgJCGTyPhDg8L/Oa636e1oEm0i4Wfcx",
        "OJpXqmcNqA4EWq4SbzZ0L0XcGrhplND/TX9AW/bi7J5y7X2t9JvA9qcoFBzH6Wk0",
        "fHtGt0FDHHmQM5xtVq2F5FE/wHdglWzG/mmDptaq70Mqv3bgK5L42C1vK2NRvWBU",
        "/YTnRjgS8QKBgQDOFgNRob1nPuUuFBH99gWvMCzOiAx2uW7GrlRc4PLsXotvNsYr",
        "F7P3hwodNGmOHsbzHR+Fg5AZLvFSUs7a1SfoUdLDhaQD9v/HldQvWK2oIROTYfm0",
        "JkYIKxs2fbAqltN3I6o//CRCX40L0EcglNKWEdWeIZmbkkF/TG+nhT2HywKBgQCW",
        "YuRxxhrB6vHnmwQN3YoOahWtNR2CM1Dp2ebR6YtzwzWPfDZj6lpI4a2CCgwuoCnL",
        "7MNG1ACJwbufvC8GFHkxenTuZgRIcwPOPT1UNKuh/jMGhQKFu6TB2LEFTEXH1NjK",
        "TDNHHwn2fMBb6RaLlMRZZ71hgmxDkwtKsWlsCc12IQKBgQDBbJtkTaXcEBx/Vg1A",
        "+l9BhRpuJh7zXvbNki0mkTqq9i7ygUaWL7Ca6FfUq4Hs3jBmHkE+p7bIuNvx1MXZ",
        "WSIPfET0M04xCR9G9wQpGM07BIeg5H3JMKj6VYIphELiF4ngCgCLDFtGVhdTK+QA",
        "Gi44ik2bp71X+kpcKiHT8X3nYA==",
      ).mkString
      val result = RSAPrivateKey.fromFile(getClass.getClassLoader.getResource("pkcs8.pem").toURI)
      result.map { key =>
        key.serialize shouldBe encoded
      }.get
    }
  }
}