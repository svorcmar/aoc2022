// AOC 2022 6-1
println(readLine()!!.windowed(4).withIndex().first { it.value.toSet().size == 4 }.index + 4)