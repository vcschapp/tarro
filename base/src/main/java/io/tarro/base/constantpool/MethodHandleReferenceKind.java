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

package io.tarro.base.constantpool;

import io.tarro.base.Valued;

/**
 * <p>
 * Enumerates available {@code reference_kind} values in a
 * {@code CONSTANT_MethodHandle_info} structure.
 * </p>
 *
 * <p>
 * The {@code reference_kind} member of the {@code CONSTANT_MethodHandle_info}
 * structure indicates how the structure's {@code reference_index} member should
 * be interpreted.
 * </p>
 *
 * @author Victor Schappert
 * @since 20171014
 * @see ConstantPoolTag#METHOD_HANDLE
 */
public enum MethodHandleReferenceKind implements Valued {

    //
    // ENUMERATORS
    //

    // Keep this list sorted in order of increasing "kind" value as set out in
    // the Java Virtual Machine Specification.
    /**
     * Kind {@code REF_getField} (1) indicating that the {@code reference_index}
     * refers to a constant pool entry of type {@code CONSTANT_Fieldref_info}
     * representing an instance field for which a field value fetching method
     * handle is to be created.
     *
     * @see #PUT_FIELD
     * @see #GET_STATIC
     * @see ConstantPoolTag#FIELDREF
     * @see io.tarro.base.bytecode.OneOperandOpcode#GETFIELD
     */
    GET_FIELD(1, "REF_getField"),
    /**
     * Kind {@code REF_getStatic} (2) indicating that the {@code reference_index}
     * refers to a constant pool entry of type {@code CONSTANT_Fieldref_info}
     * representing a {@code static} field for which a {@code static} field
     * value fetching method handle is to be created.
     *
     * @see #PUT_STATIC
     * @see #GET_FIELD
     * @see ConstantPoolTag#FIELDREF
     * @see io.tarro.base.bytecode.OneOperandOpcode#GETSTATIC
     */
    GET_STATIC(2, "REF_getStatic"),
    /**
     * Kind {@code REF_putField} (3) indicating that the {@code reference_index}
     * refers to a constant pool entry of type {@code CONSTANT_Fieldref_info}
     * representing an instance field for which a field value setting method
     * handle is to be created.
     *
     * @see #GET_FIELD
     * @see #PUT_STATIC
     * @see ConstantPoolTag#FIELDREF
     * @see io.tarro.base.bytecode.OneOperandOpcode#PUTFIELD
     */
    PUT_FIELD(3, "REF_putField"),
    /**
     * Kind {@code REF_putStatic} (4) indicating that the {@code reference_index}
     * refers to a constant pool entry of type {@code CONSTANT_Fieldref_info}
     * representing a {@code static} field for which a {@code static} field
     * setting method handle is to be created.
     * be created.
     *
     * @see #GET_STATIC
     * @see #PUT_FIELD
     * @see ConstantPoolTag#FIELDREF
     * @see io.tarro.base.bytecode.OneOperandOpcode#PUTSTATIC
     */
    PUT_STATIC(4, "REF_putStatic"),
    /**
     * Kind {@code REF_invokeVirtual} (5) indicating that the
     * {@code reference_index} refers to a constant pool entry of type
     * {@code CONSTANT_Methodref_info} representing a class' method for which a
     * method handle is to be created.
     *
     * @see #NEW_INVOKE_SPECIAL
     * @see #INVOKE_INTERFACE
     * @see #INVOKE_STATIC
     * @see #INVOKE_SPECIAL
     * @see ConstantPoolTag#METHODREF
     * @see io.tarro.base.bytecode.OneOperandOpcode#INVOKEVIRTUAL
     */
    INVOKE_VIRTUAL(5, "REF_invokeVirtual"),
    /**
     * Kind {@code REF_invokeStatic} (6) indicating that the
     * {@code reference_index} refers to a constant pool entry of type
     * {@code CONSTANT_Methodref_info} representing a class' method (or, if the
     * class file version number is
     * {@linkplain io.tarro.base.ClassFileVersion#JAVA8 52.0} or higher, it may
     * be <em>either</em> {@code CONSTANT_Methodref_info} representing a class'
     * method <em>or</em> {@code CONSTANT_InterfaceMethodref_info} representing
     * an interface's method) for which a method handle is to be created.
     *
     * @see #INVOKE_SPECIAL
     * @see #INVOKE_VIRTUAL
     * @see #INVOKE_INTERFACE
     * @see ConstantPoolTag#METHODREF
     * @see ConstantPoolTag#INTERFACE_METHODREF
     * @see io.tarro.base.bytecode.OneOperandOpcode#INVOKESTATIC
     */
    INVOKE_STATIC(6, "REF_invokeStatic"),
    /**
     * Kind {@code REF_invokeSpecial} (7) indicating that the
     * {@code reference_index} refers to a constant pool entry of type
     * {@code CONSTANT_Methodref_info} representing a class' method (or, if the
     * class file version number is
     * {@linkplain io.tarro.base.ClassFileVersion#JAVA8 52.0} or higher, it may
     * be <em>either</em> {@code CONSTANT_Methodref_info} representing a class'
     * method <em>or</em> {@code CONSTANT_InterfaceMethodref_info} representing
     * an interface's method) for which a method handle is to be created.
     *
     * @see #INVOKE_STATIC
     * @see #INVOKE_VIRTUAL
     * @see #INVOKE_INTERFACE
     * @see ConstantPoolTag#METHODREF
     * @see ConstantPoolTag#INTERFACE_METHODREF
     * @see io.tarro.base.bytecode.OneOperandOpcode#INVOKESPECIAL
     */
    INVOKE_SPECIAL(7, "REF_invokeSpecial"),
    /**
     * Kind {@code REF_newInvokeSpecial} (8) indicating that the
     * {@code reference_index} refers to a constant pool entry of type
     * {@code CONSTANT_Methodref_info} representing a class' method or
     * constructor for which a method handle is to be created.
     *
     * @see #INVOKE_VIRTUAL
     * @see #INVOKE_SPECIAL
     * @see #INVOKE_STATIC
     * @see #INVOKE_INTERFACE
     * @see ConstantPoolTag#METHODREF
     * @see io.tarro.base.bytecode.OneOperandOpcode#INVOKESPECIAL
     */
    NEW_INVOKE_SPECIAL(8, "REF_newInvokeSpecial"),
    /**
     * Kind {@code REF_invokeInterface} (9) indicating that the
     * {@code reference_index} refers to a constant pool entry of type
     * {@code CONSTANT_InterfaceMethodref_info} representing an interface's
     * method for which a method handle is to be created.
     *
     * @see #INVOKE_VIRTUAL
     * @see #INVOKE_SPECIAL
     * @see #INVOKE_STATIC
     * @see #NEW_INVOKE_SPECIAL
     * @see ConstantPoolTag#INTERFACE_METHODREF
     * @see io.tarro.base.bytecode.TwoOperandOpcode#INVOKEINTERFACE
     */
    INVOKE_INTERFACE(9, "REF_invokeInterface");

    //
    // DATA
    //

    private final int value;
    private final String kindName;

    //
    // CONSTRUCTORS
    //

    MethodHandleReferenceKind(final int value, final String kindName) {
        this.value = value;
        this.kindName = kindName;
    }

    //
    // INTERFACE: Valued
    //

    @Override
    public int getValue() {
        return value;
    }

    //
    // PUBLIC METHODS
    //

    /**
     * <p>
     * Obtains the reference kind's name.
     * </p>
     *
     * <p>
     * For example, for the value returned for the {@link #GET_FIELD}
     * enumerator is {@code "REF_getField"}.
     * </p>
     *
     * @return Kind name
     */
    public String getKindName() {
        return kindName;
    }
}
