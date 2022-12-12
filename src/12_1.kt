import java.util.*

// AOC 2022 12-1
fun main() {
    val input = generateSequence { readlnOrNull() }.toList()
    val heap = PriorityQueue<Node> { a, b -> a.length - b.length }
    val visited = mutableSetOf<Pair<Int, Int>>()
    val start = input.withIndex()
        .find { (_, v) -> v.contains("S") }
        ?.let { (idx, v) -> Node(idx, v.indexOf("S"), 'a'.elevation, 0) }!!
    heap.add(start)
    visited.add(start.x to start.y)
    // dijkstra - an edge exists when the elevation difference is at least -1
    while (heap.isNotEmpty()) {
        val node = heap.remove()
        if (input[node.x][node.y] == 'E') {
            println(node.length)
            break
        }
        for (diff in listOf(1 to 0, 0 to 1, -1 to 0, 0 to -1)) {
            val nextX = node.x + diff.first
            val nextY = node.y + diff.second
            if (nextX >= 0 && nextY >= 0 && nextX < input.size && nextY < input[nextX].length) {
                val elevation = when(input[nextX][nextY]) {
                    'E' -> 'z'
                    'S' -> 'a'
                    else -> input[nextX][nextY]
                }.elevation
                if (node.elevation - elevation >= -1 && visited.add(nextX to nextY))
                    heap.add(Node(nextX, nextY, elevation, node.length + 1))
            }
        }
    }
}