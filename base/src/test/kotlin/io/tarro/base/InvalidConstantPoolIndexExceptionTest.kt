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

package io.tarro.base

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * Unit tests for [InvalidConstantPoolIndexException].
 *
 * @author Victor Schappert
 * @since 20180407
 */
class InvalidConstantPoolIndexExceptionTest {
    @Test
    fun construct() {
        val e = InvalidConstantPoolIndexException("foo", 10)
        assertEquals("foo", e.message)
        assertEquals(10, e.constantPoolIndex)
    }

    @Test
    fun lessThanOne() {
        val e = InvalidConstantPoolIndexException.lessThanOne(0)
        assertEquals("Index into constant_pool must be positive but is 0", e.message)
        assertEquals(0, e.constantPoolIndex)
    }

    @Test
    fun notLessThanCount() {
        val e = InvalidConstantPoolIndexException.notLessThanCount(1, 1)
        assertEquals("Index into constant_pool must be less than " +
                "constant_pool_count but 1 is not less than 1", e.message)
        assertEquals(1, e.constantPoolIndex)
    }
}
