package com.phylage.scaladia.json.internal

import com.phylage.scaladia.json.Json

trait JsonTokenizer {
  def run(v: String): Json
}
