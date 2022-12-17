package day15

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

// AOC 2022 15-1
fun main() {
    val targetY = 2000000
    val beaconsColOnTargetRow = mutableSetOf<Int>()
    val segments = generateSequence { readlnOrNull() }.fold(listOf<IntRange>()) { segments, line ->
        val split = line.split(":")
        val s = split[0].split("=").let {
            it[1].split(",")[0].toInt() to it[2].toInt()
        }
        val b = split[1].split("=").let {
            it[1].split(",")[0].toInt() to it[2].toInt()
        }
        if (b.second == targetY)
            beaconsColOnTargetRow.add(b.first)
        val d = abs(s.first - b.first) + abs(s.second - b.second)
        val rem = d - abs(s.second - targetY)
        if (rem > 0) {
            val segment = IntRange(s.first - rem, s.first + rem)
            merge(segments, segment)
        } else {
            // does not reach the target row
            segments
        }
    }
    println(segments.sumOf { it.last - it.first + 1 - beaconsColOnTargetRow.filter { b -> it.contains(b) }.size})
}

private fun merge(
    list: List<IntRange>,
    new: IntRange
): List<IntRange> {
    return list.fold(new to listOf<IntRange>()) { (n, runningResult), s: IntRange ->
        if (s.contains(n.first) || s.contains(n.last) || n.contains(s.first) || n.contains(s.last)) {
            val newSegment = IntRange(min(s.first, n.first), max(s.last, n.last))
            newSegment to runningResult
        } else {
            n to (runningResult + listOf(s))
        }
    }.let { it.second + listOf(it.first) }
}