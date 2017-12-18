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

package io.tarro.parser.bytecode;

import io.tarro.base.bytecode.ATypeValue;
import io.tarro.base.bytecode.Opcode;
import io.tarro.base.bytecode.OpcodeValue;
import io.tarro.base.bytecode.OperandType;
import io.tarro.base.visitor.Visitor;
import io.tarro.parser.bytecode.visitor.LookupSwitchVisitor;
import io.tarro.parser.bytecode.visitor.NoOperandInstructionVisitor;
import io.tarro.parser.bytecode.visitor.OneOperandInstructionVisitor;
import io.tarro.parser.bytecode.visitor.TableSwitchVisitor;
import io.tarro.parser.bytecode.visitor.TwoOperandInstructionVisitor;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.stream.Stream;

import static io.tarro.base.bytecode.Opcode.IINC;
import static io.tarro.base.bytecode.Opcode.INVOKEDYNAMIC;
import static io.tarro.base.bytecode.Opcode.INVOKEINTERFACE;
import static io.tarro.base.bytecode.Opcode.LOOKUPSWITCH;
import static io.tarro.base.bytecode.Opcode.TABLESWITCH;
import static io.tarro.base.bytecode.Opcode.WIDE;
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

    private final Visitor[] allVisitors;


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
        this.allVisitors = toArray(lookupSwitchVisitor, noOperandInstructionVisitor,
                oneOperandInstructionVisitor, tableSwitchVisitor, twoOperandInstructionVisitor);
    }

    //
    // PUBLIC METHODS
    //

    public void parse(final byte[] bytecode){
        parse(ByteBuffer.wrap(bytecode));
    }

    public void parse(final ByteBuffer bytecode) {
        init(bytecode);
        before();
        after();
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

    private void before() {
        for (final Visitor visitor : allVisitors) {
            visitor.before();
        }
    }

    private void after() {
        for (final Visitor visitor : allVisitors) {
            visitor.after();
        }
    }

    private void instructions() {
        while (bytecode.hasRemaining()) {
            final int position = bytecode.position();
            final int opcodeByte = bytecode.get() & 0xff;
            final Opcode opcode = Opcode.forUnsignedByte(opcodeByte);
            if (null != opcode) {
                instruction(position, opcode);
            } else {
                badOpcode(position, opcodeByte);
            }
        }
    }

    private void instruction(final int position, final Opcode opcode) {
        switch (opcode.getNumOperands()) {
        case 0: noOperand(position, opcode); break;
        case 1: oneOperand(position, opcode); break;
        case 2: twoOperands(position, opcode); break;
        default: complexInstruction(position, opcode); break;
        }
    }

    private void badOpcode(final int position, final int opcodeByte) {
        throw bytecodeFormatException(position, "Invalid opcode: 0x%02h", opcodeByte);
    }

    private void noOperand(final int position, final Opcode opcode) {
        noOperandInstructionVisitor.visit(position, opcode);
    }

    private void oneOperand(final int position, final Opcode opcode) {
        final OperandType operandType = opcode.getOperandTypes().get(0);
        final int operand;
        try {
            switch (operandType) {
            case SIGNED_VALUE_BYTE:
                operand = bytecode.get();
                break;
            case ATYPE_BYTE:
            case UNSIGNED_VALUE_BYTE:
            case LOCAL_VARIABLE_INDEX_BYTE:
            case CONSTANT_POOL_INDEX_CONSTANT_BYTE:
                operand = bytecode.get() & 0xff;
                break;
            case SIGNED_VALUE_SHORT:
            case BRANCH_OFFSET_SHORT:
            case LOCAL_VARIABLE_INDEX_SHORT:
                operand = bytecode.getShort();
                break;
            case CONSTANT_POOL_INDEX_CONSTANT_SHORT:
            case CONSTANT_POOL_INDEX_CONSTANT2_SHORT:
            case CONSTANT_POOL_INDEX_CLASS_SHORT:
            case CONSTANT_POOL_INDEX_FIELD_REF_SHORT:
            case CONSTANT_POOL_INDEX_CLASS_METHOD_REF_SHORT:
            case CONSTANT_POOL_INDEX_INTERFACE_METHOD_REF_SHORT:
            case CONSTANT_POOL_INDEX_EITHER_METHOD_REF_SHORT:
            case CONSTANT_POOL_INDEX_INVOKE_DYNAMIC_SHORT:
                operand = bytecode.getShort() & 0xffff;
                break;
            case SIGNED_VALUE_INT:
            case BRANCH_OFFSET_INT:
                operand = bytecode.getInt();
                break;
            default:
                throw internalError("Opcode %s operand type %s not expected here",
                        opcode, operandType);
            }
        } catch (final BufferUnderflowException e) {
            throw missingOperandData(position, opcode, 0, e);
        }
        oneOperandInstructionVisitor.visit(position, opcode, operand);
    }

    private void twoOperands(final int position, final Opcode opcode) {
        final int operand1, operand2;
        int operandIndex = 0;
        try {
            switch (opcode) {
            case IINC:
                operand1 = bytecode.get() & 0xff;
                operandIndex = 1;
                operand2 = bytecode.get();
                break;
            case MULTIANEWARRAY:
                operand1 = bytecode.getShort() & 0xffff;
                operandIndex = 1;
                operand2 = bytecode.get() & 0xff;
                break;
            default:
                throw unexpectedOpcode(opcode);
            }
        } catch (final BufferUnderflowException e) {
            throw missingOperandData(position, opcode, operandIndex, e);
        }
        twoOperandInstructionVisitor.visit(position, opcode, operand1, operand2);
    }

    private void complexInstruction(final int position, final Opcode opcode) {
        switch (opcode) {
        case INVOKEDYNAMIC:
            invokedynamic(position);
            break;
        case INVOKEINTERFACE:
            invokeinterface(position);
            break;
        case LOOKUPSWITCH:
            lookupswitch(position);
            break;
        case TABLESWITCH:
            tableswitch(position);
            break;
        case WIDE:
            wide(position);
            break;
        default:
            throw unexpectedOpcode(opcode);
        }
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
            opcodeByte = bytecode.get() & 0xffffff00;
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
            localVariableInstructionWide(position, Opcode.forUnsignedByte(opcodeByte));
            break;
        case OpcodeValue.IINC:
            iincWide(position);
            break;
        default:
            badOpcode(position, opcodeByte);
            break;
        }
    }

    private void localVariableInstructionWide(final int position, final Opcode opcode) {
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
        if (0 == value) {
            return;
        } else {
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
        //  |              n0 |           (n+4)0 |                  4 |
        //  |              n1 |           (n+3)0 |                  3 |
        //  |              n2 |           (n+2)0 |                  2 |
        //  |              n3 |           (n+1)0 |                  1 |
        //  +-----------------+------------------+--------------------+
        final int padding = 4 - (position & 3);
        try {
            bytecode.position(position + padding);
        } catch (final IllegalArgumentException e) {
            throw instructionFormatException(position, opcode, "not enough bytes remain for padding (%d required but %d remain)",
                    padding, bytecode.remaining());
        }
    }

    private ByteBuffer tableOperand(final int position, final Opcode opcode, final long numEntries, final long entrySize) {
        final long tableSize = numEntries * entrySize;
        final long remaining = bytecode.remaining();
        if (tableSize <= remaining) {
            return bytecode.slice().asReadOnlyBuffer();
        } else {
            throw instructionFormatException(position, opcode, "only %d bytes are left for the table (from position %d) but %d are required",
                    remaining, bytecode.position(), tableSize);
        }
    }

    private static Visitor[] toArray(final Visitor... visitors) {
        return visitors;
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
        return internalError("Unexpected opcode %s in switch", opcode);
    }

    private static InternalError internalError(final String format, final Object... args) {
        return new InternalError(format(format, args));
    }
}
