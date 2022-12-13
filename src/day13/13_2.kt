package day13

import parsePacket

// AOC 2022 13-2
fun main() {
    val dividerPacket2 = parsePacket("[[2]]")
    val dividerPacket6 = parsePacket("[[6]]")
    val fullSequence = (generateSequence { readlnOrNull() }
        .filter { it != "" }
        .map { parsePacket(it) }) + dividerPacket2 + dividerPacket6
    println(fullSequence
        .sorted()
        .withIndex()
        .filter { (_, e) -> e == dividerPacket2 || e == dividerPacket6 }
        .map { (i, _) -> i + 1 }
        .reduce { i1, i2 -> i1 * i2 })
}


