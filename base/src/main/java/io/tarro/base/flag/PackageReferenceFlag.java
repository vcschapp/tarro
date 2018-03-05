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

import static io.tarro.base.ClassFileVersion.JAVA9;

/**
 * Enumerates flags available in two places within the {@code Module} attribute:
 * the {@code exports_flags} field of an element in the {@code exports} array;
 * and the {@code opens_flags} field of an element in the {@code opens} array.
 *
 * @author Victor Schappert
 * @since 20171126
 * @see io.tarro.base.attribute.AttributeType#MODULE
 * @see ModuleReferenceFlag
 * @see ModuleFlag
 */
public enum PackageReferenceFlag implements Flag {

    //
    // ENUMERATORS
    //

    /**
     * {@code 0x1000} ({@code ACC_SYNTHETIC}): Indicates the package export (if
     * this flag appears in {@code exports_flags} field on an element in the
     * {@code exports} array) or package opening (if this flag appears in the
     * {@code opens_flags} field on an element of the {@code opens} array) was
     * not explicitly or implicitly declared in the source of the module
     * declaration.
     *
     * @see #MANDATED
     */
    SYNTHETIC(0x1000),
    /**
     * {@code 0x8000} ({@code ACC_MANDATED}): Indicates the package export (if
     * this flag appears in {@code exports_flags} field on an element in the
     * {@code exports} array) or package opening (if this flag appears in the
     * {@code opens_flags} field on an element of the {@code opens} array) was
     * implicitly declared in the source of the module declaration.
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

    PackageReferenceFlag(final int value) {
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
        return JAVA9;
    }
}
