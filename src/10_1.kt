// AOC 2022 10-1
fun main() {
    var cycle = 1
    var x = 1
    var signal = 0
    generateSequence { readlnOrNull() }.forEach {line ->
        signal += processCycle(cycle, x)
        if ("noop" == line) {
            cycle++
        } else {
            val inc = line.split(" ")[1].toInt()
            cycle++
            signal += processCycle(cycle, x)
            cycle++
            x += inc
        }
    }
    println(signal)
}

private fun processCycle(cycle: Int, x: Int): Int {
    return if ((cycle + 20).mod(40) == 0)
        cycle * x
    else
        0
}
