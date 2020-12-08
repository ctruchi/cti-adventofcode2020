import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    Files.readString(Paths.get("/tmp/input"))
        .split("\n\n")
        .map {
            it.split("\n")
                .map { it.toSet() }
                .reduce { list1, list2 ->
                    list1.intersect(list2)
                }
        }
        .also {
            println(it.joinToString("\n"))
        }
        .map { it.count() }
        .sum()
        .also { println(it) }
}
