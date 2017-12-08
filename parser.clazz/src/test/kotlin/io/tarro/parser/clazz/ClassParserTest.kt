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
import io.tarro.test.Slow
import io.tarro.test.javaRuntimesNanoTimings
import org.junit.jupiter.api.Test
import java.lang.System.nanoTime
import java.time.Duration.ofNanos

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
        val parser = builder().build()
        val perClassTimings = javaRuntimesNanoTimings {
            parser.parse(ByteBufferInputStream(it.data))
        }
        val totalTime = ofNanos(nanoTime() - start)
        val parseTime = ofNanos(perClassTimings.map { it.value }.sum())
        println("Elapsed time: parsing - $parseTime - total - $totalTime")
    }
}

