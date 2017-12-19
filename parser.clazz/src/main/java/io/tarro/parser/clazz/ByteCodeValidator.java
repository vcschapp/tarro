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

package io.tarro.parser.clazz;

import io.tarro.base.InvalidConstantPoolIndexException;
import io.tarro.base.bytecode.ATypeValue;
import io.tarro.base.bytecode.OneOperandOpcode;
import io.tarro.base.bytecode.Opcode;
import io.tarro.base.bytecode.OperandType;
import io.tarro.base.bytecode.TwoOperandOpcode;
import io.tarro.parser.bytecode.ByteCodeFormatException;
import io.tarro.parser.bytecode.ByteCodeParser;

import java.nio.ByteBuffer;
import java.util.stream.Stream;

import static io.tarro.base.InvalidConstantPoolIndexException.lessThanOne;
import static io.tarro.base.InvalidConstantPoolIndexException.notLessThanCount;
import static io.tarro.base.bytecode.ATypeValue.isValidATypeValue;
import static io.tarro.base.bytecode.OneOperandOpcode.NEWARRAY;
import static io.tarro.base.bytecode.VariableOperandOpcode.LOOKUPSWITCH;
import static java.lang.String.format;
import static java.util.Arrays.fill;
import static java.util.Arrays.stream;
import static java.util.stream.IntStream.range;

/**
 * <p>
 * Minimally validates bytecode appearing in a classfile to ensure it meets the
 * {@link ClassParser}'s minimal standards for bytecode appearing within the
 * {@code Code} attribute of a class {@code method_info} structure.
 * </p>
 *
 * @author Victor Schappert
 * @since 20171130
 */
final class ByteCodeValidator {

    //
    // DATA
    //

    // -------------------------------------------------------------------------
    // Fields fixed at construction time.
    // -------------------------------------------------------------------------
    private final ByteCodeParser byteCodeParser;

    // -------------------------------------------------------------------------
    // Fields modified during validate().
    // -------------------------------------------------------------------------
    private byte[] bytecode;
    private boolean[] jumpTarget;
    private long[] jumpPair;
    private int jumpPairCount;
    private int constantPoolCount;
    private int maxLocals;

    //
    // CONSTRUCTORS
    //

    ByteCodeValidator() {
        byteCodeParser = ByteCodeParser.builder()
                .withNoOperandInstructionVisitor(this::validateNoOperandInstruction)
                .withOneOperandInstructionVisitor(this::validateOneOperandInstruction)
                .withTwoOperandInstructionVisitor(this::validateTwoOperandInstruction)
                .withLookupSwitchVisitor(this::validateLookupSwitchInstruction)
                .withTableSwitchVisitor(this::validateTableSwitchInstruction)
                .build();
    }

    //
    // PUBLIC METHODS
    //

    void validate(final byte[] bytecode, final int constantPoolCount, final int maxLocals) {
        init(bytecode, constantPoolCount, maxLocals);
        byteCodeParser.parse(bytecode);
        validateJumps();
    }

    //
    // INTERNALS
    //

    private void init(final byte[] bytecode, final int constantPoolCount, final int maxLocals) {
        this.bytecode = bytecode;
        initJumpTarget(bytecode.length);
        initJumpPair(bytecode.length);
        this.constantPoolCount = constantPoolCount;
        this.maxLocals = maxLocals;
    }

    private void initJumpTarget(final int bytecodeLength) {
        if (null != jumpTarget && bytecodeLength <= jumpTarget.length) {
            fill(jumpTarget, 0, bytecodeLength, false);
        } else {
            jumpTarget = new boolean[bytecodeLength];
        }
    }

    private void initJumpPair(final int bytecodeLength) {
        this.jumpPairCount = 0;
        final int maxJumps = maxJumps(bytecodeLength);
        if (null != jumpPair && maxJumps <= jumpPair.length) {
            fill(jumpPair, 0, maxJumps, 0L);
        } else {
            jumpPair = new long[maxJumps];
        }
    }

    private void validateNoOperandInstruction(final int position, final Opcode opcode) {
        if (!opcode.isReserved()) {
            recordJumpTarget(position);
        } else {
            throw bytecodeFormatException(position, "Opcode %s is not permitted in any class file", opcode);
        }
    }

    private void validateOneOperandInstruction(final int position, final OneOperandOpcode opcode,
            int operand) {
        final OperandType operandType = opcode.getOperandType();
        switch (operandType) {
        case BRANCH_OFFSET_SHORT:
        case BRANCH_OFFSET_INT:
            recordJumpSource(position, operand);
            break;
        case LOCAL_VARIABLE_INDEX_BYTE:
        case LOCAL_VARIABLE_INDEX_SHORT:
            validateLocalVariableIndex(position, opcode, operand);
            break;
        case CONSTANT_POOL_INDEX_CONSTANT_SHORT:
        case CONSTANT_POOL_INDEX_CONSTANT2_SHORT:
        case CONSTANT_POOL_INDEX_CLASS_SHORT:
        case CONSTANT_POOL_INDEX_FIELD_REF_SHORT:
        case CONSTANT_POOL_INDEX_CLASS_METHOD_REF_SHORT:
        case CONSTANT_POOL_INDEX_INTERFACE_METHOD_REF_SHORT:
        case CONSTANT_POOL_INDEX_EITHER_METHOD_REF_SHORT:
        case CONSTANT_POOL_INDEX_INVOKEDYNAMIC_SHORT:
            validateConstantPoolIndex(position, opcode, operand);
            break;
        case ATYPE_BYTE:
            validateAType(position, operand);
        default:
            // FIXME: Handle all cases explicitly and "default" should throw...
            break;
        }
        recordJumpTarget(position);
    }

    private void validateTwoOperandInstruction(final int position, final TwoOperandOpcode opcode, final int operand1,
                                               final int operand2) {
        switch (opcode) {
        case IINC:
            validateLocalVariableIndex(position, opcode, operand1);
            break;
        // FIXME: Handle InvokeInterface
        case MULTIANEWARRAY:
            validateConstantPoolIndex(position, opcode, operand1);
            break;
        default:
            break;
        }
        recordJumpTarget(position);
    }

    private void validateLookupSwitchInstruction(final int position, final int defaultOffset, final int numPairs,
            final ByteBuffer matchOffsetPairs) {
        recordJumpTarget(position);
        recordJumpSource(position, defaultOffset);
        long previousMatch = Integer.MIN_VALUE - 1L;
        for (int i = 0; i < numPairs; ++i) {
            final long match = matchOffsetPairs.getInt();
            if (previousMatch < match) {
                final long offset = matchOffsetPairs.getInt();
                recordJumpSource(position, offset);
                previousMatch = match;
            } else {
                throw instructionFormatException(position, LOOKUPSWITCH,
                        "out-of-order match-offset pair (match[%d] value %d <= match[%d] value %d",
                        i, match, i - 1, previousMatch);

            }
        }
    }

    private void validateTableSwitchInstruction(final int position, final int defaultOffset,
                                                final int lowIndex, final int highIndex,
                                                final ByteBuffer jumpOffsets) {
        recordJumpTarget(position);
        recordJumpSource(position, defaultOffset);
        int i = lowIndex;
        do {
            final long offset = jumpOffsets.getInt();
            recordJumpSource(position, offset);
        } while (++i <= highIndex);
    }

    private void validateLocalVariableIndex(final int position, final Opcode opcode, final int localVariableIndex) {
        if (maxLocals <= localVariableIndex) {
            throw instructionFormatException(position, opcode, "out-of-range local variable index %d (max_locals is %d)",
                    localVariableIndex, maxLocals);
        } else {
            assert 0 <= localVariableIndex;
        }
    }

    private void validateConstantPoolIndex(final int position, final Opcode opcode, final int constantPoolIndex) {
        if (constantPoolCount <= constantPoolIndex) {
            throw invalidConstantPoolIndex(position, opcode, notLessThanCount(constantPoolIndex, constantPoolCount));
        } else if (0 == constantPoolIndex) {
            throw invalidConstantPoolIndex(position, opcode,  lessThanOne(constantPoolIndex));
        } else {
            assert 1 <= constantPoolIndex : "constant pool index operand should never be negative";
        }
    }

    private void validateAType(final int position, final int atype) {
        if (! isValidATypeValue(atype)) {
            throw instructionFormatException(position, NEWARRAY, "bad atype operand (%d is not in range %d..%d)",
                    atype, ATypeValue.MIN, ATypeValue.MAX);
        }
    }

    private void validateJumps() {
        for (int i = 0; i < jumpPairCount; ++i) {
            final long jumpPair = this.jumpPair[i];
            final int sourcePosition = (int)(jumpPair >> 32);
            final int offset = (int)(jumpPair & 0xffffffffL);
            final int targetPosition = sourcePosition + offset;
            if (0L <= targetPosition && targetPosition < bytecode.length) {
                validateJumpTarget((int)sourcePosition, (int)targetPosition, offset);
            } else {
                throw badJumpOffset((int)sourcePosition, "offset %d is outside the bytecode block (valid offsets range here is %d..%d)",
                        offset, -sourcePosition, lastJumpTargetPosition() - sourcePosition);
            }
        }
    }

    private void validateJumpTarget(final int sourcePosition, final int targetPosition, final long offset) {
        if (!jumpTarget[targetPosition]) {
            throw badJumpOffset(sourcePosition, "offset %d (target position %d) does not refer to the start of an instruction",
                    offset);
        }
    }

    private int lastJumpTargetPosition() {
        return range(0, bytecode.length)
                .map(i -> bytecode.length - i - 1) /* reverse it */
                .filter(i -> jumpTarget[i])
                .findFirst()
                .orElse(0);
    }

    private void recordJumpTarget(final int targetInstructionPosition) {
        jumpTarget[targetInstructionPosition] = true;
    }

    private void recordJumpSource(final long sourceInstructionPosition, final long offset) {
        jumpPair[jumpPairCount++] = jumpPair(sourceInstructionPosition, offset);
    }

    private static long jumpPair(final long sourceInstructionPosition, final long offset) {
        return sourceInstructionPosition << 32 | (offset & 0xffffffffL);
    }

    private static int maxJumps(final int bytecodeLength) {
        // Returns a rough estimate of the maximum number of jump offsets that
        // have to be validated in the code in the worst case. In general, the
        // minimum size of a branching instructions is 3 bytes (one for the
        // opcode, two for the branch offset) and so, for example, the program
        //     0: GOTO 0
        // is three bytes long. However, for a large enough tableswitch
        // instruction, the average number of bytes per jump, as
        // (high - low + 1) approaches infinity, approaches two.
        return bytecodeLength / 2;
    }

    private static ByteCodeFormatException bytecodeFormatException(final int position, final String format, final Object... args) {
        return new ByteCodeFormatException(format(format, args), null, position);
    }

    private static ByteCodeFormatException instructionFormatException(final int position, final Opcode opcode, final String format, final Object... args) {
        return new ByteCodeFormatException(instructionFormatExceptionMessage(opcode, format, args), null, position);
    }

    private static ByteCodeFormatException invalidConstantPoolIndex(final int position, final Opcode opcode, final InvalidConstantPoolIndexException cause) {
        return new ByteCodeFormatException(
                instructionFormatExceptionMessage(opcode, "constant pool index out of bounds: %d", cause.getConstantPoolIndex()),
                cause, position);
    }

    private static String instructionFormatExceptionMessage(final Opcode opcode, final String format, final Object... args) {
        return format("In %s instruction, " + format, Stream.concat(Stream.of(opcode), stream(args)).toArray());
    }

    private ByteCodeFormatException badJumpOffset(final int position, final String format, final Object... args) {
        final int opcodeByte = bytecode[position] & 0xff;
        return instructionFormatException(position, Opcode.forUnsignedByte(opcodeByte), format, args);
    }
}
