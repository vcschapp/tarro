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

package io.tarro.base.bytecode;

import io.tarro.base.ClassFileVersion;
import io.tarro.base.Valued;
import io.tarro.base.Versioned;

import java.util.List;

import static io.tarro.base.ClassFileVersion.JAVA1;
import static io.tarro.base.bytecode.OpcodeTable.allOpcodes;

/**
 * <p>
 * Represents an opcode in the Java Virtual Machine instruction set.
 * </p>
 *
 * <p>
 * This interface is implemented by several enumerations in this package so as
 * to shard the opcodes into categories: opcodes taking {@linkplain
 * NoOperandOpcode no operands}; {@linkplain OneOperandOpcode a single operand};
 * {@linkplain TwoOperandOpcode}; and {@linkplain VariableOperandOpcode
 * variable operands}. This is primarily for convenience as there are many
 * scenarios where it may be convenient to write a {@code switch} statement, for
 * example, in a method for handling all the zero-operand opcodes. Having an
 * enumeration for this whole class of opcodes makes it easier to get contextual
 * assistance from the IDE (most modern development environments can easily add
 * a {@code case} statement for every enumeration member); and static analyzers,
 * some of which can detect missing {@code switch} cases for enumerations.
 * </p>
 *
 * <p>
 * This interface differs from {@link OpcodeValue}, which is just a container
 * for the complete list of Java Virtual Machine opcode bytes as integer
 * constants. That simplicity may be appropriate for some applications, while
 * others will profit from the additional functionality added by this interface.
 * </p>
 *
 * @author Victor Schappert
 * @since 20171126
 * @see OpcodeValue
 */
public interface Opcode extends Valued, Versioned {

    //
    // METHODS
    //

    List<OperandType> getOperandTypes();

    int getNumOperands();

    default boolean isVariableSize() {
        return false;
    }

    default boolean isReserved() {
        return false;
    }

    default boolean isPadded() {
        return false;
    }

    default int getNumTrailingZeroBytes() {
        return 0;
    }

    //
    // INTERFACE: Versioned
    //

    @Override
    default ClassFileVersion getFirstVersionSupporting() {
        return JAVA1;
    }

    //
    // STATICS
    //

    static Opcode forUnsignedByte(final int value) {
        return allOpcodes().forUnsignedByte(value);
    }
}
