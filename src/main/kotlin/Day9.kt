import java.lang.Long
import java.nio.file.Files

const val WINDOW_SIZE = 25

fun main() {
    val numbers = Files.readAllLines(fromResource("day9"))
        .map { Long.valueOf(it) }

    val invalidNumber = numbers
        .windowed(size = WINDOW_SIZE + 1, step = 1)
        .first {
            it.dropLast(1)
                .let { values -> values.flatMap { i -> values.map { j -> i + j } } }
                .none { n -> n == it.last() }
        }
        .last()

    numbers.mapIndexed { idx, i ->
        numbers.drop(idx + 1)
            .foldWhile(listOf(i), { _, res -> res.sum() <= invalidNumber }) { res, j ->
                res + j
            }
            .also { println(it) }
    }
        .first { it.sum() == invalidNumber }
        .also {
            println("Res:")
            println(it)
            println("${it.minOrNull()} ${it.maxOrNull()}")
            println(it.minOrNull()!! + it.maxOrNull()!!)
        }
}

public inline fun <T, R> Iterable<T>.foldWhile(
    initial: R,
    predicate: (T, R) -> Boolean,
    operation: (acc: R, T) -> R
): R {
    var accumulator = initial
    for (element in this) {
        val res = operation(accumulator, element)
        if (!predicate(element, res)) {
            break
        }
        accumulator = res
    }
    return accumulator
}
