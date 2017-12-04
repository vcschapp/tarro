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

package io.tarro.base.flag;

import io.tarro.base.Valued;
import io.tarro.base.Versioned;

import java.util.EnumSet;

import static java.util.Arrays.stream;
import static java.util.EnumSet.noneOf;

/**
 * Object representing Java class file access flag.
 *
 * @author Victor Schappert
 * @since 20171014
 */
public interface Flag extends Valued, Versioned {

    //
    // PUBLIC METHODS
    //

    String getFlagName();

    //
    // STATIC METHODS
    //

    static <F extends Enum<F> & Flag> EnumSet<F> parse(final Class<F> clazz, final int bits) {
        final EnumSet<F> set = noneOf(clazz);
        for (final F flag : clazz.getEnumConstants()) {
            final int flagBits = flag.getValue();
            if (flagBits == (flagBits & bits)) {
                set.add(flag);
            }
        }
        return set;
    }
}
