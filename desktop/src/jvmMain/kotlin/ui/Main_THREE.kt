package ui

import Consts.saldoState
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import core.CalcModule2
import core.log2
import core.safeInserting
import core.safeUpdate
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
                println("remover-> ${stateHolder.joinToString()}")
            }
        }
    }
}

fun addNew(month: Int, value: Int) {
    stateHolder[month].add(value)
    GlobalScope.launch {
        waterFall.emit(arrayListOf())
        waterFall.emit(stateHolder)
        println("addNew-> ${stateHolder.joinToString()}")
    }
}

fun safeIns(y: Int, value: Int, isConst: Boolean = false) {

    if (y >= stateHolder.size) {
        var newArrayList = arrayListOf<Int>(value)


        stateHolder.add(newArrayList)
    } else {
        stateHolder[y].add(value)
        update()
        if (isConst) {
            // add in another saldo`s
            stateHolder.forEachIndexed { index, ints ->
                if (index != y) {
                    stateHolder[index].add(value)
                }
            }
        }
    }
}

fun update() {
    GlobalScope.launch {
        stateHolder.forEachIndexed { index, ints ->
            stateHolder[index].sortDescending()
        }
        waterFall.emit(arrayListOf())
        waterFall.emit(stateHolder)
        println("addNew-> ${stateHolder.joinToString()}")
    }
}

var insMut = mutableStateOf(0)
var actionSave = mutableStateOf(false)

@Composable
fun TesterThree() {
    val col = waterFall.collectAsState(
        arrayListOf<ArrayList<Int>>(
            arrayListOf(10,10,10,10,10,-110),
            arrayListOf(10,10,10,10,10,10),
            arrayListOf(-10,-10,-10,10,10,10)
        )
    )
    //val mnth = remember { insMut }
    var sldst = remember { saldoState }

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

    Column(
        Modifier//.fillMaxWidth()
    ) {
        AnimatedVisibility(
            saldoState.value.saldoAction == SaldoAction.EDITING
        ) {
            Row(Modifier.fillMaxWidth().height(50.dp).background(Color.Red).clickable {
                saldoState.value = saldoState.value.copy(saldoAction = SaldoAction.SHOW)
                actionSave.value = true

            }, horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
            ) {
//                    Box(Modifier.fillMaxSize().weight(1f).background(Color.Red).clickable {
//
//                    })
                Text("Recalculate", fontSize = 30.sp)
            }
        }
        LazyRow {
            col.value.forEachIndexed { parentIndex, parentItem ->
                item {

                    Box(Modifier.width(200.dp)
                        .background(Color.Gray)) {
                        LazyColumn {

                            parentItem.forEach {
                                item {
                                    Turka(num = it, parentIndex = parentIndex)

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
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Turka(num: Int, parentIndex: Int) {
    var isEditing = remember { mutableStateOf(false) }
    val isShowRemoveIcon = remember { mutableStateOf(false) }
    var saldoStrokeAmount = remember { mutableStateOf("") }
    var actionInsert = remember { actionSave}

    LaunchedEffect(saldoState.value, isShowRemoveIcon.value, actionInsert.value) {
        if (saldoState.value.saldoAction != SaldoAction.EDITING) {

            isEditing.value = false
        } else {

        }

        if (actionInsert.value) {
            saldoStrokeAmount.value.toIntOrNull()?.let { safeIns(parentIndex, value = it) }

//            saldoStrokeAmount.value.toIntOrNull()?.let { addNew(parentIndex, it) }
            actionSave.value = false
        }
        //saldoStrokeAmount = saldoStrokeIn.toString()
        //println("<>>>>pizdec "+_itemBudget[indxMonth].joinToString())
    }

    Box(Modifier//.width(100.dp)
        .background(Color.LightGray)
        .onPointerEvent(PointerEventType.Enter) {
            val position = it.changes.first().position
            //println("posss ${position.toString()}")
            isShowRemoveIcon.value = true
        }.onPointerEvent(PointerEventType.Exit) {
            val position = it.changes.first().position
            //println("posss ${position.toString()}")
            isShowRemoveIcon.value = false
        }
    ) {
        if (isEditing.value) {
            Box(Modifier.size(50.dp).background(Color.Magenta).clickable {
                isEditing.value = !isEditing.value
            }) {
                BasicTextField(
//                colors = BasicTextField.textFieldColors(
//                backgroundColor = Color.White,
//                focusedIndicatorColor =  Color.Transparent, //hide the indicator
//                unfocusedIndicatorColor = Color.Green),
                    modifier = Modifier.fillMaxWidth().height(40.dp).background(Color.Magenta),
                    value = saldoStrokeAmount.value,
                    onValueChange = {
                        saldoStrokeAmount.value = it

                        //println("->>${currentBudgetX.value.joinToString()}")
                        //println("stroke ${saldoStrokeAmount.value} ${CalcModule2.currentBudgetX.value.joinToString()}")
                    },
                    textStyle = TextStyle.Default.copy(fontSize = 15.sp)
                )
            }
        } else {
            //println("$it  ${parentItem.joinToString()}")
            Row(Modifier.clickable {
                saldoState.value = saldoState.value.copy(saldoAction = SaldoAction.EDITING)
                isEditing.value = true
                //remover(parentIndex, num)
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
                Text("pip ${num}")
            }
        }
    }

}

fun String.toIntOrNull() = if (this.isNotBlank() && this.isNotEmpty()) this.toInt() else null