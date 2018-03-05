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

package io.tarro.base.flag;

import io.tarro.base.ClassFileVersion;

import static io.tarro.base.ClassFileVersion.JAVA9;

/**
 * Enumerates flags available on the {@code requires_flags} field of a
 * an entry in the {@code requires} array of a {@code Module} attribute.
 *
 * @author Victor Schappert
 * @since 20171126
 * @see io.tarro.base.attribute.AttributeType#MODULE
 * @see PackageReferenceFlag
 * @see ModuleFlag
 */
public enum ModuleReferenceFlag implements Flag {

    //
    // ENUMERATORS
    //

    /**
     * {@code 0x0020} ({@code ACC_TRANSITIVE}): Indicates that any module which
     * depends on the current module implicitly declares a dependency on the
     * module indicated by this entry in the {@code requires} table.
     */
    TRANSITIVE(0x0020),
    /**
     * {@code 0x0040} ({@code ACC_STATIC_PHASE}): Indicates that dependence on
     * the module referred to by this entry in the {@code requires} table is
     * mandatory in the static phase, <em>ie</em>, at compile time, but is
     * optional in the dynamic phase, <em>ie</em>, at run time.
     */
    STATIC_PHASE(0x0040),
    /**
     * {@code 0x1000} ({@code ACC_SYNTHETIC}): Indicates that the dependency
     * indicated by this entry in the {@code requires} table is not explicitly
     * or implicitly declared in the {@code module} declaration source code.
     *
     * @see #MANDATED
     */
    SYNTHETIC(0x1000),
    /**
     * {@code 0x8000} ({@code ACC_MANDATED}): Indicates that the dependency
     * indicated by this entry in the {@code requires} table was implicitly
     * declared in the source of the module declaration.
     *
     * @see #SYNTHETIC
     */
    MANDATED(0x8000);

    //
    // DATA
    //

    private final int value;

    //
    // CONSTRUCTORS
    //

    ModuleReferenceFlag(final int value) {
        this.value = value;
    }

    //
    // INTERFACE: Flag
    //

    @Override
    public String getFlagName() {
        return "ACC_" + name();
    }

    //
    // INTERFACE: Valued
    //

    /**
     * {@inheritDoc}
     *
     * <p>
     * This method returns the individual flag's integer value, which is
     * guaranteed to be a power of two distinct from any other flag in the
     * enumeration, since the {@code access_flags} member of class file is a
     * bitmask.
     * </p>
     *
     * @return Flag bit
     */
    @Override
    public int getValue() {
        return value;
    }

    //
    // INTERFACE: Versioned
    //

    @Override
    public ClassFileVersion getFirstVersionSupporting() {
        return JAVA9;
    }
}
