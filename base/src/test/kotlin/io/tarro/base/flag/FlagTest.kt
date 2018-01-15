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

package io.tarro.base.flag

import io.tarro.base.flag.FakeFlag.BAR
import io.tarro.base.flag.FakeFlag.BAZ
import io.tarro.base.flag.FakeFlag.FOO
import io.tarro.base.flag.Flag.parse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

/**
 * Unit tests for [Flag].
 *
 * @author Victor Schappert
 * @since 20180114
 */
class FlagTest {
    @Test
    fun parseZero() = assertEquals(emptySet<FakeFlag>(), parse(FakeFlag::class.java, 0))

    @Test
    fun parseIgnore() = assertEquals(emptySet<FakeFlag>(), parse(FakeFlag::class.java,
            0xffffff00.toInt()))

    @Test
    fun parseOne() {
        assertEquals(hashSetOf(FOO), parse(FakeFlag::class.java, 1))
    }

    @Test
    fun parsTwo() {
        assertEquals(hashSetOf(FOO, BAZ), parse(FakeFlag::class.java, 5))
    }

    @Test
    fun parseThree() {
        assertEquals(hashSetOf(FOO, BAR, BAZ), parse(FakeFlag::class.java, 7))
    }

    @Test
    fun parseBothValidAndInvalid() {
        assertEquals(hashSetOf(BAR, BAZ), parse(FakeFlag::class.java, 0xfffffffe.toInt()))
    }
}

/**
 * Simple implementation of the [Flag] interface for test purposes.
 *
 * @author Victor Schappert
 * @since 20180114
 */
enum class FakeFlag : Flag {
    //
    // ENUMERATORS
    //

    FOO, BAR, BAZ;

    //
    // INTERFACE: Flag
    //

    override fun getFlagName() = "ACC_$name"

    //
    // INTERFACE: Valued
    //

    override fun getValue() = 1 shl ordinal

    //
    // INTERFACE: Versioned
    //

    override fun getFirstVersionSupporting() = throw NotImplementedError()
}
