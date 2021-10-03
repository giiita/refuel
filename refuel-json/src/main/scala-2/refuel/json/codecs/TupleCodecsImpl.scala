package refuel.json.codecs

import refuel.json.JsonVal.{JsArray, JsObject}
import refuel.json.exception.IllegalJsonFormatException
import refuel.json.{JsonTransform, JsonVal}

import scala.jdk.CollectionConverters.MapHasAsScala

private[refuel] trait TupleCodecsImpl { _: JsonTransform =>

  private[this] def tupleRead[X](bf: JsonVal)(tb: List[JsonVal] => X): X = tb(
    bf match {
      case JsObject(x) => x.asScala.values.toList
      case JsArray(x)  => x.toList
      case _           => throw IllegalJsonFormatException("Tuple deserialization was expected JsArray or JsArray")
    }
  )

  private[this] def Tuple2CodecConst[A, B, CP[_]](a: CP[A], b: CP[B])(implicit projection: CodecTypeProjection[CP]) = projection.both[(A, B)](
    {
      tupleRead(_) {
        case ae :: be :: Nil =>
          (
            projection.read(ae)(a),
            projection.read(be)(b)
          )
      }
    },
    { t =>
      JsArray(
        Seq(
          projection.write(t._1)(a),
          projection.write(t._2)(b)
        )
      )
    }
  )

  private[this] def Tuple3CodecConst[A, B, C, CP[_]](a: CP[A], b: CP[B], c: CP[C])(implicit projection: CodecTypeProjection[CP]) = projection.both[(A, B, C)](
    {
      tupleRead(_) {
        case ae :: be :: ce :: Nil =>
          (
            projection.read(ae)(a),
            projection.read(be)(b),
            projection.read(ce)(c)
          )
      }
    },
    { t =>
      JsArray(
        Seq(
          projection.write(t._1)(a),
          projection.write(t._2)(b),
          projection.write(t._3)(c)
        )
      )
    }
  )

  private[this] def Tuple4CodecConst[A, B, C, D, CP[_]](a: CP[A], b: CP[B], c: CP[C], d: CP[D])(implicit projection: CodecTypeProjection[CP]) = projection.both[(A, B, C, D)](
    {
      tupleRead(_) {
        case ae :: be :: ce :: de :: Nil =>
          (
            projection.read(ae)(a),
            projection.read(be)(b),
            projection.read(ce)(c),
            projection.read(de)(d)
          )
      }
    },
    { t =>
      JsArray(
        Seq(
          projection.write(t._1)(a),
          projection.write(t._2)(b),
          projection.write(t._3)(c),
          projection.write(t._4)(d)
        )
      )
    }
  )

  private[this] def Tuple5CodecConst[A, B, C, D, E, CP[_]](a: CP[A], b: CP[B], c: CP[C], d: CP[D], e: CP[E])(implicit projection: CodecTypeProjection[CP]) = projection.both[(A, B, C, D, E)](
    {
      tupleRead(_) {
        case ae :: be :: ce :: de :: ee :: Nil =>
          (
            projection.read(ae)(a),
            projection.read(be)(b),
            projection.read(ce)(c),
            projection.read(de)(d),
            projection.read(ee)(e)
          )
      }
    },
    { t =>
      JsArray(
        Seq(
          projection.write(t._1)(a),
          projection.write(t._2)(b),
          projection.write(t._3)(c),
          projection.write(t._4)(d),
          projection.write(t._5)(e)
        )
      )
    }
  )

  private[this] def Tuple6CodecConst[A, B, C, D, E, F, CP[_]](a: CP[A], b: CP[B], c: CP[C], d: CP[D], e: CP[E], f: CP[F])(implicit projection: CodecTypeProjection[CP]) = projection.both[(A, B, C, D, E, F)](
    {
      tupleRead(_) {
        case ae :: be :: ce :: de :: ee :: fe :: Nil =>
          (
            projection.read(ae)(a),
            projection.read(be)(b),
            projection.read(ce)(c),
            projection.read(de)(d),
            projection.read(ee)(e),
            projection.read(fe)(f)
          )
      }
    },
    { t =>
      JsArray(
        Seq(
          projection.write(t._1)(a),
          projection.write(t._2)(b),
          projection.write(t._3)(c),
          projection.write(t._4)(d),
          projection.write(t._5)(e),
          projection.write(t._6)(f)
        )
      )
    }
  )

  private[this] def Tuple7CodecConst[A, B, C, D, E, F, G, CP[_]](a: CP[A], b: CP[B], c: CP[C], d: CP[D], e: CP[E], f: CP[F], g: CP[G])(implicit projection: CodecTypeProjection[CP]) = projection.both[(A, B, C, D, E, F, G)](
    {
      tupleRead(_) {
        case ae :: be :: ce :: de :: ee :: fe :: ge :: Nil =>
          (
            projection.read(ae)(a),
            projection.read(be)(b),
            projection.read(ce)(c),
            projection.read(de)(d),
            projection.read(ee)(e),
            projection.read(fe)(f),
            projection.read(ge)(g)
          )
      }
    },
    { t =>
      JsArray(
        Seq(
          projection.write(t._1)(a),
          projection.write(t._2)(b),
          projection.write(t._3)(c),
          projection.write(t._4)(d),
          projection.write(t._5)(e),
          projection.write(t._6)(f),
          projection.write(t._7)(g)
        )
      )
    }
  )

  private[this] def Tuple8CodecConst[A, B, C, D, E, F, G, H, CP[_]](a: CP[A], b: CP[B], c: CP[C], d: CP[D], e: CP[E], f: CP[F], g: CP[G], h: CP[H])(implicit projection: CodecTypeProjection[CP]) = projection.both[(A, B, C, D, E, F, G, H)](
    {
      tupleRead(_) {
        case ae :: be :: ce :: de :: ee :: fe :: ge :: he :: Nil =>
          (
            projection.read(ae)(a),
            projection.read(be)(b),
            projection.read(ce)(c),
            projection.read(de)(d),
            projection.read(ee)(e),
            projection.read(fe)(f),
            projection.read(ge)(g),
            projection.read(he)(h)
          )
      }
    },
    { t =>
      JsArray(
        Seq(
          projection.write(t._1)(a),
          projection.write(t._2)(b),
          projection.write(t._3)(c),
          projection.write(t._4)(d),
          projection.write(t._5)(e),
          projection.write(t._6)(f),
          projection.write(t._7)(g),
          projection.write(t._8)(h)
        )
      )
    }
  )

  private[this] def Tuple9CodecConst[A, B, C, D, E, F, G, H, I, CP[_]](a: CP[A], b: CP[B], c: CP[C], d: CP[D], e: CP[E], f: CP[F], g: CP[G], h: CP[H], i: CP[I])(implicit projection: CodecTypeProjection[CP]) = projection.both[(A, B, C, D, E, F, G, H, I)](
    {
      tupleRead(_) {
        case ae :: be :: ce :: de :: ee :: fe :: ge :: he :: ie :: Nil =>
          (
            projection.read(ae)(a),
            projection.read(be)(b),
            projection.read(ce)(c),
            projection.read(de)(d),
            projection.read(ee)(e),
            projection.read(fe)(f),
            projection.read(ge)(g),
            projection.read(he)(h),
            projection.read(ie)(i)
          )
      }
    },
    { t =>
      JsArray(
        Seq(
          projection.write(t._1)(a),
          projection.write(t._2)(b),
          projection.write(t._3)(c),
          projection.write(t._4)(d),
          projection.write(t._5)(e),
          projection.write(t._6)(f),
          projection.write(t._7)(g),
          projection.write(t._8)(h),
          projection.write(t._9)(i)
        )
      )
    }
  )

  private[this] def Tuple10CodecConst[A, B, C, D, E, F, G, H, I, J, CP[_]](a: CP[A], b: CP[B], c: CP[C], d: CP[D], e: CP[E], f: CP[F], g: CP[G], h: CP[H], i: CP[I], j: CP[J])(implicit projection: CodecTypeProjection[CP]) = projection.both[(A, B, C, D, E, F, G, H, I, J)](
    {
      tupleRead(_) {
        case ae :: be :: ce :: de :: ee :: fe :: ge :: he :: ie :: je :: Nil =>
          (
            projection.read(ae)(a),
            projection.read(be)(b),
            projection.read(ce)(c),
            projection.read(de)(d),
            projection.read(ee)(e),
            projection.read(fe)(f),
            projection.read(ge)(g),
            projection.read(he)(h),
            projection.read(ie)(i),
            projection.read(je)(j)
          )
      }
    },
    { t =>
      JsArray(
        Seq(
          projection.write(t._1)(a),
          projection.write(t._2)(b),
          projection.write(t._3)(c),
          projection.write(t._4)(d),
          projection.write(t._5)(e),
          projection.write(t._6)(f),
          projection.write(t._7)(g),
          projection.write(t._8)(h),
          projection.write(t._9)(i),
          projection.write(t._10)(j)
        )
      )
    }
  )

  private[this] def Tuple11CodecConst[A, B, C, D, E, F, G, H, I, J, K, CP[_]](a: CP[A], b: CP[B], c: CP[C], d: CP[D], e: CP[E], f: CP[F], g: CP[G], h: CP[H], i: CP[I], j: CP[J], k: CP[K])(implicit projection: CodecTypeProjection[CP]) = projection.both[(A, B, C, D, E, F, G, H, I, J, K)](
    {
      tupleRead(_) {
        case ae :: be :: ce :: de :: ee :: fe :: ge :: he :: ie :: je :: ke :: Nil =>
          (
            projection.read(ae)(a),
            projection.read(be)(b),
            projection.read(ce)(c),
            projection.read(de)(d),
            projection.read(ee)(e),
            projection.read(fe)(f),
            projection.read(ge)(g),
            projection.read(he)(h),
            projection.read(ie)(i),
            projection.read(je)(j),
            projection.read(ke)(k)
          )
      }
    },
    { t =>
      JsArray(
        Seq(
          projection.write(t._1)(a),
          projection.write(t._2)(b),
          projection.write(t._3)(c),
          projection.write(t._4)(d),
          projection.write(t._5)(e),
          projection.write(t._6)(f),
          projection.write(t._7)(g),
          projection.write(t._8)(h),
          projection.write(t._9)(i),
          projection.write(t._10)(j),
          projection.write(t._11)(k)
        )
      )
    }
  )

  private[this] def Tuple12CodecConst[A, B, C, D, E, F, G, H, I, J, K, L, CP[_]](a: CP[A], b: CP[B], c: CP[C], d: CP[D], e: CP[E], f: CP[F], g: CP[G], h: CP[H], i: CP[I], j: CP[J], k: CP[K], l: CP[L])(implicit projection: CodecTypeProjection[CP]) = projection.both[(A, B, C, D, E, F, G, H, I, J, K, L)](
    {
      tupleRead(_) {
        case ae :: be :: ce :: de :: ee :: fe :: ge :: he :: ie :: je :: ke :: le :: Nil =>
          (
            projection.read(ae)(a),
            projection.read(be)(b),
            projection.read(ce)(c),
            projection.read(de)(d),
            projection.read(ee)(e),
            projection.read(fe)(f),
            projection.read(ge)(g),
            projection.read(he)(h),
            projection.read(ie)(i),
            projection.read(je)(j),
            projection.read(ke)(k),
            projection.read(le)(l)
          )
      }
    },
    { t =>
      JsArray(
        Seq(
          projection.write(t._1)(a),
          projection.write(t._2)(b),
          projection.write(t._3)(c),
          projection.write(t._4)(d),
          projection.write(t._5)(e),
          projection.write(t._6)(f),
          projection.write(t._7)(g),
          projection.write(t._8)(h),
          projection.write(t._9)(i),
          projection.write(t._10)(j),
          projection.write(t._11)(k),
          projection.write(t._12)(l)
        )
      )
    }
  )

  private[this] def Tuple13CodecConst[A, B, C, D, E, F, G, H, I, J, K, L, M, CP[_]](a: CP[A], b: CP[B], c: CP[C], d: CP[D], e: CP[E], f: CP[F], g: CP[G], h: CP[H], i: CP[I], j: CP[J], k: CP[K], l: CP[L], m: CP[M])(implicit projection: CodecTypeProjection[CP]) = projection.both[(A, B, C, D, E, F, G, H, I, J, K, L, M)](
    {
      tupleRead(_) {
        case ae :: be :: ce :: de :: ee :: fe :: ge :: he :: ie :: je :: ke :: le :: me :: Nil =>
          (
            projection.read(ae)(a),
            projection.read(be)(b),
            projection.read(ce)(c),
            projection.read(de)(d),
            projection.read(ee)(e),
            projection.read(fe)(f),
            projection.read(ge)(g),
            projection.read(he)(h),
            projection.read(ie)(i),
            projection.read(je)(j),
            projection.read(ke)(k),
            projection.read(le)(l),
            projection.read(me)(m)
          )
      }
    },
    { t =>
      JsArray(
        Seq(
          projection.write(t._1)(a),
          projection.write(t._2)(b),
          projection.write(t._3)(c),
          projection.write(t._4)(d),
          projection.write(t._5)(e),
          projection.write(t._6)(f),
          projection.write(t._7)(g),
          projection.write(t._8)(h),
          projection.write(t._9)(i),
          projection.write(t._10)(j),
          projection.write(t._11)(k),
          projection.write(t._12)(l),
          projection.write(t._13)(m)
        )
      )
    }
  )

  private[this] def Tuple14CodecConst[A, B, C, D, E, F, G, H, I, J, K, L, M, N, CP[_]](a: CP[A], b: CP[B], c: CP[C], d: CP[D], e: CP[E], f: CP[F], g: CP[G], h: CP[H], i: CP[I], j: CP[J], k: CP[K], l: CP[L], m: CP[M], n: CP[N])(implicit projection: CodecTypeProjection[CP]) = projection.both[(A, B, C, D, E, F, G, H, I, J, K, L, M, N)](
    {
      tupleRead(_) {
        case ae :: be :: ce :: de :: ee :: fe :: ge :: he :: ie :: je :: ke :: le :: me :: ne :: Nil =>
          (
            projection.read(ae)(a),
            projection.read(be)(b),
            projection.read(ce)(c),
            projection.read(de)(d),
            projection.read(ee)(e),
            projection.read(fe)(f),
            projection.read(ge)(g),
            projection.read(he)(h),
            projection.read(ie)(i),
            projection.read(je)(j),
            projection.read(ke)(k),
            projection.read(le)(l),
            projection.read(me)(m),
            projection.read(ne)(n)
          )
      }
    },
    { t =>
      JsArray(
        Seq(
          projection.write(t._1)(a),
          projection.write(t._2)(b),
          projection.write(t._3)(c),
          projection.write(t._4)(d),
          projection.write(t._5)(e),
          projection.write(t._6)(f),
          projection.write(t._7)(g),
          projection.write(t._8)(h),
          projection.write(t._9)(i),
          projection.write(t._10)(j),
          projection.write(t._11)(k),
          projection.write(t._12)(l),
          projection.write(t._13)(m),
          projection.write(t._14)(n)
        )
      )
    }
  )

  private[this] def Tuple15CodecConst[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, CP[_]](a: CP[A], b: CP[B], c: CP[C], d: CP[D], e: CP[E], f: CP[F], g: CP[G], h: CP[H], i: CP[I], j: CP[J], k: CP[K], l: CP[L], m: CP[M], n: CP[N], o: CP[O])(implicit projection: CodecTypeProjection[CP]) = projection.both[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)](
    {
      tupleRead(_) {
        case ae :: be :: ce :: de :: ee :: fe :: ge :: he :: ie :: je :: ke :: le :: me :: ne :: oe :: Nil =>
          (
            projection.read(ae)(a),
            projection.read(be)(b),
            projection.read(ce)(c),
            projection.read(de)(d),
            projection.read(ee)(e),
            projection.read(fe)(f),
            projection.read(ge)(g),
            projection.read(he)(h),
            projection.read(ie)(i),
            projection.read(je)(j),
            projection.read(ke)(k),
            projection.read(le)(l),
            projection.read(me)(m),
            projection.read(ne)(n),
            projection.read(oe)(o)
          )
      }
    },
    { t =>
      JsArray(
        Seq(
          projection.write(t._1)(a),
          projection.write(t._2)(b),
          projection.write(t._3)(c),
          projection.write(t._4)(d),
          projection.write(t._5)(e),
          projection.write(t._6)(f),
          projection.write(t._7)(g),
          projection.write(t._8)(h),
          projection.write(t._9)(i),
          projection.write(t._10)(j),
          projection.write(t._11)(k),
          projection.write(t._12)(l),
          projection.write(t._13)(m),
          projection.write(t._14)(n),
          projection.write(t._15)(o)
        )
      )
    }
  )

  private[this] def Tuple16CodecConst[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, CP[_]](a: CP[A], b: CP[B], c: CP[C], d: CP[D], e: CP[E], f: CP[F], g: CP[G], h: CP[H], i: CP[I], j: CP[J], k: CP[K], l: CP[L], m: CP[M], n: CP[N], o: CP[O], p: CP[P])(implicit projection: CodecTypeProjection[CP]) = projection.both[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)](
    {
      tupleRead(_) {
        case ae :: be :: ce :: de :: ee :: fe :: ge :: he :: ie :: je :: ke :: le :: me :: ne :: oe :: pe :: Nil =>
          (
            projection.read(ae)(a),
            projection.read(be)(b),
            projection.read(ce)(c),
            projection.read(de)(d),
            projection.read(ee)(e),
            projection.read(fe)(f),
            projection.read(ge)(g),
            projection.read(he)(h),
            projection.read(ie)(i),
            projection.read(je)(j),
            projection.read(ke)(k),
            projection.read(le)(l),
            projection.read(me)(m),
            projection.read(ne)(n),
            projection.read(oe)(o),
            projection.read(pe)(p)
          )
      }
    },
    { t =>
      JsArray(
        Seq(
          projection.write(t._1)(a),
          projection.write(t._2)(b),
          projection.write(t._3)(c),
          projection.write(t._4)(d),
          projection.write(t._5)(e),
          projection.write(t._6)(f),
          projection.write(t._7)(g),
          projection.write(t._8)(h),
          projection.write(t._9)(i),
          projection.write(t._10)(j),
          projection.write(t._11)(k),
          projection.write(t._12)(l),
          projection.write(t._13)(m),
          projection.write(t._14)(n),
          projection.write(t._15)(o),
          projection.write(t._16)(p)
        )
      )
    }
  )

  private[this] def Tuple17CodecConst[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, CP[_]](a: CP[A], b: CP[B], c: CP[C], d: CP[D], e: CP[E], f: CP[F], g: CP[G], h: CP[H], i: CP[I], j: CP[J], k: CP[K], l: CP[L], m: CP[M], n: CP[N], o: CP[O], p: CP[P], q: CP[Q])(implicit projection: CodecTypeProjection[CP]) = projection.both[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)](
    {
      tupleRead(_) {
        case ae :: be :: ce :: de :: ee :: fe :: ge :: he :: ie :: je :: ke :: le :: me :: ne :: oe :: pe :: qe :: Nil =>
          (
            projection.read(ae)(a),
            projection.read(be)(b),
            projection.read(ce)(c),
            projection.read(de)(d),
            projection.read(ee)(e),
            projection.read(fe)(f),
            projection.read(ge)(g),
            projection.read(he)(h),
            projection.read(ie)(i),
            projection.read(je)(j),
            projection.read(ke)(k),
            projection.read(le)(l),
            projection.read(me)(m),
            projection.read(ne)(n),
            projection.read(oe)(o),
            projection.read(pe)(p),
            projection.read(qe)(q)
          )
      }
    },
    { t =>
      JsArray(
        Seq(
          projection.write(t._1)(a),
          projection.write(t._2)(b),
          projection.write(t._3)(c),
          projection.write(t._4)(d),
          projection.write(t._5)(e),
          projection.write(t._6)(f),
          projection.write(t._7)(g),
          projection.write(t._8)(h),
          projection.write(t._9)(i),
          projection.write(t._10)(j),
          projection.write(t._11)(k),
          projection.write(t._12)(l),
          projection.write(t._13)(m),
          projection.write(t._14)(n),
          projection.write(t._15)(o),
          projection.write(t._16)(p),
          projection.write(t._17)(q)
        )
      )
    }
  )

  private[this] def Tuple18CodecConst[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, CP[_]](a: CP[A], b: CP[B], c: CP[C], d: CP[D], e: CP[E], f: CP[F], g: CP[G], h: CP[H], i: CP[I], j: CP[J], k: CP[K], l: CP[L], m: CP[M], n: CP[N], o: CP[O], p: CP[P], q: CP[Q], r: CP[R])(implicit projection: CodecTypeProjection[CP]) = projection.both[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)](
    {
      tupleRead(_) {
        case ae :: be :: ce :: de :: ee :: fe :: ge :: he :: ie :: je :: ke :: le :: me :: ne :: oe :: pe :: qe :: re :: Nil =>
          (
            projection.read(ae)(a),
            projection.read(be)(b),
            projection.read(ce)(c),
            projection.read(de)(d),
            projection.read(ee)(e),
            projection.read(fe)(f),
            projection.read(ge)(g),
            projection.read(he)(h),
            projection.read(ie)(i),
            projection.read(je)(j),
            projection.read(ke)(k),
            projection.read(le)(l),
            projection.read(me)(m),
            projection.read(ne)(n),
            projection.read(oe)(o),
            projection.read(pe)(p),
            projection.read(qe)(q),
            projection.read(re)(r)
          )
      }
    },
    { t =>
      JsArray(
        Seq(
          projection.write(t._1)(a),
          projection.write(t._2)(b),
          projection.write(t._3)(c),
          projection.write(t._4)(d),
          projection.write(t._5)(e),
          projection.write(t._6)(f),
          projection.write(t._7)(g),
          projection.write(t._8)(h),
          projection.write(t._9)(i),
          projection.write(t._10)(j),
          projection.write(t._11)(k),
          projection.write(t._12)(l),
          projection.write(t._13)(m),
          projection.write(t._14)(n),
          projection.write(t._15)(o),
          projection.write(t._16)(p),
          projection.write(t._17)(q),
          projection.write(t._18)(r)
        )
      )
    }
  )

  private[this] def Tuple19CodecConst[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, CP[_]](a: CP[A], b: CP[B], c: CP[C], d: CP[D], e: CP[E], f: CP[F], g: CP[G], h: CP[H], i: CP[I], j: CP[J], k: CP[K], l: CP[L], m: CP[M], n: CP[N], o: CP[O], p: CP[P], q: CP[Q], r: CP[R], s: CP[S])(implicit projection: CodecTypeProjection[CP]) = projection.both[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)](
    {
      tupleRead(_) {
        case ae :: be :: ce :: de :: ee :: fe :: ge :: he :: ie :: je :: ke :: le :: me :: ne :: oe :: pe :: qe :: re :: se :: Nil =>
          (
            projection.read(ae)(a),
            projection.read(be)(b),
            projection.read(ce)(c),
            projection.read(de)(d),
            projection.read(ee)(e),
            projection.read(fe)(f),
            projection.read(ge)(g),
            projection.read(he)(h),
            projection.read(ie)(i),
            projection.read(je)(j),
            projection.read(ke)(k),
            projection.read(le)(l),
            projection.read(me)(m),
            projection.read(ne)(n),
            projection.read(oe)(o),
            projection.read(pe)(p),
            projection.read(qe)(q),
            projection.read(re)(r),
            projection.read(se)(s)
          )
      }
    },
    { t =>
      JsArray(
        Seq(
          projection.write(t._1)(a),
          projection.write(t._2)(b),
          projection.write(t._3)(c),
          projection.write(t._4)(d),
          projection.write(t._5)(e),
          projection.write(t._6)(f),
          projection.write(t._7)(g),
          projection.write(t._8)(h),
          projection.write(t._9)(i),
          projection.write(t._10)(j),
          projection.write(t._11)(k),
          projection.write(t._12)(l),
          projection.write(t._13)(m),
          projection.write(t._14)(n),
          projection.write(t._15)(o),
          projection.write(t._16)(p),
          projection.write(t._17)(q),
          projection.write(t._18)(r),
          projection.write(t._19)(s)
        )
      )
    }
  )

  private[this] def Tuple20CodecConst[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, CP[_]](a: CP[A], b: CP[B], c: CP[C], d: CP[D], e: CP[E], f: CP[F], g: CP[G], h: CP[H], i: CP[I], j: CP[J], k: CP[K], l: CP[L], m: CP[M], n: CP[N], o: CP[O], p: CP[P], q: CP[Q], r: CP[R], s: CP[S], t: CP[T])(implicit projection: CodecTypeProjection[CP]) = projection.both[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)](
    {
      tupleRead(_) {
        case ae :: be :: ce :: de :: ee :: fe :: ge :: he :: ie :: je :: ke :: le :: me :: ne :: oe :: pe :: qe :: re :: se :: te :: Nil =>
          (
            projection.read(ae)(a),
            projection.read(be)(b),
            projection.read(ce)(c),
            projection.read(de)(d),
            projection.read(ee)(e),
            projection.read(fe)(f),
            projection.read(ge)(g),
            projection.read(he)(h),
            projection.read(ie)(i),
            projection.read(je)(j),
            projection.read(ke)(k),
            projection.read(le)(l),
            projection.read(me)(m),
            projection.read(ne)(n),
            projection.read(oe)(o),
            projection.read(pe)(p),
            projection.read(qe)(q),
            projection.read(re)(r),
            projection.read(se)(s),
            projection.read(te)(t)
          )
      }
    },
    { _t =>
      JsArray(
        Seq(
          projection.write(_t._1)(a),
          projection.write(_t._2)(b),
          projection.write(_t._3)(c),
          projection.write(_t._4)(d),
          projection.write(_t._5)(e),
          projection.write(_t._6)(f),
          projection.write(_t._7)(g),
          projection.write(_t._8)(h),
          projection.write(_t._9)(i),
          projection.write(_t._10)(j),
          projection.write(_t._11)(k),
          projection.write(_t._12)(l),
          projection.write(_t._13)(m),
          projection.write(_t._14)(n),
          projection.write(_t._15)(o),
          projection.write(_t._16)(p),
          projection.write(_t._17)(q),
          projection.write(_t._18)(r),
          projection.write(_t._19)(s),
          projection.write(_t._20)(t)
        )
      )
    }
  )

  private[this] def Tuple21CodecConst[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, CP[_]](a: CP[A], b: CP[B], c: CP[C], d: CP[D], e: CP[E], f: CP[F], g: CP[G], h: CP[H], i: CP[I], j: CP[J], k: CP[K], l: CP[L], m: CP[M], n: CP[N], o: CP[O], p: CP[P], q: CP[Q], r: CP[R], s: CP[S], t: CP[T], u: CP[U])(implicit projection: CodecTypeProjection[CP]) = projection.both[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U)](
    {
      tupleRead(_) {
        case ae :: be :: ce :: de :: ee :: fe :: ge :: he :: ie :: je :: ke :: le :: me :: ne :: oe :: pe :: qe :: re :: se :: te :: ue :: Nil =>
          (
            projection.read(ae)(a),
            projection.read(be)(b),
            projection.read(ce)(c),
            projection.read(de)(d),
            projection.read(ee)(e),
            projection.read(fe)(f),
            projection.read(ge)(g),
            projection.read(he)(h),
            projection.read(ie)(i),
            projection.read(je)(j),
            projection.read(ke)(k),
            projection.read(le)(l),
            projection.read(me)(m),
            projection.read(ne)(n),
            projection.read(oe)(o),
            projection.read(pe)(p),
            projection.read(qe)(q),
            projection.read(re)(r),
            projection.read(se)(s),
            projection.read(te)(t),
            projection.read(ue)(u)
          )
      }
    },
    { _t =>
      JsArray(
        Seq(
          projection.write(_t._1)(a),
          projection.write(_t._2)(b),
          projection.write(_t._3)(c),
          projection.write(_t._4)(d),
          projection.write(_t._5)(e),
          projection.write(_t._6)(f),
          projection.write(_t._7)(g),
          projection.write(_t._8)(h),
          projection.write(_t._9)(i),
          projection.write(_t._10)(j),
          projection.write(_t._11)(k),
          projection.write(_t._12)(l),
          projection.write(_t._13)(m),
          projection.write(_t._14)(n),
          projection.write(_t._15)(o),
          projection.write(_t._16)(p),
          projection.write(_t._17)(q),
          projection.write(_t._18)(r),
          projection.write(_t._19)(s),
          projection.write(_t._20)(t),
          projection.write(_t._21)(u)
        )
      )
    }
  )

  private[this] def Tuple22CodecConst[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, CP[_]](a: CP[A], b: CP[B], c: CP[C], d: CP[D], e: CP[E], f: CP[F], g: CP[G], h: CP[H], i: CP[I], j: CP[J], k: CP[K], l: CP[L], m: CP[M], n: CP[N], o: CP[O], p: CP[P], q: CP[Q], r: CP[R], s: CP[S], t: CP[T], u: CP[U], v: CP[V])(implicit projection: CodecTypeProjection[CP]) = projection.both[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V)](
    {
      tupleRead(_) {
        case ae :: be :: ce :: de :: ee :: fe :: ge :: he :: ie :: je :: ke :: le :: me :: ne :: oe :: pe :: qe :: re :: se :: te :: ue :: ve :: Nil =>
          (
            projection.read(ae)(a),
            projection.read(be)(b),
            projection.read(ce)(c),
            projection.read(de)(d),
            projection.read(ee)(e),
            projection.read(fe)(f),
            projection.read(ge)(g),
            projection.read(he)(h),
            projection.read(ie)(i),
            projection.read(je)(j),
            projection.read(ke)(k),
            projection.read(le)(l),
            projection.read(me)(m),
            projection.read(ne)(n),
            projection.read(oe)(o),
            projection.read(pe)(p),
            projection.read(qe)(q),
            projection.read(re)(r),
            projection.read(se)(s),
            projection.read(te)(t),
            projection.read(ue)(u),
            projection.read(ve)(v)
          )
      }
    },
    { _t =>
      JsArray(
        Seq(
          projection.write(_t._1)(a),
          projection.write(_t._2)(b),
          projection.write(_t._3)(c),
          projection.write(_t._4)(d),
          projection.write(_t._5)(e),
          projection.write(_t._6)(f),
          projection.write(_t._7)(g),
          projection.write(_t._8)(h),
          projection.write(_t._9)(i),
          projection.write(_t._10)(j),
          projection.write(_t._11)(k),
          projection.write(_t._12)(l),
          projection.write(_t._13)(m),
          projection.write(_t._14)(n),
          projection.write(_t._15)(o),
          projection.write(_t._16)(p),
          projection.write(_t._17)(q),
          projection.write(_t._18)(r),
          projection.write(_t._19)(s),
          projection.write(_t._20)(t),
          projection.write(_t._21)(u),
          projection.write(_t._22)(v)
        )
      )
    }
  )

  implicit def __tuple2[A, B](implicit a: Codec[A], b: Codec[B]): Codec[(A, B)] = Tuple2CodecConst(a, b)
  implicit def __tuple3[A, B, C](implicit a: Codec[A], b: Codec[B], c: Codec[C]): Codec[(A, B, C)] = Tuple3CodecConst(a, b, c)
  implicit def __tuple4[A, B, C, D](implicit a: Codec[A], b: Codec[B], c: Codec[C], d: Codec[D]): Codec[(A, B, C, D)] = Tuple4CodecConst(a, b, c, d)
  implicit def __tuple5[A, B, C, D, E](implicit a: Codec[A], b: Codec[B], c: Codec[C], d: Codec[D], e: Codec[E]): Codec[(A, B, C, D, E)] = Tuple5CodecConst(a, b, c, d, e)
  implicit def __tuple6[A, B, C, D, E, F](implicit a: Codec[A], b: Codec[B], c: Codec[C], d: Codec[D], e: Codec[E], f: Codec[F]): Codec[(A, B, C, D, E, F)] = Tuple6CodecConst(a, b, c, d, e, f)
  implicit def __tuple7[A, B, C, D, E, F, G](implicit a: Codec[A], b: Codec[B], c: Codec[C], d: Codec[D], e: Codec[E], f: Codec[F], g: Codec[G]): Codec[(A, B, C, D, E, F, G)] = Tuple7CodecConst(a, b, c, d, e, f, g)
  implicit def __tuple8[A, B, C, D, E, F, G, H](implicit a: Codec[A], b: Codec[B], c: Codec[C], d: Codec[D], e: Codec[E], f: Codec[F], g: Codec[G], h: Codec[H]): Codec[(A, B, C, D, E, F, G, H)] = Tuple8CodecConst(a, b, c, d, e, f, g, h)
  implicit def __tuple9[A, B, C, D, E, F, G, H, I](implicit a: Codec[A], b: Codec[B], c: Codec[C], d: Codec[D], e: Codec[E], f: Codec[F], g: Codec[G], h: Codec[H], i: Codec[I]): Codec[(A, B, C, D, E, F, G, H, I)] = Tuple9CodecConst(a, b, c, d, e, f, g, h, i)
  implicit def __tuple10[A, B, C, D, E, F, G, H, I, J](implicit a: Codec[A], b: Codec[B], c: Codec[C], d: Codec[D], e: Codec[E], f: Codec[F], g: Codec[G], h: Codec[H], i: Codec[I], j: Codec[J]): Codec[(A, B, C, D, E, F, G, H, I, J)] = Tuple10CodecConst(a, b, c, d, e, f, g, h, i, j)
  implicit def __tuple11[A, B, C, D, E, F, G, H, I, J, K](implicit a: Codec[A], b: Codec[B], c: Codec[C], d: Codec[D], e: Codec[E], f: Codec[F], g: Codec[G], h: Codec[H], i: Codec[I], j: Codec[J], k: Codec[K]): Codec[(A, B, C, D, E, F, G, H, I, J, K)] = Tuple11CodecConst(a, b, c, d, e, f, g, h, i, j, k)
  implicit def __tuple12[A, B, C, D, E, F, G, H, I, J, K, L](implicit a: Codec[A], b: Codec[B], c: Codec[C], d: Codec[D], e: Codec[E], f: Codec[F], g: Codec[G], h: Codec[H], i: Codec[I], j: Codec[J], k: Codec[K], l: Codec[L]): Codec[(A, B, C, D, E, F, G, H, I, J, K, L)] = Tuple12CodecConst(a, b, c, d, e, f, g, h, i, j, k, l)
  implicit def __tuple13[A, B, C, D, E, F, G, H, I, J, K, L, M](implicit a: Codec[A], b: Codec[B], c: Codec[C], d: Codec[D], e: Codec[E], f: Codec[F], g: Codec[G], h: Codec[H], i: Codec[I], j: Codec[J], k: Codec[K], l: Codec[L], m: Codec[M]): Codec[(A, B, C, D, E, F, G, H, I, J, K, L, M)] = Tuple13CodecConst(a, b, c, d, e, f, g, h, i, j, k, l, m)
  implicit def __tuple14[A, B, C, D, E, F, G, H, I, J, K, L, M, N](implicit a: Codec[A], b: Codec[B], c: Codec[C], d: Codec[D], e: Codec[E], f: Codec[F], g: Codec[G], h: Codec[H], i: Codec[I], j: Codec[J], k: Codec[K], l: Codec[L], m: Codec[M], n: Codec[N]): Codec[(A, B, C, D, E, F, G, H, I, J, K, L, M, N)] = Tuple14CodecConst(a, b, c, d, e, f, g, h, i, j, k, l, m, n)
  implicit def __tuple15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O](implicit a: Codec[A], b: Codec[B], c: Codec[C], d: Codec[D], e: Codec[E], f: Codec[F], g: Codec[G], h: Codec[H], i: Codec[I], j: Codec[J], k: Codec[K], l: Codec[L], m: Codec[M], n: Codec[N], o: Codec[O]): Codec[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)] = Tuple15CodecConst(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o)
  implicit def __tuple16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P](implicit a: Codec[A], b: Codec[B], c: Codec[C], d: Codec[D], e: Codec[E], f: Codec[F], g: Codec[G], h: Codec[H], i: Codec[I], j: Codec[J], k: Codec[K], l: Codec[L], m: Codec[M], n: Codec[N], o: Codec[O], p: Codec[P]): Codec[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)] = Tuple16CodecConst(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p)
  implicit def __tuple17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q](implicit a: Codec[A], b: Codec[B], c: Codec[C], d: Codec[D], e: Codec[E], f: Codec[F], g: Codec[G], h: Codec[H], i: Codec[I], j: Codec[J], k: Codec[K], l: Codec[L], m: Codec[M], n: Codec[N], o: Codec[O], p: Codec[P], q: Codec[Q]): Codec[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)] = Tuple17CodecConst(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q)
  implicit def __tuple18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R](implicit a: Codec[A], b: Codec[B], c: Codec[C], d: Codec[D], e: Codec[E], f: Codec[F], g: Codec[G], h: Codec[H], i: Codec[I], j: Codec[J], k: Codec[K], l: Codec[L], m: Codec[M], n: Codec[N], o: Codec[O], p: Codec[P], q: Codec[Q], r: Codec[R]): Codec[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)] = Tuple18CodecConst(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r)
  implicit def __tuple19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S](implicit a: Codec[A], b: Codec[B], c: Codec[C], d: Codec[D], e: Codec[E], f: Codec[F], g: Codec[G], h: Codec[H], i: Codec[I], j: Codec[J], k: Codec[K], l: Codec[L], m: Codec[M], n: Codec[N], o: Codec[O], p: Codec[P], q: Codec[Q], r: Codec[R], s: Codec[S]): Codec[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)] = Tuple19CodecConst(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s)
  implicit def __tuple20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T](implicit a: Codec[A], b: Codec[B], c: Codec[C], d: Codec[D], e: Codec[E], f: Codec[F], g: Codec[G], h: Codec[H], i: Codec[I], j: Codec[J], k: Codec[K], l: Codec[L], m: Codec[M], n: Codec[N], o: Codec[O], p: Codec[P], q: Codec[Q], r: Codec[R], s: Codec[S], t: Codec[T]): Codec[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)] = Tuple20CodecConst(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t)
  implicit def __tuple21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U](implicit a: Codec[A], b: Codec[B], c: Codec[C], d: Codec[D], e: Codec[E], f: Codec[F], g: Codec[G], h: Codec[H], i: Codec[I], j: Codec[J], k: Codec[K], l: Codec[L], m: Codec[M], n: Codec[N], o: Codec[O], p: Codec[P], q: Codec[Q], r: Codec[R], s: Codec[S], t: Codec[T], u: Codec[U]): Codec[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U)] = Tuple21CodecConst(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u)
  implicit def __tuple22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V](implicit a: Codec[A], b: Codec[B], c: Codec[C], d: Codec[D], e: Codec[E], f: Codec[F], g: Codec[G], h: Codec[H], i: Codec[I], j: Codec[J], k: Codec[K], l: Codec[L], m: Codec[M], n: Codec[N], o: Codec[O], p: Codec[P], q: Codec[Q], r: Codec[R], s: Codec[S], t: Codec[T], u: Codec[U], v: Codec[V]): Codec[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V)] = Tuple22CodecConst(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v)

  implicit def __tuple2[A, B](implicit a: Read[A], b: Read[B]): Read[(A, B)] = Tuple2CodecConst(a, b)
  implicit def __tuple3[A, B, C](implicit a: Read[A], b: Read[B], c: Read[C]): Read[(A, B, C)] = Tuple3CodecConst(a, b, c)
  implicit def __tuple4[A, B, C, D](implicit a: Read[A], b: Read[B], c: Read[C], d: Read[D]): Read[(A, B, C, D)] = Tuple4CodecConst(a, b, c, d)
  implicit def __tuple5[A, B, C, D, E](implicit a: Read[A], b: Read[B], c: Read[C], d: Read[D], e: Read[E]): Read[(A, B, C, D, E)] = Tuple5CodecConst(a, b, c, d, e)
  implicit def __tuple6[A, B, C, D, E, F](implicit a: Read[A], b: Read[B], c: Read[C], d: Read[D], e: Read[E], f: Read[F]): Read[(A, B, C, D, E, F)] = Tuple6CodecConst(a, b, c, d, e, f)
  implicit def __tuple7[A, B, C, D, E, F, G](implicit a: Read[A], b: Read[B], c: Read[C], d: Read[D], e: Read[E], f: Read[F], g: Read[G]): Read[(A, B, C, D, E, F, G)] = Tuple7CodecConst(a, b, c, d, e, f, g)
  implicit def __tuple8[A, B, C, D, E, F, G, H](implicit a: Read[A], b: Read[B], c: Read[C], d: Read[D], e: Read[E], f: Read[F], g: Read[G], h: Read[H]): Read[(A, B, C, D, E, F, G, H)] = Tuple8CodecConst(a, b, c, d, e, f, g, h)
  implicit def __tuple9[A, B, C, D, E, F, G, H, I](implicit a: Read[A], b: Read[B], c: Read[C], d: Read[D], e: Read[E], f: Read[F], g: Read[G], h: Read[H], i: Read[I]): Read[(A, B, C, D, E, F, G, H, I)] = Tuple9CodecConst(a, b, c, d, e, f, g, h, i)
  implicit def __tuple10[A, B, C, D, E, F, G, H, I, J](implicit a: Read[A], b: Read[B], c: Read[C], d: Read[D], e: Read[E], f: Read[F], g: Read[G], h: Read[H], i: Read[I], j: Read[J]): Read[(A, B, C, D, E, F, G, H, I, J)] = Tuple10CodecConst(a, b, c, d, e, f, g, h, i, j)
  implicit def __tuple11[A, B, C, D, E, F, G, H, I, J, K](implicit a: Read[A], b: Read[B], c: Read[C], d: Read[D], e: Read[E], f: Read[F], g: Read[G], h: Read[H], i: Read[I], j: Read[J], k: Read[K]): Read[(A, B, C, D, E, F, G, H, I, J, K)] = Tuple11CodecConst(a, b, c, d, e, f, g, h, i, j, k)
  implicit def __tuple12[A, B, C, D, E, F, G, H, I, J, K, L](implicit a: Read[A], b: Read[B], c: Read[C], d: Read[D], e: Read[E], f: Read[F], g: Read[G], h: Read[H], i: Read[I], j: Read[J], k: Read[K], l: Read[L]): Read[(A, B, C, D, E, F, G, H, I, J, K, L)] = Tuple12CodecConst(a, b, c, d, e, f, g, h, i, j, k, l)
  implicit def __tuple13[A, B, C, D, E, F, G, H, I, J, K, L, M](implicit a: Read[A], b: Read[B], c: Read[C], d: Read[D], e: Read[E], f: Read[F], g: Read[G], h: Read[H], i: Read[I], j: Read[J], k: Read[K], l: Read[L], m: Read[M]): Read[(A, B, C, D, E, F, G, H, I, J, K, L, M)] = Tuple13CodecConst(a, b, c, d, e, f, g, h, i, j, k, l, m)
  implicit def __tuple14[A, B, C, D, E, F, G, H, I, J, K, L, M, N](implicit a: Read[A], b: Read[B], c: Read[C], d: Read[D], e: Read[E], f: Read[F], g: Read[G], h: Read[H], i: Read[I], j: Read[J], k: Read[K], l: Read[L], m: Read[M], n: Read[N]): Read[(A, B, C, D, E, F, G, H, I, J, K, L, M, N)] = Tuple14CodecConst(a, b, c, d, e, f, g, h, i, j, k, l, m, n)
  implicit def __tuple15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O](implicit a: Read[A], b: Read[B], c: Read[C], d: Read[D], e: Read[E], f: Read[F], g: Read[G], h: Read[H], i: Read[I], j: Read[J], k: Read[K], l: Read[L], m: Read[M], n: Read[N], o: Read[O]): Read[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)] = Tuple15CodecConst(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o)
  implicit def __tuple16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P](implicit a: Read[A], b: Read[B], c: Read[C], d: Read[D], e: Read[E], f: Read[F], g: Read[G], h: Read[H], i: Read[I], j: Read[J], k: Read[K], l: Read[L], m: Read[M], n: Read[N], o: Read[O], p: Read[P]): Read[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)] = Tuple16CodecConst(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p)
  implicit def __tuple17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q](implicit a: Read[A], b: Read[B], c: Read[C], d: Read[D], e: Read[E], f: Read[F], g: Read[G], h: Read[H], i: Read[I], j: Read[J], k: Read[K], l: Read[L], m: Read[M], n: Read[N], o: Read[O], p: Read[P], q: Read[Q]): Read[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)] = Tuple17CodecConst(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q)
  implicit def __tuple18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R](implicit a: Read[A], b: Read[B], c: Read[C], d: Read[D], e: Read[E], f: Read[F], g: Read[G], h: Read[H], i: Read[I], j: Read[J], k: Read[K], l: Read[L], m: Read[M], n: Read[N], o: Read[O], p: Read[P], q: Read[Q], r: Read[R]): Read[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)] = Tuple18CodecConst(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r)
  implicit def __tuple19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S](implicit a: Read[A], b: Read[B], c: Read[C], d: Read[D], e: Read[E], f: Read[F], g: Read[G], h: Read[H], i: Read[I], j: Read[J], k: Read[K], l: Read[L], m: Read[M], n: Read[N], o: Read[O], p: Read[P], q: Read[Q], r: Read[R], s: Read[S]): Read[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)] = Tuple19CodecConst(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s)
  implicit def __tuple20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T](implicit a: Read[A], b: Read[B], c: Read[C], d: Read[D], e: Read[E], f: Read[F], g: Read[G], h: Read[H], i: Read[I], j: Read[J], k: Read[K], l: Read[L], m: Read[M], n: Read[N], o: Read[O], p: Read[P], q: Read[Q], r: Read[R], s: Read[S], t: Read[T]): Read[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)] = Tuple20CodecConst(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t)
  implicit def __tuple21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U](implicit a: Read[A], b: Read[B], c: Read[C], d: Read[D], e: Read[E], f: Read[F], g: Read[G], h: Read[H], i: Read[I], j: Read[J], k: Read[K], l: Read[L], m: Read[M], n: Read[N], o: Read[O], p: Read[P], q: Read[Q], r: Read[R], s: Read[S], t: Read[T], u: Read[U]): Read[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U)] = Tuple21CodecConst(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u)
  implicit def __tuple22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V](implicit a: Read[A], b: Read[B], c: Read[C], d: Read[D], e: Read[E], f: Read[F], g: Read[G], h: Read[H], i: Read[I], j: Read[J], k: Read[K], l: Read[L], m: Read[M], n: Read[N], o: Read[O], p: Read[P], q: Read[Q], r: Read[R], s: Read[S], t: Read[T], u: Read[U], v: Read[V]): Read[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V)] = Tuple22CodecConst(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v)
  
  implicit def __tuple2[A, B](implicit a: Write[A], b: Write[B]): Write[(A, B)] = Tuple2CodecConst(a, b)
  implicit def __tuple3[A, B, C](implicit a: Write[A], b: Write[B], c: Write[C]): Write[(A, B, C)] = Tuple3CodecConst(a, b, c)
  implicit def __tuple4[A, B, C, D](implicit a: Write[A], b: Write[B], c: Write[C], d: Write[D]): Write[(A, B, C, D)] = Tuple4CodecConst(a, b, c, d)
  implicit def __tuple5[A, B, C, D, E](implicit a: Write[A], b: Write[B], c: Write[C], d: Write[D], e: Write[E]): Write[(A, B, C, D, E)] = Tuple5CodecConst(a, b, c, d, e)
  implicit def __tuple6[A, B, C, D, E, F](implicit a: Write[A], b: Write[B], c: Write[C], d: Write[D], e: Write[E], f: Write[F]): Write[(A, B, C, D, E, F)] = Tuple6CodecConst(a, b, c, d, e, f)
  implicit def __tuple7[A, B, C, D, E, F, G](implicit a: Write[A], b: Write[B], c: Write[C], d: Write[D], e: Write[E], f: Write[F], g: Write[G]): Write[(A, B, C, D, E, F, G)] = Tuple7CodecConst(a, b, c, d, e, f, g)
  implicit def __tuple8[A, B, C, D, E, F, G, H](implicit a: Write[A], b: Write[B], c: Write[C], d: Write[D], e: Write[E], f: Write[F], g: Write[G], h: Write[H]): Write[(A, B, C, D, E, F, G, H)] = Tuple8CodecConst(a, b, c, d, e, f, g, h)
  implicit def __tuple9[A, B, C, D, E, F, G, H, I](implicit a: Write[A], b: Write[B], c: Write[C], d: Write[D], e: Write[E], f: Write[F], g: Write[G], h: Write[H], i: Write[I]): Write[(A, B, C, D, E, F, G, H, I)] = Tuple9CodecConst(a, b, c, d, e, f, g, h, i)
  implicit def __tuple10[A, B, C, D, E, F, G, H, I, J](implicit a: Write[A], b: Write[B], c: Write[C], d: Write[D], e: Write[E], f: Write[F], g: Write[G], h: Write[H], i: Write[I], j: Write[J]): Write[(A, B, C, D, E, F, G, H, I, J)] = Tuple10CodecConst(a, b, c, d, e, f, g, h, i, j)
  implicit def __tuple11[A, B, C, D, E, F, G, H, I, J, K](implicit a: Write[A], b: Write[B], c: Write[C], d: Write[D], e: Write[E], f: Write[F], g: Write[G], h: Write[H], i: Write[I], j: Write[J], k: Write[K]): Write[(A, B, C, D, E, F, G, H, I, J, K)] = Tuple11CodecConst(a, b, c, d, e, f, g, h, i, j, k)
  implicit def __tuple12[A, B, C, D, E, F, G, H, I, J, K, L](implicit a: Write[A], b: Write[B], c: Write[C], d: Write[D], e: Write[E], f: Write[F], g: Write[G], h: Write[H], i: Write[I], j: Write[J], k: Write[K], l: Write[L]): Write[(A, B, C, D, E, F, G, H, I, J, K, L)] = Tuple12CodecConst(a, b, c, d, e, f, g, h, i, j, k, l)
  implicit def __tuple13[A, B, C, D, E, F, G, H, I, J, K, L, M](implicit a: Write[A], b: Write[B], c: Write[C], d: Write[D], e: Write[E], f: Write[F], g: Write[G], h: Write[H], i: Write[I], j: Write[J], k: Write[K], l: Write[L], m: Write[M]): Write[(A, B, C, D, E, F, G, H, I, J, K, L, M)] = Tuple13CodecConst(a, b, c, d, e, f, g, h, i, j, k, l, m)
  implicit def __tuple14[A, B, C, D, E, F, G, H, I, J, K, L, M, N](implicit a: Write[A], b: Write[B], c: Write[C], d: Write[D], e: Write[E], f: Write[F], g: Write[G], h: Write[H], i: Write[I], j: Write[J], k: Write[K], l: Write[L], m: Write[M], n: Write[N]): Write[(A, B, C, D, E, F, G, H, I, J, K, L, M, N)] = Tuple14CodecConst(a, b, c, d, e, f, g, h, i, j, k, l, m, n)
  implicit def __tuple15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O](implicit a: Write[A], b: Write[B], c: Write[C], d: Write[D], e: Write[E], f: Write[F], g: Write[G], h: Write[H], i: Write[I], j: Write[J], k: Write[K], l: Write[L], m: Write[M], n: Write[N], o: Write[O]): Write[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)] = Tuple15CodecConst(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o)
  implicit def __tuple16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P](implicit a: Write[A], b: Write[B], c: Write[C], d: Write[D], e: Write[E], f: Write[F], g: Write[G], h: Write[H], i: Write[I], j: Write[J], k: Write[K], l: Write[L], m: Write[M], n: Write[N], o: Write[O], p: Write[P]): Write[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)] = Tuple16CodecConst(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p)
  implicit def __tuple17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q](implicit a: Write[A], b: Write[B], c: Write[C], d: Write[D], e: Write[E], f: Write[F], g: Write[G], h: Write[H], i: Write[I], j: Write[J], k: Write[K], l: Write[L], m: Write[M], n: Write[N], o: Write[O], p: Write[P], q: Write[Q]): Write[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)] = Tuple17CodecConst(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q)
  implicit def __tuple18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R](implicit a: Write[A], b: Write[B], c: Write[C], d: Write[D], e: Write[E], f: Write[F], g: Write[G], h: Write[H], i: Write[I], j: Write[J], k: Write[K], l: Write[L], m: Write[M], n: Write[N], o: Write[O], p: Write[P], q: Write[Q], r: Write[R]): Write[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)] = Tuple18CodecConst(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r)
  implicit def __tuple19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S](implicit a: Write[A], b: Write[B], c: Write[C], d: Write[D], e: Write[E], f: Write[F], g: Write[G], h: Write[H], i: Write[I], j: Write[J], k: Write[K], l: Write[L], m: Write[M], n: Write[N], o: Write[O], p: Write[P], q: Write[Q], r: Write[R], s: Write[S]): Write[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)] = Tuple19CodecConst(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s)
  implicit def __tuple20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T](implicit a: Write[A], b: Write[B], c: Write[C], d: Write[D], e: Write[E], f: Write[F], g: Write[G], h: Write[H], i: Write[I], j: Write[J], k: Write[K], l: Write[L], m: Write[M], n: Write[N], o: Write[O], p: Write[P], q: Write[Q], r: Write[R], s: Write[S], t: Write[T]): Write[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)] = Tuple20CodecConst(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t)
  implicit def __tuple21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U](implicit a: Write[A], b: Write[B], c: Write[C], d: Write[D], e: Write[E], f: Write[F], g: Write[G], h: Write[H], i: Write[I], j: Write[J], k: Write[K], l: Write[L], m: Write[M], n: Write[N], o: Write[O], p: Write[P], q: Write[Q], r: Write[R], s: Write[S], t: Write[T], u: Write[U]): Write[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U)] = Tuple21CodecConst(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u)
  implicit def __tuple22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V](implicit a: Write[A], b: Write[B], c: Write[C], d: Write[D], e: Write[E], f: Write[F], g: Write[G], h: Write[H], i: Write[I], j: Write[J], k: Write[K], l: Write[L], m: Write[M], n: Write[N], o: Write[O], p: Write[P], q: Write[Q], r: Write[R], s: Write[S], t: Write[T], u: Write[U], v: Write[V]): Write[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V)] = Tuple22CodecConst(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v)

}
