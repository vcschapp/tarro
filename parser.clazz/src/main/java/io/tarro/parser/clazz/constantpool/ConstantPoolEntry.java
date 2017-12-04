package io.tarro.parser.clazz.constantpool;

import io.tarro.base.constantpool.ConstantPoolTag;

import static java.util.Objects.requireNonNull;

public class ConstantPoolEntry {

    //
    // DATA
    //

    private final ConstantPoolTag constantPoolTag;

    //
    // CONSTRUCTORS
    //

    public ConstantPoolEntry(final ConstantPoolTag constantPoolTag) {
        this.constantPoolTag = requireNonNull(constantPoolTag, "constantPoolTag cannot be null");
    }

    //
    // PUBLIC METHODS
    //

    public final ConstantPoolTag getConstantPoolTag() {
        return constantPoolTag;
    }
}
