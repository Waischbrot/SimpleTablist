package de.waischbrot.simpletablist.utils;

public record Pair<F, S>(F v1, S v2) {

    public F getFirst() {
        return v1;
    }

    public S getSecond() {
        return v2;
    }
}
