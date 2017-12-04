package io.tarro.parser.clazz.internal.context.annotation;

import io.tarro.parser.clazz.ClassParser;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <p>
 * Indicates that the current method is parsing an element of an array.
 * </p>
 *
 * <p>
 * This is an internal annotation used by the {@link ClassParser} to help it
 * figure out its the "human readable" location within the class file for error
 * reporting purposes.
 * </p>
 *
 * @author Victor Schappert
 * @since 20171126
 * @see AttributeTypeContext
 * @see ConstantPoolTagContext
 * @see ContextSymbol
 * @see FrameTypeContext
 * @see StructureContext
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface ArrayContext {
    String value() default ""; /** TODO: If empty should look up name of previous item. **/
}
