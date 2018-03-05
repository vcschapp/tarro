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

package io.tarro.base.bytecode;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.Arrays.stream;

/**
 * Lookup table for converting a opcode bytes for the Java Virtual Machine
 * instruction set into references to the {@link Opcode} interface or the
 * categorized enumerations which implement it.
 *
 * @author Victor Schappert
 * @since 20171218
 * @param <O> Type of {@link Opcode} this table supports
 */
final class OpcodeTable<O extends Opcode> {

    //
    // DATA
    //

    private final O[] byValue;
    private final Class<? extends O> clazz;

    //
    // CONSTRUCTORS
    //

    @SuppressWarnings("unchecked")
    OpcodeTable(final Class<? extends O> clazz, final Stream<? extends O> opcodes) {
        byValue = (O[]) Array.newInstance(clazz, 0b1_0000_0000);
        opcodes.forEach(o -> byValue[o.getValue()] = o);
        this.clazz = clazz;
    }

    //
    // INSTANCE MEMBERS
    //

    O forUnsignedByte(final int value) {
        final O opcode;
        try {
            opcode = byValue[value];
        } catch (final ArrayIndexOutOfBoundsException e) {
            throw notAnUnsignedByte(value);
        }
        if (null != opcode) {
            return opcode;
        } else {
            throw noOpcodeForValue(clazz, value);
        }
    }

    //
    // STATICS
    //

    static <O extends Enum<O> & Opcode> OpcodeTable<O> forOpcodeEnum(
            final Class<? extends O> clazz) {
        return new OpcodeTable<>(clazz, stream(clazz.getEnumConstants()));
    }

    static OpcodeTable<Opcode> allOpcodes() {
        // This static initializer is thread-safe because the assignment to
        // ALL_OPCODES is idempotent and we don't care if we do the work a few
        // extra times in a concurrent scenario before the cached value is
        // readable by all threads.
        if (null == ALL_OPCODES) {
            ALL_OPCODES = new OpcodeTable<>(
                Opcode.class,
                Stream.of(
                    NoOperandOpcode.values(),
                    OneOperandOpcode.values(),
                    TwoOperandOpcode.values(),
                    VariableOperandOpcode.values()
                ).flatMap(Arrays::stream));
        }
        return ALL_OPCODES;
    }

    static <O extends Opcode> IllegalArgumentException noOpcodeForValue(
            final Class<O> clazz, final int value) {
        return noSuchOpcode(format("No %s corresponds to value %d",
                clazz.getSimpleName(), value));
    }

    static IllegalArgumentException notAnUnsignedByte(final int value) {
        return noSuchOpcode("Unsigned byte must be in the range " +
                "0..255, but value given was " + value);
    }

    //
    // INTERNALS
    //

    private static OpcodeTable<Opcode> ALL_OPCODES;

    private static IllegalArgumentException noSuchOpcode(final String message) {
        return new IllegalArgumentException(message);
    }
}
