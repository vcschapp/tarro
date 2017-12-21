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

import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import kotlin.reflect.KClass
import kotlin.reflect.full.cast

/**
 * Unit tests for [Opcode].
 *
 * @author Victor Schappert
 * @since 20171220
 */
class OpcodeTest {

    @ParameterizedTest
    @MethodSource("allOpcodes")
    fun forUnsignedByteAllOpcodes(opcode: Opcode) {
        assertEquals(opcode, Opcode.forUnsignedByte(opcode.value))
    }

    @Test
    fun forUnsignedByteOutsideRange() {
        assertAll(
            badUnsignedByte(-1),
            badUnsignedByte(257),
            badUnsignedByte(Integer.MIN_VALUE),
            badUnsignedByte(Integer.MAX_VALUE)
        )
    }

    @Test
    fun isVariableSizeFalseByDefault() {
        assertFalse(TestableOpcode.INSTANCE.isVariableSize)
    }

    @Test
    fun isPaddedFalseByDefault() {
        assertFalse(TestableOpcode.INSTANCE.isPadded)
    }

    @Test
    fun isReservedFalseByDefault() {
        assertFalse(TestableOpcode.INSTANCE.isReserved)
    }

    //
    // INTERNALS
    //

    private fun badUnsignedByte(value: Int) : Executable {
        return Executable {
            assertThrows(IllegalArgumentException::class.java) {
                Opcode.forUnsignedByte(value)
            }
        }
    }

    companion object {
        @JvmStatic
        private fun allOpcodes() = arrayOf<KClass<out Opcode>>(
                NoOperandOpcode::class,
                OneOperandOpcode::class,
                TwoOperandOpcode::class,
                VariableOperandOpcode::class)
                .map(KClass<out Opcode>::java)
                .flatMap { it.enumConstants.asIterable() }
                .map { Opcode::class.cast(it) }
    }
}

/**
 * A fake opcode for testing purposes only.
 *
 * @author Victor Schappert
 * @since 20171220
 */
internal class TestableOpcode private constructor() : Opcode {

    //
    // INTERFACE: Opcode
    //

    override fun getOperandTypes() = emptyList<OperandType>()

    override fun getNumOperands() = 0

    //
    // INTERFACE: Valued
    //

    override fun getValue() = 0

    //
    // COMPANIONS
    //

    companion object {
        val INSTANCE = TestableOpcode()
    }
}

