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

package io.tarro.base.attribute;

import io.tarro.base.ClassFileVersion;
import io.tarro.base.Versioned;

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

    UNKNOWN(null, NONE, null),

    // Keep this list in alphabetical order by attribute name.
    ANNOTATION_DEFAULT("AnnotationDefault", METHOD_INFO, ClassFileVersion.JAVA5),
    BOOTSTRAP_METHODS("BootstrapMethods", CLASS_FILE, ClassFileVersion.JAVA7),
    CODE("Code", METHOD_INFO, ClassFileVersion.JAVA1),
    CONSTANT_VALUE("ConstantValue", FIELD_INFO, ClassFileVersion.JAVA1),
    DEPRECATED("Deprecated", CLASS_FILE | FIELD_INFO | METHOD_INFO, ClassFileVersion.JAVA1_1),
    ENCLOSING_METHOD("EnclosingMethod", CLASS_FILE , ClassFileVersion.JAVA5),
    EXCEPTIONS("Exceptions", METHOD_INFO, ClassFileVersion.JAVA1),
    INNER_CLASSES("InnerClasses", CLASS_FILE , ClassFileVersion.JAVA1_1),
    LINE_NUMBER_TABLE("LineNumberTable", AttributeContext.CODE, ClassFileVersion.JAVA1),
    LOCAL_VARIABLE_TABLE("LocalVariableTable", AttributeContext.CODE, ClassFileVersion.JAVA1),
    LOCAL_VARIABLE_TYPE_TABLE("LocalVariableTypeTable", AttributeContext.CODE, ClassFileVersion.JAVA5),
    METHOD_PARAMETERS("MethodParameters", METHOD_INFO, ClassFileVersion.JAVA8),
    MODULE("Module", CLASS_FILE, ClassFileVersion.JAVA9),
    MODULE_MAIN_CLASS("ModuleMainClass", CLASS_FILE, ClassFileVersion.JAVA9),
    MODULE_PACKAGES("ModulePackages", CLASS_FILE, ClassFileVersion.JAVA9),
    RUNTIME_INVISIBLE_ANNOTATIONS("RuntimeInvisibleAnnotations", CLASS_FILE | FIELD_INFO | METHOD_INFO, ClassFileVersion.JAVA5),
    RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS("RuntimeInvisibleParameterAnnotations", METHOD_INFO, ClassFileVersion.JAVA5),
    RUNTIME_INVISIBLE_TYPE_ANNOTATIONS("RuntimeInvisibleTypeAnnotations", CLASS_FILE | FIELD_INFO | METHOD_INFO | AttributeContext.CODE, ClassFileVersion.JAVA8),
    RUNTIME_VISIBLE_ANNOTATIONS("RuntimeVisibleAnnotations", CLASS_FILE | FIELD_INFO | METHOD_INFO, ClassFileVersion.JAVA5),
    RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS("RuntimeVisibleParameterAnnotations", METHOD_INFO, ClassFileVersion.JAVA5),
    RUNTIME_VISIBLE_TYPE_ANNOTATIONS("RuntimeVisibleTypeAnnotations", CLASS_FILE | FIELD_INFO | METHOD_INFO | AttributeContext.CODE, ClassFileVersion.JAVA8),
    SIGNATURE("Signature", CLASS_FILE | FIELD_INFO | METHOD_INFO, ClassFileVersion.JAVA5),
    SOURCE_DEBUG_EXTENSION("SourceDebugExtension", CLASS_FILE, ClassFileVersion.JAVA5),
    SOURCE_FILE("SourceFile", CLASS_FILE , ClassFileVersion.JAVA1),
    STACK_MAP_TABLE("StackMapTable", AttributeContext.CODE, ClassFileVersion.JAVA6),
    SYNTHETIC("Synthetic", CLASS_FILE | FIELD_INFO | METHOD_INFO, ClassFileVersion.JAVA1_1);

    //
    // DATA
    //

    private final String attributeName;
    private final int attributeContext;
    private final ClassFileVersion classFileVersion;

    //
    // CONSTRUCTORS
    //

    AttributeType(final String attributeName, final int attributeContext, final ClassFileVersion classFileVersion) {
        this.attributeName = attributeName;
        this.classFileVersion = classFileVersion;
        this.attributeContext = attributeContext;
    }

    //
    // INTERFACE: Versioned
    //

    @Override
    public ClassFileVersion getClassFileVersion() {
        return classFileVersion;
    }

    //
    // INTERFACE: Contextualized
    //

    @Override
    public boolean hasAttributeContext(final int attributeContext) {
        return attributeContext == (this.attributeContext & attributeContext);
    }

    //
    // PUBLIC METHODS
    //

    public String getAttributeName() {
        return attributeName;
    }
}
