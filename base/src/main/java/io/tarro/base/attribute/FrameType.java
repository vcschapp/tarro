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

import static java.util.Arrays.stream;
import static java.util.stream.IntStream.rangeClosed;

/**
 * <p>
 *
 * </p>
 *
 * @author Victor Schappert
 * @since 20171130
 */
public enum FrameType {

    //
    // ENUMERATORS
    //

    SAME(0, 63),
    SAME_LOCALS_1_STACK_ITEM(64, 127),
    SAME_LOCALS_1_STACK_ITEM_EXTENDED(247),
    CHOP(248, 251),
    SAME_FRAME_EXTENDED(251),
    APPEND(252, 254),
    FULL_FRAME(255);

    //
    // DATA
    //

    private final int minValue;
    private final int maxValue;

    //
    // CONSTRUCTORS
    //

    FrameType(final int lowValue, final int highValue) {
        this.minValue = lowValue;
        this.maxValue = highValue;
    }

    FrameType(final int singleValue) {
        this(singleValue, singleValue);
    }

    //
    // PUBLIC METHODS
    //

    public int getMinValue() {
        return minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    //
    // PUBLIC STATICS
    //

    private static final FrameType[] BY_VALUE = new FrameType[256];

    static {
        stream(values()).forEach(frameType -> {
            rangeClosed(frameType.minValue, frameType.maxValue)
                .forEach(i -> BY_VALUE[i] = frameType);
        });
    }

    public static FrameType forUnsignedByte(final int value) {
        return BY_VALUE[value];
    }
}
