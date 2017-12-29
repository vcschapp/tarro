/*
 * MIT License
 *
 * Copyright (c) 2017 Victor Schappert
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

package io.tarro.base.bytecode;

import java.util.List;

import static io.tarro.base.bytecode.OpcodeTable.noOpcodeForValue;
import static io.tarro.base.bytecode.OpcodeTable.notAnUnsignedByte;
import static io.tarro.base.bytecode.OperandType.CONSTANT_POOL_INDEX_SHORT;
import static io.tarro.base.bytecode.OperandType.LOCAL_VARIABLE_INDEX_BYTE;
import static io.tarro.base.bytecode.OperandType.SIGNED_VALUE_BYTE;
import static io.tarro.base.bytecode.OperandType.UNSIGNED_VALUE_BYTE;

/**
 * <p>
 * Enumerates opcodes for Java Virtual Machine instructions which have two
 * operands embedded in the instruction.
 * </p>
 *
 * <p>
 * Note that even if the instruction embeds two operands, it may and often
 * does consume one or more additional operands from the operand stack.
 * </p>
 *
 * @author Victor Schappert
 * @since 20171218
 * @see NoOperandOpcode
 * @see OneOperandOpcode
 * @see VariableOperandOpcode
 * @see OpcodeValue
 */
public enum TwoOperandOpcode implements Opcode {

    //
    // ENUMERATORS
    //

    // Keep this list in alphabetical order by opcode mnemonic.
    /**
     * <p>
     * {@code iinc} - Increment local variable by constant
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value OpcodeValue#IINC}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>
     * {@code iinc}
     * <em>{@linkplain OperandType#LOCAL_VARIABLE_INDEX_BYTE index}</em>,
     * <em>{@linkplain OperandType#SIGNED_VALUE_BYTE value}</em>
     * </dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip; &rarr; &hellip;</dd>
     * </dl>
     *
     * @see OpcodeValue#IINC
     * @see OperandType#LOCAL_VARIABLE_INDEX_BYTE
     * @see OperandType#SIGNED_VALUE_BYTE
     * @see VariableOperandOpcode#WIDE
     * @see NoOperandOpcode#IADD
     */
    IINC(OpcodeValue.IINC, LOCAL_VARIABLE_INDEX_BYTE, SIGNED_VALUE_BYTE),
    /**
     * <p>
     * {@code invokeinterface} - Invoke interface method
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value OpcodeValue#INVOKEINTERFACE}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>
     * {@code invokeinterface}
     * <em>{@linkplain OperandType#CONSTANT_POOL_INDEX_SHORT index}</em>,
     * <em>{@linkplain OperandType#UNSIGNED_VALUE_BYTE count}</em>
     * </dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>objectref</em>, [<em>arg1</em>, [<em>arg2</em>, &hellip;]]
     * &rarr;
     * &hellip;
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#INVOKEINTERFACE
     * @see OperandType#CONSTANT_POOL_INDEX_SHORT
     * @see OperandType#UNSIGNED_VALUE_BYTE
     * @see OneOperandOpcode#INVOKEVIRTUAL
     * @see OneOperandOpcode#INVOKESPECIAL
     * @see OneOperandOpcode#INVOKESTATIC
     * @see OneOperandOpcode#INVOKEDYNAMIC
     */
    INVOKEINTERFACE(OpcodeValue.INVOKEINTERFACE, CONSTANT_POOL_INDEX_SHORT,
                    UNSIGNED_VALUE_BYTE),
    /**
     * <p>
     * {@code multianewarray} - Create new multidimensional array
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value OpcodeValue#MULTIANEWARRAY}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>
     * {@code multianewarray}
     * <em>{@linkplain OperandType#CONSTANT_POOL_INDEX_SHORT index}</em>,
     * <em>{@linkplain OperandType#UNSIGNED_VALUE_BYTE dimensions}</em>
     * </dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>count1</em>, [<em>count2</em>, &hellip;] &rarr;
     * &hellip; <em>arrayref</em>
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#MULTIANEWARRAY
     * @see OperandType#CONSTANT_POOL_INDEX_SHORT
     * @see OperandType#UNSIGNED_VALUE_BYTE
     * @see OneOperandOpcode#ANEWARRAY
     * @see OneOperandOpcode#NEW
     * @see OneOperandOpcode#NEWARRAY
     */
    MULTIANEWARRAY(OpcodeValue.MULTIANEWARRAY, CONSTANT_POOL_INDEX_SHORT,
                    UNSIGNED_VALUE_BYTE);

    //
    // DATA
    //

    private final int value;
    private final List<OperandType> operandTypes;

    //
    // CONSTRUCTORS
    //

    TwoOperandOpcode(final int value, final OperandType firstOperandType,
                     final OperandType secondOperandType) {
        this.value = value;
        this.operandTypes = List.of(firstOperandType, secondOperandType);
    }

    //
    // INTERFACE: Opcode
    //

    @Override
    public List<OperandType> getOperandTypes() {
        return operandTypes;
    }

    @Override
    public int getNumOperands() {
        return 2;
    }

    //
    // INTERFACE: Valued
    //

    @Override
    public int getValue() {
        return value;
    }

    //
    // PUBLIC STATICS
    //

    /**
     * Obtains the {@link TwoOperandOpcode} for the given opcode byte value.
     *
     * @param value Opcode byte value
     * @return Opcode instance
     * @throws IllegalArgumentException If {@code value} is not in the range
     *                                  0..255 <em>or</em> the value is in the
     *                                  correct range but that value does not
     *                                  correspond to a known two-operand opcode
     * @see OpcodeValue
     * @see Opcode#forUnsignedByte(int)
     * @see NoOperandOpcode#forUnsignedByte(int)
     * @see OneOperandOpcode#forUnsignedByte(int)
     * @see VariableOperandOpcode#forUnsignedByte(int)
     */
    public static TwoOperandOpcode forUnsignedByte(final int value) {
        switch (value) {
            case OpcodeValue.IINC:
                return IINC;
            case OpcodeValue.INVOKEINTERFACE:
                return INVOKEINTERFACE;
            case OpcodeValue.MULTIANEWARRAY:
                return MULTIANEWARRAY;
            default:
                final IllegalArgumentException e;
                if (0 <= value && value <= 255) {
                    e = noOpcodeForValue(TwoOperandOpcode.class, value);
                } else {
                    e = notAnUnsignedByte(value);
                }
                throw e;
        }
    }
}
