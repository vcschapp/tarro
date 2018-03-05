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

import static io.tarro.base.bytecode.OpcodeTable.forOpcodeEnum;
import static java.util.Collections.emptyList;

/**
 * <p>
 * Enumerates opcodes for Java Virtual Machine instructions which do not have
 * any operands embedded in the instruction itself.
 * </p>
 *
 * <p>
 * Note that even if the instruction itself has no embedded operands apart from
 * the opcode, it will frequently consume one or more opcodes from the
 * operand stack.
 * </p>
 *
 * @author Victor Schappert
 * @since 20171218
 * @see OneOperandOpcode
 * @see TwoOperandOpcode
 * @see VariableOperandOpcode
 * @see OpcodeValue
 */
public enum NoOperandOpcode implements Opcode {

    //
    // ENUMERATORS
    //

    // Keep this list in alphabetical order by opcode mnemonic.
    /**
     * <p>
     * {@code aaload} - Load {@code reference} from array
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#AALOAD}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code aaload}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>arrayref</em>, <em>index</em> &rarr;
     * &hellip;, <em>value</em>
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#AALOAD
     * @see #AASTORE
     * @see #BALOAD
     * @see #CALOAD
     * @see #DALOAD
     * @see #FALOAD
     * @see #IALOAD
     * @see #LALOAD
     * @see #SALOAD
     */
    AALOAD(OpcodeValue.AALOAD),
    /**
     * <p>
     * {@code aastore} - Store into {@code reference} array
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#AASTORE}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code aastore}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>arrayref</em>, <em>index</em>, <em>value</em> &rarr;
     * &hellip;
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#AASTORE
     * @see #AALOAD
     * @see #BASTORE
     * @see #CASTORE
     * @see #DASTORE
     * @see #FASTORE
     * @see #IASTORE
     * @see #LASTORE
     * @see #SASTORE
     */
    AASTORE(OpcodeValue.AASTORE),
    /**
     * <p>
     * {@code aconst_null} - Push {@code null}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#ACONST_NULL}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code aconst_null}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip; &rarr; &hellip;, {@code null}</dd>
     * </dl>
     *
     * @see OpcodeValue#ACONST_NULL
     */
    ACONST_NULL(OpcodeValue.ACONST_NULL),
    /**
     * <p>
     * {@code aload_0} - Load {@code reference} from local variable at index 0
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#ALOAD_0}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code aload_0}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip; &rarr; &hellip;, <em>objectref</em></dd>
     * </dl>
     *
     * @see OpcodeValue#ALOAD_0
     * @see #ASTORE_0
     * @see #ALOAD_1
     * @see #ALOAD_2
     * @see #ALOAD_3
     * @see OneOperandOpcode#ALOAD
     */
    ALOAD_0(OpcodeValue.ALOAD_0),
    /**
     * <p>
     * {@code aload_1} - Load {@code reference} from local variable at index 1
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#ALOAD_1}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code aload_1}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip; &rarr; &hellip;, <em>objectref</em></dd>
     * </dl>
     *
     * @see OpcodeValue#ALOAD_1
     * @see #ASTORE_1
     * @see #ALOAD_0
     * @see #ALOAD_2
     * @see #ALOAD_3
     * @see OneOperandOpcode#ALOAD
     */
    ALOAD_1(OpcodeValue.ALOAD_1),
    /**
     * <p>
     * {@code aload_2} - Load {@code reference} from local variable at index 2
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#ALOAD_2}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code aload_2}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip; &rarr; &hellip;, <em>objectref</em></dd>
     * </dl>
     *
     * @see OpcodeValue#ALOAD_2
     * @see #ASTORE_2
     * @see #ALOAD_0
     * @see #ALOAD_1
     * @see #ALOAD_3
     * @see OneOperandOpcode#ALOAD
     */
    ALOAD_2(OpcodeValue.ALOAD_2),
    /**
     * <p>
     * {@code aload_3} - Load {@code reference} from local variable at index 3
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#ALOAD_3}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code aload_3}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip; &rarr; &hellip;, <em>objectref</em></dd>
     * </dl>
     *
     * @see OpcodeValue#ALOAD_3
     * @see #ASTORE_3
     * @see #ALOAD_0
     * @see #ALOAD_1
     * @see #ALOAD_2
     * @see OneOperandOpcode#ALOAD
     */
    ALOAD_3(OpcodeValue.ALOAD_3),
    /**
     * <p>
     * {@code areturn} - Return {@code reference} from method
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#ARETURN}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code areturn}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>objectref</em> &rarr; [empty]</dd>
     * </dl>
     *
     * @see OpcodeValue#ARETURN
     * @see #DRETURN
     * @see #FRETURN
     * @see #IRETURN
     * @see #LRETURN
     * @see #RETURN
     */
    ARETURN(OpcodeValue.ARETURN),
    /**
     * <p>
     * {@code arraylength} - Get length of array
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#ARRAYLENGTH}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code arraylength}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>arrayref</em> &rarr; &hellip;, <em>length</em></dd>
     * </dl>
     *
     * @see OpcodeValue#ARRAYLENGTH
     */
    ARRAYLENGTH(OpcodeValue.ARRAYLENGTH),
    /**
     * <p>
     * {@code astore_0} - Store {@code reference} into local variable at index 0
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#ASTORE_0}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code astore_0}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>objectref</em> &rarr; &hellip;</dd>
     * </dl>
     *
     * @see OpcodeValue#ASTORE_0
     * @see #ALOAD_0
     * @see #ASTORE_1
     * @see #ASTORE_2
     * @see #ASTORE_3
     * @see OneOperandOpcode#ASTORE
     */
    ASTORE_0(OpcodeValue.ASTORE_0),
    /**
     * <p>
     * {@code astore_1} - Store {@code reference} into local variable at index 1
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#ASTORE_1}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code astore_1}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>objectref</em> &rarr; &hellip;</dd>
     * </dl>
     *
     * @see OpcodeValue#ASTORE_1
     * @see #ALOAD_1
     * @see #ASTORE_0
     * @see #ASTORE_2
     * @see #ASTORE_3
     * @see OneOperandOpcode#ASTORE
     */
    ASTORE_1(OpcodeValue.ASTORE_1),
    /**
     * <p>
     * {@code astore_2} - Store {@code reference} into local variable at index 2
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#ASTORE_2}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code astore_2}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>objectref</em> &rarr; &hellip;</dd>
     * </dl>
     *
     * @see OpcodeValue#ASTORE_2
     * @see #ALOAD_2
     * @see #ASTORE_0
     * @see #ASTORE_1
     * @see #ASTORE_3
     * @see OneOperandOpcode#ASTORE
     */
    ASTORE_2(OpcodeValue.ASTORE_2),
    /**
     * <p>
     * {@code astore_3} - Store {@code reference} into local variable at index 3
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#ASTORE_3}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code astore_3}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>objectref</em> &rarr; &hellip;</dd>
     * </dl>
     *
     * @see OpcodeValue#ASTORE_3
     * @see #ALOAD_3
     * @see #ASTORE_0
     * @see #ASTORE_1
     * @see #ASTORE_2
     * @see OneOperandOpcode#ASTORE
     */
    ASTORE_3(OpcodeValue.ASTORE_3),
    /**
     * <p>
     * {@code athrow} - Throw exception or error
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#ATHROW}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code athrow}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>objectref</em> &rarr; &hellip;</dd>
     * </dl>
     *
     * @see OpcodeValue#ATHROW
     */
    ATHROW(OpcodeValue.ATHROW),
    /**
     * <p>
     * {@code baload} - Load {@code byte} or {@code boolean} from array
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#BALOAD}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code baload}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>arrayref</em>, <em>index</em> &rarr;
     * &hellip;, <em>value</em></dd>
     * </dl>
     *
     * @see OpcodeValue#BALOAD
     * @see #BASTORE
     * @see #AALOAD
     * @see #CALOAD
     * @see #DALOAD
     * @see #FALOAD
     * @see #IALOAD
     * @see #LALOAD
     * @see #SALOAD
     */
    BALOAD(OpcodeValue.BALOAD),
    /**
     * <p>
     * {@code bastore} - Store into {@code byte} or {@code boolean} array
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#BASTORE}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code bastore}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>arrayref</em>, <em>index</em>, <em>value</em> &rarr;
     * &hellip;
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#BASTORE
     * @see #BALOAD
     * @see #AASTORE
     * @see #CASTORE
     * @see #DASTORE
     * @see #FASTORE
     * @see #IASTORE
     * @see #LASTORE
     * @see #SASTORE
     */
    BASTORE(OpcodeValue.BASTORE),
    /**
     * <p>
     * {@code breakpoint} - Reserved for debuggers to implement breakpoints,
     * should not appear in bytecode in a class file
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#BREAKPOINT}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code breakpoint}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip; &rarr; &hellip;
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#BREAKPOINT
     * @see #IMPDEP1
     * @see #IMPDEP2
     */
    BREAKPOINT(OpcodeValue.BREAKPOINT, true),
    /**
     * <p>
     * {@code caload} - Load {@code char} from array
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#CALOAD}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code caload}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>arrayref</em>, <em>index</em> &rarr;
     * &hellip;, <em>value</em>
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#CALOAD
     * @see #CASTORE
     * @see #AALOAD
     * @see #BALOAD
     * @see #DALOAD
     * @see #FALOAD
     * @see #IALOAD
     * @see #LALOAD
     * @see #SALOAD
     */
    CALOAD(OpcodeValue.CALOAD),
    /**
     * <p>
     * {@code castore} - Store {@code char} into array
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#CASTORE}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code castore}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>arrayref</em>, <em>index</em>, <em>value</em> &rarr;
     * &hellip;
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#CASTORE
     * @see #CALOAD
     * @see #AASTORE
     * @see #BASTORE
     * @see #DASTORE
     * @see #FASTORE
     * @see #IASTORE
     * @see #LASTORE
     * @see #SASTORE
     */
    CASTORE(OpcodeValue.CASTORE),
    /**
     * <p>
     * {@code d2f} - Convert {@code double} to {@code float}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#D2F}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code d2f}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>value</em> &rarr;
     * &hellip;, <em>result</em>
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#D2F
     * @see #F2D
     * @see #D2I
     * @see #D2L
     */
    D2F(OpcodeValue.D2F),
    /**
     * <p>
     * {@code d2i} - Convert {@code double} to {@code int}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#D2I}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code d2i}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>value</em> &rarr;
     * &hellip;, <em>result</em>
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#D2I
     * @see #I2D
     * @see #D2F
     * @see #D2L
     */
    D2I(OpcodeValue.D2I),
    /**
     * <p>
     * {@code d2l} - Convert {@code double} to {@code long}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#D2L}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code d2l}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>value</em> &rarr;
     * &hellip;, <em>result</em>
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#D2L
     * @see #L2D
     * @see #D2F
     * @see #D2I
     */
    D2L(OpcodeValue.D2L),
    /**
     * <p>
     * {@code dadd} - Add {@code double}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#DADD}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code dadd}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>value1</em>, <em>value2</em> &rarr;
     * &hellip;, <em>result</em>
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#DADD
     * @see #FADD
     * @see #IADD
     * @see #LADD
     * @see #DSUB
     */
    DADD(OpcodeValue.DADD),
    /**
     * <p>
     * {@code daload} - Load {@code double} from array
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#DALOAD}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code daload}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>arrayref</em>, <em>index</em> &rarr;
     * &hellip;, <em>value</em>
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#DALOAD
     * @see #DASTORE
     * @see #AALOAD
     * @see #BALOAD
     * @see #CALOAD
     * @see #FALOAD
     * @see #IALOAD
     * @see #LALOAD
     * @see #SALOAD
     */
    DALOAD(OpcodeValue.DALOAD),
    /**
     * <p>
     * {@code dastore} - Store into {@code double} array
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#DASTORE}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code dastore}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>arrayref</em>, <em>index</em>, <em>value</em> &rarr;
     * &hellip;
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#DALOAD
     * @see #DALOAD
     * @see #AASTORE
     * @see #BASTORE
     * @see #CASTORE
     * @see #FASTORE
     * @see #IASTORE
     * @see #LASTORE
     * @see #SASTORE
     */
    DASTORE(OpcodeValue.DASTORE),
    /**
     * <p>
     * {@code dcmpg} - Compare {@code double} (on a NaN operand, push 1)
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#DCMPG}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code dcmpg}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>value1</em>, <em>value2</em> &rarr;
     * &hellip;, <em>result</em>
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#DCMPG
     * @see #DCMPL
     * @see #FCMPG
     * @see #LCMP
     */
    DCMPG(OpcodeValue.DCMPG),
    /**
     * <p>
     * {@code dcmpl} - Compare {@code double} (on a NaN operand, push -1)
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#DCMPL}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code dcmpl}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>value1</em>, <em>value2</em> &rarr;
     * &hellip;, <em>result</em>
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#DCMPL
     * @see #DCMPG
     * @see #FCMPL
     * @see #LCMP
     */
    DCMPL(OpcodeValue.DCMPL),
    /**
     * <p>
     * {@code dconst_0} - Push {@code double} value {@code 0.0d}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#DCONST_0}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code dconst_0}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip; &rarr; &hellip;, {@code 0.0d}</dd>
     * </dl>
     *
     * @see OpcodeValue#DCONST_0
     * @see #DCONST_1
     * @see #DLOAD_0
     * @see #DLOAD_1
     * @see #DLOAD_2
     * @see #DLOAD_3
     * @see #FCONST_0
     * @see #ICONST_0
     * @see #LCONST_0
     * @see OneOperandOpcode#DLOAD
     */
    DCONST_0(OpcodeValue.DCONST_0),
    /**
     * <p>
     * {@code dconst_1} - Push {@code double} value {@code 1.0d}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#DCONST_1}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code dconst_1}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip; &rarr; &hellip;, {@code 1.0d}</dd>
     * </dl>
     *
     * @see OpcodeValue#DCONST_1
     * @see #DCONST_0
     * @see #DLOAD_0
     * @see #DLOAD_1
     * @see #DLOAD_2
     * @see #DLOAD_3
     * @see #FCONST_1
     * @see #ICONST_1
     * @see #LCONST_1
     * @see OneOperandOpcode#DLOAD
     */
    DCONST_1(OpcodeValue.DCONST_1),
    /**
     * <p>
     * {@code ddiv} - Divide {@code double}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#DDIV}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code ddiv}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>value1</em>, <em>value2</em> &rarr;
     * &hellip;, <em>result</em>
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#DDIV
     * @see #FDIV
     * @see #IDIV
     * @see #LDIV
     * @see #DMUL
     * @see #DREM
     */
    DDIV(OpcodeValue.DDIV),
    /**
     * <p>
     * {@code dload_0} - Load {@code double} from local variable at index 0
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#DLOAD_0}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code dload_0}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip; &rarr; &hellip;, <em>value</em></dd>
     * </dl>
     *
     * @see OpcodeValue#DLOAD_0
     * @see #DSTORE_0
     * @see #DLOAD_1
     * @see #DLOAD_2
     * @see #DLOAD_3
     * @see OneOperandOpcode#DLOAD
     */
    DLOAD_0(OpcodeValue.DLOAD_0),
    /**
     * <p>
     * {@code dload_1} - Load {@code double} from local variable at index 1
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#DLOAD_1}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code dload_1}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip; &rarr; &hellip;, <em>value</em></dd>
     * </dl>
     *
     * @see OpcodeValue#DLOAD_1
     * @see #DSTORE_1
     * @see #DLOAD_0
     * @see #DLOAD_2
     * @see #DLOAD_3
     * @see OneOperandOpcode#DLOAD
     */
    DLOAD_1(OpcodeValue.DLOAD_1),
    /**
     * <p>
     * {@code dload_2} - Load {@code double} from local variable at index 2
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#DLOAD_2}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code dload_2}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip; &rarr; &hellip;, <em>value</em></dd>
     * </dl>
     *
     * @see OpcodeValue#DLOAD_2
     * @see #DSTORE_2
     * @see #DLOAD_0
     * @see #DLOAD_1
     * @see #DLOAD_3
     * @see OneOperandOpcode#DLOAD
     */
    DLOAD_2(OpcodeValue.DLOAD_2),
    /**
     * <p>
     * {@code dload_3} - Load {@code double} from local variable at index 3
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#DLOAD_3}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code dload_3}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip; &rarr; &hellip;, <em>value</em></dd>
     * </dl>
     *
     * @see OpcodeValue#DLOAD_3
     * @see #DSTORE_2
     * @see #DLOAD_0
     * @see #DLOAD_1
     * @see #DLOAD_2
     * @see OneOperandOpcode#DLOAD
     */
    DLOAD_3(OpcodeValue.DLOAD_3),
    /**
     * <p>
     * {@code dmul} - Multiply {@code double}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#DMUL}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code dmul}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>value1</em>, <em>value2</em> &rarr;
     * &hellip;, <em>result</em>
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#DMUL
     * @see #FMUL
     * @see #IMUL
     * @see #LMUL
     * @see #DDIV
     * @see #DREM
     */
    DMUL(OpcodeValue.DMUL),
    /**
     * <p>
     * {@code dneg} - Negate {@code double}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#DNEG}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code dneg}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value</em> &rarr; &hellip;, <em>result</em></dd>
     * </dl>
     *
     * @see OpcodeValue#DNEG
     * @see #FNEG
     * @see #INEG
     * @see #LNEG
     * @see #DSUB
     */
    DNEG(OpcodeValue.DNEG),
    /**
     * <p>
     * {@code drem} - Remainder {@code double}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#DREM}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code drem}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>value1</em>, <em>value2</em> &rarr;
     * &hellip;, <em>result</em>
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#DREM
     * @see #FREM
     * @see #IREM
     * @see #LREM
     * @see #DDIV
     * @see #DMUL
     */
    DREM(OpcodeValue.DREM),
    /**
     * <p>
     * {@code dreturn} - Return {@code double} from method
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#DRETURN}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code dreturn}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value</em> &rarr; [empty]</dd>
     * </dl>
     *
     * @see OpcodeValue#DRETURN
     * @see #ARETURN
     * @see #FRETURN
     * @see #IRETURN
     * @see #LRETURN
     * @see #RETURN
     */
    DRETURN(OpcodeValue.DRETURN),
    /**
     * <p>
     * {@code dstore_0} - Store {@code double} into local variable at index 0
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#DSTORE_0}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code dstore_0}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value</em> &rarr; &hellip;</dd>
     * </dl>
     *
     * @see OpcodeValue#DSTORE_0
     * @see #DLOAD_0
     * @see #DSTORE_0
     * @see #DSTORE_1
     * @see #DSTORE_2
     * @see OneOperandOpcode#DSTORE
     */
    DSTORE_0(OpcodeValue.DSTORE_0),
    /**
     * <p>
     * {@code dstore_1} - Store {@code double} into local variable at index 1
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#DSTORE_1}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code dstore_1}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value</em> &rarr; &hellip;</dd>
     * </dl>
     *
     * @see OpcodeValue#DSTORE_1
     * @see #DLOAD_1
     * @see #DSTORE_0
     * @see #DSTORE_2
     * @see #DSTORE_3
     * @see OneOperandOpcode#DSTORE
     */
    DSTORE_1(OpcodeValue.DSTORE_1),
    /**
     * <p>
     * {@code dstore_2} - Store {@code double} into local variable at index 2
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#DSTORE_2}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code dstore_2}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value</em> &rarr; &hellip;</dd>
     * </dl>
     *
     * @see OpcodeValue#DSTORE_2
     * @see #DLOAD_2
     * @see #DSTORE_0
     * @see #DSTORE_1
     * @see #DSTORE_3
     * @see OneOperandOpcode#DSTORE
     */
    DSTORE_2(OpcodeValue.DSTORE_2),
    /**
     * <p>
     * {@code dstore_3} - Store {@code double} into local variable at index 3
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#DSTORE_3}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code dstore_3}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value</em> &rarr; &hellip;</dd>
     * </dl>
     *
     * @see OpcodeValue#DSTORE_3
     * @see #DLOAD_3
     * @see #DSTORE_0
     * @see #DSTORE_1
     * @see #DSTORE_2
     * @see OneOperandOpcode#DSTORE
     */
    DSTORE_3(OpcodeValue.DSTORE_3),
    /**
     * <p>
     * {@code dsub} - Subtract {@code double}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#DSUB}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code dsub}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>value1</em>, <em>value2</em> &rarr;
     * &hellip;, <em>result</em>
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#DSUB
     * @see #FSUB
     * @see #ISUB
     * @see #LSUB
     * @see #DNEG
     * @see #DADD
     */
    DSUB(OpcodeValue.DSUB),
    /**
     * <p>
     * {@code dup} - Duplicate the top operand stack value
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#DUP}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code dup}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>value</em> &rarr;
     * &hellip;, <em>value</em>, <em>value</em>
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#DUP
     * @see #DUP_X1
     * @see #DUP_X2
     * @see #DUP2
     * @see #DUP2_X1
     * @see #DUP2_X2
     * @see #POP
     * @see #SWAP
     */
    DUP(OpcodeValue.DUP),
    /**
     * <p>
     * {@code dup_x1} - Duplicate the top operand stack value and insert it two
     * values down
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#DUP_X1}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code dup_x1}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>value2</em>, <em>value1</em> &rarr;
     * &hellip;, <em>value1</em>, <em>value2</em>, <em>value1</em>
     * </dl>
     *
     * @see OpcodeValue#DUP_X1
     * @see #DUP
     * @see #DUP_X2
     * @see #DUP2
     * @see #DUP2_X1
     * @see #DUP2_X2
     * @see #POP
     * @see #SWAP
     */
    DUP_X1(OpcodeValue.DUP_X1),
    /**
     * <p>
     * {@code dup_x2} - Duplicate the top operand stack value and insert it two
     * or three values down
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#DUP_X2}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code dup_x2}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * <p>Form 1:</p>
     * <p>
     * &hellip;, <em>value3</em>, <em>value2</em>, <em>value1</em> &rarr;
     * &hellip;, <em>value1</em>, <em>value3</em>, <em>value2</em>,
     * <em>value1</em>
     * </p>
     * <p>
     * where <em>value1</em>, <em>value2</em>, and <em>value3</em> are all
     * values of a category 1 computational type.
     * </p>
     * <p>Form 2:</p>
     * <p>
     * &hellip;, <em>value2</em>, <em>value1</em> &rarr;
     * &hellip;, <em>value1</em>, <em>value2</em>, <em>value1</em>
     * </p>
     * <p>
     * where <em>value1</em> is a value of a category 1 computational type and
     * <em>value2</em> is a value of a category 2 computational type.
     * </p>
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#DUP_X2
     * @see #DUP
     * @see #DUP_X1
     * @see #DUP2
     * @see #DUP2_X1
     * @see #DUP2_X2
     * @see #POP
     * @see #SWAP
     */
    DUP_X2(OpcodeValue.DUP_X2),
    /**
     * <p>
     * {@code dup2} - Duplicate the top one or two operand stack values
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#DUP2}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code dup2}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * <p>Form 1:</p>
     * <p>
     * &hellip;, <em>value2</em>, <em>value1</em> &rarr;
     * &hellip;, <em>value2</em>, <em>value1</em>, <em>value2</em>,
     * <em>value1</em>
     * </p>
     * <p>
     * where both <em>value1</em> and <em>value2</em> are values of a category 1
     * computational type.
     * </p>
     * <p>Form 2:</p>
     * <p>
     * &hellip;, <em>value</em> &rarr;
     * &hellip;, <em>value</em>, <em>value</em>
     * </p>
     * <p>
     * where <em>value</em> is a value of a category 2 computational type.
     * </p>
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#DUP2
     * @see #DUP
     * @see #DUP_X1
     * @see #DUP_X2
     * @see #DUP2_X1
     * @see #DUP2_X2
     * @see #POP2
     */
    DUP2(OpcodeValue.DUP2),
    /**
     * <p>
     * {@code dup2_x1} - Duplicate the top one or two operand values and insert
     * them two or three values down
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#DUP2_X1}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code dup2_x1}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * <p>Form 1:</p>
     * <p>
     * &hellip;, <em>value3</em>, <em>value2</em>, <em>value1</em> &rarr;
     * &hellip;, <em>value2</em>, <em>value1</em>, <em>value3</em>,
     * <em>value2</em>, <em>value1</em>
     * </p>
     * <p>
     * where <em>value1</em>, <em>value2</em>, and <em>value3</em> are all
     * values of a category 1 computational type.
     * </p>
     * <p>Form 2:</p>
     * <p>
     * &hellip;, <em>value2</em>, <em>value1</em> &rarr;
     * &hellip;, <em>value1</em>, <em>value2</em>, <em>value1</em>
     * </p>
     * <p>
     * where <em>value1</em> is a value of a category 2 computational type and
     * <em>value2</em> is a value of a category 1 computational type.
     * </p>
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#DUP2_X1
     * @see #DUP
     * @see #DUP_X1
     * @see #DUP_X2
     * @see #DUP2
     * @see #DUP2_X2
     * @see #POP2
     */
    DUP2_X1(OpcodeValue.DUP2_X1),
    /**
     * <p>
     * {@code dup2_x2} - Duplicate the top one or two operand stack values and
     * insert them two, three, or four values down
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#DUP2_X2}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code dup2_x2}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * <p>Form 1:</p>
     * <p>
     * &hellip;, <em>value4</em>, <em>value3</em>, <em>value2</em>,
     * <em>value1</em> &rarr;
     * &hellip;, <em>value2</em>, <em>value1</em>, <em>value4</em>,
     * <em>value3</em>, <em>value2</em>, <em>value1</em>
     * </p>
     * <p>
     * where <em>value1</em>, <em>value2</em>, <em>value3</em>, and
     * <em>value4</em>are all values of a category 1 computational type.
     * </p>
     * <p>Form 2:</p>
     * <p>
     * &hellip;, <em>value3</em>, <em>value2</em>, <em>value1</em> &rarr;
     * &hellip;, <em>value1</em>, <em>value3</em>, <em>value2</em>,
     * <em>value1</em>
     * </p>
     * <p>
     * where <em>value1</em> is a value of a category 2 computational type and
     * <em>value2</em> and <em>value3</em> are values of a category 1
     * computational type.
     * </p>
     * <p>Form 3:</p>
     * <p>
     * &hellip;, <em>value3</em>, <em>value2</em>, <em>value1</em> &rarr;
     * &hellip;, <em>value2</em>, <em>value1</em>, <em>value3</em>,
     * <em>value2</em>, <em>value1</em>
     * </p>
     * <p>
     * where <em>value1</em> and <em>value2</em> are values of a category 1
     * computational type and <em>value3</em> is a value of a category 2
     * computational type.
     * </p>
     * <p>Form 4:</p>
     * <p>
     * &hellip;, <em>value2</em>, <em>value1</em> &rarr;
     * &hellip;, <em>value1</em>, <em>value2</em>, <em>value1</em>
     * </p>
     * <p>
     * where <em>value1</em> and <em>value2</em> are both values of a category 2
     * computational type.
     * </p>
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#DUP2_X2
     * @see #DUP
     * @see #DUP_X1
     * @see #DUP_X2
     * @see #DUP2
     * @see #DUP2_X2
     * @see #POP2
     */
    DUP2_X2(OpcodeValue.DUP2_X2),
    /**
     * <p>
     * {@code f2d} - Convert {@code float} to {@code double}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#F2D}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code f2d}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value</em> &rarr; &hellip;, <em>result</em></dd>
     * </dl>
     *
     * @see OpcodeValue#F2D
     * @see #D2F
     * @see #F2I
     * @see #F2L
     */
    F2D(OpcodeValue.F2D),
    /**
     * <p>
     * {@code f2i} - Convert {@code float} to {@code int}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#F2I}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code f2i}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value</em> &rarr; &hellip;, <em>result</em></dd>
     * </dl>
     *
     * @see OpcodeValue#F2I
     * @see #I2F
     * @see #F2D
     * @see #F2L
     */
    F2I(OpcodeValue.F2I),
    /**
     * <p>
     * {@code f2l} - Convert {@code float} to {@code long}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#F2L}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code f2l}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value</em> &rarr; &hellip;, <em>result</em></dd>
     * </dl>
     *
     * @see OpcodeValue#F2L
     * @see #L2F
     * @see #F2D
     * @see #F2I
     */
    F2L(OpcodeValue.F2L),
    /**
     * <p>
     * {@code fadd} - Add {@code float}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#FADD}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code fadd}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>value1</em>, <em>value2</em> &rarr;
     * &hellip;, <em>result</em>
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#FADD
     * @see #DADD
     * @see #IADD
     * @see #LADD
     * @see #FSUB
     */
    FADD(OpcodeValue.FADD),
    /**
     * <p>
     * {@code faload} - Load {@code float} from array
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#FALOAD}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code faload}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>arrayref</em>, <em>value</em> &rarr;
     * &hellip;, <em>value</em>
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#FALOAD
     * @see #FASTORE
     * @see #AALOAD
     * @see #BALOAD
     * @see #CALOAD
     * @see #DALOAD
     * @see #IALOAD
     * @see #LALOAD
     * @see #SALOAD
     */
    FALOAD(OpcodeValue.FALOAD),
    /**
     * <p>
     * {@code fastore} - Store into {@code float} array
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#FASTORE}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code fastore}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>arrayref</em>, <em>value</em> &rarr;
     * &hellip;, <em>value</em>
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#FASTORE
     * @see #FALOAD
     * @see #AASTORE
     * @see #BASTORE
     * @see #CASTORE
     * @see #DASTORE
     * @see #IASTORE
     * @see #LASTORE
     * @see #SASTORE
     */
    FASTORE(OpcodeValue.FASTORE),
    /**
     * <p>
     * {@code fcmpg} - Compare {@code float} (on a NaN operand, push 1)
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#FCMPG}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code fcmpg}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>value1</em>, <em>value2</em> &rarr;
     * &hellip;, <em>result</em>
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#FCMPG
     * @see #FCMPL
     * @see #DCMPG
     * @see #LCMP
     */
    FCMPG(OpcodeValue.FCMPG),
    /**
     * <p>
     * {@code fcmpl} - Compare {@code float} (on a NaN operand, push -1)
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#FCMPL}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code fcmpl}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>value1</em>, <em>value2</em> &rarr;
     * &hellip;, <em>result</em>
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#FCMPL
     * @see #FCMPG
     * @see #DCMPL
     * @see #LCMP
     */
    FCMPL(OpcodeValue.FCMPL),
    /**
     * <p>
     * {@code fconst_1} - Push {@code float} value {@code 0.0f}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#FCONST_0}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code fconst_0}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip; &rarr; &hellip;, {@code 0.0f}</dd>
     * </dl>
     *
     * @see OpcodeValue#FCONST_0
     * @see #FCONST_1
     * @see #FCONST_2
     * @see #FLOAD_0
     * @see #FLOAD_1
     * @see #FLOAD_2
     * @see #FLOAD_3
     * @see #DCONST_0
     * @see #ICONST_0
     * @see #LCONST_0
     * @see OneOperandOpcode#FLOAD
     */
    FCONST_0(OpcodeValue.FCONST_0),
    /**
     * <p>
     * {@code fconst_1} - Push {@code float} value {@code 1.0f}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#FCONST_1}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code fconst_1}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip; &rarr; &hellip;, {@code 1.0f}</dd>
     * </dl>
     *
     * @see OpcodeValue#FCONST_1
     * @see #FCONST_0
     * @see #FCONST_2
     * @see #FLOAD_0
     * @see #FLOAD_1
     * @see #FLOAD_2
     * @see #FLOAD_3
     * @see #DCONST_1
     * @see #ICONST_1
     * @see #LCONST_1
     * @see OneOperandOpcode#FLOAD
     */
    FCONST_1(OpcodeValue.FCONST_1),
    /**
     * <p>
     * {@code fconst_2} - Push {@code float} value {@code 2.0f}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#FCONST_2}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code fconst_2}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip; &rarr; &hellip;, {@code 2.0f}</dd>
     * </dl>
     *
     * @see OpcodeValue#FCONST_2
     * @see #FCONST_0
     * @see #FCONST_1
     * @see #FLOAD_0
     * @see #FLOAD_1
     * @see #FLOAD_2
     * @see #FLOAD_3
     * @see #ICONST_2
     * @see OneOperandOpcode#FLOAD
     */
    FCONST_2(OpcodeValue.FCONST_2),
    /**
     * <p>
     * {@code fload_0} - Divide {@code float}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#FDIV}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code fdiv}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>value1</em>, <em>value2</em> &rarr;
     * &hellip;, <em>result</em>
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#FDIV
     * @see #DDIV
     * @see #IDIV
     * @see #LDIV
     * @see #FMUL
     * @see #FREM
     */
    FDIV(OpcodeValue.FDIV),
    /**
     * <p>
     * {@code fload_0} - Load float from local variable at index 0
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#FLOAD_0}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code fload_0}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip; &rarr; &hellip;, <em>value</em></dd>
     * </dl>
     *
     * @see OpcodeValue#FLOAD_0
     * @see #FSTORE_0
     * @see #FLOAD_1
     * @see #FLOAD_2
     * @see #FLOAD_3
     * @see OneOperandOpcode#FLOAD
     */
    FLOAD_0(OpcodeValue.FLOAD_0),
    /**
     * <p>
     * {@code fload_1} - Load float from local variable at index 1
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#FLOAD_1}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code fload_1}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip; &rarr; &hellip;, <em>value</em></dd>
     * </dl>
     *
     * @see OpcodeValue#FLOAD_1
     * @see #FSTORE_1
     * @see #FLOAD_0
     * @see #FLOAD_2
     * @see #FLOAD_3
     * @see OneOperandOpcode#FLOAD
     */
    FLOAD_1(OpcodeValue.FLOAD_1),
    /**
     * <p>
     * {@code fload_2} - Load float from local variable at index 2
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#FLOAD_2}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code fload_2}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip; &rarr; &hellip;, <em>value</em></dd>
     * </dl>
     *
     * @see OpcodeValue#FLOAD_2
     * @see #FSTORE_2
     * @see #FLOAD_0
     * @see #FLOAD_1
     * @see #FLOAD_3
     * @see OneOperandOpcode#FLOAD
     */
    FLOAD_2(OpcodeValue.FLOAD_2),
    /**
     * <p>
     * {@code fload_3} - Load float from local variable at index 3
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#FLOAD_3}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code fload_3}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip; &rarr; &hellip;, <em>value</em></dd>
     * </dl>
     *
     * @see OpcodeValue#FLOAD_3
     * @see #FSTORE_3
     * @see #FLOAD_0
     * @see #FLOAD_1
     * @see #FLOAD_2
     * @see OneOperandOpcode#FLOAD
     */
    FLOAD_3(OpcodeValue.FLOAD_3),
    /**
     * <p>
     * {@code fmul} - Multiply {@code float}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#FMUL}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code fmul}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>value1</em>, <em>value2</em> &rarr;
     * &hellip;, <em>result</em>
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#FMUL
     * @see #DMUL
     * @see #IMUL
     * @see #LMUL
     * @see #FDIV
     * @see #FREM
     */
    FMUL(OpcodeValue.FMUL),
    /**
     * <p>
     * {@code fneg} - Negate {@code float}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#FNEG}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code fneg}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value</em> &rarr; &hellip;, <em>result</em></dd>
     * </dl>
     *
     * @see OpcodeValue#FNEG
     * @see #DNEG
     * @see #INEG
     * @see #LNEG
     * @see #FSUB
     */
    FNEG(OpcodeValue.FNEG),
    /**
     * <p>
     * {@code frem} - Remainder {@code float}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#FREM}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code frem}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>value1</em>, <em>value2</em> &rarr;
     * &hellip;, <em>result</em>
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#FREM
     * @see #DREM
     * @see #IREM
     * @see #LREM
     * @see #FDIV
     * @see #FMUL
     */
    FREM(OpcodeValue.FREM),
    /**
     * <p>
     * {@code freturn} - Return {@code float} from method
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#FRETURN}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code freturn}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value</em> &rarr; [empty]</dd>
     * </dl>
     *
     * @see OpcodeValue#FRETURN
     * @see #ARETURN
     * @see #DRETURN
     * @see #IRETURN
     * @see #LRETURN
     * @see #RETURN
     */
    FRETURN(OpcodeValue.FRETURN),
    /**
     * <p>
     * {@code fstore_0} - Store {@code float} into local variable at index 0
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#FSTORE_0}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code fstore_0}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value</em> &rarr; &hellip;</dd>
     * </dl>
     *
     * @see OpcodeValue#FSTORE_0
     * @see #FLOAD_0
     * @see #FSTORE_1
     * @see #FSTORE_2
     * @see #FSTORE_3
     * @see OneOperandOpcode#FSTORE
     */
    FSTORE_0(OpcodeValue.FSTORE_0),
    /**
     * <p>
     * {@code fstore_1} - Store {@code float} into local variable at index 1
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#FSTORE_1}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code fstore_1}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value</em> &rarr; &hellip;</dd>
     * </dl>
     *
     * @see OpcodeValue#FSTORE_1
     * @see #FLOAD_1
     * @see #FSTORE_0
     * @see #FSTORE_2
     * @see #FSTORE_3
     * @see OneOperandOpcode#FSTORE
     */
    FSTORE_1(OpcodeValue.FSTORE_1),
    /**
     * <p>
     * {@code fstore_2} - Store {@code float} into local variable at index 2
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#FSTORE_2}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code fstore_2}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value</em> &rarr; &hellip;</dd>
     * </dl>
     *
     * @see OpcodeValue#FSTORE_2
     * @see #FLOAD_2
     * @see #FSTORE_0
     * @see #FSTORE_1
     * @see #FSTORE_3
     * @see OneOperandOpcode#FSTORE
     */
    FSTORE_2(OpcodeValue.FSTORE_2),
    /**
     * <p>
     * {@code fstore_3} - Store {@code float} into local variable at index 3
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#FSTORE_3}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code fstore_3}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value</em> &rarr; &hellip;</dd>
     * </dl>
     *
     * @see OpcodeValue#FSTORE_3
     * @see #FLOAD_3
     * @see #FSTORE_0
     * @see #FSTORE_1
     * @see #FSTORE_2
     * @see OneOperandOpcode#FSTORE
     */
    FSTORE_3(OpcodeValue.FSTORE_3),
    /**
     * <p>
     * {@code fsub} - Subtract {@code float}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#FSUB}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code fsub}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>value1</em>, <em>value2</em> &rarr;
     * &hellip;, <em>result</em>
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#FSUB
     * @see #DSUB
     * @see #ISUB
     * @see #LSUB
     * @see #FNEG
     * @see #FADD
     */
    FSUB(OpcodeValue.FSUB),
    /**
     * <p>
     * {@code i2b} - Convert {@code int} into {@code byte}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#I2B}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code i2b}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value</em> &rarr; &hellip;, <em>result</em></dd>
     * </dl>
     *
     * @see OpcodeValue#I2B
     * @see #I2C
     * @see #I2D
     * @see #I2F
     * @see #I2L
     * @see #I2S
     */
    I2B(OpcodeValue.I2B),
    /**
     * <p>
     * {@code i2c} - Convert {@code int} into {@code char}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#I2C}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code i2c}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value</em> &rarr; &hellip;, <em>result</em></dd>
     * </dl>
     *
     * @see OpcodeValue#I2C
     * @see #I2B
     * @see #I2D
     * @see #I2F
     * @see #I2L
     * @see #I2S
     */
    I2C(OpcodeValue.I2C),
    /**
     * <p>
     * {@code i2d} - Convert {@code int} into {@code double}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#I2D}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code i2d}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value</em> &rarr; &hellip;, <em>result</em></dd>
     * </dl>
     *
     * @see OpcodeValue#I2D
     * @see #D2I
     * @see #I2B
     * @see #I2C
     * @see #I2F
     * @see #I2L
     * @see #I2S
     */
    I2D(OpcodeValue.I2D),
    /**
     * <p>
     * {@code i2f} - Convert {@code int} into {@code float}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#I2F}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code i2f}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value</em> &rarr; &hellip;, <em>result</em></dd>
     * </dl>
     *
     * @see OpcodeValue#I2F
     * @see #F2I
     * @see #I2B
     * @see #I2C
     * @see #I2D
     * @see #I2L
     * @see #I2S
     */
    I2F(OpcodeValue.I2F),
    /**
     * <p>
     * {@code i2l} - Convert {@code int} into {@code long}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#I2L}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code i2l}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value</em> &rarr; &hellip;, <em>result</em></dd>
     * </dl>
     *
     * @see OpcodeValue#I2L
     * @see #L2I
     * @see #I2B
     * @see #I2C
     * @see #I2D
     * @see #I2F
     * @see #I2S
     */
    I2L(OpcodeValue.I2L),
    /**
     * <p>
     * {@code i2s} - Convert {@code int} into {@code short}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#I2S}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code i2s}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value</em> &rarr; &hellip;, <em>result</em></dd>
     * </dl>
     *
     * @see OpcodeValue#I2S
     * @see #I2B
     * @see #I2C
     * @see #I2D
     * @see #I2F
     * @see #I2L
     */
    I2S(OpcodeValue.I2S),
    /**
     * <p>
     * {@code iadd} - Add {@code int}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#IADD}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code iadd}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>value1</em>, <em>value2</em> &rarr;
     * &hellip;, <em>result</em>
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#IADD
     * @see #DADD
     * @see #FADD
     * @see #LADD
     * @see #ISUB
     */
    IADD(OpcodeValue.IADD),
    /**
     * <p>
     * {@code iaload} - Load {@code int} from array
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#IALOAD}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code iaload}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>arrayref</em>, <em>index</em> &rarr;
     * &hellip;, <em>value</em>
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#IALOAD
     * @see #IASTORE
     * @see #AALOAD
     * @see #BALOAD
     * @see #CALOAD
     * @see #DALOAD
     * @see #FALOAD
     * @see #LALOAD
     * @see #SALOAD
     */
    IALOAD(OpcodeValue.IALOAD),
    /**
     * <p>
     * {@code iand} - Boolean AND {@code int}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#IAND}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code iand}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>value1</em>, <em>value2</em> &rarr;
     * &hellip;, <em>result</em>
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#IAND
     * @see #IOR
     * @see #IXOR
     * @see #ISHL
     * @see #ISHR
     * @see #IUSHR
     * @see #LAND
     */
    IAND(OpcodeValue.IAND),
    /**
     * <p>
     * {@code iastore} - Store into {@code int} array
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#IASTORE}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code iastore}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>arrayref</em>, <em>index</em>, <em>value</em> &rarr;
     * &hellip;
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#IASTORE
     * @see #IALOAD
     * @see #AASTORE
     * @see #BASTORE
     * @see #CASTORE
     * @see #DASTORE
     * @see #FASTORE
     * @see #LASTORE
     * @see #SASTORE
     */
    IASTORE(OpcodeValue.IASTORE),
    /**
     * <p>
     * {@code iconst_m1} - Push {@code int} constant -1 ("minus one")
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#ICONST_M1}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code iconst_m1}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip; &rarr; &hellip;, {@code -1}</dd>
     * </dl>
     *
     * @see OpcodeValue#ICONST_M1
     * @see #ICONST_0
     * @see #ICONST_1
     * @see #ICONST_2
     * @see #ICONST_3
     * @see #ICONST_4
     * @see #ICONST_5
     * @see OneOperandOpcode#BIPUSH
     * @see OneOperandOpcode#SIPUSH
     * @see OneOperandOpcode#ILOAD
     */
    ICONST_M1(OpcodeValue.ICONST_M1),
    /**
     * <p>
     * {@code iconst_0} - Push {@code int} constant 0
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#ICONST_0}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code iconst_0}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip; &rarr; &hellip;, {@code 0}</dd>
     * </dl>
     *
     * @see OpcodeValue#ICONST_0
     * @see #ICONST_M1
     * @see #ICONST_1
     * @see #ICONST_2
     * @see #ICONST_3
     * @see #ICONST_4
     * @see #ICONST_5
     * @see OneOperandOpcode#BIPUSH
     * @see OneOperandOpcode#SIPUSH
     * @see OneOperandOpcode#ILOAD
     * @see #DCONST_0
     * @see #FCONST_0
     * @see #LCONST_0
     */
    ICONST_0(OpcodeValue.ICONST_0),
    /**
     * <p>
     * {@code iconst_1} - Push {@code int} constant 1
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#ICONST_1}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code iconst_1}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip; &rarr; &hellip;, {@code 1}</dd>
     * </dl>
     *
     * @see OpcodeValue#ICONST_1
     * @see #ICONST_M1
     * @see #ICONST_0
     * @see #ICONST_2
     * @see #ICONST_3
     * @see #ICONST_4
     * @see #ICONST_5
     * @see OneOperandOpcode#BIPUSH
     * @see OneOperandOpcode#SIPUSH
     * @see OneOperandOpcode#ILOAD
     * @see #DCONST_1
     * @see #FCONST_1
     * @see #LCONST_1
     */
    ICONST_1(OpcodeValue.ICONST_1),
    /**
     * <p>
     * {@code iconst_2} - Push {@code int} constant 2
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#ICONST_2}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code iconst_2}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip; &rarr; &hellip;, {@code 2}</dd>
     * </dl>
     *
     * @see OpcodeValue#ICONST_2
     * @see #ICONST_M1
     * @see #ICONST_0
     * @see #ICONST_1
     * @see #ICONST_3
     * @see #ICONST_4
     * @see #ICONST_5
     * @see OneOperandOpcode#BIPUSH
     * @see OneOperandOpcode#SIPUSH
     * @see OneOperandOpcode#ILOAD
     * @see #FCONST_2
     */
    ICONST_2(OpcodeValue.ICONST_2),
    /**
     * <p>
     * {@code iconst_3} - Push {@code int} constant 3
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#ICONST_3}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code iconst_3}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip; &rarr; &hellip;, {@code 3}</dd>
     * </dl>
     *
     * @see OpcodeValue#ICONST_3
     * @see #ICONST_M1
     * @see #ICONST_0
     * @see #ICONST_1
     * @see #ICONST_2
     * @see #ICONST_4
     * @see #ICONST_5
     * @see OneOperandOpcode#BIPUSH
     * @see OneOperandOpcode#SIPUSH
     * @see OneOperandOpcode#ILOAD
     */
    ICONST_3(OpcodeValue.ICONST_3),
    /**
     * <p>
     * {@code iconst_4} - Push {@code int} constant 4
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#ICONST_4}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code iconst_4}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip; &rarr; &hellip;, {@code 4}</dd>
     * </dl>
     *
     * @see OpcodeValue#ICONST_4
     * @see #ICONST_M1
     * @see #ICONST_0
     * @see #ICONST_1
     * @see #ICONST_2
     * @see #ICONST_3
     * @see #ICONST_5
     * @see OneOperandOpcode#BIPUSH
     * @see OneOperandOpcode#SIPUSH
     * @see OneOperandOpcode#ILOAD
     */
    ICONST_4(OpcodeValue.ICONST_4),
    /**
     * <p>
     * {@code iconst_5} - Push {@code int} constant 5
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#ICONST_5}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code iconst_5}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip; &rarr; &hellip;, {@code 5}</dd>
     * </dl>
     *
     * @see OpcodeValue#ICONST_5
     * @see #ICONST_M1
     * @see #ICONST_0
     * @see #ICONST_1
     * @see #ICONST_2
     * @see #ICONST_3
     * @see #ICONST_4
     * @see OneOperandOpcode#BIPUSH
     * @see OneOperandOpcode#SIPUSH
     * @see OneOperandOpcode#ILOAD
     */
    ICONST_5(OpcodeValue.ICONST_5),
    /**
     * <p>
     * {@code idiv} - Divide {@code int}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#IDIV}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code idiv}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>value1</em>, <em>value2</em> &rarr;
     * &hellip;, <em>result</em>
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#IDIV
     * @see #DDIV
     * @see #FDIV
     * @see #LDIV
     * @see #IMUL
     * @see #IREM
     */
    IDIV(OpcodeValue.IDIV),
    /**
     * <p>
     * {@code iload_0} - Load {@code int} from local variable at index 0
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#ILOAD_0}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code iload_0}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip; &rarr; &hellip;, <em>value</em></dd>
     * </dl>
     *
     * @see OpcodeValue#ILOAD_0
     * @see #ISTORE_0
     * @see #ILOAD_1
     * @see #ILOAD_2
     * @see #ILOAD_3
     * @see OneOperandOpcode#ILOAD
     */
    ILOAD_0(OpcodeValue.ILOAD_0),
    /**
     * <p>
     * {@code iload_1} - Load {@code int} from local variable at index 1
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#ILOAD_1}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code iload_1}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip; &rarr; &hellip;, <em>value</em></dd>
     * </dl>
     *
     * @see OpcodeValue#ILOAD_1
     * @see #ISTORE_1
     * @see #ILOAD_0
     * @see #ILOAD_2
     * @see #ILOAD_3
     * @see OneOperandOpcode#ILOAD
     */
    ILOAD_1(OpcodeValue.ILOAD_1),
    /**
     * <p>
     * {@code iload_2} - Load {@code int} from local variable at index 2
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#ILOAD_2}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code iload_2}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip; &rarr; &hellip;, <em>value</em></dd>
     * </dl>
     *
     * @see OpcodeValue#ILOAD_2
     * @see #ISTORE_2
     * @see #ILOAD_0
     * @see #ILOAD_1
     * @see #ILOAD_3
     * @see OneOperandOpcode#ILOAD
     */
    ILOAD_2(OpcodeValue.ILOAD_2),
    /**
     * <p>
     * {@code iload_3} - Load {@code int} from local variable at index 3
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#ILOAD_3}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code iload_3}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip; &rarr; &hellip;, <em>value</em></dd>
     * </dl>
     *
     * @see OpcodeValue#ILOAD_3
     * @see #ISTORE_3
     * @see #ILOAD_0
     * @see #ILOAD_1
     * @see #ILOAD_2
     * @see OneOperandOpcode#ILOAD
     */
    ILOAD_3(OpcodeValue.ILOAD_3),
    /**
     * <p>
     * {@code impdep1} - Reserved for implementation-specific software
     * functionality, should not appear in bytecode in a class file
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#IMPDEP1}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code impdep1}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip; &rarr; ???
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#IMPDEP1
     * @see #BREAKPOINT
     * @see #IMPDEP2
     */
    IMPDEP1(OpcodeValue.IMPDEP1, true),
    /**
     * <p>
     * {@code impdep1} - Reserved for implementation-specific hardware
     * functionality, should not appear in bytecode in a class file
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#IMPDEP2}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code impdep2}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip; &rarr; ???
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#IMPDEP2
     * @see #IMPDEP1
     * @see #BREAKPOINT
     */
    IMPDEP2(OpcodeValue.IMPDEP2, true),
    /**
     * <p>
     * {@code imul} - Multiply {@code int}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#IMUL}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code imul}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>value1</em>, <em>value2</em> &rarr;
     * &hellip;, <em>result</em>
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#IMUL
     * @see #DMUL
     * @see #FMUL
     * @see #LMUL
     * @see #IDIV
     * @see #IREM
     */
    IMUL(OpcodeValue.IMUL),
    /**
     * <p>
     * {@code ineg} - Negate {@code int}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#INEG}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code ineg}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value</em> &rarr; &hellip;, <em>result</em></dd>
     * </dl>
     *
     * @see OpcodeValue#INEG
     * @see #DNEG
     * @see #FNEG
     * @see #LNEG
     * @see #ISUB
     */
    INEG(OpcodeValue.INEG),
    /**
     * <p>
     * {@code ior} - Boolean OR {@code int}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#IOR}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code ior}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>value1</em>, <em>value2</em> &rarr;
     * &hellip;, <em>result</em>
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#IOR
     * @see #IAND
     * @see #IXOR
     * @see #ISHL
     * @see #ISHR
     * @see #IUSHR
     * @see #LOR
     */
    IOR(OpcodeValue.IOR),
    /**
     * <p>
     * {@code irem} - Remainder {@code int}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#IREM}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code irem}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>value1</em>, <em>value2</em> &rarr;
     * &hellip;, <em>result</em>
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#IREM
     * @see #DREM
     * @see #FREM
     * @see #LREM
     * @see #IDIV
     * @see #IMUL
     */
    IREM(OpcodeValue.IREM),
    /**
     * <p>
     * {@code ireturn} - Return {@code int} from method
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#IRETURN}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code ireturn}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value</em> &rarr; [empty]</dd>
     * </dl>
     *
     * @see OpcodeValue#IRETURN
     * @see #ARETURN
     * @see #DRETURN
     * @see #FRETURN
     * @see #LRETURN
     * @see #RETURN
     */
    IRETURN(OpcodeValue.IRETURN),
    /**
     * <p>
     * {@code ishl} - Shift left {@code int}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#ISHL}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code ishl}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>value1</em>, <em>value2</em> &rarr;
     * &hellip;, <em>result</em>
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#ISHL
     * @see #ISHR
     * @see #IUSHR
     * @see #IAND
     * @see #IOR
     * @see #IXOR
     * @see #LSHL
     */
    ISHL(OpcodeValue.ISHL),
    /**
     * <p>
     * {@code ishr} - Arithmetic shift right {@code int}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#ISHR}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code ishr}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>value1</em>, <em>value2</em> &rarr;
     * &hellip;, <em>result</em>
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#ISHR
     * @see #IUSHR
     * @see #ISHL
     * @see #IAND
     * @see #IOR
     * @see #IXOR
     * @see #LSHR
     */
    ISHR(OpcodeValue.ISHR),
    /**
     * <p>
     * {@code istore_0} - Store {@code int} into local variable at index 0
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#ISTORE_0}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code istore_0}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value</em> &rarr; &hellip;</dd>
     * </dl>
     *
     * @see OpcodeValue#ISTORE_0
     * @see #ILOAD_0
     * @see #ISTORE_1
     * @see #ISTORE_2
     * @see #ISTORE_3
     * @see OneOperandOpcode#ISTORE
     */
    ISTORE_0(OpcodeValue.ISTORE_0),
    /**
     * <p>
     * {@code istore_1} - Store {@code int} into local variable at index 1
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#ISTORE_1}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code istore_1}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value</em> &rarr; &hellip;</dd>
     * </dl>
     *
     * @see OpcodeValue#ISTORE_1
     * @see #ILOAD_1
     * @see #ISTORE_0
     * @see #ISTORE_2
     * @see #ISTORE_3
     * @see OneOperandOpcode#ISTORE
     */
    ISTORE_1(OpcodeValue.ISTORE_1),
    /**
     * <p>
     * {@code istore_2} - Store {@code int} into local variable at index 2
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#ISTORE_2}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code istore_2}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value</em> &rarr; &hellip;</dd>
     * </dl>
     *
     * @see OpcodeValue#ISTORE_2
     * @see #ILOAD_2
     * @see #ISTORE_0
     * @see #ISTORE_1
     * @see #ISTORE_3
     * @see OneOperandOpcode#ISTORE
     */
    ISTORE_2(OpcodeValue.ISTORE_2),
    /**
     * <p>
     * {@code istore_3} - Store {@code int} into local variable at index 3
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#ISTORE_3}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code istore_3}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value</em> &rarr; &hellip;</dd>
     * </dl>
     *
     * @see OpcodeValue#ISTORE_3
     * @see #ILOAD_3
     * @see #ISTORE_0
     * @see #ISTORE_1
     * @see #ISTORE_2
     * @see OneOperandOpcode#ISTORE
     */
    ISTORE_3(OpcodeValue.ISTORE_3),
    /**
     * <p>
     * {@code isub} - Subtract {@code int}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#ISUB}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code isub}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>value1</em>, <em>value2</em> &rarr;
     * &hellip;, <em>result</em>
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#ISUB
     * @see #DSUB
     * @see #FSUB
     * @see #LSUB
     * @see #INEG
     * @see #IADD
     */
    ISUB(OpcodeValue.ISUB),
    /**
     * <p>
     * {@code iushr} - Logical shift right {@code int}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#IUSHR}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code iushr}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>value1</em>, <em>value2</em> &rarr;
     * &hellip;, <em>result</em>
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#IUSHR
     * @see #ISHR
     * @see #ISHL
     * @see #IAND
     * @see #IOR
     * @see #IXOR
     * @see #LUSHR
     */
    IUSHR(OpcodeValue.IUSHR),
    /**
     * <p>
     * {@code ixor} - Boolean XOR {@code int}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#IXOR}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code ixor}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>value1</em>, <em>value2</em> &rarr;
     * &hellip;, <em>result</em>
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#IXOR
     * @see #IAND
     * @see #IOR
     * @see #ISHL
     * @see #ISHR
     * @see #IUSHR
     * @see #LXOR
     */
    IXOR(OpcodeValue.IXOR),
    /**
     * <p>
     * {@code l2d} - Convert {@code long} to {@code double}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#L2D}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code l2d}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value</em> &rarr; &hellip;, <em>result</em></dd>
     * </dl>
     *
     * @see OpcodeValue#L2D
     * @see #D2L
     * @see #L2F
     * @see #L2I
     */
    L2D(OpcodeValue.L2D),
    /**
     * <p>
     * {@code l2f} - Convert {@code long} to {@code float}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#L2F}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code l2f}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value</em> &rarr; &hellip;, <em>result</em></dd>
     * </dl>
     *
     * @see OpcodeValue#L2F
     * @see #F2L
     * @see #L2D
     * @see #L2I
     */
    L2F(OpcodeValue.L2F),
    /**
     * <p>
     * {@code l2i} - Convert {@code long} to {@code int}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#L2I}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code l2i}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value</em> &rarr; &hellip;, <em>result</em></dd>
     * </dl>
     *
     * @see OpcodeValue#L2I
     * @see #I2L
     * @see #L2D
     * @see #L2F
     */
    L2I(OpcodeValue.L2I),
    /**
     * <p>
     * {@code ladd} - Add {@code long}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#LADD}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code ladd}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>value1</em>, <em>value2</em> &rarr;
     * &hellip;, <em>result</em>
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#LADD
     * @see #DADD
     * @see #FADD
     * @see #IADD
     * @see #LSUB
     */
    LADD(OpcodeValue.LADD),
    /**
     * <p>
     * {@code laload} - Load {@code long} from array
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#LALOAD}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code laload}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>arrayref</em>, <em>index</em> &rarr;
     * &hellip;, <em>value</em>
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#LALOAD
     * @see #LASTORE
     * @see #AALOAD
     * @see #BALOAD
     * @see #CALOAD
     * @see #DALOAD
     * @see #FALOAD
     * @see #IALOAD
     * @see #SALOAD
     */
    LALOAD(OpcodeValue.LALOAD),
    /**
     * <p>
     * {@code land} - Boolean AND {@code long}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#LAND}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code land}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>value1</em>, <em>value2</em> &rarr;
     * &hellip;, <em>result</em>
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#LAND
     * @see #LOR
     * @see #LXOR
     * @see #LSHL
     * @see #LSHR
     * @see #LUSHR
     * @see #IAND
     */
    LAND(OpcodeValue.LAND),
    /**
     * <p>
     * {@code lastore} - Store into {@code long} array
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#LASTORE}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code lastore}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>arrayref</em>, <em>index</em>, <em>value</em> &rarr;
     * &hellip;
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#LASTORE
     * @see #LALOAD
     * @see #AASTORE
     * @see #BASTORE
     * @see #CASTORE
     * @see #DASTORE
     * @see #FASTORE
     * @see #IASTORE
     * @see #SASTORE
     */
    LASTORE(OpcodeValue.LASTORE),
    /**
     * <p>
     * {@code lcmp} - Compare {@code long}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#LCMP}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code lcmp}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>value1</em>, <em>value2</em> &rarr;
     * &hellip;, <em>result</em>
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#LCMP
     * @see #DCMPG
     * @see #DCMPL
     * @see #FCMPG
     * @see #FCMPL
     * @see OneOperandOpcode
     */
    LCMP(OpcodeValue.LCMP),
    /**
     * <p>
     * {@code lconst_0} - Push {@code long} constant {@code 0L}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#LCONST_0}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code lconst_0}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip; &rarr; &hellip;, {@code 0L}</dd>
     * </dl>
     *
     * @see OpcodeValue#LCONST_0
     * @see #LCONST_1
     * @see #LLOAD_0
     * @see #LLOAD_1
     * @see #LLOAD_2
     * @see #LLOAD_3
     * @see #DCONST_0
     * @see #FCONST_0
     * @see #ICONST_0
     * @see OneOperandOpcode#LLOAD
     */
    LCONST_0(OpcodeValue.LCONST_0),
    /**
     * <p>
     * {@code lconst_1} - Push {@code long} constant {@code 1L}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#LCONST_1}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code lconst_1}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip; &rarr; &hellip;, {@code 1L}</dd>
     * </dl>
     *
     * @see OpcodeValue#LCONST_1
     * @see #LCONST_0
     * @see #LLOAD_0
     * @see #LLOAD_1
     * @see #LLOAD_2
     * @see #LLOAD_3
     * @see #DCONST_1
     * @see #FCONST_1
     * @see #ICONST_1
     * @see OneOperandOpcode#LLOAD
     */
    LCONST_1(OpcodeValue.LCONST_1),
    /**
     * <p>
     * {@code ldiv} - Divide {@code long}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#LDIV}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code ldiv}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>value1</em>, <em>value2</em> &rarr;
     * &hellip;, <em>result</em>
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#LDIV
     * @see #DDIV
     * @see #FDIV
     * @see #IDIV
     * @see #LMUL
     * @see #LREM
     */
    LDIV(OpcodeValue.LDIV),
    /**
     * <p>
     * {@code lload_0} - Load {@code long} from local variable at index 0
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#LLOAD_0}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code lload_0}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip; &rarr; &hellip;, <em>value</em></dd>
     * </dl>
     *
     * @see OpcodeValue#LLOAD_0
     * @see #LSTORE_0
     * @see #LLOAD_1
     * @see #LLOAD_2
     * @see #LLOAD_3
     * @see OneOperandOpcode#LLOAD
     */
    LLOAD_0(OpcodeValue.LLOAD_0),
    /**
     * <p>
     * {@code lload_1} - Load {@code long} from local variable at index 1
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#LLOAD_1}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code lload_1}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip; &rarr; &hellip;, <em>value</em></dd>
     * </dl>
     *
     * @see OpcodeValue#LLOAD_1
     * @see #LSTORE_1
     * @see #LLOAD_0
     * @see #LLOAD_2
     * @see #LLOAD_3
     * @see OneOperandOpcode#LLOAD
     */
    LLOAD_1(OpcodeValue.LLOAD_1),
    /**
     * <p>
     * {@code lload_2} - Load {@code long} from local variable at index 2
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#LLOAD_2}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code lload_2}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip; &rarr; &hellip;, <em>value</em></dd>
     * </dl>
     *
     * @see OpcodeValue#LLOAD_2
     * @see #LSTORE_2
     * @see #LLOAD_0
     * @see #LLOAD_1
     * @see #LLOAD_3
     * @see OneOperandOpcode#LLOAD
     */
    LLOAD_2(OpcodeValue.LLOAD_2),
    /**
     * <p>
     * {@code lload_3} - Load {@code long} from local variable at index 3
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#LLOAD_3}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code lload_3}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip; &rarr; &hellip;, <em>value</em></dd>
     * </dl>
     *
     * @see OpcodeValue#LLOAD_3
     * @see #LSTORE_3
     * @see #LLOAD_0
     * @see #LLOAD_1
     * @see #LLOAD_2
     * @see OneOperandOpcode#LLOAD
     */
    LLOAD_3(OpcodeValue.LLOAD_3),
    /**
     * <p>
     * {@code lmul} - Multiply {@code long}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#LMUL}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code lmul}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>value1</em>, <em>value2</em> &rarr;
     * &hellip;, <em>result</em>
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#LMUL
     * @see #DMUL
     * @see #FMUL
     * @see #IMUL
     * @see #LDIV
     * @see #LREM
     */
    LMUL(OpcodeValue.LMUL),
    /**
     * <p>
     * {@code lneg} - Negate {@code long}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#LNEG}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code lneg}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value</em> &rarr; &hellip;, <em>result</em></dd>
     * </dl>
     *
     * @see OpcodeValue#LNEG
     * @see #DNEG
     * @see #FNEG
     * @see #INEG
     * @see #LSUB
     */
    LNEG(OpcodeValue.LNEG),
    /**
     * <p>
     * {@code lor} - Boolean OR {@code long}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#LOR}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code lor}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>value1</em>, <em>value2</em> &rarr;
     * &hellip;, <em>result</em>
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#LOR
     * @see #LAND
     * @see #LXOR
     * @see #LSHL
     * @see #LSHR
     * @see #LUSHR
     * @see #IOR
     */
    LOR(OpcodeValue.LOR),
    /**
     * <p>
     * {@code lrem} - Remainder {@code long}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#LREM}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code lrem}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>value1</em>, <em>value2</em> &rarr;
     * &hellip;, <em>result</em>
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#LREM
     * @see #DREM
     * @see #FREM
     * @see #IREM
     * @see #LDIV
     * @see #LMUL
     */
    LREM(OpcodeValue.LREM),
    /**
     * <p>
     * {@code lreturn} - Return {@code long} from method
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#LRETURN}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code lreturn}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value</em> &rarr; [empty]</dd>
     * </dl>
     *
     * @see OpcodeValue#LRETURN
     * @see #ARETURN
     * @see #DRETURN
     * @see #FRETURN
     * @see #IRETURN
     * @see #RETURN
     */
    LRETURN(OpcodeValue.LRETURN),
    /**
     * <p>
     * {@code lshl} - Shift left {@code long}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#LSHL}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code lshl}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>value1</em>, <em>value2</em> &rarr;
     * &hellip;, <em>result</em>
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#LSHL
     * @see #LSHR
     * @see #LUSHR
     * @see #LAND
     * @see #LOR
     * @see #LXOR
     * @see #ISHL
     */
    LSHL(OpcodeValue.LSHL),
    /**
     * <p>
     * {@code lshr} - Arithmetic shift right {@code long}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#LSHR}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code lshr}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>value1</em>, <em>value2</em> &rarr;
     * &hellip;, <em>result</em>
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#LSHR
     * @see #LUSHR
     * @see #LSHL
     * @see #LAND
     * @see #LOR
     * @see #LXOR
     * @see #ISHR
     */
    LSHR(OpcodeValue.LSHR),
    /**
     * <p>
     * {@code lstore_0} - Store {@code long} into local variable at index 0
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#LSTORE_0}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code lstore_0}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value</em> &rarr; &hellip;</dd>
     * </dl>
     *
     * @see OpcodeValue#LSTORE_0
     * @see #LLOAD_0
     * @see #LSTORE_1
     * @see #LSTORE_2
     * @see #LSTORE_3
     * @see OneOperandOpcode#LSTORE
     */
    LSTORE_0(OpcodeValue.LSTORE_0),
    /**
     * <p>
     * {@code lstore_1} - Store {@code long} into local variable at index 1
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#LSTORE_1}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code lstore_1}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value</em> &rarr; &hellip;</dd>
     * </dl>
     *
     * @see OpcodeValue#LSTORE_1
     * @see #LLOAD_1
     * @see #LSTORE_0
     * @see #LSTORE_2
     * @see #LSTORE_3
     * @see OneOperandOpcode#LSTORE
     */
    LSTORE_1(OpcodeValue.LSTORE_1),
    /**
     * <p>
     * {@code lstore_2} - Store {@code long} into local variable at index 2
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#LSTORE_2}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code lstore_2}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value</em> &rarr; &hellip;</dd>
     * </dl>
     *
     * @see OpcodeValue#LSTORE_2
     * @see #LLOAD_2
     * @see #LSTORE_0
     * @see #LSTORE_1
     * @see #LSTORE_3
     * @see OneOperandOpcode#LSTORE
     */
    LSTORE_2(OpcodeValue.LSTORE_2),
    /**
     * <p>
     * {@code lstore_3} - Store {@code long} into local variable at index 3
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#LSTORE_3}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code lstore_3}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value</em> &rarr; &hellip;</dd>
     * </dl>
     *
     * @see OpcodeValue#LSTORE_3
     * @see #LLOAD_3
     * @see #LSTORE_0
     * @see #LSTORE_1
     * @see #LSTORE_2
     * @see OneOperandOpcode#LSTORE
     */
    LSTORE_3(OpcodeValue.LSTORE_3),
    /**
     * <p>
     * {@code lsub} - Subtract {@code long}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#LSUB}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code lsub}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>value1</em>, <em>value2</em> &rarr;
     * &hellip;, <em>result</em>
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#LSUB
     * @see #DSUB
     * @see #FSUB
     * @see #ISUB
     * @see #LNEG
     * @see #LADD
     */
    LSUB(OpcodeValue.LSUB),
    /**
     * <p>
     * {@code lushr} - Logical shift right {@code long}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#LUSHR}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code lushr}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>value1</em>, <em>value2</em> &rarr;
     * &hellip;, <em>result</em>
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#LUSHR
     * @see #LSHR
     * @see #LSHL
     * @see #LAND
     * @see #LOR
     * @see #LXOR
     * @see #IUSHR
     */
    LUSHR(OpcodeValue.LUSHR),
    /**
     * <p>
     * {@code lxor} - Boolean XOR {@code long}
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#LXOR}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code lxor}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>value1</em>, <em>value2</em> &rarr;
     * &hellip;, <em>result</em>
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#LXOR
     * @see #LAND
     * @see #LOR
     * @see #LSHL
     * @see #LSHR
     * @see #LUSHR
     * @see #IXOR
     */
    LXOR(OpcodeValue.LXOR),
    /**
     * <p>
     * {@code monitorenter} - Enter monitor for object
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#MONITORENTER}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code monitorenter}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>objectref</em> &rarr; &hellip;</dd>
     * </dl>
     *
     * @see OpcodeValue#MONITORENTER
     * @see #MONITOREXIT
     */
    MONITORENTER(OpcodeValue.MONITORENTER),
    /**
     * <p>
     * {@code monitorexit} - Exit monitor for object
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#MONITOREXIT}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code monitorexit}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>objectref</em> &rarr; &hellip;</dd>
     * </dl>
     *
     * @see OpcodeValue#MONITOREXIT
     * @see #MONITORENTER
     */
    MONITOREXIT(OpcodeValue.MONITOREXIT),
    /**
     * <p>
     * {@code nop} - Do nothing
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#NOP}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code nop}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip; &rarr; &hellip;</dd>
     * </dl>
     *
     * @see OpcodeValue#NOP
     */
    NOP(OpcodeValue.NOP),
    /**
     * <p>
     * {@code pop} - Pop the top operand stack value
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#POP}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code pop}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip;, <em>value</em> &rarr; &hellip;</dd>
     * </dl>
     *
     * @see OpcodeValue#POP
     * @see #POP2
     * @see #DUP
     * @see #DUP_X1
     * @see #DUP_X2
     * @see #SWAP
     */
    POP(OpcodeValue.POP),
    /**
     * <p>
     * {@code pop2} - Pop the top one or two operand stack values
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#POP2}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code pop2}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * <p>Form 1:</p>
     * <p>&hellip;, <em>value2</em>, <em>value1</em> &rarr; &hellip;</p>
     * <p>
     * where <em>value1</em> and <em>value2</em> are both values of a category 1
     * computational type.
     * </p>
     * <p>Form 2:</p>
     * <p>&hellip;, <em>value</em> &rarr; &hellip;</p>
     * <p>
     * where <em>value</em> is a value of a category 2 computational type.
     * </p>
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#POP2
     * @see #POP
     * @see #DUP2
     * @see #DUP2_X1
     * @see #DUP2_X2
     */
    POP2(OpcodeValue.POP2),
    /**
     * <p>
     * {@code return} - Return {@code void} from method
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#RETURN}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code return}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>&hellip; &rarr; [empty]</dd>
     * </dl>
     *
     * @see OpcodeValue#RETURN
     * @see #ARETURN
     * @see #DRETURN
     * @see #FRETURN
     * @see #IRETURN
     * @see #LRETURN
     * @see OneOperandOpcode#RET
     */
    RETURN(OpcodeValue.RETURN),
    /**
     * <p>
     * {@code saload} - Load {@code short} from array
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#SALOAD}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code saload}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>arrayref</em>, <em>idnex</em> &rarr;
     * &hellip;, <em>value</em>
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#SALOAD
     * @see #SASTORE
     * @see #AALOAD
     * @see #BALOAD
     * @see #CALOAD
     * @see #DALOAD
     * @see #FALOAD
     * @see #IALOAD
     * @see #LALOAD
     */
    SALOAD(OpcodeValue.SALOAD),
    /**
     * <p>
     * {@code sastore} - Store into {@code short} array
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#SASTORE}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code sastore}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>arrayref</em>, <em>index</em>, <em>value</em> &rarr;
     * &hellip;
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#SASTORE
     * @see #SALOAD
     * @see #AASTORE
     * @see #BASTORE
     * @see #CASTORE
     * @see #DASTORE
     * @see #FASTORE
     * @see #IASTORE
     * @see #LASTORE
     */
    SASTORE(OpcodeValue.SASTORE),
    /**
     * <p>
     * {@code swap} - Swap the top two operand stack values
     * </p>
     *
     * <dl>
     * <dt><strong>Opcode</strong></dt>
     * <dd>{@value io.tarro.base.bytecode.OpcodeValue#SWAP}</dd>
     * <dt><strong>Format</strong></dt>
     * <dd>{@code swap}</dd>
     * <dt><strong>Operand Stack</strong></dt>
     * <dd>
     * &hellip;, <em>value2</em>, <em>value1</em> &rarr;
     * &hellip;, <em>value1</em>, <em>value2</em>
     * </dd>
     * </dl>
     *
     * @see OpcodeValue#SWAP
     * @see #DUP
     * @see #DUP_X1
     * @see #DUP_X2
     * @see #POP
     */
    SWAP(OpcodeValue.SWAP);

    //
    // DATA
    //

    private final int value;
    private final boolean reserved;

    //
    // CONSTRUCTORS
    //

    NoOperandOpcode(final int value, final boolean reserved) {
        this.value = value;
        this.reserved = reserved;
    }

    NoOperandOpcode(final int value) {
        this(value, false);
    }

    //
    // INTERFACE: Opcode
    //

    @Override
    public List<OperandType> getOperandTypes() {
        return emptyList();
    }

    @Override
    public int getNumOperands() {
        return 0;
    }

    @Override
    public boolean isReserved() {
        return reserved;
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
     * Obtains the {@link NoOperandOpcode} for the given opcode byte value.
     *
     * @param value Opcode byte value
     * @return Opcode instance
     * @throws IllegalArgumentException If {@code value} is not in the range
     *                                  0..255 <em>or</em> the value is in the
     *                                  correct range but that value does not
     *                                  correspond to a known no-operand opcode
     * @see OpcodeValue
     * @see Opcode#forUnsignedByte(int)
     * @see OneOperandOpcode#forUnsignedByte(int)
     * @see TwoOperandOpcode#forUnsignedByte(int)
     * @see VariableOperandOpcode#forUnsignedByte(int)
     */
    public static NoOperandOpcode forUnsignedByte(final int value) {
        return BY_VALUE.forUnsignedByte(value);
    }

    //
    // INTERNALS
    //

    private static final OpcodeTable<NoOperandOpcode> BY_VALUE =
            forOpcodeEnum(NoOperandOpcode.class);
}
