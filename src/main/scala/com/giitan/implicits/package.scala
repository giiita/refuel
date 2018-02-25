package com.giitan

package object implicits {
  /**
    *　ネストしたOptionの展開クラス
    *
    * @param optoptT ネストしたOption実態
    * @tparam T ネストしたOptionの型パラメータ
    */
  implicit class NOptionalT[T](optoptT: Option[Option[T]]) {

    /**
      * this が Some[Some[X] ] の時、func(X)を返す。
      *
      * @param fnc Xから実態を取得する関数
      * @tparam Z Respone Type
      * @return OptionにタップしたResponse
      */
    def >>[Z](fnc: T => Z): Option[Z] = for {
      optT <- optoptT
      t <- optT
    } yield fnc(t)

    /**
      * this が Some[X] の時、func[X]を返す。new T パターン
      *
      * @param f1 コンストラクタ
      * @tparam A Response Type
      * @return OptionにラップしたResponse
      */
    def <<[A](f1: T => A): Option[A] = for {
      optT <- optoptT
      t <- optT
    } yield f1(t)

    /**
      * this が Some[X] の時、Xを返す。
      * this が Non　　　の時、aを返す。
      *
      * @param default Noneの時のDefault値
      * @return X or a
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
      * this が Some[X] の時、Xを返す。
      * this が Non　　　の時、aを返す。
      *
      * @param e Noneの時のDefault値
      * @return X or a
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
    * Optionの展開クラス
    *
    * @param optT Option実態
    * @tparam T Optionの型パラメータ
    */
  implicit class OptionalT[T](optT: Option[T]) {

    /**
      * this が Some[X] の時、func(X)を返す。
      *
      * @param fnc Xから実態を取得する関数
      * @tparam Z Respone Type
      * @return OptionにラップしたResponse
      */
    def >>[Z](fnc: T => Z): Option[Z] = for {
      t <- optT
    } yield fnc(t)

    /**
      * this が Some[X] の時、func[X]を返す。new T パターン
      *
      * @param f1 コンストラクタ
      * @tparam A Response Type
      * @return OptionにラップしたResponse
      */
    def <<[A](f1: T => A): Option[A] = for {
      t <- optT
    } yield f1(t)

    /**
      * this が Some[X] の時、Xを返す。
      * this が Non　　　の時、aを返す。
      *
      * @param t Noneの時のDefault値
      * @return X or a
      */
    def ||=>(t: T): T = optT match {
      case Some(x) => x
      case None => t
    }

    /**
      * this が Some[X] の時、Xを返す。
      * this が Non　　　の時、aを返す。
      *
      * @param e Noneの時のDefault値
      * @return X or a
      */
    def >>>(e: Exception): T = optT match {
      case Some(x) => x
      case None => throw e
    }
  }

  /**
    * 実態クラス
    *
    * @param t 任意の値
    * @tparam T 任意の型
    */
  implicit class ValuedT[T](t: T) {

    /**
      * this が Some[X] の時、func(X)を返す。
      *
      * @param f1 Xから実態を取得する関数
      * @tparam A Respone Type
      * @return OptionにラップしたResponse
      */
    def >>[A](f1: T => Option[A]): Option[A] = for {
      a <- f1(t)
    } yield a

    /**
      * this が Some[X] の時、func[X]を返す。new T パターン
      *
      * @param f1 コンストラクタ
      * @tparam A Response Type
      * @return OptionにラップしたResponse
      */
    def <<[A](f1: T => A): A = f1(t)
  }
}
