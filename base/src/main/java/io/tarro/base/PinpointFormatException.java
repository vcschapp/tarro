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

/**
 * Thrown when an argument to a method contains a formatting error at a
 * specific zero-based byte position.
 *
 * @author Victor Schappert
 * @since 20171130
 */
public class PinpointFormatException extends IllegalArgumentException {

    //
    // DATA
    //

    private final int position;

    //
    // CONSTRUCTORS
    //

    /**
     * Creates an exception referring to the given position.
     *
     * @param message Detail message
     * @param cause Cause
     * @param position Zero-based byte position of the error
     */
    public PinpointFormatException(final String message, final Throwable cause,
                                   final int position) {
        super(message, cause);
        this.position = position;
    }

    //
    // PUBLIC METHODS
    //

    /**
     * Obtains the position at which the formatting error that triggered this
     * exception occurs.
     *
     * @return Zero-based byte position where the format error occurs
     */
    public final int getPosition() {
        return position;
    }

    //
    // INTERFACE: Serializable
    //

    private static final long serialVersionUID = 1L;
}
