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

import io.tarro.base.ClassFileVersion;
import io.tarro.base.Valued;
import io.tarro.base.Versioned;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static io.tarro.base.ClassFileVersion.JAVA1;
import static io.tarro.base.ClassFileVersion.JAVA7;
import static io.tarro.base.ClassFileVersion.JAVA9;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * <p>
 * Enumerates the tags which indicate the structure of a constant pool entry.
 * </p>
 *
 * <p>
 * Each member of this enumeration represents one of the {@code tag} values
 * documented in the Java Virtual Machine Specification's section on the class
 * file's {@code constant_pool}. The numeric tag value corresponding to a
 * particular enumerator may be obtained from the {@link #getValue()} method.
 * </p>
 *
 * @author Victor Schappert (schapper@)
 * @since 20171009
 */
public enum ConstantPoolTag implements Valued, Versioned {

    //
    // ENUMERATORS
    //

    // In the list below, keep tags organized by the order in which they appear
    // in Chapter 4 ("The class File Format") of the Java Virtual Machine
    // specification.

    /**
     * Tag {@code CONSTANT_Class} (7) identifying a structure of type
     * {@code CONSTANT_Class_info}.
     */
    CLASS(7, "CONSTANT_Class", JAVA1),
    /**
     * Tag {@code CONSTANT_Fieldref} (9) identifying a structure of type
     * {@code CONSTANT_Fieldref_info}.
     *
     * @see #METHODREF
     * @see #INTERFACE_METHODREF
     */
    FIELDREF(9, "CONSTANT_Fieldref", JAVA1),
    /**
     * Tag {@code CONSTANT_Methodref} (10) identifying a structure of type
     * {@code CONSTANT_Methodref_info}.
     *
     * @see #INTERFACE_METHODREF
     * @see #FIELDREF
     */
    METHODREF(10, "CONSTANT_Methodref", JAVA1),
    /**
     * Tag {@code CONSTANT_InterfaceMethodref} (11) identifying a structure of
     * type {@code CONSTANT_InterfaceMethodref_info}.
     *
     * @see #METHODREF
     * @see #FIELDREF
     */
    INTERFACE_METHODREF(11, "CONSTANT_InterfaceMethodref", JAVA1),
    /**
     * Tag {@code CONSTANT_String} (8) identifying a structure of type
     * {@code CONSTANT_String_info}.
     *
     * @see #UTF8
     */
    STRING(8, "CONSTANT_String", JAVA1),
    /**
     * Tag {@code CONSTANT_Integer} (3) identifying a structure of type
     * {@code CONSTANT_Integer_info}.
     *
     * @see #FLOAT
     * @see #LONG
     * @see #DOUBLE
     */
    INTEGER(3, "CONSTANT_Integer", JAVA1),
    /**
     * Tag {@code CONSTANT_Float} (4) identifying a structure of type
     * {@code CONSTANT_Float_info}.
     *
     * @see #INTEGER
     * @see #LONG
     * @see #DOUBLE
     */
    FLOAT(4, "CONSTANT_Float", JAVA1),
    /**
     * Tag {@code CONSTANT_Long} (5) identifying a structure of type
     * {@code CONSTANT_Long_info}.
     *
     * @see #INTEGER
     * @see #FLOAT
     * @see #DOUBLE
     */
    LONG(5, "CONSTANT_Long", JAVA1, 2),
    /**
     * Tag {@code CONSTANT_Double} (6) identifying a structure of type
     * {@code CONSTANT_Double_info}.
     *
     * @see #INTEGER
     * @see #FLOAT
     * @see #LONG
     */
    DOUBLE(6, "CONSTANT_Double", JAVA1, 2),
    /**
     * Tag {@code CONSTANT_NameAndType} (12) identifying a structure of type
     * {@code CONSTANT_NameAndType_info}.
     */
    NAME_AND_TYPE(12, "CONSTANT_NameAndType", JAVA1),
    /**
     * Tag {@code CONSTANT_Utf8} (1) identifying a structure of type
     * {@code CONSTANT_Utf8_info}.
     */
    UTF8(1, "CONSTANT_Utf8", JAVA1),
    /**
     * Tag {@code CONSTANT_MethodHandle} (15) identifying a structure of type
     * {@code CONSTANT_MethodHandle_info}.
     *
     * @see MethodHandleReferenceKind
     * @see #METHOD_TYPE
     * @see #INVOKE_DYNAMIC
     */
    METHOD_HANDLE(15, "CONSTANT_MethodHandle", JAVA7),
    /**
     * Tag {@code CONSTANT_MethodType} (16) identifying a structure of type
     * {@code CONSTANT_MethodType_info}.
     *
     * @see #METHOD_HANDLE
     * @see #INVOKE_DYNAMIC
     */
    METHOD_TYPE(16, "CONSTANT_MethodType", JAVA7),
    /**
     * Tag {@code CONSTANT_InvokeDynamic} (18) identifying a structure of type
     * {@code CONSTANT_InvokeDynamic_info}.
     *
     * @see #METHOD_HANDLE
     * @see #METHOD_TYPE
     */
    INVOKE_DYNAMIC(18, "CONSTANT_InvokeDynamic", JAVA7),
    /**
     * Tag {@code CONSTANT_Module} (19) identifying a structure of type
     * {@code CONSTANT_Module_info}.
     *
     * @see #PACKAGE
     */
    MODULE(19, "CONSTANT_Module", JAVA9),
    /**
     * Tag {@code CONSTANT_Package} (20) identifying a structure of type
     * {@code CONSTANT_Package_info}.
     *
     * @see #MODULE
     */
    PACKAGE(20, "CONSTANT_Package", JAVA9);

    //
    // DATA
    //

    private final int value;
    private final String tagName;
    private final ClassFileVersion classFileVersion;
    private final int numSlots;

    //
    // CONSTRUCTORS
    //

    ConstantPoolTag(final int value, final String tagName,
            final ClassFileVersion classFileVersion, final int numSlots) {
        assert 1 == numSlots || 2 == numSlots;
        this.value = value;
        this.tagName = tagName;
        this.classFileVersion = classFileVersion;
        this.numSlots = numSlots;
    }

    ConstantPoolTag(final int value, final String tagName,
            final ClassFileVersion classFileVersion) {
        this(value, tagName, classFileVersion, 1);
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
    public ClassFileVersion getFirstVersionSupporting() {
        return classFileVersion;
    }

    //
    // PUBLIC METHODS
    //

    /**
     * <p>
     * Obtains the name of the tag which this enumerator represents.
     * </p>
     *
     * <p>
     * For example, this method returns {@code "CONSTANT_Utf8"} when called on
     * the {@link #UTF8} enumerator.
     * </p>
     *
     * @return Tag name
     * @see #getStructureName()
     */
    public String getTagName() {
        return tagName;
    }

    /**
     * <p>
     * Obtains the name of the constant pool structure in which a tag
     * corresponding to this enumerator can be found.
     * </p>
     *
     * <p>
     * For example, this method returns {@code "CONSTANT_Utf8_info"} when called
     * on the {@link #UTF8} enumerator.
     * </p>
     *
     * @return Structure name
     * @see #getTagName()
     */
    public String getStructureName() {
        return getTagName() + "_info";
    }

    /**
     * <p>
     * Obtains the number of consecutive constant pool slots that the constant
     * pool structure corresponding to this enumerator occupies.
     * </p>
     *
     * <p>
     * The return value is only ever 1 or 2 and, in particular, is 2 for
     * {@link #LONG} and {@link #DOUBLE} and 1 for every other enumerator.
     * </p>
     *
     * @return Number of slots occupied (1 or 2)
     */
    public int getNumSlots() {
        return numSlots;
    }

    //
    // PUBLIC ANNOTATIONS
    //

    /**
     * <p>
     * Indicates that the type so annotated is a data structure representing
     * the data associated with one or more constant pool tags.
     * </p>
     *
     * <p>
     * This annotation is intended purely as a way of documenting connections
     * between constant pool tags and types defined in other modules.
     * </p>
     *
     * <p>
     * For example, suppose the class {@code Foo} has been designed to represent
     * the data in a {@code CONSTANT_Utf8_info} structure. The class could be
     * annotated:
     * </p>
     *
     * <blockquote><pre><code> @AssociatedWith(ConstantPoolTag.UTF8)
     * class Foo {
     *     ...
     * }</code></pre></blockquote>
     *
     * @author Victor Schappert
     * @since 20171127
     */
    @Documented
    @Retention(SOURCE)
    @Target(ElementType.TYPE)
    public @interface AssociatedWith {
        /**
         * Gets the associated tags.
         *
         * @return Set of constant pool tags the annotated type is associated
         *         with
         */
        ConstantPoolTag[] value();
    }
}
