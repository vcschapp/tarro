package io.tarro.base.flag;

import io.tarro.base.ClassFileVersion;
import io.tarro.base.Versioned;

import java.util.Collection;
import java.util.List;

/**
 * A collection of flag mix rules effective as of a particular Java version.
 *
 * @param <F> Type of flag to which the rules apply
 * @author Victor Schappert
 * @since 20180203
 */
public class VersionedFlagMixRuleCollection<F extends Enum<F> & Flag>
        implements Versioned {

    //
    // DATA
    //

    private final ClassFileVersion classFileVersion;
    private final List<FlagMixRule<F>> rules;

    //
    // CONSTRUCTORS
    //

    VersionedFlagMixRuleCollection(final ClassFileVersion classFileVersion,
                                   final FlagMixRule<F>[] rules) {
        this.classFileVersion = classFileVersion;
        this.rules = List.of(rules);
    }

    //
    // INTERFACE: Versioned
    //

    @Override
    public ClassFileVersion getFirstVersionSupporting() {
        return classFileVersion;
    }

    //
    // PUBLIC METHODS
    //

    /**
     * Obtains the set of rules applicable as of this rule set's
     * {@linkplain #getFirstVersionSupporting() version}.
     *
     * @return List of rules
     */
    public Collection<FlagMixRule<F>> getRules() {
        return rules;
    }
}
