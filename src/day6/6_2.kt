package day6

// AOC 2022 6-2
fun main() {
    println(readln().windowed(14).withIndex().first { it.value.toSet().size == 14 }.index + 14)
}