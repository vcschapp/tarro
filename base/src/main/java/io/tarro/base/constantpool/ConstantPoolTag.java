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

package io.tarro.base.constantpool;

import io.tarro.base.ClassFileVersion;
import io.tarro.base.Valued;
import io.tarro.base.Versioned;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static io.tarro.base.ClassFileVersion.JAVA1;
import static io.tarro.base.ClassFileVersion.JAVA5;
import static io.tarro.base.ClassFileVersion.JAVA7;
import static io.tarro.base.ClassFileVersion.JAVA9;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Enumerates the tags which indicate the structure of a constant pool entry.
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

    CLASS(7, "CONSTANT_Class", JAVA5),
    FIELD_REFERENCE(9, "CONSTANT_Fieldref", JAVA1),
    METHOD_REFERENCE(10, "CONSTANT_Methodref", JAVA1),
    INTERFACE_METHOD_REFERENCE(11, "CONSTANT_InterfaceMethodref", JAVA1),
    STRING(8, "CONSTANT_String", JAVA1),
    INTEGER(3, "CONSTANT_Integer", JAVA1),
    FLOAT(4, "CONSTANT_Float", JAVA1),
    LONG(5, "CONSTANT_Long", JAVA1, 2),
    DOUBLE(6, "CONSTANT_Double", JAVA1, 2),
    NAME_AND_TYPE(12, "CONSTANT_NameAndType", JAVA1),
    UTF8(1, "CONSTANT_Utf8", JAVA1),
    METHOD_HANDLE(15, "CONSTANT_MethodHandle", JAVA7),
    METHOD_TYPE(16, "CONSTANT_MethodType", JAVA7),
    INVOKE_DYNAMIC(17, "CONSTANT_InvokeDynamic", JAVA7),
    MODULE(19, "CONSTANT_Module", JAVA9),
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

    public String getTagName() {
        return tagName;
    }

    public String getStructureName() {
        return getTagName() + "_info";
    }

    public int getNumSlots() { return numSlots; }

    //
    // PUBLIC ANNOTATIONS
    //

    /**
     * @author Victor Schappert
     * @since 20171127
     */
    @Documented
    @Retention(RUNTIME)
    @Target(ElementType.TYPE)
    public @interface Represents {
        ConstantPoolTag[] value();
    }
}
