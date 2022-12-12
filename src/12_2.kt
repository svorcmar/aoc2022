import java.util.*

// AOC 2022 12-2
fun main() {
    data class Node(val x: Int, val y: Int, val elevation: Int, val length: Int)

    val input = generateSequence { readlnOrNull() }.toList()
    val heap = PriorityQueue<Node> { a, b -> a.length - b.length }
    val visited = mutableSetOf<Pair<Int, Int>>()
    val end = input.withIndex()
        .find { (_, v) -> v.contains("E") }
        ?.let { (idx, v) -> Node(idx, v.indexOf("E"), 'z'.elevation, 0) }!!
    heap.add(end)
    visited.add(end.x to end.y)
    // dijkstra from the end - an edge exists when the elevation difference is at most 1
    var minLength = Int.MAX_VALUE
    while (heap.isNotEmpty()) {
        val node = heap.remove()
        for (diff in listOf(1 to 0, 0 to 1, -1 to 0, 0 to -1)) {
            val nextX = node.x + diff.first
            val nextY = node.y + diff.second
            if (nextX >= 0 && nextY >= 0 && nextX < input.size && nextY < input[nextX].length) {
                val elevation = when(input[nextX][nextY]) {
                    'E' -> 'z'
                    'S' -> 'a'
                    else -> input[nextX][nextY]
                }.elevation
                if (node.elevation - elevation <= 1 && visited.add(nextX to nextY)) {
                    heap.add(Node(nextX, nextY, elevation, node.length + 1))
                    if (elevation == 0 && node.length + 1 < minLength)
                        minLength = node.length + 1
                }
            }
        }
    }
    print(minLength)
}