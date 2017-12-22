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
import static io.tarro.base.bytecode.OperandType.CONSTANT_POOL_INDEX_SHORT;
import static io.tarro.base.bytecode.OperandType.LOCAL_VARIABLE_INDEX_BYTE;
import static io.tarro.base.bytecode.OperandType.SIGNED_VALUE_BYTE;
import static io.tarro.base.bytecode.OperandType.UNSIGNED_VALUE_BYTE;

/**
 * Enumerates opcodes for Java Virtual Machine instructions which require two
 * operands.
 *
 * @author Victor Schappert
 * @since 20171218
 * @see NoOperandOpcode
 * @see OneOperandOpcode
 * @see VariableOperandOpcode
 * @see OpcodeValue
 */
public enum TwoOperandOpcode implements Opcode {

    //
    // ENUMERATORS
    //

    // Keep this list in alphabetical order by opcode mnemonic.
    IINC(OpcodeValue.IINC, LOCAL_VARIABLE_INDEX_BYTE, SIGNED_VALUE_BYTE),
    INVOKEINTERFACE(OpcodeValue.INVOKEINTERFACE, CONSTANT_POOL_INDEX_SHORT,
                    UNSIGNED_VALUE_BYTE),
    MULTIANEWARRAY(OpcodeValue.MULTIANEWARRAY, CONSTANT_POOL_INDEX_SHORT,
                    UNSIGNED_VALUE_BYTE);

    //
    // DATA
    //

    private final int value;
    private final List<OperandType> operandTypes;

    //
    // CONSTRUCTORS
    //

    TwoOperandOpcode(final int value, final OperandType firstOperandType,
                     final OperandType secondOperandType) {
        this.value = value;
        this.operandTypes = List.of(firstOperandType, secondOperandType);
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
        return 0;
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

    public static TwoOperandOpcode forUnsignedByte(final int value) {
        switch (value) {
            case OpcodeValue.IINC:
                return IINC;
            case OpcodeValue.INVOKEINTERFACE:
                return INVOKEINTERFACE;
            case OpcodeValue.MULTIANEWARRAY:
                return MULTIANEWARRAY;
            default:
                final IllegalArgumentException e;
                if (0 <= value && value <= 255) {
                    e = noOpcodeForValue(TwoOperandOpcode.class, value);
                } else {
                    e = notAnUnsignedByte(value);
                }
                throw e;
        }
    }
}
