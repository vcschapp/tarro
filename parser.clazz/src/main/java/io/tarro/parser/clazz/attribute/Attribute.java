package io.tarro.parser.clazz.attribute;

import io.tarro.base.attribute.AttributeType;

import static java.util.Objects.requireNonNull;

/**
 * Encapsulate an {@code attribute_info} structure in a Java class file.
 *
 * @author Victor Schappert
 * @since 20171016
 */
public class Attribute {

    //
    // DATA
    //

    private final AttributeType attributeType;

    //
    // CONSTRUCTORS
    //

    public Attribute(final AttributeType attributeType) {
        this.attributeType = requireNonNull(attributeType, "attributeType cannot be null");
    }

    //
    // ACCESSORS
    //

    public final AttributeType getAttributeType() {
        return attributeType;
    }
}
