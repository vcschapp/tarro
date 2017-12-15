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

import io.tarro.test.assertUniqueValuesByEnumerator
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.isOneOf
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

/**
 * Unit tests for [TargetType].
 *
 * @author Victor Schappert
 * @since 20171209
 */
class TargetTypeTest {
    @Test
    fun uniqueTargetType() {
        assertUniqueValuesByEnumerator(TargetType::class, "value (target type)",
                TargetType::getValue)
    }

    @Test
    fun sortedByValue() {
        assertEquals(TargetType.values().asList(),
                TargetType.values().sortedBy(TargetType::getValue))
    }

    @ParameterizedTest
    @EnumSource(TargetType::class)
    fun attributeContext(targetType: TargetType) {
        assertThat(targetType.attributeContext, isOneOf(AttributeContext.CLASS_FILE,
                AttributeContext.FIELD_INFO, AttributeContext.METHOD_INFO,
                AttributeContext.CODE));
    }

    @Test
    fun declarationTargets() {
        // Verify that all the declaration targets have 0x10 set as their
        // highest bit.
        TargetType.values()
                .filter { AttributeContext.CODE != it.attributeContext }
                .forEach {
                    assertTrue(it.value in 0x00..0x17,
                            "Expression target $it (value ${it.value}) should " +
                            "have a value in the range 0x00..0x17 but doesn't")
                }
    }

    @Test
    fun expressionTargets() {
        // Verify that all of the expression targets are contiguous and that
        // they have the 0x40 bit set as their highest bit.
        var prevValue = 0x40 - 1
        TargetType.values()
                .filter { AttributeContext.CODE == it.attributeContext }
                .forEach {
                    assertEquals(prevValue + 1, it.value,
                            "Expression target $it (value ${it.value}) " +
                            "must be adjacent to previous expression " +
                            "target, but isn't")
                    assertTrue(it.value in 0x40..0x4b,
                            "Expression target $it (value ${it.value}) should " +
                            "have a value in the range 0x40..0x4a but doesn't")
                    prevValue = it.value
                }
    }
}