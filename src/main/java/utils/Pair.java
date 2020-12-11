package utils;

public class Pair<T> {
    public final T first;
    public final T second;

    public Pair(T first, T second) {
        this.first = first;
        this.second = second;
    }

    public Pair(T[] items) {
        this(items[0], items[1]);
    }

    public boolean equalsAny(T value) {
        return value.equals(first) || value.equals(second);
    }
}
