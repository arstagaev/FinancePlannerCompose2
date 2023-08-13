package ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

private val waterFall = MutableSharedFlow<ArrayList<ArrayList<Int>>>()
var stateHolder = arrayListOf<ArrayList<Int>>(
    arrayListOf(10,10,10,10,10,-110),
    arrayListOf(10,10,10,10,10,10),
    arrayListOf(-10,-10,-10,10,10,10)
)
fun remover(month: Int, value: Int) {
    if (stateHolder.size > month) {

        if (stateHolder[month].contains(value)) {

            stateHolder[month].remove(value)
            GlobalScope.launch {
                waterFall.emit(arrayListOf())
                waterFall.emit(stateHolder)
                println("-> ${stateHolder.joinToString()}")
            }
        }


    }

}

var insMut = mutableStateOf(0)

@Composable
fun TesterThree() {
    val col = waterFall.collectAsState(
        arrayListOf<ArrayList<Int>>(
            arrayListOf(10,10,10,10,10,-110),
            arrayListOf(10,10,10,10,10,10),
            arrayListOf(-10,-10,-10,10,10,10)
        )
    )
    val mnth = remember { insMut }

    LaunchedEffect(insMut.value) {
//        waterFall.emit(
//            arrayListOf<ArrayList<Int>>(
//                arrayListOf(10,10,10,10,10,-110),
//                arrayListOf(10,10,10,10,10,10),
//                arrayListOf(-10,-10,-10,10,10,10)
//            )
//        )
//        delay(1000)
//        waterFall.emit(
//            arrayListOf<ArrayList<Int>>(
//                arrayListOf(10,10,0,-110),
//                arrayListOf(10,10,10,10),
//                arrayListOf(-10,-10,-10,10,10,10)
//            )
//        )
//        delay(1000)
//        waterFall.emit(
//            arrayListOf<ArrayList<Int>>(
//                arrayListOf(10,0,-1),
//                arrayListOf(10,10,1),
//                arrayListOf(-10,444,10,10)
//            )
//        )
//        delay(1000)
//        waterFall.emit(
//            arrayListOf<ArrayList<Int>>(
//                arrayListOf(10,0,-1),
//                arrayListOf(10,10,1),
//                arrayListOf(-10,10)
//            )
//        )
//        delay(1000)
    }

    Box(Modifier.fillMaxWidth()) {
        LazyRow {
            col.value.forEachIndexed { parentIndex, parentItem ->
                item {
                    Box(Modifier.width(200.dp)) {
                        LazyColumn {

                            parentItem.forEach {
                                item {
                                    println("$it  ${parentItem.joinToString()}")
                                    Row(Modifier.clickable {
                                        remover(parentIndex, it)
//                                GlobalScope.launch {
//                                    waterFall.emit(
//                                        arrayListOf<ArrayList<Int>>(
//                                            arrayListOf(10),
//                                            arrayListOf(10,10,10),
//                                            arrayListOf(-10,10,10)
//                                        )
//                                    )
//                                }
                                    }) {
                                        Text("pip ${it}")
                                    }
                                }
                            }
                        }
                    }
                }
            }
//            itemsIndexed(items = col.value, itemContent = { parentIndex, parentItem ->
//                //if (parentItem.size > parentIndex) {
//
//                //}
//
//            })
        }
    }
}