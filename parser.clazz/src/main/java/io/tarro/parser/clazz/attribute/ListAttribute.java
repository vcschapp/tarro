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

package io.tarro.parser.clazz.attribute;

import io.tarro.base.attribute.AttributeType;

import java.util.List;

import static io.tarro.base.attribute.AttributeType.BOOTSTRAP_METHODS;
import static io.tarro.base.attribute.AttributeType.INNER_CLASSES;
import static io.tarro.base.attribute.AttributeType.LINE_NUMBER_TABLE;
import static io.tarro.base.attribute.AttributeType.LOCAL_VARIABLE_TABLE;
import static io.tarro.base.attribute.AttributeType.LOCAL_VARIABLE_TYPE_TABLE;
import static io.tarro.base.attribute.AttributeType.METHOD_PARAMETERS;
import static io.tarro.base.attribute.AttributeType.RUNTIME_INVISIBLE_ANNOTATIONS;
import static io.tarro.base.attribute.AttributeType.RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS;
import static io.tarro.base.attribute.AttributeType.RUNTIME_INVISIBLE_TYPE_ANNOTATIONS;
import static io.tarro.base.attribute.AttributeType.RUNTIME_VISIBLE_ANNOTATIONS;
import static io.tarro.base.attribute.AttributeType.RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS;
import static io.tarro.base.attribute.AttributeType.RUNTIME_VISIBLE_TYPE_ANNOTATIONS;
import static io.tarro.base.attribute.AttributeType.STACK_MAP_TABLE;

/**
 * <p>
 * Predefined attribute that consists only of a list of members.
 * </p>
 *
 * <p>
 * At present, the parser extracts the following predefined attributes as
 * instances of this class:
 * </p>
 *
 * <table>
 * <caption>
 * <em>Table of attribute types parsing to {@link ListAttribute} with their
 * corresponding list members.</em>
 * </caption>
 * <thead>
 * <tr>
 * <th>Attribute</th>
 * <th>List Member Type {@code <T>}</th>
 * </tr>
 * </thead>
 * <tbody>
 * <tr>
 * <td>{@linkplain AttributeType#BOOTSTRAP_METHODS BootstrapMethods}</td>
 * <td>{@link BootstrapMethod}</td>
 * </tr>
 * <tr>
 * <td>{@linkplain AttributeType#INNER_CLASSES InnerClasses}</td>
 * <td>{@link InnerClass}</td>
 * </tr>
 * <tr>
 * <td>{@linkplain AttributeType#LINE_NUMBER_TABLE LineNumberTable}</td>
 * <td>{@link LineNumberTableEntry}</td>
 * </tr>
 * <tr>
 * <td>{@linkplain AttributeType#LOCAL_VARIABLE_TABLE LocalVariableTable}</td>
 * <td>{@link LocalVariableTableEntry}</td>
 * </tr>
 * <tr>
 * <td>{@linkplain AttributeType#LOCAL_VARIABLE_TYPE_TABLE LocalVariableTypeTable}</td>
 * <td>{@link LocalVariableTableEntry}</td>
 * </tr>
 * <tr>
 * <td>{@linkplain AttributeType#METHOD_PARAMETERS MethodParameters}</td>
 * <td>{@link MethodParameter}</td>
 * </tr>
 * <tr>
 * <td>{@linkplain AttributeType#RUNTIME_INVISIBLE_ANNOTATIONS RuntimeInvisibleAnnotations}</td>
 * <td>{@link Annotation}</td>
 * </tr>
 * <tr>
 * <td>{@linkplain AttributeType#RUNTIME_VISIBLE_ANNOTATIONS RuntimeVisibleAnnotations}</td>
 * <td>{@link Annotation}</td>
 * </tr>
 * <tr>
 * <td>{@linkplain AttributeType#RUNTIME_INVISIBLE_TYPE_ANNOTATIONS RuntimeInvisibleTypeAnnotations}</td>
 * <td>{@link Annotation}</td>
 * </tr>
 * <tr>
 * <td>{@linkplain AttributeType#RUNTIME_VISIBLE_TYPE_ANNOTATIONS RuntimeVisibleTypeAnnotations}</td>
 * <td>{@link Annotation}</td>
 * </tr>
 * <tr>
 * <td>{@linkplain AttributeType#RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS RuntimeInvisibleParameterAnnotations}</td>
 * <td>{@link ParameterAnnotations}</td>
 * </tr>
 * <tr>
 * <td>{@linkplain AttributeType#RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS RuntimeVisibleParameterAnnotations}</td>
 * <td>{@link ParameterAnnotations}</td>
 * </tr>
 * <tr>
 * <td>{@linkplain AttributeType#STACK_MAP_TABLE StackMapTable}</td>
 * <td>{@link StackMapTableEntry}</td>
 * </tr>
 * </tbody>
 * </table>
 *
 * @author Victor Schappert
 * @since 20171125
 * @param <T> Type of the list members (see the above table for more detail)
 */
public final class ListAttribute<T> extends Attribute {

    //
    // DATA
    //

    private final List<T> list;

    //
    // CONSTRUCTORS
    //

    public ListAttribute(final AttributeType attributeType, final T[] array) {
        super(attributeType);
        assert BOOTSTRAP_METHODS == attributeType ||
                INNER_CLASSES == attributeType ||
                LINE_NUMBER_TABLE == attributeType ||
                LOCAL_VARIABLE_TABLE == attributeType || LOCAL_VARIABLE_TYPE_TABLE == attributeType ||
                METHOD_PARAMETERS == attributeType ||
                RUNTIME_INVISIBLE_ANNOTATIONS == attributeType || RUNTIME_VISIBLE_ANNOTATIONS == attributeType ||
                RUNTIME_INVISIBLE_TYPE_ANNOTATIONS == attributeType || RUNTIME_VISIBLE_TYPE_ANNOTATIONS == attributeType ||
                RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS == attributeType || RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS == attributeType ||
                STACK_MAP_TABLE == attributeType;
        // TODO: Annotate the attribute list members with their attribute type,
        //       and then assert that the class of array is annotated with a proper attribute type.
        list = List.of(array);
    }

    //
    // PUBLIC METHODS
    //

    public List<T> getList() {
        return list;
    }
}
