package refuel.json.tokenize.combinator;

import java.io.IOException;
import java.io.StringReader;

public class JStreamReader {
  protected char read(StringReader v) throws IOException {
    return (char) v.read();
  }
}
