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

package io.tarro.parser.clazz.attribute;

import io.tarro.base.attribute.AttributeType;
import io.tarro.base.flag.ModuleFlag;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static io.tarro.base.attribute.AttributeType.MODULE;
import static java.util.Arrays.stream;
import static java.util.Collections.unmodifiableSet;
import static java.util.Objects.requireNonNull;

/**
 * {@linkplain AttributeType#MODULE Module} predefined attribute.
 *
 * @author Victor Schappert
 * @since 20171125
 */
public final class ModuleAttribute extends Attribute {

    //
    // DATA
    //

    private final int moduleNameIndex;
    private final Set<ModuleFlag> moduleFlags;
    private final int moduleVersionIndex;
    private final List<ModuleReference> requires;
    private final List<PackageReference> exports;
    private final List<PackageReference> opens;
    private final int[] uses;
    private final List<ModuleProvide> provides;

    //
    // CONSTRUCTORS
    //

    public ModuleAttribute(final int moduleNameIndex, final EnumSet<ModuleFlag> moduleFlags,
        final int moduleVersionIndex, final ModuleReference[] requires,
        final PackageReference[] exports, final PackageReference[] opens,
        final int[] uses, final ModuleProvide[] provides) {
        super(MODULE);
        this.moduleNameIndex = moduleNameIndex;
        this.moduleFlags = unmodifiableSet(moduleFlags);
        this.moduleVersionIndex = moduleVersionIndex;
        this.requires = List.of(requires);
        this.exports = List.of(exports);
        this.opens = List.of(opens);
        this.uses = requireNonNull(uses, "uses cannot be null");
        this.provides = List.of(provides);
    }

    //
    // PUBLIC METHODS
    //

    public int getModuleNameIndex() {
        return moduleNameIndex;
    }

    public Set<ModuleFlag> getModuleFlags() {
        return moduleFlags;
    }

    public int getModuleVersionIndex() {
        return moduleVersionIndex;
    }

    public List<ModuleReference> getRequires() {
        return requires;
    }

    public List<PackageReference> getExports() {
        return exports;
    }

    public List<PackageReference> getOpens() {
        return opens;
    }

    public IntStream getUses() {
        return stream(uses);
    }

    public List<ModuleProvide> getProvides() {
        return provides;
    }
}
