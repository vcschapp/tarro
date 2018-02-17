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

package io.tarro.base.flag;

import io.tarro.base.ClassFileVersion;

import static io.tarro.base.ClassFileVersion.JAVA1_1;
import static io.tarro.base.ClassFileVersion.JAVA5;
import static io.tarro.base.ClassFileVersion.JAVA7;

/**
 * <p>
 * Enumerates flags available on the {@code access_flags} field of an
 * {@code InnerClass} attribute.
 * </p>
 *
 * <p>
 * Inner class access flags are used by the Java compiler to recover original
 * information about the inner class when the source code from which the class
 * was compiled is no longer available.
 * </p>
 *
 * @author Victor Schappert
 * @since 20171125
 * @see ClassAccessFlag
 */
public enum InnerClassAccessFlag implements Flag {

    //
    // ENUMERATORS
    //

    /**
     * {@code 0x0001} ({@code ACC_PUBLIC}): Indicates the inner class is
     * marked or implicitly {@code public} in the source code.
     *
     * @see #PRIVATE
     * @see #PROTECTED
     */
    PUBLIC(0x0001),
    /**
     * {@code 0x0002} ({@code ACC_PRIVATE}): Indicates the inner class is
     * explicitly marked {@code private} in the source code.
     *
     * @see #PUBLIC
     * @see #PROTECTED
     */
    PRIVATE(0x0002),
    /**
     * {@code 0x0004} ({@code ACC_PROTECTED}): Indicates the inner class is
     * explicitly marked {@code protected} in the source code.
     *
     * @see #PUBLIC
     * @see #PRIVATE
     */
    PROTECTED(0x0004),
    /**
     * {@code 0x0008} ({@code ACC_STATIC}): Indicates the inner class is
     * marked or implicitly {@code static} in the source code.
     */
    STATIC(0x0008),
    /**
     * {@code 0x0010} ({@code ACC_FINAL}): Indicates the inner class is
     * marked or implicitly {@code final} in the source code.
     */
    FINAL(0x0010),
    /**
     * {@code 0x0200} ({@code ACC_INTERFACE}): Indicates the inner class is
     * declared an {@code interface} in the source.
     */
    INTERFACE(0x0200),
    /**
     * {@code 0x0400} ({@code ACC_ABSTRACT}): Indicates the inner class is
     * marked or implicitly {@code abstract} in the source.
     */
    ABSTRACT(0x0400),
    /**
     * {@code 0x1000} ({@code ACC_SYNTHETIC}): Indicates the inner class is
     * synthetic, that is, not explicitly declared in the source code.
     */
    SYNTHETIC(0x1000, JAVA5),
    /**
     * {@code 0x2000} ({@code ACC_ANNOTATION}): Indicates the inner class is
     * declared an annotation type in the source code.
     */
    ANNOTATION(0x2000, JAVA7),
    /**
     * {@code 0x4000} ({@code ACC_ENUM}): Indicates the inner class is declared
     * an {@code enum} type in the source code.
     */
    ENUM(0x4000, JAVA7);

    //
    // DATA
    //

    private final int value;
    private final ClassFileVersion classFileVersion;

    //
    // CONSTRUCTORS
    //

    InnerClassAccessFlag(final int value, final ClassFileVersion classFileVersion) {
        this.value = value;
        this.classFileVersion = classFileVersion;
    }

    InnerClassAccessFlag(final int value) {
        this(value, JAVA1_1);
    }

    //
    // INTERFACE: Flag
    //

    @Override
    public String getFlagName() {
        return "ACC_" + name();
    }

    //
    // INTERFACE: Valued
    //

    /**
     * {@inheritDoc}
     *
     * <p>
     * This method returns the individual flag's integer value, which is
     * guaranteed to be a power of two distinct from any other flag in the
     * enumeration, since the {@code access_flags} member of class file is a
     * bitmask.
     * </p>
     *
     * @return Flag bit
     */
    @Override
    public int getValue() {
        return value;
    }

    //
    // INTERFACE: Versioned
    //

    @Override
    public ClassFileVersion getFirstVersionSupporting() {
        return classFileVersion;
    }
}
