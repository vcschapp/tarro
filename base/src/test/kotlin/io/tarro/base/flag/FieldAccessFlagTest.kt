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

import java.util.*
import java.util.EnumSet.of
import io.tarro.base.flag.FieldAccessFlag.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.endsWith
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.function.Executable
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.EnumSet.noneOf

/**
 * Unit tests for [FieldAccessFlag].
 *
 * @author Victor Schappert
 * @since 20180203
 */
class FieldAccessFlagTest : FlagEnumTest<FieldAccessFlag>(FieldAccessFlag::class) {

    @ParameterizedTest
    @MethodSource("classFieldsValidFlags")
    fun classFieldsValidFlagsVisibilityDefault(flags: EnumSet<FieldAccessFlag>) {
        FieldAccessFlag.classFieldRules().forEach {
            it.validate(flags)
            it.validate(flags + SYNTHETIC)
        }
    }

    @ParameterizedTest
    @MethodSource("classFieldsValidFlags")
    fun classFieldsValidFlagsVisibilityPublic(flags: EnumSet<FieldAccessFlag>) {
        FieldAccessFlag.classFieldRules().forEach {
            it.validate(flags + PUBLIC)
            it.validate(flags + PUBLIC + SYNTHETIC)
        }
    }

    @ParameterizedTest
    @MethodSource("classFieldsValidFlags")
    fun classFieldsValidFlagsVisibilityPrivate(flags: EnumSet<FieldAccessFlag>) {
        FieldAccessFlag.classFieldRules().forEach {
            it.validate(flags + PRIVATE)
            it.validate(flags + PRIVATE + SYNTHETIC)
        }
    }

    @ParameterizedTest
    @MethodSource("classFieldsValidFlags")
    fun classFieldsValidFlagsVisibilityProtected(flags: EnumSet<FieldAccessFlag>) {
        FieldAccessFlag.classFieldRules().forEach {
            it.validate(flags + PROTECTED)
            it.validate(flags + PROTECTED + SYNTHETIC)
        }
    }

    @ParameterizedTest
    @MethodSource("classFieldsBadFlagsVisibility")
    fun classFieldsBadFlagsVisibility(flags: EnumSet<FieldAccessFlag>) {
        val message = "Only one of ACC_PUBLIC, ACC_PRIVATE, and ACC_PROTECTED is permitted on a field"
        val t = assertThrows(BadAccessFlagMixException::class.java) {
            FieldAccessFlag.classFieldRules().forEach {
                it.validate(flags)
            }
        }
        assertThat(t.message, endsWith(message))
    }

    @ParameterizedTest
    @MethodSource("classFieldsBadFlagsFinalAndVolatile")
    fun classFieldsBadFlagsFinalAndVolatile(flags: EnumSet<FieldAccessFlag>) {
        val message = "ACC_FINAL and ACC_VOLATILE may not both be set on a field"
        val combinations = listOf(flags, flags + PUBLIC, flags + PRIVATE, flags + PROTECTED)
                .flatMap { listOf(it, it + SYNTHETIC) }
        val assertions = combinations.map { innerFlags ->
            Executable {
                val t = assertThrows(BadAccessFlagMixException::class.java) {
                    FieldAccessFlag.classFieldRules().forEach { rule ->
                        rule.validate(innerFlags)
                    }
                }
                assertThat(t.message, endsWith(message))
            }
        }.toTypedArray()
        assertAll(*assertions)
    }

    @ParameterizedTest
    @MethodSource("interfaceFieldsValidFlags")
    fun interfaceFieldsValidFlagsVisibilityDefault(flags: EnumSet<FieldAccessFlag>) {
        FieldAccessFlag.interfaceFieldRules().forEach { it.validate(flags) }
    }

    @ParameterizedTest
    @MethodSource("interfaceFieldsBadFlagsMissingRequired")
    fun interfaceFieldsBadFlagsMissingRequired(flags: EnumSet<FieldAccessFlag>) {
        val message = "All of ACC_PUBLIC, ACC_STATIC, and ACC_FINAL must be set on an interface field"
        val t = assertThrows(BadAccessFlagMixException::class.java) {
            FieldAccessFlag.interfaceFieldRules().forEach {
                it.validate(flags)
            }
        }
        assertThat(t.message, endsWith(message))
    }

    @ParameterizedTest
    @MethodSource("interfaceFieldsBadFlagsHasNotAllowed")
    fun interfaceFieldsBadFlagsHasNotAllowed(flags: EnumSet<FieldAccessFlag>) {
        val message = "None of ACC_PRIVATE, ACC_PROTECTED, ACC_VOLATILE, ACC_TRANSIENT, or " +
                "ACC_ENUM is permitted on an interface field"
        val t = assertThrows(BadAccessFlagMixException::class.java) {
            FieldAccessFlag.interfaceFieldRules().forEach {
                it.validate(flags)
            }
        }
        assertThat(t.message, endsWith(message))
    }

    //
    // INTERNALS
    //

    private operator fun EnumSet<FieldAccessFlag>.plus(flag: FieldAccessFlag) :
            EnumSet<FieldAccessFlag> {
        return of<FieldAccessFlag>(flag,
                *this.toArray(arrayOfNulls<FieldAccessFlag>(this.size)))
    }

    private fun classFieldsValidFlags() = arrayOf<EnumSet<FieldAccessFlag>>(
            // ----------------
            // Omit visibility flags and synthetic since we can easily mix those
            // in programmatically.
            // ----------------
            noneOf(FieldAccessFlag::class.java),
            of(STATIC),
            of(FINAL),
            of(VOLATILE),
            of(TRANSIENT),
            // ----------------
            of(TRANSIENT, FINAL),
            of(TRANSIENT, VOLATILE),
            // ----------------
            of(STATIC, FINAL),
            of(STATIC, VOLATILE),
            of(STATIC, TRANSIENT),
            of(STATIC, TRANSIENT, FINAL),
            of(STATIC, TRANSIENT, VOLATILE)
    )

    private fun classFieldsBadFlagsVisibility() = arrayOf<EnumSet<FieldAccessFlag>>(
            of(PUBLIC, PRIVATE),
            of(PUBLIC, PROTECTED),
            of(PUBLIC, PRIVATE),
            of(PUBLIC, PRIVATE, PROTECTED),
            of(PRIVATE, PROTECTED),
            of(PUBLIC, PRIVATE, STATIC),
            of(PUBLIC, PROTECTED, FINAL),
            of(PUBLIC, PRIVATE, TRANSIENT),
            of(PUBLIC, PRIVATE, PROTECTED, VOLATILE),
            of(PRIVATE, PROTECTED, ENUM),
            of(PRIVATE, PUBLIC, TRANSIENT, VOLATILE, ENUM),
            of(PRIVATE, PUBLIC, TRANSIENT, VOLATILE, ENUM, SYNTHETIC)
    )

    private fun classFieldsBadFlagsFinalAndVolatile() = arrayOf<EnumSet<FieldAccessFlag>>(
            of(FINAL, VOLATILE),
            of(STATIC, FINAL, VOLATILE),
            of(ENUM, FINAL, VOLATILE)
    )

    private fun interfaceFieldsValidFlags() = arrayOf<EnumSet<FieldAccessFlag>>(
            of(PUBLIC, STATIC, FINAL),
            of(PUBLIC, STATIC, FINAL, SYNTHETIC)
    )

    private fun interfaceFieldsBadFlagsMissingRequired() = arrayOf<EnumSet<FieldAccessFlag>>(
            noneOf(FieldAccessFlag::class.java),
            of(PUBLIC),
            of(STATIC),
            of(FINAL),
            of(PUBLIC, STATIC),
            of(PUBLIC, FINAL),
            of(STATIC, FINAL)
    )

    private fun interfaceFieldsBadFlagsHasNotAllowed() = arrayOf<EnumSet<FieldAccessFlag>>(
            of(PUBLIC, STATIC, FINAL, PRIVATE),
            of(PUBLIC, STATIC, FINAL, PROTECTED),
            of(PUBLIC, STATIC, FINAL, VOLATILE),
            of(PUBLIC, STATIC, FINAL, TRANSIENT),
            of(PUBLIC, STATIC, FINAL, ENUM),
            of(PUBLIC, STATIC, FINAL, PRIVATE),
            of(PUBLIC, STATIC, FINAL, PROTECTED),
            of(PUBLIC, STATIC, FINAL, TRANSIENT),
            of(PUBLIC, STATIC, FINAL, ENUM, SYNTHETIC)
    )
}
