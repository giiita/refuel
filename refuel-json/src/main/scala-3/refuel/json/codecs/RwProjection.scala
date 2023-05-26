package refuel.json.codecs

trait RwProjection {
  given codecTypeProjectionRead: CodecTypeProjection[Read]

  given codecTypeProjectionWrite: CodecTypeProjection[Write]
}

