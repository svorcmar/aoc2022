package day8

// AOC 2022 8-2
fun main() {
    val input = generateSequence { readlnOrNull() }.toList()
    val matrix = Array(input.size) { Array(input.size) { 0 } }
    val view = Array(input.size) { Array(input.size) { 1 } }
    // fill matrix
    input.withIndex().forEach { line ->
        line.value.trim().withIndex().forEach { t ->
            matrix[t.index][line.index] = t.value.toString().toInt()
        }
    }
    // mappers from cycle indices to matrix indices in all four directions
    val indexExtractors = listOf(
        { outer: Int, inner: Int -> outer to inner },
        { outer: Int, inner: Int -> inner to outer },
        { outer: Int, inner: Int -> matrix.size - inner - 1 to outer },
        { outer: Int, inner: Int -> outer to matrix.size - inner - 1 }
    )
    // process
    for (i in matrix.indices) {
        // initialize edges with 0 score
        indexExtractors.forEach { extractor ->
            extractor(i, 0).let { (x, y) -> view[x][y] = 0 }
        }
        for (j in matrix.indices) {
            indexExtractors.forEach { extractor ->
                val (x, y) = extractor(i, j)
                for (d in 1..matrix.size) {
                    val (xNext, yNext) = extractor(i, j + d)
                    if (xNext.coerceIn(matrix.indices) != xNext || yNext.coerceIn(matrix.indices) != yNext) {
                        // edge - do not count the last difference!
                        view[x][y] *= (d - 1)
                        break
                    } else if (matrix[xNext][yNext] >= matrix[x][y]) {
                        // blocker
                        view[x][y] *= d
                        break
                    }
                }
            }
        }
    }
    println(view.flatten().max())
}