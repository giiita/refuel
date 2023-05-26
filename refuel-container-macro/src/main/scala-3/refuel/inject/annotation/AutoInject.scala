package refuel.inject.annotation

import java.lang.annotation.{Retention, RetentionPolicy, Target}
import scala.annotation.StaticAnnotation


@Retention(RetentionPolicy.SOURCE)
class AutoInject extends StaticAnnotation