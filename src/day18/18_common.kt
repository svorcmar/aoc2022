package day18

import kotlin.math.abs

fun getExposedFacesCount(input: List<IntArray>): Int {
    val neighbors = input.withIndex().fold(0) { n, (i, vertex1) ->
        n + input.drop(i + 1).fold(0) { ctr, vertex2 ->
            if (distance(vertex1, vertex2) == 1)
                ctr + 1
            else
                ctr
        }
    }
    return 6 * input.size - 2 * neighbors
}

private fun distance(v1: IntArray, v2: IntArray): Int {
    return abs(v1[0] - v2[0]) + abs(v1[1] - v2[1]) + abs(v1[2] - v2[2])
}
