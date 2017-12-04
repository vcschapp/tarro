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

import io.tarro.base.flag.ClassAccessFlag;
import io.tarro.base.visitor.Visitor;

import java.util.EnumSet;

/**
 * Visits the top-level {@code access_flags} of a class file.
 *
 * @author Victor Schappert
 * @since 20171014
 */
@FunctionalInterface
public interface ClassAccessFlagsVisitor extends Visitor {
    /**
     * Visits the class file's {@code access_flags}.
     *
     * @param accessFlags Set containing the access flags
     */
    void visit(final EnumSet<ClassAccessFlag> accessFlags);
}