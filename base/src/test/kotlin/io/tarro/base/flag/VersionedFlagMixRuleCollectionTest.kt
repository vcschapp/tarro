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

import io.tarro.base.ClassFileVersion
import io.tarro.base.ClassFileVersion.JAVA1_2
import io.tarro.base.flag.FakeFlag.BAR
import io.tarro.base.flag.FakeFlag.FOO
import io.tarro.base.flag.FlagMixRule.noneOf
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.util.Optional.empty

/**
 * Unit tests for [VersionedFlagMixRuleCollection].
 *
 * @author Victor Schappert
 * @since 20180203
 */
class VersionedFlagMixRuleCollectionTest {

    @Test
    fun firstSupportingVersion() {
        assertEquals(JAVA1_2, collection.firstVersionSupporting)
    }

    @Test
    fun lastSupportingVersion() {
        assertEquals(empty<ClassFileVersion>(), collection.lastVersionSupporting)
    }

    @Test
    fun rules() {
        assertEquals(1, collection.rules.size)
        assertEquals(rule, collection.rules.first())
    }

    @Test
    fun immutable() {
        assertThrows(UnsupportedOperationException::class.java) {
            (collection.rules as MutableList).removeAt(0)
        }
    }

    //
    // INTERNALS
    //

    private val rule = noneOf("a fake flag", FOO, BAR)

    private val collection = VersionedFlagMixRuleCollection(JAVA1_2, arrayOf(rule))
}
