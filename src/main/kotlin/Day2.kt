import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    Files.readAllLines(Paths.get("/tmp/foo"))
        .map {
            "(\\d+)-(\\d+) (\\w): (\\w+)".toRegex().find(it)!!
        }
        .map {
            Params(
                Integer.valueOf(it.groupValues[1]),
                Integer.valueOf(it.groupValues[2]),
                it.groupValues[3][0],
                it.groupValues[4]
            )
        }
        .filter { params ->
            with(params) {
                listOf(password[min - 1], password[max - 1])
                    .filter { it == c }
                    .count() == 1
            }
        }
        .also { println(it.joinToString("\n")) }
        .also { println(it.count()) }
}

private data class Params(
    val min: Int,
    val max: Int,
    val c: Char,
    val password: String
)
