/*
 * MIT License
 *
 * Copyright (c) 2017 Victor Schappert
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

package io.tarro.test

import java.io.BufferedInputStream
import java.lang.System.nanoTime
import java.net.URL
import java.nio.ByteBuffer
import java.nio.ByteBuffer.wrap
import java.util.jar.JarEntry
import java.util.jar.JarInputStream

/**
 * Describes a Java class file loaded from JRE version's runtime library.
 *
 * @author Victor Schappert
 * @since 20171207
 * @property name Name of the class file (*eg* `com/foo/bar/Baz.class`)
 * @property version Source JRE version (a member of the [javaRuntimeVersions]
 *                   list)
 * @property size Size of the class file in bytes
 */
data class ClassFileIdentifier internal constructor(val name: String,
                                                    val version: String,
                                                    val size: Int)

/**
 * Contains the binary data of a Java class file.
 *
 * @author Victor Schappert
 * @since 20171207
 * @property identifier Identifies the class file
 */
class ClassFileData internal constructor(val identifier: ClassFileIdentifier,
                                         data: ByteBuffer) {
    override fun toString() = "$identifier.<data>"

    /**
     * Read-only byte buffer containing the class file data.
     */
    val data: ByteBuffer
        get() = internalData.asReadOnlyBuffer()

    /**
     * Times how long it takes to perform an arbitrary operation on this class
     * file.
     *
     * @param operation Operation to perform
     * @return Time taken to run the operation in nanoseconds
     */
    fun nanotime(operation: (ClassFileData) -> Any): Long {
        val start = nanoTime()
        operation(this)
        return nanoTime() - start
    }

    private val internalData = data
}

/**
 * Provides a sequence of [class file data][ClassFileData] objects from an
 * underlying JAR file stream. This object must be [closed][close] after usage
 * to prevent resource leaks.
 *
 * @author Victor Schappert
 * @since 20171207
 */
class ClassFileDataStream internal constructor (
        private val dataSet: ClassFileDataStreamProvider) : AutoCloseable,
        Iterator<ClassFileData> {

    //
    // INTERFACE: AutoCloseable
    //

    override fun close() = jarStream.close()

    //
    // INTERFACE: Iterator
    //

    override fun hasNext() = null != loadNextIfNeeded()

    override fun next() : ClassFileData {
        val next = loadNextIfNeeded()
        if (null != next) {
            this.next = null
            return next
        } else {
            throw NoSuchElementException("No more classes found")
        }
    }

    //
    // INTERNALS
    //

    private fun loadNextIfNeeded() : ClassFileData? {
        if (null == next) {
            next = loadNextUnconditionally()
        }
        return next
    }

    private fun loadNextUnconditionally() : ClassFileData? {
        var entry = jarStream.nextJarEntry
        while (null != entry) {
            if (entry.isClassFile) {
                return entry.readClassFile()
            } else {
                entry = jarStream.nextJarEntry
            }
        }
        return null
    }

    private val JarEntry.isClassFile: Boolean
        get() = !isDirectory && 6 < name.length && name.endsWith(".class")

    private fun JarEntry.readClassFile(): ClassFileData {
        val uncompressed = ByteArray(size.toInt())
        jarStream.readNBytes(uncompressed, 0, uncompressed.size)
        val descriptor = ClassFileIdentifier(name, dataSet.name, uncompressed.size)
        return ClassFileData(descriptor, wrap(uncompressed))
    }

    private val jarStream = JarInputStream(BufferedInputStream(
            dataSet.jarPath.openStream()))

    private var next: ClassFileData? = null
}

/**
 * Provides the ability to open a [stream of class files][ClassFileDataStream]
 * from [an underlying JAR resource][jarPath].
 *
 * @author Victor Schappert
 * @since 20171207
 * @property name Name of the provider. This is the value that the
 * [class files][ClassFileData] read from the stream have as
 * [their identifier's][ClassFileData.identifier]
 * [version tag][ClassFileIdentifier.version] and will typically be one of the
 * values listed in the global [javaRuntimeVersions] list.
 * @property jarPath URL to the underlying JAR file from which the classes will
 * be provided. The underlying JAR file may itself be contained within a JAR
 * file (this provider is ambivalent as to whether [jarPath] refers to a regular
 * file within the filesystem, an internet location, or a
 * [JAR URL][java.net.JarURLConnection] in the form ` jar:<url>!/{entry}`
 * representing a JAR that is itself contained within another JAR. Modulo any
 * input/output exceptions, this class is able to provide a stream of class
 * files from that source.
 */
class ClassFileDataStreamProvider internal constructor(val name: String,
                                                       val jarPath: URL) {

    //
    // PUBLIC FUNCTIONS
    //

    /**
     * Opens and returns a class file stream on the [underlying JAR][jarPath].
     *
     * The caller is responsible for [closing][ClassFileDataStream.close] the
     * stream.
     *
     * @return Open class file stream
     */
    fun openClassFileDataStream() = ClassFileDataStream(this)

    /**
     * Times an arbitrary [operation] against every class available in the
     * [underlying JAR][jarPath] and returns a map from class identifier to the
     * time, in nanoseconds, the operation took to run against that class.
     *
     * @param operation Any operation on [class file data][ClassFileData]
     * @return Map of {&nbsp;class identifier&nbsp;→ operation time (in
     *         nanoseconds)&nbsp;} for the class
     * @see javaRuntimesNanoTimings
     */
    fun nanotimes(operation: (ClassFileData) -> Unit): Map<ClassFileIdentifier, Long> {
        val nanotimes = mutableMapOf<ClassFileIdentifier, Long>()
        openClassFileDataStream().use {
            it.forEach {
                val nanos = it.nanotime(operation)
                nanotimes.put(it.identifier, nanos)
            }
        }
        return nanotimes
    }

    //
    // ANCESTOR CLASS: Any
    //

    override fun toString() = "$name -> $jarPath"
}

/**
 * List of Java runtime environment versions supported.
 *
 * @see javaRuntimes
 * @see javaRuntimesNanoTimings
 */
val javaRuntimeVersions = listOf("1.1.8.16" , "1.2.2.17", "1.3.1.29",
        "1.4.2.30", "1.5.0.22", "1.6.0.45")

/**
 * Obtains a list of class file providers for each
 * [supported Java runtime version][javaRuntimeVersions].
 *
 * @return List of class file providers, one for each supported Java runtime
 *         version
 * @see javaRuntimesNanoTimings
 */
fun javaRuntimes(): List<ClassFileDataStreamProvider> {
    return javaRuntimeVersions.map {
        ClassFileDataStreamProvider::class.java
                .getResource("/jre/$it/rt.jar")
                ?.let { jarPath -> ClassFileDataStreamProvider(it, jarPath) }
                ?: throw IllegalStateException("Can't find JRE version $it")
    }
}

/**
 * Times an arbitrary [operation] against all class files available from all
 * [supported Java runtime versions][javaRuntimeVersions] and returns a mapping
 * from class identifier to the time it took to run the operation on the class
 * identified.
 *
 * @param operation Any operation on [class file data][ClassFileData]
 * @return Map of {&nbsp;class identifier&nbsp;→ operation time (in
 *         nanoseconds)&nbsp;} for the class
 * @see ClassFileDataStreamProvider.nanotimes
 */
fun javaRuntimesNanoTimings(operation: (ClassFileData) -> Unit): Map<ClassFileIdentifier, Long> {
    return javaRuntimes()
            .map { it.nanotimes(operation) }
            .flatMap { it.entries }
            .associate { it.key to it.value }
}
