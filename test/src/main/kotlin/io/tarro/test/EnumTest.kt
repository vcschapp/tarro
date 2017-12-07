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

package io.tarro.test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.function.Executable
import kotlin.reflect.KClass

/**
 * Asserts that every enumerator belonging to the given `enum` class has a
 * unique value for the given member.
 *
 * @param clazz Enumeration class
 * @param memberName Name of the member (for reporting purposes)
 * @param member Member
 * @param E Type of the enumeration class
 * @param T Type of the member
 */
fun <E: Enum<E>, T> assertUniqueValuesByEnumerator(clazz: KClass<E>, memberName: String, member: (E) -> T) {
    assertAll(
        clazz.java.enumConstants
                .groupBy(member)
                .map { Executable { assertEquals(1, it.value.size,
                        "${name(clazz)} should only have one enumerator whose " +
                                "$memberName yields value ${it.key}, but " +
                                "${it.value.size} were found: ${it.value}") } }
                .stream()
    )
}

private fun <E: Enum<E>> name(clazz: KClass<E>) = "Enumeration ${clazz.java.name}"