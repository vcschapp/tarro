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

import java.util.List;

import static java.util.Arrays.asList;

/**
 * <p>
 * Enumerates Java versions. Only those Java versions relevant to parsing the
 * class file format are enumerated, since many Java releases made no changes
 * to the format.
 * </p>
 *
 * <p>
 * Java version naming is confusing as hell. See these references:
 * </p>
 *
 * <ul>
 * <li><a href="https://stackoverflow.com/q/10858193/1911388">Java JDK, SDK, SE?
 * </a>on Stack Overflow.</li>
 * </ul>
 *
 * @author Victor Schappert
 * @since 20171009
 */
public enum ClassFileVersion {

    //
    // ENUMERATORS
    //

    JAVA1("1.0.2", 45, 3, "Java 1"),
    JAVA1_1("1.1", 45, 3, "Java 1.1"),
    JAVA1_2("1.2", 46, 0, "Java 1.2"),
    JAVA1_3("1.3", 47, 0, "Java 1.3"),
    JAVA1_4("1.4", 48, 0, "Java 1.4"),
    JAVA5("1.5", 49, 0, "Java 5"),
    JAVA6("1.6", 50, 0, "Java 6"),
    JAVA7("1.7", 51, 0, "Java 7"),
    JAVA8("1.8", 52, 0, "Java 8"),
    JAVA9("1.9", 53, 0, "Java 9");


    //
    // DATA
    //

    private final String versionString;
    private final int majorVersion;
    private final int minorVersion;
    private final String[] aliases;

    //
    // CONSTRUCTORS
    //

    ClassFileVersion(final String versionString, final int majorVersion, final int minorVersion,
            final String... aliases) {
        this.versionString = versionString;
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
        this.aliases = aliases;
    }

    //
    // PUBLIC METHODS
    //

    public final String getVersionString() {
        return versionString;
    }

    public final int getMajorVersion() {
        return majorVersion;
    }

    public final int getMinorVersion() {
        return minorVersion;
    }

    public final List<String> getAliases() {
        return asList(aliases);
    }
}
