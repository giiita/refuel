package refuel.container.anno

import java.lang.annotation.{ElementType, Retention, RetentionPolicy, Target}

import refuel.internal.di.Effect

/**
 * @see [[Effect]]
 * @param target If the target effect is valid, the symbol with this annotation is an injection candidate.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(Array(ElementType.TYPE))
final class Effective(target: Effect) extends scala.annotation.StaticAnnotation
