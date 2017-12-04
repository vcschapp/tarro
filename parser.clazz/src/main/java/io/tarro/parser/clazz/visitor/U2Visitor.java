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

package io.tarro.parser.clazz.visitor;

import io.tarro.base.visitor.Visitor;

/**
 * <p>
 * Visits a {@code u2} value in a Java class file.
 * </p>
 *
 * <p>
 * A class implementing this interface may be used to visit any value in the
 * class file that is stored as a simple {@code u2} value, including:
 * </p>
 *
 * <ul>
 * <li>the {@code constant_pool_count};</li>
 * <li>the {@code access_flags};</li>
 * <li>the {@code this_class} index;</li>
 * <li>the {@code super_class} index;</li>
 * <li>the {@code interfaces_count};</li>
 * <li>any entry in the {@code interfaces} array;</li>
 * <li>the {@code fields_count};</li>
 * <li>the {@code methods_count}; and</li>
 * <li>the {@code attributes_count}.</li>
 * </ul>
 *
 * @author Victor Schappert
 * @since 20171009
 */
@FunctionalInterface
public interface U2Visitor extends Visitor {
    /**
     * Visits a {@code u2} value in the class file.
     *
     * @param u2 Valued in the range <code>[0..65,535]</code>, <i>ie</i> a 32-bit
     *           integer containing an unsigned value in the low-order 16 bits
     *           and zeroes in the high-order 16 bits.
     */
    void visit(final int u2);
}
