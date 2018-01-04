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

import io.tarro.base.ClassFileVersion.JAVA1
import io.tarro.base.ClassFileVersion.JAVA7
import io.tarro.base.ClassFileVersion.JAVA9
import io.tarro.test.assertUniqueValuesByEnumerator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.EnumSource.Mode.EXCLUDE
import org.junit.jupiter.params.provider.EnumSource.Mode.INCLUDE
import org.junit.jupiter.params.provider.ValueSource

/**
 * Unit tests for [ConstantPoolTag].
 *
 * @author Victor Schappert
 * @since 20171228
 */
class ConstantPoolTagTest {
    @Test
    fun uniqueTagValues() {
        assertUniqueValuesByEnumerator(ConstantPoolTag::class, "getValue()",
                ConstantPoolTag::getValue)
    }

    @ParameterizedTest
    @EnumSource(ConstantPoolTag::class, mode = EXCLUDE,
            names = ["METHOD_HANDLE", "METHOD_TYPE", "INVOKE_DYNAMIC",
                     "MODULE", "PACKAGE"])
    fun firstVersionSupportingJava1(constantPoolTag: ConstantPoolTag) {
        assertEquals(JAVA1, constantPoolTag.firstVersionSupporting)
    }

    @ParameterizedTest
    @EnumSource(ConstantPoolTag::class, mode = INCLUDE,
            names = ["METHOD_HANDLE", "METHOD_TYPE", "INVOKE_DYNAMIC"])
    fun firstVersionSupportingJava7(constantPoolTag: ConstantPoolTag) {
        assertEquals(JAVA7, constantPoolTag.firstVersionSupporting)
    }

    @ParameterizedTest
    @EnumSource(ConstantPoolTag::class, mode = INCLUDE,
            names = ["MODULE", "PACKAGE"])
    fun firstVersionSupportingJava9(constantPoolTag: ConstantPoolTag) {
        assertEquals(JAVA9, constantPoolTag.firstVersionSupporting)
    }

    @ParameterizedTest
    @EnumSource(ConstantPoolTag::class)
    fun tagName(constantPoolTag: ConstantPoolTag) {
        assertEquals(constantPoolTag.expectedTagName, constantPoolTag.tagName)
    }

    @ParameterizedTest
    @EnumSource(ConstantPoolTag::class)
    fun structureName(constantPoolTag: ConstantPoolTag) {
        assertEquals(constantPoolTag.expectedStructureName,
                constantPoolTag.structureName)
    }

    @ParameterizedTest
    @EnumSource(ConstantPoolTag::class, mode = EXCLUDE,
            names = ["LONG", "DOUBLE"])
    fun numSlotsIsOne(constantPoolTag: ConstantPoolTag) {
        assertEquals(1, constantPoolTag.numSlots)
    }

    @ParameterizedTest
    @EnumSource(ConstantPoolTag::class, mode = INCLUDE,
            names = ["LONG", "DOUBLE"])
    fun numSlotsIsTwo(constantPoolTag: ConstantPoolTag) {
        assertEquals(2, constantPoolTag.numSlots)
    }

    @ParameterizedTest
    @ValueSource(ints = [2, 13, 14, 17])
    fun valueGaps(value: Int) {
        val matchingTags = ConstantPoolTag.values()
                .filter { it.value == value }
        assertEquals(emptyList<ConstantPoolTag>(), matchingTags)
    }

    @Test
    fun valueRange() {
        val min = ConstantPoolTag.values().map(ConstantPoolTag::getValue).min()!!
        val max = ConstantPoolTag.values().map(ConstantPoolTag::getValue).max()!!
        assertEquals(1..20, min..max)
    }

    //
    // INTERNALS
    //

    private val ConstantPoolTag.expectedTagName: String
        get() {
            return name.splitToSequence('_')
                    .map(String::toLowerCase)
                    .map(String::capitalize)
                    .joinToString(separator = "", prefix = "CONSTANT_")
        }

    private val ConstantPoolTag.expectedStructureName: String
        get()  = "${expectedTagName}_info"
}

