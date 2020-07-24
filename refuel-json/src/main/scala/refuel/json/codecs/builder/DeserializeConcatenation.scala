package refuel.json.codecs.builder

import refuel.json.codecs.{CodecTyper, Read}

private[refuel] class DeserializeConcatenation[T](v: Read[T])(implicit typer: CodecTyper[Read]) {
  def readMap[R](fn: T => Read[R]): Read[R] = typer.readOnly[R](json => typer.read(json)(fn(typer.read(json)(v))))
}
