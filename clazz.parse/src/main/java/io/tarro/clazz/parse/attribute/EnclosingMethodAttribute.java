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

package io.tarro.clazz.parse.attribute;

import io.tarro.base.attribute.AttributeType;

/**
 * <p>
 * {@linkplain AttributeType#ENCLOSING_METHOD EnclosingMethod}
 * predefined attribute.
 * </p>
 *
 * @author Victor Schappert
 * @since 20171104
 */
public final class EnclosingMethodAttribute extends Attribute {

    //
    // DATA
    //

    private final int classIndex;
    private final int methodIndex;

    //
    // CONSTRUCTORS
    //

    /**
     * Constructs the EnclosingMethod attribute.
     *
     * @param classIndex
     * @param methodIndex
     */
    public EnclosingMethodAttribute(final int classIndex, final int methodIndex) {
        super(AttributeType.ENCLOSING_METHOD);
        this.classIndex = classIndex;
        this.methodIndex = methodIndex;
    }

    //
    // PUBLIC METHODS
    //

    public int getClassIndex() {
        return classIndex;
    }

    public int getMethodIndex() {
        return methodIndex;
    }
}
