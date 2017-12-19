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

import static io.tarro.base.bytecode.OpcodeTable.forOpcodeEnum;
import static java.util.Collections.emptyList;

/**
 * Enumerates opcodes for Java Virtual Machine instructions which do not have
 * any operands.
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
    AALOAD(OpcodeValue.AALOAD),
    AASTORE(OpcodeValue.AASTORE),
    ACONST_NULL(OpcodeValue.ACONST_NULL),
    ALOAD_0(OpcodeValue.ALOAD_0),
    ALOAD_1(OpcodeValue.ALOAD_1),
    ALOAD_2(OpcodeValue.ALOAD_2),
    ALOAD_3(OpcodeValue.ALOAD_3),
    ARETURN(OpcodeValue.ARETURN),
    ARRAYLENGTH(OpcodeValue.ARRAYLENGTH),
    ASTORE_0(OpcodeValue.ASTORE_0),
    ASTORE_1(OpcodeValue.ASTORE_1),
    ASTORE_2(OpcodeValue.ASTORE_2),
    ASTORE_3(OpcodeValue.ASTORE_3),
    ATHROW(OpcodeValue.ATHROW),
    BALOAD(OpcodeValue.BALOAD),
    BASTORE(OpcodeValue.BASTORE),
    BREAKPOINT(OpcodeValue.BREAKPOINT, true),
    CALOAD(OpcodeValue.CALOAD),
    CASTORE(OpcodeValue.CASTORE),
    D2F(OpcodeValue.D2F),
    D2I(OpcodeValue.D2I),
    D2L(OpcodeValue.D2L),
    DADD(OpcodeValue.DADD),
    DALOAD(OpcodeValue.DALOAD),
    DASTORE(OpcodeValue.DASTORE),
    DCMPG(OpcodeValue.DCMPG),
    DCMPL(OpcodeValue.DCMPL),
    DCONST_0(OpcodeValue.DCONST_0),
    DCONST_1(OpcodeValue.DCONST_1),
    DDIV(OpcodeValue.DDIV),
    DLOAD_0(OpcodeValue.DLOAD_0),
    DLOAD_1(OpcodeValue.DLOAD_1),
    DLOAD_2(OpcodeValue.DLOAD_2),
    DLOAD_3(OpcodeValue.DLOAD_3),
    DMUL(OpcodeValue.DMUL),
    DNEG(OpcodeValue.DNEG),
    DREM(OpcodeValue.DREM),
    DRETURN(OpcodeValue.DRETURN),
    DSTORE_0(OpcodeValue.DSTORE_0),
    DSTORE_1(OpcodeValue.DSTORE_1),
    DSTORE_2(OpcodeValue.DSTORE_2),
    DSTORE_3(OpcodeValue.DSTORE_3),
    DSUB(OpcodeValue.DSUB),
    DUP(OpcodeValue.DUP),
    DUP_X1(OpcodeValue.DUP_X1),
    DUP_X2(OpcodeValue.DUP_X2),
    DUP2(OpcodeValue.DUP2),
    DUP2_X1(OpcodeValue.DUP2_X1),
    DUP2_X2(OpcodeValue.DUP2_X2),
    F2D(OpcodeValue.F2D),
    F2I(OpcodeValue.F2I),
    F2L(OpcodeValue.F2L),
    FADD(OpcodeValue.FADD),
    FALOAD(OpcodeValue.FALOAD),
    FASTORE(OpcodeValue.FASTORE),
    FCMPG(OpcodeValue.FCMPG),
    FCMPL(OpcodeValue.FCMPL),
    FCONST_0(OpcodeValue.FCONST_0),
    FCONST_1(OpcodeValue.FCONST_1),
    FCONST_2(OpcodeValue.FCONST_2),
    FDIV(OpcodeValue.FDIV),
    FLOAD_0(OpcodeValue.FLOAD_0),
    FLOAD_1(OpcodeValue.FLOAD_1),
    FLOAD_2(OpcodeValue.FLOAD_2),
    FLOAD_3(OpcodeValue.FLOAD_3),
    FMUL(OpcodeValue.FMUL),
    FNEG(OpcodeValue.FNEG),
    FREM(OpcodeValue.FREM),
    FRETURN(OpcodeValue.FRETURN),
    FSTORE_0(OpcodeValue.FSTORE_0),
    FSTORE_1(OpcodeValue.FSTORE_1),
    FSTORE_2(OpcodeValue.FSTORE_2),
    FSTORE_3(OpcodeValue.FSTORE_3),
    FSUB(OpcodeValue.FSUB),
    I2B(OpcodeValue.I2B),
    I2C(OpcodeValue.I2C),
    I2D(OpcodeValue.I2D),
    I2F(OpcodeValue.I2F),
    I2L(OpcodeValue.I2L),
    I2S(OpcodeValue.I2S),
    IADD(OpcodeValue.IADD),
    IALOAD(OpcodeValue.IALOAD),
    IAND(OpcodeValue.IAND),
    IASTORE(OpcodeValue.IASTORE),
    ICONST_M1(OpcodeValue.ICONST_M1),
    ICONST_0(OpcodeValue.ICONST_0),
    ICONST_1(OpcodeValue.ICONST_1),
    ICONST_2(OpcodeValue.ICONST_2),
    ICONST_3(OpcodeValue.ICONST_3),
    ICONST_4(OpcodeValue.ICONST_4),
    ICONST_5(OpcodeValue.ICONST_5),
    IDIV(OpcodeValue.IDIV),
    ILOAD_0(OpcodeValue.ILOAD_0),
    ILOAD_1(OpcodeValue.ILOAD_1),
    ILOAD_2(OpcodeValue.ILOAD_2),
    ILOAD_3(OpcodeValue.ILOAD_3),
    IMPDEP1(OpcodeValue.IMPDEP1, true),
    IMPDEP2(OpcodeValue.IMPDEP2, true),
    IMUL(OpcodeValue.IMUL),
    INEG(OpcodeValue.INEG),
    IOR(OpcodeValue.IOR),
    IREM(OpcodeValue.IREM),
    IRETURN(OpcodeValue.IRETURN),
    ISHL(OpcodeValue.ISHL),
    ISHR(OpcodeValue.ISHR),
    ISTORE_0(OpcodeValue.ISTORE_0),
    ISTORE_1(OpcodeValue.ISTORE_1),
    ISTORE_2(OpcodeValue.ISTORE_2),
    ISTORE_3(OpcodeValue.ISTORE_3),
    ISUB(OpcodeValue.ISUB),
    IUSHR(OpcodeValue.IUSHR),
    IXOR(OpcodeValue.IXOR),
    L2D(OpcodeValue.L2D),
    L2F(OpcodeValue.L2F),
    L2I(OpcodeValue.L2I),
    LADD(OpcodeValue.LADD),
    LALOAD(OpcodeValue.LALOAD),
    LAND(OpcodeValue.LAND),
    LASTORE(OpcodeValue.LASTORE),
    LCMP(OpcodeValue.LCMP),
    LCONST_0(OpcodeValue.LCONST_0),
    LCONST_1(OpcodeValue.LCONST_1),
    LDIV(OpcodeValue.LDIV),
    LLOAD_0(OpcodeValue.LLOAD_0),
    LLOAD_1(OpcodeValue.LLOAD_1),
    LLOAD_2(OpcodeValue.LLOAD_2),
    LLOAD_3(OpcodeValue.LLOAD_3),
    LMUL(OpcodeValue.LMUL),
    LNEG(OpcodeValue.LNEG),
    LOR(OpcodeValue.LOR),
    LREM(OpcodeValue.LREM),
    LRETURN(OpcodeValue.LRETURN),
    LSHL(OpcodeValue.LSHL),
    LSHR(OpcodeValue.LSHR),
    LSTORE_0(OpcodeValue.LSTORE_0),
    LSTORE_1(OpcodeValue.LSTORE_1),
    LSTORE_2(OpcodeValue.LSTORE_2),
    LSTORE_3(OpcodeValue.LSTORE_3),
    LSUB(OpcodeValue.LSUB),
    LUSHR(OpcodeValue.LUSHR),
    LXOR(OpcodeValue.LXOR),
    MONITORENTER(OpcodeValue.MONITORENTER),
    MONITOREXIT(OpcodeValue.MONITOREXIT),
    NOP(OpcodeValue.NOP),
    POP(OpcodeValue.POP),
    POP2(OpcodeValue.POP2),
    RETURN(OpcodeValue.RETURN),
    SALOAD(OpcodeValue.SALOAD),
    SASTORE(OpcodeValue.SASTORE),
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

    public static NoOperandOpcode forUnsignedByte(final int value) {
        return BY_VALUE.forUnsignedByte(value);
    }

    //
    // INTERNALS
    //

    private static final OpcodeTable<NoOperandOpcode> BY_VALUE =
            forOpcodeEnum(NoOperandOpcode.class);
}
