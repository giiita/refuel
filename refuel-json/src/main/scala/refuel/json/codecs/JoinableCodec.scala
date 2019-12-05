package refuel.json.codecs

import refuel.internal.json.DeserializeResult
import refuel.json.entry.JsNull
import refuel.json.error.DeserializeFailed
import refuel.json.{Codec, Json}

object JoinableCodec {

  class T1[A, Z](describer: Json => Json)
                (scriber: Json => Json)
                (apl: A => Z)
                (upl: Z => Option[A])
                (implicit n1: Codec[A]) extends Codec[Z] {
    override def serialize(t: Z): Json = upl(t) match {
      case Some(a) => scriber.apply(n1.serialize(a))
      case None => JsNull
    }

    override def deserialize(bf: Json): Either[DeserializeFailed, Z] = {
      n1.deserialize(describer(bf)).right.map(apl)
    }
  }

  class T2[A, B, Z](describer: Json => (Json, Json))
                   (scriber: (Json, Json) => Json)
                   (apl: (A, B) => Z)
                   (upl: Z => Option[(A, B)])
                   (implicit n1: Codec[A], n2: Codec[B]) extends Codec[Z] {
    override def serialize(t: Z): Json = upl(t) match {
      case Some((a, b)) => scriber.apply(
        n1.serialize(a),
        n2.serialize(b)
      )
      case None => JsNull
    }

    override def deserialize(bf: Json): Either[DeserializeFailed, Z] = {
      describer(bf) match {
        case (a, b) => DeserializeResult(n1.deserialize(a))
          .and(n2.deserialize(b))
          .asTuple2
          .right.map(apl.tupled)
      }
    }
  }

  class T3[A, B, C, Z](describer: Json => (Json, Json, Json))
                      (scriber: (Json, Json, Json) => Json)
                      (apl: (A, B, C) => Z)
                      (upl: Z => Option[(A, B, C)])
                      (implicit n1: Codec[A], n2: Codec[B], n3: Codec[C]) extends Codec[Z] {
    override def serialize(t: Z): Json = upl(t) match {
      case Some((a, b, c)) => scriber.apply(
        n1.serialize(a),
        n2.serialize(b),
        n3.serialize(c)
      )
      case None => JsNull
    }

    override def deserialize(bf: Json): Either[DeserializeFailed, Z] = {
      describer(bf) match {
        case (a, b, c) => DeserializeResult(n1.deserialize(a))
          .and(n2.deserialize(b))
          .and(n3.deserialize(c))
          .asTuple3
          .right.map(apl.tupled)
      }
    }
  }

  class T4[A, B, C, D, Z](describer: Json => (Json, Json, Json, Json))
                         (scriber: (Json, Json, Json, Json) => Json)
                         (apl: (A, B, C, D) => Z)
                         (upl: Z => Option[(A, B, C, D)])
                         (implicit n1: Codec[A], n2: Codec[B], n3: Codec[C], n4: Codec[D]) extends Codec[Z] {
    override def serialize(t: Z): Json = upl(t) match {
      case Some((a, b, c, d)) => scriber.apply(
        n1.serialize(a),
        n2.serialize(b),
        n3.serialize(c),
        n4.serialize(d)
      )
      case None => JsNull
    }

    override def deserialize(bf: Json): Either[DeserializeFailed, Z] = {
      describer(bf) match {
        case (a, b, c, d) => DeserializeResult(n1.deserialize(a))
          .and(n2.deserialize(b))
          .and(n3.deserialize(c))
          .and(n4.deserialize(d))
          .asTuple4
          .right.map(apl.tupled)
      }
    }
  }


  //  deserializer(bf) match {
  //    case Left(e) => Left(DeserializeFailPropagation(s"Parents: $bf", e))
  //    case Right(x) =>  Right(apl.tupled.apply(x))
  //  }
}
