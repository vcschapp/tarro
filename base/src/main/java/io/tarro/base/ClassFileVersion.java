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

package io.tarro.base;

/**
 * <p>
 * Enumerates those Java <em>class file</em>versions which are relevant to
 * parsing the Java class file.
 * </p>
 *
 * <p>
 * This enumeration gives symbolic names to class file {@code <major_version,
 * minor_version>} pairs. It is chiefly used to tag an entity with the Java
 * class file version, or range of versions, for which a Java class file having
 * that version may be expected to contain that entity, thus enabling
 * programmatic validation of class files of a particular version.
 * </p>
 *
 * @author Victor Schappert
 * @since 20171009
 */
public enum ClassFileVersion {

    //
    // ENUMERATORS
    //

    /**
     * <p>
     * Represents a class file from the first Java release, Java 1.0.
     * </p>
     *
     * <p>
     * There doesn't seem to be any way of differentiating between classes from
     * the first stable JDK release, JDK 1.0.2, and those from Java 1.1's JRE
     * 1.1. Both releases use class file version 45.3, even though Java 1.1 adds
     * inner classes. So this enumerator has the same {@link #majorVersion} and
     * {@link #minorVersion} as the {@link #JAVA1_1} enumerator, 45.3. We keep
     * both enumerators even though we can't distinguish between them at the
     * class file level because it is still useful to maintain both versions
     * for documentary purposes elsewhere. When parsing a class file, the parser
     * should act as if this enumerator does not exist and assume that version
     * 45.3 means {@link #JAVA1_1}.
     * </p>
     *
     * <p>
     * No other enumerators except this one and {@link #JAVA1_1} duplicate
     * version numbers. In all other cases, the enumerator with the higher
     * ordinal value has the greater combined version number.
     * </p>
     */
    JAVA1(45, 3),
    /**
     * <p>
     * Represents a class file from Java 1.1.
     * </p>
     *
     * <p>
     * This enumerator shares the same version number, 45.3, as the
     * {@link #JAVA1} enumerator. Thus there is no ability to distinguish
     * between Java 1.0.2 and Java 1.1 at the class file level and the class
     * file parser should simply assume that version 45.3 indicates the higher
     * version, Java 1 (<em>ie</em> this enumerator).
     * </p>
     *
     * <p>
     * No other enumerators except this one and {@link #JAVA1} duplicate version
     * numbers. In all other cases, the enumerator with the higher ordinal value
     * has the greater combined version number.
     * </p>
     */
    JAVA1_1(45, 3),
    /**
     * Represents a class file from Java 1.2 (version 46.0).
     */
    JAVA1_2(46, 0),
    /**
     * Represents a class file from Java 1.3 (version 47.0).
     */
    JAVA1_3(47, 0),
    /**
     * Represents a class file from Java 1.4 (version 48.0).
     */
    JAVA1_4(48, 0),
    /**
     * Represents a class file from Java 5 (version 49.0).
     */
    JAVA5(49, 0),
    /**
     * Represents a class file from Java 6 (version 50.0).
     */
    JAVA6(50, 0),
    /**
     * Represents a class file from Java 7 (version 51.0).
     */
    JAVA7(51, 0),
    /**
     * Represents a class file from Java 8 (version 52.0).
     */
    JAVA8(52, 0),
    /**
     * Represents a class file from Java 9 (version 53.0).
     */
    JAVA9(53, 0),
    /**
     * Represents a class file from Java 10 (version 54.0).
     */
    JAVA10(54, 0);

    //
    // DATA
    //

    private final int majorVersion;
    private final int minorVersion;

    //
    // CONSTRUCTORS
    //

    ClassFileVersion(final int majorVersion, final int minorVersion) {
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
    }

    //
    // PUBLIC METHODS
    //

    /**
     * Obtains the major version for this class file version.
     *
     * @return Major version
     */
    public final int getMajorVersion() {
        return majorVersion;
    }

    /**
     * Obtains the minor version for this class file version.
     *
     * @return minor version
     */
    public final int getMinorVersion() {
        return minorVersion;
    }
}
