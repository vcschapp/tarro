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

package io.tarro.clazz.parse.attribute;

import io.tarro.base.flag.InnerClassAccessFlag;

import java.util.EnumSet;
import java.util.Set;

import static java.util.Collections.unmodifiableSet;

/**
 * @author Victor Schappert
 * @since 20171125
 */
public class InnerClass {

    //
    // DATA
    //

    private final int innerClassInfoIndex;
    private final int outerClassInfoIndex;
    private final int innerNameIndex;
    private final Set<InnerClassAccessFlag> accessFlags;

    //
    // CONSTRUCTORS
    //

    public InnerClass(final int innerClassInfoIndex, final int outerClassInfoIndex, final int innerNameIndex,
            final EnumSet<InnerClassAccessFlag> accessFlags) {
        this.innerClassInfoIndex = innerClassInfoIndex;
        this.outerClassInfoIndex = outerClassInfoIndex;
        this.innerNameIndex = innerNameIndex;
        this.accessFlags = unmodifiableSet(accessFlags);
    }

    //
    // PUBLIC METHODS
    //

    public int getInnerClassInfoIndex() {
        return innerClassInfoIndex;
    }

    public int getOuterClassInfoIndex() {
        return outerClassInfoIndex;
    }

    public int getInnerNameIndex() {
        return innerNameIndex;
    }

    public Set<InnerClassAccessFlag> getAccessFlags() {
        return accessFlags;
    }
}
