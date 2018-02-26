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

package io.tarro.base.flag

import io.tarro.base.ClassFileVersion
import io.tarro.base.ClassFileVersion.JAVA9
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import java.util.Optional

/**
 * Unit tests for [ModuleReferenceFlag].
 *
 * @author Victor Schappert
 * @since 20180225
 */
class ModuleReferenceFlagTest :
        FlagEnumTest<ModuleReferenceFlag>(ModuleReferenceFlag::class) {
    @ParameterizedTest
    @EnumSource(ModuleReferenceFlag::class)
    fun version(flag: ModuleReferenceFlag) {
        Assertions.assertEquals(JAVA9, flag.firstVersionSupporting)
        Assertions.assertEquals(Optional.empty<ClassFileVersion>(), flag.lastVersionSupporting)
    }
}
