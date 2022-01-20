# Refuel

[![CircleCI](https://circleci.com/gh/giiita/refuel/tree/master.svg?style=svg)](https://circleci.com/gh/giiita/refuel/tree/master)
[![Gitter chat](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/phylage/refuel)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.phylage/refuel-container_3/badge.svg)](https://search.maven.org/artifact/com.phylage/refuel-container_3)

[![refuel-container-macro Scala version support](https://index.scala-lang.org/giiita/refuel/refuel-container-macro/latest-by-scala-version.svg)](https://index.scala-lang.org/giiita/refuel/refuel-container-macro)

[![refuel-container Scala version support](https://index.scala-lang.org/giiita/refuel/refuel-container/latest-by-scala-version.svg)](https://index.scala-lang.org/giiita/refuel/refuel-container)

[![refuel-util Scala version support](https://index.scala-lang.org/giiita/refuel/refuel-util/latest-by-scala-version.svg)](https://index.scala-lang.org/giiita/refuel/refuel-util)

[![refuel-json-macro Scala version support](https://index.scala-lang.org/giiita/refuel/refuel-json-macro/latest-by-scala-version.svg)](https://index.scala-lang.org/giiita/refuel/refuel-json-macro)

[![Latest version of json](https://index.scala-lang.org/giiita/refuel/refuel-json/latest.svg)](https://index.scala-lang.org/giiita/refuel/refuel-json)

[![Latest version of cipher](https://index.scala-lang.org/giiita/refuel/refuel-cipher/latest.svg)](https://index.scala-lang.org/giiita/refuel/refuel-cipher)

[![Latest version of http](https://index.scala-lang.org/giiita/refuel/refuel-http/latest.svg)](https://index.scala-lang.org/giiita/refuel/refuel-http)

[![Latest version of auth](https://index.scala-lang.org/giiita/refuel/refuel-auth-provider/latest.svg)](https://index.scala-lang.org/giiita/refuel/refuel-auth-provider)

[![Latest version of cipher](https://index.scala-lang.org/giiita/refuel/refuel-oauth-provider/latest.svg)](https://index.scala-lang.org/giiita/refuel/refuel-oauth-provider)

<p>
Refuel is a powerful DI based framework.

Since there is only one maintainer, support for Scala3 will be phased in.
At the moment, only refuel-container is supported for Scala3.
</p>


## [refuel-container](https://github.com/giiita/refuel/tree/master/refuel-container)

> Powerful DI framework with macros.
> 
> It is very simple and does not require a large number of component files in the executable module or explicit dependency or scope declarations. Allows you to focus on the work that needs to be done.
> It has a high affinity with DDD and layering. It is also highly flexible and testable, providing strong support for application development and open source library development.
> 
> All other refuel modules are based on refuel-container and can be used out-of-the-box.

## [refuel-util](https://github.com/giiita/refuel/tree/master/refuel-util)

> Rich type class interfaces.<br/>
> Provides extended functions such as date and time, period, and collection.

## [refuel-json](https://github.com/giiita/refuel/tree/master/refuel-json)

> Can use a SAML service provider that supports akka http.<br/>
> At this time, there is no Identity provider feature available.<br/>

## [refuel-cipher](https://github.com/giiita/refuel/tree/master/refuel-cipher)

> You can handle JSON most easily with macro.
> Even without warming up, the conversion works fast and requires little declaration of serialize / deserialize codecs.

## [refuel-http](https://github.com/giiita/refuel/tree/master/refuel-http) (Unsupported Scala3)

> Http server client with Akka-HTTP.<br/>
> In future, we plan to implement test stub control by dependency injection..<br/>

## [refuel-auth-provider](https://github.com/giiita/refuel/tree/master/refuel-auth-provider) (Unsupported Scala3)

> The wrapper library that supports the interconversion of RSA / AES encryption methods.

## [refuel-oauth-provider](https://github.com/giiita/refuel/tree/master/refuel-oauth-provider) (Unsupported Scala3)

> Supports building an OAuth authorization server for Akka HTTP.
>
>All authorization endpoints, token endpoints, and other grant flows are compliant with the OAuth 2.0 / 2.1 specification, and can be implemented mostly by implementing the prepared IF without having to think too much about the flow.
