package refuel.json.tokenize

import scala.collection.mutable.ArrayBuffer
import scala.reflect.ClassTag

object ArrayCopy {
  /**
   * Create a new array from an index.
   *
   * @param s Array to copy from.
   * @param from Copy start index.
   * @return
   */
  def from(s: Array[Char], from: Int): Array[Char] = {
    val ln = s.length - from
    val nr = new Array[Char](ln)
    Array.copy(s, from, nr, 0, ln)
    nr
  }

  def cut(s: Array[Char], length: Int): Array[Char] = {
    val nr = new Array[Char](length)
    Array.copy(s, 0, nr, 0, length)
    nr
  }

  def fromBuffer[T: ClassTag](s: ArrayBuffer[T]): Array[T] = {
    val ln = s.length
    val nr = new Array[T](ln)
    Array.copy(s, 0, nr, 0, ln)
    nr
  }

  /**
   * Combine a new array from two arrays.
   *
   * @param s1 Array to copy from.
   * @param s2 Array to copy from.
   * @return
   */
  def ++(s1: Array[Char], s2: Array[Char]): Array[Char] = {
    val ln = s1.length + s2.length
    val nr = new Array[Char](ln)
    Array.copy(s1, 0, nr, 0, s1.length)
    Array.copy(s2, 0, nr, s1.length, s2.length)
    nr
  }
}
