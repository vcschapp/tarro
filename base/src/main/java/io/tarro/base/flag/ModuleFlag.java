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

import static io.tarro.base.ClassFileVersion.JAVA9;

/**
 * Enumerates flags available on the {@code module_flags} field of a
 * {@code Module} attribute.
 *
 * @author Victor Schappert
 * @since 20171125
 * @see io.tarro.base.attribute.AttributeType#MODULE
 * @see ModuleReferenceFlag
 * @see PackageReferenceFlag
 */
public enum ModuleFlag implements Flag {

    //
    // ENUMERATORS
    //

    /**
     * {@code 0x0020} ({@code ACC_OPEN}): Indicates the module is declared
     * {@code open}, meaning it grants reflective access to its packages.
     */
    OPEN(0x0020),
    /**
     * {@code 0x1000} ({@code ACC_SYNTHETIC}): Indicates the module was not
     * explicitly or implicitly declared.
     *
     * @see #MANDATED
     */
    SYNTHETIC(0x1000),
    /**
     * {@code 0x8000} ({@code ACC_MANDATED}): Indicates the module was not
     * explicitly declared.
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

    ModuleFlag(final int value) {
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
