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

/**
 * <p>
 *
 * </p>
 *
 * @author Victor Schappert
 * @since 20171126
 */
public enum OperandType {

    //
    // ENUMERATORS
    //

    ZERO(1),
    SIGNED_VALUE_BYTE(1),
    UNSIGNED_VALUE_BYTE(1),
    SIGNED_VALUE_SHORT(2),
    SIGNED_VALUE_INT(4),
    BRANCH_OFFSET_SHORT(2),
    BRANCH_OFFSET_INT(4),
    LOCAL_VARIABLE_INDEX_BYTE(1),
    LOCAL_VARIABLE_INDEX_SHORT(2),
    CONSTANT_POOL_INDEX_CONSTANT_BYTE(1, true),
    CONSTANT_POOL_INDEX_CONSTANT_SHORT(2, true),
    CONSTANT_POOL_INDEX_CONSTANT2_SHORT(2, true),
    CONSTANT_POOL_INDEX_CLASS_SHORT(2, true),
    CONSTANT_POOL_INDEX_FIELD_REF_SHORT(2, true),
    CONSTANT_POOL_INDEX_CLASS_METHOD_REF_SHORT(2, true),
    CONSTANT_POOL_INDEX_INTERFACE_METHOD_REF_SHORT(2, true),
    CONSTANT_POOL_INDEX_EITHER_METHOD_REF_SHORT(2, true),
    CONSTANT_POOL_INDEX_INVOKE_DYNAMIC_SHORT(2, true),
    ATYPE_BYTE(1),
    OPCODE(1),
    PADDING,
    MATCH_OFFSET_PAIRS_TABLE,
    JUMP_OFFSET_TABLE,
    OPTIONAL_COUNT;

    //
    // DATA
    //

    private final int size;
    private final boolean constantPoolIndex;

    //
    // CONSTRUCTORS
    //

    OperandType(final int size, final boolean constantPoolIndex) {
        this.size = size;
        this.constantPoolIndex = constantPoolIndex;
    }

    OperandType(final int size) {
        this(size, false);
    }

    OperandType() {
        this(VARIABLE_SIZE, false);
    }

    //
    // PUBLIC METHODS
    //

    public int getSize() {
        return size;
    }

    public boolean isVariableSize() {
        return VARIABLE_SIZE == size;
    }

    public boolean isConstantPoolIndex() {
        return constantPoolIndex;
    }

    //
    // PUBLIC CONSTANTS
    //

    public static final int VARIABLE_SIZE = -1;
}
