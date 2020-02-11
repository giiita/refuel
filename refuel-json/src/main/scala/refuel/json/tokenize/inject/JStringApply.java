package refuel.json.tokenize.inject;

public class JStringApply {

    protected char[] from(char[] v, int from) {
        int ln = v.length - from;
        char[] nr = new char[ln];
        System.arraycopy(v, from, nr, 0, ln);
        return nr;
    }

    protected String apply(char[] v) {
        return new String(v);
    }
}
