// AOC 2022 6-2
println(readLine()!!.windowed(14).withIndex().first { it.value.toSet().size == 14 }.index + 14)