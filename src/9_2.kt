import kotlin.math.abs
import kotlin.math.sign

// AOC 2022 9-2
fun main() {
    val tailSet = mutableSetOf<Pair<Int, Int>>()
    val rope = Array(10) { Array(2) { 0 } } // [segment][x/y]
    generateSequence { readlnOrNull() }.map { it.split(" ") }.forEach { line ->
        val dir = line[0]
        val len = line[1].toInt()
        val m = when (dir) {
            "L" -> -1 to 0
            "R" -> 1 to 0
            "U" -> 0 to 1
            "D" -> 0 to -1
            else -> throw Exception()
        }
        for (i in 0 until len) {
            rope[0][0] += m.first
            rope[0][1] += m.second
            for (ri in 1 until rope.size) {
                val dx = rope[ri - 1][0] - rope[ri][0]
                val dy = rope[ri - 1][1] - rope[ri][1]
                val dst = abs(dx) + abs(dy)
                if (dst <= 1 /* too close to move */ || dst == 2 && abs(dx) == 1 /* diagonal */) {
                    // the next segment does not move
                } else {
                    // diagonal or straight move
                    rope[ri][0] += dx.sign
                    rope[ri][1] += dy.sign
                }
            }
            tailSet.add(rope[rope.size - 1][0] to rope[rope.size - 1][1])
        }
    }
    println(tailSet.size)
}