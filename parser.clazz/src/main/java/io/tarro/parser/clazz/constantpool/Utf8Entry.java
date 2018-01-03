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

package io.tarro.parser.clazz.constantpool;

import io.tarro.base.constantpool.ConstantPoolTag.AssociatedWith;

import static io.tarro.base.constantpool.ConstantPoolTag.UTF8;
import static java.util.Objects.requireNonNull;

/**
 * <p>
 * Constant pool entry consisting of a modified UTF-8 string.
 * </p>
 *
 * <p>
 * This class is named for consistency with the Java Virtual Machine
 * specification's chapter on the class file format. However, it truly contains
 * not the modified UTF-8 string as literally present in the class file, but the
 * result of parsing the modified UTF-8 string representation into a Java
 * string. Thus a user of this class need not be at all concerned with the
 * actual modified UTF-8 format.
 * </p>
 *
 * @author Victor Schappert (schapper@)
 * @since 20171014
 */
@AssociatedWith(UTF8)
public class Utf8Entry extends ConstantPoolEntry {

    //
    // CONSTRUCTORS
    //

    private final String value;

    //
    // CONSTRUCTORS
    //

    /**
     * Creates the string constant.
     *
     * @param value Parsed representation of the modified UTF-8 string as a Java
     *              string
     */
    public Utf8Entry(final String value) {
        super(UTF8);
        this.value = requireNonNull(value, "value cannot be null");
    }

    //
    // PUBLIC METHODS
    //

    /**
     * Obtains the parsed representation of the modified UTF-8 string as a Java
     * string.
     *
     * @return String constant value
     */
    public String getValue() {
        return value;
    }
}
