package refuel.json.tokenize

import java.util

trait GlowableBuffer {
  protected var chbuff = new Array[Char](1 << 7)

  protected final def glowArray(addStrLen: Int): Unit = {
    chbuff = util.Arrays.copyOf(chbuff, Integer.highestOneBit(addStrLen) << 1)
  }
}
