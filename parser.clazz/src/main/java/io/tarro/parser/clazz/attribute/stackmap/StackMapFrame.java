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

package io.tarro.parser.clazz.attribute.stackmap;

import io.tarro.base.attribute.FrameType;

import java.util.List;

import static io.tarro.base.attribute.FrameType.APPEND;
import static io.tarro.base.attribute.FrameType.CHOP;
import static io.tarro.base.attribute.FrameType.FULL;
import static io.tarro.base.attribute.FrameType.SAME;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

/**
 * <p>
 *
 * </p>
 *
 * @author Victor Schappert
 * @since 20171130
 */
public class StackMapFrame {

    //
    // DATA
    //

    private final FrameType frameType;
    private final int offsetDelta;
    private final List<VerificationTypeInfo> locals;
    private final List<VerificationTypeInfo> stack;
    private final int numLocalsToDelete;

    //
    // CONSTRUCTORS
    //

    private StackMapFrame(final FrameType frameType, final int offsetDelta, final List<VerificationTypeInfo> locals,
            final List<VerificationTypeInfo> stack, final int numLocalsToDelete) {
        this.frameType = frameType;
        this.offsetDelta = offsetDelta;
        this.locals = locals;
        this.stack = stack;
        this.numLocalsToDelete = numLocalsToDelete;
    }

    public StackMapFrame(final int offsetDelta) {
        this(SAME, offsetDelta, emptyList(), emptyList(), 0);
    }

    public StackMapFrame(final FrameType frameType, final int offsetDelta, final VerificationTypeInfo stack) {
        this(frameType, offsetDelta, emptyList(), singletonList(stack), 0);
    }

    public StackMapFrame(final int offsetDelta, final int numLocalsToDelete) {
        this(CHOP, offsetDelta, emptyList(), emptyList(), numLocalsToDelete);
    }

    public StackMapFrame(final int offsetDelta, final VerificationTypeInfo[] locals) {
        this(APPEND, offsetDelta, List.of(locals), emptyList(), 0);
    }

    public StackMapFrame(final int offsetDelta, final VerificationTypeInfo[] locals,
        final VerificationTypeInfo[] stack) {
        this(FULL, offsetDelta, List.of(locals), List.of(stack), 0);
    }

    //
    // PUBLIC METHODS
    //

    public FrameType getFrameType() {
        return frameType;
    }

    public int getOffsetDelta() {
        return offsetDelta;
    }

    public List<VerificationTypeInfo> getLocals() {
        return locals;
    }

    public List<VerificationTypeInfo> getStack() {
        return stack;
    }

    public int getNumLocalsToDelete() {
        return numLocalsToDelete;
    }
}
