import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    var i = 0;
    Files.readAllLines(Paths.get("/tmp/input"))
        .filterIndexed { idx, _ -> idx % 2 == 0 }
        .map {
            i to it[i % it.length]
                .also {
                    i += 1
                }
        }
        .also {
            it.forEachIndexed { row, (col, c) ->
                println("$row - $col: $c")
            }
        }
        .count { (_, c) -> c == '#' }
        .also {
            println("Result: $it")
        }
}
