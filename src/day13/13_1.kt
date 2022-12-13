package day13

import parsePacket

// AOC 2022 13-1
fun main() {
    println(generateSequence { readlnOrNull() }
        .windowed(3, 3, true) { triple -> triple[0] to triple[1] }
        .map { (str1, str2) -> parsePacket(str1) to parsePacket(str2) }
        .withIndex()
        .filter { (_, elem) -> elem.first < elem.second }
        .sumOf { (i, _) -> i + 1 })
}


