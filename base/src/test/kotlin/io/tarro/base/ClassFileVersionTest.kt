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

import io.tarro.base.attribute.AttributeType
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.lessThanOrEqualTo
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.EnumSource.Mode.EXCLUDE
import org.junit.jupiter.params.provider.EnumSource.Mode.INCLUDE

/**
 * Unit tests for [ClassFileVersion].
 *
 * @author Victor Schappert
 * @since 20180407
 */
class ClassFileVersionTest {
    @ParameterizedTest
    @EnumSource(ClassFileVersion::class, names = ["JAVA1"], mode = EXCLUDE)
    fun ascendingOrder(classFileVersion: ClassFileVersion) {
        val previous = ClassFileVersion.values()[classFileVersion.ordinal - 1]
        assertThat(previous.majorVersion, lessThanOrEqualTo(classFileVersion.majorVersion))
    }

    @ParameterizedTest
    @EnumSource(ClassFileVersion::class, names = ["JAVA1", "JAVA1_1"], mode = EXCLUDE)
    fun incrementingMajorVersions(classFileVersion: ClassFileVersion) {
        val previous = ClassFileVersion.values()[classFileVersion.ordinal - 1]
        assertEquals(previous.majorVersion + 1, classFileVersion.majorVersion)
        assertEquals(0, classFileVersion.minorVersion)
    }

    @ParameterizedTest
    @EnumSource(ClassFileVersion::class, names = ["JAVA1", "JAVA1_1"], mode = INCLUDE)
    fun initialVersions(classFileVersion: ClassFileVersion) {
        assertEquals(45, classFileVersion.majorVersion)
        assertEquals(3, classFileVersion.minorVersion)
    }
}
