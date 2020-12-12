import java.nio.file.Files
import kotlin.math.abs

fun main() {
    Files.readAllLines(fromResource("day12"))
        .map { Day12Instruction.valueOf(it.substring(0, 1)) to Integer.valueOf(it.substring(1)) }
        .fold(Positions(ship = Position(0, 0), waypoint = Position(1, 10))) { positions, (instruction, value) ->
            instruction.applyTo(positions, value)
                .also {
                    println(it)
                }
        }
        .also {
            println(it)
        }
        .also {
            with(it.ship) {
                println(abs(north) + abs(east))
            }
        }
}

data class Position(
    val north: Int,
    val east: Int,
//    val direction: Direction
)

data class Positions(
    val ship: Position,
    val waypoint: Position
)

enum class Direction {
    NORTH {
        override fun change(value: Int) = when (value) {
            90 -> EAST
            180 -> SOUTH
            270 -> WEST
            else -> throw IllegalArgumentException("Unknown angle $value")
        }
    },
    EAST {
        override fun change(value: Int) = when (value) {
            90 -> SOUTH
            180 -> WEST
            270 -> NORTH
            else -> throw IllegalArgumentException("Unknown angle $value")
        }
    },
    SOUTH {
        override fun change(value: Int) = when (value) {
            90 -> WEST
            180 -> NORTH
            270 -> EAST
            else -> throw IllegalArgumentException("Unknown angle $value")
        }
    },
    WEST {
        override fun change(value: Int) = when (value) {
            90 -> NORTH
            180 -> EAST
            270 -> SOUTH
            else -> throw IllegalArgumentException("Unknown angle $value")
        }
    };

    abstract fun change(value: Int): Direction


}


enum class Day12Instruction {
    N {
        override fun applyTo(positions: Positions, value: Int): Positions =
            positions.copy(waypoint = positions.waypoint.copy(north = positions.waypoint.north + value))
    },
    S {
        override fun applyTo(positions: Positions, value: Int): Positions =
            positions.copy(waypoint = positions.waypoint.copy(north = positions.waypoint.north - value))
    },
    E {
        override fun applyTo(positions: Positions, value: Int): Positions =
            positions.copy(waypoint = positions.waypoint.copy(east = positions.waypoint.east + value))
    },
    W {
        override fun applyTo(positions: Positions, value: Int): Positions =
            positions.copy(waypoint = positions.waypoint.copy(east = positions.waypoint.east - value))
    },
    L {
        override fun applyTo(positions: Positions, value: Int): Positions =
            R.applyTo(positions, 360 - value)
    },
    R {
        override fun applyTo(positions: Positions, value: Int): Positions =
//            position.copy(direction = position.direction.change(value))
            when (value) {
                90 -> positions.copy(
                    waypoint = positions.waypoint.copy(
                        north = -positions.waypoint.east,
                        east = positions.waypoint.north
                    )
                )
                180 -> positions.copy(
                    waypoint = positions.waypoint.copy(
                        north = -positions.waypoint.north,
                        east = -positions.waypoint.east
                    )
                )
                270 -> positions.copy(
                    waypoint = positions.waypoint.copy(
                        north = positions.waypoint.east,
                        east = -positions.waypoint.north
                    )
                )
                else -> throw IllegalArgumentException("Unknown angle $value")
            }
    },
    F {
        override fun applyTo(positions: Positions, value: Int): Positions = positions.copy(
            ship = positions.ship.copy(
                north = positions.ship.north + positions.waypoint.north * value,
                east = positions.ship.east + positions.waypoint.east * value
            )
        )
//            when (position.direction) {
//                NORTH -> position.copy(north = position.north + value)
//                EAST -> position.copy(east = position.east + value)
//                SOUTH -> position.copy(north = position.north - value)
//                WEST -> position.copy(east = position.east - value)
//            }
    };

    abstract fun applyTo(positions: Positions, value: Int): Positions
}
