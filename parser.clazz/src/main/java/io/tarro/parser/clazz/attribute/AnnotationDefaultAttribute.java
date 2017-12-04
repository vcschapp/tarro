package io.tarro.parser.clazz.attribute;

import io.tarro.parser.clazz.attribute.annotation.ElementValue;
import io.tarro.base.attribute.AttributeType;

import static java.util.Objects.requireNonNull;
import static io.tarro.base.attribute.AttributeType.ANNOTATION_DEFAULT;

/**
 * <p>
 * {@linkplain AttributeType#ANNOTATION_DEFAULT AnnotationDefault}
 * predefined attribute.
 * </p>
 *
 * @author Victor Schappert
 * @since 20171112
 */
public final class AnnotationDefaultAttribute extends Attribute {

    //
    // DATA
    //

    private final ElementValue defaultValue;

    //
    // CONSTRUCTORS
    //

    public AnnotationDefaultAttribute(final ElementValue defaultValue) {
        super(ANNOTATION_DEFAULT);
        this.defaultValue = requireNonNull(defaultValue, "defaultValue cannot be null");
    }

    //
    // PUBLIC METHODS
    //

    public ElementValue getDefaultValue() {
        return defaultValue;
    }
}
