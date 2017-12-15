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
 * Enumerates the categories of {@code frame_type} byte which may appear at the
 * front of a structure within the {@code stack_map_frame} union belonging to
 * the {@code entries} table in a {@linkplain AttributeType#STACK_MAP_TABLE
 * StackMapTable} attribute.
 * </p>
 *
 * <p>
 * The {@code frame_type} byte plays two roles simultaneously. The first role is
 * to identify categories or types of stack map frame. For example, any
 * {@code frame_type} value between zero and 63, inclusive, indicates that the
 * frame is a {@code same_frame} structure. This categorization aspect of the
 * {@code frame_type} byte is captured by the {@link FrameType} member
 * enumerators themselves. A {@code frame_type} byte can be converted into an
 * enumerator via the static {@link #forUnsignedByte(int)} method.
 * </p>
 *
 * <p>
 * The second role of the {@code frame_type} byte is, for certain frame types,
 * to encode additional information about the frame. For example, in a
 * {@code same_locals_1_stack_item_frame} structure, the {@code offset_delta}
 * for the frame is obtained by subtracting the lowest possible
 * {@code same_locals_1_stack_item_frame} {@code frame_type} value, 64, from the
 * actual {@code frame_type} byte read from the class file. This additional
 * encoded information aspect of the {@code frame_type} byte is facilitated by
 * the {@link FrameType} member enumerators' {@link #getMinValue()}  and
 * {@link #getMaxValue()} methods.
 * </p>
 *
 * @author Victor Schappert
 * @since 20171130
 * @see VerificationTypeInfoTag
 */
public enum FrameType {

    //
    // ENUMERATORS
    //

    // Keep these enumerators sorted in ascending order by frame_byte range.
    /**
     * <p>
     * Identifies a stack map frame structure of type {@code same_frame} within
     * the {@code stack_map_frame} union.
     * </p>
     *
     * <p>
     * The {@code same_frame} structure has {@code frame_type} values in the
     * range 0..63, inclusive. This range may be obtained from
     * {@link #getMinValue()} and {@link #getMaxValue()}. The
     * {@code offset_delta} for a {@code same_frame} structure can be derived by
     * subtracting this enumerator's {@link #getMinValue()} from the
     * {@code frame_type} byte.
     * </p>
     *
     * @see #SAME_EXTENDED
     * @see #SAME_LOCALS_1_STACK_ITEM
     * @see #SAME_LOCALS_1_STACK_ITEM_EXTENDED
     */
    SAME(0, 63),
    /**
     * <p>
     * Identifies a stack map frame structure of type
     * {@code same_locals_1_stack_item_frame} within the {@code stack_map_frame}
     * union.
     * </p>
     *
     * <p>
     * The {@code same_locals_1_stack_item_frame} structure has
     * {@code frame_type} values in the range 64..127, inclusive. This range may
     * be obtained from {@link #getMinValue()} and {@link #getMaxValue()}. The
     * {@code offset_delta} for a {@code same_locals_1_stack_item_frame}
     * structure can be determined by subtracting this enumerator's
     * {@link #getMinValue()} from the {@code frame_type} byte.
     * </p>
     *
     * @see #SAME
     * @see #SAME_LOCALS_1_STACK_ITEM_EXTENDED
     * @see #SAME_EXTENDED
     */
    SAME_LOCALS_1_STACK_ITEM(64, 127),
    /**
     * <p>
     * Identifies a stack map frame structure of type
     * {@code same_locals_1_stack_item_frame_extended} within the
     * {@code stack_map_frame} union.
     * </p>
     *
     * <p>
     * The {@code same_locals_1_stack_item_frame_extended} structure is
     * associated with {@code frame_type} value 247, and this value is returned
     * by both {@link #getMinValue()} and {@link #getMaxValue()}. Being
     * restricted to a single value, the {@code frame_type} for this structure
     * evidently cannot encode any additional information.
     * </p>
     *
     * @see #SAME
     * @see #SAME_LOCALS_1_STACK_ITEM
     * @see #SAME_EXTENDED
     */
    SAME_LOCALS_1_STACK_ITEM_EXTENDED(247),
    /**
     * <p>
     * Identifies a stack map frame structure of type {@code chop_frame} within
     * the {@code stack_map_frame} union.
     * </p>
     *
     * <p>
     * The {@code chop_frame} structure has {@code frame_type} values in the
     * range 248..250, inclusive. This range may be obtained from
     * {@link #getMinValue()} and {@link #getMaxValue()}. Each
     * {@code chop_frame} removes a number <em>k</em> ∈ 0..2 of local variables
     * from the previous frame's locals and this number <em>k</em> can be
     * calculated by subtracting this enumerator's {@link #getMinValue()} from
     * the {@code frame_type} byte.
     * </p>
     *
     * @see #APPEND
     */
    CHOP(248, 250),
    /**
     * <p>
     * Identifies a stack map frame structure of type
     * {@code same_frame_extended} within the {@code stack_map_frame} union.
     * </p>
     *
     * <p>
     * The {@code same_frame_extended} structure is associated with
     * {@code frame_type} value 251, and this value is returned by both
     * {@link #getMinValue()} and {@link #getMaxValue()}. Being restricted to a
     * single value, the {@code frame_type} for this structure obviously does
     * encode any extra information.
     * </p>
     *
     * @see #SAME
     * @see #SAME_LOCALS_1_STACK_ITEM
     * @see #SAME_LOCALS_1_STACK_ITEM_EXTENDED
     */
    SAME_EXTENDED(251),
    /**
     * <p>
     * Identifies a stack map frame structure of type {@code append_frame}
     * within the {@code stack_map_frame} union.
     * </p>
     *
     * <p>
     * The {@code append_frame} structure has {@code frame_type} values in the
     * range 252..254, inclusive. This range may be obtained from
     * {@link #getMinValue()} and {@link #getMaxValue()}. Each
     * {@code append_frame} adds a number <em>k</em> ∈ 0..2 of local variables
     * to the previous frame's locals and this number <em>k</em> can be
     * obtained by subtracting this enumerator's {@link #getMinValue()} from the
     * {@code frame_type} byte.
     * </p>
     *
     * @see #CHOP
     */
    APPEND(252, 254),
    /**
     * <p>
     * Identifies a stack map frame structure of type {@code full_frame} within
     * the {@code stack_map_frame} union.
     * </p>
     *
     * <p>
     * The {@code full_frame} structure is associated with {@code frame_type}
     * value 255, and this value is returned by both {@link #getMinValue()} and
     * {@link #getMaxValue()}. Being restricted to a single value, the
     * {@code frame_type} for this structure contains no information except the
     * structure type.
     * </p>
     */
    FULL(255);

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

    /**
     * <p>
     * Obtains the minimum value of the {@code frame_type} byte for a structure
     * of the type indicated by this enumerator.
     * </p>
     *
     * <p>
     * If the {@code frame_type} byte encodes additional information, such as an
     * {@code offset_delta} or affected local variable count, that information
     * can be extracted by subtracting the value returned by this method from
     * the {@code frame_type} byte.
     * </p>
     *
     * @return Minimum {@code frame_type} value
     * @see #getMaxValue()
     */
    public int getMinValue() {
        return minValue;
    }

    /**
     * Obtains the maximum value of the {@code frame_type} byte for a structure
     * of the type indicated by this enumerator.
     *
     * @return Maximum {@code frame_type} value
     * @see #getMinValue()
     */
    public int getMaxValue() {
        return maxValue;
    }

    //
    // PUBLIC STATICS
    //

    /**
     * <p>
     * Obtains the frame type enumerator corresponding to a {@code frame_type}
     * byte.
     * </p>
     *
     * <p>
     * Note that even within the range of an unsigned byte there are subranges
     * that to not currently correspond to a stack map frame structure. Passing
     * one of these values to this method will result in an exception, as will
     * passing a value outside the range of an unsigned byte.
     * </p>
     *
     * @param value A value that can be represented by an unsigned byte,
     *              <em>ie</em> in the range 0..255
     * @return Frame type enumerator corresonding to {@code value}
     * @throws IllegalArgumentException If {@code value} is not in the range
     *                                  0..255 <em>or</em> the value is in the
     *                                  correct range but that value does not
     *                                  correspond to a known stack map frame
     *                                  structure
     */
    public static FrameType forUnsignedByte(final int value) {
        final FrameType frameType;
        try {
            frameType = BY_VALUE[value];
        } catch (final ArrayIndexOutOfBoundsException e) {
            throw noSuchFrameType("Unsigned byte must be in the range " +
                    "0..255, but value given was " + value);
        }
        if (null  != frameType) {
            return frameType;
        } else {
            throw noSuchFrameType("No frame type enumerator corresponds to " +
                    "value " + value);
        }
    }

    //
    // INTERNALS
    //

    private static final FrameType[] BY_VALUE = new FrameType[256];

    static {
        stream(values()).forEach(frameType -> {
            rangeClosed(frameType.minValue, frameType.maxValue)
                .forEach(i -> BY_VALUE[i] = frameType);
        });
    }

    private static IllegalArgumentException noSuchFrameType(final String message) {
        return new IllegalArgumentException(message);
    }
}
