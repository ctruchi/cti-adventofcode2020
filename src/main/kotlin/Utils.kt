import java.nio.file.Paths

fun fromResource(s: String) = Paths.get(object {}.javaClass.getResource(s).path)
