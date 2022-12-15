package day11

// common functions and classes for day 11
data class Monkey(
    val index: Int, val items: ArrayDeque<Long>, val operation: (Long) -> Long, val divisibilityTest: Int,
    val targetIfTrue: Int, val targetIfFalse: Int
)

fun readMonkey(): Monkey? {
    var line = readlnOrNull()
    if (line == "") // skip empty line separator
        line = readlnOrNull()
    if (line == null)
        return null
    return readMonkeyContent(line.split(" ")[1].dropLast(1).toInt())
}

private fun readMonkeyContent(index: Int): Monkey {
    val items = readln().substring("  Starting items: ".length).split(", ").map { it.toLong() }.let { list ->
        ArrayDeque(list)
    }
    val operation = readln().substring("  Operation: new = ".length).split(" ").let { triple ->
        getOperation(triple[0], triple[1], triple[2])
    }
    val divisibilityTest = readln().substring("  Test: divisible by ".length).toInt()
    val targetIfTrue = readln().substring("    If true: throw to monkey ".length).toInt()
    val targetIfFalse = readln().substring("    If false: throw to monkey ".length).toInt()
    return Monkey(index, items, operation, divisibilityTest, targetIfTrue, targetIfFalse)
}

private fun getOperation(operand1: String, operator: String, operand2: String): (Long) -> Long {
    return { v ->
        val op1 = if (operand1 == "old") v else operand1.toLong()
        val op2 = if (operand2 == "old") v else operand2.toLong()
        when (operator) {
            "+" -> op1 + op2
            "*" -> op1 * op2
            else -> throw IllegalArgumentException("Invalid operator: $operator")
        }
    }
}
