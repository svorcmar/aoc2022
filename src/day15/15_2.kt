package day15

import day15.Edge.*
import kotlin.math.abs

// AOC 2022 15-2
fun main() {
    val maxCoordinate = 4000000
    val squares = generateSequence { readlnOrNull() }.map { line ->
        val split = line.split(":")
        val s = split[0].split("=").let {
            it[1].split(",")[0].toInt() to it[2].toInt()
        }
        val b = split[1].split("=").let {
            it[1].split(",")[0].toInt() to it[2].toInt()
        }
        BlockedSquare(s.first, s.second, b.first, b.second)
    }.toList()
    // find all candidate parameters
    val candidatesNWSE = squares.filter { lowerNW ->
        val pNW = lowerNW.getEdgeParam(NW)
        squares.any { upperSE -> pNW + 2 == upperSE.getEdgeParam(SE) }
    }.map { sq -> sq.getEdgeParam(NW) + 1 }.toSet()
    val candidatesNESW = squares.filter { lowerNE ->
        val pNE = lowerNE.getEdgeParam(NE)
        squares.any { upperSW -> pNE + 2 == upperSW.getEdgeParam(SW) }
    }.map { sq -> sq.getEdgeParam(NE) + 1 }.toSet()
    // find intersections between all pairs of lines; check constraint for each of them
    p0@ for (p0 in candidatesNWSE) {
        p1@ for (p1 in candidatesNESW) {
            val d = p1 - p0
            // we are only interested in integer coordinates
            if (d % 2 == 1)
                continue
            val x = d / 2
            val y = x + p0
            if (x < 0 || y < 0 || x > maxCoordinate || y > maxCoordinate)
                continue
            for (square in squares) {
                if (square.contains(x, y))
                    continue@p1
            }
            println(4000000L * x + y)
            break@p0
        }
    }
}

private enum class Edge {
    NW, NE, SW, SE
}

private data class BlockedSquare(val sx: Int, val sy: Int, val bx: Int, val by: Int) {
    val radius: Int
        get() = distance(sx, sy, bx, by)

    fun contains(x: Int, y: Int) = distance(x, y, sx, sy) <= radius

    fun getEdgeParam(e: Edge) = when (e) {
        NW -> sy - sx + radius
        NE -> sy + sx + radius
        SW -> sy + sx - radius
        SE -> sy - sx - radius
    }
}

private fun distance(ax: Int, ay: Int, bx: Int, by: Int) = abs(ax - bx) + abs(ay - by)
