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

/**
 * <p>
 * Enumerates the categories of operands used by instructions in the Java
 * Virtual Machine instruction set.
 * </p>
 *
 * @author Victor Schappert
 * @since 20171126
 * @see Opcode#getOperandTypes()
 */
public enum OperandType {

    //
    // ENUMERATORS
    //

    /**
     * A signed 8-bit value (equivalent to the Java {@code byte} type). This
     * operand type is used by the {@link OneOperandOpcode#BIPUSH bipush} and
     * un-widened {@link TwoOperandOpcode#IINC iinc} instructions.
     *
     * @see #UNSIGNED_VALUE_BYTE
     * @see #SIGNED_VALUE_SHORT
     * @see #SIGNED_VALUE_INT
     * @see OneOperandOpcode#BIPUSH
     * @see TwoOperandOpcode#IINC
     */
    SIGNED_VALUE_BYTE(1),
    /**
     * An unsigned 8-bit value. This operand type is used by
     * {@link TwoOperandOpcode#INVOKEINTERFACE invokeinterface} and
     * {@link TwoOperandOpcode#MULTIANEWARRAY multianewarray} instructions.
     *
     * @see #SIGNED_VALUE_BYTE
     * @see #LOCAL_VARIABLE_INDEX_BYTE
     * @see TwoOperandOpcode#INVOKEINTERFACE
     * @see TwoOperandOpcode#MULTIANEWARRAY
     */
    UNSIGNED_VALUE_BYTE(1),
    /**
     * A signed 16-bit value (equivalent to the Java {@code short} type). This
     * operand type is used by the {@link OneOperandOpcode#SIPUSH sipush}
     * instruction.
     *
     * @see #SIGNED_VALUE_BYTE
     * @see #SIGNED_VALUE_INT
     * @see OneOperandOpcode#SIPUSH
     */
    SIGNED_VALUE_SHORT(2),
    /**
     * A signed 32-bit value (equivalent to the Java {@code int} type). This
     * operand type is used by the {@link VariableOperandOpcode#LOOKUPSWITCH
     * lookupswitch} and {@link VariableOperandOpcode#TABLESWITCH tableswitch}
     * instructions.
     *
     * @see #SIGNED_VALUE_BYTE
     * @see #SIGNED_VALUE_SHORT
     * @see VariableOperandOpcode#LOOKUPSWITCH
     * @see VariableOperandOpcode#TABLESWITCH
     */
    SIGNED_VALUE_INT(4),
    /**
     * A signed 16-bit value indicating a branch offset from the current
     * instruction's position. This operand type is used by
     * {@link OneOperandOpcode#GOTO goto} and {@link OneOperandOpcode#JSR jsr}
     * as well as all of the <i>if*</i> instructions:
     * {@link OneOperandOpcode#IF_ACMPEQ if_acmpeq};
     * {@linkplain OneOperandOpcode#IF_ACMPNE if_acmpne};
     * {@linkplain OneOperandOpcode#IF_ICMPEQ if_icmpeq};
     * {@linkplain OneOperandOpcode#IF_ICMPGE if_icmpge};
     * {@linkplain OneOperandOpcode#IF_ICMPGT if_icmpgt};
     * {@linkplain OneOperandOpcode#IF_ICMPLE if_icmple};
     * {@linkplain OneOperandOpcode#IF_ICMPLT if_icmplt};
     * {@linkplain OneOperandOpcode#IF_ICMPNE if_icmpne};
     * {@linkplain OneOperandOpcode#IFEQ ifeq};
     * {@linkplain OneOperandOpcode#IFGE ifge};
     * {@linkplain OneOperandOpcode#IFGT ifgt};
     * {@linkplain OneOperandOpcode#IFLE ifle};
     * {@linkplain OneOperandOpcode#IFLT iflt};
     * {@linkplain OneOperandOpcode#IFNE ifne};
     * {@linkplain OneOperandOpcode#IFNONNULL ifnonnull}; and
     * {@linkplain OneOperandOpcode#IFNULL ifnull}.
     *
     * @see #BRANCH_OFFSET_INT
     */
    BRANCH_OFFSET_SHORT(2),
    /**
     * A signed 32-bit value indicating a branch offset from the current
     * instruction's position. This operand type is used by the
     * {@link OneOperandOpcode#GOTO_W goto_w}, {@link OneOperandOpcode#JSR_W
     * jsr_w}, {@link VariableOperandOpcode#LOOKUPSWITCH lookupswitch}, and
     * {@link VariableOperandOpcode#TABLESWITCH tableswitch}.
     *
     * @see #BRANCH_OFFSET_SHORT
     * @see OneOperandOpcode#GOTO_W
     * @see OneOperandOpcode#JSR_W
     * @see VariableOperandOpcode#LOOKUPSWITCH
     * @see VariableOperandOpcode#TABLESWITCH
     */
    BRANCH_OFFSET_INT(4),
    /**
     * An unsigned 8-bit value for use as an index into the local variable
     * table for the current frame. This operand type is used by
     * the un-widened {@link OneOperandOpcode#RET ret} and {@link
     * TwoOperandOpcode#IINC iinc} instruction as well as the un-widened
     * versions of the single-operand load and store instructions:
     * {@link OneOperandOpcode#ALOAD aload};
     * {@link OneOperandOpcode#ASTORE astore};
     * {@link OneOperandOpcode#DLOAD dload};
     * {@link OneOperandOpcode#DSTORE dstore};
     * {@link OneOperandOpcode#FLOAD fload};
     * {@link OneOperandOpcode#FSTORE fstore};
     * {@link OneOperandOpcode#ILOAD iload};
     * {@link OneOperandOpcode#ISTORE istore};
     * {@link OneOperandOpcode#LLOAD lload}; and
     * {@link OneOperandOpcode#LSTORE lstore}.
     *
     * @see #LOCAL_VARIABLE_INDEX_SHORT
     * @see #UNSIGNED_VALUE_BYTE
     */
    LOCAL_VARIABLE_INDEX_BYTE(1),
    /**
     * An unsigned 16-bit value for use as an index into the local variable
     * array for the current frame. This operand type is used by the
     * {@link VariableOperandOpcode#WIDE wide} instruction when used to widen
     * the local variable index range for the {@link OneOperandOpcode#RET ret}
     * instruction; the {@link TwoOperandOpcode#IINC iinc} instruction; and the
     * single-operand load and store operations.
     *
     * @see #LOCAL_VARIABLE_INDEX_BYTE
     * @see VariableOperandOpcode#WIDE
     */
    LOCAL_VARIABLE_INDEX_SHORT(2),
    /**
     * <p>
     * An unsigned 8-bit value for use as an index into the runtime constant
     * pool. This operand type is used by the {@link OneOperandOpcode#LDC ldc}
     * instruction.
     * </p>
     *
     * <p>
     * An operand of this type must be a valid index to an entry in the runtime
     * constant pool having one of the following types:
     * </p>
     *
     * <ul>
     * <li>{@code float};</li>
     * <li>{@code int};</li>
     * <li>{@code reference} to a string literal;</li>
     * <li>symbolic reference to a class;</li>
     * <li>symbolic reference to a method type; or</li>
     * <li>symbolic reference to a method handle.</li>
     * </ul>
     *
     * @see #CONSTANT_POOL_INDEX_SHORT
     * @see OneOperandOpcode#LDC
     */
    CONSTANT_POOL_INDEX_BYTE(1),
    /**
     * <p>
     * An unsigned 16-bit value for use as an index into the runtime constant
     * pool. This operand type is used by all instructions that refer to a
     * runtime constant pool entry, except {@link OneOperandOpcode#LDC ldc}.
     * </p>
     *
     * <p>
     * In particular, this operand type is used by the following instructions:
     * </p>
     *
     * <ul>
     * <li>{@link OneOperandOpcode#ANEWARRAY anewarray}</li>
     * <li>{@link OneOperandOpcode#CHECKCAST checkcast}</li>
     * <li>{@link OneOperandOpcode#GETFIELD getfield}</li>
     * <li>{@link OneOperandOpcode#GETSTATIC getstatic}</li>
     * <li>{@link OneOperandOpcode#INSTANCEOF instanceof}</li>
     * <li>{@link OneOperandOpcode#INVOKEDYNAMIC invokedynamic}</li>
     * <li>{@link TwoOperandOpcode#INVOKEINTERFACE invokeinterface}</li>
     * <li>{@link OneOperandOpcode#INVOKESPECIAL invokespecial}</li>
     * <li>{@link OneOperandOpcode#INVOKESTATIC invokestatic}</li>
     * <li>{@link OneOperandOpcode#INVOKEVIRTUAL invokevirtual}</li>
     * <li>{@link OneOperandOpcode#LDC_W ldc_w}</li>
     * <li>{@link OneOperandOpcode#LDC2_W ldc2_w}</li>
     * <li>{@link TwoOperandOpcode#MULTIANEWARRAY multianewarray}</li>
     * <li>{@link OneOperandOpcode#NEW new}</li>
     * <li>{@link OneOperandOpcode#PUTFIELD putfield}</li>
     * <li>{@link OneOperandOpcode#PUTSTATIC putstatic}</li>
     * </ul>
     *
     * @see #CONSTANT_POOL_INDEX_BYTE
     */
    CONSTANT_POOL_INDEX_SHORT(2),
    /**
     * <p>
     * An unsigned 8-bit value that is a valid <em>atype</em> value. This
     * operand type is used by the {@link OneOperandOpcode#NEWARRAY newarray}
     * instruction.
     * </p>
     *
     * <p>
     * The valid <em>atype</em> values are defined as constants in the
     * {@link ATypeValue} class.
     * </p>
     *
     * @see OneOperandOpcode#NEWARRAY
     */
    ATYPE_BYTE(1),
    /**
     * <p>
     * An unsigned 8-bit value that is a instruction opcode. This operand type
     * is used by the {@link VariableOperandOpcode#WIDE wide} instruction.
     * </p>
     *
     * <p>
     * This operand type may only take on a limited subset of the opcode values.
     * In particular, the following are valid values for an operand of this
     * type:
     * </p>
     *
     * <table>
     * <caption>
     * Valid opcodes embeddable within a {@code wide} instruction
     * </caption>
     * <tr>
     * <th>Opcode</th>
     * <th>Mnemonic</th>
     * </tr>
     * <tr>
     * <td>{@value io.tarro.base.bytecode.OpcodeValue#ILOAD}</td>
     * <td>{@link OneOperandOpcode#ILOAD iload}</td>
     * </tr>
     * <tr>
     * <td>{@value io.tarro.base.bytecode.OpcodeValue#LLOAD}</td>
     * <td>{@link OneOperandOpcode#LLOAD lload}</td>
     * </tr>
     * <tr>
     * <td>{@value io.tarro.base.bytecode.OpcodeValue#FLOAD}</td>
     * <td>{@link OneOperandOpcode#FLOAD fload}</td>
     * </tr>
     * <tr>
     * <td>{@value io.tarro.base.bytecode.OpcodeValue#DLOAD}</td>
     * <td>{@link OneOperandOpcode#DLOAD dload}</td>
     * </tr>
     * <tr>
     * <td>{@value io.tarro.base.bytecode.OpcodeValue#ALOAD}</td>
     * <td>{@link OneOperandOpcode#ALOAD aload}</td>
     * </tr>
     * <tr>
     * <td>{@value io.tarro.base.bytecode.OpcodeValue#ISTORE}</td>
     * <td>{@link OneOperandOpcode#ISTORE istore}</td>
     * </tr>
     * <tr>
     * <td>{@value io.tarro.base.bytecode.OpcodeValue#LSTORE}</td>
     * <td>{@link OneOperandOpcode#LSTORE lstore}</td>
     * </tr>
     * <tr>
     * <td>{@value io.tarro.base.bytecode.OpcodeValue#FSTORE}</td>
     * <td>{@link OneOperandOpcode#FSTORE fstore}</td>
     * </tr>
     * <tr>
     * <td>{@value io.tarro.base.bytecode.OpcodeValue#DSTORE}</td>
     * <td>{@link OneOperandOpcode#DSTORE dstore}</td>
     * </tr>
     * <tr>
     * <td>{@value io.tarro.base.bytecode.OpcodeValue#ASTORE}</td>
     * <td>{@link OneOperandOpcode#ASTORE astore}</td>
     * </tr>
     * <tr>
     * <td>{@value io.tarro.base.bytecode.OpcodeValue#IINC}</td>
     * <td>{@link TwoOperandOpcode#IINC iinc}</td>
     * </tr>
     * </table>
     *
     * @see VariableOperandOpcode#WIDE
     */
    OPCODE(1),
    /**
     * <p>
     * A variable-length array of 64-bit match-offset pairs.
     *
     * <p>
     * Each match-offset pair is an 8-byte quantity consisting of a 32-bit
     * signed ({@code int}) <em>match</em> value followed by a 32-bit signed
     * ({@code int}) branch offset to be applied relative to the current
     * instruction's position.
     * </p>
     *
     * <p>
     * This operand type is used by the {@link
     * VariableOperandOpcode#LOOKUPSWITCH lookupswitch} instruction. The number
     * of match-offset pairs in the table is given by the instruction's
     * <em>npairs</em> operand, a {@link #SIGNED_VALUE_INT signed 32-bit value}.
     * </p>
     *
     * @see VariableOperandOpcode#LOOKUPSWITCH
     * @see #BRANCH_OFFSET_INT
     * @see #SIGNED_VALUE_INT
     * @see #JUMP_OFFSET_TABLE
     */
    MATCH_OFFSET_PAIR_TABLE,
    /**
     * <p>
     * A variable-length array of 32-bit signed values indicating branch offsets
     * from the current instruction's position.
     * </p>
     *
     * <p>
     * This operand type is used by the
     * {@link VariableOperandOpcode#TABLESWITCH tableswitch} instruction. The
     * number of branch offsets in the table is the size of the range given by
     * the instruction's <em>low</em> and <em>high</em> operands.
     * </p>
     *
     * @see VariableOperandOpcode#TABLESWITCH
     * @see #BRANCH_OFFSET_INT
     * @see #MATCH_OFFSET_PAIR_TABLE
     */
    JUMP_OFFSET_TABLE,
    /**
     * <p>
     * A signed 16-bit value optionally included as an operand in a
     * variable-length instruction. This operand type is used by the {@link
     * VariableOperandOpcode#WIDE wide} instruction when it is used to widen the
     * {@link TwoOperandOpcode#IINC iinc} instruction.
     * </p>
     *
     * @see VariableOperandOpcode#WIDE
     * @see TwoOperandOpcode#IINC
     */
    OPTIONAL_SIGNED_VALUE_SHORT;

    //
    // DATA
    //

    private final int size;

    //
    // CONSTRUCTORS
    //

    OperandType(final int size) {
        this.size = size;
    }

    OperandType() {
        this(VARIABLE_SIZE);
    }

    //
    // PUBLIC METHODS
    //

    /**
     * <p>
     * Obtains the size of the operand in bytes for fixed-size operands types.
     * </p>
     *
     * <p>
     * For fixed-size operand types, the value is a positive power of two,
     * specifically 1, 2, or 4. For variable-length operand types, the result is
     * {@link #VARIABLE_SIZE}.
     * </p>
     *
     *
     * @return Size of the operand in bytes or {@link #VARIABLE_SIZE}
     * @see #VARIABLE_SIZE
     */
    public int getSize() {
        return size;
    }

    /**
     * <p>
     * Indicates whether the operand type can have variable size.
     * </p>
     *
     * <p>
     * This method returns {@code true} if {@link #getSize()} returns
     * {@link #VARIABLE_SIZE}; and {@code false} otherwise.
     * </p>
     *
     * @return Whether this operand type has variable size
     * @see #getSize()
     */
    public boolean isVariableSize() {
        return VARIABLE_SIZE == size;
    }

    //
    // PUBLIC CONSTANTS
    //

    /**
     * Constant returned by {@link #getSize()} to indicate that operands of this
     * type can vary in length depending on the value of the instruction's other
     * operands.
     */
    public static final int VARIABLE_SIZE = -1;
}
