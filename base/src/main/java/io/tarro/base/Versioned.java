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

package io.tarro.base;

import java.util.Optional;

import static java.util.Optional.empty;

/**
 * Object only available in particular Java class file versions.
 *
 * @author Victor Schappert
 * @since 20171014
 */
public interface Versioned {
    /**
     * Obtains the first Java class file version in which this object is
     * supported.
     *
     * @return Class file version
     */
    ClassFileVersion getFirstVersionSupporting();

    /**
     * If this object is deprecated, indicates the last Java class file version
     * in which this object is supported.
     *
     * @return Either the empty value, if this object is not deprecated, or a
     *         non-empty value representing the final class file version that
     *         supports this object, if it is deprecated
     */
    default Optional<ClassFileVersion> getLastVersionSupporting() {
        return empty();
    }
}
