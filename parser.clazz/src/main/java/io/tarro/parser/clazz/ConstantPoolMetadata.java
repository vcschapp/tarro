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

package io.tarro.parser.clazz;

import io.tarro.base.InvalidConstantPoolIndexException;
import io.tarro.base.attribute.AttributeType;
import io.tarro.base.constantpool.ConstantPoolTag;

import java.lang.reflect.Array;
import java.util.function.ToIntFunction;

import static io.tarro.base.InvalidConstantPoolIndexException.lessThanOne;
import static io.tarro.base.InvalidConstantPoolIndexException.notLessThanCount;
import static java.lang.String.format;
import static java.lang.System.arraycopy;
import static io.tarro.base.attribute.AttributeType.CODE;
import static io.tarro.base.attribute.AttributeType.UNKNOWN;
import static io.tarro.base.constantpool.ConstantPoolTag.DOUBLE;
import static io.tarro.base.constantpool.ConstantPoolTag.LONG;
import static io.tarro.base.constantpool.ConstantPoolTag.UTF8;

/**
 * <p>
 * Metadata about the size and content of the constant pool.
 * </p>
 *
 * <p>
 * This class is deliberately package-internal and not part of the public API.
 * </p>
 *
 * <p>
 * The internal data structure is an array of bytes, one per constant pool slot,
 * containing metadata about the constant pool entry in that slot. If we
 * stipulate that <code>b<sub>i</sub></code> is the byte value stored in the
 * <i>i<sup>th</sup></i> slot, then:
 * </p>
 *
 * <ul>
 * <li>
 * <code>b<sub>i</sub> = 0</code> &rArr; there is no constant in that slot;
 * </li>
 * <li>
 * <code>b<sub>i</sub> &lt; 0</code> &rArr; (-b<sub>i</sub> - 1) is the ordinal
 * value of the {@linkplain ConstantPoolTag constant pool tag} for the constant
 * stored in that slot; and
 * </li>
 * <li>
 * <code>0 &lt; b<sub>i</sub></code> &rArr; (b<sub>i</sub> - 1) is the ordinal
 * value of the {@linkplain AttributeType attribute type} corresponding to the
 * predefined attribute whose name is stored as a modified UTF-8 constant in
 * that slot.
 * </li>
 * </ul>
 *
 * @author Victor Schappert
 * @since 20171023
 */
final class ConstantPoolMetadata {

    //
    // DATA
    //

    private final byte[] metabits;

    //
    // CONSTRUCTORS
    //

    ConstantPoolMetadata(final int constantPoolCount) {
        // ---------------------------------------------------------------------
        // The Java Virtual Machine Specification specifies that
        // constant_pool_count is (1 + the number of actual constants). The
        // extra 1 is likely to "notionally" leave room for a "double-sized"
        // CONSTANT_Long_info or CONSTANT_Double_info structure as the last
        // constant in the pool. But since the "second slot" of those structures
        // is not validly indexable in any event and we only carry metadata
        // about valid slots in the pool, there seems no point in creating the
        // extra slot. Hence the minus one when allocating the array.
        // ---------------------------------------------------------------------
        // The default value of 0 in a slot means nothing is there.
        // ---------------------------------------------------------------------
        this.metabits = new byte[constantPoolCount - 1];
    }

    //
    // PACKAGE-INTERNAL METHODS
    //

    int count() {
        return metabits.length + 1;
    }

    void put(final int constantPoolIndex, final ConstantPoolTag constantPoolTag) {
        assert constantPoolTag != UTF8 : "Use put(int, String) for CONSTANT_Utf8";
        put(constantPoolIndex, encodeTagBits(constantPoolTag));
    }

    void put(final int constantPoolIndex, final String decodedUtf8Bytes) {
        put(constantPoolIndex, encodeAttributeNameBits(decodedUtf8Bytes));
    }

    AttributeType getAttributeType(final int constantPoolIndex, final int attributeContext) {
        final int metabits = get(constantPoolIndex);
        if (0 < metabits) {
            final AttributeType attributeType = decodeAttributeNameBits(metabits);
            if (attributeType.hasAttributeContext(attributeContext)) {
                return attributeType;
            } else {
                return UNKNOWN;
            }
        } else if (UTF8_TAG_BITS == metabits) {
            return UNKNOWN;
        } else {
            throw slotDoesNotContainUtf8(constantPoolIndex, metabits);
        }
    }

    //
    // INTERNALS
    //

    private void put(final int constantPoolIndex, final int metabits) {
        this.metabits[constantPoolIndex - 1] = (byte)metabits;
    }

    private int get(final int constantPoolIndex) {
        try {
            return metabits[constantPoolIndex - 1];
        } catch (ArrayIndexOutOfBoundsException e) {
            if (constantPoolIndex < 1) {
                throw lessThanOne(constantPoolIndex);
            } else {
                throw notLessThanCount(constantPoolIndex, metabits.length + 1);
            }
        }
    }

    private InvalidConstantPoolIndexException slotDoesNotContainUtf8(final int constantPoolIndex, final int metabits) {
        final ConstantPoolTag tag = decodeTagBits(metabits);
        if (null != tag) {
            throw new InvalidConstantPoolIndexException(format("expected CONSTANT_Utf8 in constant_pool[%d] " +
                    "but found %s", constantPoolIndex, tag.getStructureName()), constantPoolIndex);
        } else {
            final ConstantPoolTag prevTag = decodeTagBits(get(constantPoolIndex - 1));
            if (LONG == prevTag || DOUBLE == prevTag) {
                throw new InvalidConstantPoolIndexException(format("expected CONSTANT_Utf8 in constant_pool[%d] " +
                    "but found second slot belonging to %s", constantPoolIndex, prevTag), constantPoolIndex);
            }
            // Otherwise fall through to the bad state error below.
        }
        throw new IllegalStateException(format("constant_pool[%d] unexpectedly empty", constantPoolIndex));
    }

    private static final int UTF8_TAG_BITS = encodeTagBits(UTF8);
    private static final ConstantPoolTag[] CONSTANT_POOL_TAG_ORDINAL_MAP = offsetCopy(ConstantPoolTag.class, 1);

    private static int encodeTagBits(final ConstantPoolTag constantPoolTag) {
        return -(1 + constantPoolTag.ordinal());
    }

    private static ConstantPoolTag decodeTagBits(final int metabits) {
        return CONSTANT_POOL_TAG_ORDINAL_MAP[-metabits];
    }

    private static final AttributeType[] ATTRIBUTE_TYPE_ORDINAL_MAP = offsetCopy(AttributeType.class, 0);

    private static int encodeAttributeNameBits(final String candidateAttributeName) {
        return ATTRIBUTE_NAME_TO_INDEX_FUNCS[candidateAttributeName.length() &
                ATTRIBUTE_NAME_TO_INDEX_FUNCS_MASK].applyAsInt(candidateAttributeName);
    }

    private static AttributeType decodeAttributeNameBits(final int metabits) {
        return ATTRIBUTE_TYPE_ORDINAL_MAP[metabits];
    }

    private static final ToIntFunction<String> NOT_A_PREDEFINED_ATTRIBUTE = (s) -> 0;
    private static final int ATTRIBUTE_NAME_TO_INDEX_FUNCS_MASK = 0x3f;
    private static final ToIntFunction<String>[] ATTRIBUTE_NAME_TO_INDEX_FUNCS = new ToIntFunction[] {
        NOT_A_PREDEFINED_ATTRIBUTE,
        NOT_A_PREDEFINED_ATTRIBUTE,
        NOT_A_PREDEFINED_ATTRIBUTE,
        NOT_A_PREDEFINED_ATTRIBUTE,
        (ToIntFunction<String>) ConstantPoolMetadata::attributeNameCode
    };
    static {
        assert ATTRIBUTE_NAME_TO_INDEX_FUNCS.length == 1 + ATTRIBUTE_NAME_TO_INDEX_FUNCS_MASK;
    }

    private static int attributeNameCode(final String s) {
        if ('C' != s.charAt(0) || !"Code".equals(s)) {
            return 0;
        } else {
            return CODE.ordinal() + 1;
        }
    }

    private static <E extends Enum<E>> E[] offsetCopy(final Class<E> clazz, final int offset) {
        final E[] src = clazz.getEnumConstants();
        final E[] dest = (E[])Array.newInstance(clazz, src.length + offset);
        arraycopy(src, 0, dest, offset, src.length);
        return dest;
    }
}
