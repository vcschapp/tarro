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

import io.tarro.parser.clazz.attribute.annotation.ElementValue;
import io.tarro.base.attribute.AttributeType;

import static java.util.Objects.requireNonNull;
import static io.tarro.base.attribute.AttributeType.ANNOTATION_DEFAULT;

/**
 * <p>
 * {@linkplain AttributeType#ANNOTATION_DEFAULT AnnotationDefault}
 * predefined attribute.
 * </p>
 *
 * @author Victor Schappert
 * @since 20171112
 */
public final class AnnotationDefaultAttribute extends Attribute {

    //
    // DATA
    //

    private final ElementValue defaultValue;

    //
    // CONSTRUCTORS
    //

    public AnnotationDefaultAttribute(final ElementValue defaultValue) {
        super(ANNOTATION_DEFAULT);
        this.defaultValue = requireNonNull(defaultValue, "defaultValue cannot be null");
    }

    //
    // PUBLIC METHODS
    //

    public ElementValue getDefaultValue() {
        return defaultValue;
    }
}
