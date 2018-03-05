/*
 * MIT License
 *
 * Copyright (c) 2018 Victor Schappert
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
