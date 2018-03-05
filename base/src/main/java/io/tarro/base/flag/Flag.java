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

import io.tarro.base.Valued;
import io.tarro.base.Versioned;

import java.util.EnumSet;

import static java.util.EnumSet.noneOf;

/**
 * <p>
 * A single flag value within a flag field in a Java class.
 * </p>
 *
 * <p>
 * In Java class files, a flag <em>field</em> is a 16-bit ({@code u2}) value
 * containing the bitwise combination of individual flag <em>values</em> taken
 * from a defined set of values. Many flag <em>fields</em> are named
 * {@code access_flags} or end with the suffix "access_flags" and, for
 * historical, reasons the Java Virtual Machine Specification names all flag
 * values {@code ACC_FOO}, {@code ACC_BAR}, <em>etc.</em>; but many flag values
 * do not relate to the concept of access at all; and some flag fields, such as
 * the {@code module_flags} field in the Java 9+ "Module" attribute do not have
 * the word "access" in the field name.
 * </p>
 *
 * <p>
 * An {@code enum} class may implement this interface in order to provide an
 * enumeration of flag <em>values</em> applicable to a particular flag
 * <em>field</em>. The following enumerations are defined in this package:
 * </p>
 *
 * <table>
 * <caption>Flag value enumerations</caption>
 * <thead>
 * <tr>
 * <th>Flag Field Location</th>
 * <th>Flag Field Name</th>
 * <th>Value Enumeration</th>
 * <th>Java Version</th>
 * </tr>
 * </thead>
 * <tbody>
 * <tr>
 * <td>Top level of class file</td>
 * <td>{@code access_flags}</td>
 * <td>{@link ClassAccessFlag}</td>
 * <td>{@linkplain io.tarro.base.ClassFileVersion#JAVA1 Java 1}</td>
 * </tr>
 * <tr>
 * <td>{@code field_info} structure within class {@code fields} table</td>
 * <td>{@code access_flags}</td>
 * <td>{@link FieldAccessFlag}</td>
 * <td>{@linkplain io.tarro.base.ClassFileVersion#JAVA1 Java 1}</td>
 * </tr>
 * <tr>
 * <td>{@code method_info} structure within class {@code methods} table</td>
 * <td>{@code access_flags}</td>
 * <td>{@link MethodAccessFlag}</td>
 * <td>{@linkplain io.tarro.base.ClassFileVersion#JAVA1 Java 1}</td>
 * </tr>
 * <tr>
 * <td>
 * {@code classes} table within {@code InnerClasses_attribute} structure in
 * class {@code attributes} table
 * </td>
 * <td>{@code inner_class_access_flags}</td>
 * <td>{@link InnerClassAccessFlag}</td>
 * <td>{@linkplain io.tarro.base.ClassFileVersion#JAVA1_1 Java 1.1}</td>
 * </tr>
 * <tr>
 * <td>
 * {@code parameters} table within {@code MethodParameters_attribute} structure
 * in method {@code attributes} table
 * </td>
 * <td>{@code access_flags}</td>
 * <td>{@link MethodParameterAccessFlag}</td>
 * <td>{@linkplain io.tarro.base.ClassFileVersion#JAVA8 Java 8}</td>
 * </tr>
 * <tr>
 * <td>
 * {@code Module_attribute} structure within class {@code attributes} table
 * </td>
 * <td>{@code module_flags}</td>
 * <td>{@link ModuleFlag}</td>
 * <td>{@linkplain io.tarro.base.ClassFileVersion#JAVA9 Java 9}</td>
 * </tr>
 * <tr>
 * <td>
 * {@code requires} table within {@code Module_attribute} structure in class
 * {@code attributes} table
 * </td>
 * <td>{@code requires_flags}</td>
 * <td>{@link ModuleReferenceFlag}</td>
 * <td>{@linkplain io.tarro.base.ClassFileVersion#JAVA9 Java 9}</td>
 * </tr>
 * <tr>
 * <td>
 * {@code exports} table within {@code Module_attribute} structure in class
 * {@code attributes} table
 * </td>
 * <td>{@code exports_flags}</td>
 * <td>{@link PackageReferenceFlag}</td>
 * <td>{@linkplain io.tarro.base.ClassFileVersion#JAVA9 Java 9}</td>
 * </tr>
 * <tr>
 * <td>
 * {@code opens} table within {@code Module_attribute} structure in class
 * {@code attributes} table
 * </td>
 * <td>{@code opens_flags}</td>
 * <td>{@link PackageReferenceFlag}</td>
 * <td>{@linkplain io.tarro.base.ClassFileVersion#JAVA9 Java 9}</td>
 * </tr>
 * </tbody>
 * </table>
 *
 * <p>
 * Since a flag <em>field</em> is a bit set, the individual flag <em>values</em>
 * that may be set in the field each correspond to one bit in the bit set. The
 * bit for a given flag enumerator may be obtained from the {@link #getValue()}
 * method.
 * </p>
 *
 * @author Victor Schappert
 * @since 20171014
 */
public interface Flag extends Valued, Versioned {
    //
    // PUBLIC METHODS
    //

    /**
     * <p>
     * Obtains the flag value's name, as set out in the Java Virtual Machine
     * Specification.
     * </p>
     *
     * <p>
     * For example, the value returned for a value enumerator named
     * {@code PUBLIC} is {@code "ACC_PUBLIC"}.
     * </p>
     *
     * @return Flag name
     */
    String getFlagName();

    //
    // STATIC METHODS
    //

    /**
     * <p>
     * Converts the integer value read from a flag field into its constituent
     * flag values.
     * </p>
     *
     * <p>
     * In keeping with the general direction throughout the Java Virtual Machine
     * Specification that bits not assigned in the Specification but present in
     * a class file should be ignored by the Java Virtual Machine
     * implementation, this method ignores any bits in the input field value
     * which do not correspond to the value of a flag in the given flag
     * enumeration.
     * </p>
     *
     * <p>
     * Example:
     * </p>
     *
     * <blockquote><code>parse({@link ClassAccessFlag}.class, 0x11) &rarr; [FINAL, PUBLIC]
     * </code></blockquote>
     *
     * @param flagClass Flag enumeration class enumerating the available flag
     *                  values that can be parsed from the field
     * @param fieldValue Value of the flag field, interpreted as a bit set
     * @param <F> Type of the flag enumeration class
     * @return Set of flag values parsed from the flag field
     */
    static <F extends Enum<F> & Flag> EnumSet<F> parse(final Class<F> flagClass,
                                                       final int fieldValue) {
        final EnumSet<F> set = noneOf(flagClass);
        for (final F flag : flagClass.getEnumConstants()) {
            final int flagBits = flag.getValue();
            if (flagBits == (fieldValue & flagBits)) {
                set.add(flag);
            }
        }
        return set;
    }
}
