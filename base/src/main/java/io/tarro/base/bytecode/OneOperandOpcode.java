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
import static io.tarro.base.bytecode.OperandType.CONSTANT_POOL_INDEX_CLASS_METHOD_REF_SHORT;
import static io.tarro.base.bytecode.OperandType.CONSTANT_POOL_INDEX_CLASS_SHORT;
import static io.tarro.base.bytecode.OperandType.CONSTANT_POOL_INDEX_CONSTANT2_SHORT;
import static io.tarro.base.bytecode.OperandType.CONSTANT_POOL_INDEX_CONSTANT_BYTE;
import static io.tarro.base.bytecode.OperandType.CONSTANT_POOL_INDEX_CONSTANT_SHORT;
import static io.tarro.base.bytecode.OperandType.CONSTANT_POOL_INDEX_EITHER_METHOD_REF_SHORT;
import static io.tarro.base.bytecode.OperandType.CONSTANT_POOL_INDEX_FIELD_REF_SHORT;
import static io.tarro.base.bytecode.OperandType.CONSTANT_POOL_INDEX_INVOKEDYNAMIC_SHORT;
import static io.tarro.base.bytecode.OperandType.LOCAL_VARIABLE_INDEX_BYTE;
import static io.tarro.base.bytecode.OperandType.SIGNED_VALUE_BYTE;
import static io.tarro.base.bytecode.OperandType.SIGNED_VALUE_SHORT;
import static java.util.Collections.singletonList;
import static java.util.Optional.empty;
import static java.util.Optional.of;

/**
 * Enumerates opcodes for Java Virtual Machine instructions which require a
 * single operand.
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
    ALOAD(OpcodeValue.ALOAD, LOCAL_VARIABLE_INDEX_BYTE),
    ANEWARRAY(OpcodeValue.ANEWARRAY, CONSTANT_POOL_INDEX_CLASS_SHORT),
    ASTORE(OpcodeValue.ASTORE, LOCAL_VARIABLE_INDEX_BYTE),
    BIPUSH(OpcodeValue.BIPUSH, SIGNED_VALUE_BYTE),
    CHECKCAST(OpcodeValue.CHECKCAST, CONSTANT_POOL_INDEX_CLASS_SHORT),
    DLOAD(OpcodeValue.DLOAD, LOCAL_VARIABLE_INDEX_BYTE),
    DSTORE(OpcodeValue.DSTORE, LOCAL_VARIABLE_INDEX_BYTE),
    FLOAD(OpcodeValue.FLOAD, LOCAL_VARIABLE_INDEX_BYTE),
    FSTORE(OpcodeValue.FSTORE, LOCAL_VARIABLE_INDEX_BYTE),
    GETFIELD(OpcodeValue.GETFIELD, CONSTANT_POOL_INDEX_FIELD_REF_SHORT),
    GETSTATIC(OpcodeValue.GETSTATIC, CONSTANT_POOL_INDEX_FIELD_REF_SHORT),
    GOTO(OpcodeValue.GOTO, BRANCH_OFFSET_SHORT),
    GOTO_W(OpcodeValue.GOTO_W, BRANCH_OFFSET_INT),
    IF_ACMPEQ(OpcodeValue.IF_ACMPEQ, BRANCH_OFFSET_SHORT),
    IF_ACMPNE(OpcodeValue.IF_ACMPNE, BRANCH_OFFSET_SHORT),
    IF_ICMPEQ(OpcodeValue.IF_ICMPEQ, BRANCH_OFFSET_SHORT),
    IF_ICMPGE(OpcodeValue.IF_ICMPGE, BRANCH_OFFSET_SHORT),
    IF_ICMPGT(OpcodeValue.IF_ICMPGT, BRANCH_OFFSET_SHORT),
    IF_ICMPLE(OpcodeValue.IF_ICMPLE, BRANCH_OFFSET_SHORT),
    IF_ICMPLT(OpcodeValue.IF_ICMPLT, BRANCH_OFFSET_SHORT),
    IF_ICMPNE(OpcodeValue.IF_ICMPNE, BRANCH_OFFSET_SHORT),
    IFEQ(OpcodeValue.IFEQ, BRANCH_OFFSET_SHORT),
    IFGE(OpcodeValue.IFGE, BRANCH_OFFSET_SHORT),
    IFGT(OpcodeValue.IFGT, BRANCH_OFFSET_SHORT),
    IFLE(OpcodeValue.IFLE, BRANCH_OFFSET_SHORT),
    IFLT(OpcodeValue.IFLT, BRANCH_OFFSET_SHORT),
    IFNE(OpcodeValue.IFNE, BRANCH_OFFSET_SHORT),
    IFNONNULL(OpcodeValue.IFNONNULL, BRANCH_OFFSET_SHORT),
    IFNULL(OpcodeValue.IFNULL, BRANCH_OFFSET_SHORT),
    ILOAD(OpcodeValue.ILOAD, LOCAL_VARIABLE_INDEX_BYTE),
    INSTANCEOF(OpcodeValue.INSTANCEOF, CONSTANT_POOL_INDEX_CLASS_SHORT),
    INVOKEDYNAMIC(OpcodeValue.INVOKEDYNAMIC, CONSTANT_POOL_INDEX_INVOKEDYNAMIC_SHORT),
    INVOKESPECIAL(OpcodeValue.INVOKESPECIAL, CONSTANT_POOL_INDEX_EITHER_METHOD_REF_SHORT),
    INVOKESTATIC(OpcodeValue.INVOKESTATIC, CONSTANT_POOL_INDEX_EITHER_METHOD_REF_SHORT),
    INVOKEVIRTUAL(OpcodeValue.INVOKEVIRTUAL, CONSTANT_POOL_INDEX_CLASS_METHOD_REF_SHORT),
    ISTORE(OpcodeValue.ISTORE, LOCAL_VARIABLE_INDEX_BYTE),
    JSR(OpcodeValue.JSR, BRANCH_OFFSET_SHORT),
    JSR_W(OpcodeValue.JSR_W, BRANCH_OFFSET_INT),
    LDC(OpcodeValue.LDC, CONSTANT_POOL_INDEX_CONSTANT_BYTE),
    LDC_W(OpcodeValue.LDC_W, CONSTANT_POOL_INDEX_CONSTANT_SHORT),
    LDC2_W(OpcodeValue.LDC2_W, CONSTANT_POOL_INDEX_CONSTANT2_SHORT),
    LLOAD(OpcodeValue.LLOAD, LOCAL_VARIABLE_INDEX_BYTE),
    LSTORE(OpcodeValue.LSTORE, LOCAL_VARIABLE_INDEX_BYTE),
    NEW(OpcodeValue.NEW, CONSTANT_POOL_INDEX_CLASS_SHORT),
    NEWARRAY(OpcodeValue.NEWARRAY, ATYPE_BYTE),
    PUTFIELD(OpcodeValue.PUTFIELD, CONSTANT_POOL_INDEX_FIELD_REF_SHORT),
    PUTSTATIC(OpcodeValue.PUTSTATIC, CONSTANT_POOL_INDEX_FIELD_REF_SHORT),
    RET(OpcodeValue.RET, LOCAL_VARIABLE_INDEX_BYTE),
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

    @Override
    public int getNumTrailingZeroBytes() {
        if (INVOKEDYNAMIC != this) {
            return 0;
        } else {
            return 2;
        }
    }

    //
    // PUBLIC METHODS
    //

    public OperandType getOperandType() {
        return operandType;
    }

    //
    // PUBLIC STATICS
    //

    public static OneOperandOpcode forUnsignedByte(final int value) {
        return BY_VALUE.forUnsignedByte(value);
    }

    //
    // INTERNALS
    //

    private static final OpcodeTable<OneOperandOpcode> BY_VALUE =
            forOpcodeEnum(OneOperandOpcode.class);
}
