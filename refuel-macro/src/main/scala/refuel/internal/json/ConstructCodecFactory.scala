package refuel.internal.json

import refuel.internal.PropertyDebugModeEnabler
import refuel.internal.json.codec.builder.JsKeyLitOps
import refuel.json.{Codec, Json}

import scala.reflect.macros.blackbox

class ConstructCodecFactory(override val c: blackbox.Context) extends CaseCodecFactory(c) with PropertyDebugModeEnabler {

  import c.universe._

  private[this] final val JsonPkg = q"refuel.json"
  private[this] final val Codecs = q"refuel.json.codecs"
  private[this] final val JsonEntryPkg = q"refuel.json.entry"

  def fromConst1[A: c.WeakTypeTag, Z](n1: c.Expr[JsKeyLitOps])
                                     (apl: c.Expr[A => Z])
                                     (upl: c.Expr[Z => Option[A]]): c.Expr[Codec[Z]] = {
    c.Expr[Codec[Z]] {
      q"""
        new $Codecs.JoinableCodec.T1($apl)($upl)(${recall(weakTypeOf[A])}){
          override val keyLiteralRef: ${weakTypeOf[JsKeyLitOps]} = ${reify(n1.splice)}
        }
         """
    }
  }

  def fromConst2[A: c.WeakTypeTag, B: c.WeakTypeTag, Z]
  (n1: c.Expr[JsKeyLitOps], n2: c.Expr[JsKeyLitOps])
  (apl: c.Expr[(A, B) => Z])
  (upl: c.Expr[Z => Option[(A, B)]]): c.Expr[Codec[Z]] = {
    c.Expr[Codec[Z]] {
      q"""
         new $Codecs.JoinableCodec.T2($apl)($upl)(
           ${recall(weakTypeOf[A])}, ${recall(weakTypeOf[B])}
         ){
          override val keyLiteralRef: ${weakTypeOf[JsKeyLitOps]} = ${
        reify {
          n1.splice ++
            n2.splice
        }
      }
        }
       """
    }
  }

  def fromConst3[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, Z]
  (n1: c.Expr[JsKeyLitOps], n2: c.Expr[JsKeyLitOps], n3: c.Expr[JsKeyLitOps])
  (apl: c.Expr[(A, B, C) => Z])
  (upl: c.Expr[Z => Option[(A, B, C)]]): c.Expr[Codec[Z]] = {
    c.Expr[Codec[Z]] {
      q"""
         new $Codecs.JoinableCodec.T3($apl)($upl)(
           ${recall(weakTypeOf[A])}, ${recall(weakTypeOf[B])}, ${recall(weakTypeOf[C])}
         ){
          override val keyLiteralRef: ${weakTypeOf[JsKeyLitOps]} = ${
        reify {
          n1.splice ++
            n2.splice ++
            n3.splice
        }
      }
        }
       """
    }
  }

  def fromConst4[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, Z]
  (n1: c.Expr[JsKeyLitOps], n2: c.Expr[JsKeyLitOps], n3: c.Expr[JsKeyLitOps], n4: c.Expr[JsKeyLitOps])
  (apl: c.Expr[(A, B, C, D) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D)]]): c.Expr[Codec[Z]] = {
    c.Expr[Codec[Z]] {
      q"""
         new $Codecs.JoinableCodec.T4($apl)($upl)(
           ${recall(weakTypeOf[A])}, ${recall(weakTypeOf[B])}, ${recall(weakTypeOf[C])}, ${recall(weakTypeOf[D])}
         ){
          override val keyLiteralRef: ${weakTypeOf[JsKeyLitOps]} = ${
        reify {
          n1.splice ++
            n2.splice ++
            n3.splice ++
            n4.splice
        }
      }
        }
       """
    }
  }

  def fromConst5[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, Z]
  (n1: c.Expr[JsKeyLitOps], n2: c.Expr[JsKeyLitOps], n3: c.Expr[JsKeyLitOps], n4: c.Expr[JsKeyLitOps], n5: c.Expr[JsKeyLitOps])
  (apl: c.Expr[(A, B, C, D, E) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D, E)]]): c.Expr[Codec[Z]] = {
    c.Expr[Codec[Z]] {
      q"""
         new $Codecs.JoinableCodec.T5($apl)($upl)(
           ${recall(weakTypeOf[A])}, ${recall(weakTypeOf[B])}, ${recall(weakTypeOf[C])}, ${recall(weakTypeOf[D])}, ${recall(weakTypeOf[E])}
         ){
          override val keyLiteralRef: ${weakTypeOf[JsKeyLitOps]} = ${
        reify {
          n1.splice ++
            n2.splice ++
            n3.splice ++
            n4.splice ++
            n5.splice
        }
      }
        }
       """
    }
  }

  def fromConst6[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, Z]
  (n1: c.Expr[JsKeyLitOps], n2: c.Expr[JsKeyLitOps], n3: c.Expr[JsKeyLitOps], n4: c.Expr[JsKeyLitOps], n5: c.Expr[JsKeyLitOps], n6: c.Expr[JsKeyLitOps])
  (apl: c.Expr[(A, B, C, D, E, F) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D, E, F)]]): c.Expr[Codec[Z]] = {
    c.Expr[Codec[Z]] {
      q"""
         new $Codecs.JoinableCodec.T6($apl)($upl)(
           ${recall(weakTypeOf[A])},
           ${recall(weakTypeOf[B])},
           ${recall(weakTypeOf[C])},
           ${recall(weakTypeOf[D])},
           ${recall(weakTypeOf[E])},
           ${recall(weakTypeOf[F])}
         ){
          override val keyLiteralRef: ${weakTypeOf[JsKeyLitOps]} = ${
        reify {
          n1.splice ++
            n2.splice ++
            n3.splice ++
            n4.splice ++
            n5.splice ++
            n6.splice
        }
      }
        }
       """
    }
  }

  def fromConst7[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, Z]
  (n1: c.Expr[JsKeyLitOps], n2: c.Expr[JsKeyLitOps], n3: c.Expr[JsKeyLitOps], n4: c.Expr[JsKeyLitOps], n5: c.Expr[JsKeyLitOps], n6: c.Expr[JsKeyLitOps], n7: c.Expr[JsKeyLitOps])
  (apl: c.Expr[(A, B, C, D, E, F, G) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D, E, F, G)]])(implicit zt: c.WeakTypeTag[Z]): c.Expr[Codec[Z]] = {
    c.Expr[Codec[Z]] {
      q"""
         new $Codecs.JoinableCodec.T7($apl)($upl)(
           ${recall(weakTypeOf[A])},
           ${recall(weakTypeOf[B])},
           ${recall(weakTypeOf[C])},
           ${recall(weakTypeOf[D])},
           ${recall(weakTypeOf[E])},
           ${recall(weakTypeOf[F])},
           ${recall(weakTypeOf[G])}
         ){
          override val keyLiteralRef: ${weakTypeOf[JsKeyLitOps]} = ${
        reify {
          n1.splice ++
            n2.splice ++
            n3.splice ++
            n4.splice ++
            n5.splice ++
            n6.splice ++
            n7.splice
        }
      }
        }
       """
    }
  }

  def fromConst8[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, Z]
  (n1: c.Expr[JsKeyLitOps], n2: c.Expr[JsKeyLitOps], n3: c.Expr[JsKeyLitOps], n4: c.Expr[JsKeyLitOps], n5: c.Expr[JsKeyLitOps], n6: c.Expr[JsKeyLitOps], n7: c.Expr[JsKeyLitOps], n8: c.Expr[JsKeyLitOps])
  (apl: c.Expr[(A, B, C, D, E, F, G, H) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D, E, F, G, H)]])(implicit zt: c.WeakTypeTag[Z]): c.Expr[Codec[Z]] = {
    c.Expr[Codec[Z]] {
      q"""
         new $Codecs.JoinableCodec.T8($apl)($upl)(
           ${recall(weakTypeOf[A])},
           ${recall(weakTypeOf[B])},
           ${recall(weakTypeOf[C])},
           ${recall(weakTypeOf[D])},
           ${recall(weakTypeOf[E])},
           ${recall(weakTypeOf[F])},
           ${recall(weakTypeOf[G])},
           ${recall(weakTypeOf[H])}
         ){
          override val keyLiteralRef: ${weakTypeOf[JsKeyLitOps]} = ${
        reify {
          n1.splice ++
            n2.splice ++
            n3.splice ++
            n4.splice ++
            n5.splice ++
            n6.splice ++
            n7.splice ++
            n8.splice
        }
      }
        }
       """
    }
  }

  def fromConst9[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, Z]
  (n1: c.Expr[JsKeyLitOps], n2: c.Expr[JsKeyLitOps], n3: c.Expr[JsKeyLitOps], n4: c.Expr[JsKeyLitOps], n5: c.Expr[JsKeyLitOps], n6: c.Expr[JsKeyLitOps], n7: c.Expr[JsKeyLitOps], n8: c.Expr[JsKeyLitOps], n9: c.Expr[JsKeyLitOps])
  (apl: c.Expr[(A, B, C, D, E, F, G, H, I) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D, E, F, G, H, I)]])(implicit zt: c.WeakTypeTag[Z]): c.Expr[Codec[Z]] = {
    c.Expr[Codec[Z]] {
      q"""
         new $Codecs.JoinableCodec.T9($apl)($upl)(
           ${recall(weakTypeOf[A])},
           ${recall(weakTypeOf[B])},
           ${recall(weakTypeOf[C])},
           ${recall(weakTypeOf[D])},
           ${recall(weakTypeOf[E])},
           ${recall(weakTypeOf[F])},
           ${recall(weakTypeOf[G])},
           ${recall(weakTypeOf[H])},
           ${recall(weakTypeOf[I])}
         ){
          override val keyLiteralRef: ${weakTypeOf[JsKeyLitOps]} = ${
        reify {
          n1.splice ++
            n2.splice ++
            n3.splice ++
            n4.splice ++
            n5.splice ++
            n6.splice ++
            n7.splice ++
            n8.splice ++
            n9.splice
        }
      }
        }
       """
    }
  }

  def fromConst10[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, Z]
  (n1: c.Expr[JsKeyLitOps], n2: c.Expr[JsKeyLitOps], n3: c.Expr[JsKeyLitOps], n4: c.Expr[JsKeyLitOps], n5: c.Expr[JsKeyLitOps], n6: c.Expr[JsKeyLitOps], n7: c.Expr[JsKeyLitOps], n8: c.Expr[JsKeyLitOps], n9: c.Expr[JsKeyLitOps], n10: c.Expr[JsKeyLitOps])
  (apl: c.Expr[(A, B, C, D, E, F, G, H, I, J) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D, E, F, G, H, I, J)]])(implicit zt: c.WeakTypeTag[Z]): c.Expr[Codec[Z]] = {
    c.Expr[Codec[Z]] {
      q"""
         new $Codecs.JoinableCodec.T10($apl)($upl)(
           ${recall(weakTypeOf[A])},
           ${recall(weakTypeOf[B])},
           ${recall(weakTypeOf[C])},
           ${recall(weakTypeOf[D])},
           ${recall(weakTypeOf[E])},
           ${recall(weakTypeOf[F])},
           ${recall(weakTypeOf[G])},
           ${recall(weakTypeOf[H])},
           ${recall(weakTypeOf[I])},
           ${recall(weakTypeOf[J])}
         ){
          override val keyLiteralRef: ${weakTypeOf[JsKeyLitOps]} = ${
        reify {
          n1.splice ++
            n2.splice ++
            n3.splice ++
            n4.splice ++
            n5.splice ++
            n6.splice ++
            n7.splice ++
            n8.splice ++
            n9.splice ++
            n10.splice
        }
      }
        }
       """
    }
  }

  def fromConst11[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, Z]
  (n1: c.Expr[JsKeyLitOps], n2: c.Expr[JsKeyLitOps], n3: c.Expr[JsKeyLitOps], n4: c.Expr[JsKeyLitOps], n5: c.Expr[JsKeyLitOps], n6: c.Expr[JsKeyLitOps], n7: c.Expr[JsKeyLitOps], n8: c.Expr[JsKeyLitOps], n9: c.Expr[JsKeyLitOps], n10: c.Expr[JsKeyLitOps], n11: c.Expr[JsKeyLitOps])
  (apl: c.Expr[(A, B, C, D, E, F, G, H, I, J, K) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D, E, F, G, H, I, J, K)]]): c.Expr[Codec[Z]] = {
    c.Expr[Codec[Z]] {
      q"""
         new $Codecs.JoinableCodec.T11($apl)($upl)(
           ${recall(weakTypeOf[A])},
           ${recall(weakTypeOf[B])},
           ${recall(weakTypeOf[C])},
           ${recall(weakTypeOf[D])},
           ${recall(weakTypeOf[E])},
           ${recall(weakTypeOf[F])},
           ${recall(weakTypeOf[G])},
           ${recall(weakTypeOf[H])},
           ${recall(weakTypeOf[I])},
           ${recall(weakTypeOf[J])},
           ${recall(weakTypeOf[K])}
         ){
          override val keyLiteralRef: ${weakTypeOf[JsKeyLitOps]} = ${
        reify {
          n1.splice ++
            n2.splice ++
            n3.splice ++
            n4.splice ++
            n5.splice ++
            n6.splice ++
            n7.splice ++
            n8.splice ++
            n9.splice ++
            n10.splice ++
            n11.splice
        }
      }
        }
       """
    }
  }

  def fromConst12[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, Z]
  (n1: c.Expr[JsKeyLitOps], n2: c.Expr[JsKeyLitOps], n3: c.Expr[JsKeyLitOps], n4: c.Expr[JsKeyLitOps], n5: c.Expr[JsKeyLitOps], n6: c.Expr[JsKeyLitOps], n7: c.Expr[JsKeyLitOps], n8: c.Expr[JsKeyLitOps], n9: c.Expr[JsKeyLitOps], n10: c.Expr[JsKeyLitOps], n11: c.Expr[JsKeyLitOps], n12: c.Expr[JsKeyLitOps])
  (apl: c.Expr[(A, B, C, D, E, F, G, H, I, J, K, L) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L)]])(implicit zt: c.WeakTypeTag[Z]): c.Expr[Codec[Z]] = {
    c.Expr[Codec[Z]] {
      q"""
         new $Codecs.JoinableCodec.T12($apl)($upl)(
           ${recall(weakTypeOf[A])},
           ${recall(weakTypeOf[B])},
           ${recall(weakTypeOf[C])},
           ${recall(weakTypeOf[D])},
           ${recall(weakTypeOf[E])},
           ${recall(weakTypeOf[F])},
           ${recall(weakTypeOf[G])},
           ${recall(weakTypeOf[H])},
           ${recall(weakTypeOf[I])},
           ${recall(weakTypeOf[J])},
           ${recall(weakTypeOf[K])},
           ${recall(weakTypeOf[L])}
         ){
          override val keyLiteralRef: ${weakTypeOf[JsKeyLitOps]} = ${
        reify {
          n1.splice ++
            n2.splice ++
            n3.splice ++
            n4.splice ++
            n5.splice ++
            n6.splice ++
            n7.splice ++
            n8.splice ++
            n9.splice ++
            n10.splice ++
            n11.splice ++
            n12.splice
        }
      }
        }
       """
    }
  }

  def fromConst13[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, Z]
  (n1: c.Expr[JsKeyLitOps], n2: c.Expr[JsKeyLitOps], n3: c.Expr[JsKeyLitOps], n4: c.Expr[JsKeyLitOps], n5: c.Expr[JsKeyLitOps], n6: c.Expr[JsKeyLitOps], n7: c.Expr[JsKeyLitOps], n8: c.Expr[JsKeyLitOps], n9: c.Expr[JsKeyLitOps], n10: c.Expr[JsKeyLitOps], n11: c.Expr[JsKeyLitOps], n12: c.Expr[JsKeyLitOps], n13: c.Expr[JsKeyLitOps])
  (apl: c.Expr[(A, B, C, D, E, F, G, H, I, J, K, L, M) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M)]])(implicit zt: c.WeakTypeTag[Z]): c.Expr[Codec[Z]] = {
    c.Expr[Codec[Z]] {
      q"""
         new $Codecs.JoinableCodec.T13($apl)($upl)(
           ${recall(weakTypeOf[A])},
           ${recall(weakTypeOf[B])},
           ${recall(weakTypeOf[C])},
           ${recall(weakTypeOf[D])},
           ${recall(weakTypeOf[E])},
           ${recall(weakTypeOf[F])},
           ${recall(weakTypeOf[G])},
           ${recall(weakTypeOf[H])},
           ${recall(weakTypeOf[I])},
           ${recall(weakTypeOf[J])},
           ${recall(weakTypeOf[K])},
           ${recall(weakTypeOf[L])},
           ${recall(weakTypeOf[M])}
         ){
          override val keyLiteralRef: ${weakTypeOf[JsKeyLitOps]} = ${
        reify {
          n1.splice ++
            n2.splice ++
            n3.splice ++
            n4.splice ++
            n5.splice ++
            n6.splice ++
            n7.splice ++
            n8.splice ++
            n9.splice ++
            n10.splice ++
            n11.splice ++
            n12.splice ++
            n13.splice
        }
      }
        }
       """
    }
  }

  def fromConst14[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, Z]
  (n1: c.Expr[JsKeyLitOps], n2: c.Expr[JsKeyLitOps], n3: c.Expr[JsKeyLitOps], n4: c.Expr[JsKeyLitOps], n5: c.Expr[JsKeyLitOps], n6: c.Expr[JsKeyLitOps], n7: c.Expr[JsKeyLitOps], n8: c.Expr[JsKeyLitOps], n9: c.Expr[JsKeyLitOps], n10: c.Expr[JsKeyLitOps], n11: c.Expr[JsKeyLitOps], n12: c.Expr[JsKeyLitOps], n13: c.Expr[JsKeyLitOps], n14: c.Expr[JsKeyLitOps])
  (apl: c.Expr[(A, B, C, D, E, F, G, H, I, J, K, L, M, N) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N)]])(implicit zt: c.WeakTypeTag[Z]): c.Expr[Codec[Z]] = {
    c.Expr[Codec[Z]] {
      q"""
         new $Codecs.JoinableCodec.T14($apl)($upl)(
           ${recall(weakTypeOf[A])},
           ${recall(weakTypeOf[B])},
           ${recall(weakTypeOf[C])},
           ${recall(weakTypeOf[D])},
           ${recall(weakTypeOf[E])},
           ${recall(weakTypeOf[F])},
           ${recall(weakTypeOf[G])},
           ${recall(weakTypeOf[H])},
           ${recall(weakTypeOf[I])},
           ${recall(weakTypeOf[J])},
           ${recall(weakTypeOf[K])},
           ${recall(weakTypeOf[L])},
           ${recall(weakTypeOf[M])},
           ${recall(weakTypeOf[N])}
         ){
          override val keyLiteralRef: ${weakTypeOf[JsKeyLitOps]} = ${
        reify {
          n1.splice ++
            n2.splice ++
            n3.splice ++
            n4.splice ++
            n5.splice ++
            n6.splice ++
            n7.splice ++
            n8.splice ++
            n9.splice ++
            n10.splice ++
            n11.splice ++
            n12.splice ++
            n13.splice ++
            n14.splice
        }
      }
        }
       """
    }
  }

  def fromConst15[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, Z]
  (n1: c.Expr[JsKeyLitOps], n2: c.Expr[JsKeyLitOps], n3: c.Expr[JsKeyLitOps], n4: c.Expr[JsKeyLitOps], n5: c.Expr[JsKeyLitOps], n6: c.Expr[JsKeyLitOps], n7: c.Expr[JsKeyLitOps], n8: c.Expr[JsKeyLitOps], n9: c.Expr[JsKeyLitOps], n10: c.Expr[JsKeyLitOps], n11: c.Expr[JsKeyLitOps], n12: c.Expr[JsKeyLitOps], n13: c.Expr[JsKeyLitOps], n14: c.Expr[JsKeyLitOps], n15: c.Expr[JsKeyLitOps])
  (apl: c.Expr[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)]])(implicit zt: c.WeakTypeTag[Z]): c.Expr[Codec[Z]] = {
    c.Expr[Codec[Z]] {
      q"""
         new $Codecs.JoinableCodec.T15($apl)($upl)(
           ${recall(weakTypeOf[A])},
           ${recall(weakTypeOf[B])},
           ${recall(weakTypeOf[C])},
           ${recall(weakTypeOf[D])},
           ${recall(weakTypeOf[E])},
           ${recall(weakTypeOf[F])},
           ${recall(weakTypeOf[G])},
           ${recall(weakTypeOf[H])},
           ${recall(weakTypeOf[I])},
           ${recall(weakTypeOf[J])},
           ${recall(weakTypeOf[K])},
           ${recall(weakTypeOf[L])},
           ${recall(weakTypeOf[M])},
           ${recall(weakTypeOf[N])},
           ${recall(weakTypeOf[O])}
         ){
          override val keyLiteralRef: ${weakTypeOf[JsKeyLitOps]} = ${
        reify {
          n1.splice ++
            n2.splice ++
            n3.splice ++
            n4.splice ++
            n5.splice ++
            n6.splice ++
            n7.splice ++
            n8.splice ++
            n9.splice ++
            n10.splice ++
            n11.splice ++
            n12.splice ++
            n13.splice ++
            n14.splice ++
            n15.splice
        }
      }
        }
       """
    }
  }

  def fromConst16[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag, Z]
  (n1: c.Expr[JsKeyLitOps], n2: c.Expr[JsKeyLitOps], n3: c.Expr[JsKeyLitOps], n4: c.Expr[JsKeyLitOps], n5: c.Expr[JsKeyLitOps], n6: c.Expr[JsKeyLitOps], n7: c.Expr[JsKeyLitOps], n8: c.Expr[JsKeyLitOps], n9: c.Expr[JsKeyLitOps], n10: c.Expr[JsKeyLitOps], n11: c.Expr[JsKeyLitOps], n12: c.Expr[JsKeyLitOps], n13: c.Expr[JsKeyLitOps], n14: c.Expr[JsKeyLitOps], n15: c.Expr[JsKeyLitOps], n16: c.Expr[JsKeyLitOps])
  (apl: c.Expr[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)]])(implicit zt: c.WeakTypeTag[Z]): c.Expr[Codec[Z]] = {
    c.Expr[Codec[Z]] {
      q"""
         new $Codecs.JoinableCodec.T16($apl)($upl)(
           ${recall(weakTypeOf[A])},
           ${recall(weakTypeOf[B])},
           ${recall(weakTypeOf[C])},
           ${recall(weakTypeOf[D])},
           ${recall(weakTypeOf[E])},
           ${recall(weakTypeOf[F])},
           ${recall(weakTypeOf[G])},
           ${recall(weakTypeOf[H])},
           ${recall(weakTypeOf[I])},
           ${recall(weakTypeOf[J])},
           ${recall(weakTypeOf[K])},
           ${recall(weakTypeOf[L])},
           ${recall(weakTypeOf[M])},
           ${recall(weakTypeOf[N])},
           ${recall(weakTypeOf[O])},
           ${recall(weakTypeOf[P])}
         ){
          override val keyLiteralRef: ${weakTypeOf[JsKeyLitOps]} = ${
        reify {
          n1.splice ++
            n2.splice ++
            n3.splice ++
            n4.splice ++
            n5.splice ++
            n6.splice ++
            n7.splice ++
            n8.splice ++
            n9.splice ++
            n10.splice ++
            n11.splice ++
            n12.splice ++
            n13.splice ++
            n14.splice ++
            n15.splice ++
            n16.splice
        }
      }
        }
       """
    }
  }

  def fromConst17[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag, Q: c.WeakTypeTag, Z]
  (n1: c.Expr[JsKeyLitOps], n2: c.Expr[JsKeyLitOps], n3: c.Expr[JsKeyLitOps], n4: c.Expr[JsKeyLitOps], n5: c.Expr[JsKeyLitOps], n6: c.Expr[JsKeyLitOps], n7: c.Expr[JsKeyLitOps], n8: c.Expr[JsKeyLitOps], n9: c.Expr[JsKeyLitOps], n10: c.Expr[JsKeyLitOps], n11: c.Expr[JsKeyLitOps], n12: c.Expr[JsKeyLitOps], n13: c.Expr[JsKeyLitOps], n14: c.Expr[JsKeyLitOps], n15: c.Expr[JsKeyLitOps], n16: c.Expr[JsKeyLitOps], n17: c.Expr[JsKeyLitOps])
  (apl: c.Expr[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)]])(implicit zt: c.WeakTypeTag[Z]): c.Expr[Codec[Z]] = {
    c.Expr[Codec[Z]] {
      q"""
         new $Codecs.JoinableCodec.T17($apl)($upl)(
           ${recall(weakTypeOf[A])},
           ${recall(weakTypeOf[B])},
           ${recall(weakTypeOf[C])},
           ${recall(weakTypeOf[D])},
           ${recall(weakTypeOf[E])},
           ${recall(weakTypeOf[F])},
           ${recall(weakTypeOf[G])},
           ${recall(weakTypeOf[H])},
           ${recall(weakTypeOf[I])},
           ${recall(weakTypeOf[J])},
           ${recall(weakTypeOf[K])},
           ${recall(weakTypeOf[L])},
           ${recall(weakTypeOf[M])},
           ${recall(weakTypeOf[N])},
           ${recall(weakTypeOf[O])},
           ${recall(weakTypeOf[P])},
           ${recall(weakTypeOf[Q])}
         ){
          override val keyLiteralRef: ${weakTypeOf[JsKeyLitOps]} = ${
        reify {
          n1.splice ++
            n2.splice ++
            n3.splice ++
            n4.splice ++
            n5.splice ++
            n6.splice ++
            n7.splice ++
            n8.splice ++
            n9.splice ++
            n10.splice ++
            n11.splice ++
            n12.splice ++
            n13.splice ++
            n14.splice ++
            n15.splice ++
            n16.splice ++
            n17.splice
        }
      }
        }
       """
    }
  }

  def fromConst18[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag, Q: c.WeakTypeTag, R: c.WeakTypeTag, Z]
  (n1: c.Expr[JsKeyLitOps], n2: c.Expr[JsKeyLitOps], n3: c.Expr[JsKeyLitOps], n4: c.Expr[JsKeyLitOps], n5: c.Expr[JsKeyLitOps], n6: c.Expr[JsKeyLitOps], n7: c.Expr[JsKeyLitOps], n8: c.Expr[JsKeyLitOps], n9: c.Expr[JsKeyLitOps], n10: c.Expr[JsKeyLitOps], n11: c.Expr[JsKeyLitOps], n12: c.Expr[JsKeyLitOps], n13: c.Expr[JsKeyLitOps], n14: c.Expr[JsKeyLitOps], n15: c.Expr[JsKeyLitOps], n16: c.Expr[JsKeyLitOps], n17: c.Expr[JsKeyLitOps], n18: c.Expr[JsKeyLitOps])
  (apl: c.Expr[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)]])(implicit zt: c.WeakTypeTag[Z]): c.Expr[Codec[Z]] = {
    c.Expr[Codec[Z]] {
      q"""
         new $Codecs.JoinableCodec.T18($apl)($upl)(
           ${recall(weakTypeOf[A])},
           ${recall(weakTypeOf[B])},
           ${recall(weakTypeOf[C])},
           ${recall(weakTypeOf[D])},
           ${recall(weakTypeOf[E])},
           ${recall(weakTypeOf[F])},
           ${recall(weakTypeOf[G])},
           ${recall(weakTypeOf[H])},
           ${recall(weakTypeOf[I])},
           ${recall(weakTypeOf[J])},
           ${recall(weakTypeOf[K])},
           ${recall(weakTypeOf[L])},
           ${recall(weakTypeOf[M])},
           ${recall(weakTypeOf[N])},
           ${recall(weakTypeOf[O])},
           ${recall(weakTypeOf[P])},
           ${recall(weakTypeOf[Q])},
           ${recall(weakTypeOf[R])}
         ){
          override val keyLiteralRef: ${weakTypeOf[JsKeyLitOps]} = ${
        reify {
          n1.splice ++
            n2.splice ++
            n3.splice ++
            n4.splice ++
            n5.splice ++
            n6.splice ++
            n7.splice ++
            n8.splice ++
            n9.splice ++
            n10.splice ++
            n11.splice ++
            n12.splice ++
            n13.splice ++
            n14.splice ++
            n15.splice ++
            n16.splice ++
            n17.splice ++
            n18.splice
        }
      }
        }
       """
    }
  }

  def fromConst19[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag, Q: c.WeakTypeTag, R: c.WeakTypeTag, S: c.WeakTypeTag, Z]
  (n1: c.Expr[JsKeyLitOps], n2: c.Expr[JsKeyLitOps], n3: c.Expr[JsKeyLitOps], n4: c.Expr[JsKeyLitOps], n5: c.Expr[JsKeyLitOps], n6: c.Expr[JsKeyLitOps], n7: c.Expr[JsKeyLitOps], n8: c.Expr[JsKeyLitOps], n9: c.Expr[JsKeyLitOps], n10: c.Expr[JsKeyLitOps], n11: c.Expr[JsKeyLitOps], n12: c.Expr[JsKeyLitOps], n13: c.Expr[JsKeyLitOps], n14: c.Expr[JsKeyLitOps], n15: c.Expr[JsKeyLitOps], n16: c.Expr[JsKeyLitOps], n17: c.Expr[JsKeyLitOps], n18: c.Expr[JsKeyLitOps], n19: c.Expr[JsKeyLitOps])
  (apl: c.Expr[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)]])(implicit zt: c.WeakTypeTag[Z]): c.Expr[Codec[Z]] = {
    c.Expr[Codec[Z]] {
      q"""
         new $Codecs.JoinableCodec.T19($apl)($upl)(
           ${recall(weakTypeOf[A])},
           ${recall(weakTypeOf[B])},
           ${recall(weakTypeOf[C])},
           ${recall(weakTypeOf[D])},
           ${recall(weakTypeOf[E])},
           ${recall(weakTypeOf[F])},
           ${recall(weakTypeOf[G])},
           ${recall(weakTypeOf[H])},
           ${recall(weakTypeOf[I])},
           ${recall(weakTypeOf[J])},
           ${recall(weakTypeOf[K])},
           ${recall(weakTypeOf[L])},
           ${recall(weakTypeOf[M])},
           ${recall(weakTypeOf[N])},
           ${recall(weakTypeOf[O])},
           ${recall(weakTypeOf[P])},
           ${recall(weakTypeOf[Q])},
           ${recall(weakTypeOf[R])},
           ${recall(weakTypeOf[S])}
         ){
          override val keyLiteralRef: ${weakTypeOf[JsKeyLitOps]} = ${
        reify {
          n1.splice ++
            n2.splice ++
            n3.splice ++
            n4.splice ++
            n5.splice ++
            n6.splice ++
            n7.splice ++
            n8.splice ++
            n9.splice ++
            n10.splice ++
            n11.splice ++
            n12.splice ++
            n13.splice ++
            n14.splice ++
            n15.splice ++
            n16.splice ++
            n17.splice ++
            n18.splice ++
            n19.splice
        }
      }
        }
       """
    }
  }

  def fromConst20[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag, Q: c.WeakTypeTag, R: c.WeakTypeTag, S: c.WeakTypeTag, T: c.WeakTypeTag, Z]
  (n1: c.Expr[JsKeyLitOps], n2: c.Expr[JsKeyLitOps], n3: c.Expr[JsKeyLitOps], n4: c.Expr[JsKeyLitOps], n5: c.Expr[JsKeyLitOps], n6: c.Expr[JsKeyLitOps], n7: c.Expr[JsKeyLitOps], n8: c.Expr[JsKeyLitOps], n9: c.Expr[JsKeyLitOps], n10: c.Expr[JsKeyLitOps], n11: c.Expr[JsKeyLitOps], n12: c.Expr[JsKeyLitOps], n13: c.Expr[JsKeyLitOps], n14: c.Expr[JsKeyLitOps], n15: c.Expr[JsKeyLitOps], n16: c.Expr[JsKeyLitOps], n17: c.Expr[JsKeyLitOps], n18: c.Expr[JsKeyLitOps], n19: c.Expr[JsKeyLitOps], n20: c.Expr[JsKeyLitOps])
  (apl: c.Expr[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)]])(implicit zt: c.WeakTypeTag[Z]): c.Expr[Codec[Z]] = {
    c.Expr[Codec[Z]] {
      q"""
         new $Codecs.JoinableCodec.T20($apl)($upl)(
           ${recall(weakTypeOf[A])},
           ${recall(weakTypeOf[B])},
           ${recall(weakTypeOf[C])},
           ${recall(weakTypeOf[D])},
           ${recall(weakTypeOf[E])},
           ${recall(weakTypeOf[F])},
           ${recall(weakTypeOf[G])},
           ${recall(weakTypeOf[H])},
           ${recall(weakTypeOf[I])},
           ${recall(weakTypeOf[J])},
           ${recall(weakTypeOf[K])},
           ${recall(weakTypeOf[L])},
           ${recall(weakTypeOf[M])},
           ${recall(weakTypeOf[N])},
           ${recall(weakTypeOf[O])},
           ${recall(weakTypeOf[P])},
           ${recall(weakTypeOf[Q])},
           ${recall(weakTypeOf[R])},
           ${recall(weakTypeOf[S])},
           ${recall(weakTypeOf[T])}
         ){
          override val keyLiteralRef: ${weakTypeOf[JsKeyLitOps]} = ${
        reify {
          n1.splice ++
            n2.splice ++
            n3.splice ++
            n4.splice ++
            n5.splice ++
            n6.splice ++
            n7.splice ++
            n8.splice ++
            n9.splice ++
            n10.splice ++
            n11.splice ++
            n12.splice ++
            n13.splice ++
            n14.splice ++
            n15.splice ++
            n16.splice ++
            n17.splice ++
            n18.splice ++
            n19.splice ++
            n20.splice
        }
      }
        }
       """
    }
  }

  def fromConst21[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag, Q: c.WeakTypeTag, R: c.WeakTypeTag, S: c.WeakTypeTag, T: c.WeakTypeTag, _U: c.WeakTypeTag, Z]
  (n1: c.Expr[JsKeyLitOps], n2: c.Expr[JsKeyLitOps], n3: c.Expr[JsKeyLitOps], n4: c.Expr[JsKeyLitOps], n5: c.Expr[JsKeyLitOps], n6: c.Expr[JsKeyLitOps], n7: c.Expr[JsKeyLitOps], n8: c.Expr[JsKeyLitOps], n9: c.Expr[JsKeyLitOps], n10: c.Expr[JsKeyLitOps], n11: c.Expr[JsKeyLitOps], n12: c.Expr[JsKeyLitOps], n13: c.Expr[JsKeyLitOps], n14: c.Expr[JsKeyLitOps], n15: c.Expr[JsKeyLitOps], n16: c.Expr[JsKeyLitOps], n17: c.Expr[JsKeyLitOps], n18: c.Expr[JsKeyLitOps], n19: c.Expr[JsKeyLitOps], n20: c.Expr[JsKeyLitOps], n21: c.Expr[JsKeyLitOps])
  (apl: c.Expr[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, _U) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, _U)]])(implicit zt: c.WeakTypeTag[Z]): c.Expr[Codec[Z]] = {
    c.Expr[Codec[Z]] {
      q"""
         new $Codecs.JoinableCodec.T21($apl)($upl)(
           ${recall(weakTypeOf[A])},
           ${recall(weakTypeOf[B])},
           ${recall(weakTypeOf[C])},
           ${recall(weakTypeOf[D])},
           ${recall(weakTypeOf[E])},
           ${recall(weakTypeOf[F])},
           ${recall(weakTypeOf[G])},
           ${recall(weakTypeOf[H])},
           ${recall(weakTypeOf[I])},
           ${recall(weakTypeOf[J])},
           ${recall(weakTypeOf[K])},
           ${recall(weakTypeOf[L])},
           ${recall(weakTypeOf[M])},
           ${recall(weakTypeOf[N])},
           ${recall(weakTypeOf[O])},
           ${recall(weakTypeOf[P])},
           ${recall(weakTypeOf[Q])},
           ${recall(weakTypeOf[R])},
           ${recall(weakTypeOf[S])},
           ${recall(weakTypeOf[T])},
           ${recall(weakTypeOf[_U])}
         ){
          override val keyLiteralRef: ${weakTypeOf[JsKeyLitOps]} = ${
        reify {
          n1.splice ++
            n2.splice ++
            n3.splice ++
            n4.splice ++
            n5.splice ++
            n6.splice ++
            n7.splice ++
            n8.splice ++
            n9.splice ++
            n10.splice ++
            n11.splice ++
            n12.splice ++
            n13.splice ++
            n14.splice ++
            n15.splice ++
            n16.splice ++
            n17.splice ++
            n18.splice ++
            n19.splice ++
            n20.splice ++
            n21.splice
        }
      }
        }
       """
    }
  }

  def fromConst22[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag, Q: c.WeakTypeTag, R: c.WeakTypeTag, S: c.WeakTypeTag, T: c.WeakTypeTag, _U: c.WeakTypeTag, V: c.WeakTypeTag, Z]
  (n1: c.Expr[JsKeyLitOps], n2: c.Expr[JsKeyLitOps], n3: c.Expr[JsKeyLitOps], n4: c.Expr[JsKeyLitOps], n5: c.Expr[JsKeyLitOps], n6: c.Expr[JsKeyLitOps], n7: c.Expr[JsKeyLitOps], n8: c.Expr[JsKeyLitOps], n9: c.Expr[JsKeyLitOps], n10: c.Expr[JsKeyLitOps], n11: c.Expr[JsKeyLitOps], n12: c.Expr[JsKeyLitOps], n13: c.Expr[JsKeyLitOps], n14: c.Expr[JsKeyLitOps], n15: c.Expr[JsKeyLitOps], n16: c.Expr[JsKeyLitOps], n17: c.Expr[JsKeyLitOps], n18: c.Expr[JsKeyLitOps], n19: c.Expr[JsKeyLitOps], n20: c.Expr[JsKeyLitOps], n21: c.Expr[JsKeyLitOps], n22: c.Expr[JsKeyLitOps])
  (apl: c.Expr[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, _U, V) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, _U, V)]])(implicit zt: c.WeakTypeTag[Z]): c.Expr[Codec[Z]] = {
    c.Expr[Codec[Z]] {
      q"""
         new $Codecs.JoinableCodec.T22($apl)($upl)(
           ${recall(weakTypeOf[A])},
           ${recall(weakTypeOf[B])},
           ${recall(weakTypeOf[C])},
           ${recall(weakTypeOf[D])},
           ${recall(weakTypeOf[E])},
           ${recall(weakTypeOf[F])},
           ${recall(weakTypeOf[G])},
           ${recall(weakTypeOf[H])},
           ${recall(weakTypeOf[I])},
           ${recall(weakTypeOf[J])},
           ${recall(weakTypeOf[K])},
           ${recall(weakTypeOf[L])},
           ${recall(weakTypeOf[M])},
           ${recall(weakTypeOf[N])},
           ${recall(weakTypeOf[O])},
           ${recall(weakTypeOf[P])},
           ${recall(weakTypeOf[Q])},
           ${recall(weakTypeOf[R])},
           ${recall(weakTypeOf[S])},
           ${recall(weakTypeOf[T])},
           ${recall(weakTypeOf[_U])},
           ${recall(weakTypeOf[V])}
         ) {
          override val keyLiteralRef: ${weakTypeOf[JsKeyLitOps]} = ${
        reify {
          n1.splice ++
            n2.splice ++
            n3.splice ++
            n4.splice ++
            n5.splice ++
            n6.splice ++
            n7.splice ++
            n8.splice ++
            n9.splice ++
            n10.splice ++
            n11.splice ++
            n12.splice ++
            n13.splice ++
            n14.splice ++
            n15.splice ++
            n16.splice ++
            n17.splice ++
            n18.splice ++
            n19.splice ++
            n20.splice ++
            n21.splice ++
            n22.splice
        }
      }
        }
       """
    }
  }
}
