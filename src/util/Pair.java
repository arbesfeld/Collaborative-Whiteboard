package util;

/**
 * Pair represents an immutable pair of values (x,y). x and y MAY NOT be null.
 * 
 * @param <X> Type of the first object
 * @param <Y> Type of the second object
 */
public class Pair<X, Y> {

    // Using public final fields here so that client can read first and second
    // directly, but can't assign to them.
    public final X first;
    public final Y second;

    // rep invariant: first, second != null

    /**
     * Make a pair.
     * 
     * @param first
     *            Requires first != null.
     * @param second
     *            Requires second != null.
     */
    public Pair(X first, Y second) {
        if (first == null || second == null)
            throw new NullPointerException();
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { // quick check
            return true;
        }

        if (o == null || !(o instanceof Pair<?, ?>)) {
            return false;
        }

        Object otherFirst = ((Pair<?, ?>) o).first;
        Object otherSecond = ((Pair<?, ?>) o).second;
        return this.first.equals(otherFirst) && this.second.equals(otherSecond);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 37 * result + first.hashCode();
        result = 37 * result + second.hashCode();
        return result;
    }
}