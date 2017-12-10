package io.tarro.parser.clazz.attribute;

import io.tarro.base.attribute.AttributeType;

import static io.tarro.base.attribute.AttributeType.CONSTANT_VALUE;
import static io.tarro.base.attribute.AttributeType.MODULE_MAIN_CLASS;
import static io.tarro.base.attribute.AttributeType.SIGNATURE;
import static io.tarro.base.attribute.AttributeType.SOURCE_FILE;

/**
 * <p>
 * Predefined attribute whose only meaningful data consist of a single index
 * into the constant pool.
 * </p>
 *
 * <p>
 * At present, the following predefined attributes are returned as instances of
 * this class:
 * </p>
 *
 * <ul>
 * <li>{@linkplain AttributeType#CONSTANT_VALUE ConstantValue};</li>
 * <li>{@linkplain AttributeType#MODULE_MAIN_CLASS ModuleMainClass}; and</li>
 * <li>{@linkplain AttributeType#SIGNATURE Signature}.</li>
 * </ul>
 *
 * @author Victor Schappert
 * @since 20171030
 * @see ConstantPoolIndicesAttribute
 */
public final class ConstantPoolIndexAttribute extends Attribute {

    //
    // DATA
    //

    private final int constantPoolIndex;

    //
    // CONSTRUCTORS
    //

    /**
     * Creates a new constant pool index attribute.
     *
     * @param attributeType Type of the attribute
     * @param constantPoolIndex Attribute's constant pool index
     */
    public ConstantPoolIndexAttribute(final AttributeType attributeType, final int constantPoolIndex) {
        super(attributeType);
        assert CONSTANT_VALUE == attributeType ||
                MODULE_MAIN_CLASS == attributeType ||
                SIGNATURE == attributeType || SOURCE_FILE == attributeType;
        this.constantPoolIndex = constantPoolIndex;
    }

    //
    // PUBLIC METHODS
    //

    /**
     * <p>
     * Obtains the constant pool index associated with the attribute.
     * </p>
     *
     * <p>
     * The constant pool index has the following meaning depending on the
     * {@linkplain #getAttributeType() attribute type}:
     * </p>
     *
     * <table>
     * <tr>
     * <th>Attribute Type</th>
     * <th>Attribute Name</th>
     * <th>Meaning</th>
     * </tr>
     * <tr>
     * <td>{@link AttributeType#CONSTANT_VALUE}</td>
     * <td>ConstantValue</td>
     * <td>{@code constantvalue_index}</td>
     * </tr>
     * <tr>
     * <td>{@link AttributeType#MODULE_MAIN_CLASS}</td>
     * <td>ModuleMainClass</td>
     * <td>{@code main_class_index}</td>
     * </tr>
     * <tr>
     * <td>{@link AttributeType#SIGNATURE}</td>
     * <td>Signature</td>
     * <td>{@code signature_index}</td>
     * </tr>
     * </table>
     *
     * @return Constant pool index associated with the attribute
     */
    public int getConstantPoolIndex() {
        return constantPoolIndex;
    }
}
