package com.giitan

package object implicits {
  /**
    *　Function group for expanding nested option
    *
    * @param optoptT Nested option value
    * @tparam T Option type
    */
  implicit class NestedOptionT[T](optoptT: Option[Option[T]]) {

    /**
      * Function group for expanding option
      *
      * @param fnc Expanding option.
      * @tparam Z Respone optional Type
      * @return Function result.
      */
    def >>[Z](fnc: T => Z): Option[Z] = for {
      optT <- optoptT
      t <- optT
    } yield fnc(t)

    /**
      * When this is Some,do apply.
      *
      * @param f1 apply
      * @tparam A Respone optional Type
      * @return Function result or None
      */
    def <<[A](f1: T => A): Option[A] = for {
      optT <- optoptT
      t <- optT
    } yield f1(t)

    /**
      * Returns some value of Option or default.
      *
      * @param default Default value.
      * @return Some value of Option or default
      */
    def ||=>(default: T): T = {
      (for {
        optT <- optoptT
        t <- optT
      } yield t) match {
        case Some(x) => x
        case None => default
      }
    }

    /**
      * Returns the Some value of Option. Or throw an exception.
      *
      * @param e Exception
      * @return Some value of Option or exception
      */
    def >>>(e: Exception): T = (for {
      optT <- optoptT
      t <- optT
    } yield t) match {
      case Some(x) => x
      case None => throw e
    }
  }

  /**
    * Function group for expanding option.
    *
    * @param optT Option value.
    * @tparam T Option type.
    */
  implicit class OptionT[T](optT: Option[T]) {

    /**
      * Function group for expanding option
      *
      * @param fnc Expanding option.
      * @tparam Z Respone optional Type
      * @return Function result.
      */
    def >>[Z](fnc: T => Z): Option[Z] = for {
      t <- optT
    } yield fnc(t)

    /**
      * When this is Some,do apply.
      *
      * @param f1 apply
      * @tparam A Respone optional Type
      * @return Function result or None
      */
    def <<[A](f1: T => A): Option[A] = for {
      t <- optT
    } yield f1(t)

    /**
      * Returns some value of Option or default.
      *
      * @param t Default value.
      * @return Some value of Option or default
      */
    def getOrElse(t: T): T = optT match {
      case Some(x) => x
      case None => t
    }

    /**
      * Returns the Some value of Option. Or throw an exception.
      *
      * @param e Exception
      * @return Some value of Option or exception
      */
    def >>>(e: Exception): T = optT match {
      case Some(x) => x
      case None => throw e
    }
  }

  /**
    * Function group for value.
    *
    * @param t Value.
    * @tparam T Value type.
    */
  implicit class ValuedT[T](t: T) {

    /**
      * Function group for expanding option
      *
      * @param f1 Option getter.
      * @tparam A Respone option Type
      * @return Function result.
      */
    def >>[A](f1: T => Option[A]): Option[A] = for {
      a <- f1(t)
    } yield a

    /**
      * When this is Some,do apply.
      *
      * @param f1 apply
      * @tparam A Respone optional Type
      * @return Function result or None
      */
    def <<[A](f1: T => A): A = f1(t)
  }
}
