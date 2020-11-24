package refuel.authorizer

import java.util

import org.pac4j.core.client.{Client, IndirectClient}
import org.pac4j.core.context.{HttpConstants, WebContext}
import org.pac4j.core.credentials.Credentials
import org.pac4j.core.matching.checker.MatchingChecker
import org.pac4j.core.matching.matcher.csrf.{CsrfTokenGeneratorMatcher, DefaultCsrfTokenGenerator}
import org.pac4j.core.matching.matcher.{
  CacheControlMatcher,
  CorsMatcher,
  DefaultMatchers,
  HttpMethodMatcher,
  Matcher,
  StrictTransportSecurityMatcher,
  XContentTypeOptionsMatcher,
  XFrameOptionsMatcher,
  XSSProtectionMatcher
}
import org.pac4j.core.util.{CommonHelper, Pac4jConstants}
import refuel.domination.Inject
import refuel.domination.InjectionPriority.Finally
import refuel.injector.AutoInject
import refuel.saml.SAMLAuthConfig

import scala.collection.JavaConverters.{asScalaBufferConverter, mapAsScalaMapConverter}

@Inject(Finally)
class CustomOriginMatchingChecker(samlConfig: SAMLAuthConfig) extends MatchingChecker with AutoInject {
  private[this] lazy final val GET_MATCHER: Matcher    = new HttpMethodMatcher(HttpConstants.HTTP_METHOD.GET)
  private[this] lazy final val POST_MATCHER: Matcher   = new HttpMethodMatcher(HttpConstants.HTTP_METHOD.POST)
  private[this] lazy final val PUT_MATCHER: Matcher    = new HttpMethodMatcher(HttpConstants.HTTP_METHOD.PUT)
  private[this] lazy final val DELETE_MATCHER: Matcher = new HttpMethodMatcher(HttpConstants.HTTP_METHOD.DELETE)

  private[this] lazy final val STRICT_TRANSPORT_MATCHER: StrictTransportSecurityMatcher =
    new StrictTransportSecurityMatcher
  private[this] lazy final val X_CONTENT_TYPE_OPTIONS_MATCHER: XContentTypeOptionsMatcher =
    new XContentTypeOptionsMatcher
  private[this] lazy final val X_FRAME_OPTIONS_MATCHER: XFrameOptionsMatcher = new XFrameOptionsMatcher
  private[this] lazy final val XSS_PROTECTION_MATCHER: XSSProtectionMatcher  = new XSSProtectionMatcher
  private[this] lazy final val CACHE_CONTROL_MATCHER: CacheControlMatcher    = new CacheControlMatcher
  private[this] lazy final val CSRF_TOKEN_MATCHER: CsrfTokenGeneratorMatcher = {
    val matcher = new CsrfTokenGeneratorMatcher(
      new DefaultCsrfTokenGenerator
    )
    matcher.setSecure(true)
    matcher
  }
  private[this] val CORS_MATCHER: CorsMatcher = {
    val matcher = new CorsMatcher
    samlConfig.cookieDomain.fold(matcher.setAllowOrigin("*"))(x => matcher.setAllowOrigin(s"https://$x/"))
    matcher.setAllowCredentials(true)
    val methods: util.HashSet[HttpConstants.HTTP_METHOD] = new util.HashSet[HttpConstants.HTTP_METHOD]
    methods.add(HttpConstants.HTTP_METHOD.GET)
    methods.add(HttpConstants.HTTP_METHOD.PUT)
    methods.add(HttpConstants.HTTP_METHOD.POST)
    methods.add(HttpConstants.HTTP_METHOD.DELETE)
    methods.add(HttpConstants.HTTP_METHOD.OPTIONS)
    matcher.setAllowMethods(methods)
    matcher
  }

  override def matches(
      context: WebContext,
      matchersValue: String,
      matchersMap: util.Map[String, Matcher],
      clients: util.List[Client[_ <: Credentials]]
  ): Boolean = {
    var matcherNames: String = matchersValue
    // if we have no matchers defined, compute the default one(s)
    if (CommonHelper.isBlank(matcherNames)) matcherNames = computeDefaultMatchers(clients)
    val matchers: util.List[Matcher] = new util.ArrayList[Matcher]
    // we must have matchers defined
    CommonHelper.assertNotNull("matchersMap", matchersMap)
    val allMatchers: util.Map[String, Matcher] = buildAllMatchers(matchersMap)
    val names: Array[String]                   = matcherNames.split(Pac4jConstants.ELEMENT_SEPARATOR)
    val nb: Int                                = names.length
    for (i <- 0 until nb) {
      val name: String = names(i).trim
      if (DefaultMatchers.HSTS.equalsIgnoreCase(name)) matchers.add(STRICT_TRANSPORT_MATCHER)
      else if (DefaultMatchers.NOSNIFF.equalsIgnoreCase(name)) matchers.add(X_CONTENT_TYPE_OPTIONS_MATCHER)
      else if (DefaultMatchers.NOFRAME.equalsIgnoreCase(name)) matchers.add(X_FRAME_OPTIONS_MATCHER)
      else if (DefaultMatchers.XSSPROTECTION.equalsIgnoreCase(name)) matchers.add(XSS_PROTECTION_MATCHER)
      else if (DefaultMatchers.NOCACHE.equalsIgnoreCase(name)) matchers.add(CACHE_CONTROL_MATCHER)
      else if (DefaultMatchers.SECURITYHEADERS.equalsIgnoreCase(name)) {
        matchers.add(CACHE_CONTROL_MATCHER)
        matchers.add(X_CONTENT_TYPE_OPTIONS_MATCHER)
        matchers.add(STRICT_TRANSPORT_MATCHER)
        matchers.add(X_FRAME_OPTIONS_MATCHER)
        matchers.add(XSS_PROTECTION_MATCHER)
      } else if (DefaultMatchers.CSRF_TOKEN.equalsIgnoreCase(name)) matchers.add(CSRF_TOKEN_MATCHER)
      else if (DefaultMatchers.ALLOW_AJAX_REQUESTS.equalsIgnoreCase(name)) {
        matchers.add(CORS_MATCHER)
        // we don't add any matcher for none
      } else if (!DefaultMatchers.NONE.equalsIgnoreCase(name)) {
        var result: Matcher = null
        allMatchers.asScala.collectFirst {
          case (key, value) if CommonHelper.areEqualsIgnoreCaseAndTrim(key, name) =>
            result = value
        }
        // we must have a matcher defined for this name
        CommonHelper.assertNotNull("allMatchers['" + name + "']", result)
        matchers.add(result)
      }
    }
    matchers.asScala.collectFirst {
      case matcher if !matcher.matches(context) => false
    } getOrElse true
  }

  protected def computeDefaultMatchers(clients: util.List[Client[_ <: Credentials]]): String = {
    clients.asScala.collectFirst {
      case client: IndirectClient[_] =>
        DefaultMatchers.SECURITYHEADERS + Pac4jConstants.ELEMENT_SEPARATOR + DefaultMatchers.CSRF_TOKEN
    } getOrElse DefaultMatchers.SECURITYHEADERS
  }

  protected def buildAllMatchers(matchersMap: util.Map[String, Matcher]): util.Map[String, Matcher] = {
    val allMatchers: util.Map[String, Matcher] = new util.HashMap[String, Matcher]
    allMatchers.putAll(matchersMap)
    addDefaultMatcherIfNotDefined(allMatchers, DefaultMatchers.GET, GET_MATCHER)
    addDefaultMatcherIfNotDefined(allMatchers, DefaultMatchers.POST, POST_MATCHER)
    addDefaultMatcherIfNotDefined(allMatchers, DefaultMatchers.PUT, PUT_MATCHER)
    addDefaultMatcherIfNotDefined(allMatchers, DefaultMatchers.DELETE, DELETE_MATCHER)
    allMatchers
  }

  protected def addDefaultMatcherIfNotDefined(
      allMatchers: util.Map[String, Matcher],
      name: String,
      matcher: Matcher
  ): Unit = {
    if (!allMatchers.containsKey(name)) allMatchers.put(name, matcher)
  }
}
