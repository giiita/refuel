package refuel.internal.json

import refuel.internal.PropertyDebugModeEnabler
import refuel.internal.json.codec.builder.JsonKeyRef
import refuel.json.Codec

import scala.reflect.macros.blackbox

class ConstructCodecFactory(override val c: blackbox.Context) extends CaseCodecFactory(c) with PropertyDebugModeEnabler {

  import c.universe._

  private[this] final val Codecs = q"refuel.json.codecs"

  def fromConst1[A: c.WeakTypeTag, Z](n1: c.Expr[JsonKeyRef])
                                     (apl: c.Expr[A => Z])
                                     (upl: c.Expr[Z => Option[A]]): c.Expr[Codec[Z]] = {
    c.Expr[Codec[Z]] {
      q"""
        new $Codecs.JoinableCodec.T1($n1)($apl)($upl)(${recall(weakTypeOf[A])})
       """
    }
  }

  def fromConst2[A: c.WeakTypeTag, B: c.WeakTypeTag, Z]
  (n1: c.Expr[JsonKeyRef], n2: c.Expr[JsonKeyRef])
  (apl: c.Expr[(A, B) => Z])
  (upl: c.Expr[Z => Option[(A, B)]]): c.Expr[Codec[Z]] = {
    c.Expr[Codec[Z]] {
      q"""
         new $Codecs.JoinableCodec.T2($n1, $n2)($apl)($upl)(
           ${recall(weakTypeOf[A])}, ${recall(weakTypeOf[B])}
         )
       """
    }
  }

  def fromConst3[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, Z]
  (n1: c.Expr[JsonKeyRef], n2: c.Expr[JsonKeyRef], n3: c.Expr[JsonKeyRef])
  (apl: c.Expr[(A, B, C) => Z])
  (upl: c.Expr[Z => Option[(A, B, C)]]): c.Expr[Codec[Z]] = {
    c.Expr[Codec[Z]] {
      q"""
         new $Codecs.JoinableCodec.T3($n1, $n2, $n3)($apl)($upl)(
           ${recall(weakTypeOf[A])}, ${recall(weakTypeOf[B])}, ${recall(weakTypeOf[C])}
         )
       """
    }
  }

  def fromConst4[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, Z]
  (n1: c.Expr[JsonKeyRef], n2: c.Expr[JsonKeyRef], n3: c.Expr[JsonKeyRef], n4: c.Expr[JsonKeyRef])
  (apl: c.Expr[(A, B, C, D) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D)]]): c.Expr[Codec[Z]] = {
    c.Expr[Codec[Z]] {
      q"""
         new $Codecs.JoinableCodec.T4($n1, $n2, $n3, $n4)($apl)($upl)(
           ${recall(weakTypeOf[A])}, ${recall(weakTypeOf[B])}, ${recall(weakTypeOf[C])}, ${recall(weakTypeOf[D])}
         )
       """
    }
  }

  def fromConst5[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, Z]
  (n1: c.Expr[JsonKeyRef], n2: c.Expr[JsonKeyRef], n3: c.Expr[JsonKeyRef], n4: c.Expr[JsonKeyRef], n5: c.Expr[JsonKeyRef])
  (apl: c.Expr[(A, B, C, D, E) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D, E)]]): c.Expr[Codec[Z]] = {
    c.Expr[Codec[Z]] {
      q"""
         new $Codecs.JoinableCodec.T5($n1, $n2, $n3, $n4, $n5)($apl)($upl)(
           ${recall(weakTypeOf[A])}, ${recall(weakTypeOf[B])}, ${recall(weakTypeOf[C])}, ${recall(weakTypeOf[D])}, ${recall(weakTypeOf[E])}
         )
       """
    }
  }

  def fromConst6[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, Z]
  (n1: c.Expr[JsonKeyRef], n2: c.Expr[JsonKeyRef], n3: c.Expr[JsonKeyRef], n4: c.Expr[JsonKeyRef], n5: c.Expr[JsonKeyRef], n6: c.Expr[JsonKeyRef])
  (apl: c.Expr[(A, B, C, D, E, F) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D, E, F)]]): c.Expr[Codec[Z]] = {
    c.Expr[Codec[Z]] {
      q"""
         new $Codecs.JoinableCodec.T6($n1, $n2, $n3, $n4, $n5, $n6)($apl)($upl)(
           ${recall(weakTypeOf[A])},
           ${recall(weakTypeOf[B])},
           ${recall(weakTypeOf[C])},
           ${recall(weakTypeOf[D])},
           ${recall(weakTypeOf[E])},
           ${recall(weakTypeOf[F])}
         )
       """
    }
  }

  def fromConst7[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, Z]
  (n1: c.Expr[JsonKeyRef], n2: c.Expr[JsonKeyRef], n3: c.Expr[JsonKeyRef], n4: c.Expr[JsonKeyRef], n5: c.Expr[JsonKeyRef], n6: c.Expr[JsonKeyRef], n7: c.Expr[JsonKeyRef])
  (apl: c.Expr[(A, B, C, D, E, F, G) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D, E, F, G)]])(implicit zt: c.WeakTypeTag[Z]): c.Expr[Codec[Z]] = {
    c.Expr[Codec[Z]] {
      q"""
         new $Codecs.JoinableCodec.T7($n1, $n2, $n3, $n4, $n5, $n6, $n7)($apl)($upl)(
           ${recall(weakTypeOf[A])},
           ${recall(weakTypeOf[B])},
           ${recall(weakTypeOf[C])},
           ${recall(weakTypeOf[D])},
           ${recall(weakTypeOf[E])},
           ${recall(weakTypeOf[F])},
           ${recall(weakTypeOf[G])}
         )
       """
    }
  }

  def fromConst8[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, Z]
  (n1: c.Expr[JsonKeyRef], n2: c.Expr[JsonKeyRef], n3: c.Expr[JsonKeyRef], n4: c.Expr[JsonKeyRef], n5: c.Expr[JsonKeyRef], n6: c.Expr[JsonKeyRef], n7: c.Expr[JsonKeyRef], n8: c.Expr[JsonKeyRef])
  (apl: c.Expr[(A, B, C, D, E, F, G, H) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D, E, F, G, H)]])(implicit zt: c.WeakTypeTag[Z]): c.Expr[Codec[Z]] = {
    c.Expr[Codec[Z]] {
      q"""
         new $Codecs.JoinableCodec.T8($n1, $n2, $n3, $n4, $n5, $n6, $n7, $n8)($apl)($upl)(
           ${recall(weakTypeOf[A])},
           ${recall(weakTypeOf[B])},
           ${recall(weakTypeOf[C])},
           ${recall(weakTypeOf[D])},
           ${recall(weakTypeOf[E])},
           ${recall(weakTypeOf[F])},
           ${recall(weakTypeOf[G])},
           ${recall(weakTypeOf[H])}
         )
       """
    }
  }

  def fromConst9[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, Z]
  (n1: c.Expr[JsonKeyRef], n2: c.Expr[JsonKeyRef], n3: c.Expr[JsonKeyRef], n4: c.Expr[JsonKeyRef], n5: c.Expr[JsonKeyRef], n6: c.Expr[JsonKeyRef], n7: c.Expr[JsonKeyRef], n8: c.Expr[JsonKeyRef], n9: c.Expr[JsonKeyRef])
  (apl: c.Expr[(A, B, C, D, E, F, G, H, I) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D, E, F, G, H, I)]])(implicit zt: c.WeakTypeTag[Z]): c.Expr[Codec[Z]] = {
    c.Expr[Codec[Z]] {
      q"""
         new $Codecs.JoinableCodec.T9($n1, $n2, $n3, $n4, $n5, $n6, $n7, $n8, $n9)($apl)($upl)(
           ${recall(weakTypeOf[A])},
           ${recall(weakTypeOf[B])},
           ${recall(weakTypeOf[C])},
           ${recall(weakTypeOf[D])},
           ${recall(weakTypeOf[E])},
           ${recall(weakTypeOf[F])},
           ${recall(weakTypeOf[G])},
           ${recall(weakTypeOf[H])},
           ${recall(weakTypeOf[I])}
         )
       """
    }
  }

  def fromConst10[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, Z]
  (n1: c.Expr[JsonKeyRef], n2: c.Expr[JsonKeyRef], n3: c.Expr[JsonKeyRef], n4: c.Expr[JsonKeyRef], n5: c.Expr[JsonKeyRef], n6: c.Expr[JsonKeyRef], n7: c.Expr[JsonKeyRef], n8: c.Expr[JsonKeyRef], n9: c.Expr[JsonKeyRef], n10: c.Expr[JsonKeyRef])
  (apl: c.Expr[(A, B, C, D, E, F, G, H, I, J) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D, E, F, G, H, I, J)]])(implicit zt: c.WeakTypeTag[Z]): c.Expr[Codec[Z]] = {
    c.Expr[Codec[Z]] {
      q"""
         new $Codecs.JoinableCodec.T10($n1, $n2, $n3, $n4, $n5, $n6, $n7, $n8, $n9, $n10)($apl)($upl)(
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
         )
       """
    }
  }

  def fromConst11[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, Z]
  (n1: c.Expr[JsonKeyRef], n2: c.Expr[JsonKeyRef], n3: c.Expr[JsonKeyRef], n4: c.Expr[JsonKeyRef], n5: c.Expr[JsonKeyRef], n6: c.Expr[JsonKeyRef], n7: c.Expr[JsonKeyRef], n8: c.Expr[JsonKeyRef], n9: c.Expr[JsonKeyRef], n10: c.Expr[JsonKeyRef], n11: c.Expr[JsonKeyRef])
  (apl: c.Expr[(A, B, C, D, E, F, G, H, I, J, K) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D, E, F, G, H, I, J, K)]]): c.Expr[Codec[Z]] = {
    c.Expr[Codec[Z]] {
      q"""
         new $Codecs.JoinableCodec.T11($n1, $n2, $n3, $n4, $n5, $n6, $n7, $n8, $n9, $n10, $n11)($apl)($upl)(
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
         )
       """
    }
  }

  def fromConst12[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, Z]
  (n1: c.Expr[JsonKeyRef], n2: c.Expr[JsonKeyRef], n3: c.Expr[JsonKeyRef], n4: c.Expr[JsonKeyRef], n5: c.Expr[JsonKeyRef], n6: c.Expr[JsonKeyRef], n7: c.Expr[JsonKeyRef], n8: c.Expr[JsonKeyRef], n9: c.Expr[JsonKeyRef], n10: c.Expr[JsonKeyRef], n11: c.Expr[JsonKeyRef], n12: c.Expr[JsonKeyRef])
  (apl: c.Expr[(A, B, C, D, E, F, G, H, I, J, K, L) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L)]])(implicit zt: c.WeakTypeTag[Z]): c.Expr[Codec[Z]] = {
    c.Expr[Codec[Z]] {
      q"""
         new $Codecs.JoinableCodec.T12($n1, $n2, $n3, $n4, $n5, $n6, $n7, $n8, $n9, $n10, $n11, $n12)($apl)($upl)(
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
         )
       """
    }
  }

  def fromConst13[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, Z]
  (n1: c.Expr[JsonKeyRef], n2: c.Expr[JsonKeyRef], n3: c.Expr[JsonKeyRef], n4: c.Expr[JsonKeyRef], n5: c.Expr[JsonKeyRef], n6: c.Expr[JsonKeyRef], n7: c.Expr[JsonKeyRef], n8: c.Expr[JsonKeyRef], n9: c.Expr[JsonKeyRef], n10: c.Expr[JsonKeyRef], n11: c.Expr[JsonKeyRef], n12: c.Expr[JsonKeyRef], n13: c.Expr[JsonKeyRef])
  (apl: c.Expr[(A, B, C, D, E, F, G, H, I, J, K, L, M) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M)]])(implicit zt: c.WeakTypeTag[Z]): c.Expr[Codec[Z]] = {
    c.Expr[Codec[Z]] {
      q"""
         new $Codecs.JoinableCodec.T13($n1, $n2, $n3, $n4, $n5, $n6, $n7, $n8, $n9, $n10, $n11, $n12, $n13)($apl)($upl)(
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
         )
       """
    }
  }

  def fromConst14[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, Z]
  (n1: c.Expr[JsonKeyRef], n2: c.Expr[JsonKeyRef], n3: c.Expr[JsonKeyRef], n4: c.Expr[JsonKeyRef], n5: c.Expr[JsonKeyRef], n6: c.Expr[JsonKeyRef], n7: c.Expr[JsonKeyRef], n8: c.Expr[JsonKeyRef], n9: c.Expr[JsonKeyRef], n10: c.Expr[JsonKeyRef], n11: c.Expr[JsonKeyRef], n12: c.Expr[JsonKeyRef], n13: c.Expr[JsonKeyRef], n14: c.Expr[JsonKeyRef])
  (apl: c.Expr[(A, B, C, D, E, F, G, H, I, J, K, L, M, N) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N)]])(implicit zt: c.WeakTypeTag[Z]): c.Expr[Codec[Z]] = {
    c.Expr[Codec[Z]] {
      q"""
         new $Codecs.JoinableCodec.T14($n1, $n2, $n3, $n4, $n5, $n6, $n7, $n8, $n9, $n10, $n11, $n12, $n13, $n14)($apl)($upl)(
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
         )
       """
    }
  }

  def fromConst15[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, Z]
  (n1: c.Expr[JsonKeyRef], n2: c.Expr[JsonKeyRef], n3: c.Expr[JsonKeyRef], n4: c.Expr[JsonKeyRef], n5: c.Expr[JsonKeyRef], n6: c.Expr[JsonKeyRef], n7: c.Expr[JsonKeyRef], n8: c.Expr[JsonKeyRef], n9: c.Expr[JsonKeyRef], n10: c.Expr[JsonKeyRef], n11: c.Expr[JsonKeyRef], n12: c.Expr[JsonKeyRef], n13: c.Expr[JsonKeyRef], n14: c.Expr[JsonKeyRef], n15: c.Expr[JsonKeyRef])
  (apl: c.Expr[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)]])(implicit zt: c.WeakTypeTag[Z]): c.Expr[Codec[Z]] = {
    c.Expr[Codec[Z]] {
      q"""
         new $Codecs.JoinableCodec.T15($n1, $n2, $n3, $n4, $n5, $n6, $n7, $n8, $n9, $n10, $n11, $n12, $n13, $n14, $n15)($apl)($upl)(
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
         )
       """
    }
  }

  def fromConst16[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag, Z]
  (n1: c.Expr[JsonKeyRef], n2: c.Expr[JsonKeyRef], n3: c.Expr[JsonKeyRef], n4: c.Expr[JsonKeyRef], n5: c.Expr[JsonKeyRef], n6: c.Expr[JsonKeyRef], n7: c.Expr[JsonKeyRef], n8: c.Expr[JsonKeyRef], n9: c.Expr[JsonKeyRef], n10: c.Expr[JsonKeyRef], n11: c.Expr[JsonKeyRef], n12: c.Expr[JsonKeyRef], n13: c.Expr[JsonKeyRef], n14: c.Expr[JsonKeyRef], n15: c.Expr[JsonKeyRef], n16: c.Expr[JsonKeyRef])
  (apl: c.Expr[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)]])(implicit zt: c.WeakTypeTag[Z]): c.Expr[Codec[Z]] = {
    c.Expr[Codec[Z]] {
      q"""
         new $Codecs.JoinableCodec.T16($n1, $n2, $n3, $n4, $n5, $n6, $n7, $n8, $n9, $n10, $n11, $n12, $n13, $n14, $n15, $n16)($apl)($upl)(
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
         )
       """
    }
  }

  def fromConst17[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag, Q: c.WeakTypeTag, Z]
  (n1: c.Expr[JsonKeyRef], n2: c.Expr[JsonKeyRef], n3: c.Expr[JsonKeyRef], n4: c.Expr[JsonKeyRef], n5: c.Expr[JsonKeyRef], n6: c.Expr[JsonKeyRef], n7: c.Expr[JsonKeyRef], n8: c.Expr[JsonKeyRef], n9: c.Expr[JsonKeyRef], n10: c.Expr[JsonKeyRef], n11: c.Expr[JsonKeyRef], n12: c.Expr[JsonKeyRef], n13: c.Expr[JsonKeyRef], n14: c.Expr[JsonKeyRef], n15: c.Expr[JsonKeyRef], n16: c.Expr[JsonKeyRef], n17: c.Expr[JsonKeyRef])
  (apl: c.Expr[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)]])(implicit zt: c.WeakTypeTag[Z]): c.Expr[Codec[Z]] = {
    c.Expr[Codec[Z]] {
      q"""
         new $Codecs.JoinableCodec.T17($n1, $n2, $n3, $n4, $n5, $n6, $n7, $n8, $n9, $n10, $n11, $n12, $n13, $n14, $n15, $n16, $n17)($apl)($upl)(
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
         )
       """
    }
  }

  def fromConst18[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag, Q: c.WeakTypeTag, R: c.WeakTypeTag, Z]
  (n1: c.Expr[JsonKeyRef], n2: c.Expr[JsonKeyRef], n3: c.Expr[JsonKeyRef], n4: c.Expr[JsonKeyRef], n5: c.Expr[JsonKeyRef], n6: c.Expr[JsonKeyRef], n7: c.Expr[JsonKeyRef], n8: c.Expr[JsonKeyRef], n9: c.Expr[JsonKeyRef], n10: c.Expr[JsonKeyRef], n11: c.Expr[JsonKeyRef], n12: c.Expr[JsonKeyRef], n13: c.Expr[JsonKeyRef], n14: c.Expr[JsonKeyRef], n15: c.Expr[JsonKeyRef], n16: c.Expr[JsonKeyRef], n17: c.Expr[JsonKeyRef], n18: c.Expr[JsonKeyRef])
  (apl: c.Expr[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)]])(implicit zt: c.WeakTypeTag[Z]): c.Expr[Codec[Z]] = {
    c.Expr[Codec[Z]] {
      q"""
         new $Codecs.JoinableCodec.T18($n1, $n2, $n3, $n4, $n5, $n6, $n7, $n8, $n9, $n10, $n11, $n12, $n13, $n14, $n15, $n16, $n17, $n18)($apl)($upl)(
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
         )
       """
    }
  }

  def fromConst19[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag, Q: c.WeakTypeTag, R: c.WeakTypeTag, S: c.WeakTypeTag, Z]
  (n1: c.Expr[JsonKeyRef], n2: c.Expr[JsonKeyRef], n3: c.Expr[JsonKeyRef], n4: c.Expr[JsonKeyRef], n5: c.Expr[JsonKeyRef], n6: c.Expr[JsonKeyRef], n7: c.Expr[JsonKeyRef], n8: c.Expr[JsonKeyRef], n9: c.Expr[JsonKeyRef], n10: c.Expr[JsonKeyRef], n11: c.Expr[JsonKeyRef], n12: c.Expr[JsonKeyRef], n13: c.Expr[JsonKeyRef], n14: c.Expr[JsonKeyRef], n15: c.Expr[JsonKeyRef], n16: c.Expr[JsonKeyRef], n17: c.Expr[JsonKeyRef], n18: c.Expr[JsonKeyRef], n19: c.Expr[JsonKeyRef])
  (apl: c.Expr[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)]])(implicit zt: c.WeakTypeTag[Z]): c.Expr[Codec[Z]] = {
    c.Expr[Codec[Z]] {
      q"""
         new $Codecs.JoinableCodec.T19($n1, $n2, $n3, $n4, $n5, $n6, $n7, $n8, $n9, $n10, $n11, $n12, $n13, $n14, $n15, $n16, $n17, $n18, $n19)($apl)($upl)(
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
         )
       """
    }
  }

  def fromConst20[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag, Q: c.WeakTypeTag, R: c.WeakTypeTag, S: c.WeakTypeTag, T: c.WeakTypeTag, Z]
  (n1: c.Expr[JsonKeyRef], n2: c.Expr[JsonKeyRef], n3: c.Expr[JsonKeyRef], n4: c.Expr[JsonKeyRef], n5: c.Expr[JsonKeyRef], n6: c.Expr[JsonKeyRef], n7: c.Expr[JsonKeyRef], n8: c.Expr[JsonKeyRef], n9: c.Expr[JsonKeyRef], n10: c.Expr[JsonKeyRef], n11: c.Expr[JsonKeyRef], n12: c.Expr[JsonKeyRef], n13: c.Expr[JsonKeyRef], n14: c.Expr[JsonKeyRef], n15: c.Expr[JsonKeyRef], n16: c.Expr[JsonKeyRef], n17: c.Expr[JsonKeyRef], n18: c.Expr[JsonKeyRef], n19: c.Expr[JsonKeyRef], n20: c.Expr[JsonKeyRef])
  (apl: c.Expr[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)]])(implicit zt: c.WeakTypeTag[Z]): c.Expr[Codec[Z]] = {
    c.Expr[Codec[Z]] {
      q"""
         new $Codecs.JoinableCodec.T20($n1, $n2, $n3, $n4, $n5, $n6, $n7, $n8, $n9, $n10, $n11, $n12, $n13, $n14, $n15, $n16, $n17, $n18, $n19, $n20)($apl)($upl)(
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
         )
       """
    }
  }

  def fromConst21[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag, Q: c.WeakTypeTag, R: c.WeakTypeTag, S: c.WeakTypeTag, T: c.WeakTypeTag, _U: c.WeakTypeTag, Z]
  (n1: c.Expr[JsonKeyRef], n2: c.Expr[JsonKeyRef], n3: c.Expr[JsonKeyRef], n4: c.Expr[JsonKeyRef], n5: c.Expr[JsonKeyRef], n6: c.Expr[JsonKeyRef], n7: c.Expr[JsonKeyRef], n8: c.Expr[JsonKeyRef], n9: c.Expr[JsonKeyRef], n10: c.Expr[JsonKeyRef], n11: c.Expr[JsonKeyRef], n12: c.Expr[JsonKeyRef], n13: c.Expr[JsonKeyRef], n14: c.Expr[JsonKeyRef], n15: c.Expr[JsonKeyRef], n16: c.Expr[JsonKeyRef], n17: c.Expr[JsonKeyRef], n18: c.Expr[JsonKeyRef], n19: c.Expr[JsonKeyRef], n20: c.Expr[JsonKeyRef], n21: c.Expr[JsonKeyRef])
  (apl: c.Expr[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, _U) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, _U)]])(implicit zt: c.WeakTypeTag[Z]): c.Expr[Codec[Z]] = {
    c.Expr[Codec[Z]] {
      q"""
         new $Codecs.JoinableCodec.T21($n1, $n2, $n3, $n4, $n5, $n6, $n7, $n8, $n9, $n10, $n11, $n12, $n13, $n14, $n15, $n16, $n17, $n18, $n19, $n20, $n21)($apl)($upl)(
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
         )
       """
    }
  }

  def fromConst22[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag, Q: c.WeakTypeTag, R: c.WeakTypeTag, S: c.WeakTypeTag, T: c.WeakTypeTag, _U: c.WeakTypeTag, V: c.WeakTypeTag, Z]
  (n1: c.Expr[JsonKeyRef], n2: c.Expr[JsonKeyRef], n3: c.Expr[JsonKeyRef], n4: c.Expr[JsonKeyRef], n5: c.Expr[JsonKeyRef], n6: c.Expr[JsonKeyRef], n7: c.Expr[JsonKeyRef], n8: c.Expr[JsonKeyRef], n9: c.Expr[JsonKeyRef], n10: c.Expr[JsonKeyRef], n11: c.Expr[JsonKeyRef], n12: c.Expr[JsonKeyRef], n13: c.Expr[JsonKeyRef], n14: c.Expr[JsonKeyRef], n15: c.Expr[JsonKeyRef], n16: c.Expr[JsonKeyRef], n17: c.Expr[JsonKeyRef], n18: c.Expr[JsonKeyRef], n19: c.Expr[JsonKeyRef], n20: c.Expr[JsonKeyRef], n21: c.Expr[JsonKeyRef], n22: c.Expr[JsonKeyRef])
  (apl: c.Expr[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, _U, V) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, _U, V)]])(implicit zt: c.WeakTypeTag[Z]): c.Expr[Codec[Z]] = {
    c.Expr[Codec[Z]] {
      q"""
         new $Codecs.JoinableCodec.T22($n1, $n2, $n3, $n4, $n5, $n6, $n7, $n8, $n9, $n10, $n11, $n12, $n13, $n14, $n15, $n16, $n17, $n18, $n19, $n20, $n21, $n22)($apl)($upl)(
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
         )
       """
    }
  }
}
