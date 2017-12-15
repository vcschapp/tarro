/*
MIT License

Copyright (c) 2017 Victor Schappert

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package io.tarro.base.attribute;

import io.tarro.base.Valued;

/**
 * <p>
 * Enumerates valid tag values for an {@code element_value} structure appearing
 * within any of the {@linkplain AttributeType predefined attributes} which
 * contain annotations.
 * </p>
 *
 * <p>
 * Each enumerator in this enumeration represents one of the possible element
 * value tags described in the Java Virtual Machine Specification. The actual
 * tag character may be obtained from the {@link #getValue()} method, as an
 * {@code int}. If it is necessary to convert the value into a character for
 * some reason, this may be done by simply casting it to type {@code char}.
 * </p>
 *
 * @author Victor Schappert
 * @since 20171016
 */
public enum ElementValueTag implements Valued {

    //
    // ENUMERATORS
    //

    // Keep this list in lexicographical order by tag.
    /**
     * <p>
     * Identifies a constant value of primitive type {@code byte} and containing
     * a {@code const_value_index} item referring to a
     * {@code CONSTANT_Integer_info} structure in the constant pool.
     * </p>
     *
     * @see #CHAR
     * @see #DOUBLE
     * @see #FLOAT
     * @see #LONG
     * @see #SHORT
     * @see #BOOLEAN
     */
    BYTE('B'),
    /**
     * <p>
     * Identifies a constant value of primitive type {@code char} and containing
     * a {@code const_value_index} item referring to a
     * {@code CONSTANT_Integer_info} structure in the constant pool.
     * </p>
     *
     * @see #BYTE
     * @see #DOUBLE
     * @see #FLOAT
     * @see #LONG
     * @see #SHORT
     * @see #BOOLEAN
     */
    CHAR('C'),
    /**
     * <p>
     * Identifies a constant value of primitive type {@code double} and
     * containing a {@code const_value_index} item referring to a
     * {@code CONSTANT_Double_info} structure in the constant pool.
     * </p>
     *
     * @see #BYTE
     * @see #CHAR
     * @see #FLOAT
     * @see #LONG
     * @see #SHORT
     * @see #BOOLEAN
     */
    DOUBLE('D'),
    /**
     * <p>
     * Identifies a constant value of primitive type {@code double} and
     * containing a {@code const_value_index} item referring to a
     * {@code CONSTANT_Double_info} structure in the constant pool.
     * </p>
     *
     * @see #BYTE
     * @see #CHAR
     * @see #FLOAT
     * @see #LONG
     * @see #SHORT
     * @see #BOOLEAN
     */
    FLOAT('F'),
    /**
     * <p>
     * Identifies a constant value of primitive type {@code float} and
     * containing a {@code const_value_index} item referring to a
     * {@code CONSTANT_Float_info} structure in the constant pool.
     * </p>
     *
     * @see #BYTE
     * @see #CHAR
     * @see #DOUBLE
     * @see #LONG
     * @see #SHORT
     * @see #BOOLEAN
     */
    INT('I'),
    /**
     * <p>
     * Identifies a constant value of primitive type {@code long} and containing
     * a {@code const_value_index} item referring to a
     * {@code CONSTANT_Long_info} structure in the constant pool.
     * </p>
     *
     * @see #BYTE
     * @see #CHAR
     * @see #DOUBLE
     * @see #FLOAT
     * @see #SHORT
     * @see #BOOLEAN
     */
    LONG('J'),
    /**
     * <p>
     * Identifies a constant value of primitive type {@code short} and
     * containing a {@code const_value_index} item referring to a
     * {@code CONSTANT_Integer_info} structure in the constant pool.
     * </p>
     *
     * @see #BYTE
     * @see #CHAR
     * @see #DOUBLE
     * @see #FLOAT
     * @see #LONG
     * @see #BOOLEAN
     */
    SHORT('S'),
    /**
     * <p>
     * Identifies a constant value of primitive type {@code boolean} and
     * containing a {@code const_value_index} item referring to a
     * {@code CONSTANT_Integer_info} structure in the constant pool.
     * </p>
     *
     * @see #BYTE
     * @see #CHAR
     * @see #DOUBLE
     * @see #FLOAT
     * @see #LONG
     * @see #SHORT
     */
    BOOLEAN('Z'),
    /**
     * Identifies a constant value of type {@code String} and containing a
     * {@code const_value_index} item referring to a {@code CONSTANT_Utf8_info}
     * structure in the constant pool.
     */
    STRING('s'),
    /**
     * Identifies a constant value representing a member of an enumeration class
     * and containing an {@code enum_const_value} item referring to an
     * {@code enum} class and one of its enumerated members.
     */
    ENUM('e'),
    /**
     * Identifies a constant value representing a class literal and containing a
     * {@code class_info_index} item referring to a
     * {@code CONSTANT_Utf8_info} structure in the constant pool.
     */
    CLASS('c'),
    /**
     * <p>
     * Identifies a constant value representing a nested annotation and
     * containing an {@code annotation} structure describing the nested
     * annotation value.
     * </p>
     *
     * <p>
     * Nested annotations, in conjunction with arrays, are the only way in which
     * the annotation system supports decorating Java language entities with
     * highly-structured data (other than, say, embedding it in a string) since
     * annotations may not "contain" arbitrary class instances. For the purposes
     * of illustration only, the following Java code declares a nested
     * annotation:
     * </p>
     *
     * <blockquote>
     * <pre><code> class Foo {
     *     {@literal @}interface Bar {
     *         SuppressWarnings value();
     *     }
     *     {@literal @}Baz({@literal @}SuppressWarnings("unchecked"))
     *     int baz;
     * }</code></pre>
     * </blockquote>
     */
    ANNOTATION('@'),
    /**
     * Identifies a constant value representing an array literal and containing
     * an array of {@code element_value} items representing the items in the
     * array.
     */
    ARRAY('[');

    //
    // DATA
    //

    private final int tag;

    //
    // CONSTRUCTORS
    //

    ElementValueTag(final char tag) {
        this.tag = tag;
    }

    //
    // INTERFACE: Valued
    //

    /**
     * {@inheritDoc}
     *
     * <p>
     * The enumerator's value is its <em>tag</em>, as documented in the Java
     * Virtual Machine specification, represented as an {@code int}. To obtain
     * the character value of the tag, simply cast the return value of this
     * method to {@code char}.
     * </p>
     */
    @Override
    public int getValue() {
        return tag;
    }
}
