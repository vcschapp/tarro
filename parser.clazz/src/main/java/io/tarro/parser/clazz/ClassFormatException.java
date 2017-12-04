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

package io.tarro.parser.clazz;

import io.tarro.base.PinpointFormatException;

import static java.util.Objects.requireNonNull;

/**
 * Thrown by the parser when it detects an error in the format of the class file
 * being parsed.
 *
 * @author Victor Schappert
 * @since 20171008
 */
public class ClassFormatException extends PinpointFormatException {

    //
    // DATA
    //

    private final String positionContext;

    //
    // CONSTRUCTORS
    //

    /**
     * Constructs a {@link ClassFormatException} with the specified detail
     * message.
     *
     * @param message Detail message
     * @param position Position of the error within the class file stream
     * @param positionContext Non-{@code null} additional context about the
     *                        position of the error
     */
    public ClassFormatException(final String message, final int position, final String positionContext) {
        this(message, null, position, positionContext);
    }

    /**
     * Constructs a {@link ClassFormatException} with the specified detail
     * message and cause.
     *
     * @param message Detail message
     * @param cause Cause
     * @param position Position of the error within the class file stream
     * @param positionContext Non-{@code null} additional context about the
     *                        position of the error
     */
    public ClassFormatException(final String message, final Throwable cause, final int position,
            final String positionContext) {
        super(message, cause, position);
        this.positionContext = requireNonNull(positionContext, "positionContext cannot be null");
    }

    //
    // PUBLIC METHODS
    //

    /**
     * <p>
     * Obtains additional context about the position of the error.
     * </p>
     *
     * <p>
     * The string returned, which is never {@code null}, is a dot-separated
     * path to the class file entity where the parsing error was detected. For
     * example, if the error occurs while trying to read the {@code name_index}
     * field of the second method in the method table, the position context is
     * {@code "methods[1].name_index"}.
     * </p>
     *
     * @return Non-{@code null} additional context string
     */
    public final String getPositionContext() {
        return positionContext;
    }
}