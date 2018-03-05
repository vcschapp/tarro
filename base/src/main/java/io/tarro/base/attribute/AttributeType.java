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

import io.tarro.base.ClassFileVersion;
import io.tarro.base.Versioned;

import static io.tarro.base.ClassFileVersion.JAVA1;
import static io.tarro.base.ClassFileVersion.JAVA1_1;
import static io.tarro.base.ClassFileVersion.JAVA5;
import static io.tarro.base.ClassFileVersion.JAVA6;
import static io.tarro.base.ClassFileVersion.JAVA7;
import static io.tarro.base.ClassFileVersion.JAVA8;
import static io.tarro.base.ClassFileVersion.JAVA9;
import static io.tarro.base.attribute.AttributeContext.CLASS_FILE;
import static io.tarro.base.attribute.AttributeContext.FIELD_INFO;
import static io.tarro.base.attribute.AttributeContext.METHOD_INFO;
import static io.tarro.base.attribute.AttributeContext.NONE;

/**
 * Enumerates attributes predefined by the Java Virtual Machine Specification.
 *
 * @author Victor Schappert
 * @since 20171016
 */
public enum AttributeType implements Versioned, Contextualized {

    //
    // ENUMERATORS
    //

    /**
     * <p>
     * Special attribute type identifying an attribute that is not recognized by
     * the Java Virtual Machine specification as a predefined attribute in the
     * attribute context in which it is found.
     * </p>
     *
     * <p>
     * For example, a custom attribute named {@code Foo} has this attribute type
     * because the JVM specification does not document any attribute with that
     * name. But an attribute named {@code SourceFile} could equally have this
     * attribute type, rather than {@link #SOURCE_FILE}, if found within the
     * {@code attributes} table of a {@code field_info} structure, since
     * {@code SourceFile} is not a predefined attribute in the
     * {@linkplain AttributeContext#FIELD_INFO field} context.
     * </p>
     *
     * <p>
     * Note that unlike all the other attribute types, this attribute type's
     * {@linkplain #getAttributeName() attribute name} is {@code null}.
     * </p>
     */
    UNKNOWN(null, NONE, JAVA1),

    // Keep this list in alphabetical order by attribute name.
    /**
     * Identifies an {@code AnnotationDefault} attribute appearing on a
     * method whose purpose is to represent an element of an attribute type.
     */
    ANNOTATION_DEFAULT("AnnotationDefault", METHOD_INFO, JAVA5),
    /**
     * Identifies a {@code BootstrapMethods} attribute containing the class
     * bootstrap methods table for any class containing at least one
     * <em>invokedynamic</em> instruction.
     */
    BOOTSTRAP_METHODS("BootstrapMethods", CLASS_FILE, JAVA7),
    /**
     * Identifies a {@code Code} attribute containing the bytecode and
     * auxiliary execution information for a method that is not native or
     * abstract.
     */
    CODE("Code", METHOD_INFO, JAVA1),
    /**
     * Identifies a {@code ConstantValue} attribute containing the value of a
     * constant expression with which a static field should be initialized.
     */
    CONSTANT_VALUE("ConstantValue", FIELD_INFO, JAVA1),
    /**
     * Identifies a {@code Deprecated} attribute tagging a class file, field, or
     * method.
     */
    DEPRECATED("Deprecated", CLASS_FILE | FIELD_INFO | METHOD_INFO, JAVA1_1),
    /**
     * Identifies the {@code EnclosingMethod} attribute appearing in the class
     * level attributes of a local or anonymous class in order to link the class
     * back to the method which encloses it.
     */
    ENCLOSING_METHOD("EnclosingMethod", CLASS_FILE , JAVA5),
    /**
     * Identifies an {@code Exceptions} attribute appearing in the attributes
     * table of a method in order to declare one or more checked exceptions that
     * the method may throw.
     */
    EXCEPTIONS("Exceptions", METHOD_INFO, JAVA1),
    /**
     * Identifies an {@code InnerClasses} attribute in the class level attribute
     * table of any class which refers to classes which "are not members of a
     * package" (this can include inner, local, and anonymous classes).
     */
    INNER_CLASSES("InnerClasses", CLASS_FILE , JAVA1_1),
    /**
     * Identifies a {@code LineNumberTable} attribute attached to a
     * {@link #CODE Code} attribute for the purpose of mapping bytecode offsets
     * back to source file line numbers.
     */
    LINE_NUMBER_TABLE("LineNumberTable", AttributeContext.CODE, JAVA1),
    /**
     * <p>
     * Identifies a {@code LocalVariableTable} attribute attached to a
     * {@link #CODE Code} attribute in order to associate debug metadata with
     * the local variables of a method.
     * </p>
     *
     * <p>
     * The {@code LocalVariableTable} differs subtly from the similarly-named
     * {@link #LOCAL_VARIABLE_TYPE_TABLE LocalVariable<em>Type</em>Table} by
     * providing a <em>descriptor</em>, rather than a signature, on its entries.
     * </p>
     */
    LOCAL_VARIABLE_TABLE("LocalVariableTable", AttributeContext.CODE, JAVA1),
    /**
     * <p>
     * Identifies a {@code LocalVariableTypeTable} attribute attached to a
     * {@link #CODE Code} attribute in order to associate debug metadata with
     * the local variables of a method.
     * </p>
     *
     * <p>
     * The <code>LocalVariable<em>Type</em>Table</code> differs subtly from the
     * similarly-named {@link #LOCAL_VARIABLE_TABLE LocalVariableTable} by
     * providing a <em>signature</em>, rather than a descriptor, on its entries.
     * </p>
     */
    LOCAL_VARIABLE_TYPE_TABLE("LocalVariableTypeTable", AttributeContext.CODE,
            JAVA5),
    /**
     * Identifies a {@code MethodParameters} attribute providing debugging and
     * reflection-related meta-information about the parameters of a method
     * which cannot be discerned from the method descriptor.
     */
    METHOD_PARAMETERS("MethodParameters", METHOD_INFO, JAVA8),
    /**
     * Identifies a {@code Module} attribute appearing in a class file which
     * represents a Java module.
     */
    MODULE("Module", CLASS_FILE, JAVA9),
    /**
     * Identifies a {@code Module} attribute appearing in a class file in order
     * to identify the main class of a Java module.
     */
    MODULE_MAIN_CLASS("ModuleMainClass", CLASS_FILE, JAVA9),
    /**
     * Identifies a {@code ModulePackages} attribute listing packages for a Java
     * module.
     */
    MODULE_PACKAGES("ModulePackages", CLASS_FILE, JAVA9),
    /**
     * Identifies a {@code RuntimeInvisibleAnnotations} attribute recording
     * annotations for a class, field, or method which are generally ignored by
     * the JVM.
     *
     * @see #RUNTIME_VISIBLE_ANNOTATIONS
     */
    RUNTIME_INVISIBLE_ANNOTATIONS("RuntimeInvisibleAnnotations",
            CLASS_FILE | FIELD_INFO | METHOD_INFO, JAVA5),
    /**
     * Identifies a {@code RuntimeInvisibleParameterAnnotations} attribute
     * recording annotations on method parameters which are typically ignored
     * by the JVM.
     *
     * @see #RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS
     */
    RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS(
            "RuntimeInvisibleParameterAnnotations", METHOD_INFO, JAVA5),
    /**
     * Identifies a {@code RuntimeInvisibleTypeAnnotations} attribute recording
     * annotations on types (in any attribute context) which are typically
     * ignored by the JVM.
     *
     * @see #RUNTIME_VISIBLE_TYPE_ANNOTATIONS
     */
    RUNTIME_INVISIBLE_TYPE_ANNOTATIONS("RuntimeInvisibleTypeAnnotations",
            CLASS_FILE | FIELD_INFO | METHOD_INFO | AttributeContext.CODE,
            JAVA8),
    /**
     * Identifies a {@code RuntimeAnnotations} attribute recording annotations
     * for a class, field, or method which are always processed by the JVM.
     *
     * @see #RUNTIME_INVISIBLE_ANNOTATIONS
     */
    RUNTIME_VISIBLE_ANNOTATIONS("RuntimeVisibleAnnotations",
            CLASS_FILE | FIELD_INFO | METHOD_INFO, JAVA5),
    /**
     * Identifies a {@code RuntimeParameterAnnotations} attribute recording
     * annotations on method parameters which are always processed by the JVM.
     *
     * @see #RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS
     */
    RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS("RuntimeVisibleParameterAnnotations",
            METHOD_INFO, JAVA5),
    /**
     * Identifies a {@code RuntimeVisibleTypeAnnotations} attribute recording
     * annotations on types (in any attribute context) which are typically
     * ignored by the JVM.
     *
     * @see #RUNTIME_INVISIBLE_TYPE_ANNOTATIONS
     */
    RUNTIME_VISIBLE_TYPE_ANNOTATIONS("RuntimeVisibleTypeAnnotations",
            CLASS_FILE | FIELD_INFO | METHOD_INFO | AttributeContext.CODE,
            JAVA8),
    /**
     * <p>
     * Identifies a {@code Signature} attribute for a class, interface,
     * constructor, method, or field whose declaration in the Java programming
     * language uses type variables or parameterized types.
     * </p>
     *
     * <p>
     * A signature differs from a <em>descriptor</em> in that a signature
     * encodes generics information but a descriptor does not.
     * </p>
     */
    SIGNATURE("Signature", CLASS_FILE | FIELD_INFO | METHOD_INFO, JAVA5),
    /**
     * Identifies a {@code SourceDebugExtension} attribute appearing in a class
     * file for the purpose of storing an arbitrary string ignored by the JVM
     * but of use to a debugging program.
     */
    SOURCE_DEBUG_EXTENSION("SourceDebugExtension", CLASS_FILE, JAVA5),
    /**
     * Identifies a {@code SourceFile} attribute appearing in a class file in
     * order to link the class with the source code file from which it was
     * compiled.
     */
    SOURCE_FILE("SourceFile", CLASS_FILE , JAVA1),
    /**
     * Identifies a {@code StackMapTable} attribute attached to a
     * {@link #CODE Code} attribute in order to assist the JVM or class file
     * reader in performing verification by type checking on the method
     * bytecode.
     */
    STACK_MAP_TABLE("StackMapTable", AttributeContext.CODE, JAVA6),
    /**
     * <p>
     * Identifies a {@code Synthetic} attribute tagging a class, field, or
     * method.
     * </p>
     *
     * <p>
     * The more modern alternative is to set the {@code ACC_SYNTHETIC} access
     * flag on the relevant structure. This may be done with:
     * </p>
     *
     * <ul>
     * <li>{@link io.tarro.base.flag.ClassAccessFlag#SYNTHETIC}</li>
     * <li>{@link io.tarro.base.flag.FieldAccessFlag#SYNTHETIC}</li>
     * <li>{@link io.tarro.base.flag.MethodAccessFlag#SYNTHETIC}</li>
     * </ul>
     */
    SYNTHETIC("Synthetic", CLASS_FILE | FIELD_INFO | METHOD_INFO, JAVA1_1);

    //
    // DATA
    //

    private final String attributeName;
    private final int attributeContext;
    private final ClassFileVersion classFileVersion;

    //
    // CONSTRUCTORS
    //

    AttributeType(final String attributeName, final int attributeContext,
                  final ClassFileVersion classFileVersion) {
        this.attributeName = attributeName;
        this.classFileVersion = classFileVersion;
        this.attributeContext = attributeContext;
    }

    //
    // INTERFACE: Versioned
    //

    @Override
    public ClassFileVersion getFirstVersionSupporting() {
        return classFileVersion;
    }

    //
    // INTERFACE: Contextualized
    //

    @Override
    public int getAttributeContext() {
        return attributeContext;
    }

    //
    // PUBLIC METHODS
    //

    /**
     * <p>
     * Obtains the attribute name.
     * </p>
     *
     * <p>
     * The value returned is {@code null} for the {@link #UNKNOWN} attribute
     * type, and the non-{@code null} string documented in the Java Virtual
     * Machine specification otherwise.
     * </p>
     *
     * @return Attribute name, or {@code null} for {@link #UNKNOWN}
     */
    public String getAttributeName() {
        return attributeName;
    }
}
