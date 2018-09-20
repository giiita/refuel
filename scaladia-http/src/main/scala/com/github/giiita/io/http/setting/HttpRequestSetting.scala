package com.github.giiita.io.http.setting

import java.nio.charset.{Charset, StandardCharsets}

/**
  * Settings that are always reflected when making a request.
  * When changing, AutoInject injection is possible.
  *
  * {{{
  *   object MySetting extends HttpRequestSetting(retryThreshold = 3) with AutoInject[HttpRequestSetting]
  * }}}
  *
  * @param retryThreshold Retry threshold. When 2 is set, up to one failure is allowed.
  * @param globalHeader   The header set here will be given to all requests.
  * @param bodyEncoding   Body encoding charset.
  * @param timeout        Request timeout milliseconds.
  */
class HttpRequestSetting(val retryThreshold: Int = 1,
                         val globalHeader: Seq[Header] = Seq(Header("Content-type", "application/json")),
                         val bodyEncoding: Charset = StandardCharsets.UTF_8,
                         val timeout: Int = 30 * 1000)
