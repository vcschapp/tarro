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
import static io.tarro.base.InternalError.unhandledClassFileVersion;
import static io.tarro.base.flag.FlagMixRule.bothOf;
import static io.tarro.base.flag.FlagMixRule.exactlyOneOf;
import static io.tarro.base.flag.FlagMixRule.ifFirstThenNoneOfTheRest;
import static io.tarro.base.flag.FlagMixRule.listify;
import static io.tarro.base.flag.FlagMixRule.noOthersThan;
import static io.tarro.base.flag.FlagMixRule.noneOf;
import static io.tarro.base.flag.FlagMixRule.visibilityRule;
import static java.lang.Integer.bitCount;

/**
 * Enumerates the flags available for the {@code access_flags} field of a
 * {@code field_info} structure within a Java class file.
 *
 *
 * @author Victor Schappert
 * @since 20171201
 */
public enum MethodAccessFlag implements Flag {

    //
    // ENUMERATORS
    //

    PUBLIC(0x0001),
    PRIVATE(0x0002),
    PROTECTED(0x0004),
    STATIC(0x0008),
    FINAL(0x0010),
    SYNCHRONIZED(0x0020),
    BRIDGE(0x0040) /* FIXME: Is this supposed to be Java 5? */,
    VARARGS(0x0080, JAVA5),
    NATIVE(0x0100),
    ABSTRACT(0x0400),
    STRICT(0x0800),
    SYNTHETIC(0x1000);

    //
    // DATA
    //

    private final int value;
    private final ClassFileVersion classFileVersion;

    //
    // CONSTRUCTORS
    //

    MethodAccessFlag(final int value, final ClassFileVersion classFileVersion) {
        assert 0 == (value & 0xffff0000) : "Value must only occupy low-order 16 bits";
        assert 1 == bitCount(value) : "Value must have exactly one bit set";
        this.value = value;
        this.classFileVersion = classFileVersion;
    }

    MethodAccessFlag(final int value) {
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

    public static List<FlagMixRule<MethodAccessFlag>> classMethodBaseRules() {
        if (null != CLASS_METHOD_BASE_RULES) {
            return CLASS_METHOD_BASE_RULES;
        } else {
            return CLASS_METHOD_BASE_RULES = listify(makeGeneralRules(), makeClassMethodRules());
        }
    }

    public static List<FlagMixRule<MethodAccessFlag>> instanceInitializerIncrementalRules() {
        if (null != INSTANCE_INITIALIZER_INCREMENTAL_RULES) {
            return INSTANCE_INITIALIZER_INCREMENTAL_RULES;
        } else {
            return INSTANCE_INITIALIZER_INCREMENTAL_RULES = listify(makeInstanceInitializerIncrementalRules());
        }
    }

    public static List<FlagMixRule<MethodAccessFlag>> interfaceMethodBaseRules() {
        if (null != INTERFACE_METHOD_BASE_RULES) {
            return INTERFACE_METHOD_BASE_RULES;
        } else {
            return INTERFACE_METHOD_BASE_RULES = listify(makeGeneralRules(), makeInterfaceMethodBaseRules());
        }
    }

    public static List<FlagMixRule<MethodAccessFlag>> interfaceMethodIncrementalRulesForVersion(final ClassFileVersion classFileVersion) {
        switch (classFileVersion) {
        case JAVA1:
        case JAVA1_1:
        case JAVA1_2:
        case JAVA1_3:
        case JAVA1_4:
        case JAVA5:
        case JAVA6:
        case JAVA7:
            return interfaceMethodIncrementalRulesPreJava8();
        case JAVA8:
        case JAVA9:
            return interfaceMethodIncrementalRulesJava8Plus();
        default:
            throw unhandledClassFileVersion(classFileVersion);
        }
    }

    //
    // INTERNALS
    //

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static FlagMixRule<MethodAccessFlag>[] makeGeneralRules() {
        return new FlagMixRule[] {
            ifFirstThenNoneOfTheRest("a method", ABSTRACT, FINAL, NATIVE, PRIVATE, STATIC, STRICT, SYNCHRONIZED)
        };
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static FlagMixRule<MethodAccessFlag>[] makeClassMethodRules() {
        return new FlagMixRule[] {
            visibilityRule("a class method", PUBLIC, PRIVATE, PROTECTED)
        };
    }

    private static List<FlagMixRule<MethodAccessFlag>> CLASS_METHOD_BASE_RULES;

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static FlagMixRule<MethodAccessFlag>[] makeInstanceInitializerIncrementalRules() {
        return new FlagMixRule[] {
            noOthersThan("an instance initializer", PUBLIC, PRIVATE, PROTECTED, VARARGS, STRICT, SYNTHETIC)
        };
    }

    private static List<FlagMixRule<MethodAccessFlag>> INSTANCE_INITIALIZER_INCREMENTAL_RULES;

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static FlagMixRule<MethodAccessFlag>[] makeInterfaceMethodBaseRules() {
        return new FlagMixRule[] {
                noneOf("an interface method", PROTECTED, FINAL, SYNCHRONIZED, NATIVE)
        };
    }

    private static List<FlagMixRule<MethodAccessFlag>> INTERFACE_METHOD_BASE_RULES;

    private static List<FlagMixRule<MethodAccessFlag>> interfaceMethodIncrementalRulesPreJava8() {
        if (null != INTERFACE_METHOD_INCREMENTAL_RULES_PRE_JAVA8) {
            return INTERFACE_METHOD_INCREMENTAL_RULES_PRE_JAVA8;
        } else {
            return INTERFACE_METHOD_INCREMENTAL_RULES_PRE_JAVA8 = listify(makeInterfaceMethodIncrementalRulesPreJava8());
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static FlagMixRule<MethodAccessFlag>[] makeInterfaceMethodIncrementalRulesPreJava8() {
        return new FlagMixRule[] {
            bothOf("an interface method", PUBLIC, ABSTRACT)
        };
    }

    private static List<FlagMixRule<MethodAccessFlag>> INTERFACE_METHOD_INCREMENTAL_RULES_PRE_JAVA8;

    private static List<FlagMixRule<MethodAccessFlag>> interfaceMethodIncrementalRulesJava8Plus() {
        if (null != INTERFACE_METHOD_INCREMENTAL_RULES_JAVA8_PLUS) {
            return INTERFACE_METHOD_INCREMENTAL_RULES_JAVA8_PLUS;
        } else {
            return INTERFACE_METHOD_INCREMENTAL_RULES_JAVA8_PLUS = listify(makeInterfaceMethodIncrementalRulesJava8Plus());
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static FlagMixRule<MethodAccessFlag>[] makeInterfaceMethodIncrementalRulesJava8Plus() {
        return new FlagMixRule[] {
                exactlyOneOf("an interface method", PUBLIC, PRIVATE)
        };
    }

    private static List<FlagMixRule<MethodAccessFlag>> INTERFACE_METHOD_INCREMENTAL_RULES_JAVA8_PLUS;
}
