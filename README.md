# Refuel

[![CircleCI](https://circleci.com/gh/giiita/refuel/tree/master.svg?style=svg)](https://circleci.com/gh/giiita/refuel/tree/master)
[![Gitter chat](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/phylage/refuel)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.phylage/refuel-container_2.12/badge.svg)](https://search.maven.org/artifact/com.phylage/refuel-container_2.12)

[![Latest version of macro](https://index.scala-lang.org/giiita/refuel/refuel-macro/latest.svg)](https://index.scala-lang.org/giiita/refuel/refuel-macro)
[![Latest version of container](https://index.scala-lang.org/giiita/refuel/refuel-container/latest.svg)](https://index.scala-lang.org/giiita/refuel/refuel-container)
[![Latest version of util](https://index.scala-lang.org/giiita/refuel/refuel-util/latest.svg)](https://index.scala-lang.org/giiita/refuel/refuel-util)
[![Latest version of json](https://index.scala-lang.org/giiita/refuel/refuel-json/latest.svg)](https://index.scala-lang.org/giiita/refuel/refuel-json)
[![Latest version of http](https://index.scala-lang.org/giiita/refuel/refuel-http/latest.svg)](https://index.scala-lang.org/giiita/refuel/refuel-http)
[![Latest version of auth](https://index.scala-lang.org/giiita/refuel/refuel-auth-provider/latest.svg)](https://index.scala-lang.org/giiita/refuel/refuel-auth-provider)
[![Latest version of cipher](https://index.scala-lang.org/giiita/refuel/refuel-cipher/latest.svg)](https://index.scala-lang.org/giiita/refuel/refuel-cipher)

Refuel is a simple and lightweight DI framework<br/>
Provides DI, utility, JSON parser, HTTPClient, etc. suitable for layered architecture.<br/>
You can replace second or third party injections from anywhere and easily handle multi-project configuration dependencies.



## [refuel-container](https://github.com/giiita/refuel/tree/master/refuel-container)

> DI container provides powerful injection with macro.<br/>
> Just by Mixining a specific trait, it will be loaded automatically into DI container.<br/>
> It is also possible to easily change dependencies from test cases or allow access only from arbitrary scopes.<br/>

## [refuel-util](https://github.com/giiita/refuel/tree/master/refuel-util)

> Rich type class interfaces.<br/>
> Provides extended functions such as date and time, period, and collection.

## [refuel-json](https://github.com/giiita/refuel/tree/master/refuel-json)

> You can handle JSON most easily with macro.
> Even without warming up, the conversion works fast and requires little declaration of serialize / deserialize codecs.

## [refuel-http](https://github.com/giiita/refuel/tree/master/refuel-http)

> Http server client with Akka-HTTP.<br/>
> In future, we plan to implement test stub control by dependency injection..<br/>

## [refuel-auth-provider](https://github.com/giiita/refuel/tree/master/refuel-auth-provider)

> Can use a SAML service provider that supports akka http.<br/>
> At this time, there is no Identity provider feature available.<br/>

## [refuel-cipher](https://github.com/giiita/refuel/tree/master/refuel-cipher)

> The wrapper library that supports the interconversion of RSA / AES encryption methods.
