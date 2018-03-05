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

import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.function.Executable

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.lessThanOrEqualTo

/**
 * Asserts that the [class file version numbers][ClassFileVersion] on a
 * versioned object are valid. This means, at a minimum, that the first
 * supporting version may not be `null`; and that the last supporting version
 * must either be empty or identify a version that is equal to or later than
 * the first supporting version.
 *
 * @param versioned Versioned object to assert against
 */
fun assertVersionRangeValid(versioned: Versioned) {
    assertAll("Version range for $versioned",
            Executable { assertNotNull(versioned.firstVersionSupporting) },
            Executable {
                versioned.lastVersionSupporting.ifPresent {
                    assertThat(versioned.firstVersionSupporting.ordinal,
                            lessThanOrEqualTo(it.ordinal))
                }
            }
    )
}
