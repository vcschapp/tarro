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

package io.tarro.base;

import static java.lang.String.format;

/**
 * Error thrown when a component encounters a unexpected internal error.
 *
 * @author Victor Schappert
 * @since 20171130
 */
public final class InternalError extends Error {

    //
    // CONSTRUCTORS
    //

    private InternalError(final String message) {
        super(message);
    }

    //
    // PUBLIC STATICS
    //

    public static InternalError internalError(final String format, final Object... args) {
        final String message = format(format, args);
        return new InternalError(message);
    }

    public static <E extends Enum<E>> InternalError unhandledConstant(final Object value) {
        // TODO: Remove unused generic type parameter E
        return internalError("Unhandled value %s", value);
    }

    public static <E extends Enum<E>> InternalError unhandledEnumerator(final E enumerator) {
        return internalError("Unhandled enumerator %s of type %s", enumerator, enumerator.getClass());
    }

    public static InternalError unhandledClassFileVersion(final ClassFileVersion classFileVersion) {
        return internalError("Unhandled class file version: %s", classFileVersion);
    }

    //
    // INTERFACE: Serializable
    //

    private static final long serialVersionUID = 1L;
}
