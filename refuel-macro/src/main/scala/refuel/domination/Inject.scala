package refuel.domination

import java.lang.annotation.{ElementType, Retention, RetentionPolicy, Target}

import scala.annotation.StaticAnnotation

@Retention(RetentionPolicy.RUNTIME)
class Inject(value: InjectionPriority) extends StaticAnnotation
