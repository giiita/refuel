package refuel.inject

import java.lang.annotation.{Retention, RetentionPolicy}
import scala.annotation.StaticAnnotation

@Retention(RetentionPolicy.RUNTIME)
class Inject[+T <: InjectionPriority] extends StaticAnnotation
