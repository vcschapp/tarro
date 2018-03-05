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

package io.tarro.parser.clazz.constantpool;

import io.tarro.base.constantpool.ConstantPoolTag;
import io.tarro.base.constantpool.ConstantPoolTag.AssociatedWith;

import static io.tarro.base.constantpool.ConstantPoolTag.FIELDREF;
import static io.tarro.base.constantpool.ConstantPoolTag.INTERFACE_METHODREF;
import static io.tarro.base.constantpool.ConstantPoolTag.METHODREF;

/**
 * Constant pool entry which contains a reference to a member of a class. In
 * other words, encapsulates either a {@code CONSTANT_Fieldref_info} or a
 * {@code CONSTANT_Methodref_info} structure.
 *
 * @author Victor Schappert
 * @since 20171012
 */
@AssociatedWith({FIELDREF, METHODREF, INTERFACE_METHODREF})
public final class MemberReferenceEntry extends ConstantPoolEntry {

    //
    // DATA
    //

    private final int classIndex;
    private final int nameAndTypeIndex;

    //
    // CONSTRUCTORS
    //

    public MemberReferenceEntry(final ConstantPoolTag constantPoolTag, final int classIndex, final int nameAndTypeIndex) {
        super(constantPoolTag);
        assert FIELDREF == constantPoolTag || METHODREF == constantPoolTag
                || INTERFACE_METHODREF == constantPoolTag;
        this.classIndex = classIndex;
        this.nameAndTypeIndex = nameAndTypeIndex;
    }

    //
    // PUBLIC METHODS
    //

    public final int getClassIndex() {
        return classIndex;
    }

    public final int getNameAndTypeIndex() {
        return nameAndTypeIndex;
    }
}
