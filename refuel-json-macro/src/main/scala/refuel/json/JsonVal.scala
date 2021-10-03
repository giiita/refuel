package refuel.json

import refuel.json.exception.{IllegalJsonFormatException, UnexpectedJsonTreeException}

import java.util
import scala.annotation.{switch, tailrec}
import scala.jdk.CollectionConverters.{MapHasAsJava, MapHasAsScala}
import scala.util.Try

trait JsonVal extends Serializable {

  /** Determines that the target JSON syntax tree does not exist or is a null symbol.
    * Empty arrays and empty objects are not considered to be empty.
    *
    * @return
    */
  def isEmpty: Boolean = false

  def isNonEmpty: Boolean = true

  /**
    * Get a json value with a specific json key from the json object.
    * JsNull may change if that key doesn't exist.
    *
    * @param key Target json key name
    * @return
    */
  def named(key: JsonKeySpec): JsonVal = key.dig(this)
  def named(key: String): JsonVal

  /**
    * Synthesize Json object.
    * If the json object is json primitive type, an exception may occur.
    *
    * @param js join json objects.
    * @return
    */
  private[refuel] def joinUnsafe(js: JsonVal): JsonVal

  /**
    * Detects hooked binding syntax of Json literal.
    * This detects syntax errors when joining Json objects.
    * The argument receives a delimiter that is not directly related to the construction of Json.
    *
    * @param c delimiter
    */
  private[refuel] def approvalSyntax(c: Char): Unit

  /**
    * Convert to json literal.
    *
    * @return
    */
  private[refuel] def encode(b: StringBuilder): Unit

  /**
    * Squash the Json buffer under construction.
    * This will build a complete Json object.
    *
    * @return
    */
  private[refuel] def squash: JsonVal = this

  /**
    * Indicates whether the object is squashable.
    * If squashable, the object is a Json buffer under construction.
    *
    * @return
    */
  private[refuel] def isSquashable: Boolean = false

  private[refuel] def pure: String = toString

  override def toString: String = {
    val buf = new StringBuilder()
    encode(buf)
    buf.toString
  }
}

object JsonVal {

  implicit class JsonValExtension (json: JsonVal) {
    def ++(jsonVal: JsonVal): Try[JsonVal] = Try(json joinUnsafe jsonVal)
  }

  sealed trait JsVariable extends JsonVal {
    override def approvalSyntax(c: Char): Unit = ()
    override def named(key: String): JsonVal   = JsNull
  }

  sealed trait JsStack extends JsonVal {

    protected var pos = 0

    def encode(b: StringBuilder): Unit =
      throw UnexpectedJsonTreeException(s"Cannot to be String. JsStack is an unclosed json stream.")

    override def named(key: String): JsonVal =
      throw UnexpectedJsonTreeException(s"Cannot specified name. JsStack is an unclosed json stream.")

    def joinUnsafe(js: JsonVal): JsonVal

    override def isSquashable: Boolean = true
  }

  case class JsAny private (literal: String) extends JsVariable {
    override def pure: String = literal

    override def encode(sb: StringBuilder): Unit = sb.append(literal.trim)

    override def joinUnsafe(js: JsonVal): JsonVal =
      throw UnexpectedJsonTreeException(s"Cannot add element $js to JsAny($literal).")
  }

  case class JsArray private (bf: Seq[JsonVal]) extends JsVariable {
    def n(i: Int): JsonVal = bf(i)
    override def encode(sb: StringBuilder): Unit = {
      var unempty = false
      sb.append('[')
      bf.foreach { x =>
        if (unempty) sb.append(',')
        x.encode(sb)
        if (!unempty) unempty = true
      }
      sb.append(']')
    }

    override def named(key: String): JsonVal = {
      bf.map(_.named(key)).foldLeft(JsArray(None))(_ joinUnsafe _)
    }

    def joinUnsafe(js: JsonVal): JsArray = {
      if (js == null) this
      else {
        js match {
          case JsArray(x) => JsArray(bf ++ x)
          case JsNull     => this
          case _          => JsArray(bf :+ js)
        }
      }
    }
  }

  /**
    * JSON Object being constructed.
    *
    * @param key JSON key
    * @param value JSON value
    */
  case class JsEntry(key: JsString, value: JsonVal) extends JsonVal {

    override def toString: String = {
      val b = new StringBuilder()
        b.append('{')
      key.encode(b)
          b.append(':')
      value.encode(b)
        b.append('}')
      b.result()
    }

    /**
      * Detects hooked binding syntax of Json literal.
      * This detects syntax errors when joining Json objects.
      * The argument receives a delimiter that is not directly related to the construction of Json.
      *
      * @param c delimiter
      */
    override def approvalSyntax(c: Char): Unit =
      throw UnexpectedJsonTreeException(
        "Operations other than composition are not allowed for incomplete Json entries."
      )

    /**
      * Convert to json literal.
      *
      * @return
      */
    override def encode(b: StringBuilder): Unit =
      throw UnexpectedJsonTreeException(
        "Operations other than composition are not allowed for incomplete Json entries."
      )

    /**
      * Synthesize Json object.
      * If the json object is json primitive type, an exception may occur.
      *
      * @param js join json objects.
      * @return
      */
    override def joinUnsafe(js: JsonVal): JsonVal =
      throw UnexpectedJsonTreeException(
        "Operations other than composition are not allowed for incomplete Json entries."
      )

    /**
      * Get a json value with a specific json key from the json object.
      * JsNull may change if that key doesn't exist.
      *
      * @param _key Target json key name
      * @return
      */
    override def named(_key: String): JsonVal = {
      if (key.pure == _key) {
        value
      } else JsNull
    }

    private[refuel] def asTuple: (JsString, JsonVal) = key -> value
  }

  case class JsObject private[refuel] (bf: util.LinkedHashMap[JsString, JsonVal]) extends JsVariable {

    def encode(b: StringBuilder): Unit = {
      var unempty = false
      b.append('{')
      bf.entrySet().forEach { x =>
        if (unempty) b.append(",")
        x.getKey.encode(b)
        b.append(':')
        x.getValue.encode(b)
        if (!unempty) unempty = true
      }
      b.append('}')
    }

    def joinUnsafe(js: JsonVal): JsonVal = {
      js match {
        case JsNull | null => this
        case x: JsObject =>
          val cloned = new util.LinkedHashMap[JsString, JsonVal]()
          val keys = bf.keySet()
          keys.addAll(x.bf.keySet())
          keys.iterator().forEachRemaining { key =>
            bf.put(key, bf.getOrDefault(key, JsEmpty) joinUnsafe x.bf.getOrDefault(key, JsEmpty))
          }
          new JsObject(cloned)
        case JsEmpty => this
        case x       => throw UnexpectedJsonTreeException(s"Cannot add raw variable element to JsObject. $toString + $x")
      }
    }

    override def named(key: String): JsonVal = {
      bf.getOrDefault(JsString(key), JsNull)
    }

    def unapply(arg: JsObject): Option[Map[JsString, JsonVal]] = Some(bf.asScala.toMap)
  }

  case class JsString private (literal: String) extends JsVariable {

    override def pure: String = literal

    def encode(b: StringBuilder): Unit = {
      b.append('"')

      val HC = JsString.sOutputEscapes128
      val maxIndex     = literal.length - 1

      @tailrec
      def detect(i: Int): Unit = {
        if (i <= maxIndex) {
          val c = literal(i)
//          val e = try {
//            HC(c)
//          } catch {
//            case _: Exception => -1
//          }
//          if (e > 0) {
//            b.append('\\')
//            b.append(e.toChar)
//          } else {
//            b.append(c)
//          }
          toEscaping(c, b)
          detect(i + 1)
        }
      }

      detect(0)
      b.append('"')
    }
    private[this] final def toEscaping(c: Char, b: StringBuilder): Unit = {
      (c: @switch) match {
        case '\\'  => b.append("\\\\")
        case '"'   => b.append("\\\"")
        case '\r'  => b.append("\\r")
        case '\n'  => b.append("\\n")
        case '\f'  => b.append("\\f")
        case '\b'  => b.append("\\b")
        case '\t'  => b.append("\\t")
        case other => b.append(other)
      }
    }

    override def joinUnsafe(js: JsonVal): JsonVal =
      throw UnexpectedJsonTreeException("Cannot add element to JsLiteral.")

    override def named(key: String): JsonVal =
      throw UnexpectedJsonTreeException(s"Cannot access key : $key of $toString")
  }

  private[refuel] case class JsStackArray(bf: JsonVal) extends JsStack {
    protected var stack = Vector.newBuilder[JsonVal]

    private[this] var commad = true

    override def approvalSyntax(c: Char): Unit = {
      if (!commad && c == ',') {
          commad = true
      } else duplicateComma
    }

    private[this] def duplicateComma: Unit = {
      throw IllegalJsonFormatException(s"Duplicate comma after [${stack.result().mkString(", ")}]")
    }

    override def squash: JsonVal = {
      bf joinUnsafe JsArray(stack.result())
    }

    def joinUnsafe(js: JsonVal): JsonVal = {
      if (js != null && commad) {
        stack += js
        commad = false
        pos += 1
      }
      this
    }
  }

  private[refuel] case class JsStackObjects(bf: JsonVal) extends JsStack {
    protected var stack = new util.LinkedHashMap[JsString, JsonVal]()

    private var standBy: JsString = _

    private[this] var coloned: Boolean = false

    override def approvalSyntax(c: Char): Unit = {
      if (!coloned && c == ':')
        coloned = true
    }

    override def squash: JsonVal = {
      if (standBy != null || coloned) illegalJsonFormat(standBy)
      bf joinUnsafe new JsObject(stack)
    }

    private[this] final def illegalJsonFormat(js: JsonVal): Unit = {
      throw IllegalJsonFormatException(s"Unspecified json value of key: #$js#")
    }

    def joinUnsafe(js: JsonVal): JsonVal = {
      if (js != null) {
        if (standBy == null) {
          js match {
            case x: JsString =>
              standBy = x
            case _ => requireAddJsStr(js)
          }
        } else if (coloned) {
          stack.put(standBy, js)
          standBy = null
          coloned = false
          pos += 1
        } else {
          illegalJsonFormat(standBy)
        }
      }
      this
    }

    private[this] final def requireAddJsStr(js: JsonVal): Unit = {
      throw UnexpectedJsonTreeException(s"Cannot add JsKey to JsObject. Must be JsString, but was $js")
    }
  }

  object JsAny {
    def apply[T](value: T): JsonVal = {
      if (value == null || value == "null") JsNull else new JsAny(value.toString.trim)
    }
  }

  object JsArray {
    def Empty: JsArray = JsArray(Nil)
    def apply(bf: TraversableOnce[JsonVal]): JsArray = new JsArray(bf.iterator.to(Seq))
  }

  case object JsEmpty extends JsVariable {

    override def isEmpty: Boolean    = true
    override def isNonEmpty: Boolean = false

    override def toString: String     = ""
    def encode(b: StringBuilder): Unit = ()

    override def joinUnsafe(js: JsonVal): JsonVal = js
  }

  case object JsNull extends JsVariable {

    override def isEmpty: Boolean    = true
    override def isNonEmpty: Boolean = false

    def encode(b: StringBuilder): Unit = b.append(toString)

    override final def toString: String = "null"

    override def joinUnsafe(js: JsonVal): JsonVal =
      throw UnexpectedJsonTreeException("Cannot add element to JsNull.")
  }

  object JsObject {

    def apply(req: (String, JsonVal)*): JsonVal = {
      val map = new util.LinkedHashMap[JsString, JsonVal]()
      map.putAll(req.map {
        case (x, y) => JsString(x) -> y
      }.toMap.asJava)
      new JsObject(map)
    }

    def pure(nullableEntries: Iterable[(JsString, JsonVal)]): JsonVal = {
      val map = new util.LinkedHashMap[JsString, JsonVal]()
      map.putAll(nullableEntries.toMap.asJava)
      new JsObject(map)
    }
  }

  object JsString {
    def apply(literal: String): JsString      = new JsString(literal)
    def apply(literal: JsonKeySpec): JsString = new JsString(literal.toString)

    private[refuel] final val sOutputEscapes128 = {
      val table = new Array[Int](128)
      for (i <- 0 until 32) {
        table(i) = CharacterEscapes.ESCAPE_STANDARD
      }
      table('"') = '"'
      table('\\') = '\\'
      table(0x08) = 'b'
      table(0x09) = 't'
      table(0x0C) = 'f'
      table(0x0A) = 'n'
      table(0x0D) = 'r'
      table
    }
  }
}
