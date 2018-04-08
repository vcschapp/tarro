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

package io.tarro.clazz.parse.attribute.annotation;

/**
 * @author Victor Schappert (schapper@)
 * @since 20171116
 */
public final class LocalVariableTargetTableEntry {

    //
    // DATA
    //

    private final int startPC;
    private final int length;
    private final int index;

    //
    // CONSTRUCTORS
    //

    public LocalVariableTargetTableEntry(final int startPC, final int length, final int index) {
        this.startPC = startPC;
        this.length = length;
        this.index = index;
    }

    //
    // PUBLIC METHODS
    //

    public int getStartPC() {
        return startPC;
    }

    public int getLength() {
        return length;
    }

    public int getIndex() {
        return index;
    }
}
