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

import static java.lang.Integer.bitCount;

/**
 * Enumerates flags available on both the {@code exports_flags} field of a
 * an element in the {@code exports} array, and the {@code opens_flags} field of
 * an element in the {@code opens} array, of a {@code Module} attribute.
 *
 * @author Victor Schappert
 * @since 20171126
 */
public enum PackageReferenceFlag implements Flag {

    //
    // ENUMERATORS
    //

    SYNTHETIC(0x1000),
    MANDATED(0x8000);

    //
    // DATA
    //

    private final int value;

    //
    // CONSTRUCTORS
    //

    PackageReferenceFlag(int value) {
        assert 0 == (value & 0xffff0000) : "Value must only occupy low-order 16 bits";
        assert 1 == bitCount(value) : "Value must have exactly one bit set";
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

    @Override
    public int getValue() {
        return value;
    }

    //
    // INTERFACE: Versioned
    //

    @Override
    public ClassFileVersion getClassFileVersion() {
        return ClassFileVersion.JAVA9;
    }
}
