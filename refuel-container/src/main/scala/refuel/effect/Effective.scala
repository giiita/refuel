package refuel.effect

import java.lang.annotation.{Retention, RetentionPolicy}

/**
  * @see [[Effect]]
  * @param target If the target effect is valid, the symbol with this annotation is an injection candidate.
  */
@Retention(RetentionPolicy.RUNTIME)
final class Effective(target: Effect) extends scala.annotation.StaticAnnotation