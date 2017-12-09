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

package io.tarro.parser.clazz.internal.context.path;

import io.tarro.parser.clazz.internal.context.annotation.ArrayContext;
import io.tarro.parser.clazz.internal.context.annotation.AttributeTypeContext;
import io.tarro.parser.clazz.internal.context.annotation.ConstantPoolTagContext;
import io.tarro.parser.clazz.internal.context.annotation.ContextSymbol;
import io.tarro.parser.clazz.internal.context.annotation.ContextSymbols;
import io.tarro.parser.clazz.internal.context.annotation.FrameTypeContext;
import io.tarro.parser.clazz.internal.context.annotation.StructureContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static io.tarro.base.InternalError.internalError;
import static io.tarro.base.InternalError.unhandledConstant;
import static io.tarro.parser.clazz.internal.context.path.ContextPathEntry.FIELD_ENTRY_TYPE;
import static io.tarro.parser.clazz.internal.context.path.ContextPathEntry.NOTHING;
import static io.tarro.parser.clazz.internal.context.path.ContextPathEntry.SUBSCRIPT_ENTRY_TYPE;
import static io.tarro.parser.clazz.internal.context.path.ContextPathEntry.TYPE_ENTRY_TYPE;
import static java.util.Arrays.copyOfRange;
import static java.util.Arrays.stream;
import static java.util.stream.IntStream.range;

import java.lang.StackWalker.StackFrame;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * <p>
 *
 * </p>
 *
 * @author Victor Schappert
 * @since 20171202
 */
public final class ContextPathTracer {

    //
    // DATA
    //

    private final List<ContextPathEntry> contextPath;
    private final Annotation[][] contextAnnotationsSteps;
    private final int[] arrayContext;
    private int arrayContextPos;

    //
    // CONSTRUCTORS
    //

    private ContextPathTracer(final Annotation[][] contextAnnotationsSteps, final int[] arrayContext) {
        this.contextPath = new ArrayList<>();
        this.contextAnnotationsSteps = contextAnnotationsSteps;
        this.arrayContext = arrayContext;
        this.arrayContextPos = 0;
    }

    //
    // PUBLIC STATICS
    //

    public static String traceContextPath(final Class<?> clazz, final int[] arrayContext, final int arrayDepth, final String lastFieldName) {
        final List<ContextPathEntry> contextPath = traceContextPath(clazz, copyOfRange(arrayContext, 0, arrayDepth));
        final StringBuilder builder = new StringBuilder(128);
        int previousEntryType = NOTHING;
        for (final ContextPathEntry entry : contextPath) {
            final int currentEntryType = entry.getEntryType();
            switch(currentEntryType) {
            case FIELD_ENTRY_TYPE:
                if (NOTHING != previousEntryType) {
                    builder.append('.');
                }
                // Fall through.
            case TYPE_ENTRY_TYPE:
            case SUBSCRIPT_ENTRY_TYPE:
                builder.append(entry);
                previousEntryType = currentEntryType;
                break;
            default:
                throw unhandledConstant(currentEntryType);
            }
        }
        if (NOTHING != previousEntryType) {
            builder.append('.');
        }
        builder.append(lastFieldName);
        return builder.toString();
    }

    //
    // PACKAGE-INTERNAL METHODS
    //

    // TODO: Annotate visible for testing?
    static List<ContextPathEntry> traceContextPath(final Class<?> clazz, final int[] arrayContext) {
        final Predicate<StackFrame> hasClassName = hasClassName(clazz);
        final Annotation[][] contextAnnotationSteps = StackWalker.getInstance().walk(s -> s.dropWhile(hasClassName.negate())
                .takeWhile(hasClassName)
                .map(ContextPathTracer::getContextAnnotationsForFrame)
                .filter(annotations -> 0 < annotations.length)
                .toArray(Annotation[][]::new));
        return traceContextPath(contextAnnotationSteps, arrayContext);
    }

    // TODO: Annotate visible for testing?
    static List<ContextPathEntry> traceContextPath(final Annotation[][] contextAnnotations, final int[] arrayContext) {
        final ContextPathTracer contextPathTracer = new ContextPathTracer(contextAnnotations, arrayContext);
        return contextPathTracer.traceContextPath();
    }

    //
    // INTERNALS
    //

    private List<ContextPathEntry> traceContextPath() {
        reverse(arrayContext);
        range(0, contextAnnotationsSteps.length).forEach(this::traceContextPathStep);
        Collections.reverse(contextPath);
        return contextPath;
    }

    private void traceContextPathStep(final int stepIndex) {
        range(0, contextAnnotationsSteps[stepIndex].length).forEach(annotationIndex -> {
            final Annotation annotation = contextAnnotationsSteps[stepIndex][annotationIndex];
            if (annotation instanceof ArrayContext) {
                arrayContext((ArrayContext)annotation, stepIndex);
            } else if (annotation instanceof AttributeTypeContext) {
                attributeTypeContext((AttributeTypeContext)annotation);
            } else if (annotation instanceof ConstantPoolTagContext) {
                constantPoolTagContext((ConstantPoolTagContext)annotation);
            } else if (annotation instanceof FrameTypeContext) {
                frameTypeContext((FrameTypeContext)annotation);
            } else if (annotation instanceof StructureContext) {
                structureContext((StructureContext)annotation, stepIndex);
            }
        });
    }

    private void arrayContext(final ArrayContext context, final int stepIndex) {
        if (arrayContextPos < arrayContext.length) {
            addSubscriptContext(arrayContext[arrayContextPos++]);
            if (0 < context.value().length()) {
                addFieldContext(context.value(), stepIndex);
            }
        } else {
            throw cantTraceContextPath("array context %s is fully consumed at step %d (path so far: %s)",
                    Arrays.toString(arrayContext), stepIndex, contextPath);
        }
    }

    private void attributeTypeContext(final AttributeTypeContext context) {
        addTypeContext(context.value());
    }

    private void constantPoolTagContext(final ConstantPoolTagContext context) {
        addTypeContext(context.value());
    }

    private void frameTypeContext(final FrameTypeContext context) {
        addTypeContext(context.value());
    }

    private void structureContext(final StructureContext context, final int stepIndex) {
        final String[] fieldNames = context.value();
        for (int i = fieldNames.length - 1; 0 <= i; --i) {
            addFieldContext(fieldNames[i], stepIndex);
        }
    }

    private void addSubscriptContext(final int subscript) {
        contextPath.add(new SubscriptEntry(subscript));
    }

    private void addFieldContext(final String fieldName, final int stepIndex) {
        contextPath.add(new FieldEntry(replaceSymbol(fieldName, stepIndex)));
    }

    private void addTypeContext(final Object value) {
        contextPath.add(new TypeEntry(value.toString()));
    }

    private String replaceSymbol(final String fieldName, final int stepIndex) {
        final int n = fieldName.length();
        if (n < 4) {
            return fieldName;
        } else if ('$' != fieldName.charAt(0) || '{' != fieldName.charAt(1) ||
                '}' != fieldName.charAt(n - 1)) {
            return fieldName;
        } else {
            final String symbolName = fieldName.substring(1, n - 1);
            return range(0, stepIndex)
                    .mapToObj(i -> contextAnnotationsSteps[i])
                    .flatMap(Arrays::stream)
                    .flatMap(ContextPathTracer::contextSymbolsFor)
                    .filter(symbol -> symbolName.equals(symbol.name()))
                    .map(ContextSymbol::value)
                    .findFirst()
                    .orElseThrow(() -> cantTraceContextPath("no value for symbol name '%s' is available at step %d (path so far: %s)",
                            symbolName, stepIndex, contextPath));
        }
    }

    private static Predicate<StackFrame> hasClassName(final Class<?> clazz) {
        final String className = clazz.getName();
        return stackFrame -> className.equals(stackFrame.getClassName());
    }

    private static final Set<Class<? extends Annotation>> CONTEXT_ANNOTATION_CLASS_SET = Set
            .of(ArrayContext.class, AttributeTypeContext.class, ConstantPoolTagContext.class,
                ContextSymbol.class, ContextSymbols.class, FrameTypeContext.class,
                StructureContext.class);

    private static final class ClassContextAnnotations {
        final Class<?> clazz;
        final ConcurrentHashMap<String, Annotation[]> contextAnnotationsForMethodName;
        ClassContextAnnotations(final Class<?> clazz) {
            this.clazz = clazz;
            this.contextAnnotationsForMethodName = new ConcurrentHashMap<>();
        }
        static ClassContextAnnotations mergeIntoFirst(final ClassContextAnnotations first, final ClassContextAnnotations second) {
            assert first.clazz == second.clazz;
            first.contextAnnotationsForMethodName
                    .putAll(second.contextAnnotationsForMethodName);
            return first;
        }
        Annotation[] getContextAnnotationsForMethodName(final String methodName) {
            Annotation[] contextAnnotations = contextAnnotationsForMethodName.get(methodName);
            if (null != contextAnnotations) {
                return contextAnnotations;
            } else {
                final Annotation allNamedMethodsAnnotations[][] = stream(clazz.getDeclaredMethods())
                    .filter(method -> methodName.equals(method.getName()))
                    .map(Method::getDeclaredAnnotations)
                    .map(annotations -> stream(annotations)
                        .filter(annotation -> CONTEXT_ANNOTATION_CLASS_SET.contains(annotation.annotationType()))
                        .toArray(Annotation[]::new))
                    .filter(annotations -> 0 < annotations.length)
                    .toArray(Annotation[][]::new);
                if (1 == allNamedMethodsAnnotations.length) {
                    contextAnnotations = allNamedMethodsAnnotations[0];
                } else if (0 == allNamedMethodsAnnotations.length) {
                    contextAnnotations = new Annotation[0];
                } else if (1 < allNamedMethodsAnnotations.length) {
                    throw cantTraceContextPath("class %s has multiple context-annotated methods named '%s'",
                            clazz, methodName);
                }
                contextAnnotationsForMethodName.put(methodName, contextAnnotations);
                return contextAnnotations;
            }
        }
    }

    // { class name -> { method name -> [context annotations] } }
    private static final ConcurrentHashMap<String, ClassContextAnnotations> classContextAnnotationsForClassName = new ConcurrentHashMap<>();

    private static ClassContextAnnotations getClassContextAnnotations(final String className) {
        final ClassContextAnnotations classContextAnnotations = classContextAnnotationsForClassName.get(className);
        if (null != classContextAnnotations) {
            return classContextAnnotations;
        } else {
            final Class<?> clazz;
            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException(e);
            }
            return classContextAnnotationsForClassName.merge(className, new ClassContextAnnotations(clazz),
                    ClassContextAnnotations::mergeIntoFirst);
        }
    }

    private static Annotation[] getContextAnnotationsForFrame(final StackFrame stackFrame) {
        final ClassContextAnnotations classContextAnnotations = getClassContextAnnotations(stackFrame.getClassName());
        return classContextAnnotations.getContextAnnotationsForMethodName(stackFrame.getMethodName());
    }

    private static Stream<ContextSymbol> contextSymbolsFor(final Annotation annotation) {
        if (annotation instanceof ContextSymbol) {
            return contextSymbolFor((ContextSymbol)annotation);
        } else if (annotation instanceof ContextSymbols) {
            return contextSymbolsFor((ContextSymbols)annotation);
        } else {
            return Stream.empty();
        }
    }

    private static Stream<ContextSymbol> contextSymbolFor(final ContextSymbol contextSymbol) {
        return Stream.of(contextSymbol);
    }

    private static Stream<ContextSymbol> contextSymbolsFor(final ContextSymbols contextSymbols) {
        return stream(contextSymbols.value());
    }

    private static io.tarro.base.InternalError cantTraceContextPath(final String format, final Object... args) {
        return internalError("Can't trace context path because " + format, args);
    }

    private static void reverse(final int[] array) {
        range(0, array.length / 2).forEach(i -> swap(array, i, array.length - i - 1));
    }

    private static void swap(final int[] array, final int a, final int b) {
        final int c = array[a];
        array[a] = array[b];
        array[b] = c;
    }
}
