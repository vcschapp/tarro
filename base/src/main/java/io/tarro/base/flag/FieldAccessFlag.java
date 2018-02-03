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

import static io.tarro.base.ClassFileVersion.JAVA1;
import static io.tarro.base.ClassFileVersion.JAVA5;
import static io.tarro.base.flag.FlagMixRule.allOf;
import static io.tarro.base.flag.FlagMixRule.listify;
import static io.tarro.base.flag.FlagMixRule.noneOf;
import static io.tarro.base.flag.FlagMixRule.notBothOf;
import static io.tarro.base.flag.FlagMixRule.visibilityRule;
import static java.lang.Integer.bitCount;

/**
 * Enumerates the flags available for the {@code access_flags} field of a
 * {@code field_info} structure within a Java class file.
 *
 * @author Victor Schappert
 * @since 20171016
 */
public enum FieldAccessFlag implements Flag {

    //
    // ENUMERATORS
    //

    PUBLIC(0x0001),
    PRIVATE(0x0002),
    PROTECTED(0x0004),
    STATIC(0x0008),
    FINAL(0x0010),
    VOLATILE(0x0040),
    TRANSIENT(0x0080),
    SYNTHETIC(0x1000, JAVA5),
    ENUM(0x4000, JAVA5);

    //
    // DATA
    //

    private final int value;
    private final ClassFileVersion classFileVersion;

    //
    // CONSTRUCTORS
    //

    FieldAccessFlag(final int value, final ClassFileVersion classFileVersion) {
        assert 0 == (value & 0xffff0000) : "Value must only occupy low-order 16 bits";
        assert 1 == bitCount(value) : "Value must have exactly one bit set";
        this.value = value;
        this.classFileVersion = classFileVersion;
    }

    FieldAccessFlag(final int value) {
        this(value, JAVA1);
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
    // PUBLIC STATICS
    //

    // TODO: Add a compile-time annotation about optimistic laziness
    public static List<FlagMixRule<FieldAccessFlag>> classFieldRules() {
        if (null != CLASS_FIELD_RULES) {
            return CLASS_FIELD_RULES;
        } else {
            return CLASS_FIELD_RULES = listify(makeGeneralRules());
        }
    }

    // TODO: Add a compile-time annotation about optimistic laziness
    public static List<FlagMixRule<FieldAccessFlag>> interfaceFieldRules() {
        if (null != INTERFACE_FIELD_RULES) {
            return INTERFACE_FIELD_RULES;
        } else {
            return INTERFACE_FIELD_RULES = listify(makeGeneralRules(),
                    makeInterfaceSpecificRules());
        }
    }

    //
    // INTERNALS
    //

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static FlagMixRule<FieldAccessFlag>[] makeGeneralRules() {
        return new FlagMixRule[] {
            visibilityRule("a field", PUBLIC, PRIVATE, PROTECTED),
            notBothOf("a field", FINAL, VOLATILE)
        };
    }

    private static List<FlagMixRule<FieldAccessFlag>> CLASS_FIELD_RULES;

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static FlagMixRule<FieldAccessFlag>[] makeInterfaceSpecificRules() {
        return new FlagMixRule[] {
            allOf("an interface field", PUBLIC, STATIC, FINAL),
            noneOf("an interface field", VOLATILE, TRANSIENT, ENUM)
        };
    }

    private static List<FlagMixRule<FieldAccessFlag>> INTERFACE_FIELD_RULES;
}
