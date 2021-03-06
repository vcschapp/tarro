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

package io.tarro.bytecode.parse;

import io.tarro.base.PinpointFormatException;

/**
 * Thrown by the parser when it detects an error in the format of the bytecode
 * block being parsed.
 *
 * @author Victor Schappert
 * @since 20171130
 */
public class ByteCodeFormatException extends PinpointFormatException {

    //
    // CONSTRUCTORS
    //

    /**
     * Creates an exception pertaining to a format error at a particular
     * position within the bytecode block being parsed.
     *
     * @param message Detail message
     * @param cause Cause
     * @param position Byte position of the error within the bytecode block
     */
    public ByteCodeFormatException(final String message, final Throwable cause,
                                   final int position) {
        super(message, cause, position);
    }

    //
    // INTERFACE: Serializable
    //

    private static final long serialVersionUID = 1L;
}
