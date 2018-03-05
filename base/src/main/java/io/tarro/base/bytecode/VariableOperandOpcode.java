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

package io.tarro.base.bytecode;

import java.util.List;

import static io.tarro.base.bytecode.OpcodeTable.noOpcodeForValue;
import static io.tarro.base.bytecode.OpcodeTable.notAnUnsignedByte;
import static io.tarro.base.bytecode.OperandType.BRANCH_OFFSET_INT;
import static io.tarro.base.bytecode.OperandType.JUMP_OFFSET_TABLE;
import static io.tarro.base.bytecode.OperandType.LOCAL_VARIABLE_INDEX_SHORT;
import static io.tarro.base.bytecode.OperandType.MATCH_OFFSET_PAIR_TABLE;
import static io.tarro.base.bytecode.OperandType.OPCODE;
import static io.tarro.base.bytecode.OperandType.OPTIONAL_SIGNED_VALUE_SHORT;
import static io.tarro.base.bytecode.OperandType.SIGNED_VALUE_INT;

/**
 * <p>
 * Enumerates opcodes for Java Virtual Machine instructions which do not fit
 * the no operand/one operand/two operands paradigm because the number and/or
 * size of operands embedded in the instruction depends on one or more embedded
 * operands.
 * </p>
 *
 * <p>
 * Note that the instruction may consume operands from the operand stack in
 * addition to the operands embedded within the instruction.
 * </p>

 * @author Victor Schappert
 * @since 20171218
 * @see NoOperandOpcode
 * @see TwoOperandOpcode
 * @see VariableOperandOpcode
 * @see OpcodeValue
 */
public enum VariableOperandOpcode implements Opcode {

    //
    // ENUMERATORS
    //

    // Keep this list in alphabetical order by opcode mnemonic.
    /**
     * <p>
     * {@code lookupswitch} - Access jump table by key match and jump
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#LOOKUPSWITCH}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>
     * {@code lookupswitch}
     * <em>&lt;padding&gt;</em>,
     * <em>{@linkplain OperandType#BRANCH_OFFSET_INT default offset}</em>,
     * <em>{@linkplain OperandType#SIGNED_VALUE_INT npairs}</em>,
     * <em>
     * {@linkplain OperandType#MATCH_OFFSET_PAIR_TABLE match-offset pairs}
     * </em>
     * </dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>key</em> &rarr; &hellip;</dd>
     * </dl>
     *
     * @see OpcodeValue#LOOKUPSWITCH
     * @see OperandType#BRANCH_OFFSET_INT
     * @see OperandType#SIGNED_VALUE_INT
     * @see OperandType#MATCH_OFFSET_PAIR_TABLE
     * @see #TABLESWITCH
     */
    LOOKUPSWITCH(OpcodeValue.LOOKUPSWITCH, BRANCH_OFFSET_INT, SIGNED_VALUE_INT,
            MATCH_OFFSET_PAIR_TABLE),
    /**
     * <p>
     * {@code tableswitch} - Access jump table by index and jump
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#TABLESWITCH}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>
     * {@code tableswitch}
     * <em>&lt;padding&gt;</em>,
     * <em>{@linkplain OperandType#BRANCH_OFFSET_INT default offset}</em>,
     * <em>{@linkplain OperandType#SIGNED_VALUE_INT low}</em>,
     * <em>{@linkplain OperandType#SIGNED_VALUE_INT high}</em>,
     * <em>
     * {@linkplain OperandType#JUMP_OFFSET_TABLE jump offsets}
     * </em>
     * </dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>index</em> &rarr; &hellip;</dd>
     * </dl>
     *
     * @see OpcodeValue#TABLESWITCH
     * @see OperandType#BRANCH_OFFSET_INT
     * @see OperandType#SIGNED_VALUE_INT
     * @see OperandType#JUMP_OFFSET_TABLE
     * @see #LOOKUPSWITCH
     */
    TABLESWITCH(OpcodeValue.TABLESWITCH, BRANCH_OFFSET_INT,
            SIGNED_VALUE_INT, SIGNED_VALUE_INT, JUMP_OFFSET_TABLE),
    /**
     * <p>
     * {@code wide} - Execute instruction with operands extended by the addition
     * of an extra byte
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#WIDE}</dd>
     * <dt><strong>Format 1</strong></dt>
     * <dd>
     * <p>
     * {@code wide}
     * <em>{@linkplain OperandType#OPCODE opcode}</em>,
     * <em>{@linkplain OperandType#LOCAL_VARIABLE_INDEX_SHORT index}</em>
     * </p>
     * <p>
     * where <em>opcode</em> is one of
     * {@link OneOperandOpcode#ALOAD aload};
     * {@link OneOperandOpcode#ASTORE astore};
     * {@link OneOperandOpcode#DLOAD dload};
     * {@link OneOperandOpcode#DSTORE dstore};
     * {@link OneOperandOpcode#FLOAD fload};
     * {@link OneOperandOpcode#FSTORE fstore};
     * {@link OneOperandOpcode#ILOAD iload};
     * {@link OneOperandOpcode#ISTORE istore};
     * {@link OneOperandOpcode#LLOAD lload}; or
     * {@link OneOperandOpcode#LSTORE lstore}
     * </p>
     * </dd>
     * <dt><strong>Format 2</strong></dt>
     * <dd>
     * {@code wide}
     * {@link TwoOperandOpcode#IINC iinc},
     * <em>{@linkplain OperandType#LOCAL_VARIABLE_INDEX_SHORT index}</em>,
     * <em>{@linkplain OperandType#SIGNED_VALUE_SHORT count}</em>
     * </dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>Same as modified instruction</dd>
     * </dl>
     *
     * @see OpcodeValue#WIDE
     * @see OperandType#OPCODE
     * @see OperandType#LOCAL_VARIABLE_INDEX_SHORT
     * @see OperandType#SIGNED_VALUE_SHORT
     * @see OneOperandOpcode#ALOAD
     * @see OneOperandOpcode#ASTORE
     * @see OneOperandOpcode#DLOAD
     * @see OneOperandOpcode#DSTORE
     * @see OneOperandOpcode#FLOAD
     * @see OneOperandOpcode#FSTORE
     * @see OneOperandOpcode#ILOAD
     * @see OneOperandOpcode#ISTORE
     * @see OneOperandOpcode#LLOAD
     * @see OneOperandOpcode#LSTORE
     * @see TwoOperandOpcode#IINC
     */
    WIDE(OpcodeValue.WIDE, OPCODE, LOCAL_VARIABLE_INDEX_SHORT,
            OPTIONAL_SIGNED_VALUE_SHORT);

    //
    // DATA
    //

    private final int value;
    private final List<OperandType> operandTypes;

    //
    // CONSTRUCTORS
    //

    VariableOperandOpcode(final int value, final OperandType... operandTypes) {
        this.value = value;
        this.operandTypes = List.of(operandTypes);
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
        return operandTypes.size();
    }

    @Override public boolean isPadded() {
        return WIDE != this;
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
     * Obtains the {@link VariableOperandOpcode} for the given opcode byte
     * value.
     *
     * @param value Opcode byte value
     * @return Opcode instance
     * @throws IllegalArgumentException If {@code value} is not in the range
     *                                  0..255 <em>or</em> the value is in the
     *                                  correct range but that value does not
     *                                  correspond to a known variable-operand
     *                                  opcode
     * @see OpcodeValue
     * @see Opcode#forUnsignedByte(int)
     * @see NoOperandOpcode#forUnsignedByte(int)
     * @see OneOperandOpcode#forUnsignedByte(int)
     * @see TwoOperandOpcode#forUnsignedByte(int)
     */
    public static VariableOperandOpcode forUnsignedByte(final int value) {
        switch (value) {
            case OpcodeValue.LOOKUPSWITCH:
                return LOOKUPSWITCH;
            case OpcodeValue.TABLESWITCH:
                return TABLESWITCH;
            case OpcodeValue.WIDE:
                return WIDE;
            default:
                final IllegalArgumentException e;
                if (0 <= value && value <= 255) {
                    e = noOpcodeForValue(VariableOperandOpcode.class, value);
                } else {
                    e = notAnUnsignedByte(value);
                }
                throw e;
        }
    }
}
