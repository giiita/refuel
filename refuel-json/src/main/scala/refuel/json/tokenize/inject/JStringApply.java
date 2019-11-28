package refuel.json.tokenize.inject;

import refuel.json.tokenize.combinator.JStreamReader;

public class JStringApply extends JStreamReader {
  protected String apply(char[] v) {
    return new String(v);
  }
}
