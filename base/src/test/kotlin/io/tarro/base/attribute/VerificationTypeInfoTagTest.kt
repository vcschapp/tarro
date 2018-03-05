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

package io.tarro.base.attribute

import io.tarro.test.assertUniqueValuesByEnumerator
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.greaterThanOrEqualTo
import org.hamcrest.Matchers.lessThanOrEqualTo
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

/**
 * Unit tests for [VerificationTypeInfoTag].
 *
 * @author Victor Schappert
 * @since 20171214
 */
class VerificationTypeInfoTagTest {
    @Test
    fun uniqueTag() {
        assertUniqueValuesByEnumerator(VerificationTypeInfoTag::class,
                "value (tag)", VerificationTypeInfoTag::getValue)
    }

    @ParameterizedTest
    @EnumSource(VerificationTypeInfoTag::class)
    fun valueInRange(verificationTypeInfoTag: VerificationTypeInfoTag) {
        assertThat(verificationTypeInfoTag.value, greaterThanOrEqualTo(0))
        assertThat(verificationTypeInfoTag.value, lessThanOrEqualTo(8))
    }

    @Test
    fun contiguousValues() {
        val values = VerificationTypeInfoTag.values()
                .map { it.value }
                .sorted()
        assertEquals((0..8).asSequence().toList(), values)
    }
}
