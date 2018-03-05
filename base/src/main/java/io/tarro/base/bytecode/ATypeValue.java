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

/**
 * Contains the (primitive) array type codes used by the <em>atype</em> operand
 * of the {@link OneOperandOpcode#NEWARRAY newarray} instruction.
 *
 * @author Victor Schappert
 * @since 20171128
 * @see OneOperandOpcode#NEWARRAY
 * @see OperandType#ATYPE_BYTE
 */
public final class ATypeValue {

    //
    // PUBLIC CONSTANTS
    //

    /**
     * Indicates primitive {@code boolean} as the new array type ({@code atype}
     * {@value T_BOOLEAN}).
     */
    public static final int T_BOOLEAN = 4;
    /**
     * Indicates primitive {@code char} as the new array type ({@code atype}
     * {@value T_CHAR}).
     */
    public static final int T_CHAR = 5;
    /**
     * Indicates primitive {@code float} as the new array type ({@code atype}
     * {@value T_FLOAT}).
     */
    public static final int T_FLOAT = 6;
    /**
     * Indicates primitive {@code double} as the new array type ({@code atype}
     * {@value T_DOUBLE}).
     */
    public static final int T_DOUBLE = 7;
    /**
     * Indicates primitive {@code byte} as the new array type ({@code atype}
     * {@value T_BYTE}).
     */
    public static final int T_BYTE = 8;
    /**
     * Indicates primitive {@code short} as the new array type ({@code atype}
     * {@value T_SHORT}).
     */
    public static final int T_SHORT = 9;
    /**
     * Indicates primitive {@code int} as the new array type ({@code atype}
     * {@value T_INT}).
     */
    public static final int T_INT = 10;
    /**
     * Indicates primitive {@code long} as the new array type ({@code atype}
     * {@value T_LONG}).
     */
    public static final int T_LONG = 11;

    /**
     * Generic name for the minimum possible {@code atype} value. This
     * intended mainly as a convenient symbolic name for code which requires a
     * range.
     *
     * @see #MAX_VALUE
     */
    public static final int MIN_VALUE = T_BOOLEAN;
    /**
     * Generic name for the minimum possible {@code atype} value. This
     * intended mainly as a convenient symbolic name for code which requires a
     * range.
     *
     * @see #MIN_VALUE
     */
    public static final int MAX_VALUE = T_LONG;

    //
    // PUBLIC STATICS
    //

    /**
     * Determines whether an integer is a valid {@code atype} value for the
     * {@linkplain OneOperandOpcode#NEWARRAY newarray} instruction.
     *
     * @param value Candidate value to test for {@code atype}-ness
     * @return Whether {@code value} is a valid {@code atype}
     */
    public static boolean isValidATypeValue(final int value) {
        return 0 == ((value - 4) & 0xfffffff8);
    }

    //
    // CONSTRUCTORS
    //

    private ATypeValue() {
    }
}
