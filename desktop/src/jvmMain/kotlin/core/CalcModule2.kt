package core

import Saldo
import SaldoStroke
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.roundToInt
object CalcModule2 {
//    var currentBudgetX =
//        mutableStateListOf<ArrayList<Int?>>(
//            arrayListOf(10,10,10,10,10,-110),
//            arrayListOf(10,10,10,10,10,10),
//            arrayListOf(-10,-10,-10,10,10,10)
//        )

    var currentBudgetX = MutableStateFlow(
        arrayListOf<ArrayList<Int?>>(
            arrayListOf(10,10,10,10,10,-110),
            arrayListOf(10,10,10,10,10,10),
            arrayListOf(-10,-10,-10,10,10,10)
        )
    )
    val currentBudgetOUT = 1//: StateFlow<ArrayList<ArrayList<Int?>>> get() =  arrayListOf()//currentBudgetX
}

var q = arrayListOf<ArrayList<Int?>>(
//    arrayListOf<Int?>(100,100,100,100,-1,-2,-3), // saldo
//    arrayListOf<Int?>(100,100,100,100,-1,-2,-3),
//    arrayListOf<Int?>(100,100,100,100,-1,-2,-3)
    arrayListOf(0,0,0,0,0,0),
    arrayListOf(0,0,0,0,0,0),
    arrayListOf(0,0,0,0,0,0)
)

val _itemBudget = mutableStateListOf<ArrayList<Int?>>(
    arrayListOf(10,10,10,10,10,-110),
    arrayListOf(10,10,10,10,10,10),
    arrayListOf(-10,-10,-10,10,10,10)
)
val itemBudget: List<ArrayList<Int?>> = _itemBudget

private val _words = mutableStateOf(emptyList<String>()) // 1
val words: State<List<String>> = _words                  // 2

fun main() {
    log2()
    CalcModule2.currentBudgetX.value.safeInserting(0,111,true)

//    q.sortedBy { it.size }
//    q.safeInserting(1,777)
//    q.safeInserting(1,777)
//    q.safeInserting(1,777,isConst = true)
//    q.safeUpdate(1,0,888)
   // q.safeDelete(0,4,andFuture = true)
    //    log2()
    println(CalcModule2.currentBudgetX.value.joinToString())
}
fun log2() = Unit// println(">size:${q.size} | ${q.joinToString()}")

// can update or insert cell
fun ArrayList<ArrayList<Int?>>.safeInserting(y: Int, value: Int, isConst: Boolean = false) {

    if (y >= this.size) {
        var newArrayList = arrayListOf<Int?>(value)


        this.add(newArrayList)
    } else {
        this[y].add(value)

        if (isConst) {
            // add in another saldo`s
            this.forEachIndexed { index, ints ->
                if (index != y) {
                    this[index].add(value)
                }
            }
        }
    }
    log2()
}

fun ArrayList<ArrayList<Int?>>.safeUpdate(y: Int,x: Int,value: Int,isConst: Boolean = false): ArrayList<ArrayList<Int?>> {
    log2()
    if (y < this.size) {
        if (x < this[y].size) {

            this[y][x] = value

            if (isConst) {
                // add in another saldo`s
                this.forEachIndexed { index, ints ->
                    if (index != y) {
                        this[index].add(value)
                    }
                }
            }
            return this

        } else {
            println("ERROR X >")
            return arrayListOf()
        }
    }else {
        println("ERROR Y >")
        return arrayListOf()
    }
    log2()
}

fun SnapshotStateList<ArrayList<Int?>>.safeDelete(y: Int,value: Int, andFuture: Boolean = false) {
    log2()
    if (y < this.size) {
        if (this[y].remove(value)) {
            if (andFuture) {
                // remove in another saldo`s
                this.forEachIndexed { indexY, ints ->
                    if (indexY != y) {
                        this[indexY] = ArrayList(this[indexY].minus(value))
                    }
                }
            }
        }
        //return this
        println("safeDelete: ${this.joinToString()}")
    }else {
        //return arrayListOf()
        println("ERROR Y >")
    }
    //return arrayListOf()
    log2()
}

fun ArrayList<ArrayList<Int?>>.safeDeleteByIndex(y: Int,x: Int, andFuture: Boolean = false) {
    log2()
    if (y < this.size) {
        if (x < this[y].size) {
            val element = this[y][x]
            this[y].removeAt(x)
            if (andFuture) {
                // remove in another saldo`s
                this.forEachIndexed { indexY, ints ->
                    if (indexY != y) {
                        this[indexY] = ArrayList(this[indexY].minus(element))
                    }
                }
            }
            println("safeDelete: ${this.joinToString()}")
        } else {
            println("ERROR X >")
        }
    }else {
        println("ERROR Y >")
    }
    log2()
}

fun prep(arr: List<ArrayList<Int?>>): ArrayList<Saldo> {
    var result = arrayListOf<Saldo>()
    arr.forEachIndexed { index, intArrayList ->
        result.add(arrayToSaldo(intArrayList))
    }
    return result
}
fun arrayToSaldo(intArray: ArrayList<Int?>): Saldo {
    var debitArray = arrayListOf<SaldoStroke>()
    var creditArray = arrayListOf<SaldoStroke>()

    intArray.forEachIndexed { index, i ->
        if (i != null)  {
            if (i > 0) {
                debitArray.add(SaldoStroke(0L, i,0,0,Nature.DEBIT))
            } else {
                creditArray.add(SaldoStroke(0L, i,0,0,Nature.CREDIT))
            }
        }

    }

    return Saldo(1,1, wholeStrokes = ArrayList(debitArray+creditArray))
}