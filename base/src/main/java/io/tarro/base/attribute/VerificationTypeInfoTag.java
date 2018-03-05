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

package io.tarro.base.attribute;

import io.tarro.base.Valued;

/**
 * <p>
 * Enumerates valid tag values within the {@code verification_type_info}
 * discriminated union which may appear within certain members of the
 * {@code stack_map_frame} union belonging to the {@code entries} table in a
 * {@linkplain AttributeType#STACK_MAP_TABLE StackMapTable} attribute.
 * </p>
 *
 * <p>
 * Each enumerator in this enumeration represents one of the possible
 * verification type info tags described in the Java Virtual Machine
 * Specification's section on the {@code StackMapTable} attribute. The numeric
 * tag value may be obtained from the {@link #getValue()} method.
 * </p>
 *
 * @author Victor Schappert
 * @since 20171130
 * @see FrameType
 */
public enum VerificationTypeInfoTag implements Valued {

    //
    // ENUMERATORS
    //

    // The order of this list should parallel the order of the union members
    // documented in the Java Virtual Machine Specification.
    /**
     * Indicates that the {@code verification_type_info} union's active member
     * is a {@code Top_variable_info} structure.
     */
    TOP(0),
    /**
     * Indicates that the {@code verification_type_info} union's active member
     * is an {@code Integer_variable_info} structure.
     */
    INTEGER(1),
    /**
     * Indicates that the {@code verification_type_info} union's active member
     * is a {@code Float_variable_info} structure.
     */
    FLOAT(2),
    /**
     * Indicates that the {@code verification_type_info} union's active member
     * is a {@code Null_variable_info} structure.
     */
    NULL(5),
    /**
     * Indicates that the {@code verification_type_info} union's active member
     * is an {@code UninitializedThis_variable_info} structure.
     */
    UNINITIALIZED_THIS(6),
    /**
     * Indicates that the {@code verification_type_info} union's active member
     * is an {@code Object_variable_info} structure.
     */
    OBJECT(7),
    /**
     * Indicates that the {@code verification_type_info} union's active member
     * is an {@code Uninitialized_variable_info} structure.
     */
    UNINITIALIZED(8),
    /**
     * Indicates that the {@code verification_type_info} union's active member
     * is a {@code Long_variable_info} structure.
     */
    LONG(4),
    /**
     * Indicates that the {@code verification_type_info} union's active member
     * is a {@code Double_variable_info} structure.
     */
    DOUBLE(3);

    //
    // DATA
    //

    private final int value;

    //
    // CONSTRUCTORS
    //

    VerificationTypeInfoTag(final int value) {
        this.value = value;
    }

    //
    // ANCESTOR CLASS: Valued
    //

    @Override
    public int getValue() {
        return value;
    }
}
