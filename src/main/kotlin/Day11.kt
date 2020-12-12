import java.nio.file.Files

fun main() {
    val lines = Files.readAllLines(fromResource("day11"))
    val length = lines.first().length
    val height = lines.size
    var prevMatrix = Matrix<Char>(length, height) { i, j -> lines[i][j] }

    println(prevMatrix)

    var round = 1
    var matrix = Matrix<Char>(length, height) { i, j -> prevMatrix.computeNewVal(i, j) }
    while (!prevMatrix.toString().equals(matrix.toString())) {
        println("\n___________________________\n")
        round++
        prevMatrix = matrix
        matrix = Matrix<Char>(length, height) { i, j -> prevMatrix.computeNewVal(i, j) }
        println("Round: $round")
        println(matrix)
    }
    println(matrix.all().count { it == '#' })
}

private fun Matrix<Char>.computeNewVal(i: Int, j: Int): Char {
    when (get(i, j)) {
        'L' -> if (adjacent(i, j, { it != '.' }).none { it == '#' }) {
            return '#'
        }
        '#' -> if (adjacent(i, j, { it != '.' }).count { it == '#' } >= 5) {
            return 'L'
        }
    }
    return get(i, j)
}

class Matrix<T>(val length: Int, val height: Int, initialValue: (Int, Int) -> T) {
    private val grid: List<List<T>> =
        generateSequence(0) { it + 1 }
            .take(height)
            .map { i ->
                generateSequence(0) { it + 1 }
                    .take(length)
                    .map { j -> initialValue(i, j) }
                    .toList()
            }
            .toList()

    fun get(i: Int, j: Int) = grid[i][j]

    fun adjacent(i: Int, j: Int, predicate: (T) -> Boolean = { true }): List<T> {
        val list = mutableListOf<T>()

        top(i, j, predicate)
            ?.let { list.add(it) }

        topLeft(i, j, predicate)
            ?.let { list.add(it) }

        topRight(i, j, predicate)
            ?.let { list.add(it) }

        left(j, i, predicate)
            ?.let { list.add(it) }

        right(j, i, predicate)?.let { list.add(it) }

        bottom(i, j, predicate)
            ?.let { list.add(it) }

        bottomLeft(i, j, predicate)
            ?.let { list.add(it) }

        bottomRight(i, j, predicate)
            ?.let { list.add(it) }
        return list.toList()
    }

    private fun bottomRight(i: Int, j: Int, predicate: (T) -> Boolean): T? {
        var k = i + 1
        var l = j + 1
        if (k > height - 1 || l > length - 1) {
            return null
        }
        while (!predicate(grid[k][l])) {
            k++
            l++
            if (k > height - 1 || l > length - 1) {
                return null
            }
        }
        return grid[k][l]
    }

    private fun bottomLeft(i: Int, j: Int, predicate: (T) -> Boolean): T? {
        var k = i + 1
        var l = j - 1
        if (k > height - 1 || l < 0) {
            return null
        }
        while (!predicate(grid[k][l])) {
            k++
            l--
            if (k > height - 1 || l < 0) {
                return null
            }
        }
        return grid[k][l]
    }

    private fun bottom(i: Int, j: Int, predicate: (T) -> Boolean): T? {
        var k = i + 1
        var l = j
        if (k > height - 1) {
            return null
        }
        while (!predicate(grid[k][l])) {
            k++
            if (k > height - 1) {
                return null
            }
        }
        return grid[k][l]
    }

    private fun right(j: Int, i: Int, predicate: (T) -> Boolean): T? {
        var k = i
        var l = j + 1
        if (l > length - 1) {
            return null
        }
        while (!predicate(grid[k][l])) {
            l++
            if (l > length - 1) {
                return null
            }
        }
        return grid[k][l]
    }

    private fun left(j: Int, i: Int, predicate: (T) -> Boolean): T? {
        var k = i
        var l = j - 1
        if (l < 0) {
            return null
        }

        while (!predicate(grid[k][l])) {
            l--
            if (l < 0) {
                return null
            }
        }

        return grid[k][l]
    }

    private fun topRight(i: Int, j: Int, predicate: (T) -> Boolean): T? {
        var k = i - 1
        var l = j + 1
        if (k < 0 || l > length - 1) {
            return null
        }

        while (!predicate(grid[k][l])) {
            k--
            l++
            if (k < 0 || l > length - 1) {
                return null
            }
        }
        return grid[k][l]
    }

    private fun topLeft(i: Int, j: Int, predicate: (T) -> Boolean): T? {
        var k = i - 1
        var l = j - 1
        if (k < 0 || l < 0) {
            return null
        }
        while (!predicate(grid[k][l])) {
            k--
            l--
            if (k < 0 || l < 0) {
                return null
            }
        }
        return grid[k][l]
    }

    private fun top(i: Int, j: Int, predicate: (T) -> Boolean): T? {
        var k = i - 1
        var l = j
        if (k < 0) {
            return null
        }
        while (!predicate(grid[k][l])) {
            k--
            if (k < 0) {
                return null
            }
        }
        return grid[k][l]
    }

    fun all(): List<T> = grid.flatMap { it.toList() }

    override fun toString() = grid.joinToString("\n") { it.joinToString("") }
}
