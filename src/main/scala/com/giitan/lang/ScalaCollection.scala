package com.giitan.lang

import scala.annotation.tailrec
import scala.collection.GenIterable
import scala.collection.parallel.ParSeq

object ScalaCollection {


  /**
    * Sequential object expansion interface.
    *
    * @param collectionT Sequencial object.
    * @tparam T Object type.
    * @tparam C Sequence type.
    */
  class CollectableFunctions[T, C <: GenIterable[T]](collectionT: C) {

    /**
      * Create keymap with ID.
      *
      * @param identity Get identity function.
      * @tparam X Selected key type.
      * @return
      */
    def wrapIdentity[X](identity: T => X): Map[X, T] = {
      for {
        (x, Seq(r, _*)) <- collectionT.groupBy(identity).to[Seq]
      } yield x -> r
    }.toMap

    def mapping[REL, TAR, RESULT](seqB: Seq[REL], seqC: Seq[TAR])
                                 (matching: (T, REL, TAR) => Boolean)
                                 (applier: (T, Seq[TAR]) => RESULT)
    : Seq[RESULT] = {

      case class KeyValue[A, B](key: A, value: B)
      val result = for {
        t <- collectionT
        b <- seqB
        c <- seqC if matching(t, b, c)
      } yield KeyValue(t, c)

      result.groupBy(_.key).map(r => {
        val (groupedKey, values) = r

        applier(groupedKey, values.map(_.value).to[Seq])
      }).to[Seq]
    }

    /**
      * Decompose the collection into N pieces.
      * When 100 collections are divided into three,
      * sliceDevideBy(3)
      * 100 / 3 + 1 = Process 34 cases at a time
      *
      * @param size denominator.
      * @param func callback function.
      * @tparam X
      * @return
      */
    def sliceDevideBy[X](size: Int)(func: GenIterable[T] => X): Seq[X] = {
      val split = collectionT.size / size + 1
      sliceWith(_.splitAt(split))(func)
    }

    /**
      * Separate the collection by size and pass it to func.
      *
      * @param size Split size.
      * @param func callback.
      * @tparam X
      * @return
      */
    def sliceAt[X](size: Int)(func: GenIterable[T] => X): Seq[X] =
      sliceWith(_.splitAt(size))(func)

    @tailrec
    final def sliceWith[X](splitter: GenIterable[T] => (GenIterable[T], GenIterable[T]))
                          (func: GenIterable[T] => X, result: SliceSteps[X] = SliceSteps[X](collectionT)): Seq[X] = {
      if (result.next.isEmpty) result.result
      else
        splitter(result.next.seq) match {
          case (target, next) => sliceWith(splitter)(func, SliceSteps(next, result.result :+ func(target)))
        }
    }

    case class SliceSteps[X](next: GenIterable[T], result: Seq[X] = Nil)

  }

  implicit class GenIterableFunctions[T](collectionT: GenIterable[T]) extends CollectableFunctions[T, GenIterable[T]](collectionT)

  implicit class SeqFunctions[T](collectionT: Seq[T]) extends CollectableFunctions[T, Seq[T]](collectionT)

  implicit class ParFunctions[T](collectionT: ParSeq[T]) extends CollectableFunctions[T, ParSeq[T]](collectionT)
}
