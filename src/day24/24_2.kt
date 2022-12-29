package day24

import java.util.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

private val dirs = mapOf(
    8 to (0 to -1),
    4 to (0 to 1),
    2 to (-1 to 0),
    1 to (1 to 0)
)

// AOC 2022 24-2
fun main() {
    val s0 = generateSequence { readlnOrNull() }.map { line ->
        line.map { char ->
            when (char) {
                '#' -> 16
                '<' -> 8
                '>' -> 4
                '^' -> 2
                'v' -> 1
                '.' -> 0
                else -> throw Exception()
            }
        }.toIntArray()
    }.toList().toTypedArray()

    val environment = mutableListOf<Array<IntArray>>()
    environment.add(s0)
    val period = lcm(s0.size - 2, s0[0].size - 2)
    // generate environment steps
    for (i in 0 until period) {
        val s = environment[i]
        val arr = Array(s.size) {
            IntArray(s[it].size) { 0 }
        }
        for (row in s.indices) {
            for (col in 0 until s[row].size) {
                val v = s[row][col]
                if (v >= 16)
                    arr[row][col] = 16
                else {
                    for ((d, diff) in dirs.entries) {
                        val (dr, dc) = diff
                        if (v and d > 0) {
                            arr[(row + dr - 1 + s.size - 2) % (s.size - 2) + 1][(col + dc - 1 + s[row].size - 2) % (s[row].size - 2) + 1] += d
                        }
                    }
                }
            }
        }
        environment.add(arr)
    }
    // solve A* three times
    val phase1 = aStarSolve(environment, 0, 1, 0, period, s0.size - 1, s0[0].size - 2)
    val phase2 = aStarSolve(environment, s0.size - 1, s0[0].size - 2, phase1 % period, period, 0, 1)
    val phase3 = aStarSolve(environment, 0, 1, (phase1 + phase2) % period, period, s0.size - 1, s0[0].size - 2)
    println(phase1 + phase2 + phase3)
}

private fun aStarSolve(
    environment: List<Array<IntArray>>, startRow: Int, startCol: Int, startEnvIdx: Int, period: Int,
    targetRow: Int, targetCol: Int
): Int {
    data class State(val row: Int, val col: Int, val envIdx: Int)

    data class QueueItem(val state: State, val cost: Int) : Comparable<QueueItem> {
        // heuristic cost = real cost + manhattan distance to the target
        private val hCost = cost + abs(state.row - targetRow) + abs(state.col - targetCol)

        override operator fun compareTo(other: QueueItem): Int {
            return hCost - other.hCost
        }
    }

    val visited = mutableSetOf<State>()
    val initState = State(startRow, startCol, startEnvIdx)
    visited.add(initState)
    val queue = PriorityQueue<QueueItem>()
    queue.add(QueueItem(initState, 0))
    while (queue.isNotEmpty()) {
        val (state, cost) = queue.remove()
        if (state.row == targetRow && state.col == targetCol) {
            return cost
        }
        val nEnvIdx = (state.envIdx + 1) % period
        // move
        for ((dr, dc) in dirs.values) {
            val nRow = state.row + dr
            val nCol = state.col + dc
            val newState = State(nRow, nCol, nEnvIdx)
            if (nRow >= 0 && nCol >= 0 && nRow < environment[nEnvIdx].size && nCol < environment[nEnvIdx][nRow].size &&
                environment[nEnvIdx][nRow][nCol] == 0 && visited.add(newState)
            ) {
                queue.add(QueueItem(newState, cost + 1))
            }
        }
        // stay
        val newState = state.copy(envIdx = nEnvIdx)
        if (environment[nEnvIdx][state.row][state.col] == 0 && visited.add(newState)) {
            queue.add(QueueItem(newState, cost + 1))
        }
    }
    throw IllegalStateException("No path found")
}

private tailrec fun gcd(a: Int, b: Int): Int {
    val min = min(a, b)
    val max = max(a, b)
    if (max % min == 0)
        return min
    return gcd(min, max % min)
}

private fun lcm(a: Int, b: Int): Int {
    return a * b / gcd(a, b)
}
