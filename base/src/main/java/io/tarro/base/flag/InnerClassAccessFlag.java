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
 * Enumerates flags available on the {@code access_flags} field of an
 * {@code InnerClass} attribute.
 *
 * @author Victor Schappert
 * @since 20171125
 */
public enum InnerClassAccessFlag implements Flag {

    //
    // ENUMERATORS
    //

    PUBLIC(0x0001, ClassFileVersion.JAVA1_1),
    PRIVATE(0x0002, ClassFileVersion.JAVA1_1),
    PROTECTED(0x0004, ClassFileVersion.JAVA1_1),
    STATIC(0x0008, ClassFileVersion.JAVA1_1),
    FINAL(0x0010, ClassFileVersion.JAVA1_1),
    INTERFACE(0x0200, ClassFileVersion.JAVA1_1),
    ABSTRACT(0x0400, ClassFileVersion.JAVA1_1),
    SYNTHETIC(0x1000, ClassFileVersion.JAVA7),
    ANNOTATION(0x2000, ClassFileVersion.JAVA7),
    ENUM(0x4000, ClassFileVersion.JAVA7);

    //
    // DATA
    //

    private final int value;
    private final ClassFileVersion classFileVersion;

    //
    // CONSTRUCTORS
    //

    InnerClassAccessFlag(final int value, final ClassFileVersion classFileVersion) {
        assert 0 == (value & 0xffff0000) : "Value must only occupy low-order 16 bits";
        assert 1 == bitCount(value) : "Value must have exactly one bit set";
        this.value = value;
        this.classFileVersion = classFileVersion;
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
    public ClassFileVersion getFirstVersionSupporting() {
        return classFileVersion;
    }
}
