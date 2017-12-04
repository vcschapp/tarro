package io.tarro.parser.clazz.attribute;

import java.nio.ByteBuffer;
import java.util.List;

import static io.tarro.base.attribute.AttributeType.CODE;
import static java.util.Objects.requireNonNull;

/**
 * <p>
 *
 * </p>
 *
 * @author Victor Schappert
 * @since 20171130
 */
public final class CodeAttribute extends Attribute {

    //
    // DATA
    //

    private final int maxStack;
    private final int maxLocals;
    private final byte[] bytecode;
    private final List<ExceptionTableEntry> exceptionTable;
    private final List<Attribute> attributes;

    //
    // CONSTRUCTORS
    //

    public CodeAttribute(final int maxStack, final int maxLocals, final byte[] bytecode,
        final ExceptionTableEntry[] exceptionTable, final Attribute[] attributes) {
        super(CODE);
        this.maxStack = maxStack;
        this.maxLocals = maxLocals;
        this.bytecode = requireNonNull(bytecode, "bytecode cannot be null");
        this.exceptionTable = List.of(exceptionTable);
        this.attributes = List.of(attributes);
    }

    //
    // PUBLIC METHODS
    //

    public int getMaxStack() {
        return maxStack;
    }

    public int getMaxLocals() {
        return maxLocals;
    }

    public ByteBuffer getByteCode() {
        return ByteBuffer.wrap(bytecode).asReadOnlyBuffer();
    }

    public List<ExceptionTableEntry> getExceptionTable() {
        return exceptionTable;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }
}
