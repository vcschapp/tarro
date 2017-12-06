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
import java.util.Optional;

import static io.tarro.base.ClassFileVersion.JAVA1;
import static io.tarro.base.ClassFileVersion.JAVA7;
import static io.tarro.base.bytecode.OperandType.SIGNED_VALUE_INT;
import static io.tarro.base.bytecode.OperandType.JUMP_OFFSET_TABLE;
import static io.tarro.base.bytecode.OperandType.MATCH_OFFSET_PAIRS_TABLE;
import static io.tarro.base.bytecode.OperandType.PADDING;
import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static io.tarro.base.bytecode.OperandType.ATYPE_BYTE;
import static io.tarro.base.bytecode.OperandType.BRANCH_OFFSET_SHORT;
import static io.tarro.base.bytecode.OperandType.BRANCH_OFFSET_INT;
import static io.tarro.base.bytecode.OperandType.SIGNED_VALUE_BYTE;
import static io.tarro.base.bytecode.OperandType.UNSIGNED_VALUE_BYTE;
import static io.tarro.base.bytecode.OperandType.CONSTANT_POOL_INDEX_CLASS_SHORT;
import static io.tarro.base.bytecode.OperandType.CONSTANT_POOL_INDEX_CLASS_METHOD_REF_SHORT;
import static io.tarro.base.bytecode.OperandType.CONSTANT_POOL_INDEX_CONSTANT_SHORT;
import static io.tarro.base.bytecode.OperandType.CONSTANT_POOL_INDEX_CONSTANT2_SHORT;
import static io.tarro.base.bytecode.OperandType.CONSTANT_POOL_INDEX_CONSTANT_BYTE;
import static io.tarro.base.bytecode.OperandType.CONSTANT_POOL_INDEX_EITHER_METHOD_REF_SHORT;
import static io.tarro.base.bytecode.OperandType.CONSTANT_POOL_INDEX_FIELD_REF_SHORT;
import static io.tarro.base.bytecode.OperandType.CONSTANT_POOL_INDEX_INTERFACE_METHOD_REF_SHORT;
import static io.tarro.base.bytecode.OperandType.CONSTANT_POOL_INDEX_INVOKE_DYNAMIC_SHORT;
import static io.tarro.base.bytecode.OperandType.OPCODE;
import static io.tarro.base.bytecode.OperandType.LOCAL_VARIABLE_INDEX_BYTE;
import static io.tarro.base.bytecode.OperandType.LOCAL_VARIABLE_INDEX_SHORT;
import static io.tarro.base.bytecode.OperandType.OPTIONAL_COUNT;
import static io.tarro.base.bytecode.OperandType.SIGNED_VALUE_SHORT;
import static io.tarro.base.bytecode.OperandType.ZERO;

/**
 * Enumerates the Java bytecode instructions.
 *
 * @author Victor Schappert
 * @since 20171126
 */
// TODO: Possible refactoring as follows:
// TODO: interface Opcode implements Valued, Versioned
// TODO: then make 4 enums implement OpCode: NoOperand, OneOperand, TwoOperand, and Special [wide + switch insts]
public enum Opcode implements Valued, Versioned {

    //
    // ENUMERATORS
    //

    // Keep this list in alphabetical order by opcode mnemonic.
    AALOAD(OpcodeValue.AALOAD),
    AASTORE(OpcodeValue.AASTORE),
    ACONST_NULL(OpcodeValue.ACONST_NULL),
    ALOAD(OpcodeValue.ALOAD, LOCAL_VARIABLE_INDEX_BYTE),
    ALOAD_0(OpcodeValue.ALOAD_0),
    ALOAD_1(OpcodeValue.ALOAD_1),
    ALOAD_2(OpcodeValue.ALOAD_2),
    ALOAD_3(OpcodeValue.ALOAD_3),
    ANEWARRAY(OpcodeValue.ANEWARRAY, CONSTANT_POOL_INDEX_CLASS_SHORT),
    ARETURN(OpcodeValue.ARETURN),
    ARRAYLENGTH(OpcodeValue.ARRAYLENGTH),
    ASTORE(OpcodeValue.ASTORE, LOCAL_VARIABLE_INDEX_BYTE),
    ASTORE_0(OpcodeValue.ASTORE_0),
    ASTORE_1(OpcodeValue.ASTORE_1),
    ASTORE_2(OpcodeValue.ASTORE_2),
    ASTORE_3(OpcodeValue.ASTORE_3),
    ATHROW(OpcodeValue.ATHROW),
    BALOAD(OpcodeValue.BALOAD),
    BASTORE(OpcodeValue.BASTORE),
    BIPUSH(OpcodeValue.BIPUSH, SIGNED_VALUE_BYTE),
    BREAKPOINT(OpcodeValue.BREAKPOINT, JAVA1, emptyList(), true, null),
    CALOAD(OpcodeValue.CALOAD),
    CASTORE(OpcodeValue.CASTORE),
    CHECKCAST(OpcodeValue.CHECKCAST, CONSTANT_POOL_INDEX_CLASS_SHORT),
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
    DLOAD(OpcodeValue.DLOAD, LOCAL_VARIABLE_INDEX_BYTE),
    DLOAD_0(OpcodeValue.DLOAD_0),
    DLOAD_1(OpcodeValue.DLOAD_1),
    DLOAD_2(OpcodeValue.DLOAD_2),
    DLOAD_3(OpcodeValue.DLOAD_3),
    DMUL(OpcodeValue.DMUL),
    DNEG(OpcodeValue.DNEG),
    DREM(OpcodeValue.DREM),
    DRETURN(OpcodeValue.DRETURN),
    DSTORE(OpcodeValue.DSTORE, LOCAL_VARIABLE_INDEX_BYTE),
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
    FLOAD(OpcodeValue.FLOAD, LOCAL_VARIABLE_INDEX_BYTE),
    FLOAD_0(OpcodeValue.FLOAD_0),
    FLOAD_1(OpcodeValue.FLOAD_1),
    FLOAD_2(OpcodeValue.FLOAD_2),
    FLOAD_3(OpcodeValue.FLOAD_3),
    FMUL(OpcodeValue.FMUL),
    FNEG(OpcodeValue.FNEG),
    FREM(OpcodeValue.FREM),
    FRETURN(OpcodeValue.FRETURN),
    FSTORE(OpcodeValue.FSTORE, LOCAL_VARIABLE_INDEX_BYTE),
    FSTORE_0(OpcodeValue.FSTORE_0),
    FSTORE_1(OpcodeValue.FSTORE_1),
    FSTORE_2(OpcodeValue.FSTORE_2),
    FSTORE_3(OpcodeValue.FSTORE_3),
    FSUB(OpcodeValue.FSUB),
    GETFIELD(OpcodeValue.GETFIELD, CONSTANT_POOL_INDEX_FIELD_REF_SHORT),
    GETSTATIC(OpcodeValue.GETSTATIC, CONSTANT_POOL_INDEX_FIELD_REF_SHORT),
    GOTO(OpcodeValue.GOTO, BRANCH_OFFSET_SHORT),
    GOTO_W(OpcodeValue.GOTO_W, BRANCH_OFFSET_INT),
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
    IINC(OpcodeValue.IINC, LOCAL_VARIABLE_INDEX_BYTE, SIGNED_VALUE_BYTE),
    ILOAD(OpcodeValue.ILOAD, LOCAL_VARIABLE_INDEX_BYTE),
    ILOAD_0(OpcodeValue.ILOAD_0),
    ILOAD_1(OpcodeValue.ILOAD_1),
    ILOAD_2(OpcodeValue.ILOAD_2),
    ILOAD_3(OpcodeValue.ILOAD_3),
    IMPDEP1(OpcodeValue.IMPDEP1, JAVA1, emptyList(), true, null),
    IMPDEP2(OpcodeValue.IMPDEP2, JAVA1, emptyList(), true, null),
    IMUL(OpcodeValue.IMUL),
    INEG(OpcodeValue.INEG),
    INSTANCEOF(OpcodeValue.INSTANCEOF, CONSTANT_POOL_INDEX_CLASS_SHORT),
    INVOKEDYNAMIC(OpcodeValue.INVOKEDYNAMIC, JAVA7, List.of(CONSTANT_POOL_INDEX_INVOKE_DYNAMIC_SHORT, ZERO, ZERO), false, null),
    INVOKEINTERFACE(OpcodeValue.INVOKEINTERFACE, JAVA1, List.of(
            CONSTANT_POOL_INDEX_INTERFACE_METHOD_REF_SHORT,
            UNSIGNED_VALUE_BYTE, ZERO), false, null),
    INVOKESPECIAL(OpcodeValue.INVOKESPECIAL, CONSTANT_POOL_INDEX_EITHER_METHOD_REF_SHORT),
    INVOKESTATIC(OpcodeValue.INVOKESTATIC, CONSTANT_POOL_INDEX_EITHER_METHOD_REF_SHORT),
    INVOKEVIRTUAL(OpcodeValue.INVOKEVIRTUAL, CONSTANT_POOL_INDEX_CLASS_METHOD_REF_SHORT),
    IOR(OpcodeValue.IOR),
    IREM(OpcodeValue.IREM),
    IRETURN(OpcodeValue.IRETURN),
    ISHL(OpcodeValue.ISHL),
    ISHR(OpcodeValue.ISHR),
    ISTORE(OpcodeValue.ISTORE, LOCAL_VARIABLE_INDEX_BYTE),
    ISTORE_0(OpcodeValue.ISTORE_0),
    ISTORE_1(OpcodeValue.ISTORE_1),
    ISTORE_2(OpcodeValue.ISTORE_2),
    ISTORE_3(OpcodeValue.ISTORE_3),
    ISUB(OpcodeValue.ISUB),
    IUSHR(OpcodeValue.IUSHR),
    IXOR(OpcodeValue.IXOR),
    JSR(OpcodeValue.JSR, JAVA1, singletonList(BRANCH_OFFSET_SHORT), false, JAVA7),
    JSR_W(OpcodeValue.JSR_W, JAVA1, singletonList(BRANCH_OFFSET_INT), false, JAVA7),
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
    LDC(OpcodeValue.LDC, CONSTANT_POOL_INDEX_CONSTANT_BYTE),
    LDC_W(OpcodeValue.LDC_W, CONSTANT_POOL_INDEX_CONSTANT_SHORT),
    LDC2_W(OpcodeValue.LDC2_W, CONSTANT_POOL_INDEX_CONSTANT2_SHORT),
    LDIV(OpcodeValue.LDIV),
    LLOAD(OpcodeValue.LLOAD, LOCAL_VARIABLE_INDEX_BYTE),
    LLOAD_0(OpcodeValue.LLOAD_0),
    LLOAD_1(OpcodeValue.LLOAD_1),
    LLOAD_2(OpcodeValue.LLOAD_2),
    LLOAD_3(OpcodeValue.LLOAD_3),
    LMUL(OpcodeValue.LMUL),
    LNEG(OpcodeValue.LNEG),
    LOOKUPSWITCH(OpcodeValue.LOOKUPSWITCH, JAVA1, List.of(PADDING, SIGNED_VALUE_INT, MATCH_OFFSET_PAIRS_TABLE), false, null),
    LOR(OpcodeValue.LOR),
    LREM(OpcodeValue.LREM),
    LRETURN(OpcodeValue.LRETURN),
    LSHL(OpcodeValue.LSHL),
    LSHR(OpcodeValue.LSHR),
    LSTORE(OpcodeValue.LSTORE, LOCAL_VARIABLE_INDEX_BYTE),
    LSTORE_0(OpcodeValue.LSTORE_0),
    LSTORE_1(OpcodeValue.LSTORE_1),
    LSTORE_2(OpcodeValue.LSTORE_2),
    LSTORE_3(OpcodeValue.LSTORE_3),
    LSUB(OpcodeValue.LSUB),
    LUSHR(OpcodeValue.LUSHR),
    LXOR(OpcodeValue.LXOR),
    MONITORENTER(OpcodeValue.MONITORENTER),
    MONITOREXIT(OpcodeValue.MONITOREXIT),
    MULTIANEWARRAY(OpcodeValue.MULTIANEWARRAY, CONSTANT_POOL_INDEX_CLASS_SHORT, UNSIGNED_VALUE_BYTE),
    NEW(OpcodeValue.NEW, CONSTANT_POOL_INDEX_CLASS_SHORT),
    NEWARRAY(OpcodeValue.NEWARRAY, ATYPE_BYTE),
    NOP(OpcodeValue.NOP),
    POP(OpcodeValue.POP),
    POP2(OpcodeValue.POP2),
    PUTFIELD(OpcodeValue.PUTFIELD, CONSTANT_POOL_INDEX_FIELD_REF_SHORT),
    PUTSTATIC(OpcodeValue.PUTSTATIC, CONSTANT_POOL_INDEX_FIELD_REF_SHORT),
    RET(OpcodeValue.RET, LOCAL_VARIABLE_INDEX_BYTE),
    RETURN(OpcodeValue.RETURN),
    SALOAD(OpcodeValue.SALOAD),
    SASTORE(OpcodeValue.SASTORE),
    SIPUSH(OpcodeValue.SIPUSH, SIGNED_VALUE_SHORT),
    SWAP(OpcodeValue.SWAP),
    TABLESWITCH(OpcodeValue.TABLESWITCH, JAVA1, List.of(PADDING, SIGNED_VALUE_INT,
            SIGNED_VALUE_INT, JUMP_OFFSET_TABLE), false, null),
    WIDE(OpcodeValue.WIDE, JAVA1, List.of(OPCODE, LOCAL_VARIABLE_INDEX_SHORT, OPTIONAL_COUNT), false, null);

    //
    // DATA
    //

    private final int value;
    private final ClassFileVersion classFileVersion;
    private final List<OperandType> operandTypes;
    private final boolean variableSize;
    private final boolean reserved;
    private final ClassFileVersion deprecatedAtVersion;

    //
    // CONSTRUCTORS
    //

    Opcode(final int value, final ClassFileVersion classFileVersion, final List<OperandType> operandTypes, final boolean reserved, final ClassFileVersion deprecatedAtVersion) {
        this.value = value;
        this.classFileVersion = classFileVersion;
        this.operandTypes = operandTypes;
        this.variableSize = operandTypes.stream().anyMatch(OperandType::isVariableSize);
        this.reserved = false;
        assert null == deprecatedAtVersion || classFileVersion.getMajorVersion() < deprecatedAtVersion.getMajorVersion();
        this.deprecatedAtVersion = deprecatedAtVersion;
    }

    Opcode(final int value) {
        this(value, JAVA1, emptyList(), false, null);
    }

    Opcode(final int value, final OperandType operandType) {
        this(value, JAVA1, singletonList(operandType), false, null);
    }

    Opcode(final int value, final OperandType operandType1,
            final OperandType operandType2) {
        this(value, JAVA1, List.of(operandType1, operandType2), false, null);
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
    public ClassFileVersion getClassFileVersion() {
        return classFileVersion;
    }

    //
    // PUBLIC METHODS
    //

    public List<OperandType> getOperandTypes() {
        return operandTypes;
    }

    public int getNumOperands() {
        return operandTypes.size();
    }

    public boolean isVariableSize() {
        return variableSize;
    }

    public boolean isReserved() {
        return reserved;
    }

    public Optional<ClassFileVersion> getDeprecatedAtVersion() {
        return Optional.ofNullable(deprecatedAtVersion);
    }

    //
    // PUBLIC STATICS
    //

    public static Opcode forUnsignedByte(final int unsignedByte) {
        // TODO: Doc should indicate when ArrayIndexOutOfBoundsException will be thrown
        return BY_VALUE[unsignedByte];
    }

    //
    // INTERNALS
    //

    private static final Opcode[] BY_VALUE;

    static {
        BY_VALUE = new Opcode[0b1_0000_0000];
        stream(values()).forEach(opcode -> BY_VALUE[opcode.getValue()] = opcode);
    }
}
