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

import io.tarro.base.ClassFileVersion
import io.tarro.base.ClassFileVersion.JAVA1
import io.tarro.base.ClassFileVersion.JAVA5
import io.tarro.base.ClassFileVersion.JAVA6
import io.tarro.base.ClassFileVersion.JAVA9
import io.tarro.base.flag.ClassAccessFlag.ABSTRACT
import io.tarro.base.flag.ClassAccessFlag.ANNOTATION
import io.tarro.base.flag.ClassAccessFlag.ENUM
import io.tarro.base.flag.ClassAccessFlag.FINAL
import io.tarro.base.flag.ClassAccessFlag.INTERFACE
import io.tarro.base.flag.ClassAccessFlag.MODULE
import io.tarro.base.flag.ClassAccessFlag.PUBLIC
import io.tarro.base.flag.ClassAccessFlag.SUPER
import io.tarro.base.flag.ClassAccessFlag.SYNTHETIC
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.endsWith
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.*
import java.util.EnumSet.noneOf
import java.util.EnumSet.of
import java.util.Optional.empty

/**
 * Unit tests for [ClassAccessFlag].
 *
 * @author Victor Schappert
 * @since 20180203
 */
class ClassAccessFlagTest :
    FlagEnumTest<ClassAccessFlag>(ClassAccessFlag::class) {

    @ParameterizedTest
    @MethodSource("versions")
    fun validAcrossAllVersions(version: ClassFileVersion) {
        rulesForVersion(version).forEach { rule ->
            alwaysValidFlags().forEach { flags ->
                rule.validate(flags)
            }
        }
    }

    @Test
    fun basicRulesIsForJava1() {
        assertEquals(JAVA1, ClassAccessFlag.basicRules().firstVersionSupporting)
        assertEquals(empty<ClassFileVersion>(), ClassAccessFlag.basicRules()
                .lastVersionSupporting)
        assertEquals(JAVA1, ClassAccessFlag.allRules().first().firstVersionSupporting)
        assertEquals(empty<ClassFileVersion>(), ClassAccessFlag.allRules()
                .first().lastVersionSupporting)
    }

    @ParameterizedTest
    @MethodSource("basicRulesValidFlags")
    fun basicRulesValid(flags: EnumSet<ClassAccessFlag>) {
        ClassAccessFlag.basicRules().rules.forEach { it.validate(flags) }
        ClassAccessFlag.allRules().first().rules.forEach {  it.validate(flags) }
    }

    @Test
    fun basicRulesValidBecauseFlagsDidNotExistYet() {
        val flags = of(SYNTHETIC, ENUM, ANNOTATION, MODULE)
        ClassAccessFlag.basicRules().rules.forEach { it.validate(flags) }
        ClassAccessFlag.allRules().first().rules.forEach {  it.validate(flags) }
    }

    @Test
    fun basicRulesInvalidNotBothOfFinalAndAbstract() {
        val message = "ACC_FINAL and ACC_ABSTRACT may not both be set on a class"
        assertAll(
                Executable {
                    val t = assertThrows(BadAccessFlagMixException::class.java) {
                        ClassAccessFlag.basicRules().rules.forEach {
                            it.validate(of(FINAL, ABSTRACT))
                        }
                    }
                    assertThat(t.message, endsWith(message))
                },
                Executable {
                    val t = assertThrows(BadAccessFlagMixException::class.java) {
                        ClassAccessFlag.allRules().first().rules.forEach {
                            it.validate(of(FINAL, ABSTRACT))
                        }
                    }
                    assertThat(t.message, endsWith(message))
                }
        )
    }

    @ParameterizedTest
    @MethodSource("badFlagsInterfaceWithFinalOrSuper")
    fun basicRulesInvalidInterfaceCantHaveFinalOrSuper(flags: EnumSet<ClassAccessFlag>) {
        val message = "If ACC_INTERFACE is present on a class, then none of ACC_FINAL or ACC_SUPER is permitted"
        assertAll(
                Executable {
                    val t = assertThrows(BadAccessFlagMixException::class.java) {
                        ClassAccessFlag.basicRules().rules.forEach { it.validate(flags) }
                    }
                    assertThat(t.message, endsWith(message))
                },
                Executable {
                    val t = assertThrows(BadAccessFlagMixException::class.java) {
                        ClassAccessFlag.allRules().first().rules.forEach {
                            it.validate(flags)
                        }
                    }
                    assertThat(t.message, endsWith(message))
                }
        )
    }

    @ParameterizedTest
    @MethodSource("badFlagsAnnotationMustBeInterface")
    fun incrementalRulesForJava5InvalidAnnotationMustBeInterface(flags: EnumSet<ClassAccessFlag>) {
        val message = "If ACC_ANNOTATION is set on a class, then ACC_INTERFACE must also be set"
        val t = assertThrows(BadAccessFlagMixException::class.java) {
            rulesForVersion(JAVA5).forEach { it.validate(flags) }
        }
        assertThat(t.message, endsWith(message))
    }

    @ParameterizedTest
    @MethodSource("badFlagsNotBothEnumAndInterface")
    fun incrementalRulesForJava5NotBothEnumAndInterface(flags: EnumSet<ClassAccessFlag>) {
        val message = "ACC_INTERFACE and ACC_ENUM may not both be set on a class"
        val t = assertThrows(BadAccessFlagMixException::class.java) {
            rulesForVersion(JAVA5).forEach { it.validate(flags) }
        }
        assertThat(t.message, endsWith(message))
    }

    @ParameterizedTest
    @MethodSource("badFlagsInterfaceMustBeAbstract")
    fun incrementalRulesForJava6InvalidInterfaceMustBeAbstract(flags: EnumSet<ClassAccessFlag>) {
        val message = "If ACC_INTERFACE is set on a class, then ACC_ABSTRACT must also be set"
        val t = assertThrows(BadAccessFlagMixException::class.java) {
            rulesForVersion(JAVA6).forEach { it.validate(flags) }
        }
        assertThat(t.message, endsWith(message))
    }


    @ParameterizedTest
    @MethodSource("badFlagsModuleMustBeAlone")
    fun incrementalRulesForJava9InvalidModuleMustBeAlone(flags: EnumSet<ClassAccessFlag>) {
        val message = "If ACC_MODULE is set no other access_flags may be set at class level"
        val t = assertThrows(BadAccessFlagMixException::class.java) {
            rulesForVersion(JAVA9).forEach { it.validate(flags) }
        }
        assertThat(t.message, endsWith(message))
    }

    //
    // INTERNALS
    //

    private fun rulesForVersion(version: ClassFileVersion) =
            ClassAccessFlag.allRules().find {
                it.firstVersionSupporting == version
            }!!.rules

    private fun versions() = ClassAccessFlag.allRules().map { it.firstVersionSupporting }

    private fun alwaysValidFlags() = arrayOf<EnumSet<ClassAccessFlag>>(
        // Combinations available in Java 1 which are valid in Java 1 and all
        // subsequent Java versions.
        noneOf(ClassAccessFlag::class.java),
        of(PUBLIC),
        of(FINAL),
        of(SUPER),
        of(ABSTRACT),
        of(PUBLIC, FINAL),
        of(PUBLIC, SUPER),
        of(PUBLIC, ABSTRACT),
        of(PUBLIC, FINAL, SUPER),
        of(PUBLIC, ABSTRACT, SUPER),
        of(INTERFACE, ABSTRACT),
        of(PUBLIC, INTERFACE, ABSTRACT),
        // Combinations of flags available in versions later than Java 1 which
        // are valid at the version in which they become available and all
        // later versions AND which are valid according to Java 1 rules since
        // they only add additional flags which did not exist in Java 1.
        of(SYNTHETIC),
        of(PUBLIC, SYNTHETIC),
        of(FINAL, SYNTHETIC),
        of(SUPER, SYNTHETIC),
        of(ABSTRACT, SYNTHETIC),
        of(PUBLIC, FINAL, SYNTHETIC),
        of(PUBLIC, SUPER, SYNTHETIC),
        of(PUBLIC, ABSTRACT, SYNTHETIC),
        of(PUBLIC, FINAL, SUPER, SYNTHETIC),
        of(PUBLIC, ABSTRACT, SUPER, SYNTHETIC),
        of(INTERFACE, ABSTRACT, SYNTHETIC),
        of(PUBLIC, INTERFACE, ABSTRACT, SYNTHETIC),
        // --------
        of(ENUM),
        of(PUBLIC, ENUM),
        of(PUBLIC, FINAL, ENUM),
        of(ENUM, SYNTHETIC),
        of(PUBLIC, ENUM, SYNTHETIC),
        of(PUBLIC, FINAL, ENUM, SYNTHETIC),
        // --------
        of(ANNOTATION, INTERFACE, ABSTRACT),
        of(PUBLIC, ANNOTATION, INTERFACE, ABSTRACT),
        of(ANNOTATION, INTERFACE, ABSTRACT, SYNTHETIC),
        of(PUBLIC, ANNOTATION, INTERFACE, ABSTRACT, SYNTHETIC),
        // --------
        of(MODULE)
    )

    private fun basicRulesValidFlags() = alwaysValidFlags() + arrayOf<EnumSet<ClassAccessFlag>>(
        of(INTERFACE),
        of(PUBLIC, INTERFACE),
        // Same combinations plus ACC_SYNTHETIC, since flagging something
        // SYNTHETIC is always valid.
        of(INTERFACE, SYNTHETIC),
        of(PUBLIC, INTERFACE, SYNTHETIC)
    )

    private fun badFlagsInterfaceWithFinalOrSuper() = arrayOf<EnumSet<ClassAccessFlag>>(
        of(INTERFACE, FINAL),
        of(INTERFACE, SUPER),
        of(INTERFACE, FINAL, SUPER),
        of(PUBLIC, INTERFACE, FINAL),
        of(PUBLIC, INTERFACE, SUPER),
        of(PUBLIC, INTERFACE, FINAL, SUPER),
        of(PUBLIC, INTERFACE, ABSTRACT, SUPER),
        // Add ACC_SYNTHETIC in since it's always valid.
        of(INTERFACE, FINAL, SYNTHETIC),
        of(INTERFACE, SUPER, SYNTHETIC),
        of(INTERFACE, FINAL, SUPER, SYNTHETIC),
        of(PUBLIC, INTERFACE, FINAL, SYNTHETIC),
        of(PUBLIC, INTERFACE, SUPER, SYNTHETIC),
        of(PUBLIC, INTERFACE, FINAL, SUPER, SYNTHETIC),
        of(PUBLIC, INTERFACE, ABSTRACT, SUPER, SYNTHETIC)
    )

    private fun badFlagsAnnotationMustBeInterface() = arrayOf<EnumSet<ClassAccessFlag>>(
        of(ANNOTATION),
        of(PUBLIC, ANNOTATION),
        of(PUBLIC, ABSTRACT, ANNOTATION),
        of(ANNOTATION, SYNTHETIC),
        of(PUBLIC, ANNOTATION, SYNTHETIC),
        of(PUBLIC, ABSTRACT, ANNOTATION, SYNTHETIC)
    )

    private fun badFlagsNotBothEnumAndInterface() = arrayOf<EnumSet<ClassAccessFlag>>(
            of(ENUM, INTERFACE),
            of(PUBLIC, ENUM, INTERFACE),
            of(ENUM, INTERFACE, SYNTHETIC),
            of(PUBLIC, ENUM, INTERFACE, SYNTHETIC)
    )

    private fun badFlagsInterfaceMustBeAbstract() = arrayOf<EnumSet<ClassAccessFlag>>(
            of(INTERFACE),
            of(PUBLIC, INTERFACE),
            of(ANNOTATION, INTERFACE),
            of(PUBLIC, ANNOTATION, INTERFACE),
            of(INTERFACE, SYNTHETIC),
            of(PUBLIC, INTERFACE, SYNTHETIC),
            of(ANNOTATION, INTERFACE, SYNTHETIC),
            of(PUBLIC, ANNOTATION, INTERFACE, SYNTHETIC)
    )

    private fun badFlagsModuleMustBeAlone() = arrayOf<EnumSet<ClassAccessFlag>>(
            of(MODULE, PUBLIC),
            of(MODULE, FINAL),
            of(MODULE, SUPER),
            of(MODULE, ABSTRACT),
            of(MODULE, ENUM),
            of(MODULE, SYNTHETIC),
            of(MODULE, PUBLIC, FINAL),
            of(MODULE, PUBLIC, FINAL, SUPER, ABSTRACT, ENUM, SYNTHETIC)
    )
}
