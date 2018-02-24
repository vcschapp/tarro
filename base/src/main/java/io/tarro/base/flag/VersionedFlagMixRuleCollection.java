package io.tarro.base.flag;

import io.tarro.base.ClassFileVersion;
import io.tarro.base.Versioned;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;

/**
 * A collection of flag mix rules effective {@linkplain
 * #getFirstVersionSupporting() as of a particular Java version} and, possibly,
 * {@linkplain #getLastVersionSupporting() until a particular Java version}.
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

    private final ClassFileVersion firstVersionSupporting;
    private final ClassFileVersion lastVersionSupporting;
    private final List<FlagMixRule<F>> rules;

    //
    // CONSTRUCTORS
    //

    VersionedFlagMixRuleCollection(final ClassFileVersion firstVersionSupporting,
                                   final FlagMixRule<F>[] rules) {
        this.firstVersionSupporting = firstVersionSupporting;
        this.lastVersionSupporting = null;
        this.rules = List.of(rules);
    }

    @SafeVarargs
    @SuppressWarnings({"unchecked", "varargs"})
    VersionedFlagMixRuleCollection(final ClassFileVersion firstVersionSupporting,
                                   final ClassFileVersion lastVersionSupporting,
                                   final FlagMixRule<F>[]... rules) {
        this.firstVersionSupporting = firstVersionSupporting;
        this.lastVersionSupporting = lastVersionSupporting;
        this.rules = List.of(stream(rules).flatMap(Arrays::stream).toArray(
                FlagMixRule[]::new));
    }

    //
    // INTERFACE: Versioned
    //

    /**
     * The value returned indicates the lowest-numbered Java version in which
     * the rules in this collection came into force.
     *
     * @see #getLastVersionSupporting()
     */
    @Override
    public ClassFileVersion getFirstVersionSupporting() {
        return firstVersionSupporting;
    }

    /**
     * If the value returned is empty, then this rule applies to all Java
     * versions equal to or later than {@linkplain #getFirstVersionSupporting()
     * the first version to which it applies}. Otherwise, this rule applies
     * up to the Java version returned in the non-empty result.
     *
     * @return Empty if the rule applies to all known versions, or the non-empty
     *         last version to which this collection of rules applies
     * @see #getFirstVersionSupporting()
     */
    @Override
    public Optional<ClassFileVersion> getLastVersionSupporting() {
        return ofNullable(lastVersionSupporting);
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
