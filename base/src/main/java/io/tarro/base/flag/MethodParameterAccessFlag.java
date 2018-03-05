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

package io.tarro.base.flag;

import io.tarro.base.ClassFileVersion;

import static io.tarro.base.ClassFileVersion.JAVA8;

/**
 * <p>
 * Enumerates the flags available for the {@code access_flags} field of an entry
 * in the {@code parameters} table of a {@code MethodParameters} attribute.
 * </p>
 *
 * <p>
 * The {@code MethodParameters} attribute may appear in the {@code attributes}
 * table of a {@code method_info} structure in a class file compiled for
 * {@linkplain io.tarro.base.ClassFileVersion#JAVA8 Java 8} or later. Each entry
 * in the {@code parameters} table of the MethodParameters attribute contains
 * meta information about one of the formal parameters of the method which
 * contains the attribute.
 * </p>
 *
 * @author Victor Schappert
 * @since 20171119
 * @see io.tarro.base.attribute.AttributeType#METHOD_PARAMETERS
 */
public enum MethodParameterAccessFlag implements Flag {

    //
    // ENUMERATORS
    //

    /**
     * {@code 0x0010} ({@code ACC_FINAL}): Indicates the formal parameter
     * to which this flag applies was declared {@code final}.
     */
    FINAL(0x0010),
    /**
     * <p>
     * {@code 0x1000} ({@code ACC_SYNTHETIC}): Indicates the formal parameter to
     * which this flag applies was not explicitly or implicitly declared in
     * source code, according to the specification of the language in which the
     * source code was written.
     * </p>
     *
     * <p>
     * In other words, the formal parameter is an implementation artifact of the
     * compiler which produced this class file.
     * </p>
     *
     * @see #MANDATED
     */
    SYNTHETIC(0x1000),
    /**
     * <p>
     * {@code 0x8000} ({@code ACC_MANDATED}): Indicates the formal parameter to
     * which this flag applies was implicitly declared in source code, according
     * to the specification of the language in which the source code was
     * written.
     *
     * <p>
     * In other words, the formal parameter is mandated by a language
     * specification, so all compilers for the language must emit it.
     * </p>
     *
     * @see #SYNTHETIC
     */
    MANDATED(0x8000);

    //
    // DATA
    //

    private final int value;

    //
    // CONSTRUCTORS
    //

    MethodParameterAccessFlag(final int value) {
        this.value = value;
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
        return JAVA8;
    }
}
