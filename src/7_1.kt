// AOC 2022 7-1
fun main() {
    val stack = java.util.ArrayDeque<String>()
    val dirs = mutableMapOf<String, Long>()
    generateSequence { readlnOrNull() }.forEach { line ->
        if (line == "$ cd /") {
            stack.clear()
            stack.push("/")
        } else if (line == "$ cd ..") {
            stack.pop()
        } else if (line.startsWith("$ cd")) {
            val dirname = line.split(" ")[2]
            stack.push(stack.peek() + dirname + '/')
        } else if (line.startsWith("$") || line.startsWith("dir ")) {
            // either ls or directory info, ignore both of those
        } else {
            // file - add its size to every dir currently on stack
            val fileSize = line.split(" ")[0].toLong()
            stack.forEach { dirs[it] = fileSize + dirs.getOrPut(it) { 0 } }
        }
    }
    println(dirs.values.filter { it < 100000 }.sum())
}