package io.tarro.base.attribute

import io.tarro.test.SingleBitMaskConstantContainerTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/**
 * Unit tests for [AttributeType].
 *
 * @author Victor Schappert
 * @since 20171205
 */
class AttributeTypeTest : SingleBitMaskConstantContainerTest<AttributeContext>(
        AttributeContext::class.java) {
    @Test
    fun noneIsZero() {
        Assertions.assertEquals(0, AttributeContext.NONE)
    }
}