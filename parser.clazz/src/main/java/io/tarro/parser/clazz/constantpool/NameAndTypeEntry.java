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

import io.tarro.base.constantpool.ConstantPoolTag.Represents;

import static io.tarro.base.constantpool.ConstantPoolTag.NAME_AND_TYPE;

/**
 * Constant pool entry consisting of two references to modified UTF-8 constant
 * pool entries: (1) a reference to a name constant containing a field or method
 * name; and (2) a reference to a type constant containing a field or method
 * type descriptor.
 *
 * @author Victor Schappert
 * @since 20171012
 */
@Represents(NAME_AND_TYPE)
public final class NameAndTypeEntry extends ConstantPoolEntry {

    //
    // DATA
    //

    private final int nameIndex;
    private final int decriptorIndex;

    //
    // CONSTRUCTORS
    //

    /**
     * Creates the name plus type entry.
     *
     * @param nameIndex
     *         Index of a modified UTF-8 constant pool entry containing the
     *         field or method name
     * @param decriptorIndex
     *         Index of a modified UTF-8 constant pool entry containing the
     *         field or method type descriptor
     */
    public NameAndTypeEntry(final int nameIndex, final int decriptorIndex) {
        super(NAME_AND_TYPE);
        this.nameIndex = nameIndex;
        this.decriptorIndex = decriptorIndex;
    }

    //
    // PUBLIC METHODS
    //

    /**
     * Obtains the index of the modified UTF-8 constant pool entry containing
     * the field or method name constant.
     *
     * @return Index of field or method name constant
     */
    public int getNameIndex() {
        return nameIndex;
    }

    /**
     * Obtains the index of the modified UTF-8 constant pool entry containing
     * the field or method descriptor.
     *
     * @return Index of field or method type descriptor
     */
    public int getDecriptorIndex() {
        return decriptorIndex;
    }
}
