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
import static io.tarro.base.attribute.AttributeType.ANNOTATION_DEFAULT;
import static io.tarro.base.attribute.AttributeType.BOOTSTRAP_METHODS;
import static io.tarro.base.attribute.AttributeType.CODE;
import static io.tarro.base.attribute.AttributeType.CONSTANT_VALUE;
import static io.tarro.base.attribute.AttributeType.DEPRECATED;
import static io.tarro.base.attribute.AttributeType.ENCLOSING_METHOD;
import static io.tarro.base.attribute.AttributeType.EXCEPTIONS;
import static io.tarro.base.attribute.AttributeType.INNER_CLASSES;
import static io.tarro.base.attribute.AttributeType.LINE_NUMBER_TABLE;
import static io.tarro.base.attribute.AttributeType.LOCAL_VARIABLE_TABLE;
import static io.tarro.base.attribute.AttributeType.LOCAL_VARIABLE_TYPE_TABLE;
import static io.tarro.base.attribute.AttributeType.METHOD_PARAMETERS;
import static io.tarro.base.attribute.AttributeType.MODULE;
import static io.tarro.base.attribute.AttributeType.MODULE_MAIN_CLASS;
import static io.tarro.base.attribute.AttributeType.MODULE_PACKAGES;
import static io.tarro.base.attribute.AttributeType.RUNTIME_INVISIBLE_ANNOTATIONS;
import static io.tarro.base.attribute.AttributeType.RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS;
import static io.tarro.base.attribute.AttributeType.RUNTIME_INVISIBLE_TYPE_ANNOTATIONS;
import static io.tarro.base.attribute.AttributeType.RUNTIME_VISIBLE_ANNOTATIONS;
import static io.tarro.base.attribute.AttributeType.RUNTIME_VISIBLE_TYPE_ANNOTATIONS;
import static io.tarro.base.attribute.AttributeType.SIGNATURE;
import static io.tarro.base.attribute.AttributeType.SOURCE_DEBUG_EXTENSION;
import static io.tarro.base.attribute.AttributeType.SOURCE_FILE;
import static io.tarro.base.attribute.AttributeType.STACK_MAP_TABLE;
import static io.tarro.base.attribute.AttributeType.SYNTHETIC;
import static io.tarro.base.attribute.AttributeType.UNKNOWN;
import static io.tarro.base.constantpool.ConstantPoolTag.DOUBLE;
import static io.tarro.base.constantpool.ConstantPoolTag.LONG;
import static io.tarro.base.constantpool.ConstantPoolTag.UTF8;
import static java.lang.String.format;
import static java.lang.System.arraycopy;

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
            if (attributeContext == (attributeType.getAttributeContext() & attributeContext)) {
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
            throw new InvalidConstantPoolIndexException(format("expected CONSTANT_Utf8_info in constant_pool[%d] " +
                    "but found %s", constantPoolIndex, tag.getStructureName()), constantPoolIndex);
        } else {
            final ConstantPoolTag prevTag = decodeTagBits(get(constantPoolIndex - 1));
            if (LONG == prevTag || DOUBLE == prevTag) {
                throw new InvalidConstantPoolIndexException(format("expected CONSTANT_Utf8_info in constant_pool[%d] " +
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

    private static final AttributeType[] ATTRIBUTE_TYPE_ORDINAL_MAP = offsetCopy(AttributeType.class, 1);

    private static int encodeAttributeNameBits(final String candidateAttributeName) {
        return ATTRIBUTE_NAME_TO_INDEX_FUNCS[candidateAttributeName.length() &
                ATTRIBUTE_NAME_TO_INDEX_FUNCS_MASK].applyAsInt(candidateAttributeName);
    }

    private static AttributeType decodeAttributeNameBits(final int metabits) {
        return ATTRIBUTE_TYPE_ORDINAL_MAP[metabits];
    }

    private static final ToIntFunction<String> NOT_A_PREDEFINED_ATTRIBUTE = (s) -> 0;
    private static final int ATTRIBUTE_NAME_TO_INDEX_FUNCS_MASK = 0x3f;
    @SuppressWarnings("unchecked")
    private static final ToIntFunction<String>[] ATTRIBUTE_NAME_TO_INDEX_FUNCS = new ToIntFunction[] {
        NOT_A_PREDEFINED_ATTRIBUTE /* 0 */,
        NOT_A_PREDEFINED_ATTRIBUTE /* 1 */,
        NOT_A_PREDEFINED_ATTRIBUTE /* 2 */,
        NOT_A_PREDEFINED_ATTRIBUTE /* 3 */,
        (ToIntFunction<String>) ConstantPoolMetadata::attributeName04Code /* 4 = Code */,
        NOT_A_PREDEFINED_ATTRIBUTE /* 5 */,
        (ToIntFunction<String>) ConstantPoolMetadata::attributeName06Module /* 6 = Module */,
        NOT_A_PREDEFINED_ATTRIBUTE /* 7 */,
        NOT_A_PREDEFINED_ATTRIBUTE /* 8 */,
        (ToIntFunction<String>) ConstantPoolMetadata::attributeName09SigSyn /* 9 = Signature, Synthetic */,
        (ToIntFunction<String>) ConstantPoolMetadata::attributeName10DepExcSou /* 10 = Deprecated, Exceptions, SourceFile */,
        NOT_A_PREDEFINED_ATTRIBUTE /* 11 */,
        attributeName(INNER_CLASSES), /* 12 */
        attributeName(CONSTANT_VALUE, STACK_MAP_TABLE) /* 13 = ConstantValue, StackMapTable */,
        attributeName(MODULE_PACKAGES) /* 14 = ModulePackages */,
        attributeName(ENCLOSING_METHOD, LINE_NUMBER_TABLE, MODULE_MAIN_CLASS) /* 15 = EnclosingMethod, LineNumberTable, ModuleMainClass */,
        attributeName(BOOTSTRAP_METHODS, METHOD_PARAMETERS) /* 16 = BootstrapMethods, MethodParameters */,
        attributeName(ANNOTATION_DEFAULT), /* 17 */
        attributeName(LOCAL_VARIABLE_TABLE), /* 18 */
        NOT_A_PREDEFINED_ATTRIBUTE /* 19 */,
        attributeName(SOURCE_DEBUG_EXTENSION), /* 20 */
        NOT_A_PREDEFINED_ATTRIBUTE /* 21 */,
        attributeName(LOCAL_VARIABLE_TYPE_TABLE), /* 22 */
        NOT_A_PREDEFINED_ATTRIBUTE /* 23 */,
        NOT_A_PREDEFINED_ATTRIBUTE /* 24 */,
        attributeName(RUNTIME_VISIBLE_ANNOTATIONS), /* 25 */
        NOT_A_PREDEFINED_ATTRIBUTE /* 26 */,
        attributeName(RUNTIME_INVISIBLE_ANNOTATIONS), /* 27 */
        NOT_A_PREDEFINED_ATTRIBUTE /* 28 */,
        attributeName(RUNTIME_VISIBLE_TYPE_ANNOTATIONS), /* 29 */
        NOT_A_PREDEFINED_ATTRIBUTE /* 30 */,
        attributeName(RUNTIME_INVISIBLE_TYPE_ANNOTATIONS), /* 31 */
        NOT_A_PREDEFINED_ATTRIBUTE /* 32 */,
        NOT_A_PREDEFINED_ATTRIBUTE /* 33 */,
        attributeName(RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS), /* 34 */
        NOT_A_PREDEFINED_ATTRIBUTE /* 35 */,
        attributeName(RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS), /* 36 */
        NOT_A_PREDEFINED_ATTRIBUTE /* 37 */,
        NOT_A_PREDEFINED_ATTRIBUTE /* 38 */,
        NOT_A_PREDEFINED_ATTRIBUTE /* 39 */,
        NOT_A_PREDEFINED_ATTRIBUTE /* 40 */,
        NOT_A_PREDEFINED_ATTRIBUTE /* 41 */,
        NOT_A_PREDEFINED_ATTRIBUTE /* 42 */,
        NOT_A_PREDEFINED_ATTRIBUTE /* 43 */,
        NOT_A_PREDEFINED_ATTRIBUTE /* 44 */,
        NOT_A_PREDEFINED_ATTRIBUTE /* 45 */,
        NOT_A_PREDEFINED_ATTRIBUTE /* 46 */,
        NOT_A_PREDEFINED_ATTRIBUTE /* 47 */,
        NOT_A_PREDEFINED_ATTRIBUTE /* 48 */,
        NOT_A_PREDEFINED_ATTRIBUTE /* 49 */,
        NOT_A_PREDEFINED_ATTRIBUTE /* 50 */,
        NOT_A_PREDEFINED_ATTRIBUTE /* 51 */,
        NOT_A_PREDEFINED_ATTRIBUTE /* 52 */,
        NOT_A_PREDEFINED_ATTRIBUTE /* 53 */,
        NOT_A_PREDEFINED_ATTRIBUTE /* 54 */,
        NOT_A_PREDEFINED_ATTRIBUTE /* 55 */,
        NOT_A_PREDEFINED_ATTRIBUTE /* 56 */,
        NOT_A_PREDEFINED_ATTRIBUTE /* 57 */,
        NOT_A_PREDEFINED_ATTRIBUTE /* 58 */,
        NOT_A_PREDEFINED_ATTRIBUTE /* 59 */,
        NOT_A_PREDEFINED_ATTRIBUTE /* 60 */,
        NOT_A_PREDEFINED_ATTRIBUTE /* 61 */,
        NOT_A_PREDEFINED_ATTRIBUTE /* 62 */,
        NOT_A_PREDEFINED_ATTRIBUTE /* 63 */
    };
    static {
        assert ATTRIBUTE_NAME_TO_INDEX_FUNCS.length == 1 + ATTRIBUTE_NAME_TO_INDEX_FUNCS_MASK;
    }

    private static int attributeName04Code(final String s) {
        if ('C' != s.charAt(0) || 4 != s.length() || 'o' != s.charAt(1) ||
                'd' != s.charAt(2) || 'e' != s.charAt(3)) {
            return 0;
        } else {
            return CODE.ordinal() + 1;
        }
    }

    private static int attributeName06Module(final String s) {
        if ('M' != s.charAt(1) || 6 != s.length() || 'o' != s.charAt(1) ||
            'd' != s.charAt(2) || 'u' != s.charAt(3) || 'l' != s.charAt(4) ||
            'e' != s.charAt(5)) {
            return 0;
        } else {
            return MODULE.ordinal() + 1;
        }
    }

    private static int attributeName09SigSyn(final String s) {
        if ('S' != s.charAt(0) || 9 != s.length()) {
            return 0;
        } else if ("Signature".equals(s)) {
            return SIGNATURE.ordinal() + 1;
        } else if ("Synthetic".equals(s)) {
            return SYNTHETIC.ordinal() + 1;
        } else {
            return 0;
        }
    }

    private static int attributeName10DepExcSou(final String s) {
        if (10 == s.length()) {
            switch (s.charAt(0)) {
                case 'D': return !"Deprecated".equals(s) ? 0 : DEPRECATED.ordinal() + 1;
                case 'E': return !"Exceptions".equals(s) ? 0 : EXCEPTIONS.ordinal() + 1;
                case 'S': return !"SourceFile".equals(s) ? 0 : SOURCE_FILE.ordinal() + 1;
            }
        }
        return 0;
    }

    private static ToIntFunction<String> attributeName(final AttributeType attributeType) {
        final String attributeName = attributeType.getAttributeName();
        final int n = attributeName.length();
        final char first = attributeName.charAt(0);
        final int index = attributeType.ordinal() + 1;
        return s -> {
            if (n == s.length() && first == s.charAt(0) && attributeName.equals(s)) {
                return index;
            } else {
                return 0;
            }
        };
    }

    private static ToIntFunction<String> attributeName(final AttributeType attributeType1,
                                                       final AttributeType attributeType2) {
        final String name1 = attributeType1.getAttributeName();
        final String name2 = attributeType2.getAttributeName();
        final char first1 = name1.charAt(0);
        final char first2 = name2.charAt(0);
        assert name1.length() == name2.length() && first1 != first2;
        final int n = name1.length();
        return s -> {
            if (n == s.length()) {
                final char first = s.charAt(0);
                if (first1 == first && name1.equals(s)) {
                    return attributeType1.ordinal() + 1;
                } else if (first2 == first && name2.equals(s)) {
                    return attributeType2.ordinal() + 2;
                }
            }
            return 0;
        };
    }

    private static ToIntFunction<String> attributeName(final AttributeType attributeType1,
                                                       final AttributeType attributeType2,
                                                       final AttributeType attributeType3) {
        final String name1 = attributeType1.getAttributeName();
        final String name2 = attributeType2.getAttributeName();
        final String name3 = attributeType3.getAttributeName();
        final char first1 = name1.charAt(0);
        final char first2 = name2.charAt(0);
        final char first3 = name3.charAt(0);
        assert name1.length() == name2.length() && first1 != first2 && first2 != first3;
        final int n = name1.length();
        return s -> {
            if (n == s.length()) {
                final char first = s.charAt(0);
                if (first1 == first && name1.equals(s)) {
                    return attributeType1.ordinal() + 1;
                } else if (first2 == first && name2.equals(s)) {
                    return attributeType2.ordinal() + 2;
                } else if (first3 == first && name3.equals(s)) {
                    return attributeType3.ordinal() + 3;
                }
            }
            return 0;
        };
    }

    private static <E extends Enum<E>> E[] offsetCopy(final Class<E> clazz, final int offset) {
        final E[] src = clazz.getEnumConstants();
        @SuppressWarnings("unchecked")
        final E[] dest = (E[])Array.newInstance(clazz, src.length + offset);
        arraycopy(src, 0, dest, offset, src.length);
        return dest;
    }
}
