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

import io.tarro.base.assertVersionRangeValid
import io.tarro.test.assertUniqueValuesByEnumerator
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.EnumSource.Mode.EXCLUDE

/**
 * Unit tests for [AttributeType].
 *
 * @author Victor Schappert
 * @since 20171205
 */
class AttributeTypeTest {

    @Test
    fun attributeNameUnique() {
            assertUniqueValuesByEnumerator(AttributeType::class,"attribute name", AttributeType::getAttributeName)
    }

    @ParameterizedTest
    @EnumSource(AttributeType::class, names = ["UNKNOWN"], mode = EXCLUDE)
    fun attributeNameCamelCase(attributeType: AttributeType) {
        assertNotNull(attributeType.attributeName)
        assertTrue(attributeType.attributeName.matches(Regex("[A-Z][a-zA-Z]+")))
    }

    @Test
    fun attributeNameNullForUnknown() {
        assertNull(AttributeType.UNKNOWN.attributeName)
    }

    @ParameterizedTest
    @EnumSource(AttributeType::class, names = ["UNKNOWN"], mode = EXCLUDE)
    fun attributeContextValid(attributeType: AttributeType) {
        assertEquals(0, attributeType.attributeContext and AttributeContext.ALL.inv())
    }

    @Test
    fun noAttributeContextForUnknown() {
        assertEquals(AttributeContext.NONE, AttributeType.UNKNOWN.attributeContext)
    }

    @ParameterizedTest
    @EnumSource(AttributeType::class)
    fun versionRangeValid(attributeType: AttributeType) {
        assertVersionRangeValid(attributeType)
    }

}