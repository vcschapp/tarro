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

import static io.tarro.base.bytecode.OpcodeTable.noOpcodeForValue;
import static io.tarro.base.bytecode.OpcodeTable.notAnUnsignedByte;
import static io.tarro.base.bytecode.OperandType.BRANCH_OFFSET_INT;
import static io.tarro.base.bytecode.OperandType.JUMP_OFFSET_TABLE;
import static io.tarro.base.bytecode.OperandType.LOCAL_VARIABLE_INDEX_SHORT;
import static io.tarro.base.bytecode.OperandType.MATCH_OFFSET_PAIRS_TABLE;
import static io.tarro.base.bytecode.OperandType.OPCODE;
import static io.tarro.base.bytecode.OperandType.OPTIONAL_COUNT;
import static io.tarro.base.bytecode.OperandType.SIGNED_VALUE_INT;

/**
 * Enumerates opcodes for Java Virtual Machine instructions which do not fit
 * the no operand/one operand/two operands paradigm.
 *
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
    LOOKUPSWITCH(OpcodeValue.LOOKUPSWITCH, BRANCH_OFFSET_INT,
            MATCH_OFFSET_PAIRS_TABLE),
    TABLESWITCH(OpcodeValue.TABLESWITCH, BRANCH_OFFSET_INT,
            SIGNED_VALUE_INT, SIGNED_VALUE_INT, JUMP_OFFSET_TABLE),
    WIDE(OpcodeValue.WIDE, OPCODE, LOCAL_VARIABLE_INDEX_SHORT, OPTIONAL_COUNT);

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
