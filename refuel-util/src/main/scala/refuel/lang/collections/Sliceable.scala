package refuel.lang.collections

object Sliceable {

  implicit class ExpandedCollection[T, +C[_] <: Iterable[_]](value: C[T]) {

    /**
      * Decompose the collection into N pieces.
      * When 100 collections are divided into three,
      * {{{
      * sliceDevideBy(3)
      * }}}
      * 100 / 3 + 1 = Process 34 cases at a time
      *
      * @param size denominator.
      * @param func applyment function.
      * @tparam R Result type.
      * @return
      */
    def sliceDivideApply[R](size: Int)(func: Iterable[T] => R): Iterator[R] = {
      val split = value.size / size + 1
      sliceApply(split)(func)
    }

    /**
      * Disassemble N collections.
      *
      * When 100 collections are divided into three,
      * {{{
      *   sliceApply(33)
      * }}}
      *
      * each 33, 33, 33, 1
      *
      * @param size      Cut size.
      * @param applyment applyment function
      * @tparam R Result type.
      * @return
      */
    def sliceApply[R](size: Int)(applyment: Iterable[T] => R): Iterator[R] = {
      value.sliding(size).map { x => applyment(x.toSeq.asInstanceOf[Seq[T]]) }
    }
  }

}
