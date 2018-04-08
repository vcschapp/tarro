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

package io.tarro.bytecode.parse;

import io.tarro.bytecode.parse.visitor.LookupSwitchVisitor;
import io.tarro.bytecode.parse.visitor.NoOperandInstructionVisitor;
import io.tarro.bytecode.parse.visitor.OneOperandInstructionVisitor;
import io.tarro.bytecode.parse.visitor.TableSwitchVisitor;
import io.tarro.bytecode.parse.visitor.TwoOperandInstructionVisitor;

import static java.util.Objects.requireNonNull;

/**
 * <p>
 * Builder for a {@link ByteCodeParser}.
 * </p>
 *
 * @author Victor Schappert
 * @since 20171127
 */
public final class ByteCodeParserBuilder {

    //
    // DATA
    //

    private LookupSwitchVisitor lookupSwitchVisitor;
    private NoOperandInstructionVisitor noOperandInstructionVisitor;
    private OneOperandInstructionVisitor oneOperandInstructionVisitor;
    private TableSwitchVisitor tableSwitchVisitor;
    private TwoOperandInstructionVisitor twoOperandInstructionVisitor;

    //
    // CONSTRUCTORS
    //

    ByteCodeParserBuilder() {
        lookupSwitchVisitor = (position, defaultValue, numPairs, matchOffsetPairs) -> { };
        noOperandInstructionVisitor = (position, opcode) -> { };
        oneOperandInstructionVisitor = (position, opcode, operand) -> { };
        tableSwitchVisitor = (position, defaultOffset, lowIndex, highIndex, jumpOffsets) -> {};
        twoOperandInstructionVisitor = (position, opcode, operand1, operand2) -> { };
    }

    //
    // PUBLIC METHODS
    //

    /**
     * Makes and returns a new {@link ByteCodeParser} using this builder's
     * current visitor assignments.
     *
     * @return New parser
     */
    public ByteCodeParser build() {
        return new ByteCodeParser(lookupSwitchVisitor, noOperandInstructionVisitor,
                oneOperandInstructionVisitor, tableSwitchVisitor,
                twoOperandInstructionVisitor);
    }

    /**
     * Assigns the visitor which built parsers will use to visit
     * {@link io.tarro.base.bytecode.VariableOperandOpcode#LOOKUPSWITCH
     * lookupswitch} instructions.
     *
     * @param visitor Non-{@code null} visitor
     * @return This builder
     * @throws NullPointerException If {@code visitor} is {@code null}
     */
    public ByteCodeParserBuilder withLookupSwitchVisitor(
            final LookupSwitchVisitor visitor) {
        lookupSwitchVisitor = requireNonNull(visitor, "visitor cannot be null");
        return this;
    }

    /**
     * Assigns the visitor which built parsers will use to visit
     * {@linkplain io.tarro.base.bytecode.NoOperandOpcode no-operand}
     * instructions.
     *
     * @param visitor Non-{@code null} visitor
     * @return This builder
     * @throws NullPointerException If {@code visitor} is {@code null}
     */
    public ByteCodeParserBuilder withNoOperandInstructionVisitor(
            final NoOperandInstructionVisitor visitor) {
        noOperandInstructionVisitor = requireNonNull(visitor, "visitor cannot be null");
        return this;
    }

    /**
     * Assigns the visitor which built parsers will use to visit
     * {@linkplain io.tarro.base.bytecode.OneOperandOpcode one-operand}
     * instructions.
     *
     * @param visitor Non-{@code null} visitor
     * @return This builder
     * @throws NullPointerException If {@code visitor} is {@code null}
     */
    public ByteCodeParserBuilder withOneOperandInstructionVisitor(
            final OneOperandInstructionVisitor visitor) {
        oneOperandInstructionVisitor = requireNonNull(visitor, "visitor cannot be null");
        return this;
    }

    /**
     * Assigns the visitor which built parsers will use to visit
     * {@linkplain io.tarro.base.bytecode.TwoOperandOpcode two-operand}
     * instructions.
     *
     * @param visitor Non-{@code null} visitor
     * @return This builder
     * @throws NullPointerException If {@code visitor} is {@code null}
     */
    public ByteCodeParserBuilder withTwoOperandInstructionVisitor(
            final TwoOperandInstructionVisitor visitor) {
        twoOperandInstructionVisitor = requireNonNull(visitor, "visitor cannot be null");
        return this;
    }

    /**
     * Assigns the visitor which built parsers will use to visit
     * {@link io.tarro.base.bytecode.VariableOperandOpcode#TABLESWITCH
     * tableswitch} instructions.
     *
     * @param visitor Non-{@code null} visitor
     * @return This builder
     * @throws NullPointerException If {@code visitor} is {@code null}
     */
    public ByteCodeParserBuilder withTableSwitchVisitor(
            final TableSwitchVisitor visitor) {
        tableSwitchVisitor = requireNonNull(visitor, "visible cannot be null");
        return this;
    }
}
