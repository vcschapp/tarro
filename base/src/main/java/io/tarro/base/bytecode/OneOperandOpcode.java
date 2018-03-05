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

import io.tarro.base.ClassFileVersion;

import java.util.List;
import java.util.Optional;

import static io.tarro.base.ClassFileVersion.JAVA1;
import static io.tarro.base.ClassFileVersion.JAVA6;
import static io.tarro.base.ClassFileVersion.JAVA7;
import static io.tarro.base.bytecode.OpcodeTable.forOpcodeEnum;
import static io.tarro.base.bytecode.OperandType.ATYPE_BYTE;
import static io.tarro.base.bytecode.OperandType.BRANCH_OFFSET_INT;
import static io.tarro.base.bytecode.OperandType.BRANCH_OFFSET_SHORT;
import static io.tarro.base.bytecode.OperandType.CONSTANT_POOL_INDEX_BYTE;
import static io.tarro.base.bytecode.OperandType.CONSTANT_POOL_INDEX_SHORT;
import static io.tarro.base.bytecode.OperandType.LOCAL_VARIABLE_INDEX_BYTE;
import static io.tarro.base.bytecode.OperandType.SIGNED_VALUE_BYTE;
import static io.tarro.base.bytecode.OperandType.SIGNED_VALUE_SHORT;
import static java.util.Collections.singletonList;
import static java.util.Optional.empty;
import static java.util.Optional.of;

/**
 * <p>
 * Enumerates opcodes for Java Virtual Machine instructions which have a
 * single operand embedded in the instruction.
 * </p>
 *
 * <p>
 * Note that even if the instruction embeds only a single operand, it may and
 * frequently does consume one or more additional operands from the operand
 * stack.
 * </p>
 *
 * @author Victor Schappert
 * @since 20171218
 * @see NoOperandOpcode
 * @see TwoOperandOpcode
 * @see VariableOperandOpcode
 * @see OpcodeValue
 */
public enum OneOperandOpcode implements Opcode {

    //
    // ENUMERATORS
    //

    // Keep this list in alphabetical order by opcode mnemonic.
    /**
     * <p>
     * {@code aload} - Load {@code reference} from local variable
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value OpcodeValue#ALOAD}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>
     * {@code aload}
     * <em>{@linkplain OperandType#LOCAL_VARIABLE_INDEX_BYTE index}</em>
     * </dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip; &rarr; &hellip;, <em>objectref</em></dd>
     * </dl>
     *
     * @see OpcodeValue#ALOAD
     * @see OperandType#LOCAL_VARIABLE_INDEX_BYTE
     * @see #ASTORE
     * @see #DLOAD
     * @see #FLOAD
     * @see #ILOAD
     * @see #LLOAD
     * @see NoOperandOpcode#ALOAD_0
     * @see NoOperandOpcode#ALOAD_1
     * @see NoOperandOpcode#ALOAD_2
     * @see NoOperandOpcode#ALOAD_3
     */
    ALOAD(OpcodeValue.ALOAD, LOCAL_VARIABLE_INDEX_BYTE),
    /**
     * <p>
     * {@code anewarray} - Create new array of {@code reference}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value OpcodeValue#ANEWARRAY}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>
     * {@code anewarray}
     * <em>{@linkplain OperandType#CONSTANT_POOL_INDEX_SHORT index}</em>
     * </dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>count</em> &rarr; &hellip;, <em>arrayref</em></dd>
     * </dl>
     *
     * @see OpcodeValue#ANEWARRAY
     * @see OperandType#CONSTANT_POOL_INDEX_SHORT
     * @see TwoOperandOpcode#MULTIANEWARRAY
     * @see #NEW
     * @see #NEWARRAY
     */
    ANEWARRAY(OpcodeValue.ANEWARRAY, CONSTANT_POOL_INDEX_SHORT),
    /**
     * <p>
     * {@code astore} - Store {@code reference} into local variable
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value OpcodeValue#ASTORE}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>
     * {@code astore}
     * <em>{@linkplain OperandType#LOCAL_VARIABLE_INDEX_BYTE index}
     * </em>
     * </dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>objectref</em> &rarr; &hellip;</dd>
     * </dl>
     *
     * @see OpcodeValue#ASTORE
     * @see OperandType#LOCAL_VARIABLE_INDEX_BYTE
     * @see #ALOAD
     * @see #DSTORE
     * @see #FSTORE
     * @see #ISTORE
     * @see #LSTORE
     * @see NoOperandOpcode#ASTORE_0
     * @see NoOperandOpcode#ASTORE_1
     * @see NoOperandOpcode#ASTORE_2
     * @see NoOperandOpcode#ASTORE_3
     */
    ASTORE(OpcodeValue.ASTORE, LOCAL_VARIABLE_INDEX_BYTE),
    /**
     * <p>
     * {@code bipush} - Push {@code byte}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value OpcodeValue#BIPUSH}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>
     * {@code bipush}
     * <em>{@linkplain OperandType#SIGNED_VALUE_BYTE value}</em>
     * </dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip; &rarr; &hellip;, <em>value</em></dd>
     * </dl>
     *
     * @see OpcodeValue#BIPUSH
     * @see OperandType#SIGNED_VALUE_BYTE
     * @see #SIPUSH
     * @see NoOperandOpcode#ICONST_M1
     * @see NoOperandOpcode#ICONST_0
     * @see NoOperandOpcode#ICONST_1
     * @see NoOperandOpcode#ICONST_2
     * @see NoOperandOpcode#ICONST_3
     * @see NoOperandOpcode#ICONST_4
     * @see NoOperandOpcode#ICONST_5
     */
    BIPUSH(OpcodeValue.BIPUSH, SIGNED_VALUE_BYTE),
    /**
     * <p>
     * {@code checkcast} - Check whether object is of given type
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value OpcodeValue#CHECKCAST}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>
     * {@code checkcast}
     * <em>{@linkplain OperandType#CONSTANT_POOL_INDEX_SHORT index}</em>
     * </dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>objectref</em> &rarr; &hellip;, <em>objectref</em></dd>
     * </dl>
     *
     * @see OpcodeValue#CHECKCAST
     * @see OperandType#CONSTANT_POOL_INDEX_SHORT
     * @see #INSTANCEOF
     */
    CHECKCAST(OpcodeValue.CHECKCAST, CONSTANT_POOL_INDEX_SHORT),
    /**
     * <p>
     * {@code dload} - Load {@code double} from local variable
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value OpcodeValue#DLOAD}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>
     * {@code dload}
     * <em>{@linkplain OperandType#LOCAL_VARIABLE_INDEX_BYTE index}
     * </em>
     * </dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip; &rarr; &hellip;, <em>value</em></dd>
     * </dl>
     *
     * @see OpcodeValue#DLOAD
     * @see OperandType#LOCAL_VARIABLE_INDEX_BYTE
     * @see #DSTORE
     * @see #ALOAD
     * @see #FLOAD
     * @see #ILOAD
     * @see #LLOAD
     * @see NoOperandOpcode#DLOAD_0
     * @see NoOperandOpcode#DLOAD_1
     * @see NoOperandOpcode#DLOAD_2
     * @see NoOperandOpcode#DLOAD_3
     */
    DLOAD(OpcodeValue.DLOAD, LOCAL_VARIABLE_INDEX_BYTE),
    /**
     * <p>
     * {@code dstore} - Store {@code double} into local variable
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value OpcodeValue#DSTORE}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>
     * {@code dstore}
     * <em>{@linkplain OperandType#LOCAL_VARIABLE_INDEX_BYTE index}</em>
     * </dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value</em> &rarr; &hellip;</dd>
     * </dl>
     *
     * @see OpcodeValue#DSTORE
     * @see OperandType#LOCAL_VARIABLE_INDEX_BYTE
     * @see #DLOAD
     * @see #ASTORE
     * @see #FSTORE
     * @see #ISTORE
     * @see #LSTORE
     * @see NoOperandOpcode#DSTORE_0
     * @see NoOperandOpcode#DSTORE_1
     * @see NoOperandOpcode#DSTORE_2
     * @see NoOperandOpcode#DSTORE_3
     */
    DSTORE(OpcodeValue.DSTORE, LOCAL_VARIABLE_INDEX_BYTE),
    /**
     * <p>
     * {@code fload} - Load {@code float} from local variable
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value OpcodeValue#FLOAD}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>
     * {@code fload}
     * <em>{@linkplain OperandType#LOCAL_VARIABLE_INDEX_BYTE index}</em>
     * </dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip; &rarr; &hellip;, <em>value</em></dd>
     * </dl>
     *
     * @see OpcodeValue#FLOAD
     * @see OperandType#LOCAL_VARIABLE_INDEX_BYTE
     * @see #FSTORE
     * @see #ALOAD
     * @see #DLOAD
     * @see #ILOAD
     * @see #LLOAD
     * @see NoOperandOpcode#FLOAD_0
     * @see NoOperandOpcode#FLOAD_1
     * @see NoOperandOpcode#FLOAD_2
     * @see NoOperandOpcode#FLOAD_3
     */
    FLOAD(OpcodeValue.FLOAD, LOCAL_VARIABLE_INDEX_BYTE),
    /**
     * <p>
     * {@code fstore} - Store {@code float into local variable}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value OpcodeValue#FSTORE}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>
     * {@code fstore}
     * <em>{@linkplain OperandType#LOCAL_VARIABLE_INDEX_BYTE index}</em>
     * </dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value</em> &rarr; &hellip;</dd>
     * </dl>
     *
     * @see OpcodeValue#FSTORE
     * @see OperandType#LOCAL_VARIABLE_INDEX_BYTE
     * @see #FLOAD
     * @see #ASTORE
     * @see #DSTORE
     * @see #ISTORE
     * @see #LSTORE
     * @see NoOperandOpcode#FSTORE_0
     * @see NoOperandOpcode#FSTORE_1
     * @see NoOperandOpcode#FSTORE_2
     * @see NoOperandOpcode#FSTORE_3
     */
    FSTORE(OpcodeValue.FSTORE, LOCAL_VARIABLE_INDEX_BYTE),
    /**
     * <p>
     * {@code getfield} - Fetch field from object
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value OpcodeValue#GETFIELD}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>
     * {@code getfield}
     * <em>{@linkplain OperandType#CONSTANT_POOL_INDEX_SHORT index}</em>
     * </dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>objectref</em> &rarr; &hellip;, <em>value</em></dd>
     * </dl>
     *
     * @see OpcodeValue#GETFIELD
     * @see OperandType#CONSTANT_POOL_INDEX_SHORT
     * @see #PUTFIELD
     * @see #GETSTATIC
     * @see NoOperandOpcode#ARRAYLENGTH
     */
    GETFIELD(OpcodeValue.GETFIELD, CONSTANT_POOL_INDEX_SHORT),
    /**
     * <p>
     * {@code getstatic} - Get {@code static} field from class
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value OpcodeValue#GETSTATIC}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>
     * {@code getstatic}
     * <em>{@linkplain OperandType#CONSTANT_POOL_INDEX_SHORT index}</em>
     * </dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip; &rarr; &hellip;, <em>value</em></dd>
     * </dl>
     *
     * @see OpcodeValue#GETSTATIC
     * @see OperandType#CONSTANT_POOL_INDEX_SHORT
     * @see #PUTSTATIC
     * @see #GETFIELD
     */
    GETSTATIC(OpcodeValue.GETSTATIC, CONSTANT_POOL_INDEX_SHORT),
    /**
     * <p>
     * {@code goto} - Branch always
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value OpcodeValue#GOTO}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>
     * {@code goto} <em>{@linkplain OperandType#BRANCH_OFFSET_SHORT offset}</em>
     * </dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip; &rarr; &hellip;</dd>
     * </dl>
     *
     * @see OpcodeValue#GOTO
     * @see OperandType#BRANCH_OFFSET_SHORT
     * @see #GOTO_W
     * @see #JSR
     */
    GOTO(OpcodeValue.GOTO, BRANCH_OFFSET_SHORT),
    /**
     * <p>
     * {@code goto_w} - Branch always (wide offset)
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value OpcodeValue#GOTO_W}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>
     * {@code goto_w} <em>{@linkplain OperandType#BRANCH_OFFSET_INT offset}</em>
     * </dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip; &rarr; &hellip;</dd>
     * </dl>
     *
     * @see OpcodeValue#GOTO_W
     * @see OperandType#BRANCH_OFFSET_INT
     * @see #GOTO
     * @see #JSR_W
     */
    GOTO_W(OpcodeValue.GOTO_W, BRANCH_OFFSET_INT),
    /**
     * <p>
     * {@code if_acmpeq} - Branch if {@code reference} values are equal
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value OpcodeValue#IF_ACMPEQ}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>
     * {@code if_acmpeq}
     * <em>{@linkplain OperandType#BRANCH_OFFSET_SHORT offset}</em>
     * </dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value1</em>, <em>value2</em> &rarr; &hellip;</dd>
     * </dl>
     *
     * @see OpcodeValue#IF_ACMPEQ
     * @see OperandType#BRANCH_OFFSET_SHORT
     * @see #IF_ACMPNE
     * @see #IFNONNULL
     * @see #IFNULL
     * @see #IF_ICMPEQ
     * @see #IFEQ
     * @see #GOTO
     */
    IF_ACMPEQ(OpcodeValue.IF_ACMPEQ, BRANCH_OFFSET_SHORT),
    /**
     * <p>
     * {@code if_acmpne} - Branch if {@code reference} values are not equal
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value OpcodeValue#IF_ACMPNE}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>
     * {@code if_acmpne}
     * <em>{@linkplain OperandType#BRANCH_OFFSET_SHORT offset}</em>
     * </dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value1</em>, <em>value2</em> &rarr; &hellip;</dd>
     * </dl>
     *
     * @see OpcodeValue#IF_ACMPNE
     * @see OperandType#BRANCH_OFFSET_SHORT
     * @see #IF_ACMPEQ
     * @see #IFNONNULL
     * @see #IFNULL
     * @see #IF_ICMPNE
     * @see #IFNE
     * @see #GOTO
     */
    IF_ACMPNE(OpcodeValue.IF_ACMPNE, BRANCH_OFFSET_SHORT),
    /**
     * <p>
     * {@code if_icmpeq} - Branch if {@code int} values are equal
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value OpcodeValue#IF_ICMPEQ}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>
     * {@code if_icmpeq}
     * <em>{@linkplain OperandType#BRANCH_OFFSET_SHORT offset}</em>
     * </dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value1</em>, <em>value2</em> &rarr; &hellip;</dd>
     * </dl>
     *
     * @see OpcodeValue#IF_ICMPEQ
     * @see OperandType#BRANCH_OFFSET_SHORT
     * @see #IF_ICMPLT
     * @see #IF_ICMPLE
     * @see #IF_ICMPNE
     * @see #IF_ICMPGE
     * @see #IF_ICMPGT
     * @see #IF_ACMPEQ
     * @see #IFEQ
     * @see #GOTO
     */
    IF_ICMPEQ(OpcodeValue.IF_ICMPEQ, BRANCH_OFFSET_SHORT),
    /**
     * <p>
     * {@code if_icmpge} - Branch if first {@code int} greater than or equal to
     * second {@code int}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value OpcodeValue#IF_ICMPGE}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>
     * {@code if_icmpge}
     * <em>{@linkplain OperandType#BRANCH_OFFSET_SHORT offset}</em>
     * </dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value1</em>, <em>value2</em> &rarr; &hellip;</dd>
     * </dl>
     *
     * @see OpcodeValue#IF_ICMPGE
     * @see OperandType#BRANCH_OFFSET_SHORT
     * @see #IF_ICMPLT
     * @see #IF_ICMPLE
     * @see #IF_ICMPEQ
     * @see #IF_ICMPNE
     * @see #IF_ICMPGT
     * @see #IFGE
     * @see #GOTO
     */
    IF_ICMPGE(OpcodeValue.IF_ICMPGE, BRANCH_OFFSET_SHORT),
    /**
     * <p>
     * {@code if_icmpgt} - Branch if first {@code int} greater than second
     * {@code int}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value OpcodeValue#IF_ICMPGT}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>
     * {@code if_icmpgt}
     * <em>{@linkplain OperandType#BRANCH_OFFSET_SHORT offset}</em>
     * </dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value1</em>, <em>value2</em> &rarr; &hellip;</dd>
     * </dl>
     *
     * @see OpcodeValue#IF_ICMPGT
     * @see OperandType#BRANCH_OFFSET_SHORT
     * @see #IF_ICMPLT
     * @see #IF_ICMPLE
     * @see #IF_ICMPEQ
     * @see #IF_ICMPNE
     * @see #IF_ICMPGE
     * @see #IFGT
     * @see #GOTO
     */
    IF_ICMPGT(OpcodeValue.IF_ICMPGT, BRANCH_OFFSET_SHORT),
    /**
     * <p>
     * {@code if_icmple} - Branch if first {@code int} less than or equal to
     * second {@code int}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value OpcodeValue#IF_ICMPLE}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>
     * {@code if_icmple}
     * <em>{@linkplain OperandType#BRANCH_OFFSET_SHORT offset}</em>
     * </dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value1</em>, <em>value2</em> &rarr; &hellip;</dd>
     * </dl>
     *
     * @see OpcodeValue#IF_ICMPLE
     * @see OperandType#BRANCH_OFFSET_SHORT
     * @see #IF_ICMPLT
     * @see #IF_ICMPEQ
     * @see #IF_ICMPNE
     * @see #IF_ICMPGE
     * @see #IF_ICMPGT
     * @see #IFLE
     * @see #GOTO
     */
    IF_ICMPLE(OpcodeValue.IF_ICMPLE, BRANCH_OFFSET_SHORT),
    /**
     * <p>
     * {@code if_icmplt} - Branch if first {@code int} less than second
     * {@code int}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value OpcodeValue#IF_ICMPLT}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>
     * {@code if_icmplt}
     * <em>{@linkplain OperandType#BRANCH_OFFSET_SHORT offset}</em>
     * </dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value1</em>, <em>value2</em> &rarr; &hellip;</dd>
     * </dl>
     *
     * @see OpcodeValue#IF_ICMPLT
     * @see OperandType#BRANCH_OFFSET_SHORT
     * @see #IF_ICMPLE
     * @see #IF_ICMPEQ
     * @see #IF_ICMPGE
     * @see #IF_ICMPGT
     * @see #IF_ICMPNE
     * @see #IFLT
     * @see #GOTO
     */
    IF_ICMPLT(OpcodeValue.IF_ICMPLT, BRANCH_OFFSET_SHORT),
    /**
     * <p>
     * {@code if_icmpne} - Branch if {@code int} values are not equal
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value OpcodeValue#IF_ICMPNE}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>
     * {@code if_icmpne}
     * <em>{@linkplain OperandType#BRANCH_OFFSET_SHORT offset}</em>
     * </dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value1</em>, <em>value2</em> &rarr; &hellip;</dd>
     * </dl>
     *
     * @see OpcodeValue#IF_ICMPNE
     * @see OperandType#BRANCH_OFFSET_SHORT
     * @see #IF_ICMPLT
     * @see #IF_ICMPLE
     * @see #IF_ICMPEQ
     * @see #IF_ICMPGE
     * @see #IF_ICMPGT
     * @see #IF_ACMPNE
     * @see #IFNE
     * @see #GOTO
     */
    IF_ICMPNE(OpcodeValue.IF_ICMPNE, BRANCH_OFFSET_SHORT),
    /**
     * <p>
     * {@code ifeq} - Branch if {@code int} equals zero
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value OpcodeValue#IFEQ}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>
     * {@code ifeq}
     * <em>{@linkplain OperandType#BRANCH_OFFSET_SHORT offset}</em>
    * </dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value</em> &rarr; &hellip;</dd>
     * </dl>
     *
     * @see OpcodeValue#IFEQ
     * @see OperandType#BRANCH_OFFSET_SHORT
     * @see #IFLT
     * @see #IFLE
     * @see #IFNE
     * @see #IFGE
     * @see #IFGT
     * @see #IF_ICMPEQ
     * @see #GOTO
     */
    IFEQ(OpcodeValue.IFEQ, BRANCH_OFFSET_SHORT),
    /**
     * <p>
     * {@code ifge} - Branch if {@code int} greater than or equal to zero
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value OpcodeValue#IFGE}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>
     * {@code ifge}
     * <em>{@linkplain OperandType#BRANCH_OFFSET_SHORT offset}</em>
     * </dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value</em> &rarr; &hellip;</dd>
     * </dl>
     *
     * @see OpcodeValue#IFGE
     * @see OperandType#BRANCH_OFFSET_SHORT
     * @see #IFLT
     * @see #IFLE
     * @see #IFEQ
     * @see #IFNE
     * @see #IFGT
     * @see #IF_ICMPGE
     * @see #GOTO
     */
    IFGE(OpcodeValue.IFGE, BRANCH_OFFSET_SHORT),
    /**
     * <p>
     * {@code ifgt} - Branch if {@code int} greater than zero
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value OpcodeValue#IFGT}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>
     * {@code ifgt}
     * <em>{@linkplain OperandType#BRANCH_OFFSET_SHORT offset}</em>
     * </dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value</em> &rarr; &hellip;</dd>
     * </dl>
     *
     * @see OpcodeValue#IFGT
     * @see OperandType#BRANCH_OFFSET_SHORT
     * @see #IFLT
     * @see #IFLE
     * @see #IFEQ
     * @see #IFNE
     * @see #IFGE
     * @see #IF_ICMPGT
     * @see #GOTO
     */
    IFGT(OpcodeValue.IFGT, BRANCH_OFFSET_SHORT),
    /**
     * <p>
     * {@code ifle} - Branch if {@code int} less than or equal to zero
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value OpcodeValue#IFLE}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>
     * {@code ifle}
     * <em>{@linkplain OperandType#BRANCH_OFFSET_SHORT offset}</em>
     * </dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value</em> &rarr; &hellip;</dd>
     * </dl>
     *
     * @see OpcodeValue#IFLE
     * @see OperandType#BRANCH_OFFSET_SHORT
     * @see #IFLT
     * @see #IFEQ
     * @see #IFNE
     * @see #IFGE
     * @see #IFGT
     * @see #IF_ICMPLE
     * @see #GOTO
     */
    IFLE(OpcodeValue.IFLE, BRANCH_OFFSET_SHORT),
    /**
     * <p>
     * {@code iflt} - Branch if {@code int} less than zero
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value OpcodeValue#IFLT}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>
     * {@code iflt}
     * <em>{@linkplain OperandType#BRANCH_OFFSET_SHORT offset}</em>
     * </dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value</em> &rarr; &hellip;</dd>
     * </dl>
     *
     * @see OpcodeValue#IFLT
     * @see OperandType#BRANCH_OFFSET_SHORT
     * @see #IFLE
     * @see #IFEQ
     * @see #IFNE
     * @see #IFGE
     * @see #IFGT
     * @see #IF_ICMPLT
     * @see #GOTO
     */
    IFLT(OpcodeValue.IFLT, BRANCH_OFFSET_SHORT),
    /**
     * <p>
     * {@code ifne} - Branch if {@code int} not equal to zero
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value OpcodeValue#IFNE}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>
     * {@code ifne}
     * <em>{@linkplain OperandType#BRANCH_OFFSET_SHORT offset}</em>
     * </dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value</em> &rarr; &hellip;</dd>
     * </dl>
     *
     * @see OpcodeValue#IFNE
     * @see OperandType#BRANCH_OFFSET_SHORT
     * @see #IFLT
     * @see #IFLE
     * @see #IFEQ
     * @see #IFGE
     * @see #IFGT
     * @see #IF_ICMPNE
     * @see #GOTO
     */
    IFNE(OpcodeValue.IFNE, BRANCH_OFFSET_SHORT),
    /**
     * <p>
     * {@code ifnonnull} - Branch if {@code reference} not {@code null}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value OpcodeValue#IFNONNULL}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>
     * {@code ifnonnull}
     * <em>{@linkplain OperandType#BRANCH_OFFSET_SHORT offset}</em>
     * </dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value</em> &rarr; &hellip;</dd>
     * </dl>
     *
     * @see OpcodeValue#IFNONNULL
     * @see OperandType#BRANCH_OFFSET_SHORT
     * @see #IFNULL
     * @see #IF_ACMPEQ
     * @see #IF_ACMPNE
     * @see #GOTO
     */
    IFNONNULL(OpcodeValue.IFNONNULL, BRANCH_OFFSET_SHORT),
    /**
     * <p>
     * {@code ifnull} - Branch if {@code reference} is {@code null}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value OpcodeValue#IFNULL}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>
     * {@code ifnull}
     * <em>{@linkplain OperandType#BRANCH_OFFSET_SHORT offset}</em>
     * </dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value</em> &rarr; &hellip;</dd>
     * </dl>
     *
     * @see OpcodeValue#IFNULL
     * @see OperandType#BRANCH_OFFSET_SHORT
     * @see #IFNONNULL
     * @see #IF_ACMPEQ
     * @see #IF_ACMPNE
     * @see #GOTO
     */
    IFNULL(OpcodeValue.IFNULL, BRANCH_OFFSET_SHORT),
    /**
     * <p>
     * {@code iload} - Load {@code int} from local variable
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value OpcodeValue#ILOAD}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>
     * {@code iload}
     * <em>{@linkplain OperandType#LOCAL_VARIABLE_INDEX_BYTE index}</em>
     * </dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip; &rarr; &hellip;, <em>value</em></dd>
     * </dl>
     *
     * @see OpcodeValue#ILOAD
     * @see OperandType#LOCAL_VARIABLE_INDEX_BYTE
     * @see #ISTORE
     * @see #ALOAD
     * @see #DLOAD
     * @see #FLOAD
     * @see #LLOAD
     * @see NoOperandOpcode#ILOAD_0
     * @see NoOperandOpcode#ILOAD_1
     * @see NoOperandOpcode#ILOAD_2
     * @see NoOperandOpcode#ILOAD_3
     */
    ILOAD(OpcodeValue.ILOAD, LOCAL_VARIABLE_INDEX_BYTE),
    /**
     * <p>
     * {@code instanceof} - Determine if object is of given type
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value OpcodeValue#INSTANCEOF}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>
     * {@code instanceof}
     * <em>{@linkplain OperandType#CONSTANT_POOL_INDEX_SHORT index}</em>
     * </dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>objectref</em> &rarr; &hellip;, <em>value</em></dd>
     * </dl>
     *
     * @see OpcodeValue#INSTANCEOF
     * @see OperandType#CONSTANT_POOL_INDEX_SHORT
     * @see #CHECKCAST
     */
    INSTANCEOF(OpcodeValue.INSTANCEOF, CONSTANT_POOL_INDEX_SHORT),
    /**
     * <p>
     * {@code invokedynamic} - Invoke dynamic method
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value OpcodeValue#INVOKEDYNAMIC}</dd>
     * <dt><strong>Format<sup>*</sup></strong></dt>
     * <dd>
     * <p>
     * {@code invokedynamic}
     * <em>{@linkplain OperandType#CONSTANT_POOL_INDEX_SHORT index}</em>
     * </p>
     * <p>
     * *: Note that technically an {@code invokedynamic} instruction
     * is followed by two indexing bytes, <em>index1</em> and <em>index2</em>
     * (which together form <em>index</em>, the 16-bit runtime constant pool
     * index to the call site specifier); and then two additional bytes.
     * However, as of Java 9, the Java Virtual Machine Specification stipulates
     * the additional bytes must always be zero and so this documentation does
     * not treat the additional bytes as being part of an operand.
     * </p>
     * </dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, [<em>arg1</em>, [<em>arg2</em>, &hellip;]] &rarr;
     * &hellip;
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#INVOKEDYNAMIC
     * @see OperandType#CONSTANT_POOL_INDEX_SHORT
     * @see #INVOKESPECIAL
     * @see #INVOKESTATIC
     * @see #INVOKEVIRTUAL
     * @see TwoOperandOpcode#INVOKEINTERFACE
     */
    INVOKEDYNAMIC(OpcodeValue.INVOKEDYNAMIC, CONSTANT_POOL_INDEX_SHORT),
    /**
     * <p>
     * {@code invokespecial} - Invoke instance method; special handling for
     * superclass, private, and instance initialization method invocations
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value OpcodeValue#INVOKESPECIAL}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>
     * {@code invokespecial}
     * <em>{@linkplain OperandType#CONSTANT_POOL_INDEX_SHORT index}
     * </em>
     * </dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>objectref</em>, [<em>arg1</em>, [<em>arg2</em>, &hellip;]]
     * &rarr;
     * &hellip;
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#INVOKESPECIAL
     * @see OperandType#CONSTANT_POOL_INDEX_SHORT
     * @see #INVOKEDYNAMIC
     * @see #INVOKESTATIC
     * @see #INVOKEVIRTUAL
     * @see TwoOperandOpcode#INVOKEINTERFACE
     */
    INVOKESPECIAL(OpcodeValue.INVOKESPECIAL, CONSTANT_POOL_INDEX_SHORT),
    /**
     * <p>
     * {@code invokestatic} - Invoke class ({@code static}) method
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value OpcodeValue#INVOKESTATIC}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>
     * {@code invokestatic}
     * <em>{@linkplain OperandType#CONSTANT_POOL_INDEX_SHORT index}</em>
     * </dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, [<em>arg1</em>, [<em>arg2</em>, &hellip;]] &rarr;
     * &hellip;
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#INVOKESTATIC
     * @see OperandType#CONSTANT_POOL_INDEX_SHORT
     * @see #INVOKEDYNAMIC
     * @see #INVOKESTATIC
     * @see #INVOKEVIRTUAL
     * @see TwoOperandOpcode#INVOKEINTERFACE
     */
    INVOKESTATIC(OpcodeValue.INVOKESTATIC, CONSTANT_POOL_INDEX_SHORT),
    /**
     * <p>
     * {@code invokevirtual} - Invoke instance method; dispatch based on class
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value OpcodeValue#INVOKEVIRTUAL}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>
     * {@code invokevirtual}
     * <em>{@linkplain OperandType#CONSTANT_POOL_INDEX_SHORT index}</em>
     * </dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>objectref</em>, [<em>arg1</em>, [<em>arg2</em>, &hellip;]]
     * &rarr;
     * &hellip;
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#INVOKEVIRTUAL
     * @see OperandType#CONSTANT_POOL_INDEX_SHORT
     * @see #INVOKEDYNAMIC
     * @see #INVOKESPECIAL
     * @see #INVOKEVIRTUAL
     * @see TwoOperandOpcode#INVOKEINTERFACE
     */
    INVOKEVIRTUAL(OpcodeValue.INVOKEVIRTUAL, CONSTANT_POOL_INDEX_SHORT),
    /**
     * <p>
     * {@code istore} - Store {@code int} into local variable
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value OpcodeValue#ISTORE}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>
     * {@code istore}
     * <em>{@linkplain OperandType#LOCAL_VARIABLE_INDEX_BYTE index}</em>
     * </dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value</em> &rarr; &hellip;</dd>
     * </dl>
     *
     * @see OpcodeValue#ISTORE
     * @see OperandType#LOCAL_VARIABLE_INDEX_BYTE
     * @see #ILOAD
     * @see #ASTORE
     * @see #DSTORE
     * @see #FSTORE
     * @see #LSTORE
     * @see NoOperandOpcode#ISTORE_0
     * @see NoOperandOpcode#ISTORE_1
     * @see NoOperandOpcode#ISTORE_2
     * @see NoOperandOpcode#ISTORE_3
     */
    ISTORE(OpcodeValue.ISTORE, LOCAL_VARIABLE_INDEX_BYTE),
    /**
     * <p>
     * {@code jsr} - Jump subroutine
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value OpcodeValue#JSR}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>
     * {@code jsr}
     * <em>{@linkplain OperandType#BRANCH_OFFSET_SHORT offset}</em>
     * </dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip; &rarr; &hellip;, <em>address</em></dd>
     * </dl>
     *
     * @see OpcodeValue#JSR
     * @see OperandType#BRANCH_OFFSET_SHORT
     * @see #JSR_W
     * @see #RET
     * @see #GOTO
     */
    JSR(OpcodeValue.JSR, BRANCH_OFFSET_SHORT),
    /**
     * <p>
     * {@code jsr_w} - Jump subroutine (wide offset)
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value OpcodeValue#JSR_W}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>
     * {@code jsr_w}
     * <em>{@linkplain OperandType#BRANCH_OFFSET_INT offset}</em>
     * </dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip; &rarr; &hellip;, <em>address</em></dd>
     * </dl>
     *
     * @see OpcodeValue#JSR_W
     * @see OperandType#BRANCH_OFFSET_INT
     * @see #JSR
     * @see #RET
     * @see #GOTO_W
     */
    JSR_W(OpcodeValue.JSR_W, BRANCH_OFFSET_INT),
    /**
     * <p>
     * {@code ldc} - Push item from runtime constant pool
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value OpcodeValue#LDC}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>
     * {@code ldc}
     * <em>{@linkplain OperandType#CONSTANT_POOL_INDEX_BYTE index}</em>
     * </dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip; &rarr; &hellip;, <em>value</em></dd>
     * </dl>
     *
     * @see OpcodeValue#LDC
     * @see OperandType#CONSTANT_POOL_INDEX_BYTE
     * @see #LDC_W
     * @see #LDC2_W
     * @see #BIPUSH
     * @see #SIPUSH
     */
    LDC(OpcodeValue.LDC, CONSTANT_POOL_INDEX_BYTE),
    /**
     * <p>
     * {@code ldc_w} - Push item from runtime constant pool (wide index)
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value OpcodeValue#LDC_W}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>
     * {@code ldc_w}
     * <em>{@linkplain OperandType#CONSTANT_POOL_INDEX_SHORT index}</em>
     * </dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip; &rarr; &hellip;, <em>value</em></dd>
     * </dl>
     *
     * @see OpcodeValue#LDC_W
     * @see OperandType#CONSTANT_POOL_INDEX_SHORT
     * @see #LDC
     * @see #LDC2_W
     * @see #BIPUSH
     * @see #SIPUSH
     */
    LDC_W(OpcodeValue.LDC_W, CONSTANT_POOL_INDEX_SHORT),
    /**
     * <p>
     * {@code ldc2_w} - Push {@code long} or {@code double} from runtime
     * constant pool (wide index)
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value OpcodeValue#LDC2_W}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code ldc2_w} <em>{@linkplain OperandType#CONSTANT_POOL_INDEX_SHORT TODO}</em></dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip; &rarr; &hellip;, <em>value</em></dd>
     * </dl>
     *
     * @see OpcodeValue#LDC2_W
     * @see OperandType#CONSTANT_POOL_INDEX_SHORT
     * @see #LDC
     * @see #LDC_W
     * @see #BIPUSH
     * @see #SIPUSH
     */
    LDC2_W(OpcodeValue.LDC2_W, CONSTANT_POOL_INDEX_SHORT),
    /**
     * <p>
     * {@code lload} - Load {@code long} from local variable
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value OpcodeValue#LLOAD}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>
     * {@code lload}
     * <em>{@linkplain OperandType#LOCAL_VARIABLE_INDEX_BYTE offset}
     * </em>
     * </dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip; &rarr; &hellip;, <em>value</em></dd>
     * </dl>
     *
     * @see OpcodeValue#LLOAD
     * @see OperandType#LOCAL_VARIABLE_INDEX_BYTE
     * @see #LSTORE
     * @see #ALOAD
     * @see #DLOAD
     * @see #ILOAD
     * @see #FLOAD
     * @see NoOperandOpcode#LLOAD_0
     * @see NoOperandOpcode#LLOAD_1
     * @see NoOperandOpcode#LLOAD_2
     * @see NoOperandOpcode#LLOAD_3
     */
    LLOAD(OpcodeValue.LLOAD, LOCAL_VARIABLE_INDEX_BYTE),
    /**
     * <p>
     * {@code lstore} - Store {@code long} into local variable
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value OpcodeValue#LSTORE}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code lstore}
     * <em>{@linkplain OperandType#LOCAL_VARIABLE_INDEX_BYTE index}</em>
     * </dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value</em> &rarr; &hellip;</dd>
     * </dl>
     *
     * @see OpcodeValue#LSTORE
     * @see OperandType#LOCAL_VARIABLE_INDEX_BYTE
     * @see #LLOAD
     * @see #ASTORE
     * @see #DSTORE
     * @see #ISTORE
     * @see #FSTORE
     * @see NoOperandOpcode#LSTORE_0
     * @see NoOperandOpcode#LSTORE_1
     * @see NoOperandOpcode#LSTORE_2
     * @see NoOperandOpcode#LSTORE_3
     */
    LSTORE(OpcodeValue.LSTORE, LOCAL_VARIABLE_INDEX_BYTE),
    /**
     * <p>
     * {@code new} - Create new object
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value OpcodeValue#NEW}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>
     * {@code new}
     * <em>{@linkplain OperandType#CONSTANT_POOL_INDEX_SHORT index}</em>
     * </dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip; &rarr; &hellip;, <em>objectref</em></dd>
     * </dl>
     *
     * @see OpcodeValue#NEW
     * @see OperandType#CONSTANT_POOL_INDEX_SHORT
     * @see #ANEWARRAY
     * @see TwoOperandOpcode#MULTIANEWARRAY
     * @see #NEWARRAY
     */
    NEW(OpcodeValue.NEW, CONSTANT_POOL_INDEX_SHORT),
    /**
     * <p>
     * {@code newarray} - Create new array
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value OpcodeValue#NEWARRAY}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>
     * {@code newarray}
     * <em>{@linkplain OperandType#ATYPE_BYTE atype}</em>
     * </dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>count</em> &rarr; &hellip;, <em>arrayref</em></dd>
     * </dl>
     *
     * @see OpcodeValue#NEWARRAY
     * @see OperandType#ATYPE_BYTE
     * @see ATypeValue
     * @see #ANEWARRAY
     * @see TwoOperandOpcode#MULTIANEWARRAY
     * @see #NEW
     */
    NEWARRAY(OpcodeValue.NEWARRAY, ATYPE_BYTE),
    /**
     * <p>
     * {@code putfield} - Set field in object
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value OpcodeValue#PUTFIELD}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>
     * {@code putfield}
     * <em>{@linkplain OperandType#CONSTANT_POOL_INDEX_SHORT index}</em>
     * </dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>objectref</em>, <em>value</em> &rarr; &hellip;</dd>
     * </dl>
     *
     * @see OpcodeValue#PUTFIELD
     * @see OperandType#CONSTANT_POOL_INDEX_SHORT
     * @see #GETFIELD
     * @see #PUTSTATIC
     */
    PUTFIELD(OpcodeValue.PUTFIELD, CONSTANT_POOL_INDEX_SHORT),
    /**
     * <p>
     * {@code putstatic} - Set {@code static} field in class
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value OpcodeValue#PUTSTATIC}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>
     * {@code putstatic}
     * <em>{@linkplain OperandType#CONSTANT_POOL_INDEX_SHORT index}</em>
     * </dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value</em> &rarr; &hellip;</dd>
     * </dl>
     *
     * @see OpcodeValue#PUTSTATIC
     * @see OperandType#CONSTANT_POOL_INDEX_SHORT
     * @see #GETSTATIC
     * @see #PUTFIELD
     */
    PUTSTATIC(OpcodeValue.PUTSTATIC, CONSTANT_POOL_INDEX_SHORT),
    /**
     * <p>
     * {@code ret} - Return from subroutine
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value OpcodeValue#RET}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>
     * {@code ret}
     * <em>{@linkplain OperandType#LOCAL_VARIABLE_INDEX_BYTE index}</em>
     * </dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip; &rarr; &hellip;</dd>
     * </dl>
     *
     * @see OpcodeValue#RET
     * @see OperandType#LOCAL_VARIABLE_INDEX_BYTE
     * @see #JSR
     * @see #JSR_W
     * @see NoOperandOpcode#RETURN
     */
    RET(OpcodeValue.RET, LOCAL_VARIABLE_INDEX_BYTE),
    /**
     * <p>
     * {@code sipush} - Push {@code short}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value OpcodeValue#SIPUSH}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>
     * {@code sipush}
     * <em>{@linkplain OperandType#SIGNED_VALUE_SHORT value}</em>
     * </dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip; &rarr; &hellip;, <em>value</em></dd>
     * </dl>
     *
     * @see OpcodeValue#SIPUSH
     * @see OperandType#SIGNED_VALUE_SHORT
     * @see #BIPUSH
     * @see NoOperandOpcode#ICONST_M1
     * @see NoOperandOpcode#ICONST_0
     * @see NoOperandOpcode#ICONST_1
     * @see NoOperandOpcode#ICONST_2
     * @see NoOperandOpcode#ICONST_3
     * @see NoOperandOpcode#ICONST_4
     * @see NoOperandOpcode#ICONST_5
     */
    SIPUSH(OpcodeValue.SIPUSH, SIGNED_VALUE_SHORT);


    //
    // DATA
    //

    private final int value;
    private final OperandType operandType;

    //
    // CONSTRUCTORS
    //

    OneOperandOpcode(final int value, final OperandType operandType) {
        this.value = value;
        this.operandType = operandType;
    }

    //
    // INTERFACE: Opcode
    //

    @Override
    public List<OperandType> getOperandTypes() {
        return singletonList(operandType);
    }

    @Override
    public int getNumOperands() {
        return 1;
    }

    //
    // INTERFACE: Valued
    //

    @Override
    public int getValue() {
        return value;
    }

    //
    // INTERFACE: Versioned
    //

    @Override
    public ClassFileVersion getFirstVersionSupporting() {
        if (INVOKEDYNAMIC != this) {
            return JAVA1;
        } else {
            return JAVA7;
        }
    }

    @Override
    public Optional<ClassFileVersion> getLastVersionSupporting() {
        if (JSR != this && JSR_W != this) {
            return empty();
        } else {
            return of(JAVA6);
        }
    }

    //
    // PUBLIC METHODS
    //

    /**
     * <p>
     * Obtains the type of the operand embedded in an instruction having this
     * opcode.
     * </p>
     *
     * <p>
     * This method is simply a marginally faster and occasionally more
     * convenient way of obtaining the value of
     * {@link #getOperandTypes()}{@code .}{@link java.util.List#get(int)
     * get(0)}.
     * </p>
     *
     * @return Operand type
     * @see #getOperandTypes()
     * @see #getNumOperands()
     */
    public OperandType getOperandType() {
        return operandType;
    }

    //
    // PUBLIC STATICS
    //

    /**
     * Obtains the {@link OneOperandOpcode} for the given opcode byte value.
     *
     * @param value Opcode byte value
     * @return Opcode instance
     * @throws IllegalArgumentException If {@code value} is not in the range
     *                                  0..255 <em>or</em> the value is in the
     *                                  correct range but that value does not
     *                                  correspond to a known one-operand opcode
     * @see OpcodeValue
     * @see Opcode#forUnsignedByte(int)
     * @see NoOperandOpcode#forUnsignedByte(int)
     * @see TwoOperandOpcode#forUnsignedByte(int)
     * @see VariableOperandOpcode#forUnsignedByte(int)
     */
    public static OneOperandOpcode forUnsignedByte(final int value) {
        return BY_VALUE.forUnsignedByte(value);
    }

    //
    // INTERNALS
    //

    private static final OpcodeTable<OneOperandOpcode> BY_VALUE =
            forOpcodeEnum(OneOperandOpcode.class);
}
