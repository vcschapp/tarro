package io.tarro.base.attribute

import io.tarro.test.SingleBitMaskConstantContainerTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * Unit tests for [AttributeContext].
 *
 * @author Victor Schappert
 * @since 20171204
 */
class AttributeContextTest : SingleBitMaskConstantContainerTest<AttributeContext>(
        AttributeContext::class.java) {
    @Test
    fun noneIsZero() {
        assertEquals(0, AttributeContext.NONE)
    }
}