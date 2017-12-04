package io.tarro.parser.clazz.attribute;

import io.tarro.base.flag.InnerClassAccessFlag;

import java.util.EnumSet;
import java.util.Set;

import static java.util.Collections.unmodifiableSet;

/**
 * @author Victor Schappert
 * @since 20171125
 */
public class InnerClass {

    //
    // DATA
    //

    private final int innerClassInfoIndex;
    private final int outerClassInfoIndex;
    private final int innerNameIndex;
    private final Set<InnerClassAccessFlag> accessFlags;

    //
    // CONSTRUCTORS
    //

    public InnerClass(final int innerClassInfoIndex, final int outerClassInfoIndex, final int innerNameIndex,
            final EnumSet<InnerClassAccessFlag> accessFlags) {
        this.innerClassInfoIndex = innerClassInfoIndex;
        this.outerClassInfoIndex = outerClassInfoIndex;
        this.innerNameIndex = innerNameIndex;
        this.accessFlags = unmodifiableSet(accessFlags);
    }

    //
    // PUBLIC METHODS
    //

    public int getInnerClassInfoIndex() {
        return innerClassInfoIndex;
    }

    public int getOuterClassInfoIndex() {
        return outerClassInfoIndex;
    }

    public int getInnerNameIndex() {
        return innerNameIndex;
    }

    public Set<InnerClassAccessFlag> getAccessFlags() {
        return accessFlags;
    }
}
