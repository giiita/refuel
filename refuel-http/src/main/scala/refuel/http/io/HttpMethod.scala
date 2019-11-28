package refuel.http.io

object HttpMethod {

  abstract class Method

  class CONNECT extends Method

  class HEAD extends Method

  class OPTIONS extends Method

  class PATCH extends Method

  class TRACE extends Method

  class GET extends Method

  class PUT extends Method

  class POST extends Method

  class DELETE extends Method

}
