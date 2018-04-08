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

package io.tarro.bytecode.parse

import io.tarro.test.parseAsByteBuffer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNotSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * Unit tests for [ByteCodeParserBuilder].
 *
 * @author Victor Schappert
 * @since 20180407
 */
class ByteCodeParserBuilderTest {
    @Test
    fun defaultVisitors() {
        ByteCodeParserBuilder().build().parse(NOP.parseAsByteBuffer())
    }

    @Test
    fun withLookupSwitchVisitor() {
        var actualNumPairs = -1
        ByteCodeParserBuilder()
                .withLookupSwitchVisitor { _, _ , numPairs, _ ->
                    actualNumPairs = numPairs
                }
                .build()
                .parse(LOOKUPSWITCH.parseAsByteBuffer())
        assertEquals(1, actualNumPairs)
    }

    @Test
    fun withNoOperandInstructionVisitor() {
        var parsed = false
        ByteCodeParserBuilder()
                .withNoOperandInstructionVisitor { _, _ -> parsed = true }
                .build()
                .parse(NOP.parseAsByteBuffer())
        assertTrue(parsed)
    }

    @Test
    fun withOneOperandInstructionVisitor() {
        var byteValue = -1
        ByteCodeParserBuilder()
                .withOneOperandInstructionVisitor { _, _, operand -> byteValue = operand }
                .build()
                .parse(BIPUSH.parseAsByteBuffer())
        assertEquals(-126, byteValue)
    }

    @Test
    fun withTwoOperandInstructionVisitor() {
        var actualOperand1 = -1
        var actualOperand2 = -1
        ByteCodeParserBuilder()
                .withTwoOperandInstructionVisitor { _, _, operand1, operand2 ->
                    actualOperand1 = operand1
                    actualOperand2 = operand2
                }
                .build()
                .parse(IINC_W.parseAsByteBuffer())
        assertEquals(0x19, actualOperand1)
        assertEquals(0x81, actualOperand2)
    }

    @Test
    fun withTableSwitchInstructionVisitor() {
        var actualDefaultOffset = -1
        ByteCodeParserBuilder()
                .withTableSwitchVisitor { _, defaultOffset, _, _, _ ->
                    actualDefaultOffset = defaultOffset
                }
                .build()
                .parse(TABLESWITCH.parseAsByteBuffer())
        assertEquals(0x22222222, actualDefaultOffset)
    }

    @Test
    fun defaultVisitorsMultipleUse() {
        val builder = ByteCodeParserBuilder()
        val parser1 = builder.build()
        val parser2 = builder.build()
        assertNotNull(parser1)
        assertNotNull(parser2)
        assertNotSame(parser1, parser2)
    }

    @Test
    fun allVisitorsMultipleUse() {
        var timesVisited = hashMapOf<String, Int>()
        val builder = ByteCodeParserBuilder()
        val parser1 = builder.build()
        val sum = { x: Int, y: Int -> x + y }
        parser1.parse(ALL.parseAsByteBuffer())
        assertEquals(timesVisited, emptyMap<String, Int>())
        val parser2 = builder
                .withNoOperandInstructionVisitor { _, _ ->
                    timesVisited.merge("NOP", 1, sum)
                }
                .build()
        assertNotSame(parser1, parser2)
        parser2.parse(ALL.parseAsByteBuffer())
        assertEquals(mapOf("NOP" to 2), timesVisited)
        val parser3 = builder
            .withLookupSwitchVisitor { _, _, _, _ ->
                timesVisited.merge("LOOKUPSWITCH", 1, sum)
            }
            .withOneOperandInstructionVisitor { _, _, _ ->
                timesVisited.merge("BIPUSH", 1, sum)
            }
            .withTwoOperandInstructionVisitor { _, _, _, _ ->
                timesVisited.merge("IINC_W", 1, sum)
            }
            .build()
        assertNotSame(parser2, parser3)
        parser3.parse(ALL.parseAsByteBuffer())
        assertEquals(mapOf("NOP" to 4, "LOOKUPSWITCH" to 1, "BIPUSH" to 1,
                "IINC_W" to 1), timesVisited)
        val parser4 = builder
                .withTableSwitchVisitor { _, _, _, _, _ ->
                    timesVisited.merge("TABLESWITCH", 1, sum)
                }
                .build()
        assertNotSame(parser4, parser3)
        parser4.parse(ALL.parseAsByteBuffer())
        assertEquals(mapOf("NOP" to 6, "LOOKUPSWITCH" to 2, "BIPUSH" to 2,
                "IINC_W" to 2, "TABLESWITCH" to 1), timesVisited)
    }
}

private const val NOP = "00"
private const val LOOKUPSWITCH = "ab 00 00 00  11 11 11 11  00 00 00 01  00 00 00 00 ff ff ff ff"
private const val BIPUSH = "10 82"
private const val IINC_W = "c4 84 00 19 00 81"
private const val TABLESWITCH = "aa 00 00 00   22 22 22 22  00 00 00 00  00 00 00 00 7f ff ff ff"
private const val ALL = "$LOOKUPSWITCH $NOP $NOP $BIPUSH $TABLESWITCH $IINC_W"
