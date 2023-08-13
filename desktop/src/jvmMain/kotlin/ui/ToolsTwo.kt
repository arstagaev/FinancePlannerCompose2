package ui

import androidx.compose.runtime.snapshots.SnapshotStateList
import core.log2

fun ArrayList<ArrayList<Int?>>.safeDelete(y: Int, value: Int, andFuture: Boolean = false) {
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