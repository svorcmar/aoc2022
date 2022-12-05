// AOC 2022 5-1
final reader = new BufferedReader(new InputStreamReader(System.in))
final List<String> stacks = []
while (true) {
  final line = reader.readLine()
  if (line[0..1] == ' 1')
    break // " 1 2 3 4 ..."
  for (int i = 1; i < line.length(); i += 4) {
    if (line[i] != ' ') {
      final index = i.intdiv(4)
      if (stacks[index] == null)
        stacks[index] = ""
      stacks[index] += line[i]
    }
  }
}

reader.readLine() // empty line

String line
while ((line = reader.readLine()) != null) {
  final split = line.split(" ") // "move X from Y to Z"
  final x = split[1] as int, y = split[3] as int - 1, z = split[5] as int - 1
  stacks[z] = stacks[y].substring(0, x).reverse() + stacks[z]
  stacks[y] = stacks[y].substring(x)
}

println (stacks.collect { it[0] }.join(""))
