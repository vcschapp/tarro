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

package io.tarro.base.constantpool;

import io.tarro.base.Valued;

/**
 * Enumerates available kinds of {@plainlink
 * org.victorschappert.jarn.parser.constantpool.MethodHandleReferenceEntry
 * method handle reference}.
 *
 * @author Victor Schappert
 * @since 20171014
 */
public enum MethodHandleReferenceKind implements Valued {

    //
    // ENUMERATORS
    //

    GET_FIELD(1, "getField"),
    GET_STATIC(2, "getStatic"),
    PUT_FIELD(3, "putField"),
    PUT_STATIC(4, "putStatic"),
    INVOKE_VIRTUAL(5, "invokeVirtual"),
    INVOKE_STATIC(6, "invokeStatic"),
    INVOKE_SPECIAL(7, "invokeSpecial"),
    NEW_INVOKE_SPECIAL(8, "newInvokeSpecial"),
    INVOKE_INTERFACE(9, "invokeInterface");

    //
    // DATA
    //

    private final int value;
    private final String kindName;

    //
    // CONSTRUCTORS
    //

    MethodHandleReferenceKind(final int value, final String kindName) {
        this.value = value;
        this.kindName = kindName;
    }

    //
    // INTERFACE: Valued
    //

    @Override
    public int getValue() {
        return value;
    }

    //
    // PUBLIC METHODS
    //

    /**
     * Obtains the reference kind's name.
     *
     * @return Kind name
     */
    public String getKindName() {
        return kindName;
    }
}
