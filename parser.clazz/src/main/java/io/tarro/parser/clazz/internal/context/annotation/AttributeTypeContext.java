package io.tarro.parser.clazz.internal.context.annotation;

import io.tarro.base.attribute.AttributeType;
import io.tarro.parser.clazz.ClassParser;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Indicates that the current method is parsing an attribute of a known type.
 *
 * This is an internal annotation used by the {@link ClassParser} to help it
 * figure out its the "human readable" location within the class file for error
 * reporting purposes.
 *
 * @author Victor Schappert
 * @since 20171126
 * @see ArrayContext
 * @see ConstantPoolTagContext
 * @see ContextSymbol
 * @see FrameTypeContext
 * @see StructureContext
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface AttributeTypeContext {
    AttributeType value();
}
