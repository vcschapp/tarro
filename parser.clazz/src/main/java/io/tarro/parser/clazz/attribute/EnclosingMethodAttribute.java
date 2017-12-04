package io.tarro.parser.clazz.attribute;

import io.tarro.base.attribute.AttributeType;

/**
 * <p>
 * {@linkplain AttributeType#ENCLOSING_METHOD EnclosingMethod}
 * predefined attribute.
 * </p>
 *
 * @author Victor Schappert
 * @since 20171104
 */
public final class EnclosingMethodAttribute extends Attribute {

    //
    // DATA
    //

    private final int classIndex;
    private final int methodIndex;

    //
    // CONSTRUCTORS
    //

    /**
     * Constructs the EnclosingMethod attribute.
     *
     * @param classIndex
     * @param methodIndex
     */
    public EnclosingMethodAttribute(final int classIndex, final int methodIndex) {
        super(AttributeType.ENCLOSING_METHOD);
        this.classIndex = classIndex;
        this.methodIndex = methodIndex;
    }

    //
    // PUBLIC METHODS
    //

    public int getClassIndex() {
        return classIndex;
    }

    public int getMethodIndex() {
        return methodIndex;
    }
}
