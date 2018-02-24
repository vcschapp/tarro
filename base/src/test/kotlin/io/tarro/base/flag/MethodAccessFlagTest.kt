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

package io.tarro.base.flag

import com.google.common.collect.Sets.cartesianProduct
import com.google.common.collect.Sets.powerSet
import io.tarro.base.ClassFileVersion
import io.tarro.base.ClassFileVersion.JAVA1
import io.tarro.base.ClassFileVersion.JAVA8
import io.tarro.base.flag.MethodAccessFlag.ABSTRACT
import io.tarro.base.flag.MethodAccessFlag.BRIDGE
import io.tarro.base.flag.MethodAccessFlag.FINAL
import io.tarro.base.flag.MethodAccessFlag.NATIVE
import io.tarro.base.flag.MethodAccessFlag.PRIVATE
import io.tarro.base.flag.MethodAccessFlag.PROTECTED
import io.tarro.base.flag.MethodAccessFlag.PUBLIC
import io.tarro.base.flag.MethodAccessFlag.STATIC
import io.tarro.base.flag.MethodAccessFlag.STRICT
import io.tarro.base.flag.MethodAccessFlag.SYNCHRONIZED
import io.tarro.base.flag.MethodAccessFlag.SYNTHETIC
import io.tarro.base.flag.MethodAccessFlag.VARARGS
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.endsWith
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.EnumSet
import java.util.EnumSet.copyOf
import java.util.EnumSet.noneOf
import java.util.EnumSet.of
import java.util.Optional.empty

/**
 * Unit tests for [MethodAccessFlag].
 *
 * @author Victor Schappert
 * @since 20180216
 */
class MethodAccessFlagTest :
    FlagEnumTest<MethodAccessFlag>(MethodAccessFlag::class) {

    // ==================
    // CLASS METHOD RULES
    // ==================

    @Test
    fun classMethodBasicRulesIsForJava1() {
        val rules = MethodAccessFlag.classMethodBasicRules()
        assertEquals(JAVA1, rules.firstVersionSupporting)
        assertEquals(empty<ClassFileVersion>(), rules.lastVersionSupporting)
    }

    @ParameterizedTest
    @MethodSource("classMethodBasicRulesValidFlags")
    fun classMethodBasicRulesValid(flags: EnumSet<MethodAccessFlag>) {
        MethodAccessFlag.classMethodBasicRules().rules.forEach {
            it.validate(flags)
        }
    }

    @ParameterizedTest
    @MethodSource("badVisibilityFlags")
    fun classMethodBasicRulesInvalidVisibility(flags: EnumSet<MethodAccessFlag>) {
        assertThrows(BadFlagMixException::class.java) {
            MethodAccessFlag.classMethodBasicRules().rules.forEach { it.validate(flags) }
        }
        val moreFlags = (flags + of(SYNCHRONIZED, STRICT, FINAL)).toEnumSet()
        val thrown = assertThrows(BadFlagMixException::class.java) {
            MethodAccessFlag.classMethodBasicRules().rules.forEach { it.validate(moreFlags) }
        }
        assertThat(thrown.message, endsWith("bad combination: Only one of ACC_PUBLIC, " +
                "ACC_PRIVATE, and ACC_PROTECTED is permitted on a method"))
    }

    @ParameterizedTest
    @MethodSource("badAbstractFlags")
    fun classMethodBasicRulesInvalidAbstractCombination(flags: EnumSet<MethodAccessFlag>) {
        MethodAccessFlag.classMethodBasicRules().assertBadAbstractFlags(flags)
    }

    // ==========================
    // INSTANCE INITIALIZER RULES
    // ==========================

    @Test
    fun instanceInitializerBasicIncrementalRulesIsForJava1() {
        val rules = MethodAccessFlag.instanceInitializerBasicIncrementalRules()
        assertEquals(JAVA1, rules.firstVersionSupporting)
        assertEquals(empty<ClassFileVersion>(), rules.lastVersionSupporting)
    }

    @ParameterizedTest
    @MethodSource("instanceInitializerBasicIncrementalRulesValidFlags")
    fun instanceInitializerBasicIncrementalRulesValid(flags: EnumSet<MethodAccessFlag>) {
        MethodAccessFlag.instanceInitializerBasicIncrementalRules().rules.forEach {
            it.validate(flags)
        }
        // As well as passing the instance initializer incremental rules, these
        // flags should pass the class method basic rules, since the instance
        // initializer rules are supposed to be incremental on top of the class
        // method basic rules.
        MethodAccessFlag.classMethodBasicRules().rules.forEach {
            it.validate(flags)
        }
    }

    @ParameterizedTest
    @MethodSource("badInstanceInitializerFlags")
    fun instanceInitializerBasicIncrementalRulesInvalid(flags: EnumSet<MethodAccessFlag>) {
        val thrown = assertThrows(BadFlagMixException::class.java) {
            MethodAccessFlag.instanceInitializerBasicIncrementalRules().rules.forEach {
                it.validate(flags)
            }
        }
        assertThat(thrown.message, endsWith("is a bad combination: No flag other than " +
                "ACC_PUBLIC, ACC_PRIVATE, ACC_PROTECTED, ACC_VARARGS, ACC_STRICT, or " +
                "ACC_SYNTHETIC is permitted on an instance initializer"))
    }

    // ======================
    // INTERFACE METHOD RULES
    // ======================

    @Test
    fun interfaceMethodBasicRulesIsForJava1() {
        var rules = MethodAccessFlag.interfaceMethodBasicRules()
        assertEquals(JAVA1, rules.firstVersionSupporting)
        assertEquals(empty<ClassFileVersion>(), rules.lastVersionSupporting)
        rules = MethodAccessFlag.interfaceMethodAllRules().first()
        assertEquals(JAVA1, rules.firstVersionSupporting)
        assertEquals(empty<ClassFileVersion>(), rules.lastVersionSupporting)
    }

    @ParameterizedTest
    @MethodSource("interfaceMethodBasicRulesValidFlags")
    fun interfaceMethodBasicRulesValid(flags: EnumSet<MethodAccessFlag>) {
        MethodAccessFlag.interfaceMethodBasicRules().rules.forEach { it.validate(flags) }
        MethodAccessFlag.interfaceMethodAllRules().first().rules.forEach { it.validate(flags) }
    }

    @ParameterizedTest
    @MethodSource("badVisibilityFlags")
    fun interfaceMethodBasicRulesInvalidVisibility(flags: EnumSet<MethodAccessFlag>) {
        assertThrows(BadFlagMixException::class.java) {
            MethodAccessFlag.interfaceMethodBasicRules().rules.forEach { it.validate(flags) }
        }
        val moreFlags = (flags + ABSTRACT).toEnumSet()
        val thrown = assertThrows(BadFlagMixException::class.java) {
            MethodAccessFlag.interfaceMethodBasicRules().rules.forEach { it.validate(moreFlags) }
        }
        assertThat(thrown.message, endsWith("bad combination: Only one of ACC_PUBLIC, " +
                "ACC_PRIVATE, and ACC_PROTECTED is permitted on a method"))
    }

    @ParameterizedTest
    @MethodSource("badAbstractFlags")
    fun interfaceMethodBasicRulesInvalidAbstractCombination(flags: EnumSet<MethodAccessFlag>) {
        MethodAccessFlag.interfaceMethodBasicRules().assertBadAbstractFlags(flags)
        MethodAccessFlag.interfaceMethodAllRules().first().assertBadAbstractFlags(flags)
    }

    @Test
    fun interfaceMethodBasicRulesInvalidProtected() {
        val flags = of(PROTECTED, ABSTRACT)
        MethodAccessFlag.interfaceMethodBasicRules().assertBadBasicInterfaceFlags(flags)
        MethodAccessFlag.interfaceMethodAllRules().first().assertBadBasicInterfaceFlags(flags)
    }

    @ParameterizedTest
    @MethodSource("badInterfaceFlagsAllJavaVersions")
    fun interfaceMethodBasicRulesBadInterfaceFlags(flags: EnumSet<MethodAccessFlag>) {
        MethodAccessFlag.interfaceMethodBasicRules().assertBadBasicInterfaceFlags(flags)
        MethodAccessFlag.interfaceMethodAllRules().first().assertBadBasicInterfaceFlags(flags)
    }

    @ParameterizedTest
    @MethodSource("interfaceMethodAllRulesPreJava8ValidFlags")
    fun interfaceMethodAllRulesPreJava8Valid(flags: EnumSet<MethodAccessFlag>) {
        MethodAccessFlag.interfaceMethodAllRules().preJava8Rules().forEach { it.validate(flags) }
    }

    @ParameterizedTest
    @MethodSource("badPreJava8InterfaceFlags")
    fun interfaceMethodAllRulesPreJava8InvalidMissingPublicOrAbstract(flags: EnumSet<MethodAccessFlag>) {
        // "In a class file whose version number is less than 52.0, each method
        // of an interface must have its ACC_PUBLIC and ACC_ABSTRACT flags set."
        val thrown = assertThrows(BadFlagMixException::class.java) {
            MethodAccessFlag.interfaceMethodAllRules().preJava8Rules().forEach { it.validate(flags) }
        }
        assertThat(thrown.message, endsWith(" is a bad combination: Both " +
                "ACC_PUBLIC and ACC_ABSTRACT must be set on an interface method"))
    }

    @ParameterizedTest
    @MethodSource("interfaceMethodAllRulesJava8PlusValidFlags")
    fun interfaceMethodAllRulesJava8PlusValid(flags: EnumSet<MethodAccessFlag>) {
        MethodAccessFlag.interfaceMethodAllRules().java8PlusRules().forEach { it.validate(flags) }
    }

    @ParameterizedTest
    @MethodSource("badJava8PlusInterfaceFlags")
    fun interfaceMethodAllRulesJava8PlusInvalidNotExactlyOneOfPublicOrPrivate(flags: EnumSet<MethodAccessFlag>) {
        // "In a class file whose version number is 52.0 or above, each method
        // of an interface must have exactly one of its ACC_PUBLIC and
        // ACC_PRIVATE flags set."
        val thrown = assertThrows(BadFlagMixException::class.java) {
            MethodAccessFlag.interfaceMethodAllRules().java8PlusRules().forEach { it.validate(flags) }
        }
        assertThat(thrown.message, endsWith("is a bad combination: Either ACC_PUBLIC or " +
                "ACC_PRIVATE (but not both) must be set on an interface method"))
    }

    //
    // INTERNALS
    //

    // -------------------------------------------------------------------------
    // There is no combination of method access flags that is always valid,
    // across any Java version, for class methods, instance initializers, and
    // interface methods alike.
    //
    // As an example of why this is so, before class file version 52.0, all
    // interface methods had to have ACC_ABSTRACT set. But instance initializers
    // may never have ACC_ABSTRACT set! So a flag combination that was required
    // for interfaces prior to Java 8 is prohibited for instance initializers.
    // There are many other examples.
    // -------------------------------------------------------------------------

    private fun classMethodBasicRulesValidFlags() = listOf<EnumSet<MethodAccessFlag>>(
            // INSTANCE methods.
            noneOf(MethodAccessFlag::class.java),
            of(FINAL), of(FINAL, SYNCHRONIZED), of(FINAL, SYNCHRONIZED, NATIVE), of(FINAL, SYNCHRONIZED, NATIVE, STRICT),
            of(SYNCHRONIZED), of(SYNCHRONIZED, NATIVE), of(SYNCHRONIZED, NATIVE, STRICT),
            of(NATIVE), of(NATIVE, STRICT),
            of(STRICT),
            of(ABSTRACT),

            // STATIC methods.
            of(STATIC),
            of(STATIC, FINAL), of(STATIC, FINAL, SYNCHRONIZED), of(STATIC, FINAL, SYNCHRONIZED, NATIVE),
                of(STATIC, FINAL, SYNCHRONIZED, NATIVE, STRICT),
            of(STATIC, SYNCHRONIZED), of(STATIC, SYNCHRONIZED, NATIVE), of(STATIC, SYNCHRONIZED, NATIVE, STRICT),
            of(STATIC, NATIVE), of(STATIC, NATIVE, STRICT),
            of(STATIC, STRICT)

    ).augmentWith(BRIDGE, SYNTHETIC, VARARGS)
     .withEachVisibility()
     .filter { !it.containsAll(listOf(PRIVATE, ABSTRACT))  }

    private fun instanceInitializerBasicIncrementalRulesValidFlags() = powerSet(of(VARARGS, STRICT, SYNTHETIC))
            .map { it.toEnumSet() }
            .withEachVisibility()

    private fun interfaceMethodBasicRulesValidFlags() = listOf(
            of(PUBLIC, ABSTRACT), of(PUBLIC, STATIC), of(PUBLIC, STATIC, STRICT),
            of(PRIVATE, STATIC), of(PRIVATE,  STATIC, STRICT)
    ).augmentWith(BRIDGE, VARARGS, SYNTHETIC)
     .map { it.toEnumSet() }

    private fun interfaceMethodAllRulesPreJava8ValidFlags() = listOf(
        of(PUBLIC, ABSTRACT)
    ).augmentWith(BRIDGE, SYNTHETIC, VARARGS)

    private fun interfaceMethodAllRulesJava8PlusValidFlags() = listOf(
        of(PUBLIC, ABSTRACT)
    ).augmentWith(BRIDGE, SYNTHETIC, VARARGS)

    private fun badVisibilityFlags() = arrayOf(
            of(PUBLIC, PRIVATE), of(PUBLIC, PROTECTED),
            of(PRIVATE, PROTECTED), of(PUBLIC, PRIVATE, PROTECTED)
    )

    private fun badAbstractFlags() = cartesianProduct(
            of(ABSTRACT),
            of(PRIVATE, STATIC, FINAL, SYNCHRONIZED, NATIVE, STRICT)
    ).map { it.toEnumSet() }

    private fun badInstanceInitializerFlags() = listOf(
            of(STATIC), of(FINAL), of(SYNCHRONIZED), of(BRIDGE), of(NATIVE), of(ABSTRACT),
            of(STATIC, FINAL, SYNCHRONIZED, BRIDGE, NATIVE, ABSTRACT)
    ).augmentWith(VARARGS, STRICT, SYNTHETIC).withEachVisibility()

    private fun badInterfaceFlagsAllJavaVersions() = powerSet(of(FINAL, SYNCHRONIZED, NATIVE))
            .noEmptySet()
            .map { it.toEnumSet() }
            .withPublicOrPrivate()

    private fun badPreJava8InterfaceFlags() = arrayOf(
            of(PUBLIC), of(ABSTRACT),
            of(PUBLIC, SYNTHETIC), of(PUBLIC, VARARGS), of(PUBLIC, BRIDGE),
            of(PUBLIC, SYNTHETIC, VARARGS, BRIDGE),
            of(ABSTRACT, SYNTHETIC), of(ABSTRACT, VARARGS), of(ABSTRACT, BRIDGE),
            of(ABSTRACT, SYNTHETIC, VARARGS, BRIDGE)
    )

    private fun badJava8PlusInterfaceFlags() = listOf(
            of(ABSTRACT), of(STATIC), of(STATIC, STRICT)
    ).augmentWith(BRIDGE, SYNTHETIC, VARARGS)

    private fun VersionedFlagMixRuleCollection<MethodAccessFlag>.assertBadAbstractFlags(
            flags: EnumSet<MethodAccessFlag>) {
        val thrown = assertThrows(BadFlagMixException::class.java) {
            rules.forEach {  it.validate(flags) }
        }
        assertThat(thrown.message, endsWith("is a bad combination: If ACC_ABSTRACT is present " +
                "on a method, then none of ACC_FINAL, ACC_NATIVE, ACC_PRIVATE, ACC_STATIC, " +
                "ACC_STRICT, or ACC_SYNCHRONIZED is permitted"))
    }

    private fun VersionedFlagMixRuleCollection<MethodAccessFlag>.assertBadBasicInterfaceFlags(
            flags: EnumSet<MethodAccessFlag>) {
        val thrown = assertThrows(BadFlagMixException::class.java) { rules.forEach { it.validate(flags) } }
        assertThat(thrown.message, endsWith("is a bad combination: None of ACC_PROTECTED, " +
                "ACC_FINAL, ACC_SYNCHRONIZED, or ACC_NATIVE is permitted on an interface " +
                "method"))
    }

    private fun <U: Set<MethodAccessFlag>> Iterable<U>.noEmptySet() = filter { it.isNotEmpty() }

    private fun Collection<MethodAccessFlag>.toEnumSet() = when {
        isNotEmpty() -> copyOf(this)
        else -> noneOf(MethodAccessFlag::class.java)
    }

    private fun Iterable<EnumSet<MethodAccessFlag>>.augmentWith(vararg flags: MethodAccessFlag) = powerSet(setOf(*flags))
            .flatMap { augmentationSet ->  this.map { it + augmentationSet } }
            .map { it.toEnumSet() }

    private fun Iterable<EnumSet<MethodAccessFlag>>.withEachVisibility() = flatMap {
        listOf(it, it + PUBLIC, it + PRIVATE, it + PROTECTED)
    }.map { it.toEnumSet() }

    private fun Iterable<EnumSet<MethodAccessFlag>>.withPublicOrPrivate() = flatMap {
        listOf(it + PUBLIC, it + PRIVATE)
    }.map { it.toEnumSet() }

    private fun List<VersionedFlagMixRuleCollection<MethodAccessFlag>>.preJava8Rules() = filter {
        !it.lastVersionSupporting.isPresent ||
                it.lastVersionSupporting.get() < JAVA8
    }.flatMap { it.rules }

    private fun List<VersionedFlagMixRuleCollection<MethodAccessFlag>>.java8PlusRules() = filter {
        it.firstVersionSupporting <= JAVA8 &&
                (!it.lastVersionSupporting.isPresent || it.lastVersionSupporting == JAVA8)
    }.flatMap { it.rules }
}

