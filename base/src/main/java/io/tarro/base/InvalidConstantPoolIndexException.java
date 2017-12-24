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
 * <p>
 * Thrown when an erroneous attempt to subscript an element in a class file's
 * constant pool is detected.
 * </p>
 *
 * <p>
 * Example invalid constant pool index exception scenarios include:
 * </p>
 *
 * <ul>
 * <li>
 * attempting to subscript the constant pool at an index which is not positive;
 * </li>
 * <li>
 * attempting to subscript the constant pool at an index equal to or greater
 * than the {@code constant_pool_count};
 * </li>
 * <li>
 * attempting to subscript the second slot of a two-slot constant pool entry
 * (<i>ie</i> {@code CONSTANT_Double_info} or {@code CONSTANT_Long_info}); and
 * </li>
 * <li>
 * attempting to refer to a constant pool entry which is not contextually
 * appropriate (<i>eg</i> an {@code invokedynamic} instruction indexing a
 * constant pool slot which does not contain a
 * {@code CONSTANT_InvokeDynamic_info} structure).
 * </li>
 * </ul>
 *
 * @author Victor Schappert
 * @since 20171023
 */
public final class InvalidConstantPoolIndexException extends IllegalArgumentException {

    //
    // DATA
    //

    private final int constantPoolIndex;

    //
    // CONSTRUCTORS
    //

    public InvalidConstantPoolIndexException(final String message, final int constantPoolIndex, final Throwable cause) {
        super(message, cause);
        this.constantPoolIndex = constantPoolIndex;
    }

    public InvalidConstantPoolIndexException(final String message, final int constantPoolIndex) {
        this(message, constantPoolIndex, null);
    }

    //
    // PUBLIC METHODS
    //

    public int getConstantPoolIndex() {
        return constantPoolIndex;
    }

    //
    // STATIC METHODS
    //

    public static InvalidConstantPoolIndexException lessThanOne(final int constantPoolIndex) {
        assert constantPoolIndex < 1;
        return new InvalidConstantPoolIndexException(format("Index into constant_pool must be positive but is %d",
                constantPoolIndex), constantPoolIndex);
    }

    public static InvalidConstantPoolIndexException notLessThanCount(final int constantPoolIndex, final int constantPoolCount) {
        return new InvalidConstantPoolIndexException(format("Index into constant_pool must be less than constant_pool_count" +
                " but %d is not less than %d", constantPoolIndex, constantPoolCount), constantPoolIndex);
    }

    //
    // INTERFACE: Serializable
    //

    private static final long serialVersionUID = 1L;
}
