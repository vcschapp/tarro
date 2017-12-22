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

package io.tarro.base.bytecode

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.isIn
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.EnumSource.Mode.EXCLUDE
import org.junit.jupiter.params.provider.EnumSource.Mode.INCLUDE

/**
 * Unit tests for [OperandType].
 *
 * @author Victor Schappert
 * @since 20171220
 */
class OperandTypeTest {
    @ParameterizedTest
    @EnumSource(OperandType::class, mode = EXCLUDE,
            names = ["MATCH_OFFSET_PAIR_TABLE", "JUMP_OFFSET_TABLE",
                     "OPTIONAL_SIGNED_VALUE_SHORT"])
    fun fixedSizeOperandTypeReasonableSize(operandType: OperandType) {
        assertThat(operandType.size, isIn(setOf(1, 2, 4)))
        assertFalse(operandType.isVariableSize)
    }

    @ParameterizedTest
    @EnumSource(OperandType::class, mode = EXCLUDE,
            names = ["MATCH_OFFSET_PAIR_TABLE", "JUMP_OFFSET_TABLE",
                     "OPTIONAL_SIGNED_VALUE_SHORT"])
    fun fixedSizeOperandTypeSizeNames(operandType: OperandType) {
        Regex("_(BYTE|SHORT|INT)$").find(operandType.name)?.let {
            when (it.groups.last()!!.value) {
                "BYTE" -> assertEquals(1, operandType.size)
                "SHORT" -> assertEquals(2, operandType.size)
                "INT" -> assertEquals(4, operandType.size)
                else -> throw IllegalStateException("Logic error in test...")
            }
        }
    }

    @ParameterizedTest
    @EnumSource(OperandType::class, mode = INCLUDE,
            names = ["MATCH_OFFSET_PAIR_TABLE", "JUMP_OFFSET_TABLE",
                     "OPTIONAL_SIGNED_VALUE_SHORT"])
    fun variableSizeOperandType(operandType: OperandType) {
        assertEquals(OperandType.VARIABLE_SIZE, operandType.size)
        assertTrue(operandType.isVariableSize)
    }
}

