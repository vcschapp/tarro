/*
MIT License

Copyright (c) 2017 Victor Schappert

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package io.tarro.base.flag;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.joining;

/**
 * @since 20171014
 * @author Victor Schappert
 * @param <F> Type of {@link Flag} enumeration the rule applies to
 * @see Flag
 */
public final class FlagMixRule<F extends Enum<F> & Flag> {

    //
    // DATA
    //

    private final Predicate<EnumSet<F>> badnessTest;
    private final String reason;

    //
    // CONSTRUCTORS
    //

    public FlagMixRule(final Predicate<EnumSet<F>> badnessTest, final String reason) {
        this.badnessTest = badnessTest;
        this.reason = reason;
    }

    //
    // PUBLIC METHODS
    //

    public void validate(final EnumSet<F> flags) {
        if (badnessTest.test(flags)) {
            throw new BadAccessFlagsMixException(flags
                    .stream()
                    .map(Flag::getFlagName)
                    .collect(joining(" | ", "", " is a bad combination: " + reason)));
        }
    }

    //
    // STATIC METHODS
    //

    static <F extends Enum<F> & Flag> List<FlagMixRule<F>> listify(final FlagMixRule<F>[] array) {
        return List.of(array);
    }

    static <F extends Enum<F> & Flag> List<FlagMixRule<F>> listify(final FlagMixRule<F>[]... arrays) {
        return List.of(stream(arrays)
                .flatMap(array -> stream(array))
                .toArray(FlagMixRule[]::new));
    }

    static <F extends Enum<F> & Flag> FlagMixRule<F> rule(final Predicate<EnumSet<F>> badnessTest, final String reason) {
        return new FlagMixRule(badnessTest, reason);
    }

    static <F extends Enum<F> & Flag> FlagMixRule<F> visibilityRule(
            final String entityName, final F publicFlag, final F privateFlag, final F protectedFlag) {
        final String reason = joinGrammatically(
                new Flag[] { publicFlag, privateFlag, protectedFlag },
                "Only one of ", "and", " is permitted on ", entityName);
        return rule(set -> {
            int flagCount = 0;
            if (set.contains(publicFlag)) ++flagCount;
            if (set.contains(privateFlag)) ++flagCount;
            if (set.contains(protectedFlag)) ++flagCount;
            return 1 < flagCount;
        }, reason);
    }

    static <F extends Enum<F> & Flag> FlagMixRule<F> bothOf(final String entityName, final F firstFlag,
            final F secondFlag) {
        final String reason = "Both " + firstFlag.getFlagName() + " and " + secondFlag.getFlagName() +
                "must be set on " + entityName;
        return rule(set -> !set.contains(firstFlag) || !set.contains(secondFlag), reason);
    }

    static <F extends Enum<F> & Flag> FlagMixRule<F> allOf(final String entityName, final F... requiredFlags) {
        final int requiredMask = mask(requiredFlags);
        final String reason = joinGrammatically(requiredFlags, "All of", "and", "must be set on", entityName);
        return rule(set -> requiredMask != (mask(set) & requiredMask), reason);
    }

    static <F extends Enum<F> & Flag> FlagMixRule<F> exactlyOneOf(final String entityName, final F firstFlag,
            final F secondFlag) {
        final String reason = "Exactly one of " + firstFlag.getFlagName() + " and " + secondFlag.getFlagName() +
                "must be set on " + entityName;
        return rule(set -> {
            int flagCount = 0;
            if (set.contains(firstFlag)) ++flagCount;
            if (set.contains(secondFlag)) ++flagCount;
            return 1 != flagCount;
        }, reason);
    }

    static <F extends Enum<F> & Flag> FlagMixRule<F> noneOf(final String entityName, final F... notPermittedFlags) {
        final int notPermittedMask = mask(notPermittedFlags);
        final String reason = joinGrammatically(notPermittedFlags,"None of ",
                "or", " is permitted on ", entityName);
        return rule(set -> 0 != (mask(set) & notPermittedMask), reason);
    }

    static <F extends Enum<F> & Flag> FlagMixRule<F> notBothOf(final String entityName, final F firstFlag,
            final F secondFlag) {
        final String reason = firstFlag.getFlagName() + " and " + secondFlag.getFlagName() +
                " may not both be set on " + entityName;
        return rule(set -> set.contains(firstFlag) && set.contains(secondFlag), reason);
    }

    static <F extends Enum<F> & Flag> FlagMixRule<F> noOthersThan(final String entityName, final F... permittedFlags) {
        final int notPermittedMask = ~mask(permittedFlags);
        final String reason = joinGrammatically(permittedFlags, "No flag other than ", "and",
                " is permitted on", entityName);
        return rule(set -> 0 != (mask(set) & notPermittedMask), reason);
    }

    static <F extends Enum<F> & Flag> FlagMixRule<F> ifFirstThenAlsoSecond(final String entityName, final F predicateFlag,
            final F impliedFlag) {
        final String reason = "If " + predicateFlag.getFlagName() + " is set on " + entityName + ", then " +
                impliedFlag.getFlagName() + " must also be set";
        return rule(set -> set.contains(predicateFlag) && !set.contains(impliedFlag), reason);
    }

    static <F extends Enum<F> & Flag> FlagMixRule<F> ifFirstThenNoneOfTheRest(final String entityName, final F predicateFlag,
            final F... consequentlyNotPermittedFlags) {
        final int consequentlyNotPermittedMask =  mask(consequentlyNotPermittedFlags);
        final String reason = joinGrammatically(
                consequentlyNotPermittedFlags,
                "If " + predicateFlag.getFlagName() + " is present on " + entityName + ", then none of ",
                "or", " is permitted");
        return rule(set -> set.contains(predicateFlag) && 0 != (mask(set) & consequentlyNotPermittedMask), reason);
    }

    //
    // INTERNALS
    //

    private static <F extends Enum<F> & Flag> int mask(final F... flags) {
        return mask(stream(flags));
    }

    private static <F extends Enum<F> & Flag> int mask(final EnumSet<F> flags) {
        return mask(flags.stream());
    }

    private static <F extends Flag> int mask(final Stream<F> flags) {
        return flags.mapToInt(Flag::getValue).reduce(0, (a, b) -> a | b);
    }

    private static String joinGrammatically(final Flag[] flags,
            final String prefix, final String conjunction, final String... suffixes) {
        final StringBuilder builder = new StringBuilder(128)
                .append(prefix)
                .append(' ');
        builder.append(flags[0].getFlagName());
        // ---------------------------------------------------------------------
        // What the if-branch loop does:
        //    [foo, bar, baz] -> "foo, bar, "
        //    [foo, bar, baz, ham] -> "foo, bar, baz, "
        // What the else-branch does:
        //    [foo] -> "foo "
        //    [foo, bar] -> "foo "
        // ---------------------------------------------------------------------
        int i;
        if (2 < flags.length) {
            builder.append(", ");
            for (i = 1; i < flags.length - 1; ++i) {
                builder.append(flags[i].getFlagName());
                builder.append(',');
            }
        } else {
            i = 1;
        }
        // ---------------------------------------------------------------------
        // Add the last item prefixed by the conjunction.
        // ---------------------------------------------------------------------
        builder.append(' ')
                .append(conjunction)
                .append(' ')
                .append(flags[i].getFlagName());
        // ---------------------------------------------------------------------
        // Add the suffixes and return.
        // ---------------------------------------------------------------------
        for (final String suffix : suffixes) {
            builder.append(' ').append(suffix);
        }
        return builder.toString();
    }
}
