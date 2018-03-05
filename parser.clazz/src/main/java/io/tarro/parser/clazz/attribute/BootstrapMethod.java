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
 * <p>
 *
 * </p>
 *
 * @author Victor Schappert
 * @since 20171126
 */
public final class BootstrapMethod {

    //
    // DATA
    //

    private final int bootstrapMethodIndex;
    private final int[] bootstrapArgumentIndices;

    //
    // CONSTRUCTORS
    //

    public BootstrapMethod(final int bootstrapMethodIndex, final int[] bootstrapArgumentIndices) {
        this.bootstrapMethodIndex = bootstrapMethodIndex;
        this.bootstrapArgumentIndices = bootstrapArgumentIndices;
    }

    //
    // PUBLIC METHODS
    //

    public int getBootstrapMethodIndex() {
        return bootstrapMethodIndex;
    }

    public int[] getBootstrapArgumentIndices() {
        return bootstrapArgumentIndices;
    }
}
