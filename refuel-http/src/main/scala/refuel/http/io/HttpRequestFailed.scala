package refuel.http.io

class HttpRequestFailed[T](val body: T) extends Throwable {}
