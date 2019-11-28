# Dependency tree

<img src="./deptree.svg" />

# Refuel

[![CircleCI](https://circleci.com/gh/giiita/refuel/tree/master.svg?style=svg)](https://circleci.com/gh/giiita/refuel/tree/master)

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.phylage/refuel-container_2.12/badge.svg)](https://search.maven.org/artifact/com.github.giiita/refuel_2.12)

refuel is not bothersome and is lightweight Scala Dependency Injection library<br/>
Flexible support for basic injection.<br/>
You can replace second or third party injections from anywhere and easily handle multi-project configuration dependencies.



## [refuel-container](https://github.com/giiita/refuel/tree/master/refuel-container)

> DI container core.<br/>
> Just by Mixining a specific trait, it will be loaded automatically into DIContainer.<br/>
> It is also possible to easily change dependencies from test cases or allow access only from arbitrary scopes.<br/>


## [refuel-util](https://github.com/giiita/refuel/tree/master/refuel-util)

> Rich domain model interfaces.<br/>
> Provides extended functions such as date and time, period, and collection.


## [refuel-http](https://github.com/giiita/refuel/tree/master/refuel-http)

> Http server client with Akka-HTTP.<br/>
> In future, we plan to implement test stub control by dependency injection..<br/>

## refuel-json

> In preparation.

## refuel-test

> In preparation.
