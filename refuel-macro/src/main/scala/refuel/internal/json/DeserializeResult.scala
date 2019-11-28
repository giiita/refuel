package refuel.internal.json

import refuel.internal.json.DeserializeResult.RT
import refuel.json.error.{DeserializeFailed, UnexpectedDeserializedCollectionSize}

trait DeserializeResult {
  def size: Int
  def and[NEW](that: RT[NEW]): DeserializeResult

  def asTuple2[A, B]: RT[(A, B)] = Left(UnexpectedDeserializedCollectionSize(s"Tried to convert $size collections into Tuple2"))
  def asTuple3[A, B, C]: RT[(A, B, C)] = Left(UnexpectedDeserializedCollectionSize(s"Tried to convert $size collections into Tuple3"))
  def asTuple4[A, B, C, D]: RT[(A, B, C, D)] = Left(UnexpectedDeserializedCollectionSize(s"Tried to convert $size collections into Tuple4"))
  def asTuple5[A, B, C, D, E]: RT[(A, B, C, D, E)] = Left(UnexpectedDeserializedCollectionSize(s"Tried to convert $size collections into Tuple5"))
  def asTuple6[A, B, C, D, E, F]: RT[(A, B, C, D, E, F)] = Left(UnexpectedDeserializedCollectionSize(s"Tried to convert $size collections into Tuple6"))
  def asTuple7[A, B, C, D, E, F, G]: RT[(A, B, C, D, E, F, G)] = Left(UnexpectedDeserializedCollectionSize(s"Tried to convert $size collections into Tuple7"))
  def asTuple8[A, B, C, D, E, F, G, H]: RT[(A, B, C, D, E, F, G, H)] = Left(UnexpectedDeserializedCollectionSize(s"Tried to convert $size collections into Tuple8"))
  def asTuple9[A, B, C, D, E, F, G, H, I]: RT[(A, B, C, D, E, F, G, H, I)] = Left(UnexpectedDeserializedCollectionSize(s"Tried to convert $size collections into Tuple9"))
  def asTuple10[A, B, C, D, E, F, G, H, I, J]: RT[(A, B, C, D, E, F, G, H, I, J)] = Left(UnexpectedDeserializedCollectionSize(s"Tried to convert $size collections into Tuple10"))
  def asTuple11[A, B, C, D, E, F, G, H, I, J, K]: RT[(A, B, C, D, E, F, G, H, I, J, K)] = Left(UnexpectedDeserializedCollectionSize(s"Tried to convert $size collections into Tuple11"))
  def asTuple12[A, B, C, D, E, F, G, H, I, J, K, L]: RT[(A, B, C, D, E, F, G, H, I, J, K, L)] = Left(UnexpectedDeserializedCollectionSize(s"Tried to convert $size collections into Tuple12"))
  def asTuple13[A, B, C, D, E, F, G, H, I, J, K, L, M]: RT[(A, B, C, D, E, F, G, H, I, J, K, L, M)] = Left(UnexpectedDeserializedCollectionSize(s"Tried to convert $size collections into Tuple13"))
  def asTuple14[A, B, C, D, E, F, G, H, I, J, K, L, M, N]: RT[(A, B, C, D, E, F, G, H, I, J, K, L, M, N)] = Left(UnexpectedDeserializedCollectionSize(s"Tried to convert $size collections into Tuple14"))
  def asTuple15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]: RT[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)] = Left(UnexpectedDeserializedCollectionSize(s"Tried to convert $size collections into Tuple15"))
  def asTuple16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]: RT[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)] = Left(UnexpectedDeserializedCollectionSize(s"Tried to convert $size collections into Tuple16"))
  def asTuple17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]: RT[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)] = Left(UnexpectedDeserializedCollectionSize(s"Tried to convert $size collections into Tuple17"))
  def asTuple18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]: RT[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)] = Left(UnexpectedDeserializedCollectionSize(s"Tried to convert $size collections into Tuple18"))
  def asTuple19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]: RT[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)] = Left(UnexpectedDeserializedCollectionSize(s"Tried to convert $size collections into Tuple19"))
  def asTuple20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]: RT[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)] = Left(UnexpectedDeserializedCollectionSize(s"Tried to convert $size collections into Tuple20"))
  def asTuple21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]: RT[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U)] = Left(UnexpectedDeserializedCollectionSize(s"Tried to convert $size collections into Tuple21"))
  def asTuple22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]: RT[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V)] = Left(UnexpectedDeserializedCollectionSize(s"Tried to convert $size collections into Tuple22"))
}

object DeserializeResult {
  
  private[json] type RT[T] = Either[DeserializeFailed, T]

  def apply[T](that: RT[T]): DeserializeResult = that match {
    case Left(e) => FailureConfirmation(e)
    case Right(r) => DeserializeResult1(r)
  }

  private[this] case class FailureConfirmation(fail: DeserializeFailed) extends DeserializeResult {
    override final val size = 0
    override def and[NEW](that: RT[NEW]): DeserializeResult = this

    override def asTuple2[A, B]: RT[(A, B)] = Left(fail)
    override def asTuple3[A, B, C]: RT[(A, B, C)] = Left(fail)
    override def asTuple4[A, B, C, D]: RT[(A, B, C, D)] = Left(fail)
    override def asTuple5[A, B, C, D, E]: RT[(A, B, C, D, E)] = Left(fail)
    override def asTuple6[A, B, C, D, E, F]: RT[(A, B, C, D, E, F)] = Left(fail)
    override def asTuple7[A, B, C, D, E, F, G]: RT[(A, B, C, D, E, F, G)] = Left(fail)
    override def asTuple8[A, B, C, D, E, F, G, H]: RT[(A, B, C, D, E, F, G, H)] = Left(fail)
    override def asTuple9[A, B, C, D, E, F, G, H, I]: RT[(A, B, C, D, E, F, G, H, I)] = Left(fail)
    override def asTuple10[A, B, C, D, E, F, G, H, I, J]: RT[(A, B, C, D, E, F, G, H, I, J)] = Left(fail)
    override def asTuple11[A, B, C, D, E, F, G, H, I, J, K]: RT[(A, B, C, D, E, F, G, H, I, J, K)] = Left(fail)
    override def asTuple12[A, B, C, D, E, F, G, H, I, J, K, L]: RT[(A, B, C, D, E, F, G, H, I, J, K, L)] = Left(fail)
    override def asTuple13[A, B, C, D, E, F, G, H, I, J, K, L, M]: RT[(A, B, C, D, E, F, G, H, I, J, K, L, M)] = Left(fail)
    override def asTuple14[A, B, C, D, E, F, G, H, I, J, K, L, M, N]: RT[(A, B, C, D, E, F, G, H, I, J, K, L, M, N)] = Left(fail)
    override def asTuple15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]: RT[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)] = Left(fail)
    override def asTuple16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]: RT[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)] = Left(fail)
    override def asTuple17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]: RT[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)] = Left(fail)
    override def asTuple18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]: RT[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)] = Left(fail)
    override def asTuple19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]: RT[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)] = Left(fail)
    override def asTuple20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]: RT[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)] = Left(fail)
    override def asTuple21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]: RT[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U)] = Left(fail)
    override def asTuple22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]: RT[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V)] = Left(fail)
  }


  private[this] case class DeserializeResult1[AR](a: AR) extends DeserializeResult {
    override final val size = 1
    override def and[NEW](that: RT[NEW]): DeserializeResult = that match {
      case Right(z) => DeserializeResult2(a, z)
      case Left(e) => FailureConfirmation(e)
    }
  }
  private[this] case class DeserializeResult2[AR, BR](a: AR, b: BR) extends DeserializeResult {
    override final val size = 2
    override def and[NEW](that: RT[NEW]): DeserializeResult = that match {
      case Right(z) => DeserializeResult3(a, b, z)
      case Left(e) => FailureConfirmation(e)
    }
    override def asTuple2[A, B]: RT[(A, B)] = Right((a, b).asInstanceOf[(A, B)])
  }
  private[this] case class DeserializeResult3[AR, BR, CR](a: AR, b: BR, c: CR) extends DeserializeResult {
    override final val size = 3
    override def and[NEW](that: RT[NEW]): DeserializeResult = that match {
      case Right(z) => DeserializeResult4(a, b, c, z)
      case Left(e) => FailureConfirmation(e)
    }
    override def asTuple3[A, B, C]: RT[(A, B, C)] = Right((a, b, c).asInstanceOf[(A, B, C)])
  }
  private[this] case class DeserializeResult4[AR, BR, CR, DR](a: AR, b: BR, c: CR, d: DR) extends DeserializeResult {
    override final val size = 4
    override def and[NEW](that: RT[NEW]): DeserializeResult = that match {
      case Right(z) => DeserializeResult5(a, b, c, d, z)
      case Left(e) => FailureConfirmation(e)
    }
    override def asTuple4[A, B, C, D]: RT[(A, B, C, D)] = Right((a, b, c, d).asInstanceOf[(A, B, C, D)])
  }
  private[this] case class DeserializeResult5[AR, BR, CR, DR, ER](a: AR, b: BR, c: CR, d: DR, e: ER) extends DeserializeResult {
    override final val size = 5
    override def and[NEW](that: RT[NEW]): DeserializeResult = that match {
      case Right(z) => DeserializeResult6(a, b, c, d, e, z)
      case Left(_e) => FailureConfirmation(_e)
    }
    override def asTuple5[A, B, C, D, E]: RT[(A, B, C, D, E)] = Right((a, b, c, d, e).asInstanceOf[(A, B, C, D, E)])
  }
  private[this] case class DeserializeResult6[AR, BR, CR, DR, ER, FR](a: AR, b: BR, c: CR, d: DR, e: ER, f: FR) extends DeserializeResult {
    override final val size = 6
    override def and[NEW](that: RT[NEW]): DeserializeResult = that match {
      case Right(z) => DeserializeResult7(a, b, c, d, e, f, z)
      case Left(_e) => FailureConfirmation(_e)
    }
    override def asTuple6[A, B, C, D, E, F]: RT[(A, B, C, D, E, F)] = Right((a, b, c, d, e, f).asInstanceOf[(A, B, C, D, E, F)])
  }
  private[this] case class DeserializeResult7[AR, BR, CR, DR, ER, FR, GR](a: AR, b: BR, c: CR, d: DR, e: ER, f: FR, g: GR) extends DeserializeResult {
    override final val size = 7
    override def and[NEW](that: RT[NEW]): DeserializeResult = that match {
      case Right(z) => DeserializeResult8(a, b, c, d, e, f, g, z)
      case Left(_e) => FailureConfirmation(_e)
    }
    override def asTuple7[A, B, C, D, E, F, G]: RT[(A, B, C, D, E, F, G)] = Right((a, b, c, d, e, f, g).asInstanceOf[(A, B, C, D, E, F, G)])
  }
  private[this] case class DeserializeResult8[AR, BR, CR, DR, ER, FR, GR, HR](a: AR, b: BR, c: CR, d: DR, e: ER, f: FR, g: GR, h: HR) extends DeserializeResult {
    override final val size = 8
    override def and[NEW](that: RT[NEW]): DeserializeResult = that match {
      case Right(z) => DeserializeResult9(a, b, c, d, e, f, g, h, z)
      case Left(_e) => FailureConfirmation(_e)
    }
    override def asTuple8[A, B, C, D, E, F, G, H]: RT[(A, B, C, D, E, F, G, H)] = Right((a, b, c, d, e, f, g, h).asInstanceOf[(A, B, C, D, E, F, G, H)])
  }
  private[this] case class DeserializeResult9[AR, BR, CR, DR, ER, FR, GR, HR, IR](a: AR, b: BR, c: CR, d: DR, e: ER, f: FR, g: GR, h: HR, i: HR) extends DeserializeResult {
    override final val size = 9
    override def and[NEW](that: RT[NEW]): DeserializeResult = that match {
      case Right(z) => DeserializeResult10(a, b, c, d, e, f, g, h, i, z)
      case Left(_e) => FailureConfirmation(_e)
    }
    override def asTuple9[A, B, C, D, E, F, G, H, I]: RT[(A, B, C, D, E, F, G, H, I)] = Right((a, b, c, d, e, f, g, h, i).asInstanceOf[(A, B, C, D, E, F, G, H, I)])
  }
  private[this] case class DeserializeResult10[AR, BR, CR, DR, ER, FR, GR, HR, IR, JR](a: AR, b: BR, c: CR, d: DR, e: ER, f: FR, g: GR, h: HR, i: HR, j: JR) extends DeserializeResult {
    override final val size = 10
    override def and[NEW](that: RT[NEW]): DeserializeResult = that match {
      case Right(z) => DeserializeResult11(a, b, c, d, e, f, g, h, i, j, z)
      case Left(_e) => FailureConfirmation(_e)
    }
    override def asTuple10[A, B, C, D, E, F, G, H, I, J]: RT[(A, B, C, D, E, F, G, H, I, J)] = Right((a, b, c, d, e, f, g, h, i, j).asInstanceOf[(A, B, C, D, E, F, G, H, I, J)])
  }
  private[this] case class DeserializeResult11[AR, BR, CR, DR, ER, FR, GR, HR, IR, JR, KR](a: AR, b: BR, c: CR, d: DR, e: ER, f: FR, g: GR, h: HR, i: HR, j: JR, k: KR) extends DeserializeResult {
    override final val size = 11
    override def and[NEW](that: RT[NEW]): DeserializeResult = that match {
      case Right(z) => DeserializeResult12(a, b, c, d, e, f, g, h, i, j, k, z)
      case Left(_e) => FailureConfirmation(_e)
    }
    override def asTuple11[A, B, C, D, E, F, G, H, I, J, K]: RT[(A, B, C, D, E, F, G, H, I, J, K)] = Right((a, b, c, d, e, f, g, h, i, j, k).asInstanceOf[(A, B, C, D, E, F, G, H, I, J, K)])
  }
  private[this] case class DeserializeResult12[AR, BR, CR, DR, ER, FR, GR, HR, IR, JR, KR, LR](a: AR, b: BR, c: CR, d: DR, e: ER, f: FR, g: GR, h: HR, i: HR, j: JR, k: KR, l: LR) extends DeserializeResult {
    override final val size = 12
    override def and[NEW](that: RT[NEW]): DeserializeResult = that match {
      case Right(z) => DeserializeResult13(a, b, c, d, e, f, g, h, i, j, k, l, z)
      case Left(_e) => FailureConfirmation(_e)
    }
    override def asTuple12[A, B, C, D, E, F, G, H, I, J, K, L]: RT[(A, B, C, D, E, F, G, H, I, J, K, L)] = Right((a, b, c, d, e, f, g, h, i, j, k, l).asInstanceOf[(A, B, C, D, E, F, G, H, I, J, K, L)])
  }
  private[this] case class DeserializeResult13[AR, BR, CR, DR, ER, FR, GR, HR, IR, JR, KR, LR, MR](a: AR, b: BR, c: CR, d: DR, e: ER, f: FR, g: GR, h: HR, i: HR, j: JR, k: KR, l: LR, m: MR) extends DeserializeResult {
    override final val size = 13
    override def and[NEW](that: RT[NEW]): DeserializeResult = that match {
      case Right(z) => DeserializeResult14(a, b, c, d, e, f, g, h, i, j, k, l, m, z)
      case Left(_e) => FailureConfirmation(_e)
    }
    override def asTuple13[A, B, C, D, E, F, G, H, I, J, K, L, M]: RT[(A, B, C, D, E, F, G, H, I, J, K, L, M)] = Right((a, b, c, d, e, f, g, h, i, j, k, l, m).asInstanceOf[(A, B, C, D, E, F, G, H, I, J, K, L, M)])
  }
  private[this] case class DeserializeResult14[AR, BR, CR, DR, ER, FR, GR, HR, IR, JR, KR, LR, MR, NR](a: AR, b: BR, c: CR, d: DR, e: ER, f: FR, g: GR, h: HR, i: HR, j: JR, k: KR, l: LR, m: MR, n: NR) extends DeserializeResult {
    override final val size = 14
    override def and[NEW](that: RT[NEW]): DeserializeResult = that match {
      case Right(z) => DeserializeResult15(a, b, c, d, e, f, g, h, i, j, k, l, m, n, z)
      case Left(_e) => FailureConfirmation(_e)
    }
    override def asTuple14[A, B, C, D, E, F, G, H, I, J, K, L, M, N]: RT[(A, B, C, D, E, F, G, H, I, J, K, L, M, N)] = Right((a, b, c, d, e, f, g, h, i, j, k, l, m, n).asInstanceOf[(A, B, C, D, E, F, G, H, I, J, K, L, M, N)])
  }
  private[this] case class DeserializeResult15[AR, BR, CR, DR, ER, FR, GR, HR, IR, JR, KR, LR, MR, NR, OR](a: AR, b: BR, c: CR, d: DR, e: ER, f: FR, g: GR, h: HR, i: HR, j: JR, k: KR, l: LR, m: MR, n: NR, o: OR) extends DeserializeResult {
    override final val size = 15
    override def and[NEW](that: RT[NEW]): DeserializeResult = that match {
      case Right(z) => DeserializeResult16(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, z)
      case Left(_e) => FailureConfirmation(_e)
    }
    override def asTuple15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]: RT[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)] = Right((a, b, c, d, e, f, g, h, i, j, k, l, m, n, o).asInstanceOf[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)])
  }
  private[this] case class DeserializeResult16[AR, BR, CR, DR, ER, FR, GR, HR, IR, JR, KR, LR, MR, NR, OR, PR](a: AR, b: BR, c: CR, d: DR, e: ER, f: FR, g: GR, h: HR, i: HR, j: JR, k: KR, l: LR, m: MR, n: NR, o: OR, p: PR) extends DeserializeResult {
    override final val size = 16
    override def and[NEW](that: RT[NEW]): DeserializeResult = that match {
      case Right(z) => DeserializeResult17(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, z)
      case Left(_e) => FailureConfirmation(_e)
    }
    override def asTuple16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]: RT[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)] = Right((a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p).asInstanceOf[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)])
  }
  private[this] case class DeserializeResult17[AR, BR, CR, DR, ER, FR, GR, HR, IR, JR, KR, LR, MR, NR, OR, PR, QR](a: AR, b: BR, c: CR, d: DR, e: ER, f: FR, g: GR, h: HR, i: HR, j: JR, k: KR, l: LR, m: MR, n: NR, o: OR, p: PR, q: QR) extends DeserializeResult {
    override final val size = 17
    override def and[NEW](that: RT[NEW]): DeserializeResult = that match {
      case Right(z) => DeserializeResult18(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, z)
      case Left(_e) => FailureConfirmation(_e)
    }
    override def asTuple17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]: RT[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)] = Right((a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q).asInstanceOf[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)])
  }
  private[this] case class DeserializeResult18[AR, BR, CR, DR, ER, FR, GR, HR, IR, JR, KR, LR, MR, NR, OR, PR, QR, RR](a: AR, b: BR, c: CR, d: DR, e: ER, f: FR, g: GR, h: HR, i: HR, j: JR, k: KR, l: LR, m: MR, n: NR, o: OR, p: PR, q: QR, r: RR) extends DeserializeResult {
    override final val size = 18
    override def and[NEW](that: RT[NEW]): DeserializeResult = that match {
      case Right(z) => DeserializeResult19(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, z)
      case Left(_e) => FailureConfirmation(_e)
    }
    override def asTuple18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]: RT[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)] = Right((a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r).asInstanceOf[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)])
  }
  private[this] case class DeserializeResult19[AR, BR, CR, DR, ER, FR, GR, HR, IR, JR, KR, LR, MR, NR, OR, PR, QR, RR, SR](a: AR, b: BR, c: CR, d: DR, e: ER, f: FR, g: GR, h: HR, i: HR, j: JR, k: KR, l: LR, m: MR, n: NR, o: OR, p: PR, q: QR, r: RR, s: SR) extends DeserializeResult {
    override final val size = 19
    override def and[NEW](that: RT[NEW]): DeserializeResult = that match {
      case Right(z) => DeserializeResult20(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, z)
      case Left(_e) => FailureConfirmation(_e)
    }
    override def asTuple19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]: RT[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)] = Right((a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s).asInstanceOf[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)])
  }
  private[this] case class DeserializeResult20[AR, BR, CR, DR, ER, FR, GR, HR, IR, JR, KR, LR, MR, NR, OR, PR, QR, RR, SR, TR](a: AR, b: BR, c: CR, d: DR, e: ER, f: FR, g: GR, h: HR, i: HR, j: JR, k: KR, l: LR, m: MR, n: NR, o: OR, p: PR, q: QR, r: RR, s: SR, t: TR) extends DeserializeResult {
    override final val size = 20
    override def and[NEW](that: RT[NEW]): DeserializeResult = that match {
      case Right(z) => DeserializeResult21(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, z)
      case Left(_e) => FailureConfirmation(_e)
    }
    override def asTuple20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]: RT[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)] = Right((a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t).asInstanceOf[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)])
  }
  private[this] case class DeserializeResult21[AR, BR, CR, DR, ER, FR, GR, HR, IR, JR, KR, LR, MR, NR, OR, PR, QR, RR, SR, TR, UR](a: AR, b: BR, c: CR, d: DR, e: ER, f: FR, g: GR, h: HR, i: HR, j: JR, k: KR, l: LR, m: MR, n: NR, o: OR, p: PR, q: QR, r: RR, s: SR, t: TR, u: UR) extends DeserializeResult {
    override final val size = 21
    override def and[NEW](that: RT[NEW]): DeserializeResult = that match {
      case Right(z) => DeserializeResult22(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, z)
      case Left(_e) => FailureConfirmation(_e)
    }
    override def asTuple21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]: RT[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U)] = Right((a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u).asInstanceOf[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U)])
  }
  private[this] case class DeserializeResult22[AR, BR, CR, DR, ER, FR, GR, HR, IR, JR, KR, LR, MR, NR, OR, PR, QR, RR, SR, TR, UR, VR](a: AR, b: BR, c: CR, d: DR, e: ER, f: FR, g: GR, h: HR, i: HR, j: JR, k: KR, l: LR, m: MR, n: NR, o: OR, p: PR, q: QR, r: RR, s: SR, t: TR, u: UR, v: VR) extends DeserializeResult {
    override final val size = 22
    override def and[NEW](that: RT[NEW]): DeserializeResult = FailureConfirmation(UnexpectedDeserializedCollectionSize("Tuple conversion can be 22 length limits."))
    override def asTuple22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]: RT[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V)] = Right((a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v).asInstanceOf[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V)])
  }
}
