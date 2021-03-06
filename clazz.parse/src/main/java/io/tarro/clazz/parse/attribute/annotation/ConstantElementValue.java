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

import io.tarro.base.attribute.ElementValueTag;

import static java.util.Objects.requireNonNull;

/**
 * @author Victor Schappert
 * @since 20171106
 */
public final class ConstantElementValue implements ElementValue {

    //
    // DATA
    //

    private final ElementValueTag elementValueTag;
    private final int constantValueIndex;

    //
    // CONSTRUCTORS
    //

    public ConstantElementValue(final ElementValueTag elementValueTag, final int constantValueIndex) {
        assert ElementValueTag.BYTE == elementValueTag || ElementValueTag.CHAR == elementValueTag ||
            ElementValueTag.DOUBLE == elementValueTag || ElementValueTag.FLOAT == elementValueTag ||
            ElementValueTag.INT == elementValueTag || ElementValueTag.LONG == elementValueTag ||
            ElementValueTag.SHORT == elementValueTag || ElementValueTag.BOOLEAN == elementValueTag;
        this.elementValueTag = requireNonNull(elementValueTag, "elementValueTag cannot be null");
        this.constantValueIndex = constantValueIndex;
    }

    //
    // INTERFACE: ElementValue
    //

    @Override
    public ElementValueTag getElementValueTag() {
        return elementValueTag;
    }

    //
    // PUBLIC METHODS
    //

    public int getConstantValueIndex() {
        return constantValueIndex;
    }
}
