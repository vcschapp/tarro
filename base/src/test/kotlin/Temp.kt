import io.tarro.base.ClassFileVersion
import io.tarro.base.Versioned

fun foo(bar: Versioned) {
    val x: ClassFileVersion = bar.firstVersionSupporting
    println(x.majorVersion)
}