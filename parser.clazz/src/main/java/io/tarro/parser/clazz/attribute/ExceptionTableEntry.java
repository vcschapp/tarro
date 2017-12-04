package io.tarro.parser.clazz.attribute;

/**
 * <p>
 *
 * </p>
 *
 * @author Victor Schappert
 * @since 20171130
 */
public final class ExceptionTableEntry {

    //
    // DATA
    //

    private final int startPC;
    private final int endPC;
    private final int handlerPC;
    private final int catchTypeIndex;

    //
    // CONSTRUCTORS
    //

    public ExceptionTableEntry(int startPC, int endPC, int handlerPC,
            int catchTypeIndex) {
        this.startPC = startPC;
        this.endPC = endPC;
        this.handlerPC = handlerPC;
        this.catchTypeIndex = catchTypeIndex;
    }

    //
    // PUBLIC METHODS
    //

    public int getStartPC() {
        return startPC;
    }

    public int getEndPC() {
        return endPC;
    }

    public int getHandlerPC() {
        return handlerPC;
    }

    public int getCatchTypeIndex() {
        return catchTypeIndex;
    }
}
