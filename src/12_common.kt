// common functions and classes for day 12

data class Node(val x: Int, val y: Int, val elevation: Int, val length: Int)

val Char.elevation: Int
 get() = this - 'a'
