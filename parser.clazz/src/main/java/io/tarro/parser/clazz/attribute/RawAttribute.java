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

import java.nio.ByteBuffer;

import static java.util.Objects.requireNonNull;
import static io.tarro.base.attribute.AttributeType.SOURCE_DEBUG_EXTENSION;
import static io.tarro.base.attribute.AttributeType.UNKNOWN;

/**
 * <p>
 * Encapsulates an {@code attribute_info} structure within a Java class file
 * which represents an attribute whose {@code u1 info[attribute_length]} array
 * has not been interpreted by the parser and which is consequently returned as
 * an array of bytes.
 * </p>
 *
 * <p>
 * At present, the following attributes are returned as instances of this class:
 * </p>
 *
 * <ul>
 * <li>{@linkplain AttributeType#SOURCE_DEBUG_EXTENSION SourceDebugExtension}; and</li>
 * <li>
 * any attribute, regardless of name, which is not a predefined attribute
 * in the context in which it was found.
 * </li>
 * </ul>
 *
 * @author Victor Schappert
 * @since 20171016
 */
public final class RawAttribute extends Attribute {

    //
    // DATA
    //

    private final int nameIndex;
    private final byte[] info;

    //
    // CONSTRUCTORS
    //

    public RawAttribute(final AttributeType attributeType, final int nameIndex,
            final byte[] info) {
        super(attributeType);
        assert SOURCE_DEBUG_EXTENSION == attributeType || UNKNOWN == attributeType;
        this.nameIndex = nameIndex;
        this.info = requireNonNull(info, "info cannot be null");
    }

    //
    // PUBLIC METHODS
    //

    /**
     * Obtains the constant pool index of the constant pool entry that contains
     * this attribute's name.
     *
     * @return Attribute name
     */
    public int getNameIndex() {
        return nameIndex;
    }

    /**
     * Obtains a read-only byte buffer containing the bytes in the attribute's
     * {@code info} array.
     *
     * @return Length of the attribute's {@code info} byte array.
     */
    public ByteBuffer getInfo() {
        return ByteBuffer.wrap(info).asReadOnlyBuffer();
    }
}
