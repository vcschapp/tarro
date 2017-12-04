package io.tarro.parser.clazz.attribute;

/**
 * <p>
 *
 * </p>
 *
 * @author Victor Schappert
 * @since 20171126
 */
public final class BootstrapMethod {

    //
    // DATA
    //

    private final int bootstrapMethodIndex;
    private final int[] bootstrapArgumentIndices;

    //
    // CONSTRUCTORS
    //

    public BootstrapMethod(final int bootstrapMethodIndex, final int[] bootstrapArgumentIndices) {
        this.bootstrapMethodIndex = bootstrapMethodIndex;
        this.bootstrapArgumentIndices = bootstrapArgumentIndices;
    }

    //
    // PUBLIC METHODS
    //

    public int getBootstrapMethodIndex() {
        return bootstrapMethodIndex;
    }

    public int[] getBootstrapArgumentIndices() {
        return bootstrapArgumentIndices;
    }
}
