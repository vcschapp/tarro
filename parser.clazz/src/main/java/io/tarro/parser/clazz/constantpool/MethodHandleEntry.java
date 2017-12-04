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

import io.tarro.base.constantpool.MethodHandleReferenceKind;
import io.tarro.base.constantpool.Represents;

import static io.tarro.base.constantpool.ConstantPoolTag.METHOD_HANDLE;
import static java.util.Objects.requireNonNull;

/**
 * Constant pool entry representing a method handle. The entry has a
 * {@linkplain #getReferenceIndex reference index} which is an index into the
 * constant pool; and a {@linkplain #getReferenceKind() referenceKind
 * enumerator} which indicates how the reference index should be interpreted.
 *
 * @author Victor Schappert (schapper@)
 * @since 20171014
 */
@Represents(METHOD_HANDLE)
public final class MethodHandleEntry extends ConstantPoolEntry {

    //
    // DATA
    //

    private final MethodHandleReferenceKind referenceKind;
    private final int referenceIndex;

    //
    // CONSTRUCTORS
    //

    public MethodHandleEntry(final MethodHandleReferenceKind referenceKind, final int referenceIndex) {
        super(METHOD_HANDLE);
        this.referenceKind = requireNonNull(referenceKind, "referenceKind cannot be null");
        this.referenceIndex = referenceIndex;
    }

    //
    // PUBLIC METHODS
    //

    public MethodHandleReferenceKind getReferenceKind() {
        return referenceKind;
    }

    public int getReferenceIndex() {
        return referenceIndex;
    }
}
