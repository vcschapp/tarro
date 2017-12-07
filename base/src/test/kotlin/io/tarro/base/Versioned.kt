package io.tarro.base

import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.function.Executable

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.lessThanOrEqualTo

/**
 * Asserts that the [class file version numbers][ClassFileVersion] on a
 * versioned object are valid. This means, at a minimum, that the first
 * supporting version may not be `null`; and that the last supporting version
 * must either be empty or identify a version that is equal to or later than
 * the first supporting version.
 *
 * @param versioned Versioned object to assert against
 */
fun assertVersionRangeValid(versioned: Versioned) {
    assertAll("Version range for $versioned",
            Executable { assertNotNull(versioned.firstVersionSupporting) },
            Executable {
                versioned.lastVersionSupporting.ifPresent {
                    assertThat(versioned.firstVersionSupporting.ordinal,
                            lessThanOrEqualTo(it.ordinal))
                }
            }
    )
}