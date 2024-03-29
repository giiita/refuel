package refuel.json.compiletime

import scala.compiletime.package$package.{constValue, erasedValue}

private[refuel] trait ProductProjection {

  transparent inline def inferLabels[T <: Tuple]: List[String] = foldElementLabels[T]
  transparent inline def foldElementLabels[T <: Tuple]: List[String] =
    inline erasedValue[T] match {
      case _: EmptyTuple =>
        Nil
      case _: (t *: ts) =>
        constValue[t].asInstanceOf[String] :: foldElementLabels[ts]
    }
}
