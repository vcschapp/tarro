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

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import java.util.NoSuchElementException

/**
 * Unit tests for file-scope functions.
 *
 * @author Victor Schappert
 * @since 20171208
 */
class JavaRuntimesTest {
    @Test
    fun javaRuntimeVersionsSmokeTest() {
        assertAll(*javaRuntimeVersions.map { version ->
            Executable { assertTrue(Regex("""\d+(\.\d+)*""").matches(version)) }
        }.toTypedArray())
        assertThat(javaRuntimeVersions, `is`<Collection<String>>(not(empty())))
    }

    @Test
    fun javaRuntimesSmokeTest() {
        // Smoke test by verifying there's at least one class file available.
        javaRuntimes().first().let {
            it.openClassFileDataStream().use {
                assertTrue(it.hasNext())
                val classFileData = it.next()
                assertThat(0, lessThan(classFileData.identifier.size))
            }
        }
    }

    @Test
    fun javaRuntimesNanoTimingsSmokeTest() {
        // Smoke test by timing a single operation.
        class Halt : RuntimeException("halt!")
        assertThrows(
            Halt::class.java, {
            javaRuntimesNanoTimings { throw Halt() }
        })
    }

    @Test @Slow
    fun javaRuntimesNanoTimingsFullTest() {
        var totalBytes = 0
        val nanos = javaRuntimesNanoTimings { totalBytes += it.data.remaining() }
                .map { it.value }
                .sum()
        assertThat(nanos, greaterThanOrEqualTo(1L))
    }
}

/**
 * Unit tests for [ClassFileDataStream].
 *
 * @author Victor Schappert
 * @since 20171208
 */
class ClassFileDataStreamTest {
    @Test
    fun tooFar() {
        javaRuntimes().first().openClassFileDataStream().use {
            // Traverse to the end.
            while (it.hasNext()) {
                it.next()
            }
            // Now make sure we get an exception if we try to go past the end.
            assertFalse(it.hasNext()) // Sanity
            assertThrows(NoSuchElementException::class.java, { it.next() })
        }
    }
}
