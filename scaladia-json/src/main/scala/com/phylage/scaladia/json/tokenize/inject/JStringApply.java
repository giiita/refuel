package com.phylage.scaladia.json.tokenize.inject;

import com.phylage.scaladia.json.tokenize.combinator.JStreamReader;

public class JStringApply extends JStreamReader {
  protected String apply(char[] v) {
    return new String(v);
  }
}
