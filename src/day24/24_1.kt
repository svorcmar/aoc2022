package day24

import java.util.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
    val dirs = mapOf(
        8 to (0 to -1),
        4 to (0 to 1),
        2 to (-1 to 0),
        1 to (1 to 0)
    )
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
    val steps = lcm(s0.size - 2, s0[0].size - 2)
    // generate environment steps
    for (i in 0 until steps) {
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
    // solve A*
    data class State(
        val row: Int, val col: Int, val envIdx: Int,
        val targetRow: Int, val targetCol: Int
    )

    data class QueueItem(val state: State, val cost: Int) : Comparable<QueueItem> {
        // heuristic cost = real cost + manhattan distance to the target
        private val hCost = cost + abs(state.row - state.targetRow) + abs(state.col - state.targetCol)
        override operator fun compareTo(other: QueueItem): Int {
            return hCost - other.hCost
        }
    }
    val visited = mutableSetOf<State>()
    val initState = State(0, 1, 0, s0.size - 1, s0[0].size - 2)
    visited.add(initState)
    val queue = PriorityQueue<QueueItem>()
    queue.add(QueueItem(initState, 0))
    while (queue.isNotEmpty()) {
        val (state, cost) = queue.remove()
        if (state.row == state.targetRow && state.col == state.targetCol) {
            println(cost)
            break
        }
        val nEnvIdx = (state.envIdx + 1) % steps
        // move
        for ((dr, dc) in dirs.values) {
            val nRow = state.row + dr
            val nCol = state.col + dc
            val newState = State(nRow, nCol, nEnvIdx, state.targetRow, state.targetCol)
            if (nRow > 0 && environment[nEnvIdx][nRow][nCol] == 0 && visited.add(newState)) {
                queue.add(QueueItem(newState, cost + 1))
            }
        }
        // stay
        val newState = state.copy(envIdx = nEnvIdx)
        if (environment[nEnvIdx][state.row][state.col] == 0 && visited.add(newState)) {
            queue.add(QueueItem(newState, cost + 1))
        }
    }
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

