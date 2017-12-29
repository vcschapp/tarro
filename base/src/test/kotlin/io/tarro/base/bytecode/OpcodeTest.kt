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

import io.tarro.base.ClassFileVersion
import io.tarro.base.ClassFileVersion.JAVA1
import io.tarro.base.ClassFileVersion.JAVA6
import io.tarro.base.ClassFileVersion.JAVA7
import io.tarro.base.bytecode.OperandType.BRANCH_OFFSET_INT
import io.tarro.base.bytecode.OperandType.JUMP_OFFSET_TABLE
import io.tarro.base.bytecode.OperandType.LOCAL_VARIABLE_INDEX_SHORT
import io.tarro.base.bytecode.OperandType.MATCH_OFFSET_PAIR_TABLE
import io.tarro.base.bytecode.OperandType.OPCODE
import io.tarro.base.bytecode.OperandType.OPTIONAL_SIGNED_VALUE_SHORT
import io.tarro.base.bytecode.OperandType.SIGNED_VALUE_INT
import io.tarro.base.bytecode.VariableOperandOpcode.LOOKUPSWITCH
import io.tarro.base.bytecode.VariableOperandOpcode.TABLESWITCH
import io.tarro.base.bytecode.VariableOperandOpcode.WIDE
import io.tarro.test.assertUniqueValuesByEnumerator
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.EnumSource.Mode.EXCLUDE
import org.junit.jupiter.params.provider.EnumSource.Mode.INCLUDE
import org.junit.jupiter.params.provider.MethodSource
import java.util.*
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
    fun forUnsignedByteInRangeButNoOpcode() {
        val opcodes = OpcodeValueTest().uniqueFields
                .map { it.getInt(null) }
                .toSet()
        // For each unsigned byte value that does NOT correspond to a valid JVM
        // opcode, verify that trying to obtain it from Opcode.forUnsignedByte()
        // throws an exception.
        (0..255).filter{ !opcodes.contains(it) }
                .forEach {  badUnsignedByte(it).execute() }
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

    @Test
    fun firstVersionSupportingIsJava1ByDefault() {
        assertEquals(JAVA1, TestableOpcode.INSTANCE.firstVersionSupporting)
    }

    @Test
    fun deprecatedNeverByDefault() {
        assertEquals(Optional.empty<ClassFileVersion>(),
                TestableOpcode.INSTANCE.lastVersionSupporting)
    }


    @Test
    fun noDuplicateOpcodeValues() {
        val numDuplicateOpcodeValues = allOpcodes()
                .groupBy(Opcode::getValue)
                .filterValues { it.size != 1 }
                .count()
        assertEquals(0, numDuplicateOpcodeValues)
    }

    @Test
    fun allOpcodeValuesCovered() {
        val allOpcodeObjectValues = allOpcodes()
                .map(Opcode::getValue)
                .toSet()
        val allOpcodeValues = OpcodeValueTest()
                .uniqueFields
                .map { it.getInt(null) }
                .toSet()
        val intersection = allOpcodeObjectValues.intersect(allOpcodeValues)
        assertEquals(allOpcodeObjectValues, intersection)
        assertEquals(allOpcodeValues, intersection)
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
 * Unit tests for [NoOperandOpcode].
 *
 * @author Victor Schappert
 * @since 20171224
 */
class NoOperandOpcodeTest {
    @Test
    fun uniqueOpcodeValues() {
        assertUniqueValuesByEnumerator(NoOperandOpcode::class, "getValue()",
                NoOperandOpcode::getValue)
    }

    @ParameterizedTest
    @EnumSource(NoOperandOpcode::class)
    fun forUnsignedByteAllOpcodes(opcode: NoOperandOpcode) {
        assertEquals(opcode, NoOperandOpcode.forUnsignedByte(opcode.value))
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
    fun forUnsignedByteInRangeButNoOpcode() {
        val opcodes = NoOperandOpcode.values()
                .map(Opcode::getValue)
                .toSet()
        // For each unsigned byte value that does NOT correspond to a valid JVM
        // opcode, verify that trying to obtain it from Opcode.forUnsignedByte()
        // throws an exception.
        (0..255).filter{ !opcodes.contains(it) }
                .forEach {  badUnsignedByte(it).execute() }
    }

    @ParameterizedTest
    @EnumSource(NoOperandOpcode::class)
    fun operands(opcode: NoOperandOpcode) {
        assertAll(
                Executable { assertEquals(emptyList<OperandType>(), opcode.operandTypes) },
                Executable { assertEquals(0, opcode.numOperands) }
        )
    }

    @ParameterizedTest
    @EnumSource(NoOperandOpcode::class)
    fun isntVariableSize(opcode: NoOperandOpcode) {
        assertFalse(opcode.isVariableSize)
    }

    @ParameterizedTest
    @EnumSource(NoOperandOpcode::class)
    fun isntPadded(opcode: NoOperandOpcode) {
        assertFalse(opcode.isPadded)
    }

    @ParameterizedTest
    @EnumSource(NoOperandOpcode::class, mode = INCLUDE,
                names = ["BREAKPOINT", "IMPDEP1", "IMPDEP2"])
    fun isReserved(opcode: NoOperandOpcode) {
        assertTrue(opcode.isReserved)
    }

    @ParameterizedTest
    @EnumSource(NoOperandOpcode::class, mode = EXCLUDE,
            names = ["BREAKPOINT", "IMPDEP1", "IMPDEP2"])
    fun isntReserved(opcode: NoOperandOpcode) {
        assertFalse(opcode.isReserved)
    }

    @ParameterizedTest
    @EnumSource(NoOperandOpcode::class)
    fun firstVersionSupportingIsJava1(opcode: NoOperandOpcode) {
        assertEquals(JAVA1, opcode.firstVersionSupporting)
    }

    @ParameterizedTest
    @EnumSource(TwoOperandOpcode::class)
    fun deprecatedNever(opcode: TwoOperandOpcode) {
        assertEquals(Optional.empty<ClassFileVersion>(),
                opcode.lastVersionSupporting)
    }

    //
    // INTERNALS
    //

    private fun badUnsignedByte(value: Int) : Executable {
        return Executable {
            assertThrows(IllegalArgumentException::class.java) {
                NoOperandOpcode.forUnsignedByte(value)
            }
        }
    }
}

/**
 * Unit tests for [OneOperandOpcode].
 *
 * @author Victor Schappert
 * @since 20171226
 */
class OneOperandOpcodeTest {
    @Test
    fun uniqueOpcodeValues() {
        assertUniqueValuesByEnumerator(OneOperandOpcode::class, "getValue()",
                OneOperandOpcode::getValue)
    }

    @ParameterizedTest
    @EnumSource(OneOperandOpcode::class)
    fun forUnsignedByteAllOpcodes(opcode: OneOperandOpcode) {
        assertEquals(opcode, OneOperandOpcode.forUnsignedByte(opcode.value))
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
    fun forUnsignedByteInRangeButNoOpcode() {
        val opcodes = OneOperandOpcode.values()
                .map(Opcode::getValue)
                .toSet()
        // For each unsigned byte value that does NOT correspond to a valid JVM
        // opcode, verify that trying to obtain it from Opcode.forUnsignedByte()
        // throws an exception.
        (0..255).filter{ !opcodes.contains(it) }
                .forEach {  badUnsignedByte(it).execute() }
    }

    @ParameterizedTest
    @EnumSource(OneOperandOpcode::class)
    fun operands(opcode: OneOperandOpcode) {
        assertAll (
                Executable { assertEquals(1, opcode.numOperands) },
                Executable { assertEquals(listOf(opcode.operandType), opcode.operandTypes) }
        )
    }

    @ParameterizedTest
    @EnumSource(OneOperandOpcode::class)
    fun isntVariableSize(opcode: OneOperandOpcode) {
        assertFalse(opcode.isVariableSize)
    }

    @ParameterizedTest
    @EnumSource(OneOperandOpcode::class)
    fun isntPadded(opcode: OneOperandOpcode) {
        assertFalse(opcode.isPadded)
    }

    @ParameterizedTest
    @EnumSource(OneOperandOpcode::class)
    fun isntReserved(opcode: OneOperandOpcode) {
        assertFalse(opcode.isReserved)
    }

    @ParameterizedTest
    @EnumSource(OneOperandOpcode::class, mode = EXCLUDE,
            names = ["INVOKEDYNAMIC"])
    fun firstVersionSupportingIsJava1(opcode: OneOperandOpcode) {
        assertEquals(JAVA1, opcode.firstVersionSupporting)
    }

    @ParameterizedTest
    @EnumSource(OneOperandOpcode::class, mode = INCLUDE,
            names = ["INVOKEDYNAMIC"])
    fun firstVersionSupportingIsJava7(opcode: OneOperandOpcode) {
        assertEquals(JAVA7, opcode.firstVersionSupporting)
    }

    @ParameterizedTest
    @EnumSource(OneOperandOpcode::class, mode = EXCLUDE,
            names = ["JSR", "JSR_W"])
    fun deprecatedNever(opcode: OneOperandOpcode) {
        assertEquals(Optional.empty<ClassFileVersion>(),
                opcode.lastVersionSupporting)
    }

    @ParameterizedTest
    @EnumSource(OneOperandOpcode::class, mode = INCLUDE,
            names = ["JSR", "JSR_W"])
    fun deprecatedAfterJava6(opcode: OneOperandOpcode) {
        assertEquals(Optional.of(JAVA6), opcode.lastVersionSupporting)
    }

    //
    // INTERNALS
    //

    private fun badUnsignedByte(value: Int) : Executable {
        return Executable {
            assertThrows(IllegalArgumentException::class.java) {
                OneOperandOpcode.forUnsignedByte(value)
            }
        }
    }
}

/**
 * Unit tests for [TwoOperandOpcode].
 *
 * @author Victor Schappert
 * @since 20171227
 */
class TwoOperandOpcodeTest {
    @Test
    fun uniqueOpcodeValues() {
        assertUniqueValuesByEnumerator(TwoOperandOpcode::class, "getValue()",
                TwoOperandOpcode::getValue)
    }

    @ParameterizedTest
    @EnumSource(TwoOperandOpcode::class)
    fun forUnsignedByteAllOpcodes(opcode: TwoOperandOpcode) {
        assertEquals(opcode, TwoOperandOpcode.forUnsignedByte(opcode.value))
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
    fun forUnsignedByteInRangeButNoOpcode() {
        val opcodes = TwoOperandOpcode.values()
                .map(Opcode::getValue)
                .toSet()
        // For each unsigned byte value that does NOT correspond to a valid JVM
        // opcode, verify that trying to obtain it from Opcode.forUnsignedByte()
        // throws an exception.
        (0..255).filter{ !opcodes.contains(it) }
                .forEach {  badUnsignedByte(it).execute() }
    }

    @ParameterizedTest
    @EnumSource(TwoOperandOpcode::class)
    fun operands(opcode: TwoOperandOpcode) {
        assertAll (
                Executable { assertEquals(2, opcode.numOperands) },
                Executable { assertEquals(2, opcode.operandTypes.size) },
                Executable { assertNotNull(opcode.operandTypes[0]) },
                Executable { assertNotNull(opcode.operandTypes[1]) }
        )
    }

    @ParameterizedTest
    @EnumSource(TwoOperandOpcode::class)
    fun isntVariableSize(opcode: TwoOperandOpcode) {
        assertFalse(opcode.isVariableSize)
    }

    @ParameterizedTest
    @EnumSource(TwoOperandOpcode::class)
    fun isntPadded(opcode: TwoOperandOpcode) {
        assertFalse(opcode.isPadded)
    }

    @ParameterizedTest
    @EnumSource(TwoOperandOpcode::class)
    fun isntReserved(opcode: TwoOperandOpcode) {
        assertFalse(opcode.isReserved)
    }

    @ParameterizedTest
    @EnumSource(TwoOperandOpcode::class)
    fun firstVersionSupportingIsJava1(opcode: TwoOperandOpcode) {
        assertEquals(JAVA1, opcode.firstVersionSupporting)
    }

    @ParameterizedTest
    @EnumSource(TwoOperandOpcode::class)
    fun deprecatedNever(opcode: TwoOperandOpcode) {
        assertEquals(Optional.empty<ClassFileVersion>(),
                opcode.lastVersionSupporting)
    }

    //
    // INTERNALS
    //

    private fun badUnsignedByte(value: Int) : Executable {
        return Executable {
            assertThrows(IllegalArgumentException::class.java) {
                TwoOperandOpcode.forUnsignedByte(value)
            }
        }
    }
}

/**
 * Unit tests for [VariableOperandOpcode].
 *
 * @author Victor Schappert
 * @since 20171228
 */
class VariableOperandOpcodeTest {
    @Test
    fun uniqueOpcodeValues() {
        assertUniqueValuesByEnumerator(VariableOperandOpcode::class,
                "getValue()", VariableOperandOpcode::getValue)
    }

    @ParameterizedTest
    @EnumSource(VariableOperandOpcode::class)
    fun forUnsignedByteAllOpcodes(opcode: VariableOperandOpcode) {
        assertEquals(opcode, VariableOperandOpcode.forUnsignedByte(opcode.value))
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
    fun forUnsignedByteInRangeButNoOpcode() {
        val opcodes = VariableOperandOpcode.values()
                .map(Opcode::getValue)
                .toSet()
        // For each unsigned byte value that does NOT correspond to a valid JVM
        // opcode, verify that trying to obtain it from Opcode.forUnsignedByte()
        // throws an exception.
        (0..255).filter{ !opcodes.contains(it) }
                .forEach {  badUnsignedByte(it).execute() }
    }

    @ParameterizedTest
    @EnumSource(VariableOperandOpcode::class)
    fun operands(opcode: VariableOperandOpcode) {
        when (opcode) {
            LOOKUPSWITCH -> assertAll(
                Executable { assertEquals(3, opcode.numOperands) },
                Executable {
                    assertEquals(listOf(BRANCH_OFFSET_INT, SIGNED_VALUE_INT,
                            MATCH_OFFSET_PAIR_TABLE), opcode.operandTypes) })
            TABLESWITCH -> assertAll(
                    Executable { assertEquals(4, opcode.numOperands) },
                    Executable {
                        assertEquals(listOf(BRANCH_OFFSET_INT, SIGNED_VALUE_INT,
                                SIGNED_VALUE_INT, JUMP_OFFSET_TABLE),
                                opcode.operandTypes) })
            WIDE -> assertAll(
                    Executable { assertEquals(3, opcode.numOperands) },
                    Executable {
                        assertEquals(listOf(OPCODE, LOCAL_VARIABLE_INDEX_SHORT,
                                OPTIONAL_SIGNED_VALUE_SHORT),
                                opcode.operandTypes) })
            else -> throw IllegalStateException("Unexpected opcode: $opcode")
        }
    }

    @ParameterizedTest
    @EnumSource(VariableOperandOpcode::class)
    fun isntVariableSize(opcode: VariableOperandOpcode) {
        assertFalse(opcode.isVariableSize)
    }

    @ParameterizedTest
    @EnumSource(VariableOperandOpcode::class, mode = EXCLUDE, names = ["WIDE"])
    fun isPadded(opcode: VariableOperandOpcode) {
        assertTrue(opcode.isPadded)
    }

    @ParameterizedTest
    @EnumSource(VariableOperandOpcode::class, mode = INCLUDE, names = ["WIDE"])
    fun isntPadded(opcode: VariableOperandOpcode) {
        assertFalse(opcode.isPadded)
    }

    @ParameterizedTest
    @EnumSource(VariableOperandOpcode::class)
    fun isntReserved(opcode: VariableOperandOpcode) {
        assertFalse(opcode.isReserved)
    }

    @ParameterizedTest
    @EnumSource(VariableOperandOpcode::class)
    fun firstVersionSupportingIsJava1(opcode: VariableOperandOpcode) {
        assertEquals(JAVA1, opcode.firstVersionSupporting)
    }

    @ParameterizedTest
    @EnumSource(VariableOperandOpcode::class)
    fun deprecatedNever(opcode: VariableOperandOpcode) {
        assertEquals(Optional.empty<ClassFileVersion>(),
                opcode.lastVersionSupporting)
    }

    //
    // INTERNALS
    //

    private fun badUnsignedByte(value: Int) : Executable {
        return Executable {
            assertThrows(IllegalArgumentException::class.java) {
                VariableOperandOpcode.forUnsignedByte(value)
            }
        }
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

