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

import io.tarro.base.Valued;

/**
 * @author Victor Schappert
 * @since 20171113
 */
public enum TargetType implements Valued, Contextualized {

    //
    // ENUMERATORS
    //

    // In the list below, keep tags organized by the order in which they appear
    // in Chapter 4 ("The class File Format") of the Java Virtual Machine
    // specification.

    // DECLARATION contexts
    TYPE_PARAMETER_DECLARATION_OF_GENERIC_CLASS_OR_INTERFACE(0x00, AttributeContext.CLASS_FILE),
    TYPE_PARAMETER_DECLARATION_OF_GENERIC_METHOD_OR_CONSTRUCTOR(0x01, AttributeContext.METHOD_INFO),
    TYPE_IN_EXTENDS_OR_IMPLEMENTS_CLAUSE(0x10, AttributeContext.CLASS_FILE),
    TYPE_IN_BOUND_OF_GENERIC_CLASS_OR_INTERFACE_TYPE_DECLARATION(0x11, AttributeContext.CLASS_FILE),
    TYPE_IN_BOUND_OF_GENERIC_METHOD_OR_CONSTRUCTOR_TYPE_DECLARATION(0x12, AttributeContext.METHOD_INFO),
    TYPE_IN_FIELD_DECLARATION(0x13, AttributeContext.FIELD_INFO),
    TYPE_OF_METHOD_RETURN_OR_NEWLY_CONSTRUCTED_OBJECT(0x14, AttributeContext.METHOD_INFO),
    RECEIVER_TYPE_OF_METHOD_OR_CONSTRUCTOR(0x15, AttributeContext.METHOD_INFO),
    TYPE_IN_FORMAL_DECLARATION_OF_METHOD_CONSTRUCTOR_OR_LAMBDA(0x16, AttributeContext.METHOD_INFO),
    TYPE_IN_THROWS_CLAUSE(0x17, AttributeContext.METHOD_INFO),

    // EXPRESSION contexts
    TYPE_IN_LOCAL_VARIABLE_DECLARATION(0x40, AttributeContext.CODE),
    TYPE_IN_RESOURCE_VARIABLE_DECLARATION(0x41, AttributeContext.CODE),
    TYPE_IN_EXCEPTION_PARAMETER_DECLARATION(0x42, AttributeContext.CODE),
    TYPE_IN_INSTANCEOF_EXPRESSION(0x43, AttributeContext.CODE),
    TYPE_IN_NEW_EXPRESSION(0x44, AttributeContext.CODE),
    TYPE_IN_CONSTRUCTOR_REFERENCE_EXPRESSION(0x45, AttributeContext.CODE),
    TYPE_IN_METHOD_REFERENCE_EXPRESSION(0x46, AttributeContext.CODE),
    TYPE_IN_CAST_EXPRESSION(0x47, AttributeContext.CODE),
    TYPE_ARGUMENT_FOR_GENERIC_CONSTRUCTOR_IN_CONSTRUCTOR_INVOCATION_EXPRESSION(0x48, AttributeContext.CODE),
    TYPE_ARGUMENT_FOR_GENERIC_METHOD_IN_METHOD_INVOCATION_EXPRESSION(0x49, AttributeContext.CODE),
    TYPE_ARGUMENT_FOR_GENERIC_CONSTRUCTOR_IN_CONSTRUCTOR_REFERENCE_EXPRESSION(0x4a, AttributeContext.CODE),
    TYPE_ARGUMENT_FOR_GENERIC_METHOD_IN_METHOD_REFERENCE_EXPRESSION(0x4b, AttributeContext.CODE);

    //
    // DATA
    //

    private final int value;
    private final int attributeContext;

    //
    // CONSTRUCTORS
    //

    TargetType(final int value, final int attributeContext) {
        this.value = value;
        this.attributeContext = attributeContext;
    }

    //
    // INTERFACE: Valued
    //

    @Override
    public int getValue() {
        return value;
    }

    //
    // INTERFACE: Contextualized
    //

    @Override
    public boolean hasAttributeContext(final int attributeContext) {
        return attributeContext == (this.attributeContext & attributeContext);
    }
}
