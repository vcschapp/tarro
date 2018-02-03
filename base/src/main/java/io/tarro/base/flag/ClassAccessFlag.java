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

import io.tarro.base.ClassFileVersion;

import java.util.List;

import static io.tarro.base.flag.FlagMixRule.ifFirstThenAlsoSecond;
import static io.tarro.base.flag.FlagMixRule.ifFirstThenNoneOfTheRest;
import static io.tarro.base.flag.FlagMixRule.listify;
import static io.tarro.base.flag.FlagMixRule.notBothOf;
import static java.lang.Integer.bitCount;
import static io.tarro.base.flag.FlagMixRule.rule;
import static java.util.Collections.singletonList;

/**
 * Enumerates the flags available in the top-level {@code access_flags} field of
 * a Java class file.
 *
 * @author Victor Schappert
 * @since 20171014
 */
public enum ClassAccessFlag implements Flag {

    //
    // ENUMERATORS
    //

    PUBLIC(0x0001, ClassFileVersion.JAVA1),
    FINAL(0x010, ClassFileVersion.JAVA1),
    SUPER(0x0020, ClassFileVersion.JAVA1),
    INTERFACE(0x200, ClassFileVersion.JAVA1),
    ABSTRACT(0x400, ClassFileVersion.JAVA1),
    SYNTHETIC(0x1000, ClassFileVersion.JAVA5),
    ANNOTATION(0x2000, ClassFileVersion.JAVA5),
    ENUM(0x4000, ClassFileVersion.JAVA5),
    MODULE(0x8000, ClassFileVersion.JAVA9);

    //
    // DATA
    //

    private final int value;
    private final ClassFileVersion classFileVersion;

    //
    // CONSTRUCTORS
    //

    ClassAccessFlag(final int value, final ClassFileVersion classFileVersion) {
        assert 0 == (value & 0xffff0000) : "Value must only occupy low-order 16 bits";
        assert 1 == bitCount(value) : "Value must have exactly one bit set";
        this.value = value;
        this.classFileVersion = classFileVersion;
    }

    //
    // INTERFACE: Flag
    //

    @Override
    public String getFlagName() {
        return "ACC_" + name();
    }

    //
    // INTERFACE: Valued
    //

    @Override
    public int getValue() {
        return value;
    }

    //
    // INTERFACE: Versioned
    //

    @Override
    public ClassFileVersion getFirstVersionSupporting() {
        return classFileVersion;
    }

    //
    // PUBLIC CONSTANTS
    //

    // TODO: Annotate something about deliberately "optimistic" thread-safe lazy
    // TODO: Note this is all Java versions.
    // TODO: I think we want to re-write the basicRules()/additionalRules()
    //       interface to be simpler. Just rulesFor(ClassFileVersion) and have
    //       it return the complete ruleset that can be enforced at that version.
    public static final List<FlagMixRule<ClassAccessFlag>> basicRules() {
        if (null != BASIC_RULES) {
            return BASIC_RULES;
        } else {
            return BASIC_RULES = listify(makeBasicRules());
        }
    }

    // TODO: Note for now this is Java 6+. If we have to shard further on
    //       version then we should accept a version range or something as a
    //       parameter.
    public static final List<FlagMixRule<ClassAccessFlag >> additionalRules() {
        if (null != ADDITIONAL_RULES) {
            return ADDITIONAL_RULES;
        } else {
            return ADDITIONAL_RULES = singletonList(makeAdditionalRule());
        }
    }

    //
    // INTERNALS
    //

    private static List<FlagMixRule<ClassAccessFlag>> BASIC_RULES;

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static FlagMixRule<ClassAccessFlag>[] makeBasicRules() {
        // Based on the Java virtual machine specification, one would expect the
        // rule `ifFirstThenAlsoSecond("a class", INTERFACE, ABSTRACT)` to be
        // part of this rule set. However, while that rule has *technically*
        // existed since Java 1.0.2, it only started being enforced in Java 6,
        // so we don't include it in the base rule set.
        //     See: https://stackoverflow.com/a/47871486/1911388
        return new FlagMixRule[] {
                notBothOf("a class", FINAL, ABSTRACT),
                ifFirstThenNoneOfTheRest("a class", INTERFACE, FINAL, SUPER, ENUM),
                ifFirstThenAlsoSecond("a class", ANNOTATION, INTERFACE),
                rule(set -> set.contains(MODULE) && 1 < set.size(),
                        "If ACC_MODULE is set no other access_flags may be set at class level")
        };
    }

    private static List<FlagMixRule<ClassAccessFlag>> ADDITIONAL_RULES;

    private static FlagMixRule<ClassAccessFlag> makeAdditionalRule() {
        return ifFirstThenAlsoSecond("a class", INTERFACE, ABSTRACT);
    }

}
