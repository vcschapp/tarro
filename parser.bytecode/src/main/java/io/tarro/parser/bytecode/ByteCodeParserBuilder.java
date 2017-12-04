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

package io.tarro.parser.bytecode;

import io.tarro.parser.bytecode.visitor.NoOperandInstructionVisitor;
import io.tarro.parser.bytecode.visitor.LookupSwitchVisitor;
import io.tarro.parser.bytecode.visitor.OneOperandInstructionVisitor;
import io.tarro.parser.bytecode.visitor.TableSwitchVisitor;
import io.tarro.parser.bytecode.visitor.TwoOperandInstructionVisitor;

import static java.util.Objects.requireNonNull;

/**
 * <p>
 *
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
        tableSwitchVisitor = (position, lo, hi, jumpOffsets) -> { };
        twoOperandInstructionVisitor = (position, opcode, operand1, operand2) -> { };
    }

    //
    // PUBLIC METHODS
    //

    public ByteCodeParser build() {
        return new ByteCodeParser(lookupSwitchVisitor, noOperandInstructionVisitor,
                oneOperandInstructionVisitor, tableSwitchVisitor, twoOperandInstructionVisitor);
    }

    public ByteCodeParserBuilder withLookupSwitchVisitor(final LookupSwitchVisitor lookupSwitchVisitor) {
        this.lookupSwitchVisitor = requireNonNull(lookupSwitchVisitor, "lookupSwitchVisitor cannot be null");
        return this;
    }

    public ByteCodeParserBuilder withNoOperandInstructionVisitor(final NoOperandInstructionVisitor noOperandInstructionVisitor) {
        this.noOperandInstructionVisitor = requireNonNull(noOperandInstructionVisitor, "noOperandInstructionVisitor cannot be null");
        return this;
    }

    public ByteCodeParserBuilder withOneOperandInstructionVisitor(final OneOperandInstructionVisitor oneOperandInstructionVisitor) {
        this.oneOperandInstructionVisitor = requireNonNull(oneOperandInstructionVisitor, "oneOperandInstructionVisitor cannot be null");
        return this;
    }

    public ByteCodeParserBuilder withTwoOperandInstructionVisitor(final TwoOperandInstructionVisitor twoOperandInstructionVisitor) {
        this.twoOperandInstructionVisitor = requireNonNull(twoOperandInstructionVisitor, "twoOperandInstructionVisitor cannot be null");
        return this;
    }

    public ByteCodeParserBuilder withTableSwitchVisitor(final TableSwitchVisitor tableSwitchVisitor) {
        this.tableSwitchVisitor = requireNonNull(tableSwitchVisitor, "tableSwitchVisitor cannot be null");
        return this;
    }
}
