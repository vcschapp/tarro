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

package io.tarro.base.bytecode

import io.tarro.test.ConstantContainerTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.lang.reflect.Field

/**
 * Unit tests for [ATypeValue].
 *
 * @author Victor Schappert
 * @since 20171220
 */
class ATypeValueTest : ConstantContainerTest<ATypeValue>(ATypeValue::class) {

    @Test
    fun minIsMin() {
        val actualMinField = uniqueFields.minBy { it.getInt(null) }
        assertEquals(ATypeValue.MIN_VALUE, actualMinField?.get(null))
    }

    @Test
    fun maxIsMax() {
        val actualMinField = uniqueFields.maxBy { it.getInt(null) }
        assertEquals(ATypeValue.MAX_VALUE, actualMinField?.getInt(null))
    }

    @Test
    fun allConstantsAreValidATypeValues() {
        uniqueFields.forEach {
            val value = it.getInt(null)
            assertTrue(ATypeValue.isValidATypeValue(value),
                    "${it.name} ($value) should not fail the atype " +
                            "value test but is failing")
        }
    }

    @Test
    fun outsideMinMaxRangeIsNotValid() {
        assertInvalidATypeValue(ATypeValue.MIN_VALUE - 1, "one below MIN_VALUE")
        assertInvalidATypeValue(ATypeValue.MAX_VALUE + 1, "one above MIN_VALUE")
        intArrayOf(Int.MIN_VALUE, -1, 0, Int.MAX_VALUE).forEach {
            assertInvalidATypeValue(it, "boundary test")
        }
    }

    //
    // PROPERTIES
    //

    override val uniqueFields: List<Field>
        get() = visibleFields.filter { it.name != "MIN_VALUE" && it.name != "MAX_VALUE" }

    //
    // INTERNALS
    //

    private fun assertInvalidATypeValue(value: Int, description: String) {
        assertFalse(ATypeValue.isValidATypeValue(value),
                "Value $value ($description) should fail atype value test " +
                        "it is passing")
    }
}
