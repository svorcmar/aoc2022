package day21

// AOC 2022 21-2
fun main() {
    val humanMonkey = "humn"
    val monkeys = generateSequence { readlnOrNull() }.map { line ->
        line.split(":").let { it[0] to it[1].trim() }
    }.toMap().mapValues { (k, v) -> Monkey(k, v) }
    val stack = ArrayDeque<Monkey>()
    // get the operands of root monkey. One of them can be evaluated, other one depends on "humn" monkey.
    val hasToBeEqual = monkeys["root"]!!.expr.split(" ").let { it[0] to it[2] }
    stack.add(monkeys[hasToBeEqual.first]!!)
    stack.add(monkeys[hasToBeEqual.second]!!)
    val dependencies = mutableListOf<Pair<Monkey, Int>>()
    val dependencyNames = mutableSetOf<String>()
    while (stack.isNotEmpty()) {
        val monkey = stack.first()
        if (monkey.value != null) {
            throw IllegalStateException("It is expected that each monkey is depended on at most once!")
        } else if (monkey.name == humanMonkey) {
            dependencies.add(monkey to -1)
            dependencyNames.add(monkey.name)
            stack.removeFirst() // the monkey is processed
        } else if (monkey.expr.matches(Regex("[0-9]+"))) {
            monkey.value = monkey.expr.toLong()
            stack.removeFirst() // the monkey is processed
        } else {
            val tokens = monkey.expr.split(" ")
            val monkey1 = monkeys[tokens[0]]!!
            val monkey2 = monkeys[tokens[2]]!!
            // depends on any operand? This means this monkey was already reached in the stack and so it is processed
            val dependsOnIndex = if (dependencyNames.contains(monkey1.name))
                0
            else if (dependencyNames.contains(monkey2.name))
                1
            else null
            if (dependsOnIndex != null) {
                dependencyNames.add(monkey.name)
                dependencies.add(monkey to dependsOnIndex)
                stack.removeFirst() // the monkey is processed
            } else {
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
    }
    val toEqual = if (monkeys[hasToBeEqual.first]!!.value != null)
        monkeys[hasToBeEqual.first]!!.value
    else
        monkeys[hasToBeEqual.second]!!.value
    println(dependencies.reversed().fold(toEqual!!) { value, (monkey, dependsOnIndex) ->
        if (monkey.name == humanMonkey)
            return@fold value
        val tokens = monkey.expr.split(" ")
        val knownValueIndex = 2 * (1 - dependsOnIndex)
        when (tokens[1]) {
            "+" -> value - monkeys[tokens[knownValueIndex]]!!.value!!
            "*" -> value / monkeys[tokens[knownValueIndex]]!!.value!!
            "-" ->
                if (dependsOnIndex == 0) {
                    value + monkeys[tokens[knownValueIndex]]!!.value!!
                } else {
                    monkeys[tokens[knownValueIndex]]!!.value!! - value
                }

            "/" ->
                if (dependsOnIndex == 0) {
                    value * monkeys[tokens[knownValueIndex]]!!.value!!
                } else {
                    monkeys[tokens[knownValueIndex]]!!.value!! / value
                }

            else -> throw IllegalStateException("Invalid operator: ${tokens[1]}")
        }
    })
}