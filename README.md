# Dependency tree

<img src="./deptree.svg" />

# Refuel (Move from Scaladia 3.0.0)

[![CircleCI](https://circleci.com/gh/giiita/refuel/tree/master.svg?style=svg)](https://circleci.com/gh/giiita/refuel/tree/master)

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.phylage/refuel-container_2.12/badge.svg)](https://search.maven.org/artifact/com.github.giiita/refuel_2.12)

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

## refuel-test

> In preparation.

## refuel-cipher

> In preparation.
