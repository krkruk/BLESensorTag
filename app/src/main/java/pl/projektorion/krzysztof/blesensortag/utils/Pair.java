package pl.projektorion.krzysztof.blesensortag.utils;

/**
 * Created by krzysztof on 16.12.16.
 */

public class Pair <F, S> {
    private F _first;
    private S _second;

    public Pair(F first, S second) {
        this._first = first;
        this._second = second;
    }

    public F first() {
        return _first;
    }

    public S second() {
        return _second;
    }
}
