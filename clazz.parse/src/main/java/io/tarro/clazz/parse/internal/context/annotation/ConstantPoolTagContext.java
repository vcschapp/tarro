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

package io.tarro.clazz.parse.internal.context.annotation;

import io.tarro.base.constantpool.ConstantPoolTag;
import io.tarro.clazz.parse.ClassParser;

/**
 * Indicates that the current method is parsing a constant pool entry having the
 * given tag.
 *
 * This is an internal annotation used by the {@link ClassParser} to help it
 * figure out its the "human readable" location within the class file for error
 * reporting purposes.
 *
 * @author Victor Schappert
 * @since 20171126
 * @see AttributeTypeContext
 * @see ConstantPoolTagContext
 * @see ContextSymbol
 * @see FrameTypeContext
 * @see StructureContext
 */
public @interface ConstantPoolTagContext {
    ConstantPoolTag value();
}
