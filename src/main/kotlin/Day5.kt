import Half.LOWER
import Half.UPPER
import java.lang.IllegalStateException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.Comparator.comparingInt

fun main() {
//    generateSequence(0) { it + 1 }
//        .take(128)
//        .map { col ->
//            generateSequence(0) { it + 1 }
//                .take(8)
//                .map { row ->
//
//                }
//        }

    Files.readAllLines(Paths.get("/tmp/input"))
        .map { it.toSeat().toSeatId() }
        .sorted()
//        .sortedWith(
//            comparingInt<Pair<Int, Int>>({ it.first })
//                .thenComparingInt({ it.second })
//        )
        .fold(11) { res, n ->
            if (res + 1 != n) {
                throw IllegalStateException("missing seat: $res, $n")
            }
            n
        }

//        .maxByOrNull { (_, it) -> it.toSeatId() }
//        ?.also { (s, it) ->
//            println("$s -> $it : ${it.toSeatId()}")
//        }
//        ?: throw IllegalStateException()
}

private fun Pair<Int, Int>.toSeatId() = this.let { (row, col) -> row * 8 + col }

private fun String.toSeat(): Pair<Int, Int> =
    substring(0, 7).map { it.rowToPart() }.toRow() to substring(7).map { it.colToPart() }.toCol()

private fun Char.rowToPart() = when (this) {
    'F' -> LOWER
    'B' -> UPPER
    else -> throw IllegalArgumentException("No row: $this")
}

private fun Char.colToPart() = when (this) {
    'L' -> LOWER
    'R' -> UPPER
    else -> throw IllegalArgumentException("No row: $this")
}

private fun List<Half>.toRow() = part(0 to 127)

private fun List<Half>.toCol() = part(0 to 7)

private fun List<Half>.part(initial: Pair<Int, Int>) = fold(initial) { range, part ->
    part.reduce(range)
}.first

enum class Half(val mult: Int) {
    LOWER(0) {
        override fun reduce(range: Pair<Int, Int>) = range.let { (min, max) ->
            min to (min + max) / 2
        }
    },
    UPPER(1) {
        override fun reduce(range: Pair<Int, Int>) = range.let { (min, max) ->
            (min + max) / 2 + 1 to max
        }
    };

    abstract fun reduce(range: Pair<Int, Int>): Pair<Int, Int>
}
