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

package io.tarro.parser.clazz.attribute.annotation;

import static java.util.Objects.requireNonNull;

/**
 * Ordered pair modeling consisting of an {@linkplain #getElementNameIndex()
 * index into the constant pool representing the element name}; and
 * {@linkplain #getValue() an object representing the element value}.
 *
 * @author Victor Schappert
 * @since 20171106
 */
public final class ElementValuePair {

    //
    // DATA
    //

    private final int elementNameIndex;
    private final ElementValue value;

    //
    // CONSTRUCTORS
    //

    public ElementValuePair(final int elementNameIndex, final ElementValue value) {
        this.elementNameIndex = elementNameIndex;
        this.value = requireNonNull(value, "value cannot be null");
    }

    //
    // PUBLIC METHODS
    //

    public int getElementNameIndex() {
        return elementNameIndex;
    }

    public ElementValue getValue() {
        return value;
    }
}