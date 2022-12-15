package day10

// AOC 2022 10-2
fun main() {
    var cycle = 1
    var x = 1
    generateSequence { readlnOrNull() }.forEach { line ->
        if ("noop" == line) {
            processCycle(cycle, x)
            cycle++
        } else {
            val inc = line.split(" ")[1].toInt()
            processCycle(cycle, x)
            cycle++
            processCycle(cycle, x)
            cycle++
            x += inc
        }
    }
}

private fun processCycle(cycle: Int, x: Int) {
    val displayPos = (cycle - 1).mod(40)
    if (displayPos == 0)
        println()
    if (displayPos == x - 1 || displayPos == x || displayPos == x + 1)
        print("#")
    else
        print(".")
}