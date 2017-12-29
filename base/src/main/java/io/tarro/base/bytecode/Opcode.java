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

    /**
     * <p>
     * Obtains the list of operand types for the instruction having this opcode.
     * </p>
     *
     * <p>
     * The returned list provides the operands in the order they are used by the
     * instruction. The list may be empty (in the case of a
     * {@linkplain NoOperandOpcode no-operand instruction opcode}) but will
     * never be {@code null}.
     * </p>
     *
     * <table>
     * <caption>
     * Example opcodes and their operand type list:
     * </caption>
     * <tr>
     * <th>
     * Opcode
     * </th>
     * <th>
     * Operand type list
     * </th>
     * </tr>
     * <tr>
     * <td>{@link NoOperandOpcode#ALOAD_0 aload_0}</td>
     * <td>{@code []}</td>
     * </tr>
     * <tr>
     * <td>{@link OneOperandOpcode#ALOAD aload}</td>
     * <td>
     * {@code [}{@linkplain OperandType#LOCAL_VARIABLE_INDEX_BYTE local variable
     * index}{@code ]}
     * </td>
     * </tr>
     * <tr>
     * <td>{@link TwoOperandOpcode#MULTIANEWARRAY multianewarray}</td>
     * <td>
     * {@code [}{@linkplain OperandType#CONSTANT_POOL_INDEX_SHORT runtime
     * constant pool index} (symbolic reference to class){@code , }{@linkplain
     *  OperandType#UNSIGNED_VALUE_BYTE unsigned byte}{@code ]}
     *  </td>
     * </tr>
     * <tr>
     * <td>
     * {@link VariableOperandOpcode#LOOKUPSWITCH lookupswitch}
     * </td>
     * <td>
     * {@code [}{@linkplain OperandType#BRANCH_OFFSET_INT branch offset
     * (4-byte)}{@code , }{@linkplain OperandType#SIGNED_VALUE_INT signed
     * int}{@code , }{@linkplain OperandType#MATCH_OFFSET_PAIR_TABLE
     * match-offset pair table}{@code ]}
     * </td>
     * </tr>
     * </table>
     *
     * @return List of operand types
     * @see #getNumOperands()
     */
    List<OperandType> getOperandTypes();

    /**
     * <p>
     * Obtains the number of operands for instructions having this opcode.
     * </p>
     *
     * <p>
     * The value returned is the size of the {@linkplain #getOperandTypes()
     * operand types list}.
     * </p>
     *
     * @return Number of operands
     * @see #getOperandTypes()
     */
    int getNumOperands();

    /**
     * <p>
     * Indicates whether the instruction for this opcode has variable-size
     * operands.
     * </p>
     *
     * <p>
     * In practice, having variable-size operands is identical with being a
     * member of the {@link VariableOperandOpcode} enumeration.
     * </p>
     *
     * @return Whether this opcode is a variable-operand opcode
     * @see VariableOperandOpcode
     * @see #isPadded()
     */
    default boolean isVariableSize() {
        return false;
    }

    /**
     * <p>
     * Indicates whether the instruction for this opcode has padded operands.
     * </p>
     *
     * <p>
     * A padded instruction is one for which the first operand must be aligned
     * (for example on four-byte boundary) and thus there is a variable amount
     * of padding between the opcode byte and the first operand.
     * </p>
     *
     * <p>
     * In practice, every padded instruction is a {@linkplain #isVariableSize()
     * variable-size instruction}, but not every variable-size instruction is a
     * padded one. There are two padded instructions:
     * {@link VariableOperandOpcode#LOOKUPSWITCH lookupswitch} and
     * {@link VariableOperandOpcode#TABLESWITCH tableswitch}.
     * </p>
     *
     * @return Whether this opcode is for a padded instruction
     * @see #isVariableSize()
     */
    default boolean isPadded() {
        return false;
    }

    /**
     * <p>
     * Indicates whether the Java Virtual Machine Specification declares this
     * opcode to be <em>reserved</em>, meaning it must not appear in the
     * bytecode of an actual class file.
     * </p>
     *
     * <p>
     * In practice, this method returns true if-and-only-if this opcode is one
     * of:
     * </p>
     *
     * <ul>
     * <li>{@link NoOperandOpcode#BREAKPOINT breakpoint};</li>
     * <li>{@link NoOperandOpcode#IMPDEP1 impdep1}; or</li>
     * <li>{@link NoOperandOpcode#IMPDEP2 impdep2}.</li>
     * </ul>
     *
     * @return Whether this is a reserved opcode
     */
    default boolean isReserved() {
        return false;
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

    /**
     * <p>
     * Obtains the {@link Opcode} for the given opcode byte value.
     * </p>
     *
     * <p>
     * The value returned is an object that is both an instance of this
     * interface and a member of one of the following enumerations:
     * </p>
     *
     * <ul>
     * <li>{@link NoOperandOpcode};</li>
     * <li>{@link OneOperandOpcode};</li>
     * <li>{@link TwoOperandOpcode}; or</li>
     * <li>{@link VariableOperandOpcode}.</li>
     * </ul>
     *
     * @param value Opcode byte value
     * @return Opcode instance
     * @throws IllegalArgumentException If {@code value} is not in the range
     *                                  0..255 <em>or</em> the value is in the
     *                                  correct range but that value does not
     *                                  correspond to a known opcode value
     * @see OpcodeValue
     * @see NoOperandOpcode#forUnsignedByte(int)
     * @see OneOperandOpcode#forUnsignedByte(int)
     * @see TwoOperandOpcode#forUnsignedByte(int)
     * @see VariableOperandOpcode#forUnsignedByte(int)
     */
    static Opcode forUnsignedByte(final int value) {
        return allOpcodes().forUnsignedByte(value);
    }
}
