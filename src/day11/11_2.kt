package day11

// AOC 2022 11-2
fun main() {
    val monkeys = generateSequence { readMonkey() }.toList()
    val modulus = monkeys.map { it.divisibilityTest.toLong() }.reduce { a, b -> a * b }
    val activeCounter = Array(monkeys.size) { 0 }
    for (round in 1..10000) {
        for ((index, monkey) in monkeys.withIndex()) {
            activeCounter[index] += monkey.items.size
            for (item in monkey.items) {
                val worry = monkey.operation(item).mod(modulus)
                val target = if (worry.mod(monkey.divisibilityTest) == 0)
                    monkey.targetIfTrue
                else
                    monkey.targetIfFalse
                monkeys[target].items.addLast(worry)
            }
            monkey.items.clear()
        }
    }
    println(activeCounter.sorted().reversed().subList(0, 2).map { it.toLong() }.reduce { a, b -> a * b })
}

