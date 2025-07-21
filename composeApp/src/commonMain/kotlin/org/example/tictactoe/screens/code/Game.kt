package org.example.tictactoe.screens.code

fun easyMode(table : Map<Pair<Int, Int>, String>) : Pair<Int, Int>{
    val myRange = 0..2
    val pos = Pair(myRange.random(), myRange.random())
    val emptyButtons = getEmptyButtons(table)
    if (pos in emptyButtons){
        return pos
    }else{
        return easyMode(table)
    }
}

fun mediumMode(table: Map<Pair<Int, Int>, String>) : Pair<Int, Int>{
    val pos : Pair<Int, Int>? = xelseOisAboutWinning(table)
    if (pos != null)
        return pos

    val middleEmptyPosesSet = getEmptyButtons(getMiddleCenterPosesTable(table))
    if (middleEmptyPosesSet.isNotEmpty()){
        val myRange = middleEmptyPosesSet.indices
        return middleEmptyPosesSet.elementAt(myRange.random())
    }

    return easyMode(table)
}

fun hardMode(table: Map<Pair<Int, Int>, String>) : Pair<Int,Int>{
    val pos : Pair<Int, Int>? = xelseOisAboutWinning(table)
    if (pos != null)
        return pos

    val cornerEmptyPosesSet = getEmptyButtons(getCornerPosesTable(table))
    if (cornerEmptyPosesSet.isNotEmpty()){
        val myRange = cornerEmptyPosesSet.indices
        return cornerEmptyPosesSet.elementAt(myRange.random())
    }

    return easyMode(table)
}


fun impossibleMode(table: Map<Pair<Int, Int>, String>) : Pair<Int,Int> {
   val nonEmptyButtons = table.filter { (key, value) ->
       value != ""
   } //collecting nonempty buttons in order to figure the turns been playing so far
    if (nonEmptyButtons.size == 1){ //well its the first turn
        if (table[getCenterPos()].equals(""))
            return getCenterPos()
        return hardMode(table)
    }

    return mediumMode(table)
}

fun getEmptyButtons(table : Map<Pair<Int, Int>, String>) : Set<Pair<Int, Int>> {
    return table.filter { (key, value) ->
        value == ""
    }.keys
}

private fun getOppositeNumber(num : Int) : Int{
    when(num){
        2 -> return 0
        0 -> return 2
    }
    return 1
}

private fun getOppositePos(pos : Pair<Int, Int>) : Pair <Int, Int>{
    val a = pos.first
    val b = pos.second
    return Pair(getOppositeNumber(a), getOppositeNumber(b))
}

private fun getCenterPos() : Pair<Int, Int> = Pair(1,1)

private fun getMiddleCenterPosesTable(table : Map<Pair<Int, Int>, String>) : Map<Pair<Int, Int>, String>{
    val zero = 0
    val one = 1
    val set = setOf(
        Pair(zero,one),
        getOppositePos(Pair(zero,one)),
        Pair(one, zero),
        getOppositePos(Pair(one, zero))
    )
    return table.filterKeys { key ->
        set.contains(key)
    }
}

private fun getCornerPosesTable(table: Map<Pair<Int, Int>, String>) : Map<Pair<Int, Int>, String>{
    val first = Pair(0,0)
    val second = Pair(0,2)
    val set = setOf(
        first,
        getOppositePos(first),
        second,
        getOppositePos(second)
    )
    return table.filterKeys { key ->
        set.contains(key)
    }
}

private fun checkNextPosPlayerToWin(table: Map<Pair<Int, Int>, String>, player : String) : Pair<Int, Int>? {

    //checking rows
    for (i in 0 until 3) {
        if(table[Pair(i,0)].equals(player) && table[Pair(i,1)].equals(player) && table[Pair(i,2)].equals(""))
            return Pair(i,2)
        if (table[Pair(i,1)].equals(player) && table[Pair(i,2)].equals(player) && table[Pair(i,0)].equals(""))
            return Pair(i,0)
        if (table[Pair(i,0)].equals(player) && table[Pair(i,2)].equals(player) && table[Pair(i,1)].equals(""))
            return Pair(i,1)
    }
    //checking columns
    for (i in 0 until 3) {
        if(table[Pair(0,i)].equals(player) && table[Pair(1,i)].equals(player) && table[Pair(2,i)].equals(""))
            return Pair(2,i)
        if (table[Pair(1,i)].equals(player) && table[Pair(2,i)].equals(player) && table[Pair(0,i)].equals(""))
            return Pair(0,i)
        if (table[Pair(0,i)].equals(player) && table[Pair(2,i)].equals(player) && table[Pair(1,i)].equals(""))
            return Pair(1,i)
    }

    if(table[Pair(0,0)].equals(player) && table[Pair(1,1)].equals(player) && table[Pair(2,2)].equals(""))
        return Pair(2,2)
    if (table[Pair(0,0)].equals(player) && table[Pair(2,2)].equals(player) && table[Pair(1,1)].equals(""))
        return Pair(1,1)
    if (table[Pair(1,1)].equals(player) && table[Pair(2,2)].equals(player) && table[Pair(0,0)].equals(""))
        return Pair(0,0)

    if(table[Pair(2,0)].equals(player) && table[Pair(1,1)].equals(player) && table[Pair(0,2)].equals(""))
        return Pair(0,2)
    if (table[Pair(2,0)].equals(player) && table[Pair(0,2)].equals(player) && table[Pair(1,1)].equals(""))
        return Pair(1,1)
    if (table[Pair(1,1)].equals(player) && table[Pair(0,2)].equals(player) && table[Pair(2,0)].equals(""))
        return Pair(2,0)

    return null
}

private fun xelseOisAboutWinning(table: Map<Pair<Int, Int>, String>) : Pair<Int,Int>?{
    var pos : Pair<Int, Int>? = checkNextPosPlayerToWin(table, "O") //checking if O about to win
    if (pos != null)
        return pos
    pos = checkNextPosPlayerToWin(table, "X") //checking if X about to win
    if (pos != null)
        return pos
    return null
}