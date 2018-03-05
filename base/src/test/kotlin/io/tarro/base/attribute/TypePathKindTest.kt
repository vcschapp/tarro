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
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

/**
 * Unit tests for [TypePathKind].
 *
 * @author Victor Schappert
 * @since 20171214
 */
class TypePathKindTest {
    @Test
    fun uniqueTypePathKind() {
        assertUniqueValuesByEnumerator(TypePathKind::class,
                "value (type path kind)", TypePathKind::getValue)
    }

    @Test
    fun explicitValueCheck() {
        assertEquals(0, TypePathKind.DEEPER_IN_ARRAY_TYPE.value);
        assertEquals(1, TypePathKind.DEEPER_IN_NESTED_TYPE.value);
        assertEquals(2, TypePathKind.ON_TYPE_ARGUMENT_WILDCARD_BOUND.value);
        assertEquals(3, TypePathKind.ON_TYPE_ARGUMENT.value);
    }

    @ParameterizedTest
    @EnumSource(TypePathKind::class)
    fun ordinalEqualsValue(typePathKind: TypePathKind) {
        assertEquals(typePathKind.ordinal, typePathKind.value);
    }
}

