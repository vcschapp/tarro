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

import io.tarro.base.flag.PackageReferenceFlag;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.IntStream;

import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;

/**
 * <p>
 *
 * </p>
 *
 * @author Victor Schappert
 * @since 20171126
 */
public final class PackageReference {

    //
    // DATA
    //

    private final int packageInfoIndex;
    private final Set<PackageReferenceFlag> packageReferenceFlags;
    private final int[] moduleInfoIndices;

    //
    // CONSTRUCTORS
    //

    public PackageReference(final int packageInfoIndex, final EnumSet<PackageReferenceFlag> packageReferenceFlags,
        final int[] moduleInfoIndices) {
        this.packageInfoIndex = packageInfoIndex;
        this.packageReferenceFlags = Collections.unmodifiableSet(packageReferenceFlags);
        this.moduleInfoIndices = requireNonNull(moduleInfoIndices, "moduleInfoIndices cannot be null");
    }

    //
    // PUBLIC METHODS
    //

    public int getPackageInfoIndex() {
        return packageInfoIndex;
    }

    public Set<PackageReferenceFlag> getPackageReferenceFlags() {
        return packageReferenceFlags;
    }

    public IntStream getModuleInfoIndices() {
        return stream(moduleInfoIndices);
    }
}
