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

package io.tarro.base.attribute;

import io.tarro.base.Valued;

import static io.tarro.base.attribute.AttributeContext.CLASS_FILE;
import static io.tarro.base.attribute.AttributeContext.CODE;
import static io.tarro.base.attribute.AttributeContext.FIELD_INFO;
import static io.tarro.base.attribute.AttributeContext.METHOD_INFO;

/**
 * <p>
 * Enumerates valid {@code target_type} values for a
 * {@code type_annotation} structure appearing within one of the predefined
 * runtime type annotation attributes.
 * </p>
 *
 * <p>
 * Each member of this enumeration represents one of the target type values
 * documented in the Java Virtual Machine Specification's sections on type
 * annotations. The actual {@code target_type} value may be obtained from the
 * {@link #getValue()} method.
 * </p>
 *
 * <p>
 * Target types are {@linkplain Contextualized "contextualized"} meaning that a
 * given target type is only available in a limited set of
 * {@linkplain AttributeContext attribute contexts}. For example, the target
 * types associated with annotations on local variable declarations
 * ({@link #LOCAL_VARIABLE} and
 * {@link #RESOURCE_VARIABLE}) are only valid in the context
 * of a {@linkplain AttributeType#CODE Code} attribute since local variable
 * declarations may only appear in method code. The set of valid attribute
 * contexts for a target type may be obtained from the
 * {@link #getAttributeContext()} method.
 * </p>
 *
 * <p>
 * The naming of this enumeration is a little odd. A member enumerator is meant
 * to answer the question, with respect to a given type annotation, "What
 * <em>type</em> of target is the target of this annotation on a
 * <em>type</em>?" where first sense of the word type is <em>general
 * category</em> and the second sense of type is <em>something the Java language
 * recognizes as a type, such as {@code int}</em>. This enumeration represents
 * the first sense&mdash;general category of target&mdash;and uses the ambiguous
 * word type in service of the broader goal of mirroring the terminology used in
 * the Java Virtual Machine Specification.
 * </p>
 *
 * @author Victor Schappert
 * @since 20171113
 * @see TypePathKind
 * @see AttributeType#RUNTIME_INVISIBLE_TYPE_ANNOTATIONS
 * @see AttributeType#RUNTIME_VISIBLE_TYPE_ANNOTATIONS
 */
public enum TargetType implements Valued, Contextualized {

    //
    // ENUMERATORS
    //

    // In the list below, keep tags organized by the order in which they appear
    // in Chapter 4 ("The class File Format") of the Java Virtual Machine
    // specification.

    // DECLARATION contexts
    /**
     * <p>
     * Signals a {@code type_parameter_target} structure documenting a type
     * parameter annotation annotation appearing on one of the type parameters
     * in the declaration of a generic class or interface.
     * </p>
     *
     * <p>
     * This target type is described in the Java Virtual Machine Specification
     * as "type parameter declaration of generic class or interface". The
     * following example demonstrates how an annotation with this target type
     * can be created in the Java programming language:
     * </p>
     *
     * <blockquote>
     * <pre><code>class {@literal Foo<@Bar T>} { }</code></pre>
     * </blockquote>
     *
     * @see #METHOD_TYPE_PARAMETER
     * @see #CLASS_TYPE_PARAMETER_BOUND
     */
    CLASS_TYPE_PARAMETER(0x00, CLASS_FILE),
    /**
     * <p>
     * Signals a {@code type_parameter_target} structure documenting a type
     * parameter annotation appearing on one of the type parameters in the
     * declaration of a generic method or constructor.
     * </p>
     *
     * <p>
     * This target type is described in the Java Virtual Machine Specification
     * as "type parameter declaration of generic method or constructor". The
     * following example demonstrates how an annotation with this target type
     * can be created in the Java programming language:
     * </p>
     *
     * <blockquote>
     * <pre><code> interface Foo {
     *    {@literal <@Baz T>} void bar(T baz);
     * }</code></pre>
     * </blockquote>
     *
     * @see #CLASS_TYPE_PARAMETER
     * @see #METHOD_TYPE_PARAMETER_BOUND
     * @see #METHOD_RECEIVER
     * @see #METHOD_FORMAL_PARAMETER
     */
    METHOD_TYPE_PARAMETER(0x01, METHOD_INFO),
    /**
     * <p>
     * Signals a {@code supertype_target} structure documenting a type use
     * annotation appearing on a type in the {@code extends} or
     * {@code implements} clause of a class or interface declaration.
     * </p>
     *
     * <p>
     * This target type is described in the Java Virtual Machine Specification
     * as "type in {@code extends} or {@code implements} clause of class
     * declaration (including the direct superclass or direct superinterface of
     * an anonymous class declaration), or in {@code extends} clause of
     * interface declaration". The following example illustrates one of the ways
     * an annotation with this target type may be created in the Java
     * programming language:
     * </p>
     *
     * <blockquote>
     * <pre><code>interface Foo extends @Bar Baz { }</code></pre>
     * </blockquote>
     */
    CLASS_EXTENDS(0x10, CLASS_FILE),
    /**
     * <p>
     * Signals a {@code type_parameter_bound_target} structure documenting a
     * type use annotation appearing on one of the type parameter
     * <em>bounds</em> in the declaration of a generic type or interface.
     * </p>
     *
     * <p>
     * This target type is described in the Java Virtual Machine Specification
     * as "type in bound of type parameter declaration of generic class or
     * interface". The following example shows how an annotation with this
     * target type might be created in the Java programming language:
     * </p>
     *
     * <blockquote>
     * <pre><code>class {@literal Foo<T extends @Bar Baz>} { }</code></pre>
     * </blockquote>
     *
     * @see #METHOD_TYPE_PARAMETER_BOUND
     * @see #CLASS_TYPE_PARAMETER
     */
    CLASS_TYPE_PARAMETER_BOUND(0x11, CLASS_FILE),
    /**
     * <p>
     * Signals a {@code type_parameter_bound_target} structure documenting a
     * type use annotation appearing on one of the type parameter
     * <em>bounds</em> in the declaration of a generic method or constructor.
     * </p>
     *
     * <p>
     * This target type is described in the Java Virtual Machine Specification
     * as "type in bound of type parameter declaration of generic method or
     * constructor". The following example shows how an annotation with this
     * target type might be created in the Java programming language:
     * </p>
     *
     * <blockquote>
     * <pre><code> class Foo {
     *     static {@literal <T extends @Bar Baz>} T baz(T ham) {
     *         return ham.withEggs();
     *     }
     * }</code></pre>
     * </blockquote>
     *
     * @see #CLASS_TYPE_PARAMETER_BOUND
     * @see #METHOD_TYPE_PARAMETER
     * @see #METHOD_RECEIVER
     * @see #METHOD_FORMAL_PARAMETER
     */
    METHOD_TYPE_PARAMETER_BOUND(0x12, METHOD_INFO),
    /**
     * <p>
     * Signals an {@code empty_target} structure documenting a type use
     * annotation appearing directly on the type of a field declaration.
     * </p>
     *
     * <p>
     * This target type is described in the Java Virtual Machine Specification
     * as "type in field declaration". The following example shows how an
     * annotation with this target type may be written in the Java programming
     * language:
     * </p>
     *
     * <blockquote>
     * <pre><code> class Foo {
     *    {@literal @Bar} Baz baz;
     * }</code></pre>
     * </blockquote>
     */
    FIELD(0x13, FIELD_INFO),
    /**
     * <p>
     * Signals an {@code empty_target} structure documenting a type use
     * annotation appearing <em>either</em> on the return type of a method
     * <em>or</em> on a constructor.
     * </p>
     *
     * <p>
     * This target type is described in the Java Virtual Machine Specification
     * as "return type of method, or type of newly constructed object ". The
     * example below shows how an annotation with this target type may be
     * written in the Java programming language:
     * </p>
     *
     * <blockquote>
     * <pre><code> class Foo {
     *    {@literal @Bar} Foo() { }
     *    {@literal @Bar} Baz baz() {
     *         return new Baz();
     *     }
     * }</code></pre>
     * </blockquote>
     *
     * @see #METHOD_RECEIVER
     * @see #METHOD_FORMAL_PARAMETER
     */
    METHOD_RETURN(0x14, METHOD_INFO),
    /**
     * <p>
     * Signals an {@code empty_target} structure documenting a type use
     * annotation appearing on the receiver type of a method or constructor.
     * </p>
     *
     * <p>
     * This target type is described in the Java Virtual Machine Specification
     * as "receiver type of method or constructor". The example below shows how
     * an annotation with this target type may be written in the Java
     * programming language:
     * </p>
     *
     * <blockquote>
     * <pre><code> class Foo extends Bar {
     *     void baz({@literal @Baz} Foo this, int none) {
     *         bar(none);
     *     }
     * }</code></pre>
     * </blockquote>
     *
     * @see #METHOD_RETURN
     * @see #METHOD_FORMAL_PARAMETER
     */
    METHOD_RECEIVER(0x15, METHOD_INFO),
    /**
     * <p>
     * Signals a {@code formal_parameter_target} structure documenting a type
     * use annotation appearing on a formal parameter of a method, constructor,
     * or lambda expression.
     * </p>
     *
     * <p>
     * This target type is described in the Java Virtual Machine Specification
     * as "type in formal parameter declaration of method, constructor, or
     * lambda expression". The following example demonstrates one way in which
     * an annotation with this target type may be expressed in the Java
     * programming language:
     * </p>
     *
     * <blockquote>
     * <pre><code> enum Foo {
     *     BAR;
     *     static int bar = java.util.Arrays.stream(values())
     *         .mapToInt({@literal (@Baz Foo foo) -> foo.ordinal()})
     *         .sum();
     * }</code></pre>
     * </blockquote>
     *
     * @see #METHOD_RETURN
     * @see #METHOD_RECEIVER
     */
    METHOD_FORMAL_PARAMETER(0x16, METHOD_INFO),
    /**
     * <p>
     * Signals a {@code throws_target} structure documenting a type use
     * annotation appearing in the {@code throws} clause of a method or
     * constructor.
     * </p>
     *
     * <p>
     * This target type is described in the Java Virtual Machine Specification
     * as "type in {@code throws} clause of method or constructor". The
     * following example demonstrates how an annotation with this target type
     * may be expressed in the Java programming language:
     * </p>
     *
     * <blockquote>
     * <pre><code> interface Foo {
     *      void bar() throws {@literal @Baz} Exception;
     * }</code></pre>
     * </blockquote>
     */
    THROWS(0x17, METHOD_INFO),

    // EXPRESSION contexts
    /**
     * <p>
     * Signals a {@code localvar_target} structure documenting a type use
     * annotation appearing on the type in a local variable declaration.
     * </p>
     *
     * <p>
     * This target type is described in the Java Virtual Machine Specification
     * as "type in local variable declaration". The following example shows how
     * an annotation with this target type may be expressed in the Java
     * programming language:
     * </p>
     *
     * <blockquote>
     * <pre><code> class Foo {
     *     Foo() {
     *         {@literal @Bar} Baz baz = new Baz();
     *         baz.bazzle();
     *     }
     * }</code></pre>
     * </blockquote>
     *
     * @see #RESOURCE_VARIABLE
     */
    LOCAL_VARIABLE(0x40, CODE),
    /**
     * <p>
     * Signals a {@code localvar_target} structure documenting a type use
     * annotation appearing on the type in a local variable declaration in the
     * resources clause of a {@code try}-with-resources statement.
     * </p>
     *
     * <p>
     * This target type is described in the Java Virtual Machine specification
     * as "type in resource variable declaration" . The following example shows
     * how an annotation with this target type may be expressed in the Java
     * programming language:
     * </p>
     *
     * <blockquote>
     * <pre><code> class Foo {
     *     void bar() {
     *         try ({@literal @Baz} Resource resource = new Resource()) {
     *             resource.use();
     *         }
     *     }
     * }</code></pre>
     * </blockquote>
     *
     * @see #LOCAL_VARIABLE
     */
    RESOURCE_VARIABLE(0x41, CODE),
    /**
     * Signals a {@code catch_target} structure documenting a type use
     * annotation appearing on a type in the exception parameter declaration
     * part of a {@code catch} clause.
     *
     * <p>
     * This target type is described in the Java Virtual Machine Specification
     * as "type in exception parameter declaration". The snippet below gives an
     * example of how an annotation with this target type may be expressed in
     * the Java programming language:
     * </p>
     *
     * <blockquote>
     * <pre><code> class Foo {
     *     static {
     *         try {
     *             new Bar().maybeThrow();
     *         } catch ({@literal @Baz} Exception  e) {
     *             handle(e);
     *         }
     *     }
     * }</code></pre>
     * </blockquote>
     */
    EXCEPTION_PARAMETER(0x42, CODE),
    /**
     * <p>
     * Signals an {@code offset_target} structure documenting a type use
     * annotation on the type in an {@code instanceof} expression.
     * </p>
     *
     * <p>
     * This target type is described in the Java Virtual Machine Specification
     * as "type in <em>instanceof</em> expression". The snippet below gives an
     * example of how an annotation with this target type may be expressed in
     * the Java programming language:
     * </p>
     *
     * <blockquote>
     * <pre><code> abstract class Foo implements Bar {
     *     Foo(Bar other) {
     *         if (other instanceof {@literal @Baz} Foo) {
     *             ((Foo)other).foo(this);
     *         }
     *     }
     *     abstract void foo(Foo other);
     * }</code></pre>
     * </blockquote>
     *
     * @see #NEW
     * @see #CONSTRUCTOR_REFERENCE
     * @see #METHOD_REFERENCE
     */
    INSTANCEOF(0x43, CODE),
    /**
     * <p>
     * Signals an {@code offset_target} structure documenting a type use
     * annotation on the type in a {@code new} expression.
     * </p>
     *
     * <p>
     * This target type is described in the Java Virtual Machine Specification
     * as "type in <em>new</em> expression". The snippet below gives an
     * example of how an annotation with this target type may be expressed in
     * the Java programming language:
     * </p>
     *
     * <blockquote>
     * <pre><code>Foo foo = new {@literal @Bar} Baz();</code></pre>
     * </blockquote>
     *
     * @see #INSTANCEOF
     * @see #CONSTRUCTOR_REFERENCE
     * @see #METHOD_REFERENCE
     */
    NEW(0x44, CODE),
    /**
     * <p>
     * Signals an {@code offset_target} structure documenting a type use
     * annotation on a method reference expression involving a constructor.
     * </p>
     *
     * <p>
     * This target type is described in the Java Virtual Machine Specification
     * as "type in method reference expression using {@code ::}<em>new</em>". The
     * example below shows how this variety of type annotation might be
     * expressed in the Java programming language:
     * </p>
     *
     * <blockquote>
     * <pre><code>Foo[] foos = barList.stream()
     *                    .map(Bar::toFoo)
     *                    .peek(Foo::fooify)
     *                    .toArray({@literal @Baz} Foo[]::new);</code></pre>
     * </blockquote>
     *
     * @see #INSTANCEOF
     * @see #NEW
     * @see #METHOD_REFERENCE
     * @see #CONSTRUCTOR_REFERENCE_TYPE_ARGUMENT
     */
    CONSTRUCTOR_REFERENCE(0x45, CODE),
    /**
     * <p>
     * Signals an {@code offset_target} structure documenting a type use
     * annotation on a method reference expression involving a method that is
     * not a constructor.
     * </p>
     *
     * <p>
     * This target type is described in the Java Virtual Machine Specification
     * as "type in method reference expression using
     * {@code ::}<em>Identifier</em>". The example below shows how this kind of
     * type annotation might be written in the Java programming language:
     * </p>
     *
     * <blockquote>
     * <pre><code>fooList.stream()
     *       .filter(@Bar Foo::isBaz)
     *       .forEach(Foo::bazzify);</code></pre>
     * </blockquote>
     *
     * @see #INSTANCEOF
     * @see #NEW
     * @see #CONSTRUCTOR_REFERENCE
     * @see #METHOD_REFERENCE_TYPE_ARGUMENT
     */
    METHOD_REFERENCE(0x46, CODE),
    /**
     * <p>
     * Signals a {@code type_argument_target} structure documenting a type use
     * annotation on the type in a type cast expression.
     * </p>
     *
     * <p>
     * This target type is described in the Java Virtual Machine Specification
     * as "type in cast expression". The example below shows how this variety of
     * type annotation might be written in the Java programming language:
     * </p>
     *
     * <blockquote>
     * <pre><code> class Foo {
     *     byte foo(float bar) {
     *         return ({@literal @Baz} byte)(bar + 0.5f);
     *     }
     * }</code></pre>
     * </blockquote>
     *
     * @see #CONSTRUCTOR_INVOCATION_TYPE_ARGUMENT
     * @see #METHOD_INVOCATION_TYPE_ARGUMENT
     * @see #CONSTRUCTOR_REFERENCE_TYPE_ARGUMENT
     * @see #METHOD_REFERENCE_TYPE_ARGUMENT
     */
    CAST(0x47, CODE),
    /**
     * <p>
     * Signals a {@code type_argument_target} structure documenting a type use
     * annotation on a type argument in a generic constructor invocation
     * expression.
     * </p>
     *
     * <p>
     * This target type is described in the Java Virtual Machine Specification
     * as "type argument for generic constructor in new expression or explicit
     * constructor invocation statement". The example below shows some ways in
     * which this variety of type annotation might be written in the Java
     * programming language:
     * </p>
     *
     * <blockquote>
     * <pre><code> {@literal class Foo<T> extends Bar<Object>} {
     *     {@literal <U extends Number>} Foo(U value) {
     *         {@literal <@Baz Object>}super(value);
     *     }
     *     Foo() {
     *         {@literal <@Baz Integer>}this(0);
     *     }
     *     {@literal Foo<Integer>} foo(int value) {
     *         {@literal return new<@Baz Integer> Foo<Integer>(value);}
     *     }
     * }</code></pre>
     * </blockquote>
     *
     * @see #CAST
     * @see #METHOD_INVOCATION_TYPE_ARGUMENT
     * @see #CONSTRUCTOR_REFERENCE_TYPE_ARGUMENT
     * @see #METHOD_REFERENCE_TYPE_ARGUMENT
     */
    CONSTRUCTOR_INVOCATION_TYPE_ARGUMENT(0x48, CODE),
    /**
     * <p>
     * Signals a {@code type_argument_target} structure documenting a type use
     * annotation on a type argument in a generic method invocation expression.
     * </p>
     *
     * <p>
     * This target type is described in the Java Virtual Machine Specification
     * as "type argument for generic method in method invocation expression".
     * The following example shows how this variety of type annotation may be
     * written in the Java programming language:
     * </p>
     *
     * <blockquote>
     * <pre><code> class {@literal Foo<T>} {
     *     {@literal List<T>} foo(T value) {
     *         return Collections.{@literal <@Bar T>}singletonList(value);
     *     }
     * }</code></pre>
     * </blockquote>
     *
     * @see #CAST
     * @see #CONSTRUCTOR_INVOCATION_TYPE_ARGUMENT
     * @see #CONSTRUCTOR_REFERENCE_TYPE_ARGUMENT
     * @see #METHOD_REFERENCE_TYPE_ARGUMENT
     */
    METHOD_INVOCATION_TYPE_ARGUMENT(0x49, CODE),
    /**
     * <p>
     * Signals a {@code type_argument_target} structure documenting a type use
     * annotation on a type argument in a method reference expression involving
     * a generic constructor.
     * </p>
     *
     * <p>
     * This target type is described in the Java Virtual Machine Specification
     * as "type argument for generic constructor in method reference expression
     * using {@code ::}<em>new</em>". The following example shows how this
     * variety of type annotation may be expressed in the Java programming
     * language:
     * </p>
     *
     * <blockquote>
     * <pre><code> import java.util.List;
     * import static java.util.stream.Collectors.toList;
     *
     * class Foo {
     *     Bar bar;
     *     {@literal <U extends Bar> Foo(List<U> barList)} {
     *         this.bar = barList.get(0);
     *     }
     *     static {@literal <U extends Bar> List<Foo> fooify(List<List<U>> barLists)} {
     *         return barLists.stream()
     *             {@literal .map(Foo::<@Baz U>new)}
     *             .collect(toList());
     *         }
     *     }
     * }</code></pre>
     * </blockquote>
     *
     * @see #CAST
     * @see #CONSTRUCTOR_INVOCATION_TYPE_ARGUMENT
     * @see #METHOD_INVOCATION_TYPE_ARGUMENT
     * @see #METHOD_REFERENCE_TYPE_ARGUMENT
     * @see #CONSTRUCTOR_REFERENCE
     */
    CONSTRUCTOR_REFERENCE_TYPE_ARGUMENT(0x4a, CODE),
    /**
     * <p>
     * Signals a {@code type_argument_target} structure documenting a type use
     * annotation on a type argument in a method reference expression involving
     * a generic method which is not a constructor.
     * </p>
     *
     * <p>
     * This target type is described in the Java Virtual Machine Specification
     * as "type argument for generic method in method reference expression using
     * {@code ::}<em>Identifier</em>". The following example demonstrates how an
     * annotation with this target type can be created in the Java programming
     * language:
     * </p>
     *
     * <blockquote>
     * <pre><code> class {@literal Foo<T>} {
     *     T tee;
     *     {@literal <U extends T> void setTee(Map<String, U> map)} {
     *         tee = map.get("foo");
     *     }
     *     {@literal <U extends T> Consumer<Map<String, U>> getTeeSetter()} {
     *         {@literal return this::<@Bar U>setTee;}
     *     }
     * }</code></pre>
     * </blockquote>
     */
    METHOD_REFERENCE_TYPE_ARGUMENT(0x4b, CODE);

    //
    // DATA
    //

    private final int value;
    private final int attributeContext;

    //
    // CONSTRUCTORS
    //

    TargetType(final int value, final int attributeContext) {
        this.value = value;
        this.attributeContext = attributeContext;
    }

    //
    // INTERFACE: Valued
    //

    @Override
    public int getValue() {
        return value;
    }

    //
    // INTERFACE: Contextualized
    //

    @Override
    public int getAttributeContext() {
        return attributeContext;
    }
}
