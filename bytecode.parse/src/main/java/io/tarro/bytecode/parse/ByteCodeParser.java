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

package io.tarro.bytecode.parse;

import io.tarro.base.bytecode.ATypeValue;
import io.tarro.base.bytecode.NoOperandOpcode;
import io.tarro.base.bytecode.OneOperandOpcode;
import io.tarro.base.bytecode.Opcode;
import io.tarro.base.bytecode.OpcodeValue;
import io.tarro.base.bytecode.OperandType;
import io.tarro.bytecode.parse.visitor.LookupSwitchVisitor;
import io.tarro.bytecode.parse.visitor.NoOperandInstructionVisitor;
import io.tarro.bytecode.parse.visitor.OneOperandInstructionVisitor;
import io.tarro.bytecode.parse.visitor.TableSwitchVisitor;
import io.tarro.bytecode.parse.visitor.TwoOperandInstructionVisitor;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.stream.Stream;

import static io.tarro.base.bytecode.OneOperandOpcode.INVOKEDYNAMIC;
import static io.tarro.base.bytecode.TwoOperandOpcode.IINC;
import static io.tarro.base.bytecode.TwoOperandOpcode.INVOKEINTERFACE;
import static io.tarro.base.bytecode.TwoOperandOpcode.MULTIANEWARRAY;
import static io.tarro.base.bytecode.VariableOperandOpcode.LOOKUPSWITCH;
import static io.tarro.base.bytecode.VariableOperandOpcode.TABLESWITCH;
import static io.tarro.base.bytecode.VariableOperandOpcode.WIDE;
import static java.lang.String.format;
import static java.nio.ByteOrder.BIG_ENDIAN;
import static java.util.Arrays.copyOfRange;
import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;

/**
 * <p>
 * Parser for blocks of Java bytecode.
 * </p>
 *
 * <p>
 * The parser does the bare minimum required to reveal the structure of the
 * bytecode to its configured visitors.
 * </p>
 *
 * <p>
 * To be more specific, the parser performs only the those structural checks and
 * transformations that <em>cannot</em> be performed by the visitors. The parser
 * therefore checks that:
 * </p>
 *
 * <ol>
 * <li>each instruction's opcode byte is a valid opcode;</li>
 * <li>
 * each instruction contains the number of bytes required by that instructions'
 * opcode;
 * </li>
 * <li>
 * for variable-sized instructions ({@code lookupswitch}, {@code tableswitch},
 * {@code wide}):
 * <ol type="a">
 * <li>
 * the operands which influence the size of the instruction ({@code npairs},
 * {@code low}, {@code high} and {@code wide}'s {@code opcode}) are within the
 * required bounds;
 * </li>
 * <li>
 * the instruction has the correct size as dictated by the size-influencing
 * operands; and
 * </li>
 * </ol>
 * </li>
 * <li>
 * for instructions in which one or more operand bytes are required to be zero,
 * those bytes are indeed zero.
 * </li>
 * </ol>
 *
 * <p>
 * In addition to the above-mentioned checks this parser performs the following
 * transformations:
 * </p>
 *
 * <ol>
 * <li>
 * operand bytes are converted to {@code int} values according to the expected
 * size and signedness of the operand;
 * </li>
 * <li>
 * padding bytes for aligned instructions ({@code lookupswitch},
 * {@code tableswitch}) are skipped; and
 * </li>
 * <li>
 * mandatory zero bytes are skipped after being verified to be zero.
 * </li>
 * </ol>
 *
 * <p>
 * The parser does no other checks or transformations.
 * </p>
 *
 * <p>
 * The consequences of the parser's behaviour, within the limits described
 * above, include (but probably aren't limited to):
 * </p>
 *
 * <ul>
 * <li>
 * local variable indices, constant pool indices, and jump offsets are not
 * validated in any way and are sent to the visitor even if they are outside
 * any permissible bounds;
 * </li>
 * <li>match-offset pairs may be out of order;</li>
 * <li>
 * a visitor will never see a {@code wide} opcode directly since it will instead
 * see the widened opcode byte (but keep in mind that the opcode offset passed
 * to the visitor is the offset of the {@code wide} instruction, not of its
 * operand);
 * </li>
 * <li>
 * a {@code newarray} instruction's {@code atype} operand might not be a valid
 * {@linkplain ATypeValue primitive array type constant}; and
 * </li>
 * <li>
 * the reserved opcodes {@code breakpoint}, {@code impdep1}, and {@code impdep2}
 * will be sent to a visitor without exception if encountered by the parser.
 * </li>
 * </ul>
 *
 * @author Victor Schappert
 * @since 20171127
 */
public final class ByteCodeParser {

    //
    // DATA
    //

    // -------------------------------------------------------------------------
    // Fields fixed at construction time.
    // -------------------------------------------------------------------------
    private final LookupSwitchVisitor lookupSwitchVisitor;
    private final NoOperandInstructionVisitor noOperandInstructionVisitor;
    private final OneOperandInstructionVisitor oneOperandInstructionVisitor;
    private final TableSwitchVisitor tableSwitchVisitor;
    private final TwoOperandInstructionVisitor twoOperandInstructionVisitor;

    // -------------------------------------------------------------------------
    // Fields modified during parse().
    // -------------------------------------------------------------------------
    private ByteBuffer bytecode;

    //
    // CONSTRUCTORS
    //

    ByteCodeParser(final LookupSwitchVisitor lookupSwitchVisitor,
            final NoOperandInstructionVisitor noOperandInstructionVisitor,
            final OneOperandInstructionVisitor oneOperandInstructionVisitor,
            final TableSwitchVisitor tableSwitchVisitor,
            final TwoOperandInstructionVisitor twoOperandInstructionVisitor) {
        this.lookupSwitchVisitor = lookupSwitchVisitor;
        this.noOperandInstructionVisitor = noOperandInstructionVisitor;
        this.oneOperandInstructionVisitor = oneOperandInstructionVisitor;
        this.tableSwitchVisitor = tableSwitchVisitor;
        this.twoOperandInstructionVisitor = twoOperandInstructionVisitor;
    }

    //
    // PUBLIC METHODS
    //

    public void parse(final byte[] bytecode){
        parse(ByteBuffer.wrap(bytecode));
    }

    public void parse(final ByteBuffer bytecode) {
        init(bytecode);
        instructions();
    }

    //
    // STATIC METHODS
    //

    /**
     * Returns a new parser builder that has no visitors added to it.
     *
     * @return Empty builder
     */
    public static ByteCodeParserBuilder builder() {
        return new ByteCodeParserBuilder();
    }

    //
    // INTERNALS
    //

    private void init(final ByteBuffer bytecode) {
        requireNonNull(bytecode, "bytecode cannot be null");
        if (BIG_ENDIAN != bytecode.order()) {
            throw new IllegalArgumentException("bytecode must have big-endian byte ordering");
        }
        this.bytecode = bytecode;
    }

    private void instructions() {
        while (bytecode.hasRemaining()) {
            final int position = bytecode.position();
            final int opcodeByte = bytecode.get() & 0xff;
            instruction(position, opcodeByte);
        }
    }

    private void instruction(final int position, final int opcodeByte) {
        switch (opcodeByte) {
            case OpcodeValue.AALOAD:
            case OpcodeValue.AASTORE:
            case OpcodeValue.ACONST_NULL:
            case OpcodeValue.ALOAD_0:
            case OpcodeValue.ALOAD_1:
            case OpcodeValue.ALOAD_2:
            case OpcodeValue.ALOAD_3:
            case OpcodeValue.ARETURN:
            case OpcodeValue.ARRAYLENGTH:
            case OpcodeValue.ASTORE_0:
            case OpcodeValue.ASTORE_1:
            case OpcodeValue.ASTORE_2:
            case OpcodeValue.ASTORE_3:
            case OpcodeValue.ATHROW:
            case OpcodeValue.BALOAD:
            case OpcodeValue.BASTORE:
            case OpcodeValue.BREAKPOINT:
            case OpcodeValue.CALOAD:
            case OpcodeValue.CASTORE:
            case OpcodeValue.D2F:
            case OpcodeValue.D2I:
            case OpcodeValue.D2L:
            case OpcodeValue.DADD:
            case OpcodeValue.DALOAD:
            case OpcodeValue.DASTORE:
            case OpcodeValue.DCMPG:
            case OpcodeValue.DCMPL:
            case OpcodeValue.DCONST_0:
            case OpcodeValue.DCONST_1:
            case OpcodeValue.DDIV:
            case OpcodeValue.DLOAD_0:
            case OpcodeValue.DLOAD_1:
            case OpcodeValue.DLOAD_2:
            case OpcodeValue.DLOAD_3:
            case OpcodeValue.DMUL:
            case OpcodeValue.DNEG:
            case OpcodeValue.DREM:
            case OpcodeValue.DRETURN:
            case OpcodeValue.DSTORE_0:
            case OpcodeValue.DSTORE_1:
            case OpcodeValue.DSTORE_2:
            case OpcodeValue.DSTORE_3:
            case OpcodeValue.DSUB:
            case OpcodeValue.DUP:
            case OpcodeValue.DUP_X1:
            case OpcodeValue.DUP_X2:
            case OpcodeValue.DUP2:
            case OpcodeValue.DUP2_X1:
            case OpcodeValue.DUP2_X2:
            case OpcodeValue.F2D:
            case OpcodeValue.F2I:
            case OpcodeValue.F2L:
            case OpcodeValue.FADD:
            case OpcodeValue.FALOAD:
            case OpcodeValue.FASTORE:
            case OpcodeValue.FCMPG:
            case OpcodeValue.FCMPL:
            case OpcodeValue.FCONST_0:
            case OpcodeValue.FCONST_1:
            case OpcodeValue.FCONST_2:
            case OpcodeValue.FDIV:
            case OpcodeValue.FLOAD_0:
            case OpcodeValue.FLOAD_1:
            case OpcodeValue.FLOAD_2:
            case OpcodeValue.FLOAD_3:
            case OpcodeValue.FMUL:
            case OpcodeValue.FNEG:
            case OpcodeValue.FREM:
            case OpcodeValue.FRETURN:
            case OpcodeValue.FSTORE_0:
            case OpcodeValue.FSTORE_1:
            case OpcodeValue.FSTORE_2:
            case OpcodeValue.FSTORE_3:
            case OpcodeValue.FSUB:
            case OpcodeValue.I2B:
            case OpcodeValue.I2C:
            case OpcodeValue.I2D:
            case OpcodeValue.I2F:
            case OpcodeValue.I2L:
            case OpcodeValue.I2S:
            case OpcodeValue.IADD:
            case OpcodeValue.IALOAD:
            case OpcodeValue.IAND:
            case OpcodeValue.IASTORE:
            case OpcodeValue.ICONST_M1:
            case OpcodeValue.ICONST_0:
            case OpcodeValue.ICONST_1:
            case OpcodeValue.ICONST_2:
            case OpcodeValue.ICONST_3:
            case OpcodeValue.ICONST_4:
            case OpcodeValue.ICONST_5:
            case OpcodeValue.IDIV:
            case OpcodeValue.ILOAD_0:
            case OpcodeValue.ILOAD_1:
            case OpcodeValue.ILOAD_2:
            case OpcodeValue.ILOAD_3:
            case OpcodeValue.IMPDEP1:
            case OpcodeValue.IMPDEP2:
            case OpcodeValue.IMUL:
            case OpcodeValue.INEG:
            case OpcodeValue.IOR:
            case OpcodeValue.IREM:
            case OpcodeValue.IRETURN:
            case OpcodeValue.ISHL:
            case OpcodeValue.ISHR:
            case OpcodeValue.ISTORE_0:
            case OpcodeValue.ISTORE_1:
            case OpcodeValue.ISTORE_2:
            case OpcodeValue.ISTORE_3:
            case OpcodeValue.ISUB:
            case OpcodeValue.IUSHR:
            case OpcodeValue.IXOR:
            case OpcodeValue.L2D:
            case OpcodeValue.L2F:
            case OpcodeValue.L2I:
            case OpcodeValue.LADD:
            case OpcodeValue.LALOAD:
            case OpcodeValue.LAND:
            case OpcodeValue.LASTORE:
            case OpcodeValue.LCMP:
            case OpcodeValue.LCONST_0:
            case OpcodeValue.LCONST_1:
            case OpcodeValue.LDIV:
            case OpcodeValue.LLOAD_0:
            case OpcodeValue.LLOAD_1:
            case OpcodeValue.LLOAD_2:
            case OpcodeValue.LLOAD_3:
            case OpcodeValue.LMUL:
            case OpcodeValue.LNEG:
            case OpcodeValue.LOR:
            case OpcodeValue.LREM:
            case OpcodeValue.LRETURN:
            case OpcodeValue.LSHL:
            case OpcodeValue.LSHR:
            case OpcodeValue.LSTORE_0:
            case OpcodeValue.LSTORE_1:
            case OpcodeValue.LSTORE_2:
            case OpcodeValue.LSTORE_3:
            case OpcodeValue.LSUB:
            case OpcodeValue.LUSHR:
            case OpcodeValue.LXOR:
            case OpcodeValue.MONITORENTER:
            case OpcodeValue.MONITOREXIT:
            case OpcodeValue.NOP:
            case OpcodeValue.POP:
            case OpcodeValue.POP2:
            case OpcodeValue.RETURN:
            case OpcodeValue.SALOAD:
            case OpcodeValue.SASTORE:
            case OpcodeValue.SWAP:
                noOperand(position, opcodeByte);
                break;
            case OpcodeValue.ALOAD:
            case OpcodeValue.ANEWARRAY:
            case OpcodeValue.ASTORE:
            case OpcodeValue.BIPUSH:
            case OpcodeValue.CHECKCAST:
            case OpcodeValue.DLOAD:
            case OpcodeValue.DSTORE:
            case OpcodeValue.FLOAD:
            case OpcodeValue.FSTORE:
            case OpcodeValue.GETFIELD:
            case OpcodeValue.GETSTATIC:
            case OpcodeValue.GOTO:
            case OpcodeValue.GOTO_W:
            case OpcodeValue.IF_ACMPEQ:
            case OpcodeValue.IF_ACMPNE:
            case OpcodeValue.IF_ICMPEQ:
            case OpcodeValue.IF_ICMPGE:
            case OpcodeValue.IF_ICMPGT:
            case OpcodeValue.IF_ICMPLE:
            case OpcodeValue.IF_ICMPLT:
            case OpcodeValue.IF_ICMPNE:
            case OpcodeValue.IFEQ:
            case OpcodeValue.IFGE:
            case OpcodeValue.IFGT:
            case OpcodeValue.IFLE:
            case OpcodeValue.IFLT:
            case OpcodeValue.IFNE:
            case OpcodeValue.IFNONNULL:
            case OpcodeValue.IFNULL:
            case OpcodeValue.ILOAD:
            case OpcodeValue.INSTANCEOF:
            case OpcodeValue.INVOKESPECIAL:
            case OpcodeValue.INVOKESTATIC:
            case OpcodeValue.INVOKEVIRTUAL:
            case OpcodeValue.ISTORE:
            case OpcodeValue.JSR:
            case OpcodeValue.JSR_W:
            case OpcodeValue.LDC:
            case OpcodeValue.LDC_W:
            case OpcodeValue.LDC2_W:
            case OpcodeValue.LLOAD:
            case OpcodeValue.LSTORE:
            case OpcodeValue.NEW:
            case OpcodeValue.NEWARRAY:
            case OpcodeValue.PUTFIELD:
            case OpcodeValue.PUTSTATIC:
            case OpcodeValue.RET:
            case OpcodeValue.SIPUSH:
                oneOperand(position, opcodeByte);
                break;
            case OpcodeValue.INVOKEDYNAMIC:
                invokedynamic(position);
                break;
            case OpcodeValue.IINC:
                iinc(position);
                break;
            case OpcodeValue.MULTIANEWARRAY:
                multianewarray(position);
                break;
            case OpcodeValue.INVOKEINTERFACE:
                invokeinterface(position);
                break;
            case OpcodeValue.LOOKUPSWITCH:
                lookupswitch(position);
                break;
            case OpcodeValue.TABLESWITCH:
                tableswitch(position);
                break;
            case OpcodeValue.WIDE:
                wide(position);
                break;
            default:
                badOpcode(position, opcodeByte);
                break;
        }
    }

    private void noOperand(final int position, final int opcodeByte) {
        final NoOperandOpcode opcode = NoOperandOpcode.forUnsignedByte(opcodeByte);
        noOperandInstructionVisitor.visit(position, opcode);
    }

    private void oneOperand(final int position, final int opcodeByte) {
        final OneOperandOpcode opcode = OneOperandOpcode.forUnsignedByte(opcodeByte);
        final OperandType operandType = opcode.getOperandType();
        final int operand;
        try {
            switch (operandType) {
                case SIGNED_VALUE_BYTE:
                    operand = bytecode.get();
                    break;
                case ATYPE_BYTE:
                case UNSIGNED_VALUE_BYTE:
                case LOCAL_VARIABLE_INDEX_BYTE:
                case CONSTANT_POOL_INDEX_BYTE:
                    operand = bytecode.get() & 0xff;
                    break;
                case SIGNED_VALUE_SHORT:
                case BRANCH_OFFSET_SHORT:
                case LOCAL_VARIABLE_INDEX_SHORT:
                    operand = bytecode.getShort();
                    break;
                case CONSTANT_POOL_INDEX_SHORT:
                    operand = bytecode.getShort() & 0xffff;
                    break;
                case SIGNED_VALUE_INT:
                case BRANCH_OFFSET_INT:
                    operand = bytecode.getInt();
                    break;
                default:
                    throw unexpectedOpcode(opcode);
            }
        } catch (final BufferUnderflowException e) {
            throw missingOperandData(position, opcode, 0, e);
        }
        oneOperandInstructionVisitor.visit(position, opcode, operand);
    }

    private void badOpcode(final int position, final int opcodeByte) {
        throw bytecodeFormatException(position, "Invalid opcode: 0x%02x", opcodeByte);
    }

    private void invokedynamic(final int position) {
        final int index;
        try {
            index = bytecode.getShort() & 0xffff;
        } catch (final BufferUnderflowException e) {
            throw missingOperandData(position, INVOKEDYNAMIC, 0, e);
        }
        zero(position, INVOKEDYNAMIC, 1);
        zero(position, INVOKEDYNAMIC, 2);
        oneOperandInstructionVisitor.visit(position, INVOKEDYNAMIC, index);
    }

    private void iinc(final int position) {
        int operandIndex = 0;
        final int operand1, operand2;
        try {
            operand1 = bytecode.get() & 0xff;
            operandIndex = 1;
            operand2 = bytecode.get();
        } catch (final BufferUnderflowException e) {
            throw missingOperandData(position, IINC, operandIndex, e);
        }
        twoOperandInstructionVisitor.visit(position, IINC, operand1, operand2);
    }

    private void multianewarray(final int position) {
        int operandIndex = 0;
        final int operand1, operand2;
        try {
            operand1 = bytecode.getShort() & 0xffff;
            operandIndex = 1;
            operand2 = bytecode.get() & 0xff;
        } catch (final BufferUnderflowException e) {
            throw missingOperandData(position, MULTIANEWARRAY, operandIndex, e);
        }
        twoOperandInstructionVisitor.visit(position, MULTIANEWARRAY, operand1, operand2);
    }

    private void invokeinterface(final int position) {
        final int index, count;
        int operandIndex = 0;
        try {
            index = bytecode.getShort() & 0xffff;
            operandIndex = 1;
            count = bytecode.get() & 0xff;
        } catch (final BufferUnderflowException e) {
            throw missingOperandData(position, INVOKEINTERFACE, operandIndex, e);
        }
        zero(position, INVOKEINTERFACE, 1);
        twoOperandInstructionVisitor.visit(position, INVOKEINTERFACE, index, count);
    }

    private void lookupswitch(final int position) {
        padding(position, LOOKUPSWITCH);
        final int defaultOffset, numPairs;
        int operandIndex = 0;
        try {
            defaultOffset = bytecode.getInt();
            operandIndex = 1;
            numPairs = bytecode.getInt();
        } catch (final BufferUnderflowException e) {
            throw missingOperandData(position, LOOKUPSWITCH, operandIndex, e);
        }
        if (0 <= numPairs) {
            final ByteBuffer matchOffsetPairs = tableOperand(position, LOOKUPSWITCH, numPairs, 8L);
            lookupSwitchVisitor.visit(position, defaultOffset, numPairs, matchOffsetPairs);
        } else {
            throw instructionFormatException(position, LOOKUPSWITCH, "npairs must be non-negative, but is %d", numPairs);
        }
    }

    private void tableswitch(final int position) {
        padding(position, TABLESWITCH);
        final int defaultOffset = bytecode.getInt();
        final long lowIndex, highIndex;
        int operandIndex = 0;
        try {
            lowIndex = bytecode.getInt();
            operandIndex = 1;
            highIndex = bytecode.getInt();
        } catch (final BufferUnderflowException e) {
            throw missingOperandData(position, TABLESWITCH, operandIndex, e);
        }
        final long numJumps = highIndex - lowIndex + 1L;
        if (0L < numJumps) {
            final ByteBuffer jumpOffsets = tableOperand(position, TABLESWITCH, numJumps, 4L);
            tableSwitchVisitor.visit(position, defaultOffset, (int)lowIndex, (int)highIndex, jumpOffsets);
        } else {
            assert highIndex < lowIndex;
            throw instructionFormatException(position, TABLESWITCH, "lowIndex must be less than or equal to highIndex, but %d > %d", lowIndex, highIndex);
        }
    }

    private void wide(final int position) {
        final int opcodeByte;
        try {
            opcodeByte = bytecode.get() & 0xff;
        } catch (final BufferUnderflowException e) {
            throw missingOperandData(position, WIDE, 0, e);
        }
        switch (opcodeByte) {
        case OpcodeValue.ILOAD:
        case OpcodeValue.FLOAD:
        case OpcodeValue.ALOAD:
        case OpcodeValue.LLOAD:
        case OpcodeValue.DLOAD:
        case OpcodeValue.ISTORE:
        case OpcodeValue.ASTORE:
        case OpcodeValue.LSTORE:
        case OpcodeValue.DSTORE:
        case OpcodeValue.RET:
            localVariableInstructionWide(position, OneOperandOpcode.forUnsignedByte(opcodeByte));
            break;
        case OpcodeValue.IINC:
            iincWide(position);
            break;
        default:
            badOpcode(position, opcodeByte);
            break;
        }
    }

    private void localVariableInstructionWide(final int position, final OneOperandOpcode opcode) {
        final int index;
        try {
            index = bytecode.getShort() & 0xffff;
        } catch (final BufferUnderflowException e) {
            throw missingWideOperandData(position, opcode, 0, e);
        }
        oneOperandInstructionVisitor.visit(position, opcode, index);
    }

    private void iincWide(final int position) {
        final int index, constant;
        int operandIndex = 0;
        try {
            index = bytecode.getShort() & 0xffff;
            operandIndex = 1;
            constant = bytecode.getShort();
        } catch (final BufferUnderflowException e) {
            throw missingWideOperandData(position, IINC, operandIndex, e);
        }
        twoOperandInstructionVisitor.visit(position, IINC, index, constant);
    }

    private void zero(final int position, final Opcode opcode, final int operandIndex) {
        final int value;
        try {
            value = bytecode.get();
        } catch (final BufferUnderflowException e) {
            throw missingOperandData(position, opcode, operandIndex, e);
        }
        if (0 != value) {
            throw instructionFormatException(position, opcode, "operand %d (%s) must be a zero byte",
                    operandIndex, opcode.getOperandTypes().get(operandIndex));
        }
    }

    private void padding(final int position, final Opcode opcode) {
        // `position` is the location of the `opcode` byte, not the position of
        // the subsequent byte. So, taking `n` to be the high-order 30 bits of
        // `position`, we have:
        //  +-----------------+------------------+--------------------+
        //  | opcode position | operand position | 4 - (position & 3) |
        //  +-----------------+------------------+--------------------+
        //  |              n0 |           (n+4)0 |                 4  |
        //  |              n1 |           (n+3)0 |                 3  |
        //  |              n2 |           (n+2)0 |                 2  |
        //  |              n3 |           (n+1)0 |                 1* |
        //  +-----------------+------------------+--------------------+
        // * The last case is a no-op because advancing 1 from `position` just
        //   means moving to the byte following the `opcode` byte, which we
        //   would do in any event.
        final int offset = 4 - (position & 3);
        try {
            bytecode.position(position + offset);
        } catch (final IllegalArgumentException e) {
            throw instructionFormatException(position, opcode, "not enough bytes remain for padding (%d required but %d remain)",
                    offset - 1, bytecode.remaining());
        }
    }

    private ByteBuffer tableOperand(final int position, final Opcode opcode, final long numEntries, final long entrySize) {
        final long tableSize = numEntries * entrySize;
        final long remaining = bytecode.remaining();
        if (tableSize <= remaining) {
            final ByteBuffer tableBuffer = bytecode.slice().limit((int)tableSize);
            bytecode.position(bytecode.position() + (int)tableSize);
            return tableBuffer;
        } else {
            throw instructionFormatException(position, opcode, "only %d bytes are left for the table (from position %d) but %d are required",
                    remaining, bytecode.position(), tableSize);
        }
    }

    private static ByteCodeFormatException bytecodeFormatException(final int position, final String format, final Object... args) {
        final Object[] formatArgs;
        final Throwable cause;
        if (0 < args.length && args[args.length - 1] instanceof Throwable) {
            formatArgs = copyOfRange(args, 0, args.length - 1);
            cause = (Throwable)args[args.length - 1];
        } else {
            formatArgs = args;
            cause = null;
        }
        return new ByteCodeFormatException(format(format, formatArgs), cause, position);
    }

    private static ByteCodeFormatException instructionFormatException(final int position, final Object opcode, final String format, final Object...args) {
        return bytecodeFormatException(position, "In %s instruction, " + format,
                Stream.concat(Stream.of(opcode), stream(args)).toArray());
    }

    private static final String MISSING_OPERAND_DATA_FORMAT = "not enough bytes remain for operand %d (%s)";

    private static ByteCodeFormatException missingOperandData(final int position, final Opcode opcode, final int operandIndex, final BufferUnderflowException cause) {
        return instructionFormatException(position, opcode, MISSING_OPERAND_DATA_FORMAT,
                operandIndex, opcode.getOperandTypes().get(operandIndex), cause);
    }

    private static ByteCodeFormatException missingWideOperandData(final int position, final Opcode opcode, final int operandIndex, final BufferUnderflowException cause) {
        return instructionFormatException(position, format("wide %s", opcode), MISSING_OPERAND_DATA_FORMAT,
                operandIndex, "widened " + opcode.getOperandTypes().get(operandIndex), cause);
    }

    private static InternalError unexpectedOpcode(final Opcode opcode) {
        return new InternalError(format("Unexpected opcode %s in switch", opcode));
    }
}
