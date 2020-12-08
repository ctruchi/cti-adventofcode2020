import Instruction.*
import java.nio.file.Files
import kotlin.system.exitProcess

fun main() {
    Files.readAllLines(fromResource("day8"))
        .map {
            "(\\w{3}) (.*)".toRegex().find(it)!!
        }
        .map {
            valueOf(it.groupValues[1]) to Integer.valueOf(it.groupValues[2])
        }
        .let { instructions ->
            instructions.forEachIndexed { index, (instruction, value) ->
                val ret = Program(accumulator = 0,
                    instructions = instructions.toMutableList().apply {
                        set(
                            index, when (instruction) {
                                nop -> jmp
                                jmp -> nop
                                acc -> acc
                            } to value
                        )
                    }
                ).runNext()
                if (ret) {
                    exitProcess(0)
                }
            }
        }
}

enum class Instruction {
    nop,
    acc,
    jmp;
}

data class Program(
    var accumulator: Int,
    val instructions: List<Pair<Instruction, Int>>,
) {
    private var nextInstructionIndex: Int = 0
    private val visitedInstructionIndexess: MutableList<Int> = mutableListOf()

    tailrec fun runNext(): Boolean {
        if (nextInstructionIndex == instructions.size) {
            println("end: $accumulator")
            return true
        } else if (visitedInstructionIndexess.contains(nextInstructionIndex)) {
            println("infinite loop: $accumulator")
            return false
        } else {
            visitedInstructionIndexess.add(nextInstructionIndex)

            instructions[nextInstructionIndex].also { (instruction, value) ->
                println("Executing($nextInstructionIndex) : $instruction $value ")
                nextInstructionIndex++
                when (instruction) {
                    acc -> accumulator += value
                    jmp -> nextInstructionIndex = nextInstructionIndex + value - 1
                }
            }
            return runNext()
        }
    }
}
