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

package io.tarro.base.attribute;

/**
 * <p>
 * Contains constant values identifying contexts in which an {@code attribute}
 * structure may appear in a class file.
 * </p>
 *
 * <p>
 * Since all attribute context constants are even multiples of two, a standard
 * Java {@code int} value may be manipulated as a set of attribute contexts. For
 * example, the value {@link #CLASS_FILE}<code>&nbsp;| </code>
 * {@link #FIELD_INFO}<code>&nbsp;| </code>{@link #METHOD_INFO} describes an
 * attribute that may appear in the attributes table of the class file itself or
 * of a field in the fields table or method in the method table.
 * </p>
 *
 * @author Victor Schappert
 * @since 20171016
 * @see Contextualized
 */
public final class AttributeContext {

    //
    // PUBLIC CONSTANTS
    //

    /**
     * Special value indicating the absence of an attribute context.
     */
    public static final int NONE = 0x0;
    /**
     * Special value indicating the presence of all attribute contexts.
     */
    public static final int ALL = 0xf;
    /**
     * Indicates the attributes table of the class file itself.
     */
    public static final int CLASS_FILE = 0x1;
    /**
     * Indicates the attributes table of a field in the fields table.
     */
    public static final int FIELD_INFO = 0x2;
    /**
     * Indicates the attributes table of a method in the methods table.
     */
    public static final int METHOD_INFO = 0x4;
    /**
     * Indicates the attributes table of a method code block.
     */
    public static final int CODE = 0x8;

    //
    // CONSTRUCTORS
    //

    private AttributeContext() {
    }
}
