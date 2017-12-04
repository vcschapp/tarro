package io.tarro.parser.clazz.attribute;

/**
 * Ordered pair of integers.
 *
 * @author Victor Schappert
 * @since 20171104
 */
public final class IntPair { // TODO: class has only 2 usages now, may want to remove it

    //
    // DATA
    //

    private final int first;
    private final int second;

    //
    // CONSTRUCTORS
    //

    /**
     * Makes a new integer pair.
     *
     * @param first First integer in the pair
     * @param second Second integer in the pair
     */
    public IntPair(final int first,
        final int second) {
        this.first = first;
        this.second = second;
    }

    //
    // PUBLIC METHODS
    //

    /**
     * Obtains the first integer in the pair.
     *
     * @return First value
     */
    public int getFirst() {
        return first;
    }

    /**
     * Obtains the second integer in the pair.
     *
     * @return Second value
     */
    public int getSecond() {
        return second;
    }
}
