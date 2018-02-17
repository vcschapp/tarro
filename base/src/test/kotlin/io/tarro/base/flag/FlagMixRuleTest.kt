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

import com.google.common.collect.Lists.cartesianProduct
import com.google.common.collect.Sets
import com.google.common.collect.Sets.combinations
import com.google.common.collect.Sets.powerSet
import com.google.common.collect.Sets.union
import io.tarro.base.flag.FakeFlag.BAR
import io.tarro.base.flag.FakeFlag.BAZ
import io.tarro.base.flag.FakeFlag.FOO
import org.hamcrest.CoreMatchers.endsWith
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.EnumSet
import java.util.EnumSet.allOf
import java.util.EnumSet.complementOf
import java.util.EnumSet.copyOf
import java.util.EnumSet.noneOf
import java.util.EnumSet.of

/**
 * Unit tests for [FlagMixRule].
 *
 * @author Victor Schappert
 * @since 20180205
 */
class FlagMixRuleTest {
    @Test
    fun listifyOneArray() {
        val array = arrayOf(alwaysBadRule, alwaysGoodRule)
        val list = FlagMixRule.listify(array)
        assertArrayEquals(array, list.toTypedArray())
    }

    @Test
    fun listifyVarArgsArrays() {
        val array = arrayOf(alwaysBadRule, alwaysGoodRule)
        val list = FlagMixRule.listify(array.sliceArray(0..0), array.sliceArray(1..1))
        assertArrayEquals(array, list.toTypedArray())
    }

    @Test
    fun visibilityRule() {
        val rule = FlagMixRule.visibilityRule(ENTITY_NAME, FOO, BAR, BAZ)
        rule.validate(noneOf(FakeFlag::class.java))
        arrayOf(FOO, BAR, BAZ).forEach { rule.validate(of(it)) }
        val suffix = ": Only one of FOO, BAR, and BAZ is permitted on ${ENTITY_NAME}"
        val assertions = arrayOf(
                of(FOO, BAR), of(FOO, BAZ), of(BAR, BAZ),
                of(FOO, BAR, BAZ)
        ).map {
            Executable {
                val caught = assertThrows(BadFlagMixException::class.java) {
                    rule.validate(it)
                }
                assertThat(caught.message, endsWith(suffix))
            }
        }
        assertAll(*assertions.toTypedArray())
    }

    @Test
    fun bothOf() {
        val rule = FlagMixRule.bothOf(ENTITY_NAME, FOO, BAR)
        rule.validate(of(FOO, BAR))
        rule.validate(of(FOO, BAR, BAZ))
        val suffix = ": Both FOO and BAR must be set on ${ENTITY_NAME}"
        val assertions = arrayOf(
                noneOf(FakeFlag::class.java), of(FOO), of(BAR), of(BAZ),
                of(FOO, BAZ), of(BAR, BAZ)
        ).map {
            Executable {
                val caught = assertThrows(BadFlagMixException::class.java) {
                    rule.validate(it)
                }
                assertThat(caught.message, endsWith(suffix))
            }
        }
        assertAll(*assertions.toTypedArray())
    }

    @ParameterizedTest
    @MethodSource("allOfSource")
    fun allOf(flags: EnumSet<FakeFlag>, substring: String) {
        val rule = FlagMixRule.allOf(ENTITY_NAME, *flags.toTypedArray())
        // A set made of all of the given flags must pass the rule.
        rule.validate(flags)
        // A set made of all possible flags must pass the rule.
        rule.validate(allOf(FakeFlag::class.java))
        // The rule should fail for every possible combination formed by
        // crossing:
        //     1. every possible subset of the flags that are in the rule, minus
        //        the set of all flags in the rule; with
        //     2. every possible subset of the flags that are NOT in the rule.
        val subsetsInRule = (0 until flags.size).flatMap {
            combinations(flags, it)
        }
        val unusedFlags = complementOf(flags)
        val subsetsNotInRule = (0..unusedFlags.size).flatMap {
            combinations(unusedFlags, it)
        }
        val assertions = cartesianProduct(subsetsInRule, subsetsNotInRule)
                .map { union(it.first(), it.last()) }
                .map { it.toEnumSet() }
                .map {
                    Executable {
                        val thrown = assertThrows(BadFlagMixException::class.java) {
                            rule.validate(it)
                        }
                        assertThat(thrown.message, endsWith(": All of $substring must be set" +
                                " on $ENTITY_NAME"))
                    }
                }
        assertAll(*assertions.toTypedArray())
    }

    @ParameterizedTest
    @MethodSource("allPairsSource")
    fun exactlyOneOf(firstFlag: FakeFlag, secondFlag: FakeFlag) {
        val rule = FlagMixRule.exactlyOneOf(ENTITY_NAME, firstFlag, secondFlag)
        rule.validate(of(firstFlag))
        rule.validate(of(secondFlag))
        val assertions = listOf(noneOf(FakeFlag::class.java),
                of(firstFlag, secondFlag),
                allOf(FakeFlag::class.java)).map {
            Executable {
                val thrown = assertThrows(BadFlagMixException::class.java) {
                    rule.validate(it)
                }
                assertThat(thrown.message, endsWith(": Either $firstFlag or $secondFlag " +
                        "(but not both) must be set on $ENTITY_NAME"))
            }
        }
        assertAll(*assertions.toTypedArray())
    }

    @ParameterizedTest
    @MethodSource("allPairsAndTriplesSource")
    fun noneOf(flags: EnumSet<FakeFlag>) {
        val rule = FlagMixRule.noneOf(ENTITY_NAME, *flags.toTypedArray())
        rule.validate(complementOf(flags))
        val assertions = powerSet(flags)
                .filter { it.isNotEmpty() }
                .map { it.toEnumSet() }
                .map {
                    Executable {
                        val thrown = assertThrows(BadFlagMixException::class.java){
                            rule.validate(it)
                        }
                        assertTrue(Regex(": None of [A-Z]+(,( [A-Z]+,)*)? or [A-Z]+ is " +
                                "permitted on $ENTITY_NAME$").containsMatchIn(thrown
                                .message!!), thrown.message)
                    }
                }
        assertAll(*assertions.toTypedArray())
    }

    @ParameterizedTest
    @MethodSource("allPairsSource")
    fun notBothOf(firstFlag: FakeFlag, secondFlag: FakeFlag) {
        val rule = FlagMixRule.notBothOf(ENTITY_NAME, firstFlag, secondFlag)
        rule.validate(noneOf(FakeFlag::class.java))
        rule.validate(of(firstFlag))
        rule.validate(of(secondFlag))
        rule.validate(allOf(FakeFlag::class.java).minus(firstFlag).toEnumSet())
        rule.validate(allOf(FakeFlag::class.java).minus(secondFlag).toEnumSet())
        val assertions = listOf(of(firstFlag, secondFlag), allOf(FakeFlag::class.java))
                .map {
                    Executable {
                        val thrown = assertThrows(BadFlagMixException::class.java) {
                            rule.validate(it)
                        }
                        assertThat(thrown.message, endsWith(": $firstFlag and $secondFlag " +
                                "may not both be set on $ENTITY_NAME"))
                    }
                }
        assertAll(*assertions.toTypedArray())
    }

    @ParameterizedTest
    @MethodSource("allPairsAndTriplesSource")
    fun noOthersThan(flags: EnumSet<FakeFlag>) {
        val rule = FlagMixRule.noOthersThan(ENTITY_NAME, *flags.toTypedArray())
        powerSet(flags)
                .map { it.toEnumSet() }
                .forEach { rule.validate(it) }
        val assertions = powerSet(complementOf(flags))
                .filter { it.isNotEmpty() }
                .map { it.toEnumSet() }
                .map {
                    Executable {
                        val thrown = assertThrows(BadFlagMixException::class.java) {
                            rule.validate(it)
                        }
                        assertThat(thrown.message, endsWith(": No flag other than " +
                                "${flags.first()} or ${flags.last()} is permitted on " +
                                ENTITY_NAME))
                    }
                }
        assertions.takeIf { it.isNotEmpty() }?.let { assertAll(*it.toTypedArray()) }
    }

    @ParameterizedTest
    @MethodSource("allPairsSource")
    fun ifFirstThenAlsoSecond(firstFlag: FakeFlag, secondFlag: FakeFlag) {
        val rule = FlagMixRule.ifFirstThenAlsoSecond(ENTITY_NAME, firstFlag, secondFlag)
        rule.validate(allOf(FakeFlag::class.java))
        rule.validate(of(firstFlag, secondFlag))
        powerSet(allOf(FakeFlag::class.java))
                .filter { !it.contains(firstFlag) }
                .map { it.toEnumSet() }
                .forEach { rule.validate(it) }
        val assertions = powerSet(allOf(FakeFlag::class.java).minus(secondFlag))
                .filter { it.contains(firstFlag) }
                .map { it.toEnumSet() }
                .map {
                    Executable {
                        val thrown = assertThrows(BadFlagMixException::class.java) {
                            rule.validate(it)
                        }
                        assertThat(thrown.message, endsWith(": If $firstFlag is set on " +
                                "$ENTITY_NAME, then $secondFlag must also be set"))
                    }
                }
        assertAll(*assertions.toTypedArray())
    }

    @ParameterizedTest
    @MethodSource("ifFirstThenNoneOfTheRestSource")
    fun ifFirstThenNoneOfTheRest(firstFlag: FakeFlag,
                                 consequentlyNotPermittedFlags: EnumSet<FakeFlag>) {
        val rule = FlagMixRule.ifFirstThenNoneOfTheRest(ENTITY_NAME, firstFlag,
                *consequentlyNotPermittedFlags.toTypedArray())
        rule.validate(of(firstFlag))
        powerSet(consequentlyNotPermittedFlags)
                .map { it.toEnumSet() }
                .forEach { rule.validate(it) }
        val assertions = powerSet(consequentlyNotPermittedFlags).minusElement(emptySet())
                .map { union(of(firstFlag), it) }
                .map { it.toEnumSet() }
                .map {
                    Executable {
                        val thrown = assertThrows(BadFlagMixException::class.java) {
                            rule.validate(it)
                        }
                        assertThat(thrown.message, endsWith(": If $firstFlag is present on " +
                                "$ENTITY_NAME, then none of ${consequentlyNotPermittedFlags
                                        .first()} or ${consequentlyNotPermittedFlags.last()}" +
                                " is permitted"))
                    }
                }
        assertAll(*assertions.toTypedArray())
    }

    //
    // INTERNALS
    //

    companion object {
        private const val ENTITY_NAME = "a fake thing"
        private val alwaysBadRule = FlagMixRule.rule({ _: EnumSet<FakeFlag> -> true },
                "always bad")
        private val alwaysGoodRule = FlagMixRule.rule({ _: EnumSet<FakeFlag> -> false},
                "always good")

        @JvmStatic
        private fun allOfSource() = arrayOf(
                Arguments.of(of(FOO, BAR), "FOO and BAR"),
                Arguments.of(of(FOO, BAZ), "FOO and BAZ"),
                Arguments.of(of(BAR, BAZ), "BAR and BAZ"),
                Arguments.of(of(FOO, BAR, BAZ), "FOO, BAR, and BAZ")
        )

        @JvmStatic
        private fun allPairsSource() = Sets.cartesianProduct(
                allOf(FakeFlag::class.java),
                allOf(FakeFlag::class.java))
                .filter { it.first() < it.last() }
                .map { Arguments.of(it.first(), it.last()) }

        @JvmStatic
        private fun allPairsAndTriplesSource() = arrayOf(
                of(FOO, BAR), of(FOO, BAZ), of (BAR, BAZ),
                of(FOO, BAR, BAZ))

        @JvmStatic
        private fun ifFirstThenNoneOfTheRestSource() = arrayOf(
                Arguments.of(FOO, of(BAR, BAZ)),
                Arguments.of(BAR, of(FOO, BAZ)),
                Arguments.of(BAZ, of(FOO, BAR))
        )

        private fun Collection<FakeFlag>.toEnumSet() = when {
            isNotEmpty() -> copyOf(this)
            else -> noneOf(FakeFlag::class.java)
        }
    }
}

