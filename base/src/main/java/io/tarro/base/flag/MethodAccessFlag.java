/*
 * MIT License
 *
 * Copyright (c) 2018 Victor Schappert
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.tarro.base.flag;

import io.tarro.base.ClassFileVersion;

import java.util.List;

import static io.tarro.base.ClassFileVersion.JAVA1;
import static io.tarro.base.ClassFileVersion.JAVA1_2;
import static io.tarro.base.ClassFileVersion.JAVA5;
import static io.tarro.base.ClassFileVersion.JAVA7;
import static io.tarro.base.ClassFileVersion.JAVA8;
import static io.tarro.base.flag.FlagMixRule.bothOf;
import static io.tarro.base.flag.FlagMixRule.exactlyOneOf;
import static io.tarro.base.flag.FlagMixRule.ifFirstThenNoneOfTheRest;
import static io.tarro.base.flag.FlagMixRule.noOthersThan;
import static io.tarro.base.flag.FlagMixRule.noneOf;
import static io.tarro.base.flag.FlagMixRule.visibilityRule;

/**
 * Enumerates the flags available for the {@code access_flags} field of a
 * {@code method_info} structure within a Java class file.
 *
 *
 * @author Victor Schappert
 * @since 20171201
 */
public enum MethodAccessFlag implements Flag {

    //
    // ENUMERATORS
    //

    /**
     * {@code 0x0001} ({@code ACC_PUBLIC}): Indicates the method is declared
     * {@code public} and may be accessed from outside its package.
     *
     * @see #PRIVATE
     * @see #PROTECTED
     */
    PUBLIC(0x0001),
    /**
     * {@code 0x0002} ({@code ACC_PRIVATE}): Indicates the method is declared
     * {@code private} and is accessible only within the defining class.
     *
     * @see #PRIVATE
     * @see #PROTECTED
     */
    PRIVATE(0x0002),
    /**
     * {@code 0x0004} ({@code ACC_PROTECTED}): Indicates the method is declared
     * {@code protected} and may be accessed within subclasses as well as other
     * classes in the same package.
     *
     * @see #PUBLIC
     * @see #PRIVATE
     */
    PROTECTED(0x0004),
    /**
     * {@code 0x0008} ({@code ACC_STATIC}): Indicates the method is declared
     * {@code static}, <em>ie</em> is not an instance method.
     */
    STATIC(0x0008),
    /**
     * {@code 0x0010} ({@code ACC_FINAL}): Indicates the method is declared
     * {@code final} and must not be overridden.
     */
    FINAL(0x0010),
    /**
     * {@code 0x0020} ({@code ACC_SYNCHRONIZED}): Indicates the method is
     * declared {@code synchronized} and its invocation is wrapped by monitor
     * use.
     */
    SYNCHRONIZED(0x0020),
    /**
     * <p>
     * {@code 0x0040} ({@code ACC_BRIDGE}): Indicates a bridge method generated
     * by the compiler.
     * </p>
     *
     * <p>
     * The bridge concept was added in Java 5 in order to support various
     * scenarios caused by generic type erasure. The relationship between bridge
     * methods and synthetic methods seems to be this. First, every bridge
     * method is a synthetic method, but the reverse is not necessarily true.
     * Second, bridge methods are typically delegating shims whose only purpose
     * is to glue together two methods that wouldn't otherwise be able to call
     * one another. On the other hand, the category of synthetic methods
     * includes not only bridge methods but other compiler-generated methods,
     * such as, for example, those that arise to provide access from classes
     * which "contain" inner classes to {@code private} members of the inner
     * classes.
     * </p>
     *
     * @see #SYNTHETIC
     */
    BRIDGE(0x0040, JAVA5),
    /**
     * <p>
     * {@code 0x0080} ({@code ACC_VARARGS}): Indicates the method is declared to
     * have a variable number of arguments.
     * </p>
     *
     * <p>
     * This flag tells the Java compiler that the method's last parameter is a
     * varargs parameter in cases where the compiler does not have access to the
     * method source code. The flag is needed because the Java method descriptor
     * can only show the parameter as having an array type.
     * </p>
     */
    VARARGS(0x0080, JAVA5),
    /**
     * {@code 0x0100} ({@code ACC_NATIVE}): Indicates the method is declared
     * {@code native} and implemented in a language other than the Java
     * programming language.
     */
    NATIVE(0x0100),
    /**
     * {@code 0x0400} ({@code ACC_ABSTRACT}): Indicates the method is declared
     * {@code abstract}, or is an interface method, and consequently has had no
     * implementation provided.
     */
    ABSTRACT(0x0400),
    /**
     * {@code 0x0800} ({@code ACC_STRICT}): Indicates the method is declared
     * {@code strictfp} and runs in the FP-strict floating point mode.
     */
    STRICT(0x0800, JAVA1_2),
    /**
     * <p>
     * {@code 0x1000} ({@code ACC_SYNTHETIC}): Indicates a synthetic method
     * which is not present in the source code <em>and</em> is not considered
     * to be an "implementation artifact" of the source language.
     * </p>
     *
     * <p>
     * In the Java programming language, certain compiler-generated methods are
     * considered to be "implementation artifacts" which are <em>not</em> to be
     * tagged as synthetic. These include default no-argument constructors,
     * class or interface initialization methods, and several static methods on
     * enumeration classes. Consult the Java Virtual Machine Specification for
     * more details.
     * </p>
     *
     * <p>
     * The concept of a synthetic method is broader than the concept of a
     * {@linkplain #BRIDGE bridge} method, which constitutes but one of several
     * categories of synthetic method.
     * </p>
     *
     * @see #BRIDGE
     * @see io.tarro.base.attribute.AttributeType#SYNTHETIC
     */
    SYNTHETIC(0x1000, JAVA5);

    //
    // DATA
    //

    private final int value;
    private final ClassFileVersion classFileVersion;

    //
    // CONSTRUCTORS
    //

    MethodAccessFlag(final int value, final ClassFileVersion classFileVersion) {
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

    /**
     * {@inheritDoc}
     *
     * <p>
     * This method returns the individual flag's integer value, which is
     * guaranteed to be a power of two distinct from any other flag in the
     * enumeration, since the {@code access_flags} member of class file is a
     * bitmask.
     * </p>
     *
     * @return Flag bit
     */
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

    /**
     * <p>
     * Obtains the "basic" rules about which method access flags may be mixed
     * together on a method of a class proper (<em>ie</em> <em>not</em> on a
     * method in an interface).
     * </p>
     *
     * <p>
     * The "basic" rules are the rules that have been in place <em>and enforced
     * in practice</em> by the JVM since {@linkplain ClassFileVersion#JAVA1 Java
     * 1} and that continue to apply across all Java versions..
     * </p>
     *
     * <p>
     * Note that in addition to following the rules for class methods, a class
     * method that is an instance initializer must also follow the rules for
     * instance initializers.
     * </p>
     *
     * @return Basic class method flag mixing rules in force since Java 1
     * @see #instanceInitializerBasicIncrementalRules()
     */
    public static VersionedFlagMixRuleCollection<MethodAccessFlag> classMethodBasicRules() {
        // Thread-safe despite the appearance of a race condition involving a
        // static variable because all the methods called are idempotent and we
        // don't care if we do marginal amounts of extra work in unlikely corner
        // cases.
        if (null != CLASS_METHOD_BASIC_RULES) {
            return CLASS_METHOD_BASIC_RULES;
        } else {
            return CLASS_METHOD_BASIC_RULES = new VersionedFlagMixRuleCollection<>(
                    JAVA1, makeGeneralRules());
        }
    }

    /**
     * <p>
     * Obtains the "basic" <em>incremental</em> rules about which method access
     * flags may be mixed together on an instance initializer method
     * (<em>ie</em>, typically, a compiled constructor).
     * </p>
     *
     * <p>
     * The "basic" rules are the rules that have been in place <em>and enforced
     * in practice</em> by the JVM since {@linkplain ClassFileVersion#JAVA1 Java
     * 1} and continue to apply across all Java versions.
     * </p>
     *
     * <p>
     * These rules are additive or <em>incremental</em> in the sense that they
     * apply as well as the ordinary class method rules, but they do not repeat
     * the class method rules. Hence to validate an instance initializer, one
     * must test both the class method rules and the incremental instance
     * initializer rules.
     * </p>
     *
     * @return Basic incremental instance initializer method flag mixing rules
     *         in force since Java 1
     * @see #classMethodBasicRules()
     */
    public static VersionedFlagMixRuleCollection<MethodAccessFlag>
        instanceInitializerBasicIncrementalRules() {
        // Thread-safe despite the appearance of a race condition involving a
        // static variable because all the methods called are idempotent and we
        // don't care if we do marginal amounts of extra work in unlikely corner
        // cases.
        if (null != INSTANCE_INITIALIZER_BASIC_INCREMENTAL_RULES) {
            return INSTANCE_INITIALIZER_BASIC_INCREMENTAL_RULES;
        } else {
            return INSTANCE_INITIALIZER_BASIC_INCREMENTAL_RULES =
                    new VersionedFlagMixRuleCollection<>(JAVA1,
                            makeInstanceInitializerRules());
        }
    }

    /**
     * <p>
     * Obtains the "basic" rules about which method access flags may be mixed
     * together on a method of an interface.
     * </p>
     *
     * <p>
     * The "basic" rules are the rules that have been in place <em>and enforced
     * in practice</em> by the JVM since {@linkplain ClassFileVersion#JAVA1 Java
     * 1} and that continue to apply across all Java versions.
     * </p>
     *
     * <p>
     * Note the following:
     * </p>
     *
     * <ul>
     * <li>
     * The rules returned by this method are the same as the rules
     * returned in the first entry of the list returned by the
     * {@link #interfaceMethodAllRules()} method.
     * </li>
     * <li>
     * The rules returned by this method are necessary, but not sufficient, to
     * validate the method access flags of an interface method.
     * <ul>
     * <li>
     * This is because Java 8's introduction of two flavours of non-abstract
     * methods ({@code default} instance methods and {@code static} methods)
     * caused a bifurcation where some post-Java 8 rules cannot simply be added
     * to the pre-Java 8 rules. Some rules are mutually exclusive.
     * </li>
     * <li>
     * Consequently while the rules returned by this method can be used for
     * smoke testing (since they apply across all Java versions) they are not
     * complete for <em>any</em> Java version. To obtain all relevant rules, use
     * the {@link #interfaceMethodAllRules()} method.
     * </li>
     * </ul>
     * </li>
     * </ul>
     *
     * @return Basic class method flag mixing rules in force since Java 1
     * @see #interfaceMethodAllRules()
     */
    public static VersionedFlagMixRuleCollection<MethodAccessFlag>
        interfaceMethodBasicRules() {
        // Thread-safe despite the appearance of a race condition involving a
        // static variable because all the methods called are idempotent and we
        // don't care if we do marginal amounts of extra work in unlikely corner
        // cases.
        if (null != INTERFACE_METHOD_BASIC_RULES) {
            return INTERFACE_METHOD_BASIC_RULES;
        } else {
            return INTERFACE_METHOD_BASIC_RULES = new VersionedFlagMixRuleCollection<>(
                    JAVA1, null, makeGeneralRules(), makeInterfaceMethodBaseRules());
        }
    }

    /**
     * <p>
     * Obtains the complete list of rules about which method access flags may be
     * mixed together on an interface method.
     * </p>
     *
     * <p>
     * The list returned is non-empty and its first element is equivalent to the
     * value returned by {@link #interfaceMethodBasicRules()}. The rule
     * collections in the returned list are sorted in ascending order of first
     * supporting Java class file version. Where two rule collections (one
     * "infinite" or applying across all known versions, and one "finite" or
     * applying only as far as a particular last version) have the same first
     * supporting version, the "infinite" rule precedes the "finite" one.
     * </p>
     *
     * @return All known flag mixing rules, grouped and sorted by class file
     *         version
     * @see #interfaceMethodBasicRules()
     */
    public static List<VersionedFlagMixRuleCollection<MethodAccessFlag>>
        interfaceMethodAllRules() {
        // Thread-safe despite the appearance of a race condition involving a
        // static variable because all the methods called are idempotent and we
        // don't care if we do marginal amounts of extra work in unlikely corner
        // cases.
        if (null != INTERFACE_METHOD_ALL_RULES) {
            return INTERFACE_METHOD_ALL_RULES;
        } else {
            return INTERFACE_METHOD_ALL_RULES = List.of(
                    interfaceMethodBasicRules(),
                    new VersionedFlagMixRuleCollection<>(JAVA1,
                            JAVA7, makeInterfaceMethodIncrementalRulesPreJava8()),
                    new VersionedFlagMixRuleCollection<>(JAVA8,
                            null, makeInterfaceMethodIncrementalRulesJava8Plus())
            );
        }
    }

    //
    // INTERNALS
    //

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static FlagMixRule<MethodAccessFlag>[] makeGeneralRules() {
        return new FlagMixRule[]{
                visibilityRule("a method", PUBLIC, PRIVATE, PROTECTED),
                ifFirstThenNoneOfTheRest("a method", ABSTRACT, FINAL, NATIVE,
                        PRIVATE, STATIC, STRICT, SYNCHRONIZED)
        };
    }

    private static VersionedFlagMixRuleCollection<MethodAccessFlag> CLASS_METHOD_BASIC_RULES;

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static FlagMixRule<MethodAccessFlag>[] makeInstanceInitializerRules() {
        return new FlagMixRule[]{
                noOthersThan("an instance initializer", PUBLIC, PRIVATE, PROTECTED,
                        VARARGS, STRICT, SYNTHETIC)
        };
    }

    private static VersionedFlagMixRuleCollection<MethodAccessFlag>
            INSTANCE_INITIALIZER_BASIC_INCREMENTAL_RULES;

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static FlagMixRule<MethodAccessFlag>[] makeInterfaceMethodBaseRules() {
        return new FlagMixRule[]{
                noneOf("an interface method", PROTECTED, FINAL, SYNCHRONIZED, NATIVE)
        };
    }

    private static VersionedFlagMixRuleCollection<MethodAccessFlag>
            INTERFACE_METHOD_BASIC_RULES;

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static FlagMixRule<MethodAccessFlag>[]
        makeInterfaceMethodIncrementalRulesPreJava8() {
        return new FlagMixRule[]{
                bothOf("an interface method", PUBLIC, ABSTRACT)
        };
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static FlagMixRule<MethodAccessFlag>[]
        makeInterfaceMethodIncrementalRulesJava8Plus() {
        return new FlagMixRule[]{
                exactlyOneOf("an interface method", PUBLIC, PRIVATE)
        };
    }

    private static List<VersionedFlagMixRuleCollection<MethodAccessFlag>>
            INTERFACE_METHOD_ALL_RULES;
}
