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

import io.tarro.base.attribute.AttributeType;

import java.util.stream.IntStream;

import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static io.tarro.base.attribute.AttributeType.EXCEPTIONS;
import static io.tarro.base.attribute.AttributeType.MODULE_PACKAGES;

/**
 * <p>
 * Predefined attribute whose only meaningful data are a list of indices into
 * the constant pool.
 * </p>
 *
 * <p>
 * At present, the parser extracts the following predefined attributes as
 * instances of this class:
 * </p>
 *
 * <ul>
 * <li>{@linkplain AttributeType#EXCEPTIONS Exceptions}; and</li>
 * <li>{@linkplain AttributeType#MODULE_PACKAGES ModulePackages}.</li>
 * </ul>
 *
 * @author Victor Schappert
 * @since 20171031
 * @see ConstantPoolIndexAttribute
 */
public final class ConstantPoolIndicesAttribute extends Attribute {

    //
    // DATA
    //

    private final int[] constantPoolIndices;

    //
    // CONSTRUCTORS
    //

    /**
     * Creates a new attribute containing a list of constant pool indices.
     *
     * @param attributeType Type of the attribute
     * @param constantPoolIndices List of the attribute's constant pool indices
     */
    public ConstantPoolIndicesAttribute(final AttributeType attributeType,
        final int[] constantPoolIndices) {
        super(attributeType);
        assert EXCEPTIONS == attributeType || MODULE_PACKAGES == attributeType;
        this.constantPoolIndices = requireNonNull(constantPoolIndices, "constantPoolIndices cannot be null");
    }

    //
    // PUBLIC METHODS
    //

    /**
     * Obtains the size of the constant pool indices list.
     *
     * @return Non-negative number of indices in the constant pool indices list
     * @see #getConstantPoolIndices()
     */
    public int getConstantPoolIndicesCount() {
        return constantPoolIndices.length;
    }

    /**
     * Obtains a stream containing the constant pool indices.
     *
     * @return List of constant pool indices as a stream
     * @see #getConstantPoolIndicesCount()
     */
    public IntStream getConstantPoolIndices() {
        return stream(constantPoolIndices);
    }
}
