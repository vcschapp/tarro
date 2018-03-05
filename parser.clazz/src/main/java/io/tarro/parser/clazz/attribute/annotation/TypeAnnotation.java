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

package io.tarro.parser.clazz.attribute.annotation;

import io.tarro.parser.clazz.attribute.Annotation;
import io.tarro.base.attribute.AttributeType;
import io.tarro.base.attribute.TargetType;

import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Runtime type annotation contained within the
 * {@linkplain AttributeType#RUNTIME_INVISIBLE_TYPE_ANNOTATIONS RuntimeInvisibleTypeAnnnotations}
 * and
 * {@linkplain AttributeType#RUNTIME_VISIBLE_TYPE_ANNOTATIONS RuntimeVisibleTypeAnnnotations}
 * predefined attributes.
 *
 * @author Victor Schappert
 * @since 20171113
 */
public final class TypeAnnotation<TargetInfoType> extends Annotation {

    //
    // DATA
    //

    private final TargetType targetType;
    private final List<TypePathStep> targetPathSteps;
    private final TargetInfoType targetInfo;

    //
    // CONSTRUCTORS
    //

    public TypeAnnotation(final int typeIndex, final ElementValuePair[] elementValuePairs,
            final TargetType targetType,
            final TypePathStep[] targetPathSteps,
            final TargetInfoType targetInfo) {
        super(typeIndex, elementValuePairs);
        this.targetType = requireNonNull(targetType, "targetType cannot be null");
        this.targetPathSteps = List.of(targetPathSteps);
        this.targetInfo = targetInfo; // Maybe null for empty_target types
    }

    //
    // PUBLIC METHODS
    //

    public TargetType getTargetType() {
        return targetType;
    }

    public List<TypePathStep> getTargetPathSteps() {
        return targetPathSteps;
    }

    // todo: annotate this @Nullable or whatevs
    public TargetInfoType getTargetInfo() { return targetInfo; }
}
