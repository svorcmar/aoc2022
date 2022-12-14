package day14

import kotlin.math.max
import kotlin.math.min

// AOC 2022 14-2
fun main() {
    val empty = 0.toUByte()
    val wall = 1.toUByte()
    val sand = 2.toUByte()
    val source = 500 to 0
    val pathSegments = generateSequence { readlnOrNull() }
        .map { path ->
            path.split(" -> ").map {
                it.split(",").let { n -> n[0].toInt() to n[1].toInt() }
            }.windowed(2)
        }.flatten()
        .map { l -> l[0] to l[1] }
        .toMutableList()
    // add extra segment for base, it must be wide enough so no grain escapes it before the source is clogged
    val maxYorig = max(
        max(pathSegments.maxOfOrNull { it.first.second }!!, pathSegments.maxOfOrNull { it.second.second }!!),
        source.second
    )
    val maxY = maxYorig + 2
    pathSegments.add((source.first - maxY to maxY) to (source.first + maxY to maxY))
    // find grid dimensions and reduce the width
    val minX = min(
        min(pathSegments.minOfOrNull { it.first.first }!!, pathSegments.minOfOrNull { it.second.first }!!),
        source.first
    )
    val maxX = max(
        max(pathSegments.maxOfOrNull { it.first.first }!!, pathSegments.maxOfOrNull { it.second.first }!!),
        source.first
    )
    val mapX = { x: Int -> maxX - x + 1 }
    // initialize input
    val map = Array(maxX - minX + 3 /* L, R margins */) { Array(maxY + 2 /* D margin */) { 0.toUByte() } }
    pathSegments.forEach { (from, to) ->
        for (x in min(from.first, to.first)..max(from.first, to.first)) {
            for (y in min(from.second, to.second)..max(from.second, to.second)) {
                map[mapX(x)][y] = wall
            }
        }
    }
    // simulate
    var step = 0
    outer@ while (true) {
        step++
        var (x, y) = source
        while (true) {
            if (map[mapX(x)][y + 1] == empty) {
                y++
            } else if (map[mapX(x - 1)][y + 1] == empty) {
                x--
                y++
            } else if (map[mapX(x + 1)][y + 1] == empty) {
                x++
                y++
            } else if (x to y != source) {
                map[mapX(x)][y] = sand
                break
            } else {
                println(step)
                break@outer
            }
        }
    }
}


