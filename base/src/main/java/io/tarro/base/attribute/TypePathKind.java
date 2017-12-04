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

package io.tarro.base.attribute;

import io.tarro.base.Valued;

/**
 * Enumerates the kinds of type path "steps" in a {@code type_path} structure.
 *
 * @author Victor Schappert
 * @since 20171113
 */
public enum TypePathKind implements Valued {

    //
    // ENUMERATORS
    //

    DEEPER_IN_ARRAY_TYPE,
    DEEPER_IN_NESTED_TYPE,
    ON_TYPE_ARGUMENT_WILDCARD_BOUND,
    ON_TYPE_ARGUMENT;

    //
    // INTERFACE: Valued
    //

    @Override
    public int getValue() {
        return ordinal();
    }
}