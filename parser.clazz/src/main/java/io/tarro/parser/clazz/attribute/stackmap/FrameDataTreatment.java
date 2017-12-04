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

package io.tarro.parser.clazz.attribute.stackmap;

import io.tarro.base.attribute.FrameType;

import static io.tarro.base.InternalError.unhandledEnumerator;

/**
 * <p>
 *
 * </p>
 *
 * @author Victor Schappert
 * @since 20171130
 */
public enum FrameDataTreatment {

    //
    // ENUMERATORS
    //

    KEEP,
    ADD,
    DELETE,
    REPLACE;

    //
    // PUBLIC STATICS
    //

    public static FrameDataTreatment localsTreatmentForFrameType(final FrameType frameType) {
        switch (frameType) {
        case SAME:
            return KEEP;
        case SAME_LOCALS_1_STACK_ITEM:
            return KEEP;
        case SAME_LOCALS_1_STACK_ITEM_EXTENDED:
            return KEEP;
        case CHOP:
            return DELETE;
        case SAME_FRAME_EXTENDED:
            return KEEP;
        case APPEND:
            return ADD;
        case FULL_FRAME:
            return REPLACE;
        default:
            throw unhandledEnumerator(frameType);
        }
    }

    public static FrameDataTreatment stackTreatmentForFrameType(final FrameType frameType) {
        switch (frameType) {
        case SAME:
            return KEEP;
        case SAME_LOCALS_1_STACK_ITEM:
            return ADD;
        case SAME_LOCALS_1_STACK_ITEM_EXTENDED:
            return ADD;
        case CHOP:
            return REPLACE;
        case SAME_FRAME_EXTENDED:
            return KEEP;
        case APPEND:
            return KEEP;
        case FULL_FRAME:
            return REPLACE;
        default:
            throw unhandledEnumerator(frameType);
        }
    }
}