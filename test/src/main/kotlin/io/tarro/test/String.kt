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

package io.tarro.test

import java.nio.ByteBuffer
import java.nio.ByteBuffer.wrap

/**
 * Converts a string of whitespace-delimited hexadecimal integers into the
 * byte array implied by converting each of those integers into a number in the
 * range of a byte.
 *
 * @author Victor Schappert
 * @since 20171209
 */
fun String.parseAsByteArray() : ByteArray {
    return split(Regex("""\s+"""))
            .filter(String::isNotEmpty)
            .map { Integer.parseInt(it,16) }
            .onEach {
                if (it !in 0..255) throw IllegalArgumentException(it.toString())
            }
            .map { it.toByte() }
            .toByteArray()
}

/**
 * Converts a string of whitespace-delimited hexadecimal integers into the
 * mutable {@link ByteBuffer} implied by converting each of those integers into
 * a number in the range of a byte.
 *
 * @author Victor Schappert
 * @since 20180407
 */
fun String.parseAsByteBuffer() : ByteBuffer {
    return wrap(parseAsByteArray())
}

