
package io.tarro.base.bytecode

import io.tarro.test.ConstantContainerTest

/**
 * Unit tests for [OpcodeValue].
 *
 * @author Victor Schappert
 * @since 20171220
 */
class OpcodeValueTest : ConstantContainerTest<OpcodeValue>(OpcodeValue::class)

// TODO: Add asm as a test dependency and reflectively test all the constants
//       against the ones with the same name in asm (or if there's a JDK
//       constant container, do same).