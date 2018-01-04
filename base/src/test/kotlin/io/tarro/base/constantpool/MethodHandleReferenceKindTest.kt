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

package io.tarro.base.constantpool

import io.tarro.test.assertUniqueValuesByEnumerator
import org.hamcrest.Matchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.both
import org.hamcrest.Matchers.greaterThanOrEqualTo
import org.hamcrest.Matchers.lessThanOrEqualTo
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

/**
 * Unit tests for [MethodHandleReferenceKind].
 *
 * @author Victor Schappert
 * @since 20180102
 */
class MethodHandleReferenceKindTest {
    @Test
    fun uniqueTagValues() {
        assertUniqueValuesByEnumerator(MethodHandleReferenceKind::class,
                "getValue()", MethodHandleReferenceKind::getValue)
    }

    @ParameterizedTest
    @EnumSource(MethodHandleReferenceKind::class)
    fun valueRange(kind: MethodHandleReferenceKind) {
        assertThat(kind.value, `is`(both(greaterThanOrEqualTo(1))
                .and(lessThanOrEqualTo(9))))
    }

    @Test
    fun valuesSortedAscending() {
        val allValues = MethodHandleReferenceKind.values()
                .map(MethodHandleReferenceKind::getValue)
        assertEquals(allValues.sorted(), allValues,
                "${MethodHandleReferenceKind::class.simpleName} members " +
                        "should be listed in ascending order of kind value")
    }

    @ParameterizedTest
    @EnumSource(MethodHandleReferenceKind::class)
    fun kindName(kind: MethodHandleReferenceKind) {
        assertEquals(kind.expectedKindName, kind.kindName)
    }

    //
    // INTERNALS
    //

    private val MethodHandleReferenceKind.expectedKindName: String
        get() {
            return name
                    .split('_')
                    .map(String::toLowerCase)
                    .asSequence()
                    .let {
                        "REF_${it.first()}" + it.drop(1)
                                .map(String::capitalize)
                                .joinToString("")
                    }
        }
}