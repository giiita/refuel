package refuel.json.internal

import refuel.json.Json

trait JsonTokenizer {
  def run(v: String): Json
}
