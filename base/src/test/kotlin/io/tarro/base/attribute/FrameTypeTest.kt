/*
 * MIT License
 *
 * Copyright (c) 2017 Victor Schappert
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

package io.tarro.base.attribute

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.greaterThanOrEqualTo
import org.hamcrest.Matchers.lessThan
import org.hamcrest.Matchers.lessThanOrEqualTo
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

/**
 * Unit tests for [FrameType].
 *
 * @author Victor Schappert
 * @since 20171209
 */
class FrameTypeTest {
    @ParameterizedTest
    @EnumSource(FrameType::class)
    fun unsignedByteRangeEndpoints(frameType: FrameType) {
        assertAll("Boths ends of the range for $frameType should fit " +
                "within an unsigned byte (0..255).",
                Executable { assertThat(0, lessThanOrEqualTo(frameType.minValue)) },
                Executable { assertThat(0xff, greaterThanOrEqualTo(frameType.minValue)) },
                Executable { assertThat(0, lessThanOrEqualTo(frameType.maxValue)) },
                Executable { assertThat(0xff, greaterThanOrEqualTo(frameType.maxValue)) })
    }

    @ParameterizedTest
    @EnumSource(FrameType::class)
    fun nonEmptyRanges(frameType: FrameType) {
        assertThat(frameType.minValue, lessThanOrEqualTo(frameType.maxValue))
    }

    @Test
    fun ascendingOrderDiscreteRanges() {
        var maxValue = -1
        assertAll("Each enumerator's range must be discrete (it may not " +
                "intersect with any other's) and the ranges must be sorted " +
                "in ascending order.",
                *FrameType.values().map {
                    val previousMax = maxValue
                    val assertion = Executable {
                        assertThat("$it", previousMax, lessThan(it.minValue))
                    }
                    maxValue = it.maxValue
                    assertion
                }.toTypedArray()
        )
    }

    @ParameterizedTest
    @EnumSource(FrameType::class)
    fun entireRangeMapsBackToFrameType(frameType: FrameType) {
        assertAll("The entire range for $frameType (${frameType.minValue}.." +
                "${frameType.maxValue}) should map back to $frameType.",
                *(frameType.minValue..frameType.maxValue).map {
                    Executable {
                        assertEquals(frameType, FrameType.forUnsignedByte(it))
                    }
                }.toTypedArray()
        )
    }

    @Test
    fun inBoundsValuesThrowIfNoEnumerator() {
        val unusedFrameTypeBytes =
                (listOf(0..255) + FrameType.values().map { it.minValue .. it.maxValue })
                .map(Iterable<Int>::toSet)
                .reduce { lhs, rhs -> lhs.subtract(rhs) }
        assertAll("Every unused frame_type value should map to null",
            *unusedFrameTypeBytes.map {
                Executable {
                    assertThrows(IllegalArgumentException::class.java,
                            { FrameType.forUnsignedByte(it) },
                            "$it")
                }
            }.toTypedArray()
        )
    }

    @Test
    fun outOfBoundsThrows() {
        assertThrows(IllegalArgumentException::class.java,
                { FrameType.forUnsignedByte(-1) })
        assertThrows(IllegalArgumentException::class.java,
                { FrameType.forUnsignedByte(256) })
    }
}

