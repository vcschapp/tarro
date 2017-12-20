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

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Disabled

import org.junit.jupiter.api.Test
import java.lang.Long.bitCount
import java.lang.reflect.Modifier.PRIVATE
import java.lang.reflect.Modifier.STATIC
import java.lang.String.format
import java.lang.reflect.Modifier.FINAL
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.jvm.javaField

/**
 * Generic test for any class whose only purpose is to export constant values.
 *
 * @author Victor Schappert
 * @since 20171204
 * @property clazz Class under test
 */
@Disabled
open class ConstantContainerTest<T: Any>(val clazz: KClass<T>) {

    /**
     * Verifies that the class under test is final.
     */
    @Test
    open fun classIsFinal() {
        // If you are a huge nerd, you may enjoy the fact that this method
        // *could* be named 'final' in Kotlin. That would make re-use from Java
        // rather painful, however, so let's not.
        assertEquals(FINAL, clazz.java.modifiers and FINAL, "$must be final")
    }

    /**
     * Verifies that the class under test only has a private no-argument
     * constructor and no other constructors.
     */
    @Test
    open fun privateConstructor() {
        val message = "$must have only a single, private, no-args, constructor"
        clazz.java.declaredConstructors
                .asList()
                .onEach {
                    assertEquals(0, it.parameters.size, message)
                    assertEquals(PRIVATE, it.modifiers, message)
                }
        assertEquals(1, clazz.java.declaredConstructors.size, message)
    }

    /**
     * Verifies that all of the fields on the class under test are static
     * fields.
     */
    @Test
    open fun onlyStaticFields() {
        fields.onEach {
            assertTrue(STATIC == STATIC and it.modifiers, "$must have only static fields")
        }
    }

    /**
     * Verifies that each visible field on the class under test has the same
     * type.
     */
    @Test
    open fun eachVisibleFieldHasSameType() {
        assertEquals(1, visibleFields.map { it.type }.distinct().count(),
                "$must use the same type for all fields")
    }

    /**
     * Verifies that each visible field on the class under test has a different
     * value.
     */
    @Test
    open fun eachUniqueFieldHasDifferentValue() {
        uniqueFields.groupBy { it.get(null) }
                .onEach {
                    assertEquals(1, it.value.size,
                        "$must have distinct values for each field, but the value" +
                                "${it.key} is shared by ${it.value.map { it.name }}")
                }
    }

    //
    // INTERNALS
    //

    private val fields = clazz.java.declaredFields.asList()

    private val must = "As a constant container, ${clazz.java.simpleName} must"

    //
    // PROPERTIES
    //

    /**
     * Lists all visible (non-private) declared fields on the class under test.
     *
     * @see uniqueFields
     */
    val visibleFields = fields.filter { 0 == PRIVATE and it.modifiers }

    /**
     * Lists all fields on the class under test which should have a unique
     * value. Typically this is the same as [visibleFields], but derived test
     * classes may override this property to produce different effects.
     *
     * @see visibleFields
     */
    open val uniqueFields = visibleFields
}

/**
 * Generic test code for any class whose only purpose is to single bit integer
 * constants for use in bitmasks.
 *
 * @author Victor Schappert
 * @since 20171204
 * @property clazz Class under test
 * @property allField Property of the class under test representing all of the
 *                    single-bit constants *or*-ed together
 */
@Disabled
open class SingleBitMaskConstantContainerTest<T: Any>(
        clazz: KClass<T>, private val allField: KProperty<Number>) :
        ConstantContainerTest<T>(clazz) {

    /**
     * Verifies that each visible field on the class under test has a primitive
     * integer type (*ie*, in Java, `int` or `long`).
     *
     * The [eachVisibleFieldHasSameType] method complements this one by ensuring
     * that each visible field also has the same type (thus preventing the
     * intermixing of `int` and `long` valued fields).
     */
    @Test
    open fun primitiveIntegerTypeFieldsOnly() {
        assertEquals(0, visibleFields.filter { !INTEGER_TYPES.contains(it.type) }.count(),
                "$must contain only primitive integer constants")
    }

    /**
     * Verifies that each visible field on the class under test, except any
     * field which contains the value zero, has precisely one bit set.
     *
     * The [eachUniqueFieldHasDifferentValue] method complements this one by,
     * *inter alia*, ensuring that there can be only one zero field.
     */
    @Test
    open fun singleBitValueFieldsOnly() {
        visibleFieldsExcludingAll
                .filter { INTEGER_TYPES.contains(it.type) }
                .map { it to (it.get(null) as Number).toLong() }
                .onEach {
                    val n = bitCount(it.second)
                    assertTrue(n < 2, "$must have only single-bit (or zero) constants but " +
                            "${it.first.name}'s value ${format("0x%08x", it.second)} (${it.second} has $n bits")
                }
    }

    /**
     * Verifies that the designated ["all" field][allField] is exactly equal to
     * the bitwise *or* of all the other fields.
     */
    @Test
    open fun allFieldIsExactlyAllOtherFields() {
        val maskOfAllOtherFields = visibleFieldsExcludingAll
                .map { (it.get(null) as Number).toLong() }
                .reduce { a, b -> a or b }
        val allFieldValue = (allField.javaField?.get(null) as Number).toLong()
        assertEquals(maskOfAllOtherFields, allFieldValue,
                "$must have all field (${allField.name}) that is " +
                        "the bitwise OR of the other constants")
    }

    //
    // INTERNALS
    //

    private val must = "As a container of one-bit mask constants, ${clazz.java.simpleName} must"

    private val visibleFieldsExcludingAll = visibleFields.filter { it != allField.javaField }

    companion object {
        private val INTEGER_TYPES = setOf(Integer.TYPE, java.lang.Long.TYPE)
    }
}