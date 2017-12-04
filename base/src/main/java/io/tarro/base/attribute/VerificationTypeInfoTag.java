/*
MIT License

Copyright (c) 2017 Victor Schappert

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package io.tarro.base.attribute;

import io.tarro.base.Valued;

/**
 * <p>
 *
 * </p>
 *
 * @author Victor Schappert
 * @since 20171130
 */
public enum VerificationTypeInfoTag implements Valued {

    //
    // ENUMERATORS
    //

    // TODO: figure out right order. I did this fast/sloppy I think coopying JVM spec ch 4 order
    TOP(0),
    INTEGER(1),
    FLOAT(2),
    NULL(5),
    UNINITIALIZED_THIS(6),
    OBJECT(7),
    UNINITIALIZED(8),
    LONG(4),
    DOUBLE(3);

    //
    // DATA
    //

    private final int value;

    //
    // CONSTRUCTORS
    //

    VerificationTypeInfoTag(final int value) {
        this.value = value;
    }

    //
    // ANCESTOR CLASS: Valued
    //

    @Override
    public int getValue() {
        return value;
    }
}
