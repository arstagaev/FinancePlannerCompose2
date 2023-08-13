package ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

private fun  ArrayList<ArrayList<Int>>.safeDeletex(y: Int, value: Int, andFuture: Boolean = false) {
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
}
//private val _raw = MutableStateFlow(
//        arrayListOf<ArrayList<Int>>(
//            arrayListOf(10,10,10,10,10,-110),
//            arrayListOf(10,10,10,10,10,10),
//            arrayListOf(-10,-10,-10,10,10,10)
//        )
//    )
//val commonList = _raw.asStateFlow()

class ViewState() {
    var itemObjects = mutableStateListOf<ArrayList<Int>>(
        arrayListOf(10,10,10,10,10,-110),
        arrayListOf(10,10,10,10,10,10),
        arrayListOf(-10,-10,-10,10,10,10)
    )

//    private val _counter = MutableStateFlow(0) // private mutable state flow
//    val counter = _counter.asStateFlow() // publicly exposed as read-only state flow


    var isEditing = mutableStateOf(false)

    fun remove(monthIndex: Int, strokeIndex: Int,value: Int,andFuture: Boolean = false) {
        isEditing.value = true
        if (monthIndex < itemObjects.size) {
            if (itemObjects[monthIndex].remove(value)) {
                if (andFuture) {
                    // remove in another saldo`s
                    itemObjects.forEachIndexed { indexY, ints ->
                        if (indexY != monthIndex) {
                            itemObjects[indexY] = ArrayList(itemObjects[indexY].minus(value))
                        }
                    }
                }
            }
            //return this
            println("safeDelete: ${itemObjects.joinToString()}")
        }else {
            //return arrayListOf()
            println("ERROR Y >")
        }
        isEditing.value = false
    }
}

var mrr = mutableStateOf(ViewState())

@Composable
fun Tester() {
   // val viewState by viewState.collectAsState()
    val viewState = remember { mrr }
    LaunchedEffect(viewState.value.isEditing) {
//        while (true) {
//            withFrameMillis {
//                //viewState.
////                println("upd")
//            }
//        }
    }
    Box(modifier = Modifier.fillMaxWidth()) {
        LazyRow {
            itemsIndexed(viewState.value.itemObjects) {index, item ->
                subItem(index)
            }
        }
    }
}


@Composable
fun subItem(monthIndex: Int) {
//    val viewState  state
    val viewState = remember { mrr }
    LaunchedEffect(viewState.value.isEditing) {

    }

    Box(Modifier.width(300.dp)) {
        LazyColumn {
            itemsIndexed(viewState.value.itemObjects) {index: Int, item: Int ->
                Box(Modifier.width(150.dp).clickable {
                    //_viewState.value.lister.safeDeletex(index, item)
                    viewState.value.remove(monthIndex,index,item)
                }) {
                    Text(text = "${item}", fontSize = 20.sp)
                }
            }
        }
    }
}