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

package io.tarro.parser.clazz.constantpool;

import io.tarro.base.constantpool.Represents;

import static io.tarro.base.constantpool.ConstantPoolTag.INVOKE_DYNAMIC;

/**
 * <p> Constant pool entry representing bootstrap method information required
 * for an {@code invokedynamic} instruction. </p>
 *
 * <p> The constant data consists of two references: (1) an index into the
 * {@code bootstrap_methods} array of the bootstrap method table of the class
 * file (stored in the "BootstrapMethods" attribute); and (2) an index into the
 * constant pool indicating the name and type of the bootstrap method. </p>
 *
 * @author Victor Schappert
 * @since 20171014
 */
@Represents(INVOKE_DYNAMIC)
public final class InvokeDynamicEntry extends ConstantPoolEntry {

    //
    // DATA
    //

    private final int bootstrapMethodAttributeIndex;
    private final int nameAndTypeIndex;

    //
    // CONSTRUCTORS
    //

    /**
     * Creates the invoke dynamic constant pool entry.
     *
     * @param bootstrapMethodAttributeIndex
     *         Index into the {@code bootstrap_methods} array of the bootstrap
     *         methods table
     * @param nameAndTypeIndex
     *         Constant pool index of the {@linkplain NameAndTypeEntry
     *         CONSTANT_NameAndType_info} structure containing the bootstrap
     *         method name and type info
     */
    public InvokeDynamicEntry(final int bootstrapMethodAttributeIndex,
            final int nameAndTypeIndex) {
        super(INVOKE_DYNAMIC);
        this.bootstrapMethodAttributeIndex = bootstrapMethodAttributeIndex;
        this.nameAndTypeIndex = nameAndTypeIndex;
    }

    //
    // PUBLIC METHODS
    //

    /**
     * Obtains the bootstrap method index.
     *
     * @return Bootstrap method index
     */
    public int getBootstrapMethodAttributeIndex() {
        return bootstrapMethodAttributeIndex;
    }

    /**
     * Obtains the constant pool index of the {@linkplain NameAndTypeEntry
     * CONSTANT_NameAndType_info} structure containing the bootstrap method name
     * and type info
     *
     * @return Constant pool index
     */
    public int getNameAndTypeIndex() {
        return nameAndTypeIndex;
    }
}
