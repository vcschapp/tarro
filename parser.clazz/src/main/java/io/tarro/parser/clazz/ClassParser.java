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

package io.tarro.parser.clazz;

import io.tarro.base.InvalidConstantPoolIndexException;
import io.tarro.base.PinpointFormatException;
import io.tarro.base.attribute.FrameType;
import io.tarro.base.attribute.VerificationTypeInfoTag;
import io.tarro.base.flag.MethodAccessFlag;
import io.tarro.parser.bytecode.ByteCodeFormatException;
import io.tarro.parser.clazz.attribute.Annotation;
import io.tarro.parser.clazz.attribute.AnnotationDefaultAttribute;
import io.tarro.parser.clazz.attribute.CodeAttribute;
import io.tarro.parser.clazz.attribute.ConstantPoolIndexAttribute;
import io.tarro.parser.clazz.attribute.IntPair;
import io.tarro.parser.clazz.attribute.ListAttribute;
import io.tarro.parser.clazz.attribute.ModuleProvide;
import io.tarro.parser.clazz.attribute.ParameterAnnotations;
import io.tarro.parser.clazz.attribute.annotation.AnnotationElementValue;
import io.tarro.parser.clazz.attribute.annotation.ClassElementValue;
import io.tarro.parser.clazz.attribute.annotation.ConstantElementValue;
import io.tarro.parser.clazz.attribute.annotation.ElementValuePair;
import io.tarro.parser.clazz.attribute.annotation.EnumConstantElementValue;
import io.tarro.parser.clazz.attribute.annotation.LocalVariableTargetTableEntry;
import io.tarro.parser.clazz.attribute.annotation.TypeAnnotation;
import io.tarro.parser.clazz.attribute.annotation.TypePathStep;
import io.tarro.parser.clazz.attribute.stackmap.ObjectVariableInfo;
import io.tarro.parser.clazz.constantpool.FloatEntry;
import io.tarro.parser.clazz.constantpool.NameAndTypeEntry;
import io.tarro.parser.clazz.attribute.BootstrapMethod;
import io.tarro.parser.clazz.attribute.ExceptionTableEntry;
import io.tarro.base.attribute.AttributeContext;
import io.tarro.base.Magic;
import io.tarro.base.flag.Flag;
import io.tarro.base.flag.ModuleFlag;
import io.tarro.parser.clazz.attribute.stackmap.StackMapFrame;
import io.tarro.parser.clazz.attribute.stackmap.UninitializedVariableInfo;
import io.tarro.parser.clazz.attribute.stackmap.VerificationTypeInfo;
import io.tarro.parser.clazz.constantpool.MemberReferenceEntry;
import io.tarro.parser.clazz.constantpool.Utf8Entry;
import io.tarro.parser.clazz.internal.context.annotation.ArrayContext;
import io.tarro.parser.clazz.internal.context.annotation.AttributeTypeContext;
import io.tarro.parser.clazz.internal.context.annotation.ConstantPoolTagContext;
import io.tarro.parser.clazz.internal.context.annotation.ContextSymbol;
import io.tarro.parser.clazz.internal.context.annotation.FrameTypeContext;
import io.tarro.parser.clazz.internal.context.annotation.StructureContext;
import io.tarro.parser.clazz.member.Member;
import io.tarro.parser.clazz.visitor.AttributeVisitor;
import io.tarro.parser.clazz.visitor.ClassAccessFlagsVisitor;
import io.tarro.parser.clazz.visitor.MemberVisitor;
import io.tarro.parser.clazz.attribute.InnerClass;
import io.tarro.parser.clazz.attribute.LineNumberTableEntry;
import io.tarro.parser.clazz.attribute.MethodParameter;
import io.tarro.parser.clazz.attribute.ModuleAttribute;
import io.tarro.parser.clazz.attribute.ModuleReference;
import io.tarro.parser.clazz.attribute.PackageReference;
import io.tarro.parser.clazz.attribute.annotation.ArrayElementValue;
import io.tarro.parser.clazz.attribute.Attribute;
import io.tarro.parser.clazz.attribute.annotation.ElementValue;
import io.tarro.parser.clazz.attribute.ConstantPoolIndicesAttribute;
import io.tarro.parser.clazz.attribute.EnclosingMethodAttribute;
import io.tarro.parser.clazz.attribute.LocalVariableTableEntry;
import io.tarro.parser.clazz.attribute.RawAttribute;
import io.tarro.base.attribute.AttributeType;
import io.tarro.base.constantpool.ConstantPoolTag;
import io.tarro.base.attribute.ElementValueTag;
import io.tarro.base.constantpool.MethodHandleReferenceKind;
import io.tarro.base.attribute.TargetType;
import io.tarro.base.attribute.TypePathKind;
import io.tarro.base.Valued;
import io.tarro.base.flag.FlagMixRule;
import io.tarro.base.flag.BadAccessFlagsMixException;
import io.tarro.base.flag.ClassAccessFlag;
import io.tarro.base.flag.FieldAccessFlag;
import io.tarro.base.flag.InnerClassAccessFlag;
import io.tarro.base.flag.MethodParameterAccessFlag;
import io.tarro.base.flag.ModuleReferenceFlag;
import io.tarro.base.flag.PackageReferenceFlag;
import io.tarro.parser.clazz.constantpool.DoubleEntry;
import io.tarro.parser.clazz.constantpool.InvokeDynamicEntry;
import io.tarro.parser.clazz.constantpool.LongEntry;
import io.tarro.parser.clazz.constantpool.MethodHandleEntry;
import io.tarro.parser.clazz.constantpool.Utf8ReferenceEntry;
import io.tarro.parser.clazz.constantpool.IntegerEntry;
import io.tarro.parser.clazz.visitor.ConstantPoolEntryVisitor;
import io.tarro.parser.clazz.visitor.U2Visitor;
import io.tarro.parser.clazz.visitor.VersionVisitor;
import io.tarro.base.visitor.Visitor;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UTFDataFormatException;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collector;

import static io.tarro.base.InternalError.unhandledEnumerator;
import static io.tarro.base.InvalidConstantPoolIndexException.lessThanOne;
import static io.tarro.base.InvalidConstantPoolIndexException.notLessThanCount;
import static io.tarro.base.attribute.AttributeContext.CLASS_FILE;
import static io.tarro.base.attribute.AttributeContext.FIELD_INFO;
import static io.tarro.base.attribute.AttributeContext.METHOD_INFO;
import static io.tarro.base.attribute.AttributeType.STACK_MAP_TABLE;
import static io.tarro.base.attribute.FrameType.APPEND;
import static io.tarro.base.attribute.FrameType.CHOP;
import static io.tarro.base.attribute.FrameType.FULL_FRAME;
import static io.tarro.base.attribute.FrameType.SAME;
import static io.tarro.base.attribute.FrameType.SAME_FRAME_EXTENDED;
import static io.tarro.base.attribute.FrameType.SAME_LOCALS_1_STACK_ITEM;
import static io.tarro.base.attribute.FrameType.SAME_LOCALS_1_STACK_ITEM_EXTENDED;
import static io.tarro.base.attribute.VerificationTypeInfoTag.NULL;
import static io.tarro.base.attribute.VerificationTypeInfoTag.TOP;
import static io.tarro.base.attribute.VerificationTypeInfoTag.UNINITIALIZED_THIS;
import static io.tarro.base.flag.FieldAccessFlag.classFieldRules;
import static io.tarro.base.flag.FieldAccessFlag.interfaceFieldRules;
import static io.tarro.base.flag.MethodAccessFlag.classMethodBaseRules;
import static io.tarro.base.flag.MethodAccessFlag.interfaceMethodBaseRules;
import static io.tarro.parser.clazz.internal.context.path.ContextPathTracer.traceContextPath;
import static java.lang.String.format;
import static java.util.Arrays.copyOf;
import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.joining;
import static io.tarro.base.attribute.AttributeType.ANNOTATION_DEFAULT;
import static io.tarro.base.attribute.AttributeType.BOOTSTRAP_METHODS;
import static io.tarro.base.attribute.AttributeType.CODE;
import static io.tarro.base.attribute.AttributeType.CONSTANT_VALUE;
import static io.tarro.base.attribute.AttributeType.DEPRECATED;
import static io.tarro.base.attribute.AttributeType.ENCLOSING_METHOD;
import static io.tarro.base.attribute.AttributeType.EXCEPTIONS;
import static io.tarro.base.attribute.AttributeType.INNER_CLASSES;
import static io.tarro.base.attribute.AttributeType.LINE_NUMBER_TABLE;
import static io.tarro.base.attribute.AttributeType.LOCAL_VARIABLE_TABLE;
import static io.tarro.base.attribute.AttributeType.LOCAL_VARIABLE_TYPE_TABLE;
import static io.tarro.base.attribute.AttributeType.METHOD_PARAMETERS;
import static io.tarro.base.attribute.AttributeType.MODULE_MAIN_CLASS;
import static io.tarro.base.attribute.AttributeType.MODULE_PACKAGES;
import static io.tarro.base.attribute.AttributeType.RUNTIME_INVISIBLE_ANNOTATIONS;
import static io.tarro.base.attribute.AttributeType.RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS;
import static io.tarro.base.attribute.AttributeType.RUNTIME_INVISIBLE_TYPE_ANNOTATIONS;
import static io.tarro.base.attribute.AttributeType.RUNTIME_VISIBLE_ANNOTATIONS;
import static io.tarro.base.attribute.AttributeType.RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS;
import static io.tarro.base.attribute.AttributeType.RUNTIME_VISIBLE_TYPE_ANNOTATIONS;
import static io.tarro.base.attribute.AttributeType.SIGNATURE;
import static io.tarro.base.attribute.AttributeType.SOURCE_DEBUG_EXTENSION;
import static io.tarro.base.attribute.AttributeType.SOURCE_FILE;
import static io.tarro.base.attribute.AttributeType.SYNTHETIC;
import static io.tarro.base.attribute.AttributeType.UNKNOWN;
import static io.tarro.base.constantpool.ConstantPoolTag.CLASS;
import static io.tarro.base.constantpool.ConstantPoolTag.DOUBLE;
import static io.tarro.base.constantpool.ConstantPoolTag.FIELD_REFERENCE;
import static io.tarro.base.constantpool.ConstantPoolTag.FLOAT;
import static io.tarro.base.constantpool.ConstantPoolTag.INTEGER;
import static io.tarro.base.constantpool.ConstantPoolTag.INTERFACE_METHOD_REFERENCE;
import static io.tarro.base.constantpool.ConstantPoolTag.INVOKE_DYNAMIC;
import static io.tarro.base.constantpool.ConstantPoolTag.LONG;
import static io.tarro.base.constantpool.ConstantPoolTag.METHOD_HANDLE;
import static io.tarro.base.constantpool.ConstantPoolTag.METHOD_REFERENCE;
import static io.tarro.base.constantpool.ConstantPoolTag.METHOD_TYPE;
import static io.tarro.base.constantpool.ConstantPoolTag.MODULE;
import static io.tarro.base.constantpool.ConstantPoolTag.NAME_AND_TYPE;
import static io.tarro.base.constantpool.ConstantPoolTag.PACKAGE;
import static io.tarro.base.constantpool.ConstantPoolTag.STRING;
import static io.tarro.base.constantpool.ConstantPoolTag.UTF8;
import static io.tarro.base.attribute.ElementValueTag.BOOLEAN;
import static io.tarro.base.attribute.ElementValueTag.BYTE;
import static io.tarro.base.attribute.ElementValueTag.CHAR;
import static io.tarro.base.attribute.ElementValueTag.INT;
import static io.tarro.base.attribute.ElementValueTag.SHORT;
import static io.tarro.base.constantpool.MethodHandleReferenceKind.GET_FIELD;
import static io.tarro.base.constantpool.MethodHandleReferenceKind.GET_STATIC;
import static io.tarro.base.constantpool.MethodHandleReferenceKind.INVOKE_INTERFACE;
import static io.tarro.base.constantpool.MethodHandleReferenceKind.INVOKE_SPECIAL;
import static io.tarro.base.constantpool.MethodHandleReferenceKind.INVOKE_STATIC;
import static io.tarro.base.constantpool.MethodHandleReferenceKind.INVOKE_VIRTUAL;
import static io.tarro.base.constantpool.MethodHandleReferenceKind.NEW_INVOKE_SPECIAL;
import static io.tarro.base.constantpool.MethodHandleReferenceKind.PUT_FIELD;
import static io.tarro.base.constantpool.MethodHandleReferenceKind.PUT_STATIC;
import static io.tarro.base.attribute.TargetType.RECEIVER_TYPE_OF_METHOD_OR_CONSTRUCTOR;
import static io.tarro.base.attribute.TargetType.TYPE_ARGUMENT_FOR_GENERIC_CONSTRUCTOR_IN_CONSTRUCTOR_INVOCATION_EXPRESSION;
import static io.tarro.base.attribute.TargetType.TYPE_ARGUMENT_FOR_GENERIC_CONSTRUCTOR_IN_CONSTRUCTOR_REFERENCE_EXPRESSION;
import static io.tarro.base.attribute.TargetType.TYPE_ARGUMENT_FOR_GENERIC_METHOD_IN_METHOD_INVOCATION_EXPRESSION;
import static io.tarro.base.attribute.TargetType.TYPE_ARGUMENT_FOR_GENERIC_METHOD_IN_METHOD_REFERENCE_EXPRESSION;
import static io.tarro.base.attribute.TargetType.TYPE_IN_BOUND_OF_GENERIC_CLASS_OR_INTERFACE_TYPE_DECLARATION;
import static io.tarro.base.attribute.TargetType.TYPE_IN_BOUND_OF_GENERIC_METHOD_OR_CONSTRUCTOR_TYPE_DECLARATION;
import static io.tarro.base.attribute.TargetType.TYPE_IN_CAST_EXPRESSION;
import static io.tarro.base.attribute.TargetType.TYPE_IN_CONSTRUCTOR_REFERENCE_EXPRESSION;
import static io.tarro.base.attribute.TargetType.TYPE_IN_EXCEPTION_PARAMETER_DECLARATION;
import static io.tarro.base.attribute.TargetType.TYPE_IN_EXTENDS_OR_IMPLEMENTS_CLAUSE;
import static io.tarro.base.attribute.TargetType.TYPE_IN_FIELD_DECLARATION;
import static io.tarro.base.attribute.TargetType.TYPE_IN_FORMAL_DECLARATION_OF_METHOD_CONSTRUCTOR_OR_LAMBDA;
import static io.tarro.base.attribute.TargetType.TYPE_IN_INSTANCEOF_EXPRESSION;
import static io.tarro.base.attribute.TargetType.TYPE_IN_LOCAL_VARIABLE_DECLARATION;
import static io.tarro.base.attribute.TargetType.TYPE_IN_METHOD_REFERENCE_EXPRESSION;
import static io.tarro.base.attribute.TargetType.TYPE_IN_NEW_EXPRESSION;
import static io.tarro.base.attribute.TargetType.TYPE_IN_RESOURCE_VARIABLE_DECLARATION;
import static io.tarro.base.attribute.TargetType.TYPE_IN_THROWS_CLAUSE;
import static io.tarro.base.attribute.TargetType.TYPE_OF_METHOD_RETURN_OR_NEWLY_CONSTRUCTED_OBJECT;
import static io.tarro.base.attribute.TargetType.TYPE_PARAMETER_DECLARATION_OF_GENERIC_CLASS_OR_INTERFACE;
import static io.tarro.base.attribute.TargetType.TYPE_PARAMETER_DECLARATION_OF_GENERIC_METHOD_OR_CONSTRUCTOR;
import static io.tarro.base.attribute.TypePathKind.DEEPER_IN_ARRAY_TYPE;
import static io.tarro.base.attribute.TypePathKind.DEEPER_IN_NESTED_TYPE;
import static io.tarro.base.attribute.TypePathKind.ON_TYPE_ARGUMENT;
import static io.tarro.base.attribute.TypePathKind.ON_TYPE_ARGUMENT_WILDCARD_BOUND;

/**
 * <p>
 * Parser for Java class files.
 * </p>
 *
 * <p>
 * This is the main class for parsing Java class files. Performs stream-oriented
 * parsing of the Java class file using the visitor pattern to allow you to
 * handle any parts of the Java class file you are interested in. You
 * instantiate it using the builder pattern. Obtain a builder using the static
 * method {@link #builder()}.
 * </p>
 *
 * <p>
 * Once you have built the parser, you may use it to parse a Java class file
 * with the {@link #parse(InputStream)} method.
 * </p>
 *
 * @author Victor Schappert
 * @since 20171008
 */
public final class ClassParser {

    //
    // DATA
    //

    // -------------------------------------------------------------------------
    // Fields fixed at construction time.
    // -------------------------------------------------------------------------
    private final VersionVisitor versionVisitor;

    private final U2Visitor constantPoolCountVisitor;
    private final ConstantPoolEntryVisitor constantPoolEntryVisitor;

    private final ClassAccessFlagsVisitor classAccessFlagsVisitor;
    private final U2Visitor thisClassVisitor;
    private final U2Visitor superClassVisitor;
    private final U2Visitor interfacesCountVisitor;
    private final U2Visitor interfaceVisitor;

    private final U2Visitor fieldsCountVisitor;
    private final MemberVisitor<FieldAccessFlag> fieldVisitor;
    private final U2Visitor methodsCountVisitor;
    private final MemberVisitor<MethodAccessFlag> methodVisitor;
    private final U2Visitor attributesCountVisitor;
    private final AttributeVisitor attributeVisitor;

    private final Visitor[] allVisitors;
    // -------------------------------------------------------------------------
    // Fields modified during parse().
    // -------------------------------------------------------------------------
    private int[] arrayContext;

    private DataInputStream inputStream;
    private int inputStreamPos;
    private int arrayPosition;
    private ConstantPoolMetadata constantPoolMetadata;
    private boolean isInterface;
    private ByteCodeValidator byteCodeValidator;

    //
    // CONSTRUCTORS
    //

    ClassParser(
            final VersionVisitor versionVisitor,
            final U2Visitor constantPoolCountVisitor,
            final ConstantPoolEntryVisitor constantPoolEntryVisitor,
            final ClassAccessFlagsVisitor classAccessFlagsVisitor,
            final U2Visitor thisClassVisitor,
            final U2Visitor superClassVisitor,
            final U2Visitor interfacesCountVisitor,
            final U2Visitor interfaceVisitor,
            final U2Visitor fieldsCountVisitor,
            final MemberVisitor<FieldAccessFlag> fieldVisitor,
            final U2Visitor methodsCountVisitor,
            final MemberVisitor<MethodAccessFlag> methodVisitor,
            final U2Visitor attributesCountVisitor,
            final AttributeVisitor attributeVisitor) {
        this.versionVisitor = versionVisitor;
        this.constantPoolCountVisitor = constantPoolCountVisitor;
        this.constantPoolEntryVisitor = constantPoolEntryVisitor;
        this.classAccessFlagsVisitor = classAccessFlagsVisitor;
        this.thisClassVisitor = thisClassVisitor;
        this.superClassVisitor = superClassVisitor;
        this.interfacesCountVisitor = interfacesCountVisitor;
        this.interfaceVisitor = interfaceVisitor;
        this.fieldsCountVisitor = fieldsCountVisitor;
        this.fieldVisitor = fieldVisitor;
        this.methodsCountVisitor = methodsCountVisitor;
        this.methodVisitor = methodVisitor;
        this.attributesCountVisitor = attributesCountVisitor;
        this.attributeVisitor = attributeVisitor;
        this.allVisitors = toArray(versionVisitor,
                constantPoolCountVisitor, constantPoolEntryVisitor,
                classAccessFlagsVisitor,
                thisClassVisitor, superClassVisitor,
                interfacesCountVisitor, interfaceVisitor,
                fieldsCountVisitor, fieldVisitor,
                methodsCountVisitor, methodVisitor,
                attributesCountVisitor, attributeVisitor);
        this.arrayContext = new int[REASONABLE_DEPTH];
    }

    //
    // PUBLIC METHODS
    //

    public void parse(final InputStream inputStream) throws IOException {
        parse(new DataInputStream(inputStream));
    }

    //
    // STATIC METHODS
    //

    /**
     * Returns a new parser builder that has no visitors added to it.
     *
     * @return Empty builder
     */
    public static ClassParserBuilder builder() {
        return new ClassParserBuilder();
    }

    //
    // INTERNALS
    //

    private static final int REASONABLE_DEPTH = 8;

    private void parse(final DataInputStream inputStream) throws IOException {
        init(inputStream);
        before();
        magic();
        version();
        constantPool();
        classAccessFlags();
        thisClass();
        superClass();
        interfaces();
        fields();
        methods();
        classFileAttributes();
        after();
    }

    private void init(final DataInputStream inputStream) {
        this.inputStream = inputStream;
        inputStreamPos = 0;
        arrayPosition = -1;
        constantPoolMetadata = null;
    }

    private void before() {
        for (final Visitor visitor : allVisitors) {
            visitor.before();
        }
    }

    private void after() {
        for (final Visitor visitor : allVisitors) {
            visitor.after();
        }
    }

    private void pushArrayContext() {
        ++arrayPosition;
    }

    private void popArrayContext() {
        --arrayPosition;
    }

    private void setArrayIndex(final int index) {
        try {
            arrayContext[arrayPosition] = index;
        } catch (final ArrayIndexOutOfBoundsException e) {
            final int expected = 1 + arrayContext.length;
            if (arrayPosition == expected) {
                arrayContext = copyOf(arrayContext, 2 * expected);
            } else {
                throw e; // This is a logic error: better to propagate it out.
            }
        }
    }

    private void magic() throws IOException {
        final int magic = readS4("magic");
        if (Magic.CLASS_FILE_MAGIC_NUMBER != magic) {
            throw classFormatException(String.format("Invalid magic number: read 0x%08x but expected 0x%08x",
                    magic, Magic.CLASS_FILE_MAGIC_NUMBER), "magic", null);
        }
    }

    private void version() throws IOException {
        final int minorVersion = readU2("minor_version");
        final int majorVersion = readU2("major_version");
        versionVisitor.visit(majorVersion, minorVersion);
    }

    private void constantPool() throws IOException {
        final int n = constantPoolCount();
        constantPoolMetadata = new ConstantPoolMetadata(n);
        pushArrayContext();
        for (int i = 1; i < n; i += constantPoolEntry(i)) /* NO-OP */;
        popArrayContext();
    }

    private int constantPoolCount() throws IOException {
        final int constantPoolCount = readU2("constant_pool_count");
        validateConstantPoolCount(constantPoolCount);
        constantPoolCountVisitor.visit(constantPoolCount);
        return constantPoolCount;
    }

    private void classAccessFlags() throws IOException {
        final EnumSet<ClassAccessFlag> set = readFlags(ClassAccessFlag.class, ClassAccessFlag.rules(), "access_flags");
        classAccessFlagsVisitor.visit(set);
        isInterface = set.contains(ClassAccessFlag.INTERFACE);
    }

    private void thisClass() throws IOException {
        final int thisClassIndex = readConstantPoolIndex("this_class");
        thisClassVisitor.visit(thisClassIndex);
    }

    private void superClass() throws IOException {
        final int superClassIndex = readConstantPoolIndex("super_class");
        superClassVisitor.visit(superClassIndex);
    }

    private void interfaces() throws IOException {
        // Unlike the constant pool table, which uses 1-based indexing and adds
        // one to the actual count, the interfaces table uses "standard"
        // zero-based indexing, so there's no special validation to perform.
        final int n = interfacesCount();
        for (int i = 0; i < n; ++i) {
            final int constantPoolClassIndex = readConstantPoolIndexFromArray("interfaces", i);
            interfaceVisitor.visit(constantPoolClassIndex);
        }
    }

    private int interfacesCount() throws IOException {
        final int interfacesCount = readU2("interfaces_count");
        interfacesCountVisitor.visit(interfacesCount);
        return interfacesCount;
    }

    private void fields() throws IOException {
        final int n = fieldsCount();
        pushArrayContext();
        for (int i = 0; i < n; ++i) {
            setArrayIndex(i);
            field();
        }
        popArrayContext();
    }

    private int fieldsCount() throws IOException {
        final int fieldsCount = readU2("fields_count");
        fieldsCountVisitor.visit(fieldsCount);
        return fieldsCount;
    }

    private void methods() throws IOException {
        final int n = methodsCount();
        pushArrayContext();
        for (int i = 0; i < n; ++i) {
            setArrayIndex(i);
            method();
        }
        popArrayContext();
    }

    private int methodsCount() throws IOException {
        final int methodsCount = readU2("methods_count");
        methodsCountVisitor.visit(methodsCount);
        return methodsCount;
    }

    private void classFileAttributes() throws IOException {
        final int n = classFileAttributesCount();
        pushArrayContext();
        for (int i = 0; i < n; ++i) {
            setArrayIndex(i);
            final Attribute attribute = attribute(CLASS_FILE);
            attributeVisitor.visit(attribute);
        }
        popArrayContext();
    }

    private int classFileAttributesCount() throws IOException {
        final int attributesCount = readU2("attributes_count");
        attributesCountVisitor.visit(attributesCount);
        return attributesCount;
    }

    @ArrayContext("constant_pool")
    private int constantPoolEntry(final int entryIndex) throws IOException {
        setArrayIndex(entryIndex);
        final int tag = readU1("tag");
        // In the table below, keep tags organized by the order in which they
        // appear in Chapter 4 ("The class File Format") of the JVM spec.
        switch (tag) {
        case  7: return constantPoolClassEntry(entryIndex);
        case  9: return constantPoolFieldReferenceEntry(entryIndex);
        case 10: return constantPoolMethodReferenceEntry(entryIndex);
        case 11: return constantPoolInterfaceMethodReferenceEntry(entryIndex);
        case  8: return constantPoolStringEntry(entryIndex);
        case  3: return constantPoolIntegerEntry(entryIndex);
        case  4: return constantPoolFloatEntry(entryIndex);
        case  5: return constantPoolLongEntry(entryIndex);
        case  6: return constantPoolDoubleEntry(entryIndex);
        case 12: return constantPoolNameAndTypeEntry(entryIndex);
        case  1: return constantPoolUtf8Entry(entryIndex);
        case 15: return constantPoolMethodHandleEntry(entryIndex);
        case 16: return constantPoolMethodTypeEntry(entryIndex);
        case 17: return constantPoolInvokeDynamicEntry(entryIndex);
        case 19: return constantPoolModuleEntry(entryIndex);
        case 20: return constantPoolPackageEntry(entryIndex);
        default:
            throw invalidConstantPoolTag(tag);
        }
    }

    @ConstantPoolTagContext(CLASS)
    private int constantPoolClassEntry(final int entryIndex) throws IOException {
        return constantPoolUtf8ReferenceEntry(CLASS, entryIndex, "name_index");
    }

    @ConstantPoolTagContext(FIELD_REFERENCE)
    private int constantPoolFieldReferenceEntry(final int entryIndex) throws IOException {
        return constantPoolMemberReferenceEntry(FIELD_REFERENCE, entryIndex);
    }

    @ConstantPoolTagContext(METHOD_REFERENCE)
    private int constantPoolMethodReferenceEntry(final int entryIndex) throws IOException {
        return constantPoolMemberReferenceEntry(METHOD_REFERENCE, entryIndex);
    }

    @ConstantPoolTagContext(INTERFACE_METHOD_REFERENCE)
    private int constantPoolInterfaceMethodReferenceEntry(final int entryIndex) throws IOException {
        return constantPoolMemberReferenceEntry(INTERFACE_METHOD_REFERENCE, entryIndex);
    }

    @ConstantPoolTagContext(STRING)
    private int constantPoolStringEntry(final int entryIndex) throws IOException {
        return constantPoolUtf8ReferenceEntry(STRING, entryIndex, "string");
    }

    @ConstantPoolTagContext(INTEGER)
    private int constantPoolIntegerEntry(final int entryIndex) throws IOException {
        final int value = readS4("bytes");
        final IntegerEntry entry = new IntegerEntry(value);
        constantPoolEntryVisitor.visit(entry);
        constantPoolMetadata.put(entryIndex, INTEGER);
        return 1;
    }

    @ConstantPoolTagContext(FLOAT)
    private int constantPoolFloatEntry(final int entryIndex) throws IOException {
        final float value;
        try {
            value = inputStream.readFloat();
        } catch (EOFException e) {
            throw prematureEndOfStream("bytes", e);
        }
        inputStreamPos += 4;
        final FloatEntry entry = new FloatEntry(value);
        constantPoolEntryVisitor.visit(entry);
        constantPoolMetadata.put(entryIndex, FLOAT);
        return 1;
    }

    @ConstantPoolTagContext(LONG)
    private int constantPoolLongEntry(final int entryIndex) throws IOException {
        final long value;
        try {
            value = inputStream.readLong();
        } catch (EOFException e) {
            throw prematureEndOfStream("bytes", e);
        }
        inputStreamPos += 8;
        final LongEntry entry = new LongEntry(value);
        constantPoolEntryVisitor.visit(entry);
        constantPoolMetadata.put(entryIndex, LONG);
        return 2;
    }

    @ConstantPoolTagContext(DOUBLE)
    private int constantPoolDoubleEntry(final int entryIndex) throws IOException {
        final double value;
        try {
            value = inputStream.readDouble();
        } catch (EOFException e) {
            throw prematureEndOfStream("bytes", e);
        }
        inputStreamPos += 8;
        final DoubleEntry entry = new DoubleEntry(value);
        constantPoolEntryVisitor.visit(entry);
        constantPoolMetadata.put(entryIndex, DOUBLE);
        return 2;
    }

    @ConstantPoolTagContext(NAME_AND_TYPE)
    private int constantPoolNameAndTypeEntry(final int entryIndex) throws IOException {
        final int nameIndex = readConstantPoolIndex("name");
        final int descriptorIndex = readConstantPoolIndex("descriptor");
        final NameAndTypeEntry entry = new NameAndTypeEntry(nameIndex, descriptorIndex);
        constantPoolEntryVisitor.visit(entry);
        constantPoolMetadata.put(entryIndex, NAME_AND_TYPE);
        return 1;
    }

    @ConstantPoolTagContext(UTF8)
    private int constantPoolUtf8Entry(final int entryIndex) throws IOException {
        final String value;
        try {
            // TODO: Need to update stream pos here.
            value = inputStream.readUTF();
        } catch (UTFDataFormatException e) {
            throw classFormatException("Badly-formatted modified UTF8 value", "bytes", e);
        } catch (EOFException e) {
            throw prematureEndOfStream("bytes", e);
        }
        final Utf8Entry entry = new Utf8Entry(value);
        constantPoolEntryVisitor.visit(entry);
        constantPoolMetadata.put(entryIndex, value);
        return 1;
    }

    @ConstantPoolTagContext(METHOD_HANDLE)
    private int constantPoolMethodHandleEntry(final int entryIndex) throws IOException {
        final int referenceKindByte = readU1("reference_kind");
        final MethodHandleReferenceKind referenceKind;
        switch (referenceKindByte) {
        case 1: referenceKind = GET_FIELD; break;
        case 2: referenceKind = GET_STATIC; break;
        case 3: referenceKind = PUT_FIELD; break;
        case 4: referenceKind = PUT_STATIC; break;
        case 5: referenceKind = INVOKE_VIRTUAL; break;
        case 6: referenceKind = INVOKE_STATIC; break;
        case 7: referenceKind = INVOKE_SPECIAL; break;
        case 8: referenceKind = NEW_INVOKE_SPECIAL; break;
        case 9: referenceKind = INVOKE_INTERFACE; break;
        default:
            throw invalidMethodHandleReferenceKind(referenceKindByte);
        }
        final int referenceIndex = readConstantPoolIndex("reference");
        final MethodHandleEntry entry = new MethodHandleEntry(referenceKind, referenceIndex);
        constantPoolEntryVisitor.visit(entry);
        constantPoolMetadata.put(entryIndex, METHOD_HANDLE);
        return 1;
    }

    @ConstantPoolTagContext(METHOD_TYPE)
    private int constantPoolMethodTypeEntry(final int entryIndex) throws IOException {
        return constantPoolUtf8ReferenceEntry(METHOD_TYPE, entryIndex, "descriptor");
    }

    @ConstantPoolTagContext(INVOKE_DYNAMIC)
    private int constantPoolInvokeDynamicEntry(final int entryIndex) throws IOException {
        final int bootstrapMethodAttributeIndex = readU2("bootstrap_method_attr_index");
        // TODO: is bootstrap_method_attr_index one-based like constant pool indices? If so should validate.
        // TODO: regardless note that can't validate anything else about it b/c we aren't at the attributes yet.
        final int nameAndTypeIndex = readConstantPoolIndex("name_and_type");
        final InvokeDynamicEntry entry = new InvokeDynamicEntry(bootstrapMethodAttributeIndex, nameAndTypeIndex);
        constantPoolEntryVisitor.visit(entry);
        constantPoolMetadata.put(entryIndex, INVOKE_DYNAMIC);
        return 1;
    }

    @ConstantPoolTagContext(MODULE)
    private int constantPoolModuleEntry(final int entryIndex) throws IOException {
        return constantPoolUtf8ReferenceEntry(MODULE, entryIndex, "name");
    }

    @ConstantPoolTagContext(PACKAGE)
    private int constantPoolPackageEntry(final int entryIndex) throws IOException {
        return constantPoolUtf8ReferenceEntry(PACKAGE, entryIndex, "name");
    }

    private int constantPoolUtf8ReferenceEntry(final ConstantPoolTag tag, final int entryIndex, final String utf8EntryIndexFieldName) throws IOException {
        final int utf8EntryIndex = readConstantPoolIndex(utf8EntryIndexFieldName);
        final Utf8ReferenceEntry entry = new Utf8ReferenceEntry(tag, utf8EntryIndex);
        constantPoolEntryVisitor.visit(entry);
        constantPoolMetadata.put(entryIndex, tag);
        return 1;
    }

    private int constantPoolMemberReferenceEntry(final ConstantPoolTag tag, final int entryIndex) throws IOException {
        final int classIndex = readConstantPoolIndex("class");
        final int nameAndTypeIndex = readConstantPoolIndex("name_and_type");
        final MemberReferenceEntry entry = new MemberReferenceEntry(tag, classIndex, nameAndTypeIndex);
        constantPoolEntryVisitor.visit(entry);
        constantPoolMetadata.put(entryIndex, tag);
        return 1;
    }

    private ClassFormatException invalidConstantPoolTag(final int tag) {
        return nonEnumeratedValue(tag, ConstantPoolTag.class, "tag");
    }

    private ClassFormatException invalidMethodHandleReferenceKind(final int referenceKind) {
        return nonEnumeratedValue(referenceKind, MethodHandleReferenceKind.class, "reference_kind");
    }

    private <E extends Enum<E> & Valued> ClassFormatException nonEnumeratedValue(final int value, final Class<E> clazz,
        final String fieldName) {
        final String validValues = stream(clazz.getEnumConstants())
                .mapToInt(E::getValue)
                .mapToObj(Integer::toString)
                .collect(joiningToCommaDelimitedSet());
        return classFormatException(format("Invalid value for %s: %d - valid values are: %s",
                fieldName, value, validValues), fieldName);
    }

    @ArrayContext("fields")
    private void field() throws IOException {
        final EnumSet<FieldAccessFlag> fieldAccessFlags = fieldAccessFlags();
        final int nameIndex = readConstantPoolIndex("name_index");
        final int descriptorIndex = readConstantPoolIndex("descriptor_index");
        final Attribute[] attributes = attributes(FIELD_INFO);
        final Member<FieldAccessFlag> field = new Member<>(fieldAccessFlags, nameIndex, descriptorIndex, attributes);
        fieldVisitor.visit(field);
    }

    private EnumSet<FieldAccessFlag> fieldAccessFlags() throws IOException {
        if (!isInterface) {
            return readFlags(FieldAccessFlag.class, classFieldRules(), "access_flags");
        } else {
            return readFlags(FieldAccessFlag.class, interfaceFieldRules(), "access_flags");
        }
    }

    @ArrayContext("methods")
    private void method() throws IOException {
        final EnumSet<MethodAccessFlag> methodAccessFlags = methodAccessFlags();
        final int nameIndex = readConstantPoolIndex("name_index");
        final int descriptorIndex = readConstantPoolIndex("descriptor_index");
        final Attribute[] attributes = attributes(METHOD_INFO);
        final Member<MethodAccessFlag> method = new Member<>(methodAccessFlags, nameIndex, descriptorIndex, attributes);
        methodVisitor.visit(method);
    }

    private EnumSet<MethodAccessFlag> methodAccessFlags() throws IOException {
        if (!isInterface) {
            return readFlags(MethodAccessFlag.class, classMethodBaseRules(), "access_flags");
        } else {
            return readFlags(MethodAccessFlag.class, interfaceMethodBaseRules(), "access_flags");
        }
    }

    private Attribute[] attributes(final int attributeContext) throws IOException {
        final int n = readU2("attributes_count");
        final Attribute[] attributes = new Attribute[n];
        pushArrayContext();
        for (int i = 0; i < n; ++i) {
            setArrayIndex(i);
            attributes[i] = attribute(attributeContext);
        }
        popArrayContext();
        return attributes;
    }

    @ArrayContext("attributes")
    private Attribute attribute(final int attributeContext) throws IOException {
        final int nameIndex = readConstantPoolIndex("attribute_name_index");
        final AttributeType type;
        try {
            type = constantPoolMetadata.getAttributeType(nameIndex, attributeContext);
        } catch (final InvalidConstantPoolIndexException e) {
            throw invalidConstantPoolIndex(nameIndex, "attribute_name_index", e);
        }
        final int length = readU4("attribute_length");
        switch (type) {
        case ANNOTATION_DEFAULT: return attributeTypeAnnotationDefault(length);
        case BOOTSTRAP_METHODS: return attributeTypeBootstrapMethods(length);
        case CODE: return attributeTypeCode(length);
        case CONSTANT_VALUE: return attributeTypeConstantValue(length);
        case DEPRECATED: return attributeTypeDeprecated(length);
        case ENCLOSING_METHOD: return attributeTypeEnclosingMethod(length);
        case EXCEPTIONS: return attributeTypeExceptions(length);
        case INNER_CLASSES: return attributeTypeInnerClasses(length);
        case LINE_NUMBER_TABLE: return lineNumberTable(length);
        case LOCAL_VARIABLE_TABLE: return attributeTypeLocalVariableTable(length);
        case LOCAL_VARIABLE_TYPE_TABLE: return attributeTypeLocalVariableTypeTable(length);
        case METHOD_PARAMETERS: return attributeTypeMethodParameters(length);
        case MODULE: return attributeTypeModule(length);
        case MODULE_MAIN_CLASS: return attributeTypeModuleMainClass(length);
        case MODULE_PACKAGES: return attributeTypeModulePackages(length);
        case RUNTIME_INVISIBLE_ANNOTATIONS: return attributeTypeRuntimeInvisibleAnnotations(length);
        case RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS: return attributeTypeRuntimeInvisibleParameterAnnotations(length);
        case RUNTIME_INVISIBLE_TYPE_ANNOTATIONS:  return attributeTypeRuntimeInvisibleTypeAnnotations(length);
        case RUNTIME_VISIBLE_ANNOTATIONS: return attributeTypeRuntimeVisibleAnnotations(length);
        case RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS: return attributeTypeRuntimeVisibleParameterAnnotations(length);
        case RUNTIME_VISIBLE_TYPE_ANNOTATIONS: return attributeTypeRuntimeVisibleTypeAnnotations(length);
        case SIGNATURE: return attributeTypeSignature(length);
        case SOURCE_DEBUG_EXTENSION: return attributeTypeSourceDebugExtension(nameIndex, length);
        case SOURCE_FILE: return attributeTypeSourceFile(length);
        case STACK_MAP_TABLE: return attributeTypeStackMapTable(length);
        case SYNTHETIC: return attributeTypeSynthetic(length);
        case UNKNOWN: return unknownAttribute(nameIndex, length);
        default: throw unhandledEnumerator(type);
        }
    }

    @AttributeTypeContext(ANNOTATION_DEFAULT)
    @ContextSymbol(name = "elementValueFieldName", value = "default_value")
    private Attribute attributeTypeAnnotationDefault(final int expectedAttributeLength) throws IOException {
        final int[] actualAttributeLength = new int[1];
        final ElementValue defaultValue = elementValue(actualAttributeLength);
        if (expectedAttributeLength == actualAttributeLength[0]) {
            return new AnnotationDefaultAttribute(defaultValue);
        } else {
            throw attributeLengthMismatch(expectedAttributeLength, actualAttributeLength[0]);
        }
    }

    @AttributeTypeContext(BOOTSTRAP_METHODS)
    private Attribute attributeTypeBootstrapMethods(final int expectedAttributeLength) throws IOException {
        final int n = readU2("num_bootstrap_methods");
        final int[] actualAttributeLength = { 2 };
        final BootstrapMethod[] bootstrapMethods = new BootstrapMethod[n];
        pushArrayContext();
        for (int i = 0; i < n; ++i) {
            setArrayIndex(i);
            bootstrapMethods[i] = bootstrapMethod(actualAttributeLength);
        }
        popArrayContext();
        if (expectedAttributeLength == actualAttributeLength[0]) {
            return new ListAttribute<>(BOOTSTRAP_METHODS, bootstrapMethods);
        } else {
            throw attributeLengthMismatch(expectedAttributeLength, actualAttributeLength[0]);
        }
    }

    @AttributeTypeContext(CODE)
    private Attribute attributeTypeCode(final int expectedAttributeLength) throws IOException {
        final int maxStack = readU2("max_stack");
        final int maxLocals = readU2("max_locals");
        final int codeLength = readU4("code_length");
        final byte[] code = readBytes(codeLength, "code");
        final int[] actualAttributeLength = { 8 + code.length };
        validateByteCode(code, maxLocals);
        final ExceptionTableEntry[] exceptionTable = exceptionTable(actualAttributeLength);
        final Attribute[] attributes = attributes(AttributeContext.CODE);
        if (expectedAttributeLength == actualAttributeLength[0]) {
            return new CodeAttribute(maxStack, maxLocals, code, exceptionTable,
                    attributes);
        } else {
            throw attributeLengthMismatch(expectedAttributeLength, actualAttributeLength[0]);
        }
    }

    @AttributeTypeContext(CONSTANT_VALUE)
    private Attribute attributeTypeConstantValue(final int attributeLength) throws IOException {
        return constantPoolIndexAttribute(CONSTANT_VALUE, attributeLength, "constantvalue_index");
    }

    @AttributeTypeContext(DEPRECATED)
    private Attribute attributeTypeDeprecated(final int attributeLength) {
        return emptyAttribute(DEPRECATED, attributeLength);
    }

    @AttributeTypeContext(ENCLOSING_METHOD)
    private Attribute attributeTypeEnclosingMethod(final int attributeLength) throws IOException {
        if (4 == attributeLength) {
            final int classIndex = readConstantPoolIndex("class_index");
            final int methodIndex = readConstantPoolIndexOrZero("method_index");
            return new EnclosingMethodAttribute(classIndex, methodIndex);
        } else {
            throw attributeLengthMismatch(attributeLength, 4);
        }
    }

    @AttributeTypeContext(EXCEPTIONS)
    private Attribute attributeTypeExceptions(final int attributeLength) throws IOException {
        return constantPoolIndicesAttribute(EXCEPTIONS, attributeLength,
                "number_of_exceptions", "exception_index_table");
    }

    @AttributeTypeContext(INNER_CLASSES)
    private Attribute attributeTypeInnerClasses(final int attributeLength) throws IOException {
        final int n = readU2FixedSizeAttributeArrayLength(attributeLength, 2, 8, "number_of_classes");
        final InnerClass[] classes = new InnerClass[n];
        pushArrayContext();
        for (int i = 0; i < n; ++i) {
            setArrayIndex(i);
            classes[i] = innerClass();
        }
        popArrayContext();
        return new ListAttribute<>(INNER_CLASSES, classes);
    }

    @AttributeTypeContext(LINE_NUMBER_TABLE)
    private Attribute lineNumberTable(final int attributeLength) throws IOException {
        final int n = readU2FixedSizeAttributeArrayLength(attributeLength,2, 4, "line_number_table_length");
        final LineNumberTableEntry[] lineNumberTableEntries = new LineNumberTableEntry[n];
        pushArrayContext();
        for (int i = 0; i < n; ++i) {
            setArrayIndex(i);
            lineNumberTableEntries[i] = lineNumberTableEntry();
        }
        popArrayContext();
        return new ListAttribute<>(LINE_NUMBER_TABLE, lineNumberTableEntries);
    }

    @AttributeTypeContext(LOCAL_VARIABLE_TABLE)
    @ContextSymbol(name = "localVariableTableArrayFieldName", value = "local_variable_table")
    @ContextSymbol(name = "localVariableTableDescriptorOrSignature", value = "descriptor")
    private Attribute attributeTypeLocalVariableTable(final int attributeLength) throws IOException {
        return localVariableTableEntryArrayAttribute(LOCAL_VARIABLE_TABLE, attributeLength,"local_variable_table_length");
    }

    @AttributeTypeContext(LOCAL_VARIABLE_TYPE_TABLE)
    @ContextSymbol(name = "localVariableTableArrayFieldName", value = "local_variable_type_table")
    @ContextSymbol(name = "localVariableTableDescriptorOrSignature", value = "attributeTypeSignature")
    private Attribute attributeTypeLocalVariableTypeTable(final int attributeLength) throws IOException {
        return localVariableTableEntryArrayAttribute(LOCAL_VARIABLE_TYPE_TABLE, attributeLength,"local_variable_type_table_length");
    }

    @AttributeTypeContext(METHOD_PARAMETERS)
    private Attribute attributeTypeMethodParameters(final int attributeLength) throws IOException {
        final int n = readU1FixedSizeAttributeArrayLength(attributeLength, 1, 4, "parameters_count");
        final MethodParameter[] parameters = new MethodParameter[n];
        pushArrayContext();
        for (int i = 0; i < n; ++i) {
            setArrayIndex(i);
            parameters[i] = methodParameter();
        }
        popArrayContext();
        return new ListAttribute<>(METHOD_PARAMETERS, parameters);
    }

    @AttributeTypeContext(AttributeType.MODULE)
    private Attribute attributeTypeModule(final int expectedAttributeLength) throws IOException {
        final int moduleNameIndex = readConstantPoolIndex("module_name_index");
        final EnumSet<ModuleFlag> moduleFlags = moduleFlags();
        final int moduleVersionIndex = readConstantPoolIndex("module_version_index");
        final int[] actualAttributeLength = { 6 };
        final ModuleReference[] requires = moduleRequires(actualAttributeLength);
        final PackageReference[] exports = moduleExports(actualAttributeLength);
        final PackageReference[] opens = moduleOpens(actualAttributeLength);
        final int[] uses = moduleUses(actualAttributeLength);
        final ModuleProvide[] provides = moduleProvides(actualAttributeLength);
        if (expectedAttributeLength == actualAttributeLength[0]) {
            return new ModuleAttribute(moduleNameIndex, moduleFlags, moduleVersionIndex, requires, exports, opens, uses, provides);
        } else {
            throw attributeLengthMismatch(expectedAttributeLength, actualAttributeLength[0]);
        }
    }

    @AttributeTypeContext(MODULE_MAIN_CLASS)
    private Attribute attributeTypeModuleMainClass(final int attributeLength) throws IOException {
        return constantPoolIndexAttribute(MODULE_MAIN_CLASS, attributeLength, "main_class");
    }

    @AttributeTypeContext(MODULE_PACKAGES)
    private Attribute attributeTypeModulePackages(final int attributeLength) throws IOException {
        return constantPoolIndicesAttribute(MODULE_PACKAGES, attributeLength, "package_count", "package_index");
    }

    @AttributeTypeContext(RUNTIME_INVISIBLE_ANNOTATIONS)
    private Attribute attributeTypeRuntimeInvisibleAnnotations(final int attributeLength) throws IOException {
        return runtimeAnnotationsAttribute(RUNTIME_INVISIBLE_ANNOTATIONS, attributeLength);
    }

    @AttributeTypeContext(RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS)
    private Attribute attributeTypeRuntimeInvisibleParameterAnnotations(final int attributeLength) throws IOException {
        return runtimeParameterAnnotationsAttribute(RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS, attributeLength);
    }

    @AttributeTypeContext(RUNTIME_INVISIBLE_TYPE_ANNOTATIONS)
    private Attribute attributeTypeRuntimeInvisibleTypeAnnotations(final int attributeLength) throws IOException {
        return runtimeTypeAnnotationsAttribute(RUNTIME_INVISIBLE_TYPE_ANNOTATIONS, attributeLength);
    }

    @AttributeTypeContext(RUNTIME_VISIBLE_ANNOTATIONS)
    private Attribute attributeTypeRuntimeVisibleAnnotations(final int attributeLength) throws IOException {
        return runtimeAnnotationsAttribute(RUNTIME_VISIBLE_ANNOTATIONS, attributeLength);
    }

    @AttributeTypeContext(RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS)
    private Attribute attributeTypeRuntimeVisibleParameterAnnotations(final int attributeLength) throws IOException {
        return runtimeParameterAnnotationsAttribute(RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS, attributeLength);
    }

    @AttributeTypeContext(RUNTIME_VISIBLE_TYPE_ANNOTATIONS)
    private Attribute attributeTypeRuntimeVisibleTypeAnnotations(final int attributeLength) throws IOException {
        return runtimeTypeAnnotationsAttribute(RUNTIME_VISIBLE_TYPE_ANNOTATIONS, attributeLength);
    }

    @AttributeTypeContext(SIGNATURE)
    private Attribute attributeTypeSignature(final int attributeLength) throws IOException {
        return constantPoolIndexAttribute(SIGNATURE, attributeLength, "attributeTypeSignature");
    }

    @AttributeTypeContext(SOURCE_DEBUG_EXTENSION)
    private Attribute attributeTypeSourceDebugExtension(final int attributeNameIndex, final int attributeLength) throws IOException {
        return rawAttribute(SOURCE_DEBUG_EXTENSION, attributeNameIndex,
                attributeLength, "debug_extension");
    }

    @AttributeTypeContext(SOURCE_FILE)
    private Attribute attributeTypeSourceFile(final int attributeLength) throws IOException {
        return constantPoolIndexAttribute(SOURCE_FILE, attributeLength, "sourcefile");
    }

    @AttributeTypeContext(STACK_MAP_TABLE)
    private Attribute attributeTypeStackMapTable(final int expectedAttributeLength) throws IOException {
        final int[] actualAttributeLength = { 2 };
        final int n = readU2("number_of_entries");
        final StackMapFrame[] stackMapTable = new StackMapFrame[n];
        pushArrayContext();
        for (int i = 0; i < n; ++i) {
            setArrayIndex(i);
            stackMapTable[i] = stackMapFrame(actualAttributeLength);
        }
        popArrayContext();
        if (expectedAttributeLength == actualAttributeLength[0]) {
            return new ListAttribute<>(STACK_MAP_TABLE, stackMapTable);
        } else {
            throw attributeLengthMismatch(expectedAttributeLength, actualAttributeLength[0]);
        }
    }

    @AttributeTypeContext(SYNTHETIC)
    private Attribute attributeTypeSynthetic(final int attributeLength) {
        return emptyAttribute(SYNTHETIC, attributeLength);
    }

    @AttributeTypeContext(UNKNOWN)
    private Attribute unknownAttribute(final int attributeNameIndex, final int attributeLength) throws IOException {
        return rawAttribute(UNKNOWN, attributeNameIndex, attributeLength, "info");
    }

    private Attribute constantPoolIndexAttribute(final AttributeType attributeType, final int attributeLength, final String indexFieldName) throws IOException {
        if (2 == attributeLength) {
            final int constantPoolIndex = readConstantPoolIndex(indexFieldName);
            return new ConstantPoolIndexAttribute(attributeType, constantPoolIndex);
        } else {
            throw attributeLengthMismatch(attributeLength, 2);
        }
    }

    private Attribute constantPoolIndicesAttribute(
            final AttributeType attributeType, final int attributeLength,
            final String countFieldName, final String arrayFieldName) throws IOException {
        final int n = readU2(countFieldName);
        final int ACTUAL_ATTRIBUTE_LENGTH = 2 + 2 * n;
        if (ACTUAL_ATTRIBUTE_LENGTH == attributeLength) {
            final int[] constantPoolIndices = readConstantPoolIndexArray(n, arrayFieldName);
            return new ConstantPoolIndicesAttribute(attributeType, constantPoolIndices);
        } else {
            throw attributeLengthMismatch(attributeLength, ACTUAL_ATTRIBUTE_LENGTH, format("since %s is %d", countFieldName, n));
        }
    }

    private Attribute emptyAttribute(final AttributeType attributeType, final int attributeLength) {
        if (0L == attributeLength) {
            return new Attribute(attributeType);
        } else {
            throw attributeLengthMismatch(attributeLength, 0,
                    format("since attribute '%s' is always empty", attributeType.getAttributeName()));
        }
    }

    private Attribute rawAttribute(final AttributeType attributeType,
            final int attributeNameIndex, final int attributeLength, final String fieldName) throws IOException {
        final byte[] info = readBytes(attributeLength, fieldName);
        inputStreamPos += attributeLength;
        return new RawAttribute(attributeType, attributeNameIndex, info);
    }

    private Attribute localVariableTableEntryArrayAttribute(
            final AttributeType attributeType, final int attributeLength, final String arrayLengthFieldName) throws IOException {
        final int n = readU2FixedSizeAttributeArrayLength(attributeLength,2, 10, arrayLengthFieldName);
        final LocalVariableTableEntry[] entries = new LocalVariableTableEntry[n];
        pushArrayContext();
        for (int i = 0; i < n; ++i) {
            setArrayIndex(i);
            entries[i] = localVariableTableEntry();
        }
        popArrayContext();
        return new ListAttribute<>(attributeType, entries);
    }

    @ArrayContext("${localVariableTableArrayFieldName}")
    private LocalVariableTableEntry localVariableTableEntry() throws IOException {
        final int startPC = readU2("start_pc");
        final int length = readU2("length");
        final int nameIndex = readConstantPoolIndex("name_index");
        final int descriptorOrSignatureIndex = readConstantPoolIndex("${localVariableTableDesciptorOrSignature}");
        final int index = readU2("index");
        return new LocalVariableTableEntry(startPC, length, nameIndex, descriptorOrSignatureIndex, index);
    }

    private Attribute runtimeAnnotationsAttribute(final AttributeType attributeType,
            final int expectedAttributeLength) throws IOException {
        // We can't immediately validate the attribute length field because
        // annotations have variable length. Use an array to collect the length
        // as the subroutines parse the variable components of the attribute.
        final int[] actualAttributeLength = { 2 };
        final Annotation[] annotations = annotations(actualAttributeLength);
        if (expectedAttributeLength == actualAttributeLength[0]) {
            return new ListAttribute<>(attributeType, annotations);
        } else {
            throw attributeLengthMismatch(expectedAttributeLength, actualAttributeLength[0]);
        }
    }

    private Attribute runtimeParameterAnnotationsAttribute(final AttributeType attributeType, final int expectedAttributeLength) throws IOException {
        // We can't immediately validate the attribute length field because
        // annotations have variable length. Use an array to collect the length
        // as the subroutines parse the variable components of the attribute.
        final int[] actualAttributeLength = { 1 };
        final int n = readU1("num_parameters");
        final ParameterAnnotations[] parametersAnnotations = parametersAnnotations(n, actualAttributeLength);
        if (expectedAttributeLength == actualAttributeLength[0]) {
            return new ListAttribute<>(attributeType, parametersAnnotations);
        } else {
            throw attributeLengthMismatch(expectedAttributeLength, actualAttributeLength[0]);
        }
    }

    private Attribute runtimeTypeAnnotationsAttribute(final AttributeType attributeType, final int expectedAttributeLength) throws IOException {
        // We can't immediately validate the attribute length field because
        // annotations have variable length. Use an array to collect the length
        // as the subroutines parse the variable components of the attribute.
        final int[] actualAttributeLength = { 2 };
        final int n = readU2("num_annotations");
        final TypeAnnotation[] typeAnnotations = typeAnnotations(n, actualAttributeLength);
        if (expectedAttributeLength == actualAttributeLength[0]) {
            return new ListAttribute<>(attributeType, typeAnnotations);
        } else {
            throw attributeLengthMismatch(expectedAttributeLength, actualAttributeLength[0]);
        }
    }

    @ArrayContext("bootstrap_methods")
    private BootstrapMethod bootstrapMethod(final int[] actualAttributeLength) throws IOException {
        final int bootstrapMethodRef = readConstantPoolIndex("boostrap_method_ref");
        final int[] bootstrapArguments = readConstantPoolIndexArray("num_bootstrap_arguments", "bootstrap_arguments");
        actualAttributeLength[0] += 2 + 2 + 2 * bootstrapArguments.length;
        return new BootstrapMethod(bootstrapMethodRef, bootstrapArguments);
    }

    private ExceptionTableEntry[] exceptionTable(final int[] actualAttributeLength) throws IOException {
        final int n = readU2("exception_table_length");
        final ExceptionTableEntry[] exceptionTable = new ExceptionTableEntry[n];
        pushArrayContext();
        for (int i = 0; i < n; ++i) {
            setArrayIndex(i);
            exceptionTable[i] = exceptionTableEntry();
        }
        popArrayContext();
        actualAttributeLength[0] += 2 + n * 8;
        return exceptionTable;
    }

    @ArrayContext("exception_table")
    private ExceptionTableEntry exceptionTableEntry() throws IOException {
        final int startPC = readU2("start_pc");
        final int endPC = readU2("end_pc");
        final int handlerPC = readU2("handler_pc");
        final int catchTypeIndex = readConstantPoolIndexOrZero("catch_type");
        return new ExceptionTableEntry(startPC, endPC, handlerPC, catchTypeIndex);
    }

    @ArrayContext("classes")
    private InnerClass innerClass() throws IOException {
        final int innerClassInfoIndex = readConstantPoolIndex("inner_class_info_index");
        final int outerClassInfoIndex = readConstantPoolIndexOrZero("outer_class_info_index");
        final int innerClassNameIndex = readConstantPoolIndexOrZero("inner_class_name_index");
        final EnumSet<InnerClassAccessFlag> accessFlags = innerClassAccessFlags();
        return new InnerClass(innerClassInfoIndex, outerClassInfoIndex, innerClassNameIndex, accessFlags);
    }

    private EnumSet<InnerClassAccessFlag> innerClassAccessFlags() throws IOException {
        return readFlags(InnerClassAccessFlag.class, emptyList(), "inner_class_access_flags");
        // TODO: should we have validation rules for this? spec doesn't give any
    }

    @ArrayContext("line_number_table")
    private LineNumberTableEntry lineNumberTableEntry() throws IOException {
        final int startPC = readU2("start_pc");
        final int lineNumber = readU2("line_number");
        return new LineNumberTableEntry(startPC, lineNumber);
    }

    @ArrayContext("parameters")
    private MethodParameter methodParameter() throws IOException {
        final int nameIndex = readConstantPoolIndex("name_index");
        final EnumSet<MethodParameterAccessFlag> accessFlags = methodParameterAccessFlags();
        return new MethodParameter(nameIndex, accessFlags);
    }

    private EnumSet<MethodParameterAccessFlag> methodParameterAccessFlags() throws IOException {
        return readFlags(MethodParameterAccessFlag.class, emptyList(), "access_flags");
    }

    private EnumSet<ModuleFlag> moduleFlags() throws IOException {
        return readFlags(ModuleFlag.class, emptyList(), "module_flags");
    }

    private ModuleReference[] moduleRequires(final int[] actualAttributeLength) throws IOException {
        final int n = readU2("requires_count");
        final ModuleReference[] requires = new ModuleReference[n];
        pushArrayContext();
        for (int i = 0; i < n; ++i) {
            setArrayIndex(i);
            requires[i] = moduleRequire();
        }
        popArrayContext();
        actualAttributeLength[0] += 6 * n;
        return requires;
    }

    @ArrayContext("requires")
    private ModuleReference moduleRequire() throws IOException {
        final int requiresIndex = readConstantPoolIndex("requires_index");
        final EnumSet<ModuleReferenceFlag> moduleReferenceFlags = readFlags(ModuleReferenceFlag.class, emptyList(), "requires_flags");
        final int requiresVersionIndex = readConstantPoolIndexOrZero("requires_version_index");
        return new ModuleReference(requiresIndex, moduleReferenceFlags, requiresVersionIndex);
    }

    @ContextSymbol(name = "packageReferenceType", value = "exports")
    private PackageReference[] moduleExports(final int[] actualAttributeLength) throws IOException {
        return packageReferences(actualAttributeLength);
    }

    @ContextSymbol(name = "packageReferenceType", value = "opens")
    private PackageReference[] moduleOpens(final int[] actualAttributeLength) throws IOException {
        return packageReferences(actualAttributeLength);
    }

    private PackageReference[] packageReferences(final int[] actualAttributeLength) throws IOException {
        final int n = readU2("${packageReferenceType}_count");
        final PackageReference[] packageReferences = new PackageReference[n];
        actualAttributeLength[0] += 2;
        pushArrayContext();
        for (int i = 0; i < n; ++i) {
            setArrayIndex(i);
            packageReferences[i] = packageReference(actualAttributeLength);
        }
        popArrayContext();
        return packageReferences;
    }

    @ArrayContext("${packageReferenceType}")
    private PackageReference packageReference(final int[] actualAttributeLength) throws IOException {
        final int packageInfoIndex = readConstantPoolIndex("${packageReferenceType}_index");
        final EnumSet<PackageReferenceFlag> packageReferenceFlags = packageReferenceFlags();
        final int[] moduleInfoIndex = readConstantPoolIndexArray("${packageReferenceType}_to_count", "${packageReferenceType}_to_index");
        actualAttributeLength[0] += 6 + 2 * moduleInfoIndex.length;
        return new PackageReference(packageInfoIndex, packageReferenceFlags, moduleInfoIndex);
    }

    private EnumSet<PackageReferenceFlag> packageReferenceFlags() throws IOException {
        return readFlags(PackageReferenceFlag.class, emptyList(), "${packageReferenceType}_flags");
    }

    private int[] moduleUses(final int[] actualAttributeLength) throws IOException {
        final int[] uses = readConstantPoolIndexArray("uses_count", "uses_index");
        actualAttributeLength[0] += 2 + 2 * uses.length;
        return uses;
    }

    private ModuleProvide[] moduleProvides(final int[] actualAttributeLength) throws IOException {
        final int n = readU2("provides_count");
        actualAttributeLength[0] += 2;
        final ModuleProvide[] moduleProvides = new ModuleProvide[n];
        pushArrayContext();
        for (int i = 0; i < n; ++i) {
            setArrayIndex(i);
            moduleProvides[i] = moduleProvide(actualAttributeLength);
        }
        popArrayContext();
        return moduleProvides;
    }

    @ArrayContext("provides")
    private ModuleProvide moduleProvide(final int[] actualAttributeLength) throws IOException  {
        final int providesIndex = readU2("provides_index");
        final int[] providesWithIndices = readConstantPoolIndexArray("provides_with_count", "provides_with_index");
        actualAttributeLength[0] += 2 + 2 * providesWithIndices.length;
        return new ModuleProvide(providesIndex, providesWithIndices);
    }

    @ArrayContext("entries")
    private StackMapFrame stackMapFrame(final int[] actualAttributeLength) throws IOException {
        final int frameTypeByte = readU1("frame_type");
        final FrameType frameType = FrameType.forUnsignedByte(frameTypeByte);
        ++actualAttributeLength[0];
        try {
            switch (frameType) {
            case SAME: return stackMapFrameTypeSame(frameTypeByte);
            case SAME_LOCALS_1_STACK_ITEM: return stackMapFrameTypeSameLocals1StackItem(frameTypeByte, actualAttributeLength);
            case SAME_LOCALS_1_STACK_ITEM_EXTENDED: return stackMapFrameTypeSameLocals1StackItemExtended(actualAttributeLength);
            case CHOP: return stackMapFrameTypeChop(frameTypeByte, actualAttributeLength);
            case SAME_FRAME_EXTENDED: return stackMapFrameTypeSameExtended(actualAttributeLength);
            case APPEND: return stackMapFrameTypeAppend(frameTypeByte, actualAttributeLength);
            case FULL_FRAME: return stackMapFrameTypeFullFrame(actualAttributeLength);
            default: throw unhandledEnumerator(frameType);
            }
        } catch (NullPointerException e) {
            if (null == frameType) {
                throw classFormatException(format("Invalid frame type byte: %d", frameTypeByte), "frame_type");
            } else {
                throw e;
            }
        }
    }

    private StackMapFrame stackMapFrameTypeSame(final int frameTypeByte) {
        return new StackMapFrame(frameTypeByte - SAME.getMinValue());
    }

    @FrameTypeContext(SAME_LOCALS_1_STACK_ITEM)
    private StackMapFrame stackMapFrameTypeSameLocals1StackItem(final int frameTypeByte, final int[] actualAttributeLength) throws IOException {
        final VerificationTypeInfo newStack = verificationTypeInfoSingleStack(actualAttributeLength);
        return new StackMapFrame(SAME_LOCALS_1_STACK_ITEM, frameTypeByte - SAME_LOCALS_1_STACK_ITEM.getMinValue(),
                newStack);
    }

    @FrameTypeContext(SAME_LOCALS_1_STACK_ITEM_EXTENDED)
    private StackMapFrame stackMapFrameTypeSameLocals1StackItemExtended(final int[] actualAttributeLength) throws IOException {
        final int offsetDelta = readOffsetDelta(actualAttributeLength);
        final VerificationTypeInfo newStack = verificationTypeInfoSingleStack(actualAttributeLength);
        return new StackMapFrame(SAME_LOCALS_1_STACK_ITEM_EXTENDED, offsetDelta, newStack);
    }

    @FrameTypeContext(CHOP)
    private StackMapFrame stackMapFrameTypeChop(final int frameTypeByte, final int[] actualAttributeLength) throws IOException {
        final int offsetDelta = readOffsetDelta(actualAttributeLength);
        return new StackMapFrame(offsetDelta, frameTypeByte - CHOP.getMinValue());
    }

    @FrameTypeContext(SAME_FRAME_EXTENDED)
    private StackMapFrame stackMapFrameTypeSameExtended(final int[] actualAttributeLength) throws IOException {
        final int offsetDelta = readOffsetDelta(actualAttributeLength);
        return new StackMapFrame(offsetDelta);
    }

    @FrameTypeContext(APPEND)
    private StackMapFrame stackMapFrameTypeAppend(final int frameTypeByte, final int[] actualAttributeLength) throws IOException {
        final int offsetDelta = readOffsetDelta(actualAttributeLength);
        final VerificationTypeInfo[] locals = verificationTypeInfoLocalsArray(frameTypeByte - APPEND.getMinValue(),
            actualAttributeLength);
        return new StackMapFrame(offsetDelta, locals);
    }

    @FrameTypeContext(FULL_FRAME)
    private StackMapFrame stackMapFrameTypeFullFrame(final int[] actualAttributeLength) throws IOException {
        final int offsetDelta = readOffsetDelta(actualAttributeLength);
        final int numLocals = readU2("number_of_locals");
        actualAttributeLength[0] += 2;
        final VerificationTypeInfo[] locals = verificationTypeInfoLocalsArray(numLocals, actualAttributeLength);
        final int numStackItems = readU2("number_of_stack_items");
        final VerificationTypeInfo[] stack = verificationTypeInfoStackArray(numStackItems, actualAttributeLength);
        return new StackMapFrame(offsetDelta, locals, stack);
    }

    @StructureContext("locals")
    private VerificationTypeInfo[] verificationTypeInfoLocalsArray(final int numLocals, final int[] actualAttributeLength) throws IOException {
        return verificationTypeInfoArray(numLocals, actualAttributeLength);
    }

    @StructureContext("stack")
    private VerificationTypeInfo[] verificationTypeInfoStackArray(final int numLocals, final int[] actualAttributeLength) throws IOException {
        return verificationTypeInfoArray(numLocals, actualAttributeLength);
    }

    private VerificationTypeInfo[] verificationTypeInfoArray(final int numLocals, final int[] actualAttributeLength) throws IOException {
        final VerificationTypeInfo[] verificationTypeInfoArray = new VerificationTypeInfo[numLocals];
        pushArrayContext();
        for (int i = 0; i < numLocals; ++i) {
            setArrayIndex(i);
            verificationTypeInfoArray[i] = verificationTypeInfoSingleArrayMember(actualAttributeLength);
        }
        popArrayContext();
        return verificationTypeInfoArray;
    }

    @ArrayContext
    private VerificationTypeInfo verificationTypeInfoSingleArrayMember(final int[] actualAttributeLength) throws IOException {
        return verificationTypeInfo(actualAttributeLength);
    }

    @StructureContext("stack")
    private VerificationTypeInfo verificationTypeInfoSingleStack(final int[] actualAttributeLength) throws IOException {
        return verificationTypeInfo(actualAttributeLength);
    }

    @StructureContext("verification_type_info")
    private VerificationTypeInfo verificationTypeInfo(final int[] actualAttributeLength) throws IOException {
        final int tag = readU1("tag");
        switch (tag) {
        case 0: return verificationTypeInfo(TOP, actualAttributeLength);
        case 1: return verificationTypeInfo(VerificationTypeInfoTag.INTEGER, actualAttributeLength);
        case 2: return verificationTypeInfo(VerificationTypeInfoTag.FLOAT, actualAttributeLength);
        case 5: return verificationTypeInfo(NULL, actualAttributeLength);
        case 6: return verificationTypeInfo(UNINITIALIZED_THIS, actualAttributeLength);
        case 7: return objectVariableInfo(actualAttributeLength);
        case 8: return uninitializedVariableInfo(actualAttributeLength);
        case 4: return verificationTypeInfo(VerificationTypeInfoTag.LONG, actualAttributeLength);
        case 3: return verificationTypeInfo(VerificationTypeInfoTag.DOUBLE, actualAttributeLength);
        default:
            throw nonEnumeratedValue(tag, VerificationTypeInfoTag.class, "tag");
        }
    }

    private VerificationTypeInfo verificationTypeInfo(final VerificationTypeInfoTag tag, final int[] actualAttributeLength) {
        ++actualAttributeLength[0];
        return new VerificationTypeInfo(tag);
    }

    @StructureContext("Object_variable_info")
    private VerificationTypeInfo objectVariableInfo(final int[] actualAttributeLength) throws IOException {
        final int constantPoolIndex = readConstantPoolIndex("cpool_index");
        actualAttributeLength[0] += 2;
        return new ObjectVariableInfo(constantPoolIndex);
    }

    @StructureContext("Uninitialized_variable_info")
    private VerificationTypeInfo uninitializedVariableInfo(final int[] actualAttributeLength) throws IOException {
        final int newInstructionOffset = readU2("offset");
        actualAttributeLength[0] += 2;
        return new UninitializedVariableInfo(newInstructionOffset);
    }

    private Annotation[] annotations(final int[] actualAttributeLength) throws IOException {
        final int n = readU2("num_annotations");
        return annotations(n, actualAttributeLength);
    }

    @ArrayContext("annotations")
    private Annotation[] annotations(final int numAnnotations,
            final int[] actualAttributeLength) throws IOException {
        final Annotation[] annotations = new Annotation[numAnnotations];
        pushArrayContext();
        for (int i = 0; i < numAnnotations; ++i) {
            setArrayIndex(i);
            annotations[i] = annotation(actualAttributeLength);
        }
        popArrayContext();
        return annotations;
    }

    private Annotation annotation(final int[] actualAttributeLength) throws IOException {
        return annotation(actualAttributeLength, Annotation::new);
    }

    private <T extends Annotation> T annotation(final int[] actualAttributeLength,
            BiFunction<Integer, ElementValuePair[], T> annotationFactory) throws IOException {
        final int typeIndex = readConstantPoolIndex("type_index");
        final int n = readU2("num_element_value_pairs");
        actualAttributeLength[0] += 4;
        final ElementValuePair[] elementValuePairs = new ElementValuePair[n];
        pushArrayContext();
        for (int i = 0; i < n; ++i) {
            elementValuePairs[i] = elementValuePair(actualAttributeLength);
        }
        popArrayContext();
        return annotationFactory.apply(typeIndex, elementValuePairs);
    }

    @ArrayContext("element_value_pairs")
    @ContextSymbol(name = "elementValueFieldName", value = "value")
    private ElementValuePair elementValuePair(final int[] actualAttributeLength) throws IOException {
        final int elementNameIndex = readConstantPoolIndex("element_name_index");
        actualAttributeLength[0] += 2;
        final ElementValue value = elementValue(actualAttributeLength);
        return new ElementValuePair(elementNameIndex, value);
    }

    @StructureContext("${elementValueFieldName}")
    private ElementValue elementValue(final int[] actualAttributeLength) throws IOException {
        // Because `element_value` can indirectly contain itself, this method
        // could produce a StackOverflowError on a sufficiently corrupt or
        // devious input file, although any reasonable class file should not do
        // it.
        final int tag = readU1("tag");
        switch (tag) {
        case 'B': return elementValueConstant(BYTE, actualAttributeLength);
        case 'C': return elementValueConstant(CHAR, actualAttributeLength);
        case 'D': return elementValueConstant(ElementValueTag.DOUBLE, actualAttributeLength);
        case 'F': return elementValueConstant(ElementValueTag.FLOAT, actualAttributeLength);
        case 'I': return elementValueConstant(INT, actualAttributeLength);
        case 'J': return elementValueConstant(ElementValueTag.LONG, actualAttributeLength);
        case 'S': return elementValueConstant(SHORT, actualAttributeLength);
        case 'Z': return elementValueConstant(BOOLEAN, actualAttributeLength);
        case 's': return elementValueConstant(ElementValueTag.STRING, actualAttributeLength);
        case 'e': return elementValueEnumConstant(actualAttributeLength);
        case 'c': return elementValueClass(actualAttributeLength);
        case '@': return elementValueAnnotation(actualAttributeLength);
        case '[': return elementValueArray(actualAttributeLength);
        default:
            throw invalidElementValueTag(tag);
        }
    }

    private ElementValue elementValueConstant(final ElementValueTag tag, final int[] actualAttributeLength) throws IOException {
        final int constantValueIndex = readConstantPoolIndex("const_value_index");
        actualAttributeLength[0] += 2;
        return new ConstantElementValue(tag, constantValueIndex);
    }

    @StructureContext("enum_const_value")
    private ElementValue elementValueEnumConstant(final int[] actualAttributeLength) throws IOException {
        final int typeNameIndex = readConstantPoolIndex("type_name_index");
        final int constantNameIndex = readConstantPoolIndex("const_name_index");
        actualAttributeLength[0] += 4;
        return new EnumConstantElementValue(typeNameIndex, constantNameIndex);
    }

    private ElementValue elementValueClass(final int[] actualAttributeLength) throws IOException {
        final int classInfoIndex = readConstantPoolIndex("class_info_index");
        actualAttributeLength[0] += 2;
        return new ClassElementValue(classInfoIndex);
    }

    @StructureContext("annotation_value")
    private ElementValue elementValueAnnotation(final int[] actualAttributeLength) throws IOException {
        final Annotation annotation = annotation(actualAttributeLength);
        return new AnnotationElementValue(annotation);
    }

    @StructureContext("array_value")
    @ContextSymbol(name = "elementValueFieldName", value = "value")
    private ElementValue elementValueArray(final int[] actualAttributeLength) throws IOException {
        final int n = readU2("num_values");
        actualAttributeLength[0] += 2;
        final ElementValue[] values = new ElementValue[n];
        pushArrayContext();
        for (int i = 0; i < n; ++i) {
            setArrayIndex(i);
            values[i] = elementValue(actualAttributeLength);
        }
        popArrayContext();
        return new ArrayElementValue(values);
    }

    private ClassFormatException invalidElementValueTag(final int tag) {
        return nonEnumeratedValue(tag, ElementValueTag.class, "tag");
    }

    @ArrayContext("parameter_annotations")
    private ParameterAnnotations[] parametersAnnotations(final int numParameters, final int[] actualAttributeLength) throws IOException {
        final ParameterAnnotations[] parametersAnnotations = new ParameterAnnotations[numParameters];
        pushArrayContext();
        for (int i = 0; i < numParameters; ++i) {
            setArrayIndex(i);
            final Annotation[] annotations = annotations(actualAttributeLength);
            parametersAnnotations[i] = new ParameterAnnotations(annotations);
        }
        popArrayContext();
        return parametersAnnotations;
    }

    @ArrayContext("annotations")
    private TypeAnnotation[] typeAnnotations(final int numAnnotations, final int[] actualAttributeLength) throws IOException {
        final TypeAnnotation[] typeAnnotations = new TypeAnnotation[numAnnotations];
        pushArrayContext();
        for (int i = 0; i < numAnnotations; ++i) {
            setArrayIndex(i);
            typeAnnotations[i] = typeAnnotation(actualAttributeLength);
        }
        popArrayContext();;
        return typeAnnotations;
    }

    private TypeAnnotation<?> typeAnnotation(final int[] actualAttributeLength) throws IOException {
        final int targetTypeByte = readU1("target_type");
        final TargetType targetType;
        Object targetInfo;
        switch (targetTypeByte) {
        case 0x00: targetType = TYPE_PARAMETER_DECLARATION_OF_GENERIC_CLASS_OR_INTERFACE;
                   targetInfo = typeParameterTarget(actualAttributeLength);
                   break;
        case 0x01: targetType = TYPE_PARAMETER_DECLARATION_OF_GENERIC_METHOD_OR_CONSTRUCTOR;
                   targetInfo = typeParameterTarget(actualAttributeLength);
                   break;
        case 0x10: targetType = TYPE_IN_EXTENDS_OR_IMPLEMENTS_CLAUSE;
                   targetInfo = superTypeTarget(actualAttributeLength);
                   break;
        case 0x11: targetType = TYPE_IN_BOUND_OF_GENERIC_CLASS_OR_INTERFACE_TYPE_DECLARATION;
                   targetInfo = typeParameterBoundTarget(actualAttributeLength);
                   break;
        case 0x12: targetType = TYPE_IN_BOUND_OF_GENERIC_METHOD_OR_CONSTRUCTOR_TYPE_DECLARATION;
                   targetInfo = typeParameterBoundTarget(actualAttributeLength);
                   break;
        case 0x13: targetType = TYPE_IN_FIELD_DECLARATION;
                   targetInfo = emptyTarget();
                   break;
        case 0x14: targetType = TYPE_OF_METHOD_RETURN_OR_NEWLY_CONSTRUCTED_OBJECT;
                   targetInfo = emptyTarget();
                   break; /* empty_target */
        case 0x15: targetType = RECEIVER_TYPE_OF_METHOD_OR_CONSTRUCTOR;
                   targetInfo = emptyTarget();
                   break;
        case 0x16: targetType = TYPE_IN_FORMAL_DECLARATION_OF_METHOD_CONSTRUCTOR_OR_LAMBDA;
                   targetInfo = formalParameterTarget(actualAttributeLength);
                   break;
        case 0x17: targetType = TYPE_IN_THROWS_CLAUSE;
                   targetInfo = throwsTarget(actualAttributeLength);
                   break;
        case 0x40: targetType = TYPE_IN_LOCAL_VARIABLE_DECLARATION;
                   targetInfo = localVarTarget(actualAttributeLength);
                   break;
        case 0x41: targetType = TYPE_IN_RESOURCE_VARIABLE_DECLARATION;
                   targetInfo = localVarTarget(actualAttributeLength);
                   break;
        case 0x42: targetType = TYPE_IN_EXCEPTION_PARAMETER_DECLARATION;
                   targetInfo = catchTarget(actualAttributeLength);
                   break;
        case 0x43: targetType = TYPE_IN_INSTANCEOF_EXPRESSION;
                   targetInfo = offsetTarget(actualAttributeLength);
                   break;
        case 0x44: targetType = TYPE_IN_NEW_EXPRESSION;
                   targetInfo = offsetTarget(actualAttributeLength);
                   break;
        case 0x45: targetType = TYPE_IN_CONSTRUCTOR_REFERENCE_EXPRESSION;
                   targetInfo = offsetTarget(actualAttributeLength);
                   break;
        case 0x46: targetType = TYPE_IN_METHOD_REFERENCE_EXPRESSION;
                   targetInfo = offsetTarget(actualAttributeLength);
                   break;
        case 0x47: targetType = TYPE_IN_CAST_EXPRESSION;
                   targetInfo = typeArgumentTarget(actualAttributeLength);
                   break;
        case 0x48: targetType = TYPE_ARGUMENT_FOR_GENERIC_CONSTRUCTOR_IN_CONSTRUCTOR_INVOCATION_EXPRESSION;
                   targetInfo = typeArgumentTarget(actualAttributeLength);
                   break;
        case 0x49: targetType = TYPE_ARGUMENT_FOR_GENERIC_METHOD_IN_METHOD_INVOCATION_EXPRESSION;
                   targetInfo = typeArgumentTarget(actualAttributeLength);
                   break;
        case 0x4a: targetType = TYPE_ARGUMENT_FOR_GENERIC_CONSTRUCTOR_IN_CONSTRUCTOR_REFERENCE_EXPRESSION;
                   targetInfo = typeArgumentTarget(actualAttributeLength);
                   break;
        case 0x4b: targetType = TYPE_ARGUMENT_FOR_GENERIC_METHOD_IN_METHOD_REFERENCE_EXPRESSION;
                   targetInfo = typeArgumentTarget(actualAttributeLength);
                   break;
        default:
                   throw nonEnumeratedValue(targetTypeByte, TargetType.class, "target_type");
        }
        final TypePathStep[] targetPathSteps = targetPath(actualAttributeLength);
        return annotation(actualAttributeLength,
                (typeIndex, elementValuePairs) -> new TypeAnnotation(typeIndex, elementValuePairs, targetType,
                        targetPathSteps, targetInfo));
    }

    @StructureContext("type_parameter_target")
    private Integer typeParameterTarget(final int[] actualAttributeLength) throws IOException {
        return singleU1Target(actualAttributeLength, "type_parameter_index");
    }

    @StructureContext("supertype_target")
    private Integer superTypeTarget(final int[] actualAttributeLength) throws IOException {
        return singleU2Target(actualAttributeLength, "supertype_index");
    }

    @StructureContext("type_parameter_bound_target")
    private IntPair typeParameterBoundTarget(final int[] actualAttributeLength) throws IOException {
        final int typeParameterIndex = readU1("type_parameter_index");
        final int boundIndex = readU1("bound_index");
        actualAttributeLength[0] += 2;
        return new IntPair(typeParameterIndex, boundIndex);
    }

    private static Void emptyTarget() {
        return null;
    }

    @StructureContext("formal_parameter_target")
    private Integer formalParameterTarget(final int[] actualAttributeLength) throws IOException {
        return singleU1Target(actualAttributeLength, "formal_parameter_index");
    }

    @StructureContext("throws_target")
    private Integer throwsTarget(final int[] actualAttributeLength) throws IOException {
        return singleU2Target(actualAttributeLength, "throws_type_index");
    }

    @StructureContext("localvar_target")
    private List<LocalVariableTargetTableEntry> localVarTarget(final int[] actualAttributeLength) throws IOException {
        final int n = readU2("table_length");
        final LocalVariableTargetTableEntry[] localVariableTargetTable = new LocalVariableTargetTableEntry[n];
        pushArrayContext();
        for (int i = 0; i < n; ++i) {
            setArrayIndex(i);
            localVariableTargetTable[i] = localVarTargetTableEntry();
        }
        popArrayContext();
        actualAttributeLength[0] += 2 + n * 6;
        return List.of(localVariableTargetTable);
    }

    @ArrayContext("table")
    private LocalVariableTargetTableEntry localVarTargetTableEntry() throws IOException {
        final int startPC = readU2("start_pc");
        final int length = readU2("length");
        final int index = readU2("index");
        return new LocalVariableTargetTableEntry(startPC, length, index);
    }

    @StructureContext("catch_target")
    private Integer catchTarget(final int[] actualAttributeLength) throws IOException {
        return singleU2Target(actualAttributeLength, "exception_table_index");
    }

    @StructureContext("offset_target")
    private Integer offsetTarget(final int[] actualAttributeLength) throws IOException {
        return singleU2Target(actualAttributeLength, "offset");
    }

    @StructureContext("type_argument_target")
    private IntPair typeArgumentTarget(final int[] actualAttributeLength) throws IOException {
        final int offset = readU2("offset");
        final int typeArgumentIndex = readU1(".type_argument_index");
        actualAttributeLength[0] += 3;
        return new IntPair(offset, typeArgumentIndex);
    }

    private Integer singleU1Target(final int[] actualAttributeLength, final String indexName) throws IOException {
        final int u1 = readU1(indexName);
        actualAttributeLength[0] += 1;
        return u1;
    }

    private Integer singleU2Target(final int[] actualAttributeLength, final String indexName) throws IOException {
        final int u2 = readU2(indexName);
        actualAttributeLength[0] += 2;
        return u2;
    }

    @StructureContext("target_path")
    private TypePathStep[] targetPath(final int[] actualAttributeLength) throws IOException {
        final int n = readU1("path_length");
        final TypePathStep[] targetPathSteps = new TypePathStep[n];
        pushArrayContext();
        for (int i = 0; i < n; ++i) {
            setArrayIndex(i);
            targetPathSteps[i] = targetPathStep();
        }
        popArrayContext();
        actualAttributeLength[0] += 1 + n * 2;
        return targetPathSteps;
    }

    @ArrayContext("path")
    private TypePathStep targetPathStep() throws IOException {
        final TypePathKind typePathKind = typePathKind();
        final int typeArgumentIndex = readU1("type_argument_index");
        return new TypePathStep(typePathKind, typeArgumentIndex);
    }

    private TypePathKind typePathKind() throws IOException {
        final int typePathKindByte = readU1("type_path_kind");
        switch (typePathKindByte) {
        case 0: return DEEPER_IN_ARRAY_TYPE;
        case 1: return DEEPER_IN_NESTED_TYPE;
        case 2: return ON_TYPE_ARGUMENT_WILDCARD_BOUND;
        case 3: return ON_TYPE_ARGUMENT;
        default: throw nonEnumeratedValue(typePathKindByte, TypePathKind.class, "type_path_kind");
        }
    }

    private int readU1FixedSizeAttributeArrayLength(final int attributeLength,
            final int fixedAttributeSize, final int arrayElementSize,
            final String arrayLengthFieldName) throws IOException {
        final int arrayLength = readU1(arrayLengthFieldName);
        return validateFixedSizeAttributeArrayLength(arrayLength, attributeLength, fixedAttributeSize,
                arrayElementSize, arrayLengthFieldName);
    }

    private int readU2FixedSizeAttributeArrayLength(final int attributeLength,
            final int fixedAttributeSize, final int arrayElementSize,
            final String arrayLengthFieldName) throws IOException {
        final int arrayLength = readU2(arrayLengthFieldName);
        return validateFixedSizeAttributeArrayLength(arrayLength, attributeLength, fixedAttributeSize,
                arrayElementSize, arrayLengthFieldName);
    }

    private int validateFixedSizeAttributeArrayLength(final int arrayLength, final int attributeLength, final int fixedAttributeSize,
            final int arrayElementSize, final String arrayLengthFieldName) {
        final int actualAttributeLength = fixedAttributeSize + arrayLength * arrayElementSize;
        if (attributeLength == actualAttributeLength) {
            return arrayLength;
        } else {
            throw attributeLengthMismatch(attributeLength, actualAttributeLength,
                    format("since %s is %d and %d + %d x %d = %d)", arrayLengthFieldName, arrayLength,
                            fixedAttributeSize, arrayLength, arrayElementSize, actualAttributeLength));
        }
    }

    private ClassFormatException attributeLengthMismatch(final int readAttributeLength,
        final int actualAttributeLength) {
        return attributeLengthMismatch(readAttributeLength, actualAttributeLength, "");
    }

    private ClassFormatException attributeLengthMismatch(final int readAttributeLength,
            final int actualAttributeLength, final String additionalContext) {
        String message = format("Invalid attribute length: actual structure is %d bytes, not %d as indicated",
                actualAttributeLength, readAttributeLength);
        if (0 < additionalContext.length()) {
            message += " (" + additionalContext + ')';
        }
        return classFormatException(message, "attribute_length");
    }

    private int readU1(final String fieldName) throws IOException {
        final int u1;
        try {
            u1 = inputStream.readUnsignedByte();
        } catch (EOFException e) {
            throw prematureEndOfStream(fieldName, e);
        }
        ++inputStreamPos;
        return u1;
    }

    private int readU2(final String fieldName) throws IOException {
        final int u2;
        try {
            u2 = inputStream.readUnsignedShort();
        } catch (EOFException e) {
            throw prematureEndOfStream(fieldName, e);
        }
        inputStreamPos += 2;
        return u2;
    }

    private int readS4(final String fieldName) throws IOException {
        final int s4;
        try {
            s4 = inputStream.readInt();
        } catch (EOFException e) {
            throw prematureEndOfStream(fieldName, e);
        }
        inputStreamPos += 4;
        return s4;
    }

    private int readU4(final String fieldName) throws IOException {
        final int signed = readS4(fieldName);
        // While technically several fields in the JVM specification, such as
        // `attribute_length` and `code_length` are listed as type 'u4', we only
        // support attribute size of 2^31 -1 or less (2GB) because Java arrays
        // are limited to Integer.MAX_VALUE indices. If this non-problem truly
        // becomes a problem, we could (a) cast to long; (b) bitwise AND it with
        // 0x00000000ffffffffL; and (c) store the raw bytes in an array of short
        // to get us back up to the (2^32 - 1) theoretical supported by a u4
        // value.
        if (0 <= signed) {
            return signed;
        } else {
            final long unsigned = (long)signed & 0x00000000ffffffffL;
            throw classFormatException(format("This parser only supports u4 values"
                            + "up to %d (0x%08x), but read value %d (0x%08x)",
                    Integer.MAX_VALUE, Integer.MAX_VALUE, unsigned, signed), fieldName);
        }
    }

    private byte[] readBytes(final int numBytes, final String fieldName) throws IOException {
        final byte[] bytes = new byte[numBytes];
        try {
            inputStream.readFully(bytes);
        } catch (EOFException e) {
            throw prematureEndOfStream(fieldName);
        }
        return bytes;
    }

    private int readConstantPoolIndex(final String fieldName) throws IOException {
        final int index = readU2(fieldName);
        return validateConstantPoolIndex(index, fieldName);
    }

    private int readConstantPoolIndexOrZero(final String fieldName) throws IOException {
        final int index = readU2(fieldName);
        if (0 != index) {
            return validateConstantPoolIndex(index, fieldName);
        } else {
            return 0;
        }
    }

    private int readConstantPoolIndexFromArray(final String arrayFieldName, final int arrayIndex) throws IOException {
        final int constantPoolIndex;
        try {
            constantPoolIndex = inputStream.readUnsignedShort();
        } catch (EOFException e) {
            throw prematureEndOfStream(formatArrayFieldAtIndex(arrayFieldName, arrayIndex), e);
        }
        inputStreamPos += 2;
        return validateConstantPoolIndex(constantPoolIndex, arrayFieldName, arrayIndex);
    }

    private int[] readConstantPoolIndexArray(final String arrayLengthFieldName, final String arrayFieldName) throws IOException {
        final int n = readU2(arrayLengthFieldName);
        return readConstantPoolIndexArray(n, arrayFieldName);
    }

    private int[] readConstantPoolIndexArray(final int arrayLength, final String arrayFieldName) throws IOException {
        final int[] array = new int[arrayLength];
        for (int i = 0; i < arrayLength; ++i) {
            array[i] = readConstantPoolIndexFromArray(arrayFieldName, i);
        }
        return array;
    }

    private <F extends Enum<F> & Flag> EnumSet<F> readFlags(final Class<F> clazz,
            final Collection<FlagMixRule<F>> rules, final String flagFieldName) throws IOException {
        final int bits = readU2(flagFieldName);
        final EnumSet<F> flags = Flag.parse(clazz, bits);
        validateFlags(flags, rules, flagFieldName);
        return flags;
    }

    private int readOffsetDelta(final int[] actualAttributeLength) throws IOException {
        final int offsetDelta = readU2("offset_delta");
        actualAttributeLength[0] += 2;
        return offsetDelta;
    }

    private <F extends Enum<F> & Flag> void validateFlags(final EnumSet<F> accessFlags,
            final Collection<FlagMixRule<F>> rules, final String flagFieldName) {
        try {
            rules.forEach(rule -> rule.validate(accessFlags));
        } catch (BadAccessFlagsMixException e) {
            throw classFormatException("Invalid flag mix", flagFieldName, e);
        }
    }

    private void validateConstantPoolCount(final int constantPoolCount) {
        if (constantPoolCount < 1) {
            throw classFormatException(format("constant_pool_count must be positive but is %d",
                    constantPoolCount), "constant_pool_count");
        }
    }

    private int validateConstantPoolIndex(final int refIndex, final String fieldName) {
        if (refIndex < 1) {
            throw invalidConstantPoolIndexLessThanOne(refIndex, fieldName);
        } else if (constantPoolMetadata.count() <= refIndex) {
            throw invalidConstantPoolIndexNotLessThanCount(refIndex, fieldName);
        } else {
            return refIndex;
        }
    }

    private int validateConstantPoolIndex(final int refIndex, final String arrayFieldName, final int arrayIndex) {
        if (refIndex < 1) {
            throw invalidConstantPoolIndexLessThanOne(refIndex, indexedArrayFieldName(arrayFieldName, arrayIndex));
        } else if (constantPoolMetadata.count() <= refIndex) {
            throw invalidConstantPoolIndexNotLessThanCount(refIndex, indexedArrayFieldName(arrayFieldName, arrayIndex));
        } else {
            return refIndex;
        }
    }

    private void validateByteCode(final byte[] bytecode, final int maxLocals)  {
        if (null == byteCodeValidator) {
            byteCodeValidator = new ByteCodeValidator();
        }
        try {
            byteCodeValidator.validate(bytecode, constantPoolMetadata.count(), maxLocals);
        } catch (ByteCodeFormatException e) {
            throw classFormatException("Bad bytecode", "code", e);
        }
    }

    private static String indexedArrayFieldName(final String arrayFieldName, final int arrayIndex) {
        return arrayFieldName + '[' + arrayIndex + ']';
    }

    private ClassFormatException classFormatException(final String message, final String fieldName) {
        return classFormatException(message, fieldName, null);
    }

    private ClassFormatException classFormatException(final String message, final String fieldName, final Throwable cause) {
        final String positionContext = traceContextPath(ClassParser.class, arrayContext, arrayPosition + 1, fieldName);
        final int position;
        if (cause instanceof PinpointFormatException) {
            position = ((PinpointFormatException)cause).getPosition();
        } else {
            position = inputStreamPos;
        }
        return new ClassFormatException(message, cause, position, positionContext);
    }

    private ClassFormatException prematureEndOfStream(final String fieldName) {
        return prematureEndOfStream(fieldName, null);
    }

    private ClassFormatException prematureEndOfStream(final String fieldName, final EOFException cause) {
        return classFormatException("Premature end of stream", fieldName, cause);
    }

    private ClassFormatException invalidConstantPoolIndex(final int constantPoolIndex, final String fieldName, final InvalidConstantPoolIndexException cause) {
        return classFormatException(format("Invalid constant pool index: %d", constantPoolIndex), fieldName, cause);
    }

    private ClassFormatException invalidConstantPoolIndexLessThanOne(final int constantPoolIndex, final String fieldName) {
        throw invalidConstantPoolIndex(constantPoolIndex, fieldName, lessThanOne(constantPoolIndex));
    }

    private ClassFormatException invalidConstantPoolIndexNotLessThanCount(final int constantPoolIndex, final String fieldName) {
        throw invalidConstantPoolIndex(constantPoolIndex, fieldName, notLessThanCount(constantPoolIndex, constantPoolMetadata.count()));
    }

    private static String formatArrayFieldAtIndex(final String arrayFieldName, final int arrayIndex) {
        return format("%s[%d]", arrayFieldName, arrayIndex);
    }

    private static Visitor[] toArray(final Visitor... visitors) {
        return visitors;
    }

    private static Collector<CharSequence, ?, String> joiningToCommaDelimitedSet() {
        return joining(", ", "{", "}");
    }
}