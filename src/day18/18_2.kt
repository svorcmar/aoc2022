package day18

import day18.State.*

// AOC 2022 18-2
fun main() {
    val input = generateSequence { readlnOrNull() }.map { line ->
        line.split(",").map { it.toInt() }.toIntArray()
    }.toList()
    val ranges = (0 until 3).map { coordinate ->
        IntRange(input.minOf { it[coordinate] }, input.maxOf { it[coordinate] })
    }
    val array = Array(ranges[0].length) { Array(ranges[1].length) { Array(ranges[2].length) { NOT_VISITED } } }
    input.forEach { (x, y, z) ->
        array[x - ranges[0].first][y - ranges[1].first][z - ranges[2].first] = BLOCK
    }
    // flood fill
    val neighborDiffs = listOf(
        listOf(0, 0, 1), listOf(0, 0, -1),
        listOf(0, 1, 0), listOf(0, -1, 0),
        listOf(1, 0, 0), listOf(-1, 0, 0)
    )
    var totalClosedSize = 0
    for ((x, plane) in array.withIndex()) {
        for ((y, line) in plane.withIndex()) {
            for (z in line.indices) {
                if (array[x][y][z] == NOT_VISITED) {
                    val (size, closed) = floodFill(array, x, y, z, neighborDiffs)
                    if (closed) {
                        totalClosedSize += size
                    }
                }
            }
        }
    }
    println(getExposedFacesCount(input) - totalClosedSize)
}

private enum class State {
    NOT_VISITED, VISITED, BLOCK
}

private fun floodFill(
    array: Array<Array<Array<State>>>,
    startX: Int, startY: Int, startZ: Int,
    neighborDiffs: List<List<Int>>
): Pair<Int, Boolean> {
    val stack = ArrayDeque<Array<Int>>()
    stack.addFirst(arrayOf(startX, startY, startZ))
    array[startX][startY][startZ] = VISITED
    var closedSpace = true
    var touchingFaces = 0
    while (!stack.isEmpty()) {
        val (x, y, z) = stack.removeFirst()
        array[x][y][z] = VISITED
        for (diff in neighborDiffs) {
            val nx = x + diff[0]
            val ny = y + diff[1]
            val nz = z + diff[2]
            if (nx >= 0 && ny >= 0 && nz >= 0 && nx < array.size && ny < array[nx].size && nz < array[nx][ny].size) {
                if (array[nx][ny][nz] == NOT_VISITED) {
                    stack.addFirst(arrayOf(nx, ny, nz))
                    array[nx][ny][nz] = VISITED
                } else if (array[nx][ny][nz] == BLOCK) {
                    touchingFaces++
                }
            } else {
                closedSpace = false
            }
        }
    }
    return touchingFaces to closedSpace
}

private val IntRange.length: Int
    get() = last - start + 1

