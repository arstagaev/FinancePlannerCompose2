package ui

import Consts.saldoState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private fun  ArrayList<ArrayList<Int?>>.safeDeletex(y: Int, value: Int, andFuture: Boolean = false) {
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
private val _raw = MutableStateFlow(
    arrayListOf<ArrayList<Int?>>(
        arrayListOf(10,10,10,10,10,-110),
        arrayListOf(10,10,10,10,10,10),
        arrayListOf(-10,-10,-10,10,10,10)
    )
)
val commonList = _raw.asStateFlow()

private val pizdec = MutableSharedFlow<ArrayList<ArrayList<Int>>>()

class ViewState() {
//    var itemObjects = mutableStateListOf<ArrayList<Int>>(
//        arrayListOf(10,10,10,10,10,-110),
//        arrayListOf(10,10,10,10,10,10),
//        arrayListOf(-10,-10,-10,10,10,10)
//    )

//    private val _counter = MutableStateFlow(0) // private mutable state flow
//    val counter = _counter.asStateFlow() // publicly exposed as read-only state flow
    var majorList = arrayListOf<ArrayList<Int>>(
        arrayListOf(10,10,10,10,10,-110),
        arrayListOf(10,10,10,10,10,10),
        arrayListOf(-10,-10,-10,10,10,10)
    )


    var isEditing = mutableStateOf(false)

    fun remove(monthIndex: Int, value: Int,andFuture: Boolean = false) {

        isEditing.value = true
        if (monthIndex < majorList.size) {
            println("safeDelete: ${majorList.joinToString()} before")
            majorList[monthIndex].remove(value)
            //if (majorList[monthIndex].remove(value)) {
//                if (andFuture) {
//                    // remove in another saldo`s
//                    majorList.forEachIndexed { indexY, ints ->
//                        if (indexY != monthIndex) {
//                            majorList[indexY] = ArrayList(majorList[indexY].minus(value))
//                        }
//                    }
//                }
           // }
            //return this
            println("safeDelete: ${majorList.joinToString()}")
        } else {
            //return arrayListOf()
            println("ERROR Y >")
        }
        GlobalScope.launch {
            pizdec.emit(majorList)
        }
        isEditing.value = false
    }
}

var shareViewState = mutableStateOf(ViewState())
var isEditing = mutableStateOf(false)

@Composable
fun Tester() {
   // val viewState by viewState.collectAsState()
    val viewState = remember { shareViewState }
    var listtt = pizdec.collectAsState(
        arrayListOf<ArrayList<Int>>(
            arrayListOf(10,10,10,10,10,-110),
            arrayListOf(10,10,10,10,10,10),
            arrayListOf(-10,-10,-10,10,10,10)
        )
    )
    var asd = arrayListOf<ArrayList<Int?>>()


    LaunchedEffect(saldoState) {
//        pizdec.emit(arrayListOf<ArrayList<Int>>(
//            arrayListOf(10,10,10,10,10,-110),
//            arrayListOf(10,10,10,10,10,10),
//            arrayListOf(-10,-10,-10,10,10,10)
//        ))

        delay(2000)
//        pizdec.emit(arrayListOf<ArrayList<Int>>(
//            arrayListOf(222,10,10,10,10,-110),
//            arrayListOf(10,10,10,10,10,10),
//            arrayListOf(-10)
//        ))
        //asd = listtt.value[1]
        //listtt.value = commonList.value
    }
    Box(modifier = Modifier.fillMaxWidth().clickable {  }) {
        LazyRow {
            itemsIndexed(items = listtt.value, itemContent = { indexParent, itemParent ->
                Box(Modifier.width(300.dp)) {
                    LazyColumn(Modifier.width(300.dp)) {
                        println("-> ${itemParent.joinToString()}")
                        itemsIndexed(items = itemParent, itemContent = { indxStroke, item ->
//                              Box(Modifier.clickable {  }) {
//                                  Text(text = "${item}", fontSize = 20.sp)
//                              }

                            if (item != null && itemParent.size > indxStroke) {
                                subSubItem(indxStroke, item)
//                                Box(Modifier.clickable {
//                                    _raw.value.safeDeletex(1,1)
//                                }) {
//                                    Text(text = "${item}", fontSize = 20.sp)
//                                }
                            }

                        })
                    }
                }
                //subItem(index)
            })
        }
    }
}


@Composable
fun subItem(monthIndex: Int) {
//    val viewState  state
    val viewState = remember { shareViewState }
    var lister = commonList.value[monthIndex]
//    LaunchedEffect(viewState.value.isEditing) {
//        viewState.value = mrr.value
//
//       // println("subItem ${lister.value.joinToString()}")
//    }

//    Box(Modifier.width(300.dp)) {
//        LazyColumn {
//            itemsIndexed(lister.value[monthIndex]) {index, item ->
//                subSubItem(index,item)
//            }
//        }
//    }
}

@Composable
fun subSubItem(monthIndex: Int,item: Int) {
    val viewState = remember { shareViewState }

    Box(Modifier.width(150.dp).clickable {
        viewState.value.remove(monthIndex, item)

        //_raw.value.safeDeletex(y = monthIndex,item)
        //saldoState.value.invalidate()
        //_viewState.value.lister.safeDeletex(index, item)
        //mrr.value.remove(monthIndex,item)
    }) {

        Text(text = "${item}", fontSize = 20.sp)
    }
}