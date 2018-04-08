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

package io.tarro.clazz.parse.constantpool;

import io.tarro.base.constantpool.ConstantPoolTag.AssociatedWith;

import static io.tarro.base.constantpool.ConstantPoolTag.LONG;

/**
 * <p>
 * Encapsulates a CONSTANT_Long structure in the constant pool.
 * </p>
 *
 * <p>
 * Note that the CONSTANT_Long structure occupies two adjacent indices in the
 * constant pool.
 * </p>
 *
 * @author Victor Schappert
 * @since 20171014
 */
@AssociatedWith(LONG)
public final class LongEntry extends ConstantPoolEntry {

    //
    // DATA
    //

    final long value;

    //
    // CONSTRUCTORS
    //

    /**
     * Creates the long constant entry.
     *
     * @param value Long constant value
     */
    public LongEntry(final long value) {
        super(LONG);
        this.value = value;
    }

    //
    // PUBLIC METHODS
    //

    /**
     * Returns the long constant value.
     *
     * @return Long constant value
     */
    public long getValue() {
        return value;
    }
}
