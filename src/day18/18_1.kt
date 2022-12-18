package day18

// AOC 2022 18-1
fun main() {
    val input = generateSequence { readlnOrNull() }.map { line ->
        line.split(",").map { it.toInt() }.toIntArray()
    }.toList()
    println(getExposedFacesCount(input))
}



