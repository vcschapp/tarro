package io.tarro.parser.clazz.attribute;

import io.tarro.parser.clazz.attribute.annotation.ElementValuePair;

import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Runtime annotation contained within one of the predefined runtime annotation
 * attributes.
 *
 * @author Victor Schappert
 * @since 20171106
 */
public class Annotation {

    //
    // DATA
    //

    private final int typeIndex;
    private final List<ElementValuePair> elementValuePairs;

    //
    // CONSTRUCTORS
    //

    public Annotation(final int typeIndex, final ElementValuePair[] elementValuePairs) {
        this.typeIndex = typeIndex;
        this.elementValuePairs = List.of(elementValuePairs);
    }

    //
    // PUBLIC METHODS
    //

    public final int getTypeIndex() {
        return typeIndex;
    }

    public final List<ElementValuePair> getElementValuePairs() {
        return elementValuePairs;
    }
}
