package refuel.json.codecs

trait RwProjection {
  implicit def codecTypeProjectionRead: CodecTypeProjection[Read]

  implicit def codecTypeProjectionWrite: CodecTypeProjection[Write]
}

