package refuel.container.internal

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater
import java.util.function.UnaryOperator
import scala.annotation.tailrec

trait AtomicUpdater[U, W] {
  self: U =>
  val updater: AtomicReferenceFieldUpdater[U, W]

  /**
   * Recursive, atomic updates.
   * If there is an unguaranteed update of atomicity, it may loop indefinitely.
   *
   * @param f Update function.
   * @return
   */
  @tailrec
  protected final def atomicUpdate(f: W => W): W = {
    val ref = getRef
    val nw = f(snapshot(ref))
    if (compareAndSet(ref, nw)) nw else atomicUpdate(f)
  }

  /**
   * Provides a way to create a snapshot if necessary.
   * Usually not processed.
   *
   * In that case, response is a reference, so if you change it,
   * it may affect the value that other threads reference.
   * To prevent this, use [[snapshot]] or [[compareAndSet]] to override snapshot and update.
   *
   * {{{
   *   override def snapshot(w: W): W = w.snapshot()
   *
   *   val old = getRef
   *   val newRef = get
   *   newRef.update(x -> y)
   *   compareAndSet(old, newRef)
   * }}}
   *
   * @param w value type
   * @return
   */
  protected def snapshot(w: W): W = throw new RuntimeException("Snapshot method is not defined.")

  /**
   * Update only if the existing data is in the expected state.
   * If it is not in the expected state, it will not be updated.
   *
   * @param o Expected symbol.
   * @param n Update symbol.
   * @return
   */
  protected def compareAndSet(o: W, n: W): Boolean = updater.compareAndSet(this, o, n)

  /**
   * Returns a new reference that is the result of the snapshot.
   *
   * @return
   */
  protected def get: W = snapshot(getRef)

  /**
   * Gets the current value held in the field of the given object managed
   * by this updater.
   *
   * @return the current value
   */
  protected def getRef: W = updater.get(this)

  /**
   * Sets the field of the given object managed by this updater to the
   * given updated value. This operation is guaranteed to act as a volatile
   * store with respect to subsequent invocations of [[compareAndSet]].
   *
   * @param n the new value
   */
  protected def set(n: W): Unit = updater.set(this, n)

  /**
   * Atomically sets the field of the given object managed by this updater
   * to the given value and returns the old value.
   *
   * @param n the new value
   * @return the previous value
   */
  protected def getAndSet(n: W): W = updater.getAndSet(this, n)

  /**
   * Atomically updates the field of the given object managed by this updater
   * with the results of applying the given function, returning the previous
   * value. The function should be side-effect-free, since it may be
   * re-applied when attempted updates fail due to contention among threads.
   *
   * @param nf a side-effect-free function
   * @return the previous value
   * @since 1.8
   */
  protected def getAndUpdate(nf: UnaryOperator[W]): W = updater.getAndUpdate(this, nf)
}
