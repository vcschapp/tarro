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

package io.tarro.test

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Test

/**
 * Unit tests for the [String] extension function [parseAsByteArray].
 *
 * @author Victor Schappert
 * @since 20171209
 */
class StringParseByteArrayTest {
    @Test
    fun empty() {
        assertArrayEquals(byteArrayOf(), "".parseAsByteArray())
    }
    @Test
    fun one() {
        assertArrayEquals(byteArrayOf(1), "1".parseAsByteArray())
        assertArrayEquals(byteArrayOf(-1), "  ff  ".parseAsByteArray())
    }
    @Test
    fun several() {
        assertArrayEquals(
                byteArrayOf(15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0),
                """
                0f 0e 0d 0c
                0b  a 09 08
                07 06  5  4
                 3  2  1  000
                """.parseAsByteArray())
    }
}