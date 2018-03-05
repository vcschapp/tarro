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

package io.tarro.parser.clazz;

import io.tarro.base.flag.FieldAccessFlag;
import io.tarro.base.flag.MethodAccessFlag;
import io.tarro.parser.clazz.member.Member;
import io.tarro.parser.clazz.visitor.AttributeVisitor;
import io.tarro.parser.clazz.visitor.MemberVisitor;
import io.tarro.parser.clazz.visitor.ClassAccessFlagsVisitor;
import io.tarro.parser.clazz.visitor.ConstantPoolEntryVisitor;
import io.tarro.parser.clazz.visitor.U2Visitor;
import io.tarro.parser.clazz.visitor.VersionVisitor;

import static java.util.Objects.requireNonNull;

/**
 * Builder for a {@link ClassParser}.
 *
 * @author Victor Schappert
 * @since 20171008
 */
public class ClassParserBuilder {

    //
    // DATA
    //

    private VersionVisitor versionVisitor;
    private U2Visitor constantPoolCountVisitor;
    private ConstantPoolEntryVisitor constantPoolEntryVisitor;
    private ClassAccessFlagsVisitor classAccessFlagsVisitor;
    private U2Visitor thisClassVisitor;
    private U2Visitor superClassVisitor;
    private U2Visitor interfacesCountVisitor;
    private U2Visitor interfaceVisitor;
    private U2Visitor fieldsCountVisitor;
    private MemberVisitor<FieldAccessFlag> fieldVisitor;
    private U2Visitor methodsCountVisitor;
    private MemberVisitor<MethodAccessFlag> methodVisitor;
    private U2Visitor attributesCountVisitor;
    private AttributeVisitor attributeVisitor;

    //
    // CONSTRUCTORS
    //

    @SuppressWarnings("unchecked")
    ClassParserBuilder() {
        versionVisitor = (majorVersion, minorVersion) -> { };
        constantPoolCountVisitor = NOOP_U2_VISITOR;
        constantPoolEntryVisitor = (constantPoolEntry) -> { };
        classAccessFlagsVisitor = (classAccessFlags) -> { };
        thisClassVisitor = NOOP_U2_VISITOR;
        superClassVisitor = NOOP_U2_VISITOR;
        interfacesCountVisitor = NOOP_U2_VISITOR;
        interfaceVisitor = NOOP_U2_VISITOR;
        fieldsCountVisitor = NOOP_U2_VISITOR;
        fieldVisitor = NOOP_MEMBER_VISITOR;
        methodsCountVisitor = NOOP_U2_VISITOR;
        methodVisitor = NOOP_MEMBER_VISITOR;
        attributesCountVisitor = NOOP_U2_VISITOR;
        attributeVisitor = (attribute) -> { };
    }

    //
    // PUBLIC METHODS
    //

    public ClassParser build() {
        return new ClassParser(versionVisitor,
                constantPoolCountVisitor, constantPoolEntryVisitor,
                classAccessFlagsVisitor,
                thisClassVisitor, superClassVisitor,
                interfacesCountVisitor, interfaceVisitor,
                fieldsCountVisitor, fieldVisitor,
                methodsCountVisitor, methodVisitor,
                attributesCountVisitor, attributeVisitor);
    }

    public ClassParserBuilder withVersionVisitor(
            final VersionVisitor versionVisitor) {
        this.versionVisitor = requireNonNull(versionVisitor, "versionVisitor cannot be null");
        return this;
    }

    public ClassParserBuilder withConstantPoolCountVisitor(
            final U2Visitor constantPoolCountVisitor) {
        this.constantPoolCountVisitor = requireNonNull(constantPoolCountVisitor, "constantPoolCountVisitor cannot be null");
        return this;
    }

    public ClassParserBuilder withConstantPoolEntryVisitor(
            final ConstantPoolEntryVisitor constantPoolEntryVisitor) {
        this.constantPoolEntryVisitor = requireNonNull(constantPoolEntryVisitor, "constantPoolEntryVisitor cannot be null");
        return this;
    }

    public ClassParserBuilder withClassAccessFlagsVisitor(
            final ClassAccessFlagsVisitor classAccessFlagsVisitor) {
        this.classAccessFlagsVisitor = requireNonNull(classAccessFlagsVisitor, "classAccessFlagsVisitor cannot be null");
        return this;
    }

    public ClassParserBuilder withThisClassVisitor(
            final U2Visitor thisClassVisitor) {
        this.thisClassVisitor = requireNonNull(thisClassVisitor, "thisClassVisitor cannot be null");
        return this;
    }

    public ClassParserBuilder withSuperClassVisitor(
            final U2Visitor superClassVisitor) {
        this.superClassVisitor = requireNonNull(superClassVisitor, "superClassVisitor cannot be null");
        return this;
    }

    public ClassParserBuilder withInterfacesCountVisitor(
            final U2Visitor interfacesCountVisitor) {
        this.interfacesCountVisitor = requireNonNull(interfacesCountVisitor, "interfacesCountVisitor cannot be null");
        return this;
    }

    public ClassParserBuilder withInterfaceVisitor(
            final U2Visitor interfaceVisitor) {
        this.interfaceVisitor = requireNonNull(interfaceVisitor, "interfaceVisitor cannot be null");
        return this;
    }

    public ClassParserBuilder withFieldsCountVisitor(
            final U2Visitor fieldsCountVisitor) {
        this.fieldsCountVisitor = requireNonNull(fieldsCountVisitor, "fieldsCountVisitor cannot be null");
        return this;
    }

    public ClassParserBuilder withFieldVisitor(
            final MemberVisitor<FieldAccessFlag> fieldVisitor) {
        this.fieldVisitor = requireNonNull(fieldVisitor, "fieldVisitor cannot be null");
        return this;
    }

    public ClassParserBuilder withMethodsCountVisitor(
            final U2Visitor methodsCountVisitor) {
        this.methodsCountVisitor = requireNonNull(methodsCountVisitor, "methodsCountVisitor cannot be null");
        return this;
    }

    public ClassParserBuilder withMethodVisitor(
            final MemberVisitor<MethodAccessFlag> methodVisitor) {
        this.methodVisitor = requireNonNull(methodVisitor, "methodVisitor cannot be null");
        return this;
    }

    public ClassParserBuilder withAttributesCountVisitor(
            final U2Visitor attributesCountVisitor) {
        this.attributesCountVisitor = requireNonNull(attributesCountVisitor, "attributesCountVisitor cannot be null");
        return this;
    }

    public ClassParserBuilder withAttributeVisitor(
            final AttributeVisitor attributeVisitor) {
        this.attributeVisitor = requireNonNull(attributeVisitor, "attributeVisitor cannot be null");
        return this;
    }

    //
    // INTERNALS
    //

    private static final U2Visitor NOOP_U2_VISITOR = (u2) -> { };
    @SuppressWarnings("rawtypes")
    private static final MemberVisitor NOOP_MEMBER_VISITOR = (member) -> { };
}
