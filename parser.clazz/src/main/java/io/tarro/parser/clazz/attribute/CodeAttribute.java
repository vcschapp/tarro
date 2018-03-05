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

import java.nio.ByteBuffer;
import java.util.List;

import static io.tarro.base.attribute.AttributeType.CODE;
import static java.util.Objects.requireNonNull;

/**
 * <p>
 *
 * </p>
 *
 * @author Victor Schappert
 * @since 20171130
 */
public final class CodeAttribute extends Attribute {

    //
    // DATA
    //

    private final int maxStack;
    private final int maxLocals;
    private final byte[] bytecode;
    private final List<ExceptionTableEntry> exceptionTable;
    private final List<Attribute> attributes;

    //
    // CONSTRUCTORS
    //

    public CodeAttribute(final int maxStack, final int maxLocals, final byte[] bytecode,
        final ExceptionTableEntry[] exceptionTable, final Attribute[] attributes) {
        super(CODE);
        this.maxStack = maxStack;
        this.maxLocals = maxLocals;
        this.bytecode = requireNonNull(bytecode, "bytecode cannot be null");
        this.exceptionTable = List.of(exceptionTable);
        this.attributes = List.of(attributes);
    }

    //
    // PUBLIC METHODS
    //

    public int getMaxStack() {
        return maxStack;
    }

    public int getMaxLocals() {
        return maxLocals;
    }

    public ByteBuffer getByteCode() {
        return ByteBuffer.wrap(bytecode).asReadOnlyBuffer();
    }

    public List<ExceptionTableEntry> getExceptionTable() {
        return exceptionTable;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }
}
