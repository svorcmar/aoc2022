// AOC 2022 8-1
fun main() {
    val input = generateSequence { readlnOrNull() }.toList()
    val matrix = Array(input.size) { Array(input.size) { 0 } }
    val visible = Array(input.size) { Array(input.size) { false } }
    // fill matrix
    input.withIndex().forEach { line ->
        line.value.trim().withIndex().forEach { t ->
            matrix[t.index][line.index] = t.value.toString().toInt()
        }
    }
    // mappers from cycle indices to matrix indices in all four directions
    val indexExtractors = listOf(
        {outer: Int, inner:Int -> outer to inner},
        {outer: Int, inner:Int -> inner to outer},
        {outer: Int, inner:Int -> matrix.size - inner - 1 to outer},
        {outer: Int, inner:Int -> outer to matrix.size - inner - 1}
    )
    // process
    for (i in matrix.indices) {
        indexExtractors.forEach { extractor ->
            var tallest = -1
            for (j in matrix.indices) {
                val (x, y) = extractor(i, j)
                if (matrix[x][y] > tallest) {
                    tallest = matrix[x][y]
                    visible[x][y] = true
                }
            }
        }
    }
    println(visible.flatten().count { it })
}