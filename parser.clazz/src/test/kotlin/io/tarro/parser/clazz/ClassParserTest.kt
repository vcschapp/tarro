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

package io.tarro.parser.clazz

import io.tarro.parser.clazz.ClassParser.builder
import io.tarro.test.ByteBufferInputStream
import io.tarro.test.ClassFileIdentifier
import io.tarro.test.Slow
import io.tarro.test.javaRuntimesNanoTimings
import io.tarro.test.parseAsByteArray
import org.junit.jupiter.api.Test
import java.lang.System.nanoTime
import java.time.Duration.ofNanos

/**
 * Smoke tests designed to catch only the most egregious regressions in the
 * [ClassParser].
 *
 * @author Victor Schappert
 * @since 20171209
 */
class ClassParserSmokeTest {
    @Test
    fun tinyClassNoCode() {
        builder().build().parse(tinyClassNoCode.inputStream())
    }
    // This is "interface A {}" compiled with the Java 9.0.1 compiler and debug
    // symbols turned off.
    private val tinyClassNoCode =
            """
            CA FE BA BE 00 00 00 35 00 05 07 00 03 07 00 04
            01 00 01 41 01 00 10 6A 61 76 61 2F 6C 61 6E 67
            2F 4F 62 6A 65 63 74 06 00 00 01 00 02 00 00 00
            00 00 00 00 00
            """.parseAsByteArray()
}

/**
 * Simplistically regression tests [ClassParser] against every JDK class from
 * one release of the JRE for every major Java release.
 *
 * @author Victor Schappert
 * @since 20171208
 */
class ClassParserAllJREClassesTest {
    @Test @Slow
    fun allJREClasses() {
        val start = nanoTime()
        var count = 0
        val parser = builder().build()
        val perClassTimings = javaRuntimesNanoTimings {
            try {
                parser.parse(ByteBufferInputStream(it.data))
            } catch (e: ClassFormatException) {
                throw AssertionError(parserFailure(count, it.identifier, e), e)
            } catch (e: Exception) {
                throw RuntimeException(nonParserFailure(count, it.identifier), e)
            }
            count++
        }
        val totalTime = ofNanos(nanoTime() - start)
        val parseTime = ofNanos(perClassTimings.map { it.value }.sum())
        println("Elapsed time: parsing - $parseTime - total - $totalTime")
    }

    //
    // INTERNALS
    //

    private fun parserFailure(count: Int,
                              identifier: ClassFileIdentifier,
                              cause: ClassFormatException): String {
        return """
               Parser failure on class #$count - $identifier -
               ${cause.message} -
               context: ${cause.positionContext} -
               position: ${cause.position}
               """.trimIndent()
                  .replace('\n' , ' ')
    }

    private fun nonParserFailure(count: Int, identifier: ClassFileIdentifier) =
            "Non-parser failure on class #$count - $identifier"
}

