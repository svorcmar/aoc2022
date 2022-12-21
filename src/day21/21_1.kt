package day21

// AOC 2022 21-1
fun main() {
    val monkeys = generateSequence { readlnOrNull() }.map { line ->
        line.split(":").let { it[0] to it[1].trim() }
    }.toMap().mapValues { (k, v) -> Monkey(k, v) }
    val stack = ArrayDeque<Monkey>()
    stack.addFirst(monkeys["root"]!!)
    while (stack.isNotEmpty()) {
        val monkey = stack.first()
        if (monkey.value != null) {
            throw IllegalStateException("It is expected that each monkey is depended on at most once!")
        } else if (monkey.expr.matches(Regex("[0-9]+"))) {
            monkey.value = monkey.expr.toLong()
            stack.removeFirst() // the monkey is processed
        } else {
            val tokens = monkey.expr.split(" ")
            val monkey1 = monkeys[tokens[0]]!!
            val monkey2 = monkeys[tokens[2]]!!
            val monkey1Value = monkey1.value
            val monkey2Value = monkey2.value
            if (monkey1Value == null) {
                stack.addFirst(monkey1)
            }
            if (monkey2Value == null) {
                stack.addFirst(monkey2)
            }
            if (monkey1Value != null && monkey2Value != null) {
                monkey.value = when (tokens[1]) {
                    "+" -> monkey1Value + monkey2Value
                    "*" -> monkey1Value * monkey2Value
                    "-" -> monkey1Value - monkey2Value
                    "/" -> monkey1Value / monkey2Value
                    else -> throw IllegalStateException("Invalid operator: ${tokens[1]}")
                }
                stack.removeFirst() // the monkey is processed
            }
        }
    }
    println(monkeys["root"]!!.value)
}

