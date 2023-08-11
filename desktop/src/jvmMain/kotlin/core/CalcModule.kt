//package core
//
//import Consts.currentBudget
//import java.util.LinkedList
//import kotlin.math.roundToInt
//
//suspend fun calculate() {
//    currentBudget
//
//
//    //_currentBudget.emit()
//}
//var f = LinkedList<Int>()
//var a = arrayListOf<ArrayList<Int?>>(
//    arrayListOf<Int?>(0,0,0,0), // strokes
//    arrayListOf<Int?>(0,0,0,0), // strokes
//    arrayListOf<Int?>(0,0,0,0), // strokes
////    arrayListOf<Int?>(0,0,0,0), // strokes
////    arrayListOf<Int?>(0,0,0,4),
//    arrayListOf<Int?>(-1,-2,-3,-4),
//    arrayListOf<Int?>(-1,-2,-3,-4),
//    arrayListOf<Int?>(-1,-2,-3,-4)
//)
//
//fun main() {
//    //addRepeatedConst(1,0,100,1.5f)
//    addSingleValue(10,10,777)
//    //println("-> ${a.joinToString()}")
//
//    f.addAll(listOf(1,2,3,4))
//    f[0] = 100
//
//    //println("<>> ${ f.joinToString()}")
//}
////
////      x -->
////  y
////  |
////  V
//
//fun addRepeatedConst(x: Int, y: Int, value: Int, coeff: Float) {
//    addSingleValue(x, y, value)
//    a[y].forEachIndexed { index, cell ->
//        if (x < index) {
//            a[y][index] = (value * coeff).roundToInt()
//        }
//    }
//}
//
//fun addSingleValue(x: Int, y: Int, value: Int) {
//    println()
//
//
//    a.safeInsert(1,6,value)
//    log()
//}
//
//
//fun ArrayList<ArrayList<Int?>>.safeInsert(y: Int,x: Int,value: Int) {
//    log()
//    if (y >= this.size) {
//        var newArrayList = arrayListOf<Int?>(0)
//
//        repeat(x) {
//            if (it == x-1) {
//                newArrayList.add(value)
//            } else {
//                newArrayList.add(0)
//            }
//        }
//        this.add(newArrayList)
//    } else {
//        println("NEED INCREASE Y${y} X${x} size:${this.size}")
//
////        this[y][x] = value
////        if (x >= this[y].size) {
////            this[y].forEachIndexed { index, i ->
////                if (x > index) {
////                    this[y].add(0)
////                } else if (x == index) {
////                    this[y][x] = value
////                }
////            }
////        } else {
////
////        }
//    }
//}
//
//
//fun extensionNewYLine(x: Int) {
//    var newArrayList = arrayListOf<Int?>(0)
//    repeat(x) {
//        newArrayList.add(x)
//    }
//    println("ppp ${newArrayList.joinToString()}")
//    a.add(newArrayList)
//
//}
//
//fun log() = println(">size:${a.size} | ${a.joinToString()}")
//var c = arrayListOf<ArrayList<Int>>(
//    arrayListOf(1,1,1,1), // strokes
//    arrayListOf(2,2,2,2), // strokes
////    arrayListOf<Int>(1,2,3,4),
////    arrayListOf<Int>(1,2,3,4)
//)
//
//
//fun collecter(x: Int, y: Int, value: Int, coeff: Float) {
//    b[y][x] = Cell(value)
//    b[y].forEachIndexed { index, cell ->
//        if (x < index) {
//            b[y][index] = Cell(value, Formula(parentCell = Coord(x,y), coeff = coeff))
//        }
//    }
//}
//
//var b = arrayListOf<ArrayList<Cell>>(
//    arrayListOf(Cell(1),Cell(1),Cell(1),Cell(1)), // strokes
//    arrayListOf(Cell(2),Cell(2),Cell(2),Cell(2)), // strokes
////    arrayListOf<Int>(1,2,3,4),
////    arrayListOf<Int>(1,2,3,4)
//)
//
////fun setupNew(line: Int, )
//data class Cell(
//    var value: Int,
//    var formula: Formula? = null
//)
//
//data class Formula(
//    var parentCell: Coord? = null,
//    var coeff: Float = 1.0f
//)
//
//data class Coord(val x: Int, val y: Int)
