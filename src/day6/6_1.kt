package day6

// AOC 2022 6-1
fun main() {
    println(readln().windowed(4).withIndex().first { it.value.toSet().size == 4 }.index + 4)
}