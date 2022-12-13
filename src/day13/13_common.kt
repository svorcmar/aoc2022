// common functions and classes for day 13
fun parsePacket(str: String): Elem<*> {
    return parsePacketElem(str, 0).first
}

private fun parsePacketElem(str: String, startIdx: Int): Pair<Elem<*>, Int> {
    return when (str[startIdx]) {
        '[' -> parsePacketListContent(str, startIdx + 1).let { it.first to it.second + 1 }
        else -> {
            val number = str.substring(startIdx).split(Regex("[^0-9]"), 2)[0]
            Elem.IntElem(number.toInt()) to number.length
        }
    }
}

private fun parsePacketListContent(str: String, startIdx: Int): Pair<Elem.ListElem, Int> {
    return when (str[startIdx]) {
        ']' -> Elem.ListElem(listOf()) to 1
        else -> {
            val (elem, length) = parsePacketElem(str, startIdx)
            val skippedComma = if (str[startIdx + length] == ',') 1 else 0
            val (tail, tailLength) = parsePacketListContent(str, startIdx + length + skippedComma)
            Elem.ListElem(listOf(elem) + tail.value) to tailLength + length + skippedComma
        }
    }
}

sealed class Elem<out T>(val value: T) : Comparable<Elem<*>> {
    class IntElem(value: Int) :
        Elem<Int>(value) {
        override fun compareTo(other: Elem<*>): Int {
            return when (other) {
                is IntElem -> value - other.value
                is ListElem -> ListElem(listOf(this)).compareTo(other)
            }
        }
    }

    class ListElem(value: List<Elem<*>>) :
        Elem<List<Elem<*>>>(value) {
        override fun compareTo(other: Elem<*>): Int {
            return when (other) {
                is IntElem -> compareTo(ListElem(listOf(other)))
                is ListElem -> compareLists(value, other.value)
            }
        }

        private tailrec fun compareLists(
            list1: List<Elem<*>>,
            list2: List<Elem<*>>
        ): Int {
            if (list1.isEmpty() || list2.isEmpty())
                return list1.size - list2.size
            val cmp = list1[0].compareTo(list2[0])
            if (cmp != 0)
                return cmp
            return compareLists(list1.drop(1), list2.drop(1))
        }
    }
}