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

import io.tarro.base.constantpool.ConstantPoolTag;
import io.tarro.base.constantpool.Represents;

import static io.tarro.base.constantpool.ConstantPoolTag.CLASS;
import static io.tarro.base.constantpool.ConstantPoolTag.MODULE;
import static io.tarro.base.constantpool.ConstantPoolTag.PACKAGE;
import static io.tarro.base.constantpool.ConstantPoolTag.STRING;

/**
 * Encapsulates a constant pool entry which refers to another constant pool
 * entry that is a modified UTF-8 string.
 *
 * @author Victor Schappert
 * @since 20171009
 */
@Represents({ CLASS, STRING, MODULE, PACKAGE })
public final class Utf8ReferenceEntry extends ConstantPoolEntry {

    //
    // DATA
    //

    private final int utf8EntryIndex;

    //
    // CONSTRUCTORS
    //

    /**
     * Creates the UTF-8 reference entry.
     *
     * @param constantPoolTag Constant pool tag enumerator indicating a constant
     *                        pool structure which refers to a modified UTF-8
     *                        constant
     * @param utf8EntryIndex Constant pool index of the modified UTF-8 constant
     */
    public Utf8ReferenceEntry(final ConstantPoolTag constantPoolTag, final int utf8EntryIndex) {
        super(constantPoolTag);
        assert CLASS == constantPoolTag || STRING == constantPoolTag ||
                MODULE == constantPoolTag || PACKAGE == constantPoolTag;
        this.utf8EntryIndex = utf8EntryIndex;
    }

    //
    // ACCESSORS
    //

    /**
     * Returns the constant pool index of the modified UTF-8 constant to which
     * this constant pool entry refers.
     *
     * @return Constant pool index
     */
    public int getUtf8EntryIndex() {
        return utf8EntryIndex;
    }
}
